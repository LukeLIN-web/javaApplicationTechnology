package fangT;
//实现了display方法.
public class Myclient extends FangTclient implements Display{
	public void display() {
		displayIt();//static的方法直接用类名调用就可以了,新建一个对象调用static方法会造成空间浪费,所以编译器会有警告
	}
}
