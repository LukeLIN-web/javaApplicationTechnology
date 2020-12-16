package dto;

import java.sql.*;
import java.util.Vector;

public class People {
    private int id;
    private String name;
    private int age;
    private String address;
	String username = "db_user1";
	String password = "111111";
    Vector<People> vt = new Vector<People>(); 
    Statement s = null;
    ResultSet rs  = null;
    private PreparedStatement preSt;
    // from database Derby_data\\\\dedb  ,table people, create class people
    public People() {
    	Init();
    	try {
    		rs= s.executeQuery("select * from people");
    		while(rs.next() ) {
    		//	System.out.println("age is "+rs.getInt("age"));
    			id = rs.getInt("id"); 
    			String name = rs.getString("name"); 
    		//	System.out.println(rs.getString("name"));// see whether or not has correct 
    			age = rs.getInt("age");
    			String add = rs.getString("address"); 
    			People p  = new People(id, name, age, add); // pass into traditional constructor 
    			vt.add(p);
    		}
    	}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
    }

    private void Init() {
		// TODO Auto-generated method stub
    	String url = "jdbc:derby:Derby_data\\\\dedb";
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
     //   System.out.println("thisname "+this.name);
        this.age = age;
        this.address = address;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
    	String url = "jdbc:derby:Derby_data\\\\dedb";
   		String sql = "select age from people";
   		try {
   			 Connection conn = DriverManager.getConnection(url,username,password);
   			 s = conn.createStatement();
   			 rs = s.executeQuery(sql);
   			 System.out.print(" add success!"+rs);
   		} catch (SQLException e) { 
   		e.printStackTrace();
   		}
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public void add(int id,String name,int age,String add ) {
        String url = "jdbc:derby:Derby_data\\\\dedb";
		String sql = "insert into people values(?,'?' ,?,'?')";//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, String.valueOf(id));
			System.out.print(preSt.toString());
			preSt.setString(2, name);
			
			preSt.setString(3, String.valueOf(age));
			System.out.print(preSt.toString());
			preSt.setString(4, add);
			System.out.print(preSt.toString());
			 System.out.print(" add success!"+preSt.executeUpdate());
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
                '}';// you should use String s2=new String(s1); id will change ,
    }// you can't use id to copy string in the method, name will change out of the method
    
    public static void main(String[] args) {
		 	People p = new People();
		    System.out.println(p.vt);
		    p.add(3,"zhang",13,"hangzhou");//insert into people values(2,'wangqiang' ,12,'Beijingwang');
		   // p.add("string");
		    System.out.println(" finished");
		}
}
