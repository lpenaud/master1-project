package helpers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.Base;
import http.HttpStatusCode;

public class Servlet {

	public static boolean connect(String login, String password) {
		Base b = new Base();
		String sql = "SELECT * FROM Administrator WHERE login = ? AND password = PASSWORD(?)";
		ResultSet rs;
		Boolean result = false;
		PreparedStatement ps;
		try {
			b.open();
			ps = b.prepareStatement(sql);
			ps.setString(1, login);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		try {
			ps.close();
			rs.close();
			b.close();
		} catch (Exception e) {}
		return result;
	}
	
	public static void sendJson(Object object, HttpServletResponse response) throws IOException {
		if (object == null) {
			HttpStatusCode.InternalServerError.sendStatus(response);
			return;
		}
		response.setContentType("application/json");
		JsonGenerator generator = new JsonFactory().createGenerator(response.getOutputStream());
		generator.setCodec(new ObjectMapper());
		generator.writeObject(object);
		generator.close();
		response.setStatus(HttpStatusCode.Ok.getCode());
	}
	
	public static void sendJson(List<?> objects, HttpServletResponse response) throws IOException {
		sendJson(objects.toArray(), response);
	}
	
	public static void sendJson(Object[] objects, HttpServletResponse response) throws IOException {
		if (objects == null) {
			HttpStatusCode.InternalServerError.sendStatus(response);
			return;
		}
		response.setContentType("application/json");
		JsonGenerator generator = new JsonFactory().createGenerator(response.getOutputStream());
		generator.setCodec(new ObjectMapper());
		generator.writeObject(objects);
		generator.close();
		response.setStatus(HttpStatusCode.Ok.getCode());
	}
	
	public static String randomFilename() {
		return Long.toString(System.currentTimeMillis(), 36) + Long.toString(Math.round(Math.random()), 36);
	}
}
