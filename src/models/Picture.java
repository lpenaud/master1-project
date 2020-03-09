package models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import base.Base;
import base.Model;

public class Picture implements Model<Picture> {

	public Integer id;
	public String pathname;
	
	@Override
	public void insert(Base b) throws SQLException {
		String sql = "INSERT INTO Picture (pathname) VALUES (?)";
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setString(1, this.pathname);
		ps.execute();
	}

	@Override
	public void update(Base b) throws SQLException {
		String sql = "UPDATE Movie SET pathname=? WHERE id=?";
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setString(1, this.pathname);
		ps.setInt(2, this.id);
		ps.executeUpdate();
	}

	@Override
	public Picture select(Base b) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	

}
