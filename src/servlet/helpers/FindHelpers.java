package servlet.helpers;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import models.Movie;

public class FindHelpers implements Consumer<PreparedStatement>, Function<ResultSet, Movie> {
	
	public FindHelpers(String title, Long before, Long after) {
		this.title = title;
		this.before = before;
		this.after = after;
	}
	
	@Override
	public void accept(PreparedStatement statement) {
		Integer i = 1;
		try {
			statement.setString(i++, "cover");
			if (this.title != null) {
				statement.setString(i++, "%" + this.title + "%");
			}
			if (this.before != null) {
				statement.setDate(i++, new Date(this.before));
				statement.setInt(i++, 0);
			}
			if (this.after != null) {
				statement.setDate(i++, new Date(this.after));
				statement.setInt(i++, 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Movie apply(ResultSet rs) {
		try {
			return new Movie(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String title;
	protected Long before;
	protected Long after;
}
