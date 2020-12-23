### 时间安排：

12月23号

开始做, 





12月17日 完成各个部分如加入、删除、成绩 录入修改、条件查询、汇总统计，学院，班级，同学的主要功能 。同时设置快捷键。

12月18-24日  数据管理 功能

12月25-31日  绘图功能，绘图菜单，图标工具栏，状态信息栏

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

完成UDP