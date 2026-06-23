package ch.frily.xyzbot.feature;

import ch.frily.xyzbot.util.EnvKey;
import ch.frily.xyzbot.util.EnvResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.*;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
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
