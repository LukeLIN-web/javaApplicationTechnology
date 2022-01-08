import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	String start  = in.nextLine();
    	String end = in.nextLine();
    	//Set<Integer> huiwen  = new HashSet<Integer>();
    	int s = Integer.parseInt(start);
    	int e =  Integer.parseInt(end);// 三次方想不到啊 !!
    	//找到所有三次方可能在范围中的数.
    	if(huiwen("9"))
    //		System.out.println("111");
    	System.out.print("[");
    	int count = -1 ;
    	for(int i = 1 ; i <= Math.sqrt(e) ;i++ ) {
    		if(huiwen(String.valueOf(i) ) ) {
    			//huiwen.add(i);
    			int j = i*i*i;
    			if( j<= e && j >= s && huiwen(String.valueOf(j))) { 
    				count++;
    				if(count == 0)
    					System.out.print(j);// 到底怎么输出啊!!
    				else 
    					System.out.print(","+j);
    			}
    		}
    	}
    	System.out.print("]");
    	in.close();
    	//        Scanner in = new Scanner(System.in);
//        Vector<Integer> id  = new Vector<Integer>();//不能用int
//        Vector<String> old  = new Vector<String>();//不能用int
//        String line1  = in.nextLine();
//        for(int i = 0 ; i < line1.length(); ) {
//        	String c = line1.substring(i,i+1);
//        	id.add( Integer.parseInt(c));
        }
    	private static boolean huiwen(String str){
        if(str.isEmpty()) return false;
        int slow = 0,fast = str.length();
        //if first = last
        while(slow <= fast){
           // System.out.println(str.indexOf(slow)); 
            if(str.charAt(slow) == str.charAt(fast)){
                slow++;
                fast --;
            }
            else{
                return false;
            }
        }
        return true;
    }
        
}

