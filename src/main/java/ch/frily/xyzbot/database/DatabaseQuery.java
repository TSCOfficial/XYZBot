package ch.frily.xyzbot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseQuery {
	// Query table
    private final String table;
	
	// Fields to select
    private final List<String> selectFields = new ArrayList<>();
	
	// Joins to execute
    private final List<String> joins = new ArrayList<>();
	
	// Where clauses to extract only wanted results
    private final List<String> whereClauses = new ArrayList<>();
	
	// Order a select by its value numeral ranking
	private final Map<String, OrderBy> selectOrder = new HashMap<>();
	
	// Parameters to insert into the where clause & update statement
    private final List<Object> params = new ArrayList<>();
	
	// Fields to update
    private final Map<String, Object> updateFields = new LinkedHashMap<>();

    // Default query type
    private QueryType type = QueryType.SELECT;

    /**
     * The possible query types that can be executed
     */
    private enum QueryType {
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }
	
	/**
	 * Define in what order the select query should be executed<br>
	 * <i>Help text is not relevant for programming. Its only goal is to be shown when selecting an enum option in the popup window.</i>
	 */
	public enum OrderBy {
		ASCENDED ("asc", "from low to high, lowest number/character first"),
		DESCENDED ("desc", "from high to low, high number/character first");
		
		private final String name;
		
		OrderBy(String name, String helpText) {
			this.name = name;
		}
		
		private String getName() {
			return name;
		}
	}

    /**
     * Define what type of join is needed
     */
    public enum JoinType {
        NORMAL (""),
        INNER ("INNER"),
        LEFT ("LEFT"),
        RIGHT ("RIGHT"),
        FULL ("FULL");

        private final String name;

        JoinType(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }
    }


    /**
     * All available operators that can be used in a query
     */
    public enum Operator {
        // Comparison
        EQUALS("=" ),
        NOT_EQUALS("<>" ),
        GREATER_THAN(">" ),
        GREATER_OR_EQUAL(">=" ),
        LESS_THAN("<" ),
        LESS_OR_EQUAL("<=" ),

        // Pattern Matching
        LIKE("LIKE" ),
        NOT_LIKE("NOT LIKE" ),
        REGEXP("REGEXP" ),

        // Amounts
        IN("IN" ),
        NOT_IN("NOT IN" ),
        BETWEEN("BETWEEN" ),
        NOT_BETWEEN("NOT BETWEEN" ),

        // Null pointer check
        IS_NULL("IS NULL" ),
        IS_NOT_NULL("IS NOT NULL" ),
        EXISTS("EXISTS" ),
        NOT_EXISTS("NOT EXISTS" );

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        private String getSymbol() {
            return symbol;
        }
    }

    /**
     * Execute a query on the database
     * @param table The database table to execute the query on
     */
    public DatabaseQuery(Table table) {
        this.table = table.getTable();
    }

    /**
     * Define the query as a select
     * @param fields All fields to be selected. If left empty, it selects all ( * )
     * @return The database query
     */
    public DatabaseQuery select(String... fields) {
        selectFields.addAll(Arrays.asList(fields));
        type = QueryType.SELECT;
        return this;
    }

    /**
     * Add a where clause to the query
     * @param column The database column name
     * @param operator The operator to use
     * @param value The value to check for
     * @return The database query
     */
    public DatabaseQuery where(Table.Column column, Operator operator, Object value) {
        whereClauses.add(column + " " + operator.getSymbol() + " ?");
        params.add(value);
        return this;
    }
	
	public DatabaseQuery orderBy(String column, OrderBy order) {
		selectOrder.put(column, order);
		return this;
	}

    /**
     * Define the query as an insert
     * @param fieldName The field name to insert into
     * @param value The value to insert into the field
     * @return The database query
     */
    public DatabaseQuery insert(String fieldName, Object value) {
        type = QueryType.INSERT;
        updateFields.put(fieldName, value);
        return this;
    }

    /**
     * Define the query as an update
     * @param fieldName The field name to update from
     * @param value The value to update the field to
     * @return The database query
     */
    public DatabaseQuery update(String fieldName, Object value) {
        type = QueryType.UPDATE;
        updateFields.put(fieldName, value);
        return this;
    }

    /**
     * Define the query as a <b>destructive</b> delete
     * @return The database query
     */
    public DatabaseQuery delete() {
        type = QueryType.DELETE;
        return this;
    }

    /**
     * Add a <b>normal</b> join from a foreign table to the original table
     * @param table The table to join to
     * @param originalRef The original table column reference
     * @param operator The operator used to join the correct records
     * @param foreignRef The foreign table column reference
     * @return The database query
     */
    public DatabaseQuery join(String table, String originalRef, Operator operator, String foreignRef) {
        return join(JoinType.NORMAL, table, originalRef, operator, foreignRef);
    }

    /**
     * Add a <b>specific</b> join from a foreign table to the original table
     * @param joinType Select the specific join type
     * @param table The table to join to
     * @param originalRef The original table column reference
     * @param operator The operator used to join the correct records
     * @param foreignRef The foreign table column reference
     * @return The database query
     */
    public DatabaseQuery join(JoinType joinType, String table, String originalRef, Operator operator, String foreignRef) {
        joins.add(joinType.getName() + " JOIN " + table + " ON " + originalRef + " " + operator.getSymbol() + " " + foreignRef);
        return this;
    }


    /**
     * Execute the database query
     * @return The database results
     * @throws SQLException Happens when the Database refuses the connection or is not reachable
     */
    public ResultSet executeQuery() throws SQLException {
        Connection conn = Database.getInstance().connect();
        PreparedStatement stmt = conn.prepareStatement(buildSQL());
        setParameters(stmt);
        return stmt.executeQuery();
    }

//    public int executeUpdate() throws SQLException {
//        Connection conn = Database.connect();
//        PreparedStatement stmt = conn.prepareStatement(buildSQL());
//        setParameters(stmt);
//        return stmt.executeUpdate();
//    }

    /**
     * Builds the SQl query depending on the query type
     * @return The built query statement
     */
    private String buildSQL() {
        return switch (type) {
            case SELECT -> buildSelect();
            case INSERT -> buildInsert();
            case UPDATE -> buildUpdate();
            case DELETE -> buildDelete();
        };
    }

    /**
     * Build a select query
     * @return The select query
     */
    private String buildSelect() {
        String fields = selectFields.isEmpty() ? "*" : String.join(", ", selectFields);
        String sql = "SELECT " + fields + " FROM " + table;

        if (!joins.isEmpty()) {
            sql += " " + String.join(" ", joins);
        }

        if (!whereClauses.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", whereClauses);
        }
		
		if (!selectOrder.isEmpty()) {
			List<String> orderArray = selectOrder.entrySet().stream()
				.map(entry -> entry.getKey() + " " + entry.getValue().getName()).toList();
			sql += " ORDER BY " + String.join(" ,", orderArray);
		}
		System.out.println(sql);
        return sql;
    }

    /**
     * Build an insert query
     * @return The insert query
     */
    private String buildInsert() {
        String columns = String.join(", ", updateFields.keySet());
        String queryString = String.join(", ", Collections.nCopies(updateFields.size(), "?"));
        return "INSERT INTO " + table + " (" + columns + ") VALUES (" + queryString + ")";
    }

    /**
     * Build an update query
     * @return The update query
     */
    private String buildUpdate() {
        String setPart = String.join(", ",
                updateFields.keySet().stream().map(key -> key + " = ?").toList()
        );
        String sql = "UPDATE " + table + " SET " + setPart;
        if (!whereClauses.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", whereClauses);
        }
        return sql;
    }

    /**
     * Build a delete query
     * @return The delete query
     */
    private String buildDelete() {
        String sql = "DELETE FROM " + table;
        if (!whereClauses.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", whereClauses);
        }
        return sql;
    }

    /**
     * Insert the parameters in the prepared query statement
     * @param stmt The query statement
     * @throws SQLException Happens when the parameter or field array index is out of range
     */
    private void setParameters(PreparedStatement stmt) throws SQLException {
        int i = 1;
        for (Object value : updateFields.values()) {
            stmt.setObject(i++, value);
        }
        for (Object value : params) {
            stmt.setObject(i++, value);
        }
    }
}

