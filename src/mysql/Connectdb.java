package mysql;
import java.sql.*;
import java.util.Properties;

import commandcentral.Config;

public class Connectdb {
	private final String userName = Config.dbUsername;
	private final String password = Config.dbPassword;
	private final String serverName = Config.dbHost;
	private final int portNumber = Config.dbPort;
	private final String dbName = Config.dbName;
	
	public Connection getConnection() {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		try {
			conn = DriverManager.getConnection("jdbc:mysql://"
					+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
					connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	

}
