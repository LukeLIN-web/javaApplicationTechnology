package qqserv;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;



public class qqserv_main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		webServ w = new webServ();
		w.start();
	}
}

class webServ extends Thread {
	ServerSocket serverSocket = null;//服务器
	//所有客户端的输出管道
	private Vector<DataOutputStream> clients = new Vector<DataOutputStream>();
	boolean file = false;
	
	public webServ() {
		// TODO Auto-generated constructor stub
		try {
			serverSocket = new ServerSocket(3080, 100);//端口3080，请求连接的客户端最多100个
		}catch(Exception e) {
			System.out.println("IOException:"+e);
			return;
		}
	}
	
	public void run() {
		while(true) {
			try {
				//接受连接
				System.out.println("\n\n"+"accepte...");
				Socket socket = serverSocket.accept();
				socket.setKeepAlive(true);
				//打开输入输出流
				DataOutputStream remoteOut = 
						new DataOutputStream(
						socket.getOutputStream());
				if(!clients.contains(remoteOut)) {
					clients.addElement(remoteOut);
					DataInputStream remoInput = 
							new DataInputStream(
							socket.getInputStream());
					//新线程专程监听该客户端
					new ServerHelder(remoInput, remoteOut).start();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class ServerHelder extends Thread{//另起线程监听此客户端输入
		DataInputStream remoteInput;
		DataOutputStream remoteOutput;
		login_table login_table = null;
		
		ServerHelder(DataInputStream i, DataOutputStream o){
			remoteInput = i;
			remoteOutput = o;
		}
		
		public void run() {
			try {
				while(true) {
					String buf = null;
					if(file != true)
						buf = remoteInput.readUTF();//读取
					else {
						file = false;
						continue;
					}

					System.out.println(buf);
					if(buf.contains("login")) {//判断登陆成功吗？
						String account = buf.substring(buf.indexOf(':')+1,buf.indexOf('&'));
						String passport_client = buf.substring(buf.indexOf('&')+1);
						String passport_database = null;
						String name = null;
						int id;
						if(account.equals("")) {//未传值
							remoteOutput.writeUTF("login:failed");
							//登陆失败，不hold连接
							clients.removeElement(remoteOutput);
							continue;
						}
						login_table = new login_table();
						id = login_table.selectIDbyAccount_number(Long.valueOf(account));
						if(id<0) {
							remoteOutput.writeUTF("login:failed");
							//登陆失败，不hold连接
							clients.removeElement(remoteOutput);
							continue;
						}
						name = login_table.vt.get(id).account_name;
						passport_database = login_table.vt.get(id).passport;
						
						if(passport_client.equals(passport_database))
							remoteOutput.writeUTF("login:success&"+name+"&"+account);
						else {
							remoteOutput.writeUTF("login:failed");
							//登陆失败，不hold连接
							clients.removeElement(remoteOutput);
						}
					}else if(buf.contains("register")) {//注册帐号
						String account_name = buf.substring(buf.indexOf(':')+1,buf.indexOf('&'));
						String passport = buf.substring(buf.indexOf('&')+1);
						login_table = new login_table();
						long account = login_table.add(passport, account_name);
						if(account>=0)
							remoteOutput.writeUTF("register:"+account+"&"+account_name);
						else
							remoteOutput.writeUTF("register:failed");
					}else if(buf.contains("chat:")){
						//发送聊天信息--string\image\file
						broadcast(buf);//发送给所有hold连接的用户
					}else if(buf.contains("image:")||buf.contains("file:")) {
						file = true;
						//先收到信息头
						broadcast(buf);//发送给所有hold连接的用户
						//然后接受文件
						String temp = buf.substring(
								buf.indexOf("<size>")+"<size>".length(), buf.indexOf("</size>"));
						int len = Integer.valueOf(temp);
						byte[] b = new byte[len];
						remoteInput.read(b);
						broadcast(b);
					}
				}
			}
			catch (SocketException e) {
				clients.removeElement(remoteOutput);//客户端关闭了
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void broadcast(String buf) {//给所有连接过的发消息
			DataOutputStream dout = null;
			Enumeration<DataOutputStream> e = clients.elements();
			while(e.hasMoreElements()) {
				dout = e.nextElement();
				try {
					dout.writeUTF(buf);
				}catch (IOException eio) {
					eio.printStackTrace();
				}
			}
		}
		private void broadcast(byte[] buf) {//给所有连接过的发消息
			DataOutputStream dout = null;
			Enumeration<DataOutputStream> e = clients.elements();
			while(e.hasMoreElements()) {
				dout = e.nextElement();
				try {
					dout.write(buf);
				}catch (IOException eio) {
					eio.printStackTrace();
				}
			}
		}
	}
}