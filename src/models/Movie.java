package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import annotation.AutoIncrement;
import annotation.Column;
import annotation.NotNull;
import annotation.PrimaryKey;
import annotation.Table;
import base.DatabaseType;
import base.Model;

@Table(name="Movie")
public class Movie implements Model {

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
	
	public Movie() {}
	
	public Movie(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.title = rs.getString("title");
		this.releaseDate = rs.getDate("releaseDate");
		this.description = rs.getString("description");
	}
	
}
