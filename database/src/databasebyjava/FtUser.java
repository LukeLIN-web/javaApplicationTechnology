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
       			id = rs.getInt("ftid"); 
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

    public void delete(int ftid) {
		String sql = "DELETE FROM ft_user WHERE ftid = ? ";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setInt(1, ftid);
			System.out.print("\n  name  = " );
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

    // password 不能change 12.31 16:06 为什么呢?
    public void setPassword(int ftid,String pwd) {
		String sql = "update ft_user set password = ? where ftid = ?";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
		System.out.print(sql);
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
			 preSt = conn.prepareStatement(sql);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, pwd);
			System.out.print("id =  "+String.valueOf(ftid));
			preSt.setInt(2, ftid);
			System.out.print("preset  = "+preSt.toString());
			 System.out.print(" set password  success! change "+preSt.executeUpdate()+" lines");
		} catch (SQLException e) { 
		e.printStackTrace();
		}
    }

  

    public void setName(int id,String name) {
		String sql = "update ft_user set name = ? where ftid = ?";//insert into ft_user values(2,'wangqiang' ,12,'Beijingwang');
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
			 System.out.print(" set name success! change "+preSt.executeUpdate()+" lines");
		} catch (SQLException e) { 
		e.printStackTrace();
		}
    }
  
    public void add(String name,String pwd ) {
		String sql = "insert into ft_user(name,password) values( ?  , ?)";//insert into ft_user(name,password) values( 'user1', '123');
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
			preSt.setString(2, pwd);// 你可以通过把pwd改成password来统一设置密码为111111
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
                ",pwd ="+this.pwd+
                "}\n";// you should use String s2=new String(s1); id will change ,
    }// you can't use id to copy string in the method, name will change out of the method
    
    public static void main(String[] args) {
		 	FtUser p = new FtUser();
		    //p.add("user2","pass2");//注册, 已完成
		 	//p.setPassword(102, "pass101");//修改密码, 已完成
		 	//p.setName(1, "usertry");// 修改名字, 已完成
			//p.delete(2);// 通过ftid 删除已完成
		 	System.out.println(p.vt); // show all line in the table
		    System.out.println(" finished");
		}
}
