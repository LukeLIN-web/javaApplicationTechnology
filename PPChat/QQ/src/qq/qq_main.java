package qq;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class qq_main implements Observer {
	private login_frame frame_login;
	private register_frame frame_register;
	private chat_frame frame_chat;
	datacenter dc;
	boolean file = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					qq_main window = new qq_main();
					window.frame_login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public qq_main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DataInputStream remoteIn = null;
		dc = new datacenter();
		dc.addObserver(this);
		frame_login = new login_frame(dc);
		frame_register = new register_frame(dc);
		frame_chat = new chat_frame(dc);
		InetAddress servAddress;
		try {
			servAddress = InetAddress.getByName(null);
			Socket client = new Socket(servAddress.getHostName(), 3080);
			client.setKeepAlive(true);
			dc.remoteOut = new DataOutputStream(client.getOutputStream());
			remoteIn = new DataInputStream(client.getInputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new reciverSever(remoteIn).start();
	}
	
	public void update(Observable t, Object o) {
		if(dc.type == 0) {
			//先全不显示
			frame_login.setVisible(false);
			frame_register.setVisible(false);
			frame_chat.setVisible(false);
			//分情况将需要显示显示
			if(dc.visible == 0)//login
				frame_login.setVisible(true);
			else if(dc.visible==1)//register
				frame_register.setVisible(true);
			else if(dc.visible==2) {//chat
				frame_chat.setTitle("PP聊天室--欢迎"+dc.name);
				frame_chat.setVisible(true);
			}
		}
		if(dc.type == 2) {
			frame_chat.btnNewButton.setText("File");
		}
	}
	
	class reciverSever extends Thread{
		DataInputStream remoteIn;
		public reciverSever(DataInputStream i) {
			remoteIn = i;
		}
		
		public void run() {
			try {
				while (true) {
					String str = null;
					str = remoteIn.readUTF();//读取
					System.out.println(str);
					//分情况
					if(str.contains("login")) {
						//收到消息是否为login成功
						if(str.contains("success")) {
							dc.setlogin(true, 
									str.substring(str.indexOf('&')+1, str.lastIndexOf('&')),
									str.substring(str.lastIndexOf('&')+1));
						}
						else
							dc.setlogin(false, "", "-1");
					}else if(str.contains("register")) {
						//提取account
						long account = Long.valueOf(str.substring(str.indexOf(':')+1, str.indexOf('&')));
						String name = str.substring(str.indexOf('&')+1);
						dc.setacc(account, name);
					}else if(str.contains("chat:")){
						if(dc.login_ok==true) {//登陆成功才可以收消息
							dc.setchat(str);//截取主体
						}							
					}else if(str.contains("image:")||str.contains("file:")){
						file = true;
						if(dc.login_ok==true) {//登陆成功才可以收消息
							//然后接受文件
							String temp = str.substring(
									str.indexOf("<size>")+"<size>".length(), str.indexOf("</size>"));
							//然后接受文件
							int len = Integer.valueOf(temp);
							byte[] b = new byte[len*33];
							//byte[] bb = new byte[10000];
							remoteIn.read(b);
							//remoteIn.read(bb);
							//System.out.println(new String(b));
							//System.out.println("!!!"+new String(bb));
							//将文件存入数据中心
							dc.setchat(str, b);
						}							
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
	
