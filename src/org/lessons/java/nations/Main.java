package org.lessons.java.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {

		String url = "jdbc:mysql://localhost:3300/db_nations";
		String user = "root";
		String password = "root";

		try (Connection connessione = DriverManager.getConnection(url, user, password)) {

			System.out.println("Connessione effettuata!");

		} catch (SQLException se) {
			se.printStackTrace();
		}

	}

}
