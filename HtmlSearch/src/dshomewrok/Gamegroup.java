package dshomewrok;

import java.util.Scanner;
import java.util.Vector;

public class Gamegroup {
	private int Find(int x, Vector<Integer> vt) {
		if(vt.elementAt(x) <= -1)
			return x;
		return vt[x] = Find(vt.elementAt(x));
	}
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Vector<Integer> vt = new Vector<Integer>(n,-1);
        Vector<String> svt = new Vector<String>(n);
        Vector<String> wantvt = new Vector<String>(n);
        System.out.println(vt.size());
        //先遍历一次把所有的都存进来.
        for(int i = 0; i < n; i++){
            String a = sc.nextLine();
            String[] ch =  a.split(" ");
            svt.add(ch[0]);
            wantvt.add(ch[1]);
        } 
        //并查集
        for(int i = 0; i < n; i++){
        	
        } 
        
    }
}
