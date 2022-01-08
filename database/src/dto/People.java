package dto;
// BuildClassManually  12.16 
import java.sql.*;
import java.util.Vector;

public class People {
	Statement s = null;
	String url = "jdbc:derby:Derby_data\\\\dedb";
    private int id;
    private String name;
    private int age;
    private String address;
	String username = "db_user1";
	String password = "111111";
    Vector<People> vt = new Vector<People>(); 
    ResultSet rs  = null;
    private PreparedStatement preSt;
    // from database Derby_data\\\\dedb  ,table people, create class people
    public People() {
    	Init();
    	try {
    		rs= s.executeQuery("select * from people");
    		while(rs.next() ) {
    			id = rs.getInt("id"); 
    			String name = rs.getString("name"); // see whether or not has correct 
    			age = rs.getInt("age");
    			String add = rs.getString("address"); 
    			People p  = new People(id, name, age, add); // pass into traditional constructor 
    			vt.add(p);
    		}
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void Init() {
    	try {
			 Connection conn = DriverManager.getConnection(url,username,password);
			 s = conn.createStatement();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
	}

	public People(int id, String name, int age, String address) {
        this.id = id;
        this.name = new String(name);
        this.age = age;
        this.address = address;
    }


    public void setId(int id,String name) {
    	String sql = "select * from people";//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);// return a updatable resultset
			ResultSet rsup =  stat.executeQuery(sql);
			System.out.print("\n  id  = " +id + name);
			while(rsup.next() ) {
				System.out.println("\n  rsup.getString(\"Name\")  = " + rsup.getString("Name"));
	    		  if(name.equals(rsup.getString("Name")) == true ) {   // you cannot use == in string comparation.
	    			  rsup.updateInt("Id", id);// change the row in rs
	    			  System.out.println("\n  id  = " + rsup.getInt("Id"));
	    			  rsup.updateRow();// send to the database, if you don't want to send, use cancelRowpUpdates() 
	    		  } // it cannot work out I don't know why?12.20 19:43
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}
    }
    
    public void deletebyid(int id) {
    	String sql = "select * from people";//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);// return a updatable resultset
			ResultSet rsup =  stat.executeQuery(sql);
			
			System.out.print("\n  id  = " +id +rsup.getConcurrency());
			while(rsup.next() ) {
				System.out.println("\n  rsup.getInt(\"id\")  " + rsup.getInt("id") );
	    		  if(id == rsup.getInt("id") ) {   // you cannot use == in string comparation.
	    			  System.out.println("\n delete id =   " + rsup.getInt("id") );
	    			  rsup.deleteRow();
	    		  } 
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}
    }

    public void delete(String name) {
		String sql = "DELETE FROM people WHERE name = ? ";//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, name);
			System.out.print("\n  name  = " +name);
			int line = preSt.executeUpdate();
			if(line > 0)
				System.out.print(" delete  success! change "+line+" lines");
			else 
				System.out.print("\n don't delete anything! change "+line+" lines");
		} catch (SQLException e) { 
			e.printStackTrace();
		}
    }

    public int getAge() {
   		String sql = "select age from people";
   		try {
   			 Connection conn = DriverManager.getConnection(url,username,password);
   			 s = conn.createStatement();
   			 rs = s.executeQuery(sql);
   			 System.out.print(" query success!"+rs);
   		} catch (SQLException e) { 
   		e.printStackTrace();
   		}
        return age;
    }

    public void setAge(int age,String name) {
		String sql = "update people set age = ? where name = ?";//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, String.valueOf(age));
			System.out.print("id =  "+String.valueOf(id)+"name  = " +name);
			preSt.setString(2, name);
			System.out.print("preset  = "+preSt.toString());
			System.out.print(" set age  success! change "+preSt.executeUpdate()+" lines");
		} catch (SQLException e) { 
		e.printStackTrace();
		}
    }

    public void setAddress(String address,String name) {
		String sql = "update people set address = ? where name = ?";//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		try {
			preSt.setString(1, address);
			System.out.print("id =  "+String.valueOf(id)+"name  = " +name);
			preSt.setString(2, name);
			System.out.print("preset  = "+preSt.toString());
			 System.out.print(" add success! change "+preSt.executeUpdate()+" lines");
		} catch (SQLException e) { 
			e.printStackTrace();
		}
    }
  
    public void add(int id,String name,int age,String add ) {
		String sql = "insert into people values(?, ?  ,?, ?)";//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, String.valueOf(id));
			System.out.print("id =  "+String.valueOf(id)+"name  = " +name);
			preSt.setString(2, name);
			preSt.setString(3, String.valueOf(age));
			System.out.print("preset  = "+preSt.toString());
			preSt.setString(4, add);
			System.out.print("preset  = "+preSt.toString());
			 System.out.print(" change address success!"+preSt.executeUpdate());
		} catch (SQLException e) { 
			e.printStackTrace();
		}
	}
    @Override
    public String toString() {
        return "People{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                "}\n";// you should use String s2=new String(s1); id will change ,
    }// you can't use id to copy string in the method, name will change out of the method
    
    public static void main(String[] args) {
		 	People p = new People();
		   // p.add(5,"wangwu",15,"hangzhouyuquan");//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		 	//p.setAddress("hangzhouzjg","we");
			//p.setId(5, "wangwu");
			p.deletebyid(1);
		 	System.out.println(p.vt); // show all line in the table
		    //p.setAge(20, "we");
		    System.out.println(" finished");
		}
}
