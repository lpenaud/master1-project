package models;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import annotation.*;
import base.Base;
import base.DatabaseType;
import base.Model;

@Table(name="MoviePicture")
public class MoviePicture implements Model<MoviePicture> {

	
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
