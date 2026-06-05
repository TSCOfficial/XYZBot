package ch.frily.xyzbot.database;

/**
 * The collection of all possible database tables and columns
 */
public abstract class TableOld {
	/**
	 * Actions
	 */
	public static final class Action {
		public static final String SELF = "action";
		public static final String ID = "id";
		public static final String TYPE_ID = "type_id";
		public static final String DATASOURCE_ID = "datasource_id";
	}
	

	
	/**
	 * This method allows to clear ambiguous column calls by using the SQL <code>table.column</code> syntax
	 * @param table The table name
	 * @param column The column name
	 * @return The <code>table.column</code> SQL syntax variant
	 */
	public static String define(String table, String column) {
		return table + "." + column;
	}
}
