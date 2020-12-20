import java.sql.*;
import java.util.Vector;
public class t_user {  
	Statement s = null;
	String username = "db_user1";
	String password = "111111";
	String	UUID;
	String	NAME;
	int	AGE;
	String	ADDRESS;
	t_user(){}
	t_user(int id){
		Init();
		try{
		ResultSet rs  = s.executeQuery("select * from t_user");  
		while( rs.next() ) { 
	UUID = rs.getString(1);
	NAME = rs.getString(2);
	AGE= rs.getInt(3);
	ADDRESS = rs.getString(4);
		}
	}catch (Exception e) {
		e.printStackTrace();}
	}
	private void Init() {
	String url = "jdbc:derby:Derby_data\\\\dedb";
	try {
	Connection conn = DriverManager.getConnection(url,username,password);
	s = conn.createStatement();
		} catch (SQLException e) { 
		e.printStackTrace();
		}
	}
}

