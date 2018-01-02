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

public class User_groups {

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

	public User_groups() {

	}

	public User_groups(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User_groups [id=" + id + ", name=" + name + "]" + "\n";
	}

	static public User_groups[] loadAllGroups(Connection conn) throws SQLException {

		ArrayList<User_groups> groups = new ArrayList<User_groups>();
		String sql = "SELECT * FROM user_groups";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			User_groups loadedGroup = new User_groups();
			loadedGroup.id = resultSet.getInt("id");
			loadedGroup.name = resultSet.getString("name");
			groups.add(loadedGroup);
		}
		User_groups[] gArray = new User_groups[groups.size()];
		gArray = groups.toArray(gArray);
		return gArray;
	}

	static public User_groups loadGroupById(Connection conn, int id) throws SQLException {

		String sql = "SELECT * FROM user_groups where id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			User_groups loadedGroup = new User_groups();
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

	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(Arrays.toString(loadAllGroups(Connector.getConnection())));

		try {
			while (conn.isValid(3)) {
				System.out.println("Choose option: " + "\n" + "-> add - add new Group;" + "\n"
						+ "-> edit - edit Group information;" + "\n" + "-> delete - delete Group from DataBase;" + "\n"
						+ "-> quit;");
				String input = br1.readLine();

				if (input.equals("add")) {
					System.out.println("Enter group name");
					String groupName = br1.readLine();

					User_groups newGroup = new User_groups();
					newGroup.setName(groupName);
					newGroup.saveToDB(conn);
					System.out.println("Group has been added");
				}
				if (input.equals("edit")) {
					System.out.println("Enter group ID");
					int groupId = Integer.parseInt(br1.readLine());
					System.out.println("Enter new name");
					String groupName = br1.readLine();

					User_groups updatedGroup = new User_groups();
					updatedGroup.setName(groupName);
					updatedGroup.saveToDB(conn);
					System.out.println("Information has been updated");
				}
				if (input.equals("delete")) {
					System.out.println("enter group ID");
					int groupId = Integer.parseInt(br1.readLine());
					if (groupId != 0) {
						User_groups deleteGroup = loadGroupById(conn, groupId);
						deleteGroup.delete(conn);

					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
