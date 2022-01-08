import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// 多线程和并行程序设计 30章 demo代码
public class TaskThreadDemo {
	public static void main(String[] args) {
		Vector<String> rs = new Vector<String>();
		
		//ExecutorService executor = Executors.newFixedThreadPool(3);
		//ExecutorService executor = Executors.newFixedThreadPool(1);//会依次执行
		ExecutorService executor = Executors.newCachedThreadPool();// 一个任务可以用thread类,多个任务可以用线程池.
		executor.execute(new PrintChar('a', 100));
		executor.execute(new PrintChar('b', 100));
		executor.execute(new PrintNum(100));
		executor.shutdown();
//		Runnable printA = new PrintChar('a',100);
//		Runnable printB = new PrintChar('b',100);
//		Runnable print100 = new PrintNum(100);// 把任务从线程中分离出来.
//		
//		Thread thread1 = new Thread(printA);
//		Thread thread2 = new Thread(printB);
//		Thread thread3 = new Thread(print100);
//		
//		thread1.start();
//		thread2.start();
//		thread3.setPriority(Thread.MAX_PRIORITY);
//		thread3.start();	
	}
}
class PrintChar implements Runnable{
	private char charToPrint;
	private int times;
	public PrintChar(char c, int t ) {
		charToPrint = c;
		times = t;
	}
	@Override  //jvm 会自动调用这个方法
	public void run() {
		for(int i = 0 ; i < times ; i++) {
			System.out.print(charToPrint);
		}
	}
}

class PrintNum implements Runnable{
	private int lastNum;
	public PrintNum( int n ) {
		lastNum = n;
	}
	@Override
	public void run() {
	//	Thread thread4  = new Thread( new PrintChar('c', 40));
	//	thread4.start();
		try {
			for(int i = 0 ; i <= lastNum ; i++) {
				System.out.print(" "+ i);
				//Thread.yield();//给其他线程让出cpu时间
			//	if( i > 50 ) Thread.sleep(1);//sleep 一定要catch 异常 可能休眠后有interrupt 异常
			//	if( i > 1 ) thread4.join();// 等待thread4结束后再继续打印3-100
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
}

