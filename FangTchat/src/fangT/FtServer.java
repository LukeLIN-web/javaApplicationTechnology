package fangT;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;//实现线程安全. 
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

public class FtServer implements FangTangConstants{
	private ConcurrentHashMap<Socket, String> users = new ConcurrentHashMap<Socket, String>();//save user name and socket
	String localName = null;
	String hostName;
	private int clientNo = 0;//number a client
	DataInputStream fromClient;
	DataOutputStream toClient ;
	boolean flag = false ; // in the method init flag will occur error 匿名内部类和局部内部类在初始化后，又对这个变量进行了赋值。赋值后会认为这个变量不是final了，所以报错
	public static void main(String[] args){
			try {
				new FtServer().Service();
			} catch (IOException e) {
				e.printStackTrace();// maybe you can show message JoptionPane
			}
	}

	private ServerSocket serverSocket;//定义服务器套接字
	private ExecutorService executorService = Executors.newCachedThreadPool();
	public FtServer() throws IOException{		
		serverSocket = new ServerSocket(port);
		System.out.println(new Date()+"服务器启动监听在"+port+"端口...");  //constructor init the serversocket
	}

	// can send and receive
	class Handler implements Runnable{
		private Socket socket;
		boolean isExist;
		public Handler(Socket socket) {// constructor
			this.socket = socket;
		}
		//判断用户是否已经下线
		private Boolean isLeaved(Socket temp){
			Boolean leave=true;
			for(Map.Entry<Socket,String> mapEntry : users.entrySet()) {
				if (mapEntry.getKey().equals(temp))
					leave = false;
				}
			return leave;
		}
		public void responseLogin(String usr,String pwd) throws IOException {
			toClient.writeUTF("服务器收到的用户名和密码为  =    "+usr+pwd);
		}
		public void responseRgstr(String usr,String pwd) throws IOException {
			toClient.writeUTF("服务器收到的用户名和密码为 =    "+usr+pwd);
		}
		
		public void run() {//本地服务器控制台显示客户端连接的用户信息
			System.out.println("New connection accept:" + socket.getInetAddress());
			try { 
				fromClient = new DataInputStream(socket.getInputStream());
				toClient = new DataOutputStream(socket.getOutputStream());
				toClient.writeInt(CONNECTSUCCESS);//给他一个信号,可以开始了。对应int logstatus = fromServer.readInt()
				System.out.println("发送CONNECTSUCCESS信号 可以开始");// 应该要先开启服务器再开启客户端否则会连接不上。因为客户端开启就直接建立连接了。
				while (true) {
					int signal = fromClient.readInt();// 信号是登录呢？ 还是信息呢？ 还是注册呢？还是退出呢？
					if(signal == LOGIN || signal == REGISTER) {
						System.out.println("	接收用户名和密码中.... ");
						String username = fromClient.readUTF();
						String password = fromClient.readUTF();
						System.out.println("收到的用户名和密码为 = "+username+password);
						toClient.writeInt( CONTINUESEND);
						if(signal == LOGIN)
							responseLogin(username,password);
						else {
							responseRgstr(username,password);
						}
					}
				}

////				while ((hostName = fromClient.readUTF()) != null){
////					users.forEach((k,v)->{
////					if (v.equals(hostName))
////						flag =true;//线程修改了全局变量
////					});
////					if (!flag){
////						localName=hostName;
////						users.put(socket,hostName);
////						flag=false;	break;
////					}
////					else{
////						flag=false;// use flag to avoid same name
////						dout.writeUTF("该用户名已存在，请修改！");//存在bug, 一旦用户名存在, 连接就会禁用, 但是还没有连接上, java.net.SocketException: Socket is closed
////					}// 失败了, 服务器要转发一个信息回去把连接按钮enable
////				}
////				sendToMembers("已经上线", localName, socket);//login in success
////				dout.writeUTF("输入命令功能    谢谢谢谢 ");
//				//下面这些太冗长了, 怎么处理?
//				String msg = null;
//			
//				while ((msg = fromClient.readUTF() ) != null) {
//					switch (msg.trim().toUpperCase()){
//						case "BYE": {// exit
//							dout.writeUTF("From 服务器：服务器已断开连接，结束服务！");
//								sendToMembers("已经下线", localName, socket);
//								System.out.println("客户端离开。");
//								break;
//							}
//						case "H":{// show help 
//							dout.writeUTF("input command function");
//							continue;
//						}
//						case "L":{ // show all the member
//							users.forEach( (k,v)->{
//								try {
//									dout.writeUTF("user: "+ v);
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} // lambda 
//							});// print all user from the hashmap
//							continue;
//						}
//						case "O":{ // send to one 
//							dout.writeUTF("请输入私信人的用户名：");
//							String name = fromClient.readUTF();
//							//查找map中匹配的socket，与之建立通信
//							users.forEach((k, v)->{
//								if (v.equals(name)) {
//									isExist=true;//全局变量与线程修改问题
//								}
//							});
//
//							Socket temp = null;//已修复用户不存在的处理逻辑
//							for(Map.Entry<Socket,String> mapEntry : users.entrySet()){
//								if(mapEntry.getValue().equals(name))
//									temp = mapEntry.getKey();
//							}
//							if (isExist){
//								isExist=false;//私信后有一方用户离开，另一方未知，仍然发信息而未收到回复，未处理这种情况
//								while ((msg = fromClient.readUTF() ) != null){
//									if (!msg.equals("E")&&!isLeaved(temp))
//										sendToOne(msg,localName,temp);//continue send
//									else if (isLeaved(temp)){
//										dout.writeUTF("对方已经离开，已断开连接！");
//										break;
//									}
//									else{
//										dout.writeUTF("您已退出私信模式！");
//										break;
//									}
//								}
//							}
//							else dout.writeUTF("用户不存在！");
//							break;
//						}
//						case "G":{ // group chat
//							dout.writeUTF("您已进入群聊。");
//							while ((msg = fromClient.readUTF() )!=null){
//								if (!msg.equals("E")&&users.size()!=1)
//									sendToMembers(msg,localName,socket);// continue send
//								else if (users.size()==1){
//									dout.writeUTF("当前群聊无其他用户在线，已自动退出！");
//									break;
//								}
//								else {
//									dout.writeUTF("您已退出群组聊天室！");
//									break;
//								}
//							}
//						}
//						default: dout.writeUTF("please input ");
//					}
//					dout.writeUTF("From 服务器：" + msg);
//					dout.writeUTF("来自服务器,重复消息："+msg);
//				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void Service() throws IOException{
		while(true) {
			Socket s = null ;
			s = serverSocket.accept();//等待直到有客户端连接到这个端口
			clientNo ++;
			InetAddress inetAddress = s.getInetAddress();//when a new client join the chat, print the IP address and the hostname. 
			System.out.println("client "+clientNo +"   host name is "+ inetAddress.getHostName());
			System.out.println("client "+clientNo +"   host address is "+ inetAddress.getHostAddress());
			Handler handler = new Handler(s);
			executorService.execute(handler);
		}
	}

	// send to all users save socket in hash map
	private void sendToMembers(String msg,String hostAddress,Socket mySocket) throws IOException{

		OutputStream outstream = mySocket.getOutputStream();//通过socket
		DataOutputStream dout = new DataOutputStream(outstream);
		Iterator iterator = users.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry entry=(Map.Entry) iterator.next();
			Socket tempSocket = (Socket) entry.getKey();
			//String name = (String) entry.getValue();
			if (!tempSocket.equals(mySocket)){
				//out = tempSocket.getOutputStream();
				dout.writeUTF(hostAddress+"："+msg);
			}
		}

	}
	// send to specific socket
	private void sendToOne(String msg,String hostAddress,Socket another) throws IOException{
		OutputStream out;
		Iterator iterator=users.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry entry=(Map.Entry) iterator.next();
			Socket tempSocket = (Socket) entry.getKey();
			//String name = (String) entry.getValue();
			if (tempSocket.equals(another)){
				out = tempSocket.getOutputStream();
				DataOutputStream dout = new DataOutputStream(out);
				dout.writeUTF(hostAddress+"私信了你："+msg);
			}
		}
	}

}
