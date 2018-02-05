package adminView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import entities.Solutions;
import entities.Users;
import utils.Connector;

public class ManageSolutions {

	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		System.out.println(Arrays.toString(Solutions.loadAllSolutions(Connector.getConnection())));
		// System.out.println(Arrays.toString(loadSolutionByUserId(Connector.getConnection(),
		// 2)));
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

		try {
			while (conn.isValid(3)) {
				System.out.println("Chose option: " + "\n" + "-> add - add solution to the user;" + "\n"
						+ "-> view - show solutions from specific user;" + "\n" + "-> quit;");
				String input = br1.readLine();

				if (input.equals("view")) {
					System.out.println("Enter user ID");
					int userID = Integer.parseInt(br1.readLine());

					Solutions userSolutions = new Solutions();
					// userSolutions.loadSolutionByUserId(conn, userID);
					System.out.println(Solutions.loadSolutionByUserId(conn, userID));
				}
				if (input.equals("add")) {
					Users allUsers = new Users();
					System.out.println(Arrays.toString(Users.loadAll(Connector.getConnection())));

					System.out.println("Enter user ID");
					int userId = Integer.parseInt(br1.readLine());
					Solutions userSolution = new Solutions();
					// System.out.println(userSolution.loadSolutionByUserId(conn,
					// userId));
					System.out.println("Enter exercise ID");
					int exercise = Integer.parseInt(br1.readLine());

					userSolution.setCreated(null);
					userSolution.setUpdated(null);
					userSolution.setExcercise_id(exercise);
					userSolution.setUser_id(userId);
					userSolution.saveToDB(conn);
				}
				if (input.equals("quit")) {
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
