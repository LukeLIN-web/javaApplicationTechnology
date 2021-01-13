package qqserv;

import java.sql.*;
import java.util.Vector;

public class login_table {
	//vector
	Vector<Record> vt = new Vector<Record>();
	
	//数据库操作变量
	Connection connection;
	Statement statement;
	ResultSet rs;
	
	//构造函数--创建表对应的vector
	login_table() {
		//表内列
		int id;
		long account_number;
		String passport, account_name;		
		String sql = "select * from login";
		
		init();//连接数据库
		//执行sql语句并为属性赋值
		try {
			rs = statement.executeQuery(sql);
			//保证ID与vt的index一致，不一致处用Login(0, null, null, null)补全
			vt.add(new Record(0, 0, null, null));
			//获取每条record内容，并生成对应Login类
			while(rs.next()) {
				id = rs.getInt(1);
				account_number = rs.getLong(2);
				passport = rs.getString(3);
				account_name = rs.getString(4);
				Record r = new Record(id, account_number, passport, account_name);
				while(id!=vt.size())//若index与id不符就插入空项
					vt.add(new Record(0, 0, null, null));
				vt.add(r);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println(e.getNextException());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//连接数据库
	private void init() {
		try {
			//由于jdk8以后没有了jdbc-odbc桥，因此下载使用了access的jdbc驱动
			String driveName = "com.hxtt.sql.access.AccessDriver";
			String url = "Jdbc:Access:///account.mdb";
			//连接数据库
			Class.forName(driveName);
			connection = DriverManager.getConnection(url);
			//创建SQL语句
			statement = connection.createStatement();			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println(e.getNextException());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	//数据库操作
	
	//用account——number检索对应的ID
	public int selectIDbyAccount_number(long account_number) {
		//以id为查找对象更新record	
		String sql = "select ID from login where account_number = "+account_number;
		
		//数据库查找
		try {
			rs = statement.executeQuery(sql);
			//由于ID和account_number一一对应，仅一个结果
			if(rs.next())
				return rs.getInt(1);
			else 
				return -1;
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}			
	}
	
	//id用于检索，id和account_number系统自动赋值，不可更改
	public int set(int id, String passport, String account_name) {
		//以id为查找对象更新record	
		String sql = "update login set passport=\'"+passport
				+"\',account_name=\'"+account_name
				+"\' where id="+id;			
		//调用对应函数修改值
		vt.get(id).set_record(passport, account_name);
		//更新数据库
		try {
			return statement.executeUpdate(sql);		
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}	
	}
	//插入记录
		public long add(String passport, String account_name) {
			//计算新的id和account_number
			long new_account_number = vt.lastElement().account_number + 1;
			int new_id = vt.lastElement().id + 1;
			//数据库中插入对应的记录
			String sql = "insert into login(ID, account_number, passport, account_name) values("
					    +new_id+","+new_account_number+",\'"+passport+"\',\'"+account_name+"\')";
			//更新vector
			Record r = new Record(new_id, new_account_number, passport, account_name);
			vt.add(r);
			
			//更新数据库
			try {
				if(statement.executeUpdate(sql)>=0)
					return new_account_number;		
				else
					return -1;
			}catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}	
	//删除记录
	public int delect(int id) {
		//数据库中删除对应的记录
		String sql = "delete from login where id="+id;
		//更新vector，标记为删除
		if(id == vt.size() - 1)//若为末尾，可直接删除
			vt.remove(id);
		else
			vt.get(id).tonull();//若不为末尾，为了与id一致，不删除仅标记
		//更新数据库
		try {
			return statement.executeUpdate(sql);		
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	 //login 的一条record
	public class Record{
		//表内列
		int id;
		long account_number;
		String passport, account_name;

		//构造函数--输入值，构造记录
		Record(int id, long account_number, String passport, String account_name){
			this.id = id;
			this.account_number = account_number;
			this.account_name = account_name;
			this.passport = passport;
		}
		//ID和account_number自动分配不能更改
		public void set_record(String passport, String account_name) {			
			//更新类内变量
			this.passport = passport;
			this.account_name = account_name;
		}
		//代表这个记录已经被删掉了
		public void tonull() {
			id = 0;
			account_number = 0;
			account_name = null;
			passport = null;
		}
		public String toString() {//记录转为字符串
			if(account_name == null)
				return null;
			String result = String.format(
					"ID: %d, account_number: %d, account_name: %s, passport: %s",
					id, account_number,account_name,passport.replace('1', '*').replace('5', '*'));
			return result;
		}
	}
} 
