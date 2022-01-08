import java.util.Scanner;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;
public class manjianwenti {

    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	int T = Integer.parseInt(in.nextLine());
    	int y = Integer.parseInt(in.nextLine());
    	System.out.println(T +y);
    	String [] ss = new String[10010];
    	String s = in.nextLine();
    	ss =  s.split(" ");
    	System.out.println(ss[0]);
    	Vector<Integer> vt = new Vector<Integer>() ;//怎么是一个有序数组???
    	PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
    	for(int i = 0 ; i < ss.length; i ++) {
    		pq.add(Integer.parseInt(ss[i]));
    	}
    	System.out.print(pq.peek());
    	//怎么遍历啊!!!遍历一个有序数组
    	while(!pq.isEmpty()) {    		//find closest num
    		vt.add(pq.poll());// 获得一个有序数组
    	}
    	int i = 0 ;
    	for(;i< vt.size() && vt.indexOf(i) < T;i++){    	}
    	//找到最接近的大一点点.
    	int closest;
    	if(i == vt.size()) {
  		  closest  = Integer.MAX_VALUE;// all smaller than T,那就照常处理
    	}	
    	else {
    		closest = vt.elementAt(i);
		}
    	if(closest == T) {
    		System.out.print(T-y);
    		return;
    	}
    	boolean flag = false;
    	for(int j = i-1; j> 0 ; j -- ) {
    		// j一定小于T
    		int temp = vt.elementAt(j);
    		for(int k = j ;k > 0 ; k --) {
    			temp  += vt.elementAt(k)  ;//从最大开始加
    			if(temp >=T) {
    				if(temp  < closest ) {
    					closest = temp;
    				}//更新closest
    				temp -= vt.elementAt(k);
    			}
    		}
    	}
    	System.out.print(T-y);
    	in.close();
}
}
//100
10
47 59 42 54
输出 91
