package base;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import annotation.AutoIncrement;
import annotation.Column;
import annotation.ForeignKey;
import annotation.NotNull;
import annotation.PrimaryKey;
import annotation.Table;
import config.Config;
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
		this(Config.config);
	}
	
	public Base(Config config) {
		this.url = config.getUrl();
		this.user = config.getUser();
		this.password = config.getPassword();
	}
	
	public boolean isClosed() {
		if (this.conn == null) {
			return true;
		}
		try {
			return this.conn.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
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
	
	private static String getTableName(Class<?> c) {
		Table t = (Table) c.getAnnotation(Table.class);
		if (t == null) {
			throw new Error(String.format("%s must be annoted by annotation.Table", c.getName()));
		}
		return t.name();
	}
	
	public <T> T insert(T object) throws SQLException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		boolean isClosed = this.isClosed();
		String tableName = getTableName(object.getClass());
		Field fields[] = object.getClass().getDeclaredFields();
		Insert insert = new Insert(tableName);
		Field primaryKey = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				primaryKey = primaryKey == null ? field : null;
			}
			if (field.isAnnotationPresent(Column.class) == false) {
				continue;
			}
			try {
				insert.put(field.getName(), field.get(object), field.getAnnotation(Column.class).type().sqlType());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (isClosed) {
			this.open();	
		}
		PreparedStatement statement = this.conn.prepareStatement(insert.toString(), Statement.RETURN_GENERATED_KEYS);
		Value[] values = insert.values();
		for (int i = 0; i < values.length; i++) {
			Value value = values[i];
			statement.setObject(i + 1, value.getValue(), value.getSqlType());
		}
		int result = statement.executeUpdate();
		ResultSet rs = statement.getGeneratedKeys();
		if (rs.next()) {
			result = rs.getInt(1);
		}
		if (primaryKey != null) {
			primaryKey.set(object, result);			
		}
		if (isClosed) {
			this.close();	
		}
		return object;
	}
	
	public <R> List<R> select(String sql, Function<ResultSet, R> function) throws SQLException {
		this.open();
		PreparedStatement statement = this.conn.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		List<R> objects = new ArrayList<>();
		while (rs.next()) {
			objects.add(function.apply(rs));
		}
		statement.close();
		rs.close();
		this.close();
		return objects;
	}
	
	public <R> List<R> select(String sql, Consumer<PreparedStatement> setStatement, Function<ResultSet, R> function) throws SQLException {
		this.open();
		PreparedStatement statement = this.conn.prepareStatement(sql);
		setStatement.accept(statement);
		ResultSet rs = statement.executeQuery();
		List<R> objects = new ArrayList<>();
		while (rs.next()) {
			objects.add(function.apply(rs));
		}
		statement.close();
		rs.close();
		this.close();
		return objects;
	}
	
	public Integer updateOne(Object model) throws SQLException, IllegalArgumentException, IllegalAccessException {
		String tableName = getTableName(model.getClass());
		Update update = new Update(tableName);
		Field[] fields = model.getClass().getFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class) == false) {
				continue;
			}
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				update.putWhere(field, model);
				continue;
			}
			update.putSet(field, model);
		}
		this.open();
		PreparedStatement statement = this.conn.prepareStatement(update.toString());
		int i = 1;
		for (Value s : update.values()) {
			statement.setObject(i++, s.getValue(), s.getSqlType());
		}
		for (Value s : update.where()) {
			statement.setObject(i++, s.getValue(), s.getSqlType());
		}
		Integer res = statement.executeUpdate();
		try {
			statement.close();
			this.close();
		} catch (Exception e) {}
		return res;
	}

	public Integer deleteOne(Object model) throws SQLException, IllegalArgumentException, IllegalAccessException {
		Delete delete = new Delete(getTableName(model.getClass()));
		for (Field f : model.getClass().getFields()) {
			if (f.isAnnotationPresent(Column.class) && f.isAnnotationPresent(PrimaryKey.class)) {
				delete.putWhere(f, model);
			}
		}
		this.open();
		PreparedStatement statement = this.conn.prepareStatement(delete.toString());
		Value[] where = delete.where();
		for (int i = 0; i < where.length; i++) {
			statement.setObject(i + 1, where[i].getValue(), where[i].getSqlType());
		}
		Integer res = statement.executeUpdate();
		try {
			statement.close();
			this.close();
		} catch (Exception e) {}
		return res;
	}

	private static String generateScriptTable(Class<?> c) {
		String tableName = getTableName(c);
		PrimaryKeys primaries = new PrimaryKeys(tableName);
		StringBuilder foreignKeys = new StringBuilder();
		StringBuilder builder = new StringBuilder("CREATE TABLE " + tableName + "(");
		Field fields[] = c.getDeclaredFields();
		for (Field field : fields) {
			String colName = field.getName();
			builder.append(" ");
			Column col = (Column) field.getAnnotation(Column.class);
			if (col == null) {
				continue;
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
				foreignKeys.append(tableName);
				foreignKeys.append(" FOREIGN KEY (");
				foreignKeys.append(colName);
				foreignKeys.append(") REFERENCES ");
				foreignKeys.append(foreignKey.table());
				foreignKeys.append("(");
				foreignKeys.append(foreignKey.primaryKey());
				foreignKeys.append(") ON DELETE CASCADE,");
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
			foreignKeys.replace(foreignKeys.length() - 1, foreignKeys.length(), "");
			builder.append(foreignKeys);
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
