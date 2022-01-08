package databasebyjava;
 import java.sql.*;
import java.util.Vector;
public class t_user {  
	Statement s = null;
	String username = "db_user1";
	String password = "111111";
	String url = "jdbc:derby:Derby_data\\\\dedb";
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
	try {
	Connection conn = DriverManager.getConnection(url,username,password);
	s = conn.createStatement();
		} catch (SQLException e) { 
		e.printStackTrace();
		}
	}
     public String toString() {
return  UUID+NAME+AGE+ADDRESS ;	}
public void add(	String	UUID,	String	NAME,	int	AGE,	String	ADDRESS)
{	String sql = "insert into t_user (\"UUID\",\"NAME\",\"AGE\",\"ADDRESS \"  ) values ("+UUID+","+NAME+","+String.valueOf(AGE)+","+ADDRESS+  ") " ; 	
	System.out.print(sql);
		try{
		Connection conn = DriverManager.getConnection(url,username,password);
			 Statement s  = conn.createStatement();
	s.executeUpdate(sql);	} catch (SQLException e) { 
		e.printStackTrace();
		}	
}
public void delete(int id){
String sql = "DELETE FROM people WHERE id = " +  String.valueOf(id); 
System.out.print(sql);
		try {
		Connection conn = DriverManager.getConnection(url,username,password);
		Statement s  = conn.createStatement();
	int line = s.executeUpdate(sql);
if(line > 0)
			 System.out.print(" delete  success! change "+line+" lines");
			else 
				System.out.print("\n don't delete anything! change "+line+" lines");
			} catch (SQLException e) { 
		e.printStackTrace();
		}
	
}

}

