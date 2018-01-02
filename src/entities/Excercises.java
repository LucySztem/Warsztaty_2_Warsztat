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
	// create table excercises (id INT PRIMARY KEY AUTO_INCREMENT,title
	// VARCHAR(100),description TEXT);

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

	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		System.out.println(Arrays.toString(loadAllExcercises(Connector.getConnection())));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

		try {
			while (conn.isValid(3)) {
				System.out.println("Chose option: " + "\n" + "-> add - add new Exercise;" + "\n"
						+ "-> edit - edit Exercise information;" + "\n" + "-> delete - delete Exercise from DataBase;"
						+ "\n" + "-> quit;");
				String input = br1.readLine();

				if (input.equals("add")) {
					System.out.println("Enter title");
					String titleOfExercise = br1.readLine();
					System.out.println("Enter description of the exercise");
					String description = br1.readLine();

					Excercises addExercise = new Excercises(titleOfExercise, description);
					addExercise.setTitle(titleOfExercise);
					addExercise.setDescription(description);
					addExercise.saveToDB(conn);

				}
				if (input.equals("edit")) {
					System.out.println("Enter exercise ID");
					int exerciseId = Integer.parseInt(br1.readLine());
					System.out.println("Enter title");
					String titleOfExercise = br1.readLine();
					System.out.println("Enterdescription of the exercise");
					String description = br1.readLine();

					Excercises editExercise = loadExerciseById(conn, exerciseId);
					editExercise.setTitle(titleOfExercise);
					editExercise.setDescription(description);
					editExercise.saveToDB(conn);
				}
				if (input.equals("delete")) {
					System.out.println("Enter exercise ID");
					int exerciseId = Integer.parseInt(br1.readLine());

					Excercises deleteExercise = loadExerciseById(conn, exerciseId);
					deleteExercise.delete(conn);

				} else if (input.equals("quit")) {
					return;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
