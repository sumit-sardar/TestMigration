package com.ctb.contentBridge.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClosableHelper {

	public static void close(Statement ps, ResultSet rs) {
		close(ps);
		close(rs);
	}

	public static void close(Statement ps) {

		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {

			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {

			}
		}
	}

	public static void close(InputStream closable) {
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException e) {
			}
		}

	}

	public static void close(OutputStream closable) {
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException e) {
			}
		}

	}

}
