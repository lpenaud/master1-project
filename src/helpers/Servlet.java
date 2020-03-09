package helpers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import base.Base;

public class Servlet {

	public static boolean connect(String login, String password) throws SQLException {
		Base b = new Base();
		String sql = "SELECT * FROM Administrator WHERE login = ? AND password = PASSWORD(?)";
		ResultSet rs;
		Boolean result;
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setString(0, login);
		ps.setString(1, password);
		rs = ps.executeQuery();
		result = rs.getFetchSize() == 0;
		try {
			ps.close();
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
