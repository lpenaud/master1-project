package base;

import java.sql.Types;


public enum DatabaseType {
	Integer("INT", Types.INTEGER),
	Date("DATE", Types.DATE),
	String("VARCHAR(255)", Types.VARCHAR);
	
	private String type;
	private int sql;
	
	private DatabaseType(String type, int sql) {
		this.type = type;
		this.sql = sql;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
	public int sqlType() {
		return this.sql;
	}
}
