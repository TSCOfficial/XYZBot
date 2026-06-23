package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.managers.channel.ChannelManager;
import net.dv8tion.jda.api.managers.channel.concrete.ForumChannelManager;
import net.dv8tion.jda.api.managers.channel.concrete.NewsChannelManager;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelControl {

    private static ExcelControl instance;

    private static final String SHEET_NAME = "Kanäle";
    private static final String[] HEADERS = {"Sync", "Typ", "Name", "Beschreibung", "Channel-ID"};

    private Workbook workbook;

    public static ExcelControl getInstance() {
        if (instance == null) {
            instance = new ExcelControl();
        }
        return instance;
    }

    /**
     * Generate the Excel file for the channel list.
     * @return
     */
    public FileUpload generateExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        this.workbook = workbook;
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME);

        CellStyle headerStyle = createHeaderStyle();
        CellStyle emptyDescriptionStyle = createEmptyDescriptionStyle();

        writeHeaderRow(sheet, headerStyle);

        Guild guild = EnvResolver.getGuildById(1004035867679129662L);

        List<GuildChannel> channels = guild.getChannels(); // already ordered by position
        int rowIndex = 1;
        for (GuildChannel channel : channels) {
            writeChannelRow(sheet, rowIndex, channel, emptyDescriptionStyle);
            rowIndex++;
        }
        int lastDataRow = rowIndex - 1;

        addSyncDropdown(sheet, lastDataRow);
        addSyncConditionalFormatting(sheet, lastDataRow);
        addTypeDropdown(sheet, lastDataRow);

        // Hide channel-ID column (only used for the import)
        sheet.setColumnHidden(4, true);

        autoSizeColumns(sheet);
        sheet.createFreezePane(0, 1); // Fix header

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        return FileUpload.fromData(out.toByteArray(), "channels.xlsx");
    }

    /**
     * Reads an uploaded channel Excel file and applies every row that has Sync = true
     * back to Discord. Rows with an existing channel-ID (hidden column E) are updated,
     * rows without one are created as new channels using the selected type.
     *
     * @param inputStream the uploaded .xlsx file stream
     * @return a human-readable summary of what was done
     * @throws IOException if the file cannot be read
     */
    public String importExcel(InputStream inputStream) throws IOException {
        Guild guild = EnvResolver.getGuildById(1004035867679129662L);

        List<String> log = new ArrayList<>();
        int created = 0;
        int updated = 0;
        int skipped = 0;

        try (Workbook importWorkbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = importWorkbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                throw new IOException("Das Tabellenblatt \"" + SHEET_NAME + "\" wurde nicht gefunden.");
            }

            // Skip header row (row 0)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (isRowEmpty(row)) {
                    continue;
                }

                boolean sync = "true".equalsIgnoreCase(getCellString(row, 0).trim());
                if (!sync) {
                    skipped++;
                    continue;
                }

                String typeName = getCellString(row, 1).trim();
                String name = getCellString(row, 2).trim();
                String description = getCellString(row, 3).trim().replace("\\n", "\n");
                String channelId = getCellString(row, 4).trim();

                if (name.isBlank()) {
                    log.add("⚠️ Zeile " + (rowIndex + 1) + " übersprungen: kein Name angegeben.");
                    skipped++;
                    continue;
                }

                if (!channelId.isBlank()) {
                    boolean ok = updateExistingChannel(guild, channelId, name, description, log, rowIndex);
                    if (ok) {
                        updated++;
                    } else {
                        skipped++;
                    }
                } else {
                    boolean ok = createNewChannel(guild, typeName, name, description, log, rowIndex);
                    if (ok) {
                        created++;
                    } else {
                        skipped++;
                    }
                }
            }
        }

        StringBuilder summary = new StringBuilder();
        summary.append("✅ Import abgeschlossen.\n")
                .append("• Erstellt: ").append(created).append("\n")
                .append("• Aktualisiert: ").append(updated).append("\n")
                .append("• Übersprungen: ").append(skipped);

        if (!log.isEmpty()) {
            summary.append("\n\n").append(String.join("\n", log));
        }
        return summary.toString();
    }

    /**
     * Updates an existing channel's name and (if supported) its topic.
     */
    private boolean updateExistingChannel(Guild guild, String channelId, String name,
                                          String description, List<String> log, int rowIndex) {
        GuildChannel channel = guild.getGuildChannelById(channelId);
        if (channel == null) {
            log.add("⚠️ Zeile " + (rowIndex + 1) + ": Kanal mit ID " + channelId + " existiert nicht mehr.");
            return false;
        }

        channel.getManager().setName(name).queue();
        if (acceptsTopic(channel) && !description.isBlank()) {
            applyTopic(channel, description);
        }
        return true;
    }

    /**
     * Creates a new channel of the given type with name and (if supported) topic.
     */
    private boolean createNewChannel(Guild guild, String typeName, String name,
                                     String description, List<String> log, int rowIndex) {
        ChannelType type;
        try {
            type = ChannelType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            log.add("⚠️ Zeile " + (rowIndex + 1) + ": ungültiger Kanaltyp \"" + typeName + "\".");
            return false;
        }

        switch (type) {
            case TEXT -> guild.createTextChannel(name)
                    .setTopic(description.isBlank() ? null : description)
                    .queue();
            case NEWS -> guild.createNewsChannel(name)
                    .setTopic(description.isBlank() ? null : description)
                    .queue();
            case FORUM -> guild.createForumChannel(name)
                    .setTopic(description.isBlank() ? null : description)
                    .queue();
            case VOICE -> guild.createVoiceChannel(name).queue();
            case STAGE -> guild.createStageChannel(name).queue();
            case CATEGORY -> guild.createCategory(name).queue();
            default -> {
                log.add("⚠️ Zeile " + (rowIndex + 1) + ": Kanaltyp \"" + typeName
                        + "\" kann nicht erstellt werden.");
                return false;
            }
        }
        return true;
    }

    /**
     * Applies a topic via the channel manager, depending on the concrete channel type.
     */
    private void applyTopic(GuildChannel channel, String description) {
        if (channel instanceof TextChannel textChannel) {
            textChannel.getManager().setTopic(description).queue();
        } else if (channel instanceof NewsChannel newsChannel) {
            newsChannel.getManager().setTopic(description).queue();
        } else if (channel instanceof ForumChannel forumChannel) {
            forumChannel.getManager().setTopic(description).queue();
        }
    }

    /**
     * Returns the trimmed string value of a cell, or empty string if missing.
     */
    private String getCellString(Row row, int colIndex) {
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case NUMERIC -> {
                // IDs are large numbers; avoid scientific notation
                double value = cell.getNumericCellValue();
                yield value == Math.floor(value)
                        ? String.valueOf((long) value)
                        : String.valueOf(value);
            }
            default -> "";
        };
    }

    /**
     * Whether a row has no meaningful content.
     */
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int c = 0; c < HEADERS.length; c++) {
            if (!getCellString(row, c).isBlank()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Define the header style.
     * @return
     */
    private CellStyle createHeaderStyle() {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return headerStyle;
    }

    /**
     * Style for the description cell when no topic is available: only visual,
     * greys the cell out. The actual "no value allowed" restriction is enforced
     * via a data validation rule (see addEmptyOnlyValidation), not via locking,
     * so no sheet protection is required.
     * @return
     */
    private CellStyle createEmptyDescriptionStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * Write header with each column name.
     * @param sheet
     * @param headerStyle
     */
    private void writeHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Write each channel row.
     * @param sheet
     * @param rowIndex
     * @param channel GuildChannel to write
     * @param emptyDescriptionStyle style used for the description cell when no topic exists
     */
    private void writeChannelRow(Sheet sheet, int rowIndex, GuildChannel channel,
                                        CellStyle emptyDescriptionStyle) {
        Row row = sheet.createRow(rowIndex);

        // Sync-Flag
        Cell syncFlagCell = row.createCell(0);
        syncFlagCell.setCellValue("true");

        // Channel type
        Cell channelTypeCell = row.createCell(1);
        channelTypeCell.setCellValue(channel.getType().name());
        addFixedTypeValidation(sheet, rowIndex, 1, channel.getType().name());

        CellStyle channelTypeStyle = workbook.createCellStyle();
        channelTypeStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(217, 217, 217), null));
        channelTypeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        channelTypeCell.setCellStyle(channelTypeStyle);

        // Channel name
        row.createCell(2).setCellValue(channel.getName());

        // Channel topic
        String description = getTopic(channel);
        Cell descriptionCell = row.createCell(3);
        if (description != null && !description.isBlank()) {
            descriptionCell.setCellValue(description);

            CellStyle descriptionStyle = workbook.createCellStyle();
            descriptionStyle.setWrapText(true);
            descriptionStyle.setVerticalAlignment(VerticalAlignment.TOP);
            descriptionCell.setCellStyle(descriptionStyle);
        } else if (acceptsTopic(channel)) {
            descriptionCell.setCellValue("");
        } else {
            // No topics allowed, grey out the cell
            descriptionCell.setCellStyle(emptyDescriptionStyle);
            addEmptyOnlyValidation(sheet, rowIndex, 3);
        }

        // Spalte E (versteckt): Channel-ID für den späteren Re-Import
        row.createCell(4).setCellValue(channel.getId());
    }

    /**
     * Restricts a single cell to only accept an empty value, via a custom formula
     * data validation (ISBLANK). Used for description cells where no topic exists,
     * as an alternative to sheet protection / cell locking.
     * @param sheet
     * @param rowIndex POI row index (0-based)
     * @param colIndex POI column index (0-based)
     */
    private void addEmptyOnlyValidation(Sheet sheet, int rowIndex, int colIndex) {
        DataValidationHelper validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

        String columnLetter = CellReference.convertNumToColString(colIndex);
        String cellRef = columnLetter + (rowIndex + 1);
        DataValidationConstraint constraint =
                validationHelper.createCustomConstraint("ISBLANK(" + cellRef + ")");

        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, colIndex, colIndex);

        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.createErrorBox("Keine Beschreibung möglich",
                "Für diesen Kanal kann kein Topic gesetzt werden.");
        sheet.addValidationData(validation);
    }

    /**
     * Creates the dropdown for the Sync-Flag, with the options true/false.
     * This flag controls whether the channel should be synced or not when uploading in back to Discord.
     */
    private void addSyncDropdown(Sheet sheet, int lastDataRow) {
        if (lastDataRow < 1) {
            return;
        }

        DataValidationHelper validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        DataValidationConstraint constraint =
                validationHelper.createExplicitListConstraint(new String[]{"true", "false"});

        int lastDropdownRow = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
        CellRangeAddressList addressList = new CellRangeAddressList(1, lastDropdownRow, 0, 0);

        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        validation.createErrorBox("Ungültiger Wert", "Bitte nur 'true' oder 'false' wählen.");
        sheet.addValidationData(validation);
    }

    /**
     * Creates the dropdown for the Sync-Flag, with the options true/false.
     * This flag controls whether the channel should be synced or not when uploading in back to Discord.
     */
    private void addTypeDropdown(Sheet sheet, int lastDataRow) {
        if (lastDataRow < 1) {
            return;
        }

        DataValidationHelper validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        DataValidationConstraint constraint =
                validationHelper.createExplicitListConstraint(Arrays.stream(ChannelType.values()).map(Enum::name).toArray(String[]::new));

        int firstDropdownRow = lastDataRow + 1;
        int lastDropdownRow = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
        CellRangeAddressList addressList = new CellRangeAddressList(firstDropdownRow,  lastDropdownRow, 1, 1);
        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        validation.createErrorBox("Ungültiger Wert", String.format("Bitte ein gültigen Kanaltyp angeben: %s", Arrays.stream(ChannelType.values()).map(Enum::name).collect(Collectors.joining(", "))));
        sheet.addValidationData(validation);
    }

    /**
     * Locks the channel type of a single (existing) row so it cannot be changed.
     * The value is fixed to the original type via EXACT cell formula validation,
     * which only accepts the exact (case-sensitive) original value.
     * @param sheet
     * @param rowIndex POI row index (0-based)
     * @param colIndex POI column index (0-based)
     * @param expectedType the channel type that must remain in the cell
     */
    private void addFixedTypeValidation(Sheet sheet, int rowIndex, int colIndex, String expectedType) {
        DataValidationHelper validationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

        String columnLetter = CellReference.convertNumToColString(colIndex);
        String cellRef = columnLetter + (rowIndex + 1);
        DataValidationConstraint constraint =
                validationHelper.createCustomConstraint("EXACT(" + cellRef + ",\"" + expectedType + "\")");

        CellRangeAddressList addressList = new CellRangeAddressList(rowIndex, rowIndex, colIndex, colIndex);

        DataValidation validation = validationHelper.createValidation(constraint, addressList);
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.createErrorBox("Kanaltyp gesperrt",
                "Der Kanaltyp dieser Zeile darf nicht geändert werden (" + expectedType + ").");
        sheet.addValidationData(validation);
    }


    /**
     * Adds conditional formatting to the Sync column: green when "true", red when "false".
     */
    private void addSyncConditionalFormatting(Sheet sheet, int lastDataRow) {
        if (lastDataRow < 1) {
            return;
        }

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        int lastDropdownRow = SpreadsheetVersion.EXCEL2007.getLastRowIndex();
        CellRangeAddress[] region = {new CellRangeAddress(1, lastDropdownRow, 0, 0)};

        ConditionalFormattingRule trueRule =
                sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"true\"");
        PatternFormatting truePattern = trueRule.createPatternFormatting();
        truePattern.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        truePattern.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

        ConditionalFormattingRule falseRule =
                sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"false\"");
        PatternFormatting falsePattern = falseRule.createPatternFormatting();
        falsePattern.setFillBackgroundColor(IndexedColors.RED.getIndex());
        falsePattern.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

        sheetCF.addConditionalFormatting(region, trueRule, falseRule);
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int col = 0; col < HEADERS.length; col++) {
            sheet.autoSizeColumn(col);
        }
        sheet.setColumnWidth(0, 10 * 256);
    }

    /**
     * Whether the given channel can have a topic.
     * @param channel
     * @return True if a topic can be set, false if not.
     */
    private static boolean acceptsTopic(GuildChannel channel) {
        return switch (channel.getType()) {
            case TEXT, FORUM, NEWS -> true;
            default -> false;
        };
    }
    /**
     * Get the channel topic, if available.
     * <p></p>
     * Only Text-/Forum-/News-Channels have a topic.
     * @param channel
     * @return
     */
    private String getTopic(GuildChannel channel) {
        if (channel instanceof TextChannel textChannel) {
            return textChannel.getTopic();
        } else if (channel instanceof ForumChannel forumChannel) {
            return forumChannel.getTopic();
        } else if (channel instanceof NewsChannel newsChannel) {
            return newsChannel.getTopic();
        }
        return null;
    }
}
