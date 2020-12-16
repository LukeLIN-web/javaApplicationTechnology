# java notebooks





### 变量

类中的变量不用初始化 方法中的局部变量必须初始化

报错:The local variable set may not have been initialized

你确实在for循环中给d2赋值了，但是，循环体并不是一定会执行，如果不执行，d2就没有初始化，而d在for循环外就被赋值了，所以，编译器要求你初始化d2。

初始化方法

```java
  Set<String> testSet = new HashSet<String>();
List<Integer> arr = new ArrayList<Integer>();
```

   Set<Integer> set = new Set<Integer>();是不行的



offer，add区别：
一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。
这时新的 offer 方法就可以起作用了。它不是对调用 add() 方法抛出一个 unchecked 异常，而只是得到由 offer() 返回的 false。 

poll，remove区别：
remove() 和 poll() 方法都是从队列中删除第一个元素。remove() 的行为与 Collection 接口的版本相似，
但是新的 poll() 方法在用空集合调用时不是抛出异常，只是返回 null。因此新的方法更适合容易出现异常条件的情况。
peek，element区别：
element() 和 peek() 用于在队列的头部查询元素。与 remove() 方法类似，在队列为空时， element() 抛出一个异常，而 peek() 返回 null
LinkedList与ArrayList最大的区别是LinkedList更加灵活，并且部分方法的效率比ArrayList对应方法的效率要高很多，
对于数据频繁出入的情况下，并且要求操作要足够灵活，建议使用LinkedList；
对于数组变动不大，主要是用来查询的情况下，可以使用ArrayList。

10月28日
从别人的服务器
接收html文件
然后用浏览器解析.
JSP全称Java Server Pages，是一种动态网页开发技术。它使用JSP标签在HTML网页中插入Java代码
https://www.runoob.com/jsp/eclipse-jsp.html

输入管道
DataInputStream(new buffer(new filestream)) 
输出管道

main

implement  是给编译器看的,证明他有这个能力. 运行的时候不看.
构造函数可以传入一个类.

构造函数 可以new(this).



extend JFrame

stu = new JTextField();

super( "计算器2020");//父类的构造方法.



### String 的用法

java 中String是 immutable的，也就是不可变，一旦初始化，引用指向的内容是不可变的（注意：是内容不可变）。

　　也就是说，假设代码中有String str = “aa”;str=“bb”;，则第二条语句不是改变“aa”原来所在存储地址中的内容，而是另外开辟了一个空间用来存储“bb”；同时由于str原来指向的“aa”现在已经不可达，jvm会通过GC自动回收。

 

　　在方法调用时，String类型和数组属于引用传递，在上述代码中，str作为参数传进change(String str, char ch[]) 方法，方法参数str指向了类中str指向的字符串，但str= "test ok"; 语句使得方法参数str指向了新分配的地址，该地址存储“test ok”，而原来的str仍然指向“good”。对于数组而言，在change方法中，方法参数ch指向了类中ch指向的数组，ch[0] = 'g';语句改变了类中ch指向的数组的内容