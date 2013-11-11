package com.ctb.lexington.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 395168 kingshukc
 *         ***************************************************************************
 *         This class is a DAO class which will be used to fetch set of rules
 *         and set of correct answers for each items. There is also a
 *         getConnection method to establish a connection to database.
 *         *************************************************************************
 */
public class GrRulesDAO {

	public static Connection getConnection() {

		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@mcsoas503-vip:1521:OASR51D1", "oas",
					"oasr5d");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void main(String[] args) {

		// try {
		// getConnection();
		// System.out.println("Connection Obtained");
		// new GrRulesDAO().getRulesSet("777");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	/*
	 * Fetch the rule set against each item
	 * 
	 */
	public String getRulesSet(String itemId) {
		String ruleSet = new String();
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("select GR_ITEM_RULES from GR_ITEM_RULES where GR_ITEM_ID=?");
			pstmt.setString(1, itemId.trim());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				ruleSet = rs.getString(1);
				System.out.println("Rule set:" + ruleSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ruleSet;
	}

	/*
	 * Fetch the correct answer against each rule
	 * 
	 */

	public String getCorrectAnswers(String itemId) {
		String correctAnswer = new String();
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("select GR_ITEM_CORRECT_ANSWER from GR_ITEM_RULES where GR_ITEM_ID=?");
			pstmt.setString(1, itemId.trim());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				correctAnswer = rs.getString(1);
				// System.out.println("Rule set:" + correctAnswer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return correctAnswer;
	}

	public String getGRResponse(String itemId) {
		Clob grResponse = null;
		ResultSet rs = null;
		Connection conn = null;
		StringBuilder sb = new StringBuilder();
		try {
			conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("select CONSTRUCTED_RESPONSE from ITEM_RESPONSE_CR where ITEM_ID=?");
			pstmt.setString(1, itemId.trim());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				grResponse = rs.getClob(1);
				// System.out.println("Rule set:" + correctAnswer);
			}

			// Processs the clob to get response
			if (grResponse == null) {
				System.out.println("No Valid Response or not GR item");

			} else {
				Reader reader = grResponse.getCharacterStream();
				BufferedReader br = new BufferedReader(reader);
				String line;
				while (null != (line = br.readLine())) {
					sb.append(line);
				}
				br.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println(":"+sb.toString()+":");
		return sb.toString();
	}
}
