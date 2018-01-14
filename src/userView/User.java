package userView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import entities.Excercises;
import entities.Solutions;
import entities.Users;
import utils.Connector;

public class User {
	// public void CheckUserId(Connection conn, int id) throws SQLException{
	// int[] listID = {};
	// String query = "Select id from users;";
	// PreparedStatement prepStmt = conn.prepareStatement(query);
	// ResultSet resultSet = prepStmt.executeQuery();
	// while(resultSet.next()){
	// for(int i=0; )
	// resultSet.getInt(id);
	//
	//
	// }
	// }

	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		Users usersId = new Users();
		// System.out.println(Arrays.toString(usersId.loadUsersId(conn)));
		Excercises list = new Excercises();
		System.out.println(Arrays.toString(list.loadAllExcercises(conn)));
		try {

			while (conn.isValid(3)) {

				System.out.println("Enter your ID number:");
				int userId = Integer.parseInt(br1.readLine());
				if (userId != 0) {

					System.out.println(
							"Chose option :" + "\n" + "-> add - add solution" + "\n" + "-> view - view your solutions" + "\n" + "-> quit;");
					String input = br1.readLine();
					if (input.equals("add")) {
						Solutions userSolutions = new Solutions();
						System.out.println("You have submited solutions to the the below excersises:" + "\n"
								+ Arrays.toString(userSolutions.loadSolutionByUserId(conn, userId)));
						Solutions userNewSolution = new Solutions();
						System.out.println("Enter excercise id:");
						int excerciseId = Integer.parseInt(br1.readLine());
						System.out.println("enter description");
						String desc = br1.readLine();

						userNewSolution.setDescription(desc);
						userNewSolution.setExcercise_id(excerciseId);
						userNewSolution.setUser_id(userId);
						userNewSolution.saveToDB(conn);

						System.out.println("Your solution has been added");

					}
					if (input.equals("view")) {
						Solutions userSolutions = new Solutions();
						System.out.println(Arrays.toString(userSolutions.loadSolutionByUserId(conn, userId)));
					}
					if(input.equalsIgnoreCase("quit")){
						return;
					}
				} else {
					System.out.println("Id has to be diffrent than 0");

				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
