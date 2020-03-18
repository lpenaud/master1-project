package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import base.Base;
import config.Config;
import http.HttpStatusCode;
import http.error.ImageNotFound;
import models.Picture;
import servlet.helpers.ImagePicture;

/**
 * Servlet implementation class Image
 */
@WebServlet("/image/*")
public class Image extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getPathInfo();
		if (name == null || name.isEmpty()) {
			HttpStatusCode.NotFound.sendStatus(response);
			return;
		}
		String[] split = name.split("/", 3);
		if (split.length >= 3) {
			HttpStatusCode.NotFound.sendStatus(response);
			return;
		}
		name = split[1];
		try {
			Base base = new Base();
			ImagePicture funcHelper = new ImagePicture(name);
			List<Picture> pictures = base.select("SELECT * FROM Picture WHERE name=?", funcHelper, funcHelper);
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

}
