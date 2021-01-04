package fangT;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class FangTclient extends Application implements FangTangConstants{
	Image imagesend = new Image("envelope.png");
	private Button btnExit=new Button("退出");  
	private Button btnSend = new Button("发送",new ImageView(imagesend));
	private TextField tfSend=new TextField();//输入信息区域
	private TextArea taDisplay = new TextArea();//显示区域
	private TextField username = new TextField();//填写用户名
	private TextField password = new TextField();//填写密码
	private Button btConn=new Button("连接");
	private Button btLog=new Button("登录");
	private Button btRegst=new Button("注册");
	//private Thread readThread;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	public  boolean continueToSend = true;
	public  boolean waiting = true;
	private String msg = "try a massge";
	private Socket socket;
	public static void main(String[] args) {
		launch(args);//用来启动整个Application的
	}
	
	@Override
	public void start(Stage primaryStage) { //start()方法传入的Stage对象在JavaFX程序加载时被加载（JavaFX的Stage是顶层容器）
		adjustStyle(primaryStage);
	// btConn.defaultButtonProperty();
		btConn.setOnAction(new BtnConnHandler() );
		btnSend.setOnAction(new BtnSendHandler() );
	//	tfSend.setOnKeyPressed(new PressSendHandler() );	//对输入区域绑定键盘事件
	//	btRegst.setOnAction(new BtnRegstHandler());
		btnExit.setOnAction(event -> {
			try {
				exit();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		});
	//窗体关闭响应的事件,点击右上角的×关闭,客户端也关闭
		primaryStage.setOnCloseRequest(event -> {
			try {
				exit();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		});
		connectToserver();
	}
	private void connectToserver() {
		try {
			socket = new Socket("localhost",port);
			toServer = new DataOutputStream(socket.getOutputStream());
			fromServer = new DataInputStream(socket.getInputStream());
			System.out.println("连接成功！！ ");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		new Thread( () ->{
			try {
				int logstatus = fromServer.readInt();//服务器告诉他可以开始了
				System.out.println(logstatus+"接收到 登录状态");
				if (logstatus == CONNECTSUCCESS) {
					Platform.runLater( ()->{
						taDisplay.appendText("	连接成功!!! \n");
					});
					btnSend.setDisable(false);
					// todo: 可以开始. 做一些设置。
				} else if (logstatus == CONNECTFAIL) {
					Platform.runLater( ()->{
						taDisplay.appendText("	连接失败!!! \n");
					});
					//todo :重新尝试连接.
				}
				while(continueToSend) {
					waitForSendAction();
					//sendMessage();//send 信息放在按钮事件中
					receiveInfoFromServer();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		).start();
	}
	
	private void waitForSendAction() throws InterruptedException {
		while (waiting) {
			Thread.sleep(100);
		}
		waiting = true;
	}
	
	private void sendMessage() throws IOException{
		toServer.writeBytes(msg);// change massage in different place
	}
	private void receiveInfoFromServer() throws IOException{
		int currentStatus = fromServer.readInt();
		if (currentStatus == STOPSEND) {
			continueToSend = false;
			Platform.runLater( ()->{
				taDisplay.appendText("	停止发送!!! \n");
			});
		}
		else if(currentStatus == CONTINUESEND){ // 约定好, 先发CONTINUESEND,然后再发字符串.
			//reveiveMsg();//把接收和事件分离开, 而不是在这里readutf
			Platform.runLater( ()->{
				taDisplay.appendText("可以继续发送");
			});
		}
	}
	
//	private void reveiveMsg() throws IOException{
//		String tmp = fromServer.readUTF();
//		Platform.runLater( ()->{
//			taDisplay.appendText(tmp);
//		});
//	}
	
	// 把前端的工作分离出start();
	public void adjustStyle(Stage primaryStage) {
		BorderPane mainPane=new BorderPane();//BorderPane，默认就分割好了上下左右中的五个部分
		//connect with server field
		HBox hBox1=new HBox();
		hBox1.setSpacing(10);
		hBox1.setPadding(new Insets(10,20,10,20));//setPadding()：以外部控件的角度，规定其内部控件与其的距离
		hBox1.setAlignment(Pos.CENTER);
		hBox1.getChildren().addAll(new Label("用户名："),username,new Label("密码："),password,btConn,btLog,btRegst);// 从左到右horizon
		mainPane.setTop(hBox1);// 放上方
		
		VBox vBox=new VBox();//vertical box 
		vBox.setSpacing(10); //设置单行面板组件的间距为10像素
		vBox.setPadding(new Insets(10,20,10,20));
		vBox.getChildren().addAll(new javafx.scene.control.Label("信息显示区"),taDisplay,new Label("信息输入区"),tfSend);
		VBox.setVgrow(taDisplay, Priority.ALWAYS);
		mainPane.setCenter(vBox);
		
		HBox hBox=new HBox();
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(10,20,10,20));
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.getChildren().addAll(btnSend,btnExit);
		btnSend.setStyle("-fx-background-color:linear-gradient(#E4EAA2, #9CD672);");//-fx-border-color:black; -fx-padding:3px;-fx-background-color:POWDERBLUE;
		mainPane.setBottom(hBox);//下方 
		
		Scene scene =new Scene(mainPane,700,500);//scene is a tree-structure,其根节点一般是Pane面板（如StackPane、BorderPane
		primaryStage.setScene(scene);// Place the scene in the stage,Stage包含Scene，Scene包含一个或者多个node节点
		primaryStage.show();// Display the stage
		primaryStage.setTitle("FangTangChatRoom");// Set the stage title
		primaryStage.getIcons().add(new Image("LOGO2.png"));//Stage相当于swing的Jwindow,一个Stage必须至少有一个Scene.Scene相当于JFrame,包含一个或者多个node节点
		
	}
	// 按下注册按钮, 户端发送用户密码到服务器, 服务器去数据库找是否对应,如果有,那就显示已存在用户名,  如果不对应,那就add 这个. 显示注册成功. 
//	class BtnRegstHandler implements EventHandler<ActionEvent>{
//		String usr = username.getText().trim();
//		String pwd= password.getText().trim();// remove the space
//		@Override
//		public void handle(ActionEvent event) {
//			try {
//			//	tcpClient.objSend(usr,pwd,2);//注册, id =2 , name 
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			taDisplay.appendText("注册中\n");
//			username.clear();
//			password.clear();
//		}
//	}
	
	class BtnConnHandler implements EventHandler<ActionEvent>{ //连接按钮触发的事件原先用lambda函数表示 , now 改写为单独的类
		String usr = username.getText().trim();
		String pwd= password.getText().trim();// remove the space
		@Override
		public void handle(ActionEvent event) {
			waiting = false;
			try {
				toServer.writeInt(LOGIN);
				toServer.writeUTF(usr);
				toServer.writeUTF(pwd);
			//用于接收服务器信息的单独线程
//			readThread = new Thread(()->{
//				String receiveMsg = null;//从服务器接收一串字符
//				while ((receiveMsg = tcpClient.receive()) != null){
//					//lambda表达式不能直接访问外部非final类型局部变量，需要定义一个临时变量,若将receiveMsg定义为类成员变量，则无需临时变量
//					String msgTemp = receiveMsg;
					Platform.runLater(()->{// 稍后更新GUI
						LocalDateTime now = LocalDateTime.now();
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss") ;
						taDisplay.appendText(dtf.format(now)+"\n");// format print
						taDisplay.setStyle("-fx-text-fill:black");
						taDisplay.appendText("\n");
					});
//				}
				Platform.runLater(()->{
					taDisplay.appendText("用户名和密码以发送！\n");
				});
//			});
//				readThread.start();			
//				btConn.setDisable(true);	//连接服务器之后未结束服务前禁用再次连接
//				tfSend.setDisable(false);	//重新连接服务器时启用输入发送功能
//				btnSend.setDisable(false);
			}catch (Exception e){
				taDisplay.appendText("服务器连接失败！"+e.getMessage()+"\n");
			}
		}
	}
	
	class BtnSendHandler implements  EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			String msg = tfSend.getText();
			//修改一些变量. 
			waiting = false; // 打断waiting 的状态. 结束waitForSendAction()
			try {
				toServer.writeBytes(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//向服务器发送一串字符 用户名和密码
			taDisplay.appendText("客户端发送："+msg+"\n");
			if (msg.equalsIgnoreCase("bye")){ //业务逻辑写在里面, 比较冗长.
				btnSend.setDisable(true);//发送bye后禁用发送按钮
				tfSend.setDisable(true);//禁用Enter发送信息输入区域
				//结束服务后再次启用连接按钮
				btConn.setDisable(false);
			}
			tfSend.clear();
		}
	}
//	class PressSendHandler implements EventHandler<KeyEvent>{
//		@Override
//		public void handle(KeyEvent event) {
//			if(event.getCode()==KeyCode.ENTER){ //或许可以把他改成
//				String msg = tfSend.getText();
//				try {
//					toServer.writeInt( );
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}//向服务器发送一串字符
//				taDisplay.appendText("客户端发送："+msg+"\n");
//				
//				if (msg.equalsIgnoreCase("bye")){ //业务逻辑应该分离出来, 不应该在键盘事件中. 服务器发送一个禁用给你, 然后你的禁用.
//					tfSend.setDisable(true);//禁用Enter发送信息输入区域, 
//					btnSend.setDisable(true);//发送bye后禁用发送按钮
//					//结束服务后再次启用连接按钮
//					btConn.setDisable(false);// 两个bye的代码, 这好愚蠢, 我可不可以把msg抽象出来, 只要发送了bye, 那就禁用.
//				}
//				tfSend.clear();
//			}
//		}
//	}
	private void exit() throws InterruptedException, IOException {
//		if (tcpClient!=null){
//			tcpClient.send("bye");
			Thread.sleep(1000);//多线程等待，关闭窗口时还有线程等待IO，设置1s间隔保证所有线程已关闭
			close();
//		}
		System.exit(0);
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