package base;

import java.sql.SQLException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface Model<T> {	
	public default void update(Base b) throws SQLException {
		throw new NotImplementedException();
	};
	
	public T select(Base b) throws SQLException;
}
