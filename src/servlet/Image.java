package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import base.Base;
import config.Config;
import helpers.Validator;
import http.HttpStatusCode;
import http.error.ImageNotFound;
import models.Picture;

/**
 * Servlet implementation class Image
 */
@WebServlet("/image")
public class Image extends HttpServlet implements Consumer<PreparedStatement>, Function<ResultSet, Picture> {
	private static final long serialVersionUID = 1L;
	public String name;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Validator validator = new Validator(request);
		name = validator.getString("name");
		if (validator.sendError(response)) {
			return;
		}
		try {
			Base base = new Base();
			List<Picture> pictures = base.select("SELECT * FROM Picture WHERE name=?", this, this);
			Picture pic = pictures.isEmpty() ? null : pictures.get(0);
			if (pic == null) {
				ImageNotFound notFound = new ImageNotFound(name);
				notFound.sendError(response);
				return;
			}
			File img = Paths.get(Config.config.getBucket(), pic.name).toFile();
			response.setContentType(pic.contentType);
			IOUtils.copy(new FileInputStream(img), response.getOutputStream());
			return;
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		HttpStatusCode.InternalServerError.sendStatus(response);
	}

	@Override
	public Picture apply(ResultSet t) {
		try {
			return new Picture(t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void accept(PreparedStatement t) {
		try {
			t.setString(1, name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
