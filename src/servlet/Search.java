package servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import base.Base;
import helpers.ListHelpers;
import helpers.Servlet;
import helpers.Verification;
import http.HttpStatusCode;
import models.Movie;

/**
 * Servlet implementation class Search
 */
@WebServlet("/search")
public class Search extends HttpServlet implements Consumer<PreparedStatement>, Function<ResultSet, Movie> {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Verification verification = new Verification(request);
		title = verification.getNullableString("title");
		before = verification.getNullableLong("before");
		after = verification.getNullableLong("after");
		if (verification.sendError(response) || (title == null && before == null && after == null)) {
			return;
		}
		List<String> and = new ArrayList<>();
		StringBuilder builder = new StringBuilder("SELECT Movie.id as id, title, releaseDate, description, Picture.name as cover");
		builder.append(" FROM Movie");
		builder.append(" INNER JOIN MoviePicture ON Movie.id = MoviePicture.idMovie");
		builder.append(" INNER JOIN Picture ON MoviePicture.idPicture = Picture.id");
		builder.append(" WHERE MoviePicture.type=? AND ");
		if (title != null) {
			and.add("title LIKE ?");
		}
		if (before != null) {
			and.add("DATEDIFF(Movie.releaseDate, ?) >= ?");
		}
		if (after != null) {
			and.add("DATEDIFF(Movie.releaseDate, ?) <= ?");
		}
		builder.append(ListHelpers.join(and, " AND "));
		Base base = new Base();
		try {
			List<Movie> movies = base.select(builder.toString(), this, this);
			Servlet.sendJson(movies, response);
		} catch (SQLException e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
		}
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

	private String title;
	private Long before;
	private Long after;
}
