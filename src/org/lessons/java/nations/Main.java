package org.lessons.java.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {

		String url = "jdbc:mysql://localhost:3300/db_nations";
		String user = "root";
		String password = "root";

		milestone1(url, user, password);

	}

	public static void milestone1(String url, String user, String password) {

		try (Connection con = DriverManager.getConnection(url, user, password)) {

			System.out.println("Connessione effettuata!");

			String sql = "SELECT  c.name AS country_name,c.country_id , r.name AS region_name, cont.name AS continent_name\r\n"
					+ "FROM countries c, regions r , continents cont\r\n"
					+ "WHERE c.region_id = r.region_id AND cont.continent_id = r.continent_id \r\n"
					+ "ORDER BY c.name;";

			try (PreparedStatement statement = con.prepareStatement(sql)) {

				try (ResultSet rs = statement.executeQuery()) {

					while (rs.next()) {

						String countryName = rs.getString("country_name");
						int countryId = rs.getInt("country_id");
						String regionName = rs.getString("region_name");
						String continentName = rs.getString("continent_name");

						System.out.print("Nazione: " + countryName);
						System.out.print(" | ID: " + countryId);
						System.out.print(" | Regione: " + regionName);
						System.out.println(" | Continente: " + continentName);

					}

				}

			}

		} catch (SQLException se) {
			se.printStackTrace();
		}

	}

}
