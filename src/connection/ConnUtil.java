package connection;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnUtil {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("����̹� �˻� ����");
		} catch (ClassNotFoundException e) {
			System.err.println("����̹� �˻� ����");
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "mytest","mytest");
	}
}