package qqserv;

import java.sql.*;
import java.util.Vector;

public class login_table {
	//vector
	Vector<Record> vt = new Vector<Record>();
	
	//���ݿ��������
	Connection connection;
	Statement statement;
	ResultSet rs;
	
	//���캯��--�������Ӧ��vector
	login_table() {
		//������
		int id;
		long account_number;
		String passport, account_name;		
		String sql = "select * from login";
		
		init();//�������ݿ�
		//ִ��sql��䲢Ϊ���Ը�ֵ
		try {
			rs = statement.executeQuery(sql);
			//��֤ID��vt��indexһ�£���һ�´���Login(0, null, null, null)��ȫ
			vt.add(new Record(0, 0, null, null));
			//��ȡÿ��record���ݣ������ɶ�ӦLogin��
			while(rs.next()) {
				id = rs.getInt(1);
				account_number = rs.getLong(2);
				passport = rs.getString(3);
				account_name = rs.getString(4);
				Record r = new Record(id, account_number, passport, account_name);
				while(id!=vt.size())//��index��id�����Ͳ������
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
	
	//�������ݿ�
	private void init() {
		try {
			//����jdk8�Ժ�û����jdbc-odbc�ţ��������ʹ����access��jdbc����
			String driveName = "com.hxtt.sql.access.AccessDriver";
			String url = "Jdbc:Access:///account.mdb";
			//�������ݿ�
			Class.forName(driveName);
			connection = DriverManager.getConnection(url);
			//����SQL���
			statement = connection.createStatement();			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println(e.getNextException());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	//���ݿ����
	
	//��account����number������Ӧ��ID
	public int selectIDbyAccount_number(long account_number) {
		//��idΪ���Ҷ������record	
		String sql = "select ID from login where account_number = "+account_number;
		
		//���ݿ����
		try {
			rs = statement.executeQuery(sql);
			//����ID��account_numberһһ��Ӧ����һ�����
			if(rs.next())
				return rs.getInt(1);
			else 
				return -1;
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}			
	}
	
	//id���ڼ�����id��account_numberϵͳ�Զ���ֵ�����ɸ���
	public int set(int id, String passport, String account_name) {
		//��idΪ���Ҷ������record	
		String sql = "update login set passport=\'"+passport
				+"\',account_name=\'"+account_name
				+"\' where id="+id;			
		//���ö�Ӧ�����޸�ֵ
		vt.get(id).set_record(passport, account_name);
		//�������ݿ�
		try {
			return statement.executeUpdate(sql);		
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}	
	}
	//�����¼
		public long add(String passport, String account_name) {
			//�����µ�id��account_number
			long new_account_number = vt.lastElement().account_number + 1;
			int new_id = vt.lastElement().id + 1;
			//���ݿ��в����Ӧ�ļ�¼
			String sql = "insert into login(ID, account_number, passport, account_name) values("
					    +new_id+","+new_account_number+",\'"+passport+"\',\'"+account_name+"\')";
			//����vector
			Record r = new Record(new_id, new_account_number, passport, account_name);
			vt.add(r);
			
			//�������ݿ�
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
	//ɾ����¼
	public int delect(int id) {
		//���ݿ���ɾ����Ӧ�ļ�¼
		String sql = "delete from login where id="+id;
		//����vector�����Ϊɾ��
		if(id == vt.size() - 1)//��Ϊĩβ����ֱ��ɾ��
			vt.remove(id);
		else
			vt.get(id).tonull();//����Ϊĩβ��Ϊ����idһ�£���ɾ�������
		//�������ݿ�
		try {
			return statement.executeUpdate(sql);		
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	 //login ��һ��record
	public class Record{
		//������
		int id;
		long account_number;
		String passport, account_name;

		//���캯��--����ֵ�������¼
		Record(int id, long account_number, String passport, String account_name){
			this.id = id;
			this.account_number = account_number;
			this.account_name = account_name;
			this.passport = passport;
		}
		//ID��account_number�Զ����䲻�ܸ���
		public void set_record(String passport, String account_name) {			
			//�������ڱ���
			this.passport = passport;
			this.account_name = account_name;
		}
		//���������¼�Ѿ���ɾ����
		public void tonull() {
			id = 0;
			account_number = 0;
			account_name = null;
			passport = null;
		}
		public String toString() {//��¼תΪ�ַ���
			if(account_name == null)
				return null;
			String result = String.format(
					"ID: %d, account_number: %d, account_name: %s, passport: %s",
					id, account_number,account_name,passport.replace('1', '*').replace('5', '*'));
			return result;
		}
	}
} 
