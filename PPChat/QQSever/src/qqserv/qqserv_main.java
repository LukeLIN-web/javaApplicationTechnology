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
	ServerSocket serverSocket = null;//������
	//���пͻ��˵�����ܵ�
	private Vector<DataOutputStream> clients = new Vector<DataOutputStream>();
	boolean file = false;
	
	public webServ() {
		// TODO Auto-generated constructor stub
		try {
			serverSocket = new ServerSocket(3080, 100);//�˿�3080���������ӵĿͻ������100��
		}catch(Exception e) {
			System.out.println("IOException:"+e);
			return;
		}
	}
	
	public void run() {
		while(true) {
			try {
				//��������
				System.out.println("\n\n"+"accepte...");
				Socket socket = serverSocket.accept();
				socket.setKeepAlive(true);
				//�����������
				DataOutputStream remoteOut = 
						new DataOutputStream(
						socket.getOutputStream());
				if(!clients.contains(remoteOut)) {
					clients.addElement(remoteOut);
					DataInputStream remoInput = 
							new DataInputStream(
							socket.getInputStream());
					//���߳�ר�̼����ÿͻ���
					new ServerHelder(remoInput, remoteOut).start();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class ServerHelder extends Thread{//�����̼߳����˿ͻ�������
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
						buf = remoteInput.readUTF();//��ȡ
					else {
						file = false;
						continue;
					}

					System.out.println(buf);
					if(buf.contains("login")) {//�жϵ�½�ɹ���
						String account = buf.substring(buf.indexOf(':')+1,buf.indexOf('&'));
						String passport_client = buf.substring(buf.indexOf('&')+1);
						String passport_database = null;
						String name = null;
						int id;
						if(account.equals("")) {//δ��ֵ
							remoteOutput.writeUTF("login:failed");
							//��½ʧ�ܣ���hold����
							clients.removeElement(remoteOutput);
							continue;
						}
						login_table = new login_table();
						id = login_table.selectIDbyAccount_number(Long.valueOf(account));
						if(id<0) {
							remoteOutput.writeUTF("login:failed");
							//��½ʧ�ܣ���hold����
							clients.removeElement(remoteOutput);
							continue;
						}
						name = login_table.vt.get(id).account_name;
						passport_database = login_table.vt.get(id).passport;
						
						if(passport_client.equals(passport_database))
							remoteOutput.writeUTF("login:success&"+name+"&"+account);
						else {
							remoteOutput.writeUTF("login:failed");
							//��½ʧ�ܣ���hold����
							clients.removeElement(remoteOutput);
						}
					}else if(buf.contains("register")) {//ע���ʺ�
						String account_name = buf.substring(buf.indexOf(':')+1,buf.indexOf('&'));
						String passport = buf.substring(buf.indexOf('&')+1);
						login_table = new login_table();
						long account = login_table.add(passport, account_name);
						if(account>=0)
							remoteOutput.writeUTF("register:"+account+"&"+account_name);
						else
							remoteOutput.writeUTF("register:failed");
					}else if(buf.contains("chat:")){
						//����������Ϣ--string\image\file
						broadcast(buf);//���͸�����hold���ӵ��û�
					}else if(buf.contains("image:")||buf.contains("file:")) {
						file = true;
						//���յ���Ϣͷ
						broadcast(buf);//���͸�����hold���ӵ��û�
						//Ȼ������ļ�
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
				clients.removeElement(remoteOutput);//�ͻ��˹ر���
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void broadcast(String buf) {//���������ӹ��ķ���Ϣ
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
		private void broadcast(byte[] buf) {//���������ӹ��ķ���Ϣ
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