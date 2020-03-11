package models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

import annotation.AutoIncrement;
import annotation.Column;
import annotation.NotNull;
import annotation.PrimaryKey;
import annotation.Table;
import base.Base;
import base.DatabaseType;
import base.Model;

@Table(name="Movie")
public class Movie implements Model<Movie> {

	@Column(type=DatabaseType.Integer)
	@AutoIncrement
	@PrimaryKey
	@NotNull
	public Integer id;
	
	@Column(type=DatabaseType.String)
	@NotNull
	public String title;
	
	@Column(type=DatabaseType.Date)
	@NotNull
	public Date releaseDate;
	
	@Column(type=DatabaseType.String)
	@NotNull
	public String description;

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
