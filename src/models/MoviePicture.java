package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import annotation.*;
import base.DatabaseType;
import base.Model;

@Table(name="MoviePicture")
public class MoviePicture implements Model {

	
	@Column(type=DatabaseType.Integer)
	@PrimaryKey
	@NotNull
	@ForeignKey(table="Movie", primaryKey="id")
	public Integer idMovie;
	
	@Column(type=DatabaseType.Integer)
	@PrimaryKey
	@NotNull
	@ForeignKey(table="Picture", primaryKey="id")
	public Integer idPicture;
	
	@Column(type=DatabaseType.String)
	public String type;
	
	public MoviePicture() {}
	
	public MoviePicture(ResultSet rs) throws SQLException {
		this.idMovie = rs.getInt("idMovie");
		this.idPicture = rs.getInt("idPicture");
		this.type = rs.getString("type");
	}
}
