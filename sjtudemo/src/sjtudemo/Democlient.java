package sjtudemo;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

public class Democlient extends Application{
	JFXPanel jfxPanel = new JFXPanel();//否则会有java.lang.RuntimeException: Internal graphics not initialized yet
	protected Button btnExit = new Button("退出");  
	protected Button btnexplore = new Button("explore");
	protected Button btBegin = new Button("确定");
	protected Button btRegst = new Button("注册");
	public TextField tfSend = new TextField();//输入信息区域
	protected TextArea taDisplay = new TextArea();//显示区域
	protected TextField username = new TextField();//填写用户名
	protected PasswordField LOGINpassword = new PasswordField();//login in 
	protected TextField fangTangId = new TextField();//填写id
	protected PasswordField REGIpassword = new PasswordField();//填写密码
	protected File file; 
	public void adjustStyle(Stage primaryStage) {
		BorderPane mainPane = new BorderPane();//BorderPane，默认就分割好了上下左右中的五个部分

		HBox hBox1 = new HBox();
		hBox1.setSpacing(10);
		hBox1.setPadding(new Insets(10,20,10,20));//setPadding()：以外部控件的角度，规定其内部控件与其的距离
		hBox1.setAlignment(Pos.CENTER);
		hBox1.getChildren().addAll(new Label("备用输入框:"),fangTangId,btnexplore,btnExit);// 从左到右horizon

		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(10,20,10,20));
		tfSend.setPrefWidth(300.0);// 可以手动修改长度
		//hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(new Label("信息输入区"),tfSend,btBegin);
		btnexplore.setStyle("-fx-background-color:linear-gradient(#E4EAA2, #9CD672);");//-fx-border-color:black; -fx-padding:3px;-fx-background-color:POWDERBLUE;
	
		VBox vbox1 = new VBox();
		vbox1.setSpacing(10); //设置单行面板组件的间距为10像素
		vbox1.setPadding(new Insets(10,20,10,20));
		vbox1.getChildren().addAll(hBox1,hBox);
		
		VBox vBox = new VBox();//vertical box 
		vBox.setSpacing(10); //设置单行面板组件的间距为10像素
		vBox.setPadding(new Insets(10,20,10,20));
		vBox.getChildren().addAll(new javafx.scene.control.Label("结果显示区"),taDisplay);
		VBox.setVgrow(taDisplay, Priority.ALWAYS);//Priority，一个枚举类,用于确定给定节点的增长（或缩小）优先级。比如:一个HBox布局，里面有三个控件，当屏幕宽度是800时，刚好把屏幕占满，但是当屏幕扩大到1200时，这个Priority规定了这三个控件如何处理增加的400宽度。共有三个取值：ALWAYS：布局区域将始终尝试增长（或缩小），共享那些空间;
		
		mainPane.setTop(vbox1);// 放上方
		//mainPane.setCenter(hBox);
		mainPane.setCenter(vBox);//下方 
		
		Scene scene =new Scene(mainPane,700,600);//scene is a tree-structure,其根节点一般是Pane面板（如StackPane、BorderPane
		primaryStage.setScene(scene);// Place the scene in the stage,Stage包含Scene，Scene包含一个或者多个node节点
		primaryStage.show();// Display the stage
		primaryStage.setTitle("sjtu gui");// Set the stage title
	}
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss") ;
	public void displayIt(){
		launch();//用来启动整个Application
	}
	@Override
	public void start(Stage primaryStage) { //start()方法传入的Stage对象在JavaFX程序加载时被加载（JavaFX的Stage是顶层容器）
		adjustStyle(primaryStage);
		primaryStage.show();
		System.out.println("XXX");
		btBegin.setOnAction(event ->{
			String msg = tfSend.getText();
			System.out.println(msg);
			Writefile wr = new Writefile();
			try {
				wr.name(file,msg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
					Platform.runLater(()->{// 稍后更新GUI
						now = LocalDateTime.now();
						taDisplay.appendText(dtf.format(now)+"\n");// format print
					});
			}catch (Exception e){
				taDisplay.appendText("服务器连接失败！"+e.getMessage()+"\n");
			}
		});
		btnexplore.setOnAction(event -> {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				file = fileChooser.showOpenDialog(primaryStage);
				taDisplay.appendText("已读入文件,文件路径为"+file.toString()+"\n");
			}catch (Exception e) {
				e.printStackTrace();
			}
		});// 按浏览按钮
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
	}
	
	//更新客户端的按钮等控件, 修改一些变量. 
	private void resetByServerInfo(String serverInfo){
		char[] arr = serverInfo.toCharArray();    // char数组
		switch( arr[0] ){
			case 'l': //登录成功
				btBegin.setDisable(true);//一个客户端登录一个账号,不再登录,连接服务器之后未结束服务前禁用再次连接
				btRegst.setDisable(true);//一个客户端登录一个账号,也不再注册.
				break;
			case 'f':// 登录失败	
				break;
			case 'b':
				tfSend.setDisable(true);//禁用Enter发送信息输入区域
				btBegin.setDisable(false);//结束服务后再次启用连接按钮
				break;
			default :
				taDisplay.appendText("\n客户端更新完毕! \n");
		}	
	}


	private void exit() throws InterruptedException, IOException {
			Thread.sleep(1000);//多线程等待，关闭窗口时还有线程等待IO，设置1s间隔保证所有线程已关闭
			close();
		System.exit(0);
	}
	public void close(){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}