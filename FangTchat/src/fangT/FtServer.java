package fangT;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;//实现线程安全. 
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
public class FtServer implements FangTangConstants{
	private static FtServer ftServer  = new FtServer();//该类被加载时运行一次
	private ConcurrentHashMap<Socket,Integer> users = new ConcurrentHashMap<Socket,Integer>();//save user name and socket
	private int clientNo = 0;//number a client
	FtUser ftuser = new FtUser();//数据库只有一个,可以用全局变量
	//匿名内部类和局部内部类在初始化后，又对这个变量进行了赋值。赋值后会认为这个变量不是final了，所以报错
	public static void main(String[] args){
			try {
				ftServer.Service();
			} catch (IOException e) {
				e.printStackTrace();// maybe you can show message JoptionPane
			}
	}
	private ServerSocket serverSocket;//定义服务器套接字
	private ExecutorService executorService = Executors.newCachedThreadPool();// 用cached 线程池,强制线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 构造函数的方式.CachedThreadPool 和 ScheduledThreadPool ： 允许创建的线程数量为 Integer.MAX_VALUE ，可能会创建大量线程，从而导致 OOM。
	public FtServer(){		
	}
	public static FtServer getInstance() {
		return ftServer;//在类外获得唯一实例
	}
	// can send and receive
	class Handler implements Runnable{
		private Socket socket;
		String localName = null;
		boolean isExist = false;
		public Handler(Socket socket) {// constructor
			this.socket = socket;
			System.out.print(socket);
		}
		//判断用户是否已经下线
		private Boolean isLeaved(Integer temp){
			Boolean leave=true;
			for(Map.Entry<Socket,Integer> mapEntry : users.entrySet()) {
				if (mapEntry.getValue().equals(temp))
					leave = false;
				}
			return leave;
		}
		public boolean responseLogin(int ftid,String pwd) throws IOException {
			boolean flag = false;
			if (users.containsValue(ftid)) {
				return false;//如果已经登录, 那就登录失败, 退出时可以移除.
			}
		    for(Iterator<FtUser> ite = ftuser.vt.iterator(); ite.hasNext();) {
		    	FtUser tmpft = ite.next();// we cannot use next() twice .
				int tmpid = tmpft.getFtid();
		        if (tmpid == ftid ) {
					try {
						if (tmpft.getPassword(tmpid).equals(pwd)) {
							flag = true;
							System.out.println("	password =  "+ ftuser.getPassword(tmpid));
							localName = tmpft.getName(ftid);//当前登录的人的名字, 传递给sendmembers方法.
							users.put(socket,ftid);// login success
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		    }
			return flag;
		}
		public boolean responseRgstr(String usr,String pwd) throws IOException {
			boolean flag = false;
			for(Iterator<FtUser> ite = ftuser.vt.iterator(); ite.hasNext();) {
				 String tmpname = ite.next().getUserName();
		        if (tmpname.equals(usr) ) {
					flag = true;//如果有一样的,  那就报告重复, 通知
				}
		    }
			if (!flag) {
				ftuser.add(usr, pwd);		//如果没有一样的,那就注册一个
			}
			return flag;
		}

		public int responseSentence(String msg) throws IOException {
			switch (msg.trim().toUpperCase()){
				case "BYE": {// exit			
						sendToMembers("已经下线", localName, socket);
						System.out.println(" 客户端 "+localName+" 离开 ");
						users.remove(socket);
						return 1;
					}
				case "H":// show help 
					return 2;
				case "L":// show help 
					return 3;
				case "O": // send to one 
					return 4; 
				case "G": //group 
					return 5;
				default : return 99;
			}
		}
		
		public void run() {//本地服务器控制台显示客户端连接的用户信息
			System.out.println("New connection accept:" + socket.getInetAddress());
			try { 
				DataInputStream fromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
				int ftid = 0;
				boolean flag = false;	
				String username = "";	String password;
				toClient.writeInt(CONNECTSUCCESS);//给他一个信号,可以开始了。对应int logstatus = fromServer.readInt()
				System.out.println("发送CONNECTSUCCESS信号 可以开始");// 应该要先开启服务器再开启客户端否则会连接不上。因为客户端开启就直接建立连接了。
				while (true) {
					int signal = fromClient.readInt();// 信号是登录呢？ 还是信息呢？ 还是注册呢？还是退出呢？
					if(signal == LOGIN) {
						System.out.println("\n服务器接收用户名和密码中.... ");
						toClient.writeInt(CONTINUESEND);
						ftid = fromClient.readInt();
						password = fromClient.readUTF();
						flag = responseLogin(ftid,password);
						if(flag == false) {				//如果没有一样的,那就返回错误信息
							toClient.writeUTF("fail login! 服务器收到的id和密码为  =    "+ftid+password+"\n账号或密码错误! 请重新输入账号或密码,然后再次点击登录按钮!\n  ");	
						}// 失败了, 服务器要转发一个信息回去把连接按钮enable
						else {
							try {
								toClient.writeUTF("login in success!  username is "+ftuser.getName(ftid)+"  账号为 "+ftid);// 登录状态为1; 
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
					}
					
					else if(signal == REGISTER) {
						System.out.println("\n服务器接收用户名和密码中.... ");
						username = fromClient.readUTF();
						password = fromClient.readUTF();
						System.out.println("收到的用户名和密码为 = "+username+password);
						if(responseRgstr(username,password) == false) {
							int id = ftuser.getNewestId();
							toClient.writeUTF("register success,没有用户名, 注册了一个账户, 用户名为 =    "+username+"账号为 "+id + " 请牢记! 服务器收到的用户名为 =    "+username);
							System.out.println("register success,没有用户名, 注册了一个账户, 用户名为 =    "+username+"账号为 "+id);
						}
						else {
							toClient.writeUTF("error:用户名"+username+"已经存在, 请重新输入用户名和密码,再次点击注册按钮!  ");
						}
					}
					else {
						toClient.writeInt(STOPSEND);;//如果是其他的话返回错误信息
					}
					if (flag) {
						sendToMembers("已经上线",  localName,socket);//login in success 这个好难实现, 
						toClient.writeUTF("输入命令功能    (1)L(list):查看当前上线用户;(2)G(group):"
								+ "进入群聊;(3)O(one-one):私信;(4)E(exit):退出当前聊天状态;(5)bye:离线;(6)H(help):帮助 ");
						break;
					}
				}
				String message = null;
				while( !socket.isClosed()  && fromClient != null) {
					message  = fromClient.readUTF();
					System.out.println(socket);
					System.out.println("服务器收到信息 = "+message+" 来自: "+users.get(socket));//第二个执行
					toClient.writeUTF("\n收到的信息 = "+message+"来自"+localName+"\n");//第三个执行
					int tag = responseSentence(message);
					switch (tag) {
						case 1: {
							toClient.writeUTF("From 服务器：服务器已断开连接，结束服务！");//然后在客户端可以开启重新登录, 可能以后再做.功能是做不完的.2021年1月11日.
							fromClient.close();
							toClient.close();
							break;
						}
						case 2:{
							toClient.writeUTF("输入命令功能\n"
									+ "(1)L(list):查看当前上线用户;(2)G(group):进入群聊;(3)O(one-one):私信;(4)E(exit):退出当前聊天状态;(5)bye:离线;(6)H(help):帮助");
							break;
						}
						case 3:{
								users.forEach( (k,v)->{
								try {
//									int count = 0; count++;// 里面不能修改变量
									toClient.writeUTF("在线用户 id: "+ v);
								} catch (IOException e) {
									e.printStackTrace();
								} // lambda 
							});// print all user from the hashmap
							break;
						}
						case 4: {
							toClient.writeUTF("请输入私信人的id：");
							int fid = Integer.valueOf(fromClient.readUTF());	//查找map中匹配的socket，与之建立通信
							Socket temp = null;//已修复用户不存在的处理逻辑
							String msg;
							for(Map.Entry<Socket,Integer> mapEntry : users.entrySet()){
								if(mapEntry.getValue().equals(fid)) {
									isExist=true;
									System.out.println(isExist);
									temp = mapEntry.getKey();// 找到对应的socket
								}
							}	
							System.out.println(temp);
							if (isExist){
								toClient.writeUTF("可以开始给对方发送私信! ");
								isExist=false;//私信后有一方用户离开，另一方未知，仍然发信息而未收到回复，未处理这种情况
								while ((msg = fromClient.readUTF() ) != null){
									if (!msg.equals("E")&&!isLeaved(fid)) {
										sendToOne(msg,localName,temp);//continue send
										System.out.println("已经发送msg！");
									}
									else if (isLeaved(fid)){
										toClient.writeUTF("对方已经离开，已断开连接！");
										System.out.println(fid+"已经离开，已断开连接！");
										break;
									}
									else{
										toClient.writeUTF("您已退出私信模式！");
										System.out.println("已退出私信模式！");
										break;
									}
								}
							}
							else toClient.writeUTF("用户不存在！");
							break;
						}
						case 5:{ // group chat
							toClient.writeUTF("您已进入群聊。");
							String msg;
							while ((msg = fromClient.readUTF() )!=null){
								if (!msg.equals("E")&&users.size()!=1)
									sendToMembers(msg,localName,socket);// continue send
								else if (users.size()==1){
									toClient.writeUTF("当前群聊无其他用户在线，已自动退出！");
									break;
								}
								else {
									toClient.writeUTF("您已退出群组聊天室！");
									break;
								}
							}
						}
						default:	toClient.writeUTF("输入命令功能    (1)L(list):查看当前上线用户;(2)G(group):进入群聊;(3)O(one-one):私信;(4)E(exit):退出当前聊天状态;(5)bye:离线;(6)H(help):帮助");	
					}  
				}
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
	//服务器停留在这循环里等待连接. 有一个接入, 就开一个handler, 然后提交到线程池.
	public void Service() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println(new Date()+"服务器启动监听在"+port+"端口...");  //constructor init the serversocket
		while(true) {
			Socket s = null ;
			s = serverSocket.accept();//等待直到有客户端连接到这个端口,1个监听socket,每次accept成功后返回一个数据socket, 再继续用监听socket监听
			clientNo ++;
			InetAddress inetAddress = s.getInetAddress();//when a new client join the chat, print the IP address and the hostname. 
			System.out.println("client "+clientNo +"   host name is "+ inetAddress.getHostName());
			System.out.println("client "+clientNo +"   host address is "+ inetAddress.getHostAddress());
			Handler handler = new Handler(s);
			executorService.execute(handler);//提交一个任务到线程池中去
		}
	}

	// send to all users save socket in hash map,怎么实现发给别人呢?通过socket新建一个stream
	private void sendToMembers(String msg,String hostName,Socket mySocket) throws IOException{ 
		DataOutputStream dout; 
		Iterator<Entry<Socket, Integer>> iterator = users.entrySet().iterator();
		System.out.println("开始通知所有人他已经登录!m my socket = ");
		System.out.println(mySocket);//传入的mySocket好像没有错确实是第二个客户端的socket
		while (iterator.hasNext()){
			Map.Entry<Socket,Integer>  entry = (Map.Entry<Socket,Integer>) iterator.next();
			Socket tempSocket = (Socket) entry.getKey();
			System.out.println(tempSocket);
			if (!tempSocket.equals(mySocket)){
				dout = new DataOutputStream(tempSocket.getOutputStream());//根据保存的来新建, 应该是socket是一样的,只是多建了几个流
				dout.writeUTF(hostName+"："+msg);
				System.out.println("dout.writeUTF hostname = "+hostName+"msg = "+msg);
			}
		}
	}
	
	// send to specific socket
	private void sendToOne(String msg,String hostAddress,Socket another) throws IOException{
		OutputStream out;
		Iterator<Entry<Socket, Integer>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry entry=(Map.Entry) iterator.next();
			Socket tempSocket = (Socket) entry.getKey();
			if (tempSocket.equals(another)){
				out = tempSocket.getOutputStream();
				DataOutputStream dout = new DataOutputStream(out);
				dout.writeUTF(hostAddress+"私信了你："+msg);
			}
		}
	}
}
