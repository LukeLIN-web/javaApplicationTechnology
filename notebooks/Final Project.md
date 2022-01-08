### 时间安排：

12月23号

开始做, 

17点19完成UDP,20点02完成单线程.首先是实现了Server和Client的互相通信，在这个过程发现问题，接着就使用多线程技术解决客户端实时接收信息的问题，后来到了服务器端，发现多用户连接服务器的“先到先得”问题，“后到者”无法正常通信，所以再使用线程池技术解决了多用户服务器的问题。

12月17日 

12月18-24日  数据管理 功能

12月30日  给他加了icon, logo. 阿里巴巴icon矢量图库. 

接下来, 我觉得不需要输入端口,首先把端口和local固定. 用户名和密码



1月2日 , 实现了每次加入一个客户端, 服务器就会输出一次连接的客户端主机名和IP地址.

登录功能完成2021.1.6

 1月10日-1月14日 撰写大程序最终版报告



### 计划草稿

这个学期, 第一个是爬虫, 爬取网页然后处理文本.

第二个是web服务器,  可以用浏览器打开服务器发送过来的文件. 

第三个是数据库操作 . 提取数据库数据.

那我首先应该搞个GUI,

  然后数据库操作

我可以做servlet 和 MVC 

三、PPT：10%

https://www.cnblogs.com/chenzhenhong/p/13972517.html#l3

https://www.cnblogs.com/chenzhenhong/p/14168284.html 借鉴意义. 他是tcp 字符串一直发,我们改进是 先登录,然后也一直发. 

### 遇到的问题

1. 没有javafx ,因为已经分离出来了

先去https://gluonhq.com/products/javafx/ 下载, 解压,然后add jar, 把/lib下面的jar都加入进去.

2. 错误: 在类 fangT.MainServer 中找不到 main 方法, 请将 main 方法定义为:
      public static void main(String[] args)
   否则 JavaFX 应用程序类必须扩展javafx.application.Application

但是我已经有main方法了. 可能因为类名是关键字,改了之后出现:

3. 错误: 找不到或无法加载主类 fangT.FtServer
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

5. 错误java.net.SocketException: Socket is closed

该异常在客户端和服务器均可能发生。异常的原因是己方主动关闭了连接后（调用了Socket的close方法）再对网络连接进行读写操作。

由于程序开启了多线程，这就存在有几个线程处理问题。那么，在我们手动关闭窗体的时候，有的线程处于等待状态，等待IO操作，并非在关闭的同时所有线程立即也关闭了，所以解决方法就是，设置一小段间隔时间保证所有线程已关闭。

在退出方法里面设定线程睡眠时间，保证退出按钮和关闭窗体不抛出此类异常。

6.  错误 java.lang.NullPointerException

发送消息时, Exception in thread "JavaFX Application Thread" 

因为前面没有给变量赋值.

7. 退出时有异常

退出客户端时有EOF异常:

当Socket.InputStream亦或是Socket.OutputStream调用close()方法都会使得Socket失效，从而服务端抛出异常。
 另一方面在套接的其他流调用了close()方法中会调用Socket的流的close()方法。

服务器有java.net.SocketException: Socket closed

While里面套while是不行的, 里面的while还是会出错.

事实上这里有几个点, 第1个问题是要正确区分长、短连接。所谓的长连接是一经建立就永久保持。短连接就是在以下场景下，准备数据—>建立连接— >发送数据—>关闭连接。要判断啥时候用长连接,啥时候用短连接.

第2个问题是对长连接的维护。所谓的维护包括两个方面，首先是检测对方的主动断连（既调用 Socket的close方法），其次是检测对方的宕机、异常退出及网络不通。这是一个健壮的通信程序必须具备的。检测对方的主动断连很简单，主要一方主动断连，另一方如果在进行读操作，则此时的返回值-1，一旦检测到对方断连，则应该主动关闭己方的连接（调用Socket的close方法）。

而检测对方,通常方法是用“心跳”，也就是双方周期性的发送数据给对方，同时也从对方接收“心跳”，如果连续几个周期都没有收到对方心跳，则可以判断对方或者宕机或者异常推出或者网络不通，此时也需要主动关闭己方连接，如果是客户端可在延迟一定时间后重新发起连接。

第3个问题是处理效率问题。不管是客户端还是服务器，如果是长连接一个程序至少需要两个线程，一个用于接收数据，一个用于发送心跳，写数据不需要专门的线程，当然另外还需要一类线程（俗称Worker线程）用于进行消息的处理，也就是说接收线程仅仅负责接收数据，然后再分发给Worker进行数据的处理。如果是短连接，则不需要发送心跳的线程，如果是服务器还需要一个专门的线程负责进行连接请求的监听。这些是一个通信程序的整体要求. 

8. 局部变量不能赋值

这是因为你使用的局部变量在初始化后，又对这个变量进行了赋值。赋值后会认为这个变量不是final了，所以报错. 即将你想要使用的局部变量，在使用前赋值给一个新的变量，这样java8会认为这个新的变量是final，没有变化的，可以使用。lambda表达式也有类似问题，其可以访问给它传递的变量，访问自己内部定义的变量，同时也能访问它外部的变量。但lambda表达式访问外部变量有一个非常重要的限制：变量不可变（只是引用不可变，而不是真正的不可变）。

有两种方式：使用数组或者把局部变量定义为全局变量。

这2种方式，其实本质是一样的：内存都分配在堆上。这就决定了，使用这2种方式来修改变量的值，是可行的。

9. toServer.writeUTF(usr); 不能收到

因为usr = username.getText(). 没有, why?

原因:在句柄中。您现在所做的是读取默认值 - 这些值为空字符串。此外，您应该向按钮添加侦听器，而不是在初始化时调用句柄

10. Read会阻塞吗? readUTF 可控吗? 怎么知道要读多少?

public final int read(byte b[], int off, int len) throws IOException这个方法。
 而public final int read(byte[] b) throws IOException，需要IO将参数缓冲区填满才返回，
 这就有两种情况：
 一：缓冲区的大小，比数据包小。
   这样，实际上读取的是个半包，当然，一般后面程序会出现异常。
 二：缓冲区的大小，比数据包大。
   这样，实际上读取的是一个整包加一个半包。
   这时，后面的程序，有可能只处理了那个整包的数据，缓冲区中的半包数据没有被处理，被丢弃了。
 上述两种状况，都会引起后面的粘包现象。
 所以，在知道包长度的情况下，使用我说的那个方法来读取数据，可以有效控制所读数据的长度。

  方法二是 准备数据报文然后装
 public String receive(){
 String msg;
 //准备空的数据报文
 DatagramPacket inPacket=new DatagramPacket(new byte[MAX_PACKET_SIZE],MAX_PACKET_SIZE);
 try {
 //读取报文，阻塞语句，有数据就装包在inPacket报文中，以装完或装满为止
 socket.receive(inPacket);
 //将接收到的字节数组转为对应的字符串
 msg=new String(inPacket.getData(),0,inPacket.getLength(),"utf-8");
 } catch (IOException e) {
 e.printStackTrace();
 msg=null;
 }
 return msg;
 }
 本文链接：https://www.cnblogs.com/chenzhenhong/p/13825286.html#l4



 





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

todo:设置登录按钮,以及通过用户名和密码登录. 之后还可以输入用户名和密码, 

注册按钮-> 客户端发送用户密码到服务器, 服务器去数据库找是否对应,如果有,那就显示已存在用户名,  如果不对应,那就add 这个. 显示注册成功. 

已经实现了发送string, 但是buffered区分不出来到底是登录信号还是字符串, 所以想要改成dataoutputstream.  写入int作为登录信号.

2021.1.5 完成改成dataoutputstream.  写入int作为登录信号.

账号202 密码wes

账号1 密码123 

Id 302 密码123 

803 , 903 密码 123

问题6:

**while** (continueToSend);会死锁

waitForSendAction(); 我不知道为啥之前不会卡死,现在会卡死.

改成登录成功后再新建.thread, 把这一块放在按钮事件处理中 .

我的登录注册还是有问题, ,注册判断只有一次,一次登录失败了之后就会登录出错.因为是用if来判断之前用while



##### 使用序列化：

先将需要发送的内容构建为一个类,我们定义一个作为注册的信号. 但是java序列化其实是不安全的.而且接收的时候, 接收bufferreader和objectreader可以同时存在吗? 可以同时接收吗?

不知道, 不如全部用dataoutputstream把! 这样应该可以.

子扬说可以http ,但是http 传输, 不知道怎么让服务器接收http解析, 客户端接收http解析, 还是先tcp搞定了, 别的再说.**可以用****java****后台给指定接口发送****json****数据**

下次你调试应该另开一个小地方复制代码调试,而不是直接在大代码上改. 或者你restore之前先把代码复制出来, 不然都白肝了.

之前是按一下按钮连接, 现在是打开就连接, 

```
[Serializable]
public class WBMessage
{
    public byte _number;
    public short IP_Port;
    public string IP_Address;
}
```

发送前序列化,接收后反序列化





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



豆瓣搜索

前端HTML,css ----keyword两边加个标签,实现高亮.

但是她很多东西没有, 需要先手工输入芙蓉镇, 爬取, 然后再搜索. 老师说你可以定时爬取一次. (shell脚本hhh ) 









#### 事件的监听

一开始的时候,我的设计是客户端发送"bye"只是为了转发, appendText和 disable按钮都是在客户端事件中直接控制.  匿名内部类. 太过冗长 , 可以改写.

 *在事件源对象注册 source.setOnXEventType(listener)*

 btEnlarge.setOnAction(new EnlargeHandler());

 *// 事件处理器：实现EventHandler <T extends Event>接口*    class EnlargeHandler implements EventHandler<ActionEvent> {

1.2. 11.21 实现了把事件从start()中分离出来, 新建BtnSendHandler类. 

start()中只需要new BtnSendHandler() 即可

修改想法: 应该是信息输入区域监听msg?  源对象为按钮,监听器为tadisplay和其他被禁用的按钮



#### 和服务器的通讯

2021年1月11日星期一

问题7: 

1. 依然没发给另一个客户端B发送A登录的信息.而是给B发送B登录的信息.怎么发给A的stream呢?

2. A 发送信息, 服务器可以接收到, 但是返回给了B,服务器users.get(socket)显示是B

3. A 继续发送信息, 服务器不可以接收到.此时B发送, 服务器也不可以接收到

 登录后新建一个thread接收. 因为inputstream是全局变量,后一个登录了,全局变量就改了, 所以别的客户端收不到. 把inputstream 放在handler里就可以了

Socket write 真的好蠢,每个write read都要自己写, 还是http一个包发过去清楚明白.

]

### Adapter:

类中静态的方法或者属性，本质上来讲并不是该类的成员，在java虚拟机装在类的时候，这些静态的东西已经有了对象，它只是在这个类中”寄居”，不需要通过类的构造器（构造函数）类实现实例化；而***非静态的属性或者方法，在类的装载是并没有存在，需在执行了该类的构造函数后才可依赖该类的实例对象存在。所以在静态方法中调用非静态方法时\***，编译器会报错

main（）函数一定是静态的，没有返回值，形参为数组。

```java
public class MediaAdapter implements MediaPlayer {
 
   AdvancedMediaPlayer advancedMusicPlayer;
 
   public MediaAdapter(String audioType){
      if(audioType.equalsIgnoreCase("vlc") ){
         advancedMusicPlayer = new VlcPlayer();       
      } else if (audioType.equalsIgnoreCase("mp4")){
         advancedMusicPlayer = new Mp4Player();
      }  
   }
 //实现多态, 都用
   @Override
   public void play(String audioType, String fileName) {
      if(audioType.equalsIgnoreCase("vlc")){
         advancedMusicPlayer.playVlc(fileName);
      }else if(audioType.equalsIgnoreCase("mp4")){
         advancedMusicPlayer.playMp4(fileName);
      }
   }
}
```

这样多态, play() 就可以用了. 用户只用play, 不用管,也不管实现, 

![d](https://www.runoob.com/wp-content/uploads/2014/08/20201204-adapter.png)



#### pre: PPchat 

绘图在label后面,   看起来像气泡.

自定义协议.

输出流. 



解释器模式, 不要一个个o, l ,e解析,而是通用的解释成语法树. 



frame和业务逻辑分开两个java文件.

其实是因为用了swing图形化编程，直接拖的,是自动生成的

她这个可以发送文件, 可以显示在两边.

#### 21点卡牌在线游戏

前端用html, java写后端.

网址可以用学校内网. 一个服务器在她宿舍的电脑上.

手撸前端框架.

好家伙, 我只实现了她游戏中 聊天室的功能.



看别人的presentation, 才知道很多东西. 这就是选课上课的意义.和优秀的同学交流.

有个同学做了服务器UI, 但是服务器UI没有任何功能 , 就纯显示. 老师说可以加个按钮移除用户. 





一个同学做 AI player,基类abstract class  player 

然后human player和AI player 继承这个基类.





#### concurrent hashmap保存socket和id

ConcurrentHashMap是由Segment数组结构和HashEntry数组结构组成。Segment是一种可重入锁ReentrantLock，在ConcurrentHashMap里扮演锁的角色，HashEntry则用于存储键值对数据。

一个ConcurrentHashMap里包含一个Segment数组，Segment的结构和HashMap类似，是一种数组和链表结构， 一个Segment里包含一个HashEntry数组，每个HashEntry是一个链表结构的元素， 每个Segment守护者一个HashEntry数组里的元素,当对HashEntry数组的数据进行修改时，必须首先获得它对应的Segment锁。

ConcurrentHashMap完全允许多个读操作并发进行，读操作并不需要加锁。

如果使用传统的技术，如HashMap中的实现，如果允许可以在hash链的中间添加或删除元素，读操作不加锁将得到不一致的数据。

```java
static final class HashEntry<K,V> {  
     final K key;  
     final int hash;  
     volatile V value;  
     final HashEntry next;  
 }
```

可以看到除了value不是final的，其它值都是final的，这意味着不能从hash链的中间或尾部添加或删除节点，因为这需要修改next 引用值，所有的节点的修改只能从头部开始。对于put操作，可以一律添加到Hash链的头部。

但是对于remove操作，可能需要从中间删除一个节点，这就需要将要删除节点的前面所有节点整个复制一遍，最后一个节点指向要删除结点的下一个结点。这在讲解删除操作时还会详述。为了确保读操作能够看到最新的值，将value设置成volatile，这避免了加锁。

- **是否需要扩容。**在插入元素前会先判断Segment里的HashEntry数组是否超过容量（threshold），如果超过阀值，数组进行扩容。值得一提的是，Segment的扩容判断比HashMap更恰当，因为HashMap是在插入元素后判断元素是否已经到达容量的，如果到达了就进行扩容，但是很有可能扩容之后没有新元素插入，这时HashMap就进行了一次无效的扩容。

- **如何扩容。**扩容的时候首先会创建一个两倍于原容量的数组，然后将原数组里的元素进行再hash后插入到新的数组里。为了高效ConcurrentHashMap不会对整个容器进行扩容，而**只对某个segment进行扩容**。







