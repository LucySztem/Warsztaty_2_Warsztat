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
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import utils.Connector;

public class Users {
	/*
	 * atrybuty odpowiadajace nazwa kolumn w tabeli User
	 */
	private int id;
	private int user_group_id;
	private String username;
	private String password;
	private String email;

	public int getId() {
		return id;
	}

	public int getUser_group_id() {
		return user_group_id;
	}

	public void setUser_group_id(int user_group_id) {
		this.user_group_id = user_group_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	/* setter hashujacy haslo */
	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Users(String username, String password, String email) {
		this.username = username;
		this.setPassword(password); // set bo bedziemy ustawiac i kodowac haslo
		this.email = email;
	}

	public Users() {

	}

	/* METHODS */

	static public Users[] loadAll(Connection conn) throws SQLException {
		List<Users> users = new ArrayList<Users>();

		String sql = "SELECT * FROM	users";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Users loadedUser = new Users();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			loadedUser.user_group_id = resultSet.getInt("user_group_id");
			users.add(loadedUser);
		}

		Users[] uArray = new Users[users.size()];
		uArray = users.toArray(uArray);
		return uArray;
	}

	static public Users[] loadUsersId(Connection conn) throws SQLException {
		List<Users> usersList = new ArrayList<Users>();
		String sql = "SELECT id FROM users;";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Users users = new Users();
			users.id = resultSet.getInt("id");
			usersList.add(users);
		}
		Users[] uArray = new Users[usersList.size()];
		uArray = usersList.toArray(uArray);
		return uArray;

	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO users(username, email, password, user_group_id) VALUES (?, ?, ?, ?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.user_group_id);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {

			String sql = "UPDATE users	SET	username=?,	email=?,	password=?	WHERE	id	=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.user_group_id);
			preparedStatement.setInt(5, this.id);
			preparedStatement.executeUpdate();
		}
	}

	static public Users loadUserById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM	users	where	id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Users loadedUser = new Users();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			loadedUser.user_group_id = resultSet.getInt("user_group_id");
			return loadedUser;
		}
		return null;
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE	FROM	users	WHERE	id=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", user_group_id=" + user_group_id + "\n " + " username=" + username + ", password="
				+ password + ", email=" + email + "]" + "\n";
	}

}
