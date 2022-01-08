package databasebyjava;
import java.sql.*;
// connect with the database directly 
public class DerbyFirstTry {
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";// register driver class
	private static String protocol = "jdbc:derby:";
	String dbName = "Derby_data\\dedb";
	String username = "db_user1";
	String password = "111111";
	static void loadDriver() {
	try {
		Class.forName(driver).getDeclaredConstructor().newInstance();// register driver class
		System.out.println("Loaded the appropriate driver");
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	public void doIt() {
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
	
		System.out.println("starting");
		try {
			 conn = DriverManager.getConnection(protocol + dbName ,username, password);
		} catch (SQLException e) {
		e.printStackTrace();
		}
	
		System.out.println("Connected to and created database " + dbName);
	
		try {
			s = conn.createStatement();
			rs = s.executeQuery("select * from  tty2");
		
			while (rs.next()) {
				   System.out.println(rs.getString(1));
	               System.out.println(rs.getString(2));
	               System.out.println(rs.getInt(3));
	               System.out.println(rs.getString(4));
	               System.out.print("zheshi  n"); // \n can be a newline
	               System.out.print("zheshi  r \r\n");
			}
		} catch (SQLException e1) {
		e1.printStackTrace();
		}
		try {
			conn.close();
			conn = null;
			s.close();
			s = null;
			rs.close();
			rs = null;
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DerbyFirstTry t = new DerbyFirstTry();
		DerbyFirstTry.loadDriver();
		t.doIt();
	}
}
