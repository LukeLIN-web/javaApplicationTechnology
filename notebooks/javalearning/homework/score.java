import java.util.Scanner;

public class score {
	
		public static void main(String args[]) {
			Scanner scanner =new Scanner(System.in); // create scanner object
			System.out.print("input score");
			int assem=0,Archi=0,logic=0,embed = 0,organ=0,sum=0;
			organ = scanner.nextInt();
			Archi = scanner.nextInt();
			logic = scanner.nextInt();
			assem = scanner.nextInt();
			embed = scanner.nextInt();
		
			sum = (int) (0.8*embed + 0.9*assem+logic +Archi+organ);  //0.8 is double
			if(sum<0.6*organ) {
				sum = (int) (0.6*organ);
			}
			if(logic<60||Archi<60||organ<60) {
				sum = 0;
			}
			
			System.out.println(sum/5);
		}
		
	
}
