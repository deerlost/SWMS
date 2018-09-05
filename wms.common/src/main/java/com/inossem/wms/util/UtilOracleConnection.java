package com.inossem.wms.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class UtilOracleConnection {
	public static Connection getConnection() {
		Connection conn = null;
		try {
			String driver = UtilProperties.getInstance().getOracleDriver();

			String url = UtilProperties.getInstance().getOracleUrl();

			String username = UtilProperties.getInstance().getOracleUserName();

			String password = UtilProperties.getInstance().getOraclePassword();

			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
