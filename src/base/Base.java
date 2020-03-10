package base;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import annotation.AutoIncrement;
import annotation.Column;
import annotation.ForeignKey;
import annotation.NotNull;
import annotation.PrimaryKey;
import annotation.Table;
import models.Movie;
import models.MoviePicture;
import models.Picture;

public class Base {
	
	protected String url, user, password;
	protected Connection conn;
	
	static {
		try {
			Class.forName
				("com.mysql.jdbc.Driver").
					newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Base() {
		this("config/config");
	}
	
	public Base(String pathname) {
		ResourceBundle rb = ResourceBundle.getBundle(pathname);
		this.url = rb.getString("url");
		this.user = rb.getString("user");
		this.password = rb.getString("password");
	}
	
	public boolean open() {
		try {
			this.conn = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			System.err.println("Erreur getConnection");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.err.println("Erreur close");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void insert(Model model) {
		try {
			this.open();
			model.insert(this);
		} catch (Exception e) {
			System.err.println("Erreur insert");
			e.printStackTrace();
		}
		try {
			this.clone();
		} catch (Exception e) {
			
		}
	}
	
	public void update(Model model) {
		try {
			this.open();
			model.update(this);
		} catch (Exception e) {
			System.err.println("Erreur update");
			e.printStackTrace();
		}
		try {
			this.clone();
		} catch (Exception e) {
		}
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.conn.prepareStatement(sql);
	}
	
	public void createTable(Class<?> c) throws SQLException {
		String drop = generateDropIfExists(c);
		String create = generateScriptTable(c);
		Statement stm = this.conn.createStatement();
		stm.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
		System.out.println(drop);
		stm.executeUpdate(drop);
		System.out.println(create);
		stm.executeUpdate(create);
		stm.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
		try {
			stm.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static String generateDropIfExists(Class<?> c) {
		Table table = (Table) c.getAnnotation(Table.class);
		if (table == null) {
			throw new Error(String.format("%s must be annoted by annotation.Table", c.getName()));
		}
		return String.format("DROP TABLE IF EXISTS %s", table.name());
	}
	
	private static String generateScriptTable(Class<?> c) {
		String className = c.getName();
		Table table = (Table) c.getAnnotation(Table.class);
		if (table == null) {
			throw new Error(String.format("%s must be annoted by annotation.Table", className));
		}
		PrimaryKeys primaries = new PrimaryKeys(table.name());
		StringBuilder foreignKeys = new StringBuilder("");
		StringBuilder builder = new StringBuilder("CREATE TABLE " + table.name() + "(");
		Field fields[] = c.getDeclaredFields();
		for (Field field : fields) {
			String colName = field.getName();
			builder.append(" ");
			Column col = (Column) field.getAnnotation(Column.class);
			if (col == null) {
				throw new Error(String.format("%s.%s must be annoted by annotation.Column", className, field.getName()));
			}
			builder.append(colName);
			builder.append(" ");
			builder.append(col.type());
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				primaries.addKey(colName);
			}
			ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
			if (foreignKey != null) {
				foreignKeys.append("CONSTRAINT FK_");
				foreignKeys.append(foreignKey.table());
				foreignKeys.append(table.name());
				foreignKeys.append(" FOREIGN KEY (");
				foreignKeys.append(colName);
				foreignKeys.append(") REFERENCES ");
				foreignKeys.append(foreignKey.table());
				foreignKeys.append("(");
				foreignKeys.append(foreignKey.primaryKey());
				foreignKeys.append("),");
			}
			if (field.isAnnotationPresent(NotNull.class)) {
				builder.append(" NOT NULL");
			}
			AutoIncrement increment = field.getAnnotation(AutoIncrement.class);
			if (increment != null) {
				builder.append(" AUTO_INCREMENT");
			}
			builder.append(",");
		}
		builder.append(primaries);
		if (foreignKeys.length() > 0) {
			builder.append(",");
			foreignKeys.replace(foreignKeys.length() - 2, foreignKeys.length(), "");
			builder.append(foreignKeys);
			builder.append(")");
		}
		builder.append(" )");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		Base b = new Base();
		try {
			b.open();
			b.createTable(Movie.class);
			b.createTable(Picture.class);
			b.createTable(MoviePicture.class);
			b.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}













