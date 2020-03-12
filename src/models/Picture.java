package models;

import annotation.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import base.DatabaseType;
import base.Model;

@Table(name="Picture")
public class Picture implements Model {

	@Column(type=DatabaseType.Integer)
	@AutoIncrement
	@NotNull
	@PrimaryKey
	public Integer id;
	
	@Column(type=DatabaseType.String)
	@NotNull
	public String name;
	
	@Column(type=DatabaseType.String)
	@NotNull
	public String contentType;
	
	public Picture() {}
	
	public Picture(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.name = rs.getString("pathname");
		this.contentType = rs.getString("contentType");
	}
}
