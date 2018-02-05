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

public class Excercises {

	private int id;
	private String title;
	private String description;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Excercises() {
	}

	public Excercises(String title, String description) {
		this.title = title;
		this.description = description;
	}
	/* METHODS */

	static public Excercises[] loadAllExcercises(Connection conn) throws SQLException {

		ArrayList<Excercises> exercises = new ArrayList<Excercises>();
		String sql = "SELECT * FROM excercises";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Excercises loadedExercise = new Excercises();
			loadedExercise.id = resultSet.getInt("id");
			loadedExercise.title = resultSet.getString("title");
			loadedExercise.description = resultSet.getString("description");
			exercises.add(loadedExercise);
		}
		Excercises[] eArray = new Excercises[exercises.size()];
		eArray = exercises.toArray(eArray);
		return eArray;
	}

	static public Excercises loadExerciseById(Connection conn, int id) throws SQLException {

		String sql = "SELECT * FROM excercises where id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Excercises loadedExercise = new Excercises();
			loadedExercise.id = resultSet.getInt("id");
			loadedExercise.title = resultSet.getString("title");
			loadedExercise.description = resultSet.getString("description");

			return loadedExercise;
		}
		return null;
	}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {
			String sql = "DELETE FROM excercises WHERE id= ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO excercises(title, description) VALUES (?, ?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			;
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE excercises SET title=?, description=? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			preparedStatement.setInt(3, this.id);
			preparedStatement.executeUpdate();
		}
	}

	@Override
	public String toString() {
		return "[id=" + id + ", title= " + title + ", description= " + description + "]" + "\n";
	}

}
