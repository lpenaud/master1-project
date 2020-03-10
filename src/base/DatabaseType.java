package base;

public enum DatabaseType {
	Integer("INT"),
	Date("DATE"),
	String("VARCHAR(255)");
	
	private String type;
	
	private DatabaseType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
