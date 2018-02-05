package adminView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import entities.Excercises;
import utils.Connector;

public class ManageExercise {
	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		System.out.println(Arrays.toString(Excercises.loadAllExcercises(Connector.getConnection())));
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
					System.out.println("Enter description of the exercise");
					String description = br1.readLine();

					Excercises editExercise = Excercises.loadExerciseById(conn, exerciseId);
					editExercise.setTitle(titleOfExercise);
					editExercise.setDescription(description);
					editExercise.saveToDB(conn);
				}
				if (input.equals("delete")) {
					System.out.println("Enter exercise ID");
					int exerciseId = Integer.parseInt(br1.readLine());

					Excercises deleteExercise = Excercises.loadExerciseById(conn, exerciseId);
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
