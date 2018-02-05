package entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import utils.Connector;

public class UserGroups {

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserGroups() {

	}

	public UserGroups(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User_groups [id=" + id + ", name=" + name + "]" + "\n";
	}

	/* METHODS */
	static public UserGroups[] loadAllGroups(Connection conn) throws SQLException {

		ArrayList<UserGroups> groups = new ArrayList<UserGroups>();
		String sql = "SELECT * FROM user_groups";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			UserGroups loadedGroup = new UserGroups();
			loadedGroup.id = resultSet.getInt("id");
			loadedGroup.name = resultSet.getString("name");
			groups.add(loadedGroup);
		}
		UserGroups[] gArray = new UserGroups[groups.size()];
		gArray = groups.toArray(gArray);
		return gArray;
	}

	static public UserGroups loadGroupById(Connection conn, int id) throws SQLException {

		String sql = "SELECT * FROM user_groups where id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			UserGroups loadedGroup = new UserGroups();
			loadedGroup.id = resultSet.getInt("id");
			loadedGroup.name = resultSet.getString("name");
			return loadedGroup;
		}
		return null;
	}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {
			String sql = "DELETE FROM user_groups WHERE id= ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO user_groups(name) VALUES (?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.name);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE user_groups SET name=? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.name);
			preparedStatement.setInt(2, this.id);
			preparedStatement.executeUpdate();
		}
	}

}
