package servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import base.Base;
import helpers.HttpStatusCode;
import helpers.Servlet;
import helpers.Verification;
import models.Movie;

/**
 * Servlet implementation class Movie
 */
@WebServlet("/Movie")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Base b = new Base();
		List<Movie> movies = null;
		try {
			movies = b.select("SELECT * FROM Movie", (rs) -> {
				try {
					return new Movie(rs);
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
		}
		Servlet.sendJson(movies, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Movie movie = new Movie();
		Verification verif = new Verification(request);
		Long timestamp = verif.getLong("releaseDate");
		movie.title = verif.getString("title", 1);
		movie.description = verif.getString("description", 1);
		if (verif.sendError(response)) {
			return;
		}
		try {
			movie.releaseDate = new Date(timestamp);
		} catch (Exception e) {
			HttpStatusCode.InternalServerError.sendStatus(response);
		}
		try {
			Base b = new Base();
			b.insert(Movie.class, movie);
		} catch (SQLException e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
			return;
		}
		HttpStatusCode.Created.sendStatus(response);
	}

}
