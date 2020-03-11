package models;

import annotation.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import base.DatabaseType;
import base.Model;

@Table(name="Picture")
public class Picture implements Model {

	@Column(type=DatabaseType.Integer)
	@NotNull
	@PrimaryKey
	public Integer id;
	
	@Column(type=DatabaseType.String)
	@NotNull
	public String pathname;
	
	public Picture() {}
	
	public Picture(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.pathname = rs.getString("pathname");
	}
}
