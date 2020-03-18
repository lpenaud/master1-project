package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import base.Base;
import helpers.ListHelpers;
import http.HttpStatusCode;
import models.Movie;
import servlet.helpers.FindHelpers;
import servlet.helpers.ImagePicture;
import servlet.helpers.Servlet;
import servlet.helpers.Validator;

/**
 * Servlet implementation class Search
 */
@WebServlet("/find")
public class Find extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Validator validator = new Validator(request);
		String title = validator.getNullableString("title");
		Long before = validator.getNullableLong("before");
		Long after = validator.getNullableLong("after");
		if (validator.sendError(response)) {
			return;
		}
		if (title == null && before == null && after == null) {
			HttpStatusCode.UnprocessableEntity.sendStatus(response);
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
			and.add("DATEDIFF(Movie.releaseDate, ?) <= ?");
		}
		if (after != null) {
			and.add("DATEDIFF(Movie.releaseDate, ?) >= ?");
		}
		builder.append(ListHelpers.join(and, " AND "));
		Base base = new Base();
		FindHelpers helper = new FindHelpers(title, before, after);
		try {
			List<Movie> movies = base.select(builder.toString(), helper, helper);
			for (Movie movie : movies) {
				movie.cover = ImagePicture.generateCoverUrl(request, movie.cover);
			}
			Servlet.sendJson(movies, response);
		} catch (SQLException e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
		}
	}
}
