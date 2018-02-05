package adminView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import entities.Users;
import utils.Connector;

public class ManageUsers {
	
	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		System.out.println(Arrays.toString(Users.loadAll(Connector.getConnection())));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

		try {
			while (conn.isValid(3)) {
				System.out.println(
						"Choose option: " + "\n" + "-> add - add new User;" + "\n" + "-> edit - edit User information;"
								+ "\n" + "-> delete - delete User from DataBase;" + "\n" + "-> quit;");
				String input = br1.readLine();

				if (input.equals("add")) {
					System.out.println("Enter username:");
					String userName = br1.readLine();
					System.out.println("Enter password:");
					String passWord = br1.readLine();
					System.out.println("Enter e-mail address:");
					String email = br1.readLine();
					System.out.println("Enter group number:");
					int userGroup = Integer.parseInt(br1.readLine());

					Users addUser = new Users(userName, passWord, email);
					addUser.setUser_group_id(userGroup);
					addUser.setUsername(userName);
					addUser.setEmail(email);
					addUser.saveToDB(conn);
					System.out.println("User has been added");
				}

				if (input.equals("edit")) {
					System.out.println("Enter user ID");
					int userId = Integer.parseInt(br1.readLine());
					System.out.println("Enter username:");
					String userName = br1.readLine();
					System.out.println("Enter password:");
					String passWord = br1.readLine();
					System.out.println("Enter e-mail address:");
					String email = br1.readLine();
					System.out.println("Enter group number:");
					int userGroup = Integer.parseInt(br1.readLine());

					Users updateUser = new Users(userName, passWord, email);
					updateUser.setUser_group_id(userGroup);
					updateUser.setUsername(userName);
					updateUser.setEmail(email);
					updateUser.saveToDB(conn);

				}
				if (input.equals("delete")) {
					System.out.println("Enter User ID number");

					int userId = Integer.parseInt(br1.readLine());

					if (userId == 0) {
						System.out.println("User ID has to be different than 0");
						System.out.println("Enter User ID number");
						userId = Integer.parseInt(br1.readLine());
						if (userId != 0) {
							Users deleteUser = Users.loadUserById(conn, userId);
							deleteUser.delete(conn);
						}
					} else {
						Users deleteUser = Users.loadUserById(conn, userId);
						deleteUser.delete(conn);
					}

				} else if (input.equals("quit")) {
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
