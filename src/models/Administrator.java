package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import base.Base;
import base.Model;

public class Administrator implements Model<Boolean> {
	
	protected String login;
	protected String password;
	
	public Administrator(String login, String password) {
		this.login = login;
		this.password = password;
	}

	@Override
	public Boolean select(Base b) throws SQLException {
		String sql = "SELECT * FROM Administrator WHERE login = ? AND password = PASSWORD(?) LIMIT 1;";
		ResultSet rs;
		Boolean result;
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setString(0, this.login);
		ps.setString(1, this.password);
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
