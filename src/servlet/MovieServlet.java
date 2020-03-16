package servlet;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import base.Base;
import helpers.PartFormater;
import helpers.Servlet;
import http.HttpStatusCode;
import http.Token;
import models.Movie;
import models.MoviePicture;
import models.Picture;

/**
 * Servlet implementation class Picture
 */
@WebServlet("/movie")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5,
maxRequestSize = 1024 * 1024 * 5 * 5)
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Base b = new Base();
		try {
			List<Movie> movies = b.select("SELECT Movie.id as id, title, releaseDate, description, Picture.name as cover " + 
					"FROM Movie " + 
					"INNER JOIN MoviePicture ON Movie.id = MoviePicture.idMovie " + 
					"INNER JOIN Picture ON MoviePicture.idPicture = Picture.id " + 
					"WHERE MoviePicture.type='cover'", (rs) -> {
				try {
					return new Movie(rs);
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			});
			Servlet.sendJson(movies, response);
		} catch (SQLException e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Token.isConnected(request)) {
			HttpStatusCode.Unauthorized.sendStatus(response);
			return;
		}
		PartFormater formater = new PartFormater(request);
		String title = formater.readString("title");
		String description = formater.readString("description");
		Long releaseDate = formater.readLong("releaseDate");
		PartFormater.File cover = formater.getFile("cover");
		if (formater.sendError(response)) {
			return;
		}
		Base base = new Base();
		Movie movie = new Movie();
		movie.description = description;
		movie.title = title;
		movie.releaseDate = new Date(releaseDate);
		Picture picture = new Picture();
		picture.contentType = cover.getContentType();
		picture.name = cover.getPathname().getFileName().toString();
		try {
			cover.write();
			MoviePicture mp = new MoviePicture();
			base.insert(movie);
			base.insert(picture);
			mp.idMovie = movie.id;
			mp.idPicture = picture.id;
			mp.type = "cover";
			base.insert(mp);
		} catch (Exception e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
			return;
		}
		movie.cover = picture.name;
		Servlet.sendJson(movie, response);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Token.isConnected(request)) {
			HttpStatusCode.Unauthorized.sendStatus(response);
			return;
		}
		PartFormater formater = new PartFormater(request);
		Integer id = formater.readInteger("id");
		if (formater.sendError(response)) {
			return;
		}
		Movie movie;
		Base b = new Base();
		try {
			List<Movie> result = b.select("SELECT * FROM Movie WHERE id=?", (PreparedStatement statement) -> {
				try {
					statement.setInt(1, id);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			},
			(rs) -> {
				try {
					return new Movie(rs);
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			});
			if (result == null) {
				throw new Exception("Not found");
			}
			movie = result.get(0);
			String title = formater.readString("title");
			String description = formater.readString("description");
			Long releaseDate = formater.readLong("releaseDate");
			if (title != null) {
				movie.title = title;
			}
			if (description != null) {
				movie.description = description;
			}
			if (releaseDate != null) {
				movie.releaseDate = new Date(releaseDate);
			}
			b.updateOne(movie);
			List<Movie> movies = b.select("SELECT Movie.id as id, title, releaseDate, description, Picture.name as cover " + 
					"FROM Movie " + 
					"INNER JOIN MoviePicture ON Movie.id = MoviePicture.idMovie " + 
					"INNER JOIN Picture ON MoviePicture.idPicture = Picture.id " + 
					"WHERE MoviePicture.type='cover' AND Movie.id=?", (statement) -> {
						try {
							statement.setInt(1, movie.id);
						} catch (SQLException e) {}
					}, (rs) -> {
						try {
							return new Movie(rs);
						} catch (SQLException e) {
							return null;
						}
					});
			Servlet.sendJson(movies.get(0), response);
		} catch (Exception e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!Token.isConnected(request)) {
			HttpStatusCode.Unauthorized.sendStatus(response);
			return;
		}
		PartFormater formater = new PartFormater(request);
		Integer id = formater.readInteger("id");
		if (formater.sendError(response)) {
			return;
		}
		Base base = new Base();
		try {
			base.deleteOne(new Movie(id));
			HttpStatusCode.Ok.sendStatus(response);
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
		}
	}

}
