package fangT;

//UDPServer.java
import java.io.*;
import java.net.*;
import java.util.*;

public class FtServer {

	public static void main(String[] args){
		
			new FtServer().Service();
	}

private DatagramSocket socket = null;
private int port = 9020;

private ServerSocket serverSocket;//定义服务器套接字

public void TCPServer() throws IOException{
serverSocket =new ServerSocket(port);
System.out.println("服务器启动监听在"+port+"端口...");

}
//单客户版本，每次只能与一个用户建立通信连接
public void Service(){
while (true){
Socket socket=null;
try {
//此处程序阻塞，监听并等待用户发起连接，有连接请求就生成一个套接字
socket=serverSocket.accept();

//本地服务器控制台显示客户连接的用户信息
System.out.println("New connection accepted:"+socket.getInetAddress());
BufferedReader br=getReader(socket);//字符串输入流
PrintWriter pw=getWriter(socket);//字符串输出流
pw.println("来自服务器消息：欢迎使用本服务！");

String msg=null;
//此处程序阻塞，每次从输入流中读入一行字符串
while ((msg=br.readLine())!=null){
//如果用户发送信息为”bye“，就结束通信
if(msg.equals("bye")){
pw.println("来自服务器消息：服务器断开连接，结束服务！");
System.out.println("客户端离开。");
break;
}
pw.println("来自服务器消息："+msg);
}
}catch (IOException e){
e.printStackTrace();
}finally {
try {
if (socket!=null)
socket.close();//关闭socket连接以及相关的输入输出流
}catch (IOException e){
e.printStackTrace();
}
}
}
}
// can send and receive


public DatagramPacket udpReceive() throws IOException {
DatagramPacket receive;
byte[] dataR = new byte[1024];
receive = new DatagramPacket(dataR, dataR.length);
socket.receive(receive);
return receive;
}
private PrintWriter getWriter(Socket socket) throws IOException{
	//获得输出流缓冲区的地址
	OutputStream socketOut=socket.getOutputStream();
	//网络流写出需要使用flush，这里在printWriter构造方法直接设置为自动flush
	return new PrintWriter(new OutputStreamWriter(socketOut,"utf-8"),true);
	}

	private BufferedReader getReader(Socket socket) throws IOException{
	//获得输入流缓冲区的地址
	InputStream socketIn=socket.getInputStream();
	return new BufferedReader(new InputStreamReader(socketIn,"utf-8"));
	}
public void udpSend(String msg,InetAddress ipRemote,int portRemote) throws IOException {
DatagramPacket sendPacket;
byte[] dataSend = msg.getBytes();
sendPacket = new DatagramPacket(dataSend,dataSend.length,ipRemote,portRemote);
socket.send(sendPacket);
}

}
