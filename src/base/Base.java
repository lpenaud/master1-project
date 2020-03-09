package base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import annotation.AutoIncrement;
import annotation.Column;
import annotation.NotNull;
import annotation.PrimaryKey;
import annotation.Table;
import models.Movie;

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
	
	public static void createTable(Class c) {
		String className = c.getName();
		Table table = (Table) c.getAnnotation(Table.class);
		// c.isAnnotationPresent(Table.class)
		if (table == null) {
			throw new Error(String.format("%s must be annoted by annotation.Table", className));
		}
		StringBuilder builder = new StringBuilder("CREATE TABLE " + table.name() + "(");
		Field fields[] = c.getDeclaredFields();
		for (Field field : fields) {
			builder.append("\n");
			Column col = (Column) field.getAnnotation(Column.class);
			if (col == null) {
				throw new Error(String.format("%s.%s must be annoted by annotation.Column", className, field.getName()));
			}
			builder.append("\t");
			builder.append(field.getName());
			builder.append(" ");
			builder.append(col.type());
			builder.append(",");
		}
		builder.replace(builder.length() - 1, builder.length(), "\n)");
		System.out.println(builder.toString());
	}
	
	public static void main(String[] args) {
		createTable(Movie.class);
//		Base base = new Base("config/config");
//		try {
//			base.open();
//			Statement stm = base.conn.createStatement();
//			stm.executeUpdate("DROP Table IF EXISTS 'MaTable'");
//			stm.executeUpdate(
//				"CREATE TABLE Movie ( \n" + 
//				"	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" + 
//				"	title VARCHAR(255),\n" + 
//				"	releaseDate DATE,\n" + 
//				"	description VARCHAR(255) \n" + 
//				");"
//			);
//			stm.executeUpdate("CREATE TABLE Picture (\n" + 
//					"	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" + 
//					"	pathname VARCHAR(255) \n" + 
//					");"
//			);
//			stm.executeUpdate("CREATE TABLE MoviePicture (\n" + 
//					"	idMovie INT NOT NULL, \n" + 
//					"	idPicture INT NOT NULL,\n" + 
//					"	type VARCHAR(255),\n" + 
//					"	CONSTRAINT FK_idMovie FOREIGN KEY (idMovie) REFERENCES Movie(id), \n" + 
//					"	CONSTRAINT FK_idPicture FOREIGN KEY (idPicture) REFERENCES Picture(id) \n" + 
//					");"
//			);
//			stm.close();
//			base.close();
//		} catch (Exception e) {
//			System.err.println("Erreur !");
//			e.printStackTrace();
//		}
	}
}













