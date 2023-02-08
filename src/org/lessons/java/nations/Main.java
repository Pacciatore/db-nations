package org.lessons.java.nations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		String url = "jdbc:mysql://localhost:3300/db_nations";
		String user = "root";
		String password = "root";

		milestone2(url, user, password);

		// milestone3(url, user, password);

		getLanguageByCountryId(url, user, password);

	}

	public static void milestone2(String url, String user, String password) {

		try (Connection con = DriverManager.getConnection(url, user, password)) {

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

	public static void milestone3(String url, String user, String password) {

		Scanner s = new Scanner(System.in);

		System.out.print("Inserire nazione da ricercare: ");
		String countryQuery = s.nextLine();

		try (Connection con = DriverManager.getConnection(url, user, password)) {

			String sql = "SELECT  c.name AS country_name,c.country_id , r.name AS region_name, cont.name AS continent_name\r\n"
					+ "FROM countries c, regions r , continents cont\r\n"
					+ "WHERE c.region_id = r.region_id AND cont.continent_id = r.continent_id \r\n"
					+ "AND c.name LIKE ? \r\n" + "ORDER BY c.name;";

			try (PreparedStatement statement = con.prepareStatement(sql)) {

				statement.setString(1, "%" + countryQuery + "%");

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

		s.close();

	}

	public static void getLanguageByCountryId(String url, String user, String password) {

		System.out.println("----------------------------------------------------");
		System.out.println("RICERCA LINGUE IN UNA NAZIONE");

		Scanner s = new Scanner(System.in);

		boolean correctFormat = false;
		int countryQuery = 0;

		do {
			try {
				System.out.print("\nInserire ID della nazione da ricercare (da 1 a 239): ");
				countryQuery = Integer.parseInt(s.nextLine());
				correctFormat = true;
			} catch (Exception e) {
				System.out.println("Inserire un numero intero!");
				correctFormat = false;
			}
		} while (!correctFormat);

		try (Connection con = DriverManager.getConnection(url, user, password)) {

			String sql = "SELECT l.`language` AS country_language  , cs.population AS country_population, cs.gdp AS country_gdp, c.name AS country_name\r\n"
					+ "FROM countries c , country_languages cl , languages l , country_stats cs \r\n"
					+ "WHERE c.country_id = cl.country_id AND cl.language_id = l.language_id \r\n"
					+ "	AND cs.country_id  = c.country_id \r\n" + "AND c.country_id = ? AND cs.`year` = 2017\r\n"
					+ "ORDER BY c.name;";

			try (PreparedStatement statement = con.prepareStatement(sql)) {

				statement.setInt(1, countryQuery);

				try (ResultSet rs = statement.executeQuery()) {

					// Uso del 2017 per avere una sola popolazione e pil
					System.out.println("\nAnno di riferimento: 2017");

					if (rs.next()) {
						String countryName = rs.getString("country_name");
						System.out.println("Nazione ricercata: " + countryName.toUpperCase());

						BigDecimal countryGdp = rs.getBigDecimal("country_gdp");
						System.out.println("\nPIL: " + gdpFormatter(countryGdp));

						int countryPopulation = rs.getInt("country_population");
						System.out.println("Popolazione: " + String.format("%,d", countryPopulation));
					}

					System.out.println("\nLingue parlate: ");

					while (rs.next()) {

						String countryLanguage = rs.getString("country_language");
						System.out.print(countryLanguage + " | ");

					}

				}

			}

		} catch (SQLException se) {
			se.printStackTrace();
		}

		s.close();

	}

	public static String gdpFormatter(BigDecimal gdp) {

		DecimalFormat df = new DecimalFormat("#,###.00€");
		df.setRoundingMode(RoundingMode.HALF_EVEN);

		return df.format(gdp);

	}

}
