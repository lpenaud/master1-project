package servlet;

import java.io.IOException;
import java.sql.Date;
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
			base.insert(Movie.class, movie);
			base.insert(Picture.class, picture);
			mp.idMovie = movie.id;
			mp.idPicture = picture.id;
			mp.type = "cover";
			base.insert(MoviePicture.class, mp);
		} catch (Exception e) {
			e.printStackTrace();
			HttpStatusCode.InternalServerError.sendStatus(response);
			return;
		}
		HttpStatusCode.Ok.sendStatus(response);
	}

}
