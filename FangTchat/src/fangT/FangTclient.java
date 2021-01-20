package fangT;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

class baseClient extends Application implements FangTangConstants{
	JFXPanel jfxPanel = new JFXPanel();//否则会有java.lang.RuntimeException: Internal graphics not initialized yet
	Image imagesend = new Image("envelope.png");
	protected Button btnExit = new Button("退出");  
	protected Button btnSend = new Button("发送",new ImageView(imagesend));
	protected Button btConn = new Button("登录");
	protected Button btRegst = new Button("注册");
	protected TextField tfSend = new TextField();//输入信息区域
	protected TextArea taDisplay = new TextArea();//显示区域
	protected TextField username = new TextField();//填写用户名
	protected PasswordField LOGINpassword = new PasswordField();//login in 
	protected TextField fangTangId = new TextField();//填写id
	protected PasswordField REGIpassword = new PasswordField();//填写密码
	public void start(Stage primaryStage) {
		adjustStyle(primaryStage);
		primaryStage.show();
	}
	// 把前端的工作分离出start();使用baseClient父类作为UI调整, 这样可以通过同样的UI写别的业务代码.
	public void adjustStyle(Stage primaryStage) {
		BorderPane mainPane=new BorderPane();//BorderPane，默认就分割好了上下左右中的五个部分
		
		HBox hBox1 = new HBox();
		hBox1.setSpacing(10);
		hBox1.setPadding(new Insets(10,20,10,20));//setPadding()：以外部控件的角度，规定其内部控件与其的距离
		hBox1.setAlignment(Pos.CENTER);
		hBox1.getChildren().addAll(new Label("账号:"),fangTangId,new Label("密码："),LOGINpassword,btConn);// 从左到右horizon
		
		
		HBox hBox2 = new HBox();
		hBox2.setSpacing(10);
		hBox2.setPadding(new Insets(10,20,10,20));//setPadding()：以外部控件的角度，规定其内部控件与其的距离
		hBox2.setAlignment(Pos.CENTER);
		hBox2.getChildren().addAll(new Label("昵称:"),username,new Label("密码："),REGIpassword,btRegst);// 从左到右horizon
		
		VBox vBox1 = new VBox();//vertical box 
		vBox1.setSpacing(10); //设置单行面板组件的间距为10像素
		vBox1.setPadding(new Insets(10,20,10,20));
		vBox1.getChildren().addAll(hBox1,hBox2);
		mainPane.setTop(vBox1);// 放上方
		
		VBox vBox = new VBox();//vertical box 
		vBox.setSpacing(10); //设置单行面板组件的间距为10像素
		vBox.setPadding(new Insets(10,20,10,20));
		vBox.getChildren().addAll(new javafx.scene.control.Label("信息显示区"),taDisplay,new Label("信息输入区"),tfSend);
		VBox.setVgrow(taDisplay, Priority.ALWAYS);//Priority，一个枚举类,用于确定给定节点的增长（或缩小）优先级。比如:一个HBox布局，里面有三个控件，当屏幕宽度是800时，刚好把屏幕占满，但是当屏幕扩大到1200时，这个Priority规定了这三个控件如何处理增加的400宽度。共有三个取值：ALWAYS：布局区域将始终尝试增长（或缩小），共享那些空间;
		mainPane.setCenter(vBox);
		
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(10,20,10,20));
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.getChildren().addAll(btnSend,btnExit);
		btnSend.setDisable(true);// at first disable the send button
		btnSend.setStyle("-fx-background-color:linear-gradient(#E4EAA2, #9CD672);");//-fx-border-color:black; -fx-padding:3px;-fx-background-color:POWDERBLUE;
		mainPane.setBottom(hBox);//下方 
		
		
		Scene scene =new Scene(mainPane,700,500);//scene is a tree-structure,其根节点一般是Pane面板（如StackPane、BorderPane
		primaryStage.setScene(scene);// Place the scene in the stage,Stage包含Scene，Scene包含一个或者多个node节点
		primaryStage.show();// Display the stage
		primaryStage.setTitle("FangTangChatRoom");// Set the stage title
		primaryStage.getIcons().add(new Image("LOGO2.png"));//Stage相当于swing的Jwindow,一个Stage必须至少有一个Scene.Scene相当于JFrame,包含一个或者多个node节点
	}
}

public class FangTclient extends baseClient{
	private Socket socket;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	public  boolean continueToSend = true;
	public  boolean waiting = true;
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss") ;
	public void displayIt(){
		launch();//用来启动整个Application
	}
	@Override
	public void start(Stage primaryStage) { //start()方法传入的Stage对象在JavaFX程序加载时被加载（JavaFX的Stage是顶层容器）
		super.start(primaryStage);
		btConn.setOnAction(new BtnConnHandler() );
		btnSend.setOnAction(new BtnSendHandler() );
		tfSend.setOnKeyPressed(new PressSendHandler() );	//对输入区域绑定键盘事件
		btRegst.setOnAction(new BtnRegstHandler());
		btnExit.setOnAction(event -> {
			try {
				exit();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		});	//窗体关闭响应的事件,点击右上角的×关闭,客户端也关闭
		primaryStage.setOnCloseRequest(event -> {
			try {
				exit();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		});
		connectToserver();// 打开客户端同时连接服务器.
	}
	private void connectToserver() {
		try {
			socket = new Socket("localhost",port);
			toServer = new DataOutputStream(socket.getOutputStream());
			fromServer = new DataInputStream(socket.getInputStream());
			Platform.runLater( ()->{
				taDisplay.appendText("	启动客户端成功! 打开socket成功!!  \n");
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int logstatus = fromServer.readInt();//服务器告诉他可以开始了
			if(logstatus == CONNECTSUCCESS) {
					Platform.runLater( ()->{
						taDisplay.appendText("	连接成功!!!  欢迎使用服务！\n请输入用户名：\n");});	//remind user to input user name
			} else if (logstatus == CONNECTFAIL) {
					Platform.runLater( ()->{
						taDisplay.appendText("	连接失败!!! \n");});					//TODO :重新尝试连接.
			}
			System.out.print("debug 语句连接成功 ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//接收服务器发来的消息
	private int receiveInfoFromServer() throws IOException{
		int currentStatus = fromServer.readInt();
		if (currentStatus == STOPSEND) {//还是继续尝试登录
			Platform.runLater( ()->{
				taDisplay.appendText("\n	登录失败!!! \n");
			});
			return STOPSEND;
		}
		else if(currentStatus == CONTINUESEND){ // 约定好, 先发CONTINUESEND,然后再发字符串.
			//把接收和事件分离开, 而不是在这里readutf
			String tmp = fromServer.readUTF();// 阅读服务器的返回消息
			resetByServerInfo(tmp);// 根据返回信息来设置客户端
			Platform.runLater( ()->{
				taDisplay.appendText(dtf.format(now)+"\n");// format print
				taDisplay.appendText("收到服务器的信息:  "+tmp+"\n");
			});
			return CONTINUESEND;
		}
		else return STOPSEND;
	}
	
	//更新客户端的按钮等控件, 修改一些变量. 
	private void resetByServerInfo(String serverInfo){
		char[] arr = serverInfo.toCharArray();    // char数组
		switch( arr[0] ) {
			case 'l': //登录成功
				btnSend.setDisable(false);//登录成功,可以发送信息
				btConn.setDisable(true);//一个客户端登录一个账号,不再登录,连接服务器之后未结束服务前禁用再次连接
				btRegst.setDisable(true);//一个客户端登录一个账号,也不再注册.
				break;
			case 'f':// 登录失败	
				btnSend.setDisable(true);//登录失败,不可以发送信息
				break;
			case 'b':
				btnSend.setDisable(true);//发送bye后禁用发送按钮
				tfSend.setDisable(true);//禁用Enter发送信息输入区域
				btConn.setDisable(false);//结束服务后再次启用连接按钮
				break;
			default :
				taDisplay.appendText("\n客户端更新完毕! \n");
		}	
	}
	
	// 按下注册按钮, 发送用户密码到服务器, 服务器去数据库找是否对应,如果有,那就显示已存在用户名,  如果不对应,那就add 这个. 显示注册成功. 
	class BtnRegstHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			waiting = false;
			String usr = username.getText().trim();// 这个必须放在handle或者event中, 否则只能获得默认值.
			String pwd = REGIpassword.getText().trim();// remove the space
			try {
				toServer.writeInt(REGISTER);//客户端发注册信号
				toServer.writeUTF(usr);
				toServer.writeUTF(pwd);
				Platform.runLater(()->{// 稍后更新GUI
					now = LocalDateTime.now();
					taDisplay.appendText(dtf.format(now)+"\n");// format print
					taDisplay.appendText(usr+"希望注册的用户名和密码已经发送！\n");
				});	
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				String msg = fromServer.readUTF();
				Platform.runLater(()->{// 稍后更新GUI
					now = LocalDateTime.now();
					taDisplay.appendText(dtf.format(now)+"\n");// format print
					taDisplay.appendText(msg);
				});	
			} catch (IOException e) {
				e.printStackTrace();
			}
			username.clear();
			REGIpassword.clear();
		}
	}
	
	//连接按钮触发的事件原先用lambda函数表示 , now 改写为单独的类
	class BtnConnHandler implements EventHandler<ActionEvent>{ 
		@Override
		public void handle(ActionEvent event) {
			waiting = false;// 打断waiting 的状态. 结束waitForSendAction()
			String ftid = fangTangId.getText().trim();// 这个必须放在handle或者event中, 否则只能获得默认值.
			String pwd = LOGINpassword.getText().trim();// remove the 
			try {
				toServer.writeInt(LOGIN);
				toServer.writeInt(Integer.parseInt(ftid));
				toServer.writeUTF(pwd);//发送信息放在按钮事件中
					Platform.runLater(()->{// 稍后更新GUI
						now = LocalDateTime.now();
						taDisplay.appendText(dtf.format(now)+"\n");// format print
						taDisplay.appendText(ftid+pwd+"用户名和密码已经发送！\n");
					});
				int i = receiveInfoFromServer();
				if (i == CONTINUESEND) {//登录是严格的协议, 之后接收是多线程的.
					try {// after login then communicate with server.
						new Thread( () ->{ // 进入发送信息循环. 这里可以把用户信息放入map
							try {// 大多数时候停留在这里,等用户按按钮来发送信息.
								while( !socket.isClosed()  && fromServer != null) {
									String msg = fromServer.readUTF();
									Platform.runLater( ()->{
										now = LocalDateTime.now();
										taDisplay.appendText("\n"+dtf.format(now)+"\n");// format print
										taDisplay.appendText("\n收到服务器的消息: "+msg);});//第四个执行
								}
							} catch (IOException e) {
								System.out.println("这是合法的，循环结束");
							}
							Platform.runLater( ()->{
								taDisplay.appendText("\n收到服务器的循环已经结束 ");});
						}).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}catch (Exception e){
				taDisplay.appendText("服务器连接失败！"+e.getMessage()+"\n");
			}
			fangTangId.clear();		LOGINpassword.clear();
		}
	}
	// 按发送按钮
	class BtnSendHandler implements  EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			String msg = tfSend.getText(); 
			try {
				toServer.writeUTF(msg);//向服务器发送信息
			} catch (IOException e) {
				e.printStackTrace();
			}
			now = LocalDateTime.now();
			taDisplay.appendText(dtf.format(now)+"本地客户端发送："+msg+"\n");//第一个执行
			if (msg.equalsIgnoreCase("bye"))
				try {
					exit();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("这是合法的，客户端已经关闭");
				}//让他移除掉用户
			tfSend.clear();
		}
	}
	// 按下回车键
	class PressSendHandler implements EventHandler<KeyEvent>{
		@Override
		public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER) {
				String msg = tfSend.getText();
				waiting = false; // 打断waiting 的状态. 结束waitForSendAction()
				try {
					toServer.writeUTF(msg);//向服务器发送信息
				} catch (IOException e) {
					e.printStackTrace();
				}
				now = LocalDateTime.now();
				taDisplay.appendText(dtf.format(now)+"本地客户端发送："+msg+"\n");
				tfSend.clear();
				if (msg.equalsIgnoreCase("bye"))
					try {
						exit();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						System.out.println("这是合法的，客户端已经关闭");
					}//让他移除掉用户
			}
		}
	}

	private void exit() throws InterruptedException, IOException {
			Thread.sleep(1000);//多线程等待，关闭窗口时还有线程等待IO，设置1s间隔保证所有线程已关闭
			close();
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