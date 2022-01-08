package fangT;
/*BuildClassManually  12.30  you can use this class for register add user 
and log in name corresponding to password


*/
import java.sql.*;
import java.util.Iterator;
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
    public int getAllName(int ftid) {
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
    
    public String getPassword(int ftid) throws SQLException  {
   		String sql = "select * from ft_user WHERE ftid = ? ";//查到那一行
   		String tmp = "";
   		try {
   			Connection conn = DriverManager.getConnection(url,username,password);
   			preSt = conn.prepareStatement(sql);
   			preSt.setInt(1, ftid);
   			rs = preSt.executeQuery();
   			System.out.print(" query success!   "+rs);
    			while (rs.next()) {
       				tmp = rs.getString("password");
    			}
   		} catch (SQLException e) { 
   		e.printStackTrace();
   		}
        return tmp;
    }
    
    public String getName(int ftid) throws SQLException  {
   		String sql = "select * from ft_user WHERE ftid = ? ";//查到那一行
   		String tmp = "";
   		try {
   			Connection conn = DriverManager.getConnection(url,username,password);
   			preSt = conn.prepareStatement(sql);
   			preSt.setInt(1, ftid);
   			rs = preSt.executeQuery();
   			System.out.print(" query success!   ");
    			while (rs.next()) {
       				tmp = rs.getString("name");
    			}
   		} catch (SQLException e) { 
   			e.printStackTrace();
   		}
        return tmp;
    }
    
    public int getNewestId() {
		String findIdsql = "select max(ftid) from ft_user";
		 int tmp = 0;
		try {
			Connection conn = DriverManager.getConnection(url,username,password);
   			s  = conn.createStatement();
   			rs = s.executeQuery(findIdsql);
   			while (rs.next()) {
   				tmp = rs.getInt(1);
			}
			System.out.println("the latest id:  "+tmp);
		} catch (SQLException e) { 
		e.printStackTrace();
		}
        return tmp;
    }
    
    public String getUserName() {
    	return this.name;
    }
    public int getFtid() {
    	return this.id;
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
			 s = conn.createStatement();
		} catch (SQLException e) { 
		e.printStackTrace();
		}
		try {
			preSt.setString(1, name);
			System.out.print("id =  "+String.valueOf(id)+"name  = " +name+"passwrod = "+pwd);//在服务器端输出密码没关系,只要不发送给客户端
			preSt.setString(2, pwd);// 你可以通过把pwd改成password来统一设置密码为111111
			 System.out.print(" user add success!"+preSt.executeUpdate()); // 要给一个信息给前端显示,在messagebox
			 rs = preSt.getGeneratedKeys(); 
		} catch (SQLException e) { 
		e.printStackTrace();
		}
   	 	System.out.print(" find id!"+rs);
   	}
    
    
    @Override
    public String toString() {
        return "ft_user{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ",pwd ="+this.pwd+
                "}\n";// you should use String s2=new String(s1); id will change ,
    }// you can't use id to copy string in the method, name will change out of the method
    
    public static void main(String[] args) throws SQLException {
		 	FtUser p = new FtUser();
		 	int id2 = 0 ;
//		    try {
//				id2 = p.add("user4","123");
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}//注册, 已完成
		    boolean flag = false;
		    for(Iterator<FtUser> ite = p.vt.iterator(); ite.hasNext();) {
		    	FtUser tmpft = ite.next();// we cannot use next() twice .
				 int tmpid = tmpft.getFtid();
		        if (tmpid == 1903 ) {
					//如果有一样的,  那就报告重复, 通知
		        	System.out.println("	password =  "+ tmpft.getPassword(tmpid));
					if (tmpft.getPassword(tmpid).equals("123")) {
						flag = true;
						System.out.println("	password =  "+ tmpft.getPassword(tmpid));
					}
				}
				System.out.println("id = "+ tmpid);
		    }
		 	//p.setPassword(102, "pass101");//修改密码, 已完成
		 	//p.setName(1, "usertry");// 修改名字, 已完成
			//p.delete(1703);// 通过ftid 删除已完成
		 	System.out.println(p.vt); // show all line in the table
		    System.out.println(" finished\n");
		//    System.out.println(p.getNewestId());
		    System.out.println(p.getName(1603));
		}
}
