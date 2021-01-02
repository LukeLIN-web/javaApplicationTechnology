package fangT;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FangTclient extends Application {
	
	private Button btnExit=new Button("退出");
	private Button btnSend = new Button("发送");
	private TextField tfSend=new TextField();//输入信息区域
	private TextArea taDisplay = new TextArea();//显示区域
	private TextField username = new TextField();//填写用户名
	private TextField password = new TextField();//填写密码
	private Button btConn=new Button("连接");
	private Button btLog=new Button("登录");
	private Button btRegst=new Button("注册");
	private TCPClient tcpClient;
	private Thread readThread;
		
	public static void main(String[] args) {
		launch(args);//用来启动整个Application的
	}
	
	@Override
	public void start(Stage primaryStage) { //start()方法传入的Stage对象在JavaFX程序加载时被加载（JavaFX的Stage是顶层容器）
		BorderPane mainPane=new BorderPane();//BorderPane，默认就分割好了上下左右中的五个部分
		//connect with server field
		HBox hBox1=new HBox();
		hBox1.setSpacing(10);
		hBox1.setPadding(new Insets(10,20,10,20));
		hBox1.setAlignment(Pos.CENTER);
		hBox1.getChildren().addAll(new Label("用户名："),username,new Label("密码："),password,btConn,btLog,btRegst);// 从左到右horizon
		mainPane.setTop(hBox1);// 放上方
		
		VBox vBox=new VBox();//vertical box 
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10,20,10,20));
		vBox.getChildren().addAll(new javafx.scene.control.Label("信息显示区"),taDisplay,new Label("信息输入区"),tfSend);
		VBox.setVgrow(taDisplay, Priority.ALWAYS);
		mainPane.setCenter(vBox);
		
		HBox hBox=new HBox();
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(10,20,10,20));
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.getChildren().addAll(btnSend,btnExit);
		mainPane.setBottom(hBox);//下方 
		
		Scene scene =new Scene(mainPane,700,500);
		primaryStage.setScene(scene);// Place the scene in the stage
		primaryStage.show();// Display the stage
		primaryStage.setTitle("FangTangChatRoom");// Set the stage title
		primaryStage.getIcons().add(new Image("LOGO2.png"));
		
		//连接按钮触发的事件用lambda函数表示 , 
		btConn.setOnAction(event -> {
		String usr = username.getText().trim();
		String pwd= password.getText().trim();// remove the space
		
			try {
			//tcpClient是本程序定义的一个TCPClient类型的成员变量
			tcpClient = new TCPClient("localhost", "9020");
			//用于接收服务器信息的单独线程
			readThread = new Thread(()->{
				String receiveMsg=null;//从服务器接收一串字符
				while ((receiveMsg=tcpClient.receive()) != null){
					//lambda表达式不能直接访问外部非final类型局部变量，需要定义一个临时变量,若将receiveMsg定义为类成员变量，则无需临时变量
					String msgTemp = receiveMsg;
					Platform.runLater(()->{
						LocalDateTime now = LocalDateTime.now();
						DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss") ;
						taDisplay.appendText(dtf.format(now)+"\n");// format print
						taDisplay.setStyle("-fx-text-fill:black");
						taDisplay.appendText(msgTemp+"\n");
					});
				}
				Platform.runLater(()->{
					taDisplay.appendText("对话已关闭！\n");
				});
			});
				readThread.start();			
				btConn.setDisable(true);	//连接服务器之后未结束服务前禁用再次连接
				tfSend.setDisable(false);	//重新连接服务器时启用输入发送功能
				btnSend.setDisable(false);
			}catch (Exception e){
				taDisplay.appendText("服务器连接失败！"+e.getMessage()+"\n");
			}
		});
	// btConn.defaultButtonProperty();

	btnSend.setOnAction(new BtnSendHandler() );
	//对输入区域绑定键盘事件
	tfSend.setOnKeyPressed(new PressSendHandler() );
	
	
		btnExit.setOnAction(event -> {
			try {
				exit();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
	//窗体关闭响应的事件,点击右上角的×关闭,客户端也关闭
		primaryStage.setOnCloseRequest(event -> {
			try {
				exit();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
	class BtnSendHandler implements  EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			String msg=tfSend.getText();
			tcpClient.send(msg);//向服务器发送一串字符 用户名和密码
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
	class PressSendHandler implements EventHandler<KeyEvent>{
		@Override
		public void handle(KeyEvent event) {
			if(event.getCode()==KeyCode.ENTER){
				String msg=tfSend.getText();
				tcpClient.send(msg);//向服务器发送一串字符
				taDisplay.appendText("客户端发送："+msg+"\n");
				
				if (msg.equalsIgnoreCase("bye")){ //业务逻辑应该分离出来, 不应该在键盘事件中. 服务器发送一个禁用给你, 然后你的禁用.
					tfSend.setDisable(true);//禁用Enter发送信息输入区域, 
					btnSend.setDisable(true);//发送bye后禁用发送按钮
					//结束服务后再次启用连接按钮
					btConn.setDisable(false);// 两个bye的代码, 这好愚蠢, 我可不可以把msg抽象出来, 只要发送了bye, 那就禁用.
				}
				tfSend.clear();
			}
		}
	}
	private void exit() throws InterruptedException {
		if (tcpClient!=null){
			tcpClient.send("bye");
			Thread.sleep(1000);//多线程等待，关闭窗口时还有线程等待IO，设置1s间隔保证所有线程已关闭
			tcpClient.close();
		}
		System.exit(0);
	}
}