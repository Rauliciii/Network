package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import model.User;

public class UsersRegistry {
	private static List<User> users = new LinkedList<User>();
	private static final String fileName = "users.txt";

	public UsersRegistry() {

	}

	private void updateUsersList() {
		users = getAllUsers();
	}

	public static List<User> getAllUsers() {
		try {
			return getUsersFromStream((new FileInputStream(fileName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

	public boolean addUser(User u) {
		users = UsersRegistry.getAllUsers();
		for (User user : users) {
			if (user.getNickName().equals(u.getNickName())) {
				return false;
			}
		}
		users.add(u);
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(fileName, true)))) {
			out.println(u.getNickName() + "");
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
		return true;
	}


	private static List<User> getUsersFromStream(InputStream inputStream)
			throws IOException {
		List<User> users = new LinkedList<>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		String line = null;
		while ((line = reader.readLine()) != null) {
			User e = new User(line);
			users.add(e);
		}
		return users;
	}

	public static void emptyFile() {
		Formatter f;
		try {
			f = new Formatter(fileName);
			f.format("");
			f.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		String s = "Users: ";
		updateUsersList();
		for (User user : users)
			s += user.toString() + ", ";
		return s;
	}
}
