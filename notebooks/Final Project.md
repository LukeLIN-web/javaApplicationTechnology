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



线程真的是一个难点, 程序很容易崩溃,而且java也没有抛出异常.

存储, 要用对象的思维来考虑，不要考虑raw data，举例 :

map不要用户名+ 密码就放不下了, 你就socket 作为key ,然后用户的一堆信息作为class放在value.



如何做到群组的聊天？想法就是客户A的聊天信息通过服务器转发到同时在线的所有客户。

具体做法是需要在服务器端新增记录登陆客户信息的功能，每个用户都有自己的标识。本篇将使用简单的“在线方式”记录客户套接字，即采用集合来保存用户登陆的套接字信息，来跟踪用户连接。

所以，我们需要选择一种合适的数据结构来保存用户的Socket和用户名信息

　　如果是有用户名、学号登录的操作，就可以采用Map类型的集合来存储，例如可使用key记录用户名+学号，value保存套接字。对于本篇的网络聊天室的需求，需要采用Map，用来保存不同用户的socket和个人信息。用户套接字socket作为key来标识一个在线用户是比较方便的选择，因为每一个客户端的IP地址+端口组合是不一样的。


#### 亮点

多线程技术, concurrenthashmap 实现线程安全. 

#### GUI技术javafx

​	taDisplay.setStyle("-fx-text-fill:black"); 变色.但是没法让文字和日期不同颜色 , 可能是因为Platform.runLater的缘故.

//如果构造多个Stage对象，能达到多窗口的效果



#### 登录

id as primary key,
    name , password, name can be show but log in with id and password.

 I will create class to add password and name in
    database, delete it, find id and compare input password with password, change password.

