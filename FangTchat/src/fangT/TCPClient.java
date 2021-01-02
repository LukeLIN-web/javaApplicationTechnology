package fangT;


import java.io.*;
import java.net.*;

public class TCPClient implements FangTangConstants{
		private Socket socket; //need .net package
//		private PrintWriter pw; // need .io package
//		private BufferedReader br; //
//		private ObjectOutputStream objStream;
		private DataInputStream fromServer;
		private DataOutputStream toServer;
		public TCPClient(String ip, String port) throws IOException{
			//主动向服务器发起连接，实现TCP三次握手, 不成功则抛出错误，由调用者处理错误
			socket = new Socket(ip,Integer.parseInt(port));
			//得到网络流输出字节流地址，并封装成网络输出字符流
			OutputStream socketOut = socket.getOutputStream();
			toServer = new DataOutputStream(socketOut);
			//pw = new PrintWriter(new OutputStreamWriter(socketOut,"utf-8"),true);//参数true表示自动flush数据
			InputStream socketIn=socket.getInputStream();//得到网络输入字节流地址，并封装成网络输入字符流
			//br = new BufferedReader(new InputStreamReader(socketIn,"utf-8"));
			fromServer = new DataInputStream(socketIn);
		}
	
		//定义一个数据的发送方法
		public void send(String msg) throws IOException{
			toServer.writeUTF(msg);//输出字符流，由socket调用系统底层函数，经网卡发送字节流
			toServer.flush();
		}
		//定义登录和注册请求的对象发送方法
		public void objSend(String name, String pwd,int flag) throws IOException {
			toServer.write(flag);//怎么约定好呢? 她怎么接收可以做到区分注册信息和普通字符串?
		}
		
		public String receive(){
			String msg = null;
			try {
				//从网络输入字符流中读取信息，每次只能接受一行信息
				//不够一行时（无行结束符），该语句阻塞, 直到条件满足，程序往下运行
				msg = fromServer.readUTF();
			}catch (IOException e){
				e.printStackTrace();
			}
			return msg;
		}
	
		public void close(){
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
