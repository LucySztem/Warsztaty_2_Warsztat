package entities;

import java.io.BufferedReader;


import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
//import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import utils.Connector;

public class Solutions {

	private int id;
	private Date created;
	private Date updated;
	private String description;
	private int excercise_id;
	private int user_id;

	public int getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExcercise_id() {
		return excercise_id;
	}

	public void setExcercise_id(int excercise_id) {
		this.excercise_id = excercise_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public Solutions() {

	}

	public Solutions(Date created, Date updated, String description) {
		this.created = created;
		this.updated = updated;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Solutions [id=" + id + ", created=" + created + ", updated=" + updated + ", description=" + description
				+ ", excercise_id=" + excercise_id + ", user_id=" + user_id + "]" + "\n";
	}

	static public Solutions[] loadAllSolutions(Connection conn) throws SQLException {

		ArrayList<Solutions> solutions = new ArrayList<Solutions>();
		String sql = "SELECT * FROM solutions";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solutions loadedSolutions = new Solutions();
			loadedSolutions.id = resultSet.getInt("id");
			loadedSolutions.excercise_id = resultSet.getInt("excercise_id");
			loadedSolutions.user_id = resultSet.getInt("user_id");
			loadedSolutions.description = resultSet.getString("description");
			loadedSolutions.created = resultSet.getDate("created");
			loadedSolutions.updated = resultSet.getDate("updated");
			solutions.add(loadedSolutions);
		}
		Solutions[] eArray = new Solutions[solutions.size()];
		eArray = solutions.toArray(eArray);
		return eArray;
	}

	static public Solutions[] loadAllSortedByCreateDate(Connection conn, int limit) throws SQLException {

		ArrayList<Solutions> solutions = new ArrayList<Solutions>();
		String sql = "SELECT * FROM solutions ORDER BY created DESC LIMIT ? ;";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, limit);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Solutions loadedSolutions = new Solutions();
			loadedSolutions.id = resultSet.getInt("id");
			loadedSolutions.created = resultSet.getDate("created");
			loadedSolutions.updated = resultSet.getDate("updated");
			loadedSolutions.excercise_id = resultSet.getInt("excercise_id");
			loadedSolutions.user_id = resultSet.getInt("user_id");
			loadedSolutions.description = resultSet.getString("description");
			solutions.add(loadedSolutions);
		}
		Solutions[] sArray = new Solutions[solutions.size()];
		sArray = solutions.toArray(sArray);
		return sArray;
	}

	static public Solutions[] loadSolutionByUserId(Connection conn, int userId) throws SQLException {
		
		ArrayList<Solutions> solutions = new ArrayList<Solutions>();
		String sql = "SELECT * FROM solutions where user_id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, userId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solutions loadedSolution = new Solutions();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getDate("created");
			loadedSolution.updated = resultSet.getDate("updated");
			loadedSolution.excercise_id = resultSet.getInt("excercise_id");
			loadedSolution.user_id = resultSet.getInt("user_id");
			loadedSolution.description = resultSet.getString("description");
			solutions.add(loadedSolution);
		}
			Solutions[] sArray = new Solutions[solutions.size()];
			sArray = solutions.toArray(sArray);
			return sArray;
		
		}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {

			String sql = "DELETE FROM solutions WHERE id= ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO solutions(created, updated, excercise_id, user_id, description) VALUES (?, ?, ?, ?, ?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setDate(1, (java.sql.Date) this.created);
			preparedStatement.setDate(2, (java.sql.Date) this.updated);
			preparedStatement.setInt(3, this.excercise_id);
			preparedStatement.setInt(4, this.user_id);
			preparedStatement.setString(5, this.description);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE solutions SET created=?, updated=?, exercise_id=?, user_id=?, description=? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setDate(1, (java.sql.Date) this.created);
			preparedStatement.setDate(2, (java.sql.Date) this.updated);
			preparedStatement.setInt(3, this.excercise_id);
			preparedStatement.setInt(4, this.user_id);
			preparedStatement.setString(5, this.description);
			preparedStatement.setInt(6, this.id);
			preparedStatement.executeUpdate();
		}
	}
	

	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		System.out.println(Arrays.toString(loadAllSolutions(Connector.getConnection())));
		//System.out.println(Arrays.toString(loadSolutionByUserId(Connector.getConnection(), 2)));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		
		try{
			while(conn.isValid(3)){
				System.out.println("Chose option: " + "\n" + "-> add - add solution to the user;" + "\n"
						+ "-> view - show solutions from specific user;" + "\n" + "-> quit;");
				String input = br1.readLine();
				
				if(input.equals("view")){
					System.out.println("Enter user ID");
					int userID = Integer.parseInt(br1.readLine());
					
					Solutions userSolutions = new Solutions();
					//userSolutions.loadSolutionByUserId(conn, userID);
					System.out.println(userSolutions.loadSolutionByUserId(conn, userID));
				}
				if(input.equals("add")){
					Users allUsers = new Users();
					System.out.println(Arrays.toString(allUsers.loadAll(Connector.getConnection())));
					
					System.out.println("Enter user ID");
					int userId = Integer.parseInt(br1.readLine());
					Solutions userSolution = new Solutions();
					//System.out.println(userSolution.loadSolutionByUserId(conn, userId));
					System.out.println("Enter exercise ID");
					int exercise = Integer.parseInt(br1.readLine());
								
					userSolution.setCreated(null);
					userSolution.setUpdated(null);
					userSolution.setExcercise_id(exercise);
					userSolution.setUser_id(userId);
					userSolution.saveToDB(conn);
				}
				if (input.equals("quit")){
					return;
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}

}
