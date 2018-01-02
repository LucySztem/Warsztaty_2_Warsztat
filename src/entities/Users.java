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
	 * atrybuty odpowiadajace nazwa kolumn w tabeli User, obiekt jest
	 * zscynronizowany
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

	/* setter kodujacy haslo */
	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Users(String username, String password, String email) { // source ->
																	// generate
																	// constructor
																	// for
																	// fields

		this.username = username;
		this.setPassword(password); // set bo bedziemy ustawiac i kodowac haslo
		this.email = email;
	}

	public Users() { // trzeba stworzyc pusty konstruktor

	}
	// create table user_group ( id INT PRIMARY KEY AUTO_INCREMENT, name
	// VARCHAR(50));

	// create table users (id INT PRIMARY KEY AUTO_INCREMENT,user_group_id int,
	// username VARCHAR(50), password VARCHAR(255), email VARCHAR(255),
	// constraint users_email_uindex unique (email),
	// constraint users_id_uindex unique (id),
	// constraint users_user_group_id_fk foreign key (user_group_id) references
	// user_group (id));

	// create table solutions (id INT PRIMARY KEY AUTO_INCREMENT, created
	// DATETIME, updated DATETIME, description TEXT, excercise_id int, user_id
	// int,
	// constraint solutions_excercise_id_id_fk FOREIGN KEY (excercise_id)
	// references excercises(id)
	// constraint solutions_user_id_id_fk FOREIGN KEY (user_id) references
	// users(id));

	// insert into user_groups(name) values ('normal'), ('admin');
	// insert into excercises (title, description) values ('zadanie1', 'opis1'),
	// ('zadania2', 'opis2'), ('zadanie3', 'opis3');
	// insert into users (user_group_id, username, password, email) values (1,
	// 'jhon', '', 'jhon@gmail.com');
	// insert into solutions (created, updated, description, excercise_id,
	// user_id) values (Now(), NOW(), 'opis zadania 1', 1, 1);

	/* PONIEZEJ METODY KLASY USER SLUZACE DO KOMUNIKACJI Z BAZA DANYCH!!!!! */

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

	@Override // utowrzone przez sourse -> generete toString
	public String toString() {
		return "Users [id=" + id + ", user_group_id=" + user_group_id + "\n " + " username=" + username + ", password="
				+ password + ", email=" + email + "]" + "\n";
	}

	public static void main(String[] args) throws SQLException {
		Connection conn = Connector.getConnection();
		System.out.println(Arrays.toString(loadAll(Connector.getConnection())));
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
							Users deleteUser = loadUserById(conn, userId);
							deleteUser.delete(conn);
						}
					} else {
						Users deleteUser = loadUserById(conn, userId);
						deleteUser.delete(conn);
					}

				} else if (input.equals("quit")) {
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * przypisanie user do grupy, i trzeba za kazdym razem gdy bedziemy
		 * tworzyc
		 */
		/* nowego uzytkownika trzeba bedzie go przypisac do grupy */

		//
		// Users user3 = loadUserById(conn, 4);
		// if (user != null) {
		// user3.setUsername("jhony5");
		// user3.setEmail("jhony5@gmail.com");
		// user3.saveToDB(conn);
		//
		// user.delete(conn);//do usuniecia uzytkownika o id4
		// }

		// Users user5 = new Users("Alicja", "francja", "alicja.frog@o2.pl");
		// user5.setUser_group_id(2);
		// user5.setUsername("Alicja");
		// user5.setEmail("alicja.frog@o2.pl");
		// user5.saveToDB(conn);
		/* haslo nie uswatiamy, bo mamy na to setPassword ktory go hashuje */

	}
}
