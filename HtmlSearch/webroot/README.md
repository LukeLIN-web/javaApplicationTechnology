# Java服务器程序设计



#### 使用手册

启动project,  运行httpserver 程序,  显示"服务器开始等待..."

打开浏览器, 输入(http://localhost:9090/hello.jsp) 或者把hello.jsp改成webroot的其他文件. 

try1.jzup第一次访问时产生对应java文件,再自动编译成相对应的.class文件, 执行结果发送到控制台. 

程序就会读取文件, 然后发送给浏览器.

不再访问时, 显示"关闭 socket 对象"





##### 【系统实现功能】

- 以Socket建立一Web服务器，对不同文件扩展名采用不同方式向客户端传送内容： 

  1、.htm/.txt/.java文件：原样文本传送。 

  2、.zup：原样文本传送，但对"<%"和"%>"标记之间的内容按表达式或JS程序方式执行后传送结果。(静态网页)  未实现处理标记内结果

  3、.jzup：在第一次访问时将文件转化成"<文件名>_jzup.java"，手工或自动编译成相应的类，传送该类的执行结果。以后则直接传送"<文件名>_jzup.class"的运行结果。(类似Tomcat，.jzup参考.jsp的语法) 

  3、其它文件按二进制传送。 (拓展是asp jsp(自己定协议,自己定规则,如四则运算),就特殊处理, 

  其他拓展名,就传输字节流.)

  4、文件不存在：按html协议发送404消息。(直接发送错误信息就可以.) √已完成
  
  *：表单处理、上传、下载组件等可选。

本地需要什么文件?

我们本地需要 有a.htm, .zup, .jzup, java,一个图像.我们要写jsp 文件.有两个目录,一个是写代码的目录, work目录是放置发布了之后预处理后的.我们要写jsp 文件.jsp是直接把 html 与脚本混编,实现简单的动态页面. 浏览器将 GET index.jsp 的请求发送到我们的服务器，服务器看到这个请求以后，把 jsp 中的脚本拿出来执行，将 JSP 翻译成 html，然后再把这些 html 文本发送回客户端。我们的服务器类似于 Jetty程序,还要负责进行脚本的解释。

客户端是浏览器 , 服务器在哪里工作?是cmd吗? 

服务器就是在运行的java文件,浏览器客户端 可以访问127.0.0.1:9999/a.htm ,然后服务器找到这个文件通过http发给客户端. 

有两个目录,一个是写代码的目录, work目录是放置发布了之后预处理后的,

 [实验原理]

三种服务器 

一种读了 ,就写,下一个循环就重新连接.

Asp. 嵌入的 进行识别,编译运行出来,其他的照旧

Jsp, 服务器先第一次预处理,把他变成类, 之后把类的结果给客户端. 有六大类变量, request(把十六进制变成正常的字符串), application, cookie(浏览器记住cookie,发给服务器)等, 

聊天室(论坛), 在服务器有个变量, 有人讲话了, 就加在变量上, 然后推送给客户端. 以前浏览器需要自动刷新, 自动推送一次.  

线程服务器

Tomcat的服务器

有POST方式和GET方式, 

可以处理上传,下载,表单处理.

 



##### txt,jsp,文件不存在和htm实现 11月9日

ajax.html 文件 

在这个网页上有一个button，当点击这个 button 的时候，
就会调用 loadXMLDoc。在这个方法里，创建了一个XMLHttpRequest，
通过这个对象的open方法，访问了服务端的 ajax_info.txt，
当数据返回的时候，就会调用一个匿名函数，来设置网页内容。
注意看一下上面代码中的注释。
客户端收到一个普通的HTML文件，里面有一些javascript代码，
这些代码会使得浏览器向服务端再发送一次GET报文，去请求服务端的数据。



##### hello.jsp  

hello.jsp 利用 method="post" , 可以输入账号密码,

账号密码不会直接显示在地址栏,可以发送到服务器,密码在服务器端显示.服务器发送login.jsp给客户端 





##### 待完成:解析jsp

https://www.cnblogs.com/zollty/p/3309310.html 这个太复杂了, 是整个jsp协议的解析.



##### 待完成:其它文件按二进制传送

需要用if 来判断后缀名  √ 完成了switch case

在字节流中输出数据主要是使用OutputStream完成，输入使用的是InputStream，在字符流中输出主要是使用Writer类完成，输入主要是使用Reader类完成。



##### 待完成:在第一次访问时将文件转化成手工或自动编译成相应的类，传送该类的执行结果。以后则直接传送的运行结果

完成结果: 11.25号, 完成了第一次访问时自动编译成相对应的类, 执行结果发送到控制台. 传送该类的执行结果。以后则直接传送的运行结果,不写了,作业写不完了, 以后没事再看看. 



#### 表单处理原理

http请求有两种方式，Get和Post。

使用post方法，第一行，http method会发生变化，然后我们自己的参数都会以正文的形式发送到服务端。而不再是直接放在URL里，展示在地址栏里了。在浏览器地址栏中输入地址的方式是Get方式，所有的链接都是Get方式，表单默认也是Get方式，唯有在表单中指定method=“post”才是Post方式。

Jetty在帮我们分析完http报文以后，都把这些用户参数放到一个叫request的内置变量中去了。request变量不需要声明，是JSP内嵌的对象，它的作用就是封装用户数据，换句话说，Jetty从HTTP报文中得到的参数都以某种形式存在这个request中。

实际上，request这个内嵌变量是JSP标准规定的。但是具体怎么实现，可能各种Server都不一样。这一点，涉及到Java标准的制定，这些知识我们下节再讲。

好了，今天总结一下，要掌握的重点就是一个：浏览器通过form获取用户参数，并把它们转化成HTTP报文发给服务端，服务端从报文中解析出参数，然后就可以在服务端脚本中加以使用了。





##### Servlet

Servlet（Server Applet）是[Java](https://baike.baidu.com/item/Java/85979) Servlet的简称，称为小服务程序或服务连接器，用Java编写的[服务器](https://baike.baidu.com/item/服务器/100571)端程序，具有独立于平台和[协议](https://baike.baidu.com/item/协议/13020269)的特性，主要功能在于交互式地浏览和生成数据，生成动态[Web](https://baike.baidu.com/item/Web/150564)内容。Servlet运行于支持Java的应用服务器中。从原理上讲，Servlet可以响应任何类型的请求，但绝大多数情况下Servlet只用来扩展基于HTTP协议的Web服务器。



#### 问题

问题1:

The archive: C:/Users/12638/Downloads/htmlparserv16/htmlparser1_6/libs/sax2.jar which is referenced by the classpath, does not exist.

解决方法:

https://docs.oracle.com/en/ 这里有文档

1. Right-click on the project in the **Project Explorer** view and select **Properties** from the drop-down menu.This will open the **Propertis** dialog.
2. On the **Propertis** dialog, select the **Java Build Path** from the list of properties.



问题2 :

The method readLine() from the type DataInputStream is deprecated 不同意你这么用

干脆全部去掉

问题3 : 

java.net.BindException: Address already in use: bind

solution: 

**在cmd窗口中输入命令--**

**netstat -ano|findstr 8080  (8080指的是被占用的端口号)**

**该命令执行完之后，可以得到占用改端口号的进程的pid**

**然后在cmd窗口中输入命令--**

**taskkill -pid  上个命令得到的pid  -f**



问题4：我们在浏览器中可以访问html，但是怎么访问一个java类呢？java 文件直接按文本传输,但是他不能换行?
   html是静态资源（它里面是不可以定义变量的），它可以直接被浏览器解析，但是jsp不行，因为它里面有变量，必须被服务器变成html才能被解析。

问题5:

```java
File file = new File(Constants.BASEDIR+"/"+filename+"_jzup.java"); 
//  这个"/" 就相等于"\\",  路径要有"/" ,要有后缀.
Constants.BASEDIR+"/"+filename+"_jzup.java" // 就可以在basedir下面,加一个java文件.
     PrintWriter out = new PrintWriter(new FileWriter(file),true); 
```



问题6:  我后缀要加上jzup,但是错误: 类 是公共的, 应在名为 的文件中声明 

所以在不做其他变动的情况下，最简单 的解决办法是将public class改为public static class，或者尝试删除public。

public static class 也没用,我删除了public,但是又报错

问题6.1:

cannot access a member of class yst with modifiers "public static"

后来发现我是个呆瓜

我直接在filename后面加上

```java
String filename = code.substring(1, 4)+ "_jzup";
```

不就行了.



**当程序运行结束，JVM终止时才真正调用deleteOnExit()方法实现删除操作。即该方法是将删除的命令缓存了一下，到服务停止的时候再进行操作！**

#### 完成时间

大三上2020/11月




附录: 
####  中文代码实现一个最简单的服务器,后缀是cjv
  ``` C++
 
导入 输入输出包.*; 
导入 网络包.*; 
导入 实用包.*; 
类 网站服务器 { 公共 静态 空返 主程序(字串[] 命令行) 
{ 服务套接字 服; 套接字 客; 
 尝试 
{ 服 = 新建 服务套接字(9999); } 
 捕捉(异常 异)
 { 系统.输出.打印("异常: " + 异); 返回; } 
 当(真){ 尝试 { 系统.输出.打印("\n\n接受客户连接..."); 
           客 = 服.接受(); 
           //接受连接 
           文本缓读器 读 = 新建 文本缓读器( 新建 文本输入流(客.获得输入流()));
           字串 行 = 读.行读(); 系统.输出.打印("--=> "+行); 
           字串 名 = 行.子串(行.搜索("/")+1, 行.倒搜(" ")); 
           系统.输出.打印("--=> "+名); 
           数据输入流 入 = 新建 数据输入流( 新建 缓冲输入流(新建 文件输入流(名))); 
           数据输出流 出 = 新建 数据输出流( 新建 缓冲输出流(客.获得输出流())); 
           尝试 { 当(真)出.字节写(入.字节读()); } 
           捕捉(异常 异) {} 出.关闭(); 入.关闭(); } 
      捕捉(异常 异){异.栈跟踪显示();} } //当 
} 
        } 
  ```



#### 12月2日更新

服务器给你发送, 发送到服务器上, 然后你连接了再给你发过去.

别人不知道你的ip地址, 服务器记住你的ip地址.

但是这样服务器转发压力很大.

有的服务器把地址发给你, 你登录后得到另一个人的地址.

维护一个好友的ip地址表.

最早qq 有人做了插件, 通过ip地址分析出来好友在哪里. 腾讯改改数据格式, 改协议, 但是还是老是被破解, 然后腾讯就把有名的破解公司给告了, 负责人关了几年.



服务器 最重要两个, 

一个要保留输出方式. 用一个数组保存起来.

另一个 针对客户端的输入监听. 

就像你上课, 想听下课的铃, 其实你是一直听着的, 下课你就会行动.

对于每一个来连接的人, 都留下联系方式. 每个客户端有服务器的联系方式

服务器监听

客户端



按键的事件响应没有settext , 而是发送到服务器, 服务器收到了, 有一个广播, 

客户端有一个线程等待这个消息, 收到消息后再settext.