package databasebyjava;
//BuildClassManually  12.30 
import java.sql.*;
import java.util.Vector;

public class FtUser {
	Statement s = null;
	String url = "jdbc:derby:Derby_data\\\\dedb";
    private String name;
    private String pwd;
    private int id;
	String username = "db_user1";
	String password = "111111";
    Vector<FtUser> vt = new Vector<FtUser>(); 
    ResultSet rs  = null;
    private PreparedStatement preSt;
    // from database Derby_data\\\\dedb  ,table ftuser, create class ftuser
    public FtUser() {
    	Init();
    	try {
    		rs= s.executeQuery("select * from ft_user");
    		while(rs.next() ) {
       			id = rs.getInt("id"); 
    			String tmpname = rs.getString("name"); 
    			String pasw = rs.getString("password"); 
    			FtUser p  = new FtUser(id,tmpname, pasw); // pass into traditional constructor 
    			vt.add(p);
    		}
    	}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
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

	public FtUser(int id, String name, String pwd) {
        this.id = id;
        this.name = new String(name);
        this.pwd = new String(pwd);
    }


    public void setId(int id,String name) {
    	String sql = "select * from ft_user";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
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
    // 12.31 
    public void deletebyid(int id) {
    	String sql = "select * from ft_user";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);// return a updatable resultset
			ResultSet rsup =  stat.executeQuery(sql);
			System.out.print("\n  id  = " +id + rsup.getConcurrency());
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
		String sql = "DELETE FROM ft_user WHERE name = ? ";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
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
    // user can search all user name from id 
    public int getName() {
   		String sql = "select name from ft_user";
   		try {
   			 Connection conn = DriverManager.getConnection(url,username,password);
   			 s = conn.createStatement();
   			 rs = s.executeQuery(sql);
   			 System.out.print(" query success!"+rs);
   		} catch (SQLException e) { 
   		e.printStackTrace();
   		}
        return id;
    }

    public void setAge(int age,String name) {
		String sql = "update ft_user set age = ? where name = ?";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
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

  

    public void setName(int id,String name) {
		String sql = "update ft_user set name = ? where id = ?";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, name);
			System.out.print("id =  "+String.valueOf(id)+"name  = " +name);
			preSt.setInt(2, id);
			System.out.print("preset  = "+preSt.toString());
			 System.out.print(" add success! change "+preSt.executeUpdate()+" lines");
		} catch (SQLException e) { 
		e.printStackTrace();
		}
    }
  
    public void add(String name,String pwd ) {
		String sql = "insert into ft_user values( ?  , ?)";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, name);
			System.out.print("id =  "+String.valueOf(id)+"name  = " +name);
			preSt.setString(2, password);
			 System.out.print(" user add success!"+preSt.executeUpdate()); // 要给一个信息给前端显示,在messagebox
		} catch (SQLException e) { 
		e.printStackTrace();
		}
	}
    @Override
    public String toString() {
        return "ft_user{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                "}\n";// you should use String s2=new String(s1); id will change ,
    }// you can't use id to copy string in the method, name will change out of the method
    
    public static void main(String[] args) {
		 	FtUser p = new FtUser();
		   // p.add(5,"wangwu",15,"hangzhouyuquan");//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
		 	//p.setAddress("hangzhouzjg","we");
			//p.setId(5, "wangwu");
			//p.deletebyid(1);
		 	System.out.println(p.vt); // show all line in the table
		    //p.setAge(20, "we");
		    System.out.println(" finished");
		}
}
