package helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import base.Base;
import models.Movie;

public class FindMovieId implements Consumer<PreparedStatement>, Function<ResultSet, Movie> {
	protected Movie m;
	
	public FindMovieId(Movie m) {
		this.m = m;
	}

	@Override
	public void accept(PreparedStatement statement)  {
		try {
			statement.setString(0, m.description);
			statement.setString(1, m.title);
			statement.setDate(2, m.releaseDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Movie apply(ResultSet rs) {		
		try {
			return this.m = new Movie(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Movie run(Base b) throws SQLException {
		String sql = "SELECT * FROM Movie WHERE description=? AND title=? AND releaseDate=?";
		b.select(sql, this, this);
		return this.m;
	}

}
