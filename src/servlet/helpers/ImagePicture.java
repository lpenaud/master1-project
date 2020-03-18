package servlet.helpers;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import models.Picture;

public class ImagePicture implements Function<ResultSet, Picture>, Consumer<PreparedStatement> {
	public static String generateCoverUrl(HttpServletRequest request, String cover) {
		try {
			URI uri = new URI(request.getRequestURL().toString());
			return uri.resolve(request.getContextPath() + "/image/" + cover).toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ImagePicture(String name) {
		this.name = name;
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

	protected String name;
}
