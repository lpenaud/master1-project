package models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import base.Base;
import base.Model;

public class MoviePicture implements Model<MoviePicture> {

	public Integer idMovie;
	public Integer idPicture;
	public String type;
	
	@Override
	public void insert(Base b) throws SQLException {
		String sql = "INSERT INTO MoviePicture (idMovie, idPicture, type) VALUES (?, ?, ?)";
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setInt(1, idMovie);
		ps.setInt(2, idPicture);
		ps.setString(3, type);
		ps.execute();
	}

	@Override
	public void update(Base b) throws SQLException {
		String sql = "UPDATE Movie SET type=? WHERE idMovie=? AND idPicture=?";
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setString(1, type);
		ps.setInt(2, idMovie);
		ps.setInt(3, idPicture);
		ps.executeUpdate();		
	}

	@Override
	public MoviePicture select(Base b) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
