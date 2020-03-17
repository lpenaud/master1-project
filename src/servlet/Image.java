package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import config.Config;
import helpers.Validator;
import http.error.ImageNotFound;

/**
 * Servlet implementation class Image
 */
@WebServlet("/image")
public class Image extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Validator validator = new Validator(request);
		String name = validator.getString("name");
		if (validator.sendError(response)) {
			return;
		}
		File img = Paths.get(Config.config.getBucket(), name).toFile();
		if (!img.exists()) {
			ImageNotFound notFound = new ImageNotFound(name);
			notFound.sendError(response);
			return;
		}
		IOUtils.copy(new FileInputStream(img), response.getOutputStream());
	}

}
