package helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import base.Base;

public class Servlet {

	public static boolean connect(String login, String password) {
		Base b = new Base();
		String sql = "SELECT * FROM Administrator WHERE login = ? AND password = PASSWORD(?)";
		ResultSet rs;
		Boolean result;
		PreparedStatement ps;
		try {
			b.open();
			ps = b.prepareStatement(sql);
			ps.setString(0, login);
			ps.setString(1, password);
			rs = ps.executeQuery();
			result = rs.getFetchSize() == 0;
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
}
