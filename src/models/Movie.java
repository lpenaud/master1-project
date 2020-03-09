package models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import base.Base;
import base.Model;

public class Movie implements Model<Movie> {

	public Integer id;
	public String title;
	public Date releaseDate;
	public String description;
	
	@Override
	public void insert(Base b) throws SQLException {
		String sql = "INSERT INTO Movie (title, releaseDate, description) VALUES (?, ?, ?)";
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setString(1, this.title);
		ps.setDate(2,  new java.sql.Date(this.releaseDate.getTime()));
		ps.setString(3, this.description);
		ps.execute();
	}

	@Override
	public void update(Base b) throws SQLException {
		String sql = "UPDATE Movie SET title=?, releaseDate=?, description=? WHERE id=?";
		PreparedStatement ps = b.prepareStatement(sql);
		ps.setString(1, this.title);
		ps.setDate(2,  new java.sql.Date(this.releaseDate.getTime()));
		ps.setString(3, this.description);
		ps.setInt(4,this.id);
		ps.executeUpdate();
	}

	@Override
	public Movie select(Base b) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
