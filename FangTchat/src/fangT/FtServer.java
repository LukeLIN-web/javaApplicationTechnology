package fangT;

//UDPServer.java
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class FtServer {

public static void main(String[] args){
try {
	new FtServer().Service();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}

private DatagramSocket socket = null;
private int port = 9020;
private DatagramPacket receivePacket;

public void UDPServer() throws IOException {
System.out.println("服务器启动监听在" + port + "端口...");
}

public void Service() throws IOException {
try {
socket = new DatagramSocket(port);
System.out.println("服务器创建成功，端口号：" + socket.getLocalPort());

while (true) {

//服务器接收数据
String msg=null;
receivePacket = udpReceive();
InetAddress ipR = receivePacket.getAddress();
int portR = receivePacket.getPort();
msg = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");

//System.out.println("收到："+receivePacket.getSocketAddress()+"内容："+msg);

if (msg.equalsIgnoreCase("bye")) {
udpSend("来自服务器消息：服务器断开连接，结束服务！",ipR,portR);
System.out.println(receivePacket.getSocketAddress()+"的客户端离开。");
continue;
}
System.out.println("建立连接："+receivePacket.getSocketAddress());

String now = new Date().toString();
String hello = "From 服务器：&" + now + "&" + msg;
udpSend(hello,ipR,portR);

}
} catch (IOException e) {
e.printStackTrace();
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

public void udpSend(String msg,InetAddress ipRemote,int portRemote) throws IOException {
DatagramPacket sendPacket;
byte[] dataSend = msg.getBytes();
sendPacket = new DatagramPacket(dataSend,dataSend.length,ipRemote,portRemote);
socket.send(sendPacket);
}

}
