### 时间安排：

12月23号

开始做, 

17点19完成UDP,20点02完成单线程.首先是实现了Server和Client的互相通信，在这个过程发现问题，接着就使用多线程技术解决客户端实时接收信息的问题，后来到了服务器端，发现多用户连接服务器的“先到先得”问题，“后到者”无法正常通信，所以再使用线程池技术解决了多用户服务器的问题。

12月17日 

12月18-24日  数据管理 功能

12月30日  给他加了icon, logo. 阿里巴巴icon矢量图库. 

接下来, 我觉得不需要输入端口,首先把端口和local固定. 用户名和密码

1月1日-1月9日 撰写大程序报告，调试，制作视频，

 1月10日-1月14日 撰写大程序最终版报告



### 计划草稿

这个学期, 第一个是爬虫, 爬取网页然后处理文本.

第二个是web服务器,  可以用浏览器打开服务器发送过来的文件. 

第三个是数据库操作 . 提取数据库数据.

那我首先应该搞个GUI,

  然后数据库操作

我可以做servlet 和 MVC 

三、PPT：10%

https://www.cnblogs.com/chenzhenhong/p/14168284.html

https://www.cnblogs.com/chenzhenhong/p/13972517.html#l3



### 遇到的问题

1.没有javafx ,已经分离出来了

先去https://gluonhq.com/products/javafx/ 下载, 解压,然后add jar, 

把/lib下面的jar都加入进去.

2.错误: 在类 fangT.MainServer 中找不到 main 方法, 请将 main 方法定义为:
   public static void main(String[] args)
否则 JavaFX 应用程序类必须扩展javafx.application.Application

但是我已经有main方法了. 可能因为类名是关键字,改了之后出现:

3.错误: 找不到或无法加载主类 fangT.FtServer
原因: java.lang.ClassNotFoundException: fangT.FtServer

解决:  看一下eclipse下面的problems, 因为把不能用的文件放在了classpath里,就报错了.

```
tfSend.setOnKeyPressed(new EventHandler<KeyEvent>() {
	@Override 
	public void handle(KeyEvent event) { //注意这里public 顶格写会报错,必须tab
		if(event.getCode()==KeyCode.ENTER){
		String msg=tfSend.getText();
		FangTclient.send(msg);//向服务器发送一串字符
		taDisplay.appendText("客户端发送："+msg+"\n");
```



4. 错误: 缺少 JavaFX 运行时组件, 需要使用该组件来运行此应用程序

App是默认启动类，自己创建一个main类，然后调用App去启动即可解决问题



5.错误java.net.SocketException: Socket is closed

该异常在客户端和服务器均可能发生。异常的原因是己方主动关闭了连接后（调用了Socket的close方法）再对网络连接进行读写操作。

由于程序开启了多线程，这就存在有几个线程处理问题。那么，在我们手动关闭窗体的时候，有的线程处于等待状态，等待IO操作，并非在关闭的同时所有线程立即也关闭了，所以解决方法就是，设置一小段间隔时间保证所有线程已关闭。

在退出方法里面设定线程睡眠时间，保证退出按钮和关闭窗体不抛出此类异常。

6 错误 java.lang.NullPointerException

发送消息时, Exception in thread "JavaFX Application Thread" 

因为前面没有给变量赋值.

### 设计方法

在java中，主要有两种实现多线程的方法，一是使用Thread类，二是使用Runnable类并实现run()方法。下面我是用Thread类实现，匿名内部类使用了lambda的写法 ，显得更加简洁。

readThread = new Thread(()->{
//匿名内部类函数体

});

把客户端接收的功能交给一个线程处理, 线程放到链接按钮里, 成功连接服务器之后, 程序就启动线程.

由于程序开启了多线程，这就存在有几个线程处理问题。那么，在我们手动关闭窗体的时候，有的线程处于等待状态，等待IO操作，并非在关闭的同时所有线程立即也关闭了，所以解决方法就是，设置一小段间隔时间保证所有线程已关闭。

在退出方法里面设定线程睡眠时间，保证退出按钮和关闭窗体不抛出此类异常。

多线程技术, concurrenthashmap 实现线程安全. 

线程真的是一个难点, 程序很容易崩溃,而且java也没有抛出异常.

存储, 要用对象的思维来考虑，不要考虑raw data，举例 :

map不要用户名+ 密码就放不下了, 你就socket 作为key ,然后用户的一堆信息作为class放在value.



如何做到群组的聊天？想法就是客户A的聊天信息通过服务器转发到同时在线的所有客户。

具体做法是需要在服务器端新增记录登陆客户信息的功能，每个用户都有自己的标识。本篇将使用简单的“在线方式”记录客户套接字，即采用集合来保存用户登陆的套接字信息，来跟踪用户连接。

所以，我们需要选择一种合适的数据结构来保存用户的Socket和用户名信息

　　如果是有用户名、学号登录的操作，就可以采用Map类型的集合来存储，例如可使用key记录用户名+学号，value保存套接字。对于本篇的网络聊天室的需求，需要采用Map，用来保存不同用户的socket和个人信息。用户套接字socket作为key来标识一个在线用户是比较方便的选择，因为每一个客户端的IP地址+端口组合是不一样的。



#### GUI技术javafx

​	taDisplay.setStyle("-fx-text-fill:black"); 变色.但是没法让文字和日期不同颜色 , 可能是因为Platform.runLater的缘故.

//如果构造多个Stage对象，能达到多窗口的效果

一个 JavaFX **场景**（scene graph）来说，事实上它是一个树形的数据结构，其中场景中的每一个**物件**（Item）包含在这棵场景树的**结点**（Node）中



#### 登录

id as primary key,
    name , password, name can be show but log in with id and password.

 I will create class to add password and name in
    database, delete it, find id and compare input password with password, change password.

客户端发送用户密码到服务器, 服务器去数据库找是否对应,如果对应,那就显示登录成功, 连接. 如果不对应

todo:设置登录按钮,

注册按钮-> 客户端发送用户密码到服务器, 服务器去数据库找是否对应,如果有,那就显示已存在用户名,  如果不对应,那就add 这个. 显示注册成功. 

##### 使用序列化：

先将需要发送的内容构建为一个类,我们定义一个作为注册的信号.

```
[Serializable]
public class WBMessage
{
public byte _number;
public short IP_Port;
public string IP_Address;
}
```

发送前序列化

发送前序列化

```
public MemoryStream Serialize(WBMessage msg)
{
MemoryStream ms= new MemoryStream(); 
BinaryFormatter formatter= new BinaryFormatter();
formatter.AssemblyFormat= FormatterAssemblyStyle.Simple;
formatter.TypeFormat= FormatterTypeStyle.TypesWhenNeeded;
formatter.Serialize(ms,msg);
return ms;
}
```

接收后反序列化

```
public WBMessage Deserialize(byte [] bytes, int offset, int count)
{
MemoryStream ms = new MemoryStream(bytes, offset, count);
BinaryFormatter formatter = new BinaryFormatter();
formatter.AssemblyFormat = FormatterAssemblyStyle.Simple;
formatter.TypeFormat = FormatterTypeStyle.TypesWhenNeeded;
WBMessage msg;
msg = (WBMessage)formatter.Deserialize(ms);
return msg;
}
```



如 ，`setOnAction(EventHandler)``setOnMouseClicked(EventHandler)`

第二种是使用`addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler)` 有啥区别?

setOn 鼠标单击"将添加新的事件处理器到您的对象。但它也将取代任何事件处理器之前添加通过该方法！因此，如果您计划在运行时更改对鼠标单击做出反应的方式，则此方法是正确的。

现在，如果您只想添加另一个事件处理器（或多个事件），则应使用"添加事件手"方法。因此，您将在一侧添加处理程序。如果之前添加了事件，您将通过"setOn鼠标单击"保留事件手添加。



#### MVC

很多程序员倾向于将软件的业务逻辑放在Controller里，将数据库访问操作的代码放在Model里

观察者模式有两部分组成，被观察的对象和观察者，观察者也被称为监听者。**对应到MVC中，Model是被观察的对象，View是观察者，Model层一旦发生变化，View层即被通知更新。**View层和Model层互相之间是持有引用的。 我们在开发Web MVC程序时，因为视图层的html和Model层的业务逻辑之间隔了一个http，所以不能显示的进行关联，但是他们观察者和收听者的关系却没有改变。当View通过http提交数据给服务器，服务器上的Model接受到数据执行某些操作，再通过http响应将结果回送给View，View（浏览器）接受到数据更新界面，这不正是一个接受到通知并执行更新的行为吗，是观察者模式的另一种表现形式。

最后是策略模式。策略模式是View和Controller之间的关系，Controller是View的一个策略，Controller对于View是可替换的， View和Controller的关系是一对多，在实际的开发场景中，也经常会碰到一个View被多个Controller引用，这即使策咯模式的一种体现，只是不那么直观而已。



总结一下，关于MVC各层之间关系所对应的设计模式

Model层和View层，实现了观察者模式

View层和Controller层，实现了策咯模式



#### 事件的监听

一开始的时候,我的设计是客户端发送"bye"只是为了转发, appendText和 disable按钮都是在客户端事件中直接控制.  匿名内部类. 太过冗长 , 可以改写.

 *在事件源对象注册 source.setOnXEventType(listener)*

 btEnlarge.setOnAction(new EnlargeHandler());

 *// 事件处理器：实现EventHandler <T extends Event>接口*    class EnlargeHandler implements EventHandler<ActionEvent> {

1.2. 11.21 实现了把事件从start()中分离出来, 新建BtnSendHandler类. 

start()中只需要new BtnSendHandler() 即可

修改想法: 应该是信息输入区域监听msg?  源对象为按钮,监听器为tadisplay和其他被禁用的按钮

