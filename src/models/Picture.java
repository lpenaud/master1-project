package models;

import annotation.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import base.Base;
import base.DatabaseType;
import base.Model;

@Table(name="Picture")
public class Picture implements Model<Picture> {

	@Column(type=DatabaseType.Integer)
	@NotNull
	@PrimaryKey
	public Integer id;
	
	@Column(type=DatabaseType.String)
	@NotNull
	public String pathname;

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
