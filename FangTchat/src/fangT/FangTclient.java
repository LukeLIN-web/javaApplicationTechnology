package fangT;
//UDPClient.java
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class FangTclient extends Application {

private Button btnExit=new Button("退出");
private Button btnSend = new Button("发送");

private TextField tfSend=new TextField();//输入信息区域

private TextArea taDisplay=new TextArea();//显示区域
private TextField ipAddress=new TextField();//填写ip地址
private TextField tfport=new TextField();//填写端口
private Button btConn=new Button("连接");
private UDPClient FangTclient;

private String ip;
private String port;


@Override
public void start(Stage primaryStage) {
BorderPane mainPane=new BorderPane();

//连接服务器区域
HBox hBox1=new HBox();
hBox1.setSpacing(10);
hBox1.setPadding(new Insets(10,20,10,20));
hBox1.setAlignment(Pos.CENTER);
hBox1.getChildren().addAll(new Label("ip地址："),ipAddress,new Label("端口："),tfport,btConn);
mainPane.setTop(hBox1);

VBox vBox=new VBox();
vBox.setSpacing(10);

vBox.setPadding(new Insets(10,20,10,20));
vBox.getChildren().addAll(new Label("信息显示区"),taDisplay,new Label("信息输入区"),tfSend);

VBox.setVgrow(taDisplay, Priority.ALWAYS);
mainPane.setCenter(vBox);


HBox hBox=new HBox();
hBox.setSpacing(10);
hBox.setPadding(new Insets(10,20,10,20));
hBox.setAlignment(Pos.CENTER_RIGHT);
hBox.getChildren().addAll(btnSend,btnExit);
mainPane.setBottom(hBox);

Scene scene =new Scene(mainPane,700,500);
primaryStage.setScene(scene);
primaryStage.show();

//连接服务器之前，发送bye后禁用发送按钮，禁用Enter发送信息输入区域，禁用下载按钮
btnSend.setDisable(true);
tfSend.setDisable(true);

//连接按钮
btConn.setOnAction(event -> {
ip=ipAddress.getText().trim();
port=tfport.getText().trim();

try {
FangTclient = new UDPClient(ip,port);
//连接服务器之后未结束服务前禁用再次连接
btConn.setDisable(true);
//重新连接服务器时启用输入发送功能
tfSend.setDisable(false);
btnSend.setDisable(false);
} catch (IOException e) {
e.printStackTrace();
}
});

//发送按钮事件
btnSend.setOnAction(event -> {
String msg=tfSend.getText();
FangTclient.send(msg);//向服务器发送一串字符
taDisplay.appendText("客户端发送："+msg+"\n");

String Rmsg=null;
Rmsg=FangTclient.receive();
// System.out.println(Rmsg);
taDisplay.appendText(Rmsg+"\n");

if (msg.equals("bye")){
btnSend.setDisable(true);//发送bye后禁用发送按钮
tfSend.setDisable(true);//禁用Enter发送信息输入区域
//结束服务后再次启用连接按钮
btConn.setDisable(false);
}
tfSend.clear();
});
//对输入区域绑定键盘事件
tfSend.setOnKeyPressed(new EventHandler<KeyEvent>() {
	@Override
	public void handle(KeyEvent event) {
		if(event.getCode()==KeyCode.ENTER){
		String msg=tfSend.getText();
		FangTclient.send(msg);//向服务器发送一串字符
		taDisplay.appendText("客户端发送："+msg+"\n");
		
		
		String Rmsg=null;
		Rmsg=FangTclient.receive();
		taDisplay.appendText(Rmsg+"\n");
		
		if (msg.equals("bye")){
		tfSend.setDisable(true);//禁用Enter发送信息输入区域
		btnSend.setDisable(true);//发送bye后禁用发送按钮
		//结束服务后再次启用连接按钮
		btConn.setDisable(false);
		}
		tfSend.clear();
		}
	}
});

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


//信息显示区鼠标拖动高亮文字直接复制到信息输入框，方便选择文件名
//taDispaly为信息选择区的TextArea，tfSend为信息输入区的TextField
//为taDisplay的选择范围属性添加监听器，当该属性值变化(选择文字时)，会触发监听器中的代码
taDisplay.selectionProperty().addListener(((observable, oldValue, newValue) -> {
//只有当鼠标拖动选中了文字才复制内容
if(!taDisplay.getSelectedText().equals(""))
tfSend.setText(taDisplay.getSelectedText());
}));
}

private void exit() throws InterruptedException {
if (FangTclient!=null){
//向服务器发送关闭连接的约定信息
FangTclient.send("bye");
FangTclient.close();
}
System.exit(0);
}


}