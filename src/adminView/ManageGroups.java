package adminView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import entities.UserGroups;
import utils.Connector;

public class ManageGroups {
	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(Arrays.toString(UserGroups.loadAllGroups(Connector.getConnection())));

		try {
			while (conn.isValid(3)) {
				System.out.println("Choose option: " + "\n" + "-> add - add new Group;" + "\n"
						+ "-> edit - edit Group information;" + "\n" + "-> delete - delete Group from DataBase;" + "\n"
						+ "-> quit;");
				String input = br1.readLine();

				if (input.equals("add")) {
					System.out.println("Enter group name");
					String groupName = br1.readLine();

					UserGroups newGroup = new UserGroups();
					newGroup.setName(groupName);
					newGroup.saveToDB(conn);
					System.out.println("Group has been added");
				}
				if (input.equals("edit")) {
					System.out.println("Enter group ID");
					int groupId = Integer.parseInt(br1.readLine());
					System.out.println("Enter new name");
					String groupName = br1.readLine();

					UserGroups updatedGroup = UserGroups.loadGroupById(conn, groupId);
					updatedGroup.setName(groupName);
					updatedGroup.saveToDB(conn);
					System.out.println("Information has been updated");
				}
				if (input.equals("delete")) {
					System.out.println("enter group ID");
					int groupId = Integer.parseInt(br1.readLine());
					if (groupId != 0) {
						UserGroups deleteGroup = UserGroups.loadGroupById(conn, groupId);
						deleteGroup.delete(conn);

					}
				}else if (input.equals("quit")) {
					return;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
