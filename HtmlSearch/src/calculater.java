import java.util.Scanner;
import java.util.Stack;
/*
 *编程对于可能含有以下运算符的表达式计算结果。

1：+、-、*、/、%：四则运算  %优先级高于 加减 ,等于乘. 已完成

2：>>、<<：移位运算 , <<  优先级低于 加减 ;

3：High、Low：常数的高、低16位 

4：(、)：括号内优先，可嵌套 已完成
 */
public class calculater {
    //计算表达式的值
    public void getExpressionValue(String A){
        char[] arrayA = A.toCharArray();
        Stack<Integer> Value = new Stack<Integer>();      //存放运算数字及表达式计算结果
        Stack<Character> Operator =  new Stack<Character>();   //存放运算符
        for(int i = 0;i < A.length();i++){
            int temp = 0;
            //如果是数字
            if(arrayA[i] >= '0' && arrayA[i] <= '9'){  
                temp = arrayA[i] - '0';
                i = i + 1;
                while(i < A.length() && arrayA[i] >= '0' && arrayA[i] <= '9'){
                    temp = temp * 10 + (arrayA[i] - '0');
                    i++;
                }
                i--;     //对应上面一句i = i+1;因为在for循环中有i++自增操作，若不执行此句，会导致i自增两次
                Value.push(temp);
            }
            //如果是运算符
            else{
                if(Operator.empty()){
                   Operator.push(arrayA[i]);  //如果运算符栈是空的就直接存放操作符.
                }
                else{
                    char temp1 = Operator.pop();   //进栈前，pop出运算符栈中栈顶存放字符
                    int tempA , tempB,result;
                    int judge = comparePriority(temp1,arrayA[i]);  //比较当前运算符与temp1栈顶运算符优先级
                    switch (judge) {
	                    case 1:
	                    {           //当前运算符优先级小于等于栈顶运算符,  先计算栈顶两个数字.
	                        tempA = Value.pop();
	                        tempB = Value.pop();
	                        result = computeNumber(tempB,tempA,temp1);
	                        Value.push(result);
	                        Operator.push(arrayA[i]);
	                        break;
	                    }
	                    case 0:
	                    {           //当前运算符优先级大于栈顶运算符,再放回去看看之后有没有更大的.
	                        Operator.push(temp1);  //pop出来的要push回去.
	                        Operator.push(arrayA[i]); //先计算arrayA[i]
	                        break;
	                    }
	                    case 2 :
	                    {  //字符')'遇到'('  当前字符')' 栈顶字符'('
	                        System.out.println("'('刚好遇到')'");   //这种情况也应该不会出现，按照给定优先级，')'一般会先遇到+、-、*、/字符
	                        break;
	                    }
	                    case 3: 
	                    {   //此时')'刚好准备进栈
	                        while(temp1 != '('){          //')'字符要等到第一个'('出栈才能结束循环
	                            //System.out.println(temp1);
	                            tempA = Value.pop();
	                            tempB = Value.pop();
	                            result = computeNumber(tempB,tempA,temp1);
	                            Value.push(result);
	                            temp1 = Operator.pop();  
	                        }
	                        break;
	                    }
	                    case 4:
	                           tempA = Value.pop();
	                           tempB = Value.pop();
	                           result = tempB<<tempA ;
	                           Value.push(result);
	                           break ; 
	                    case 5: 
	                           tempA = Value.pop();
	                           tempB = Value.pop();
	                           result = tempB>>tempA ;
	                           Value.push(result);
	                           break ; 
	                    default:  
	                    	System.out.println("出现栈顶有')'错误！！！");// if(judge == -1){    //此时，说明当前栈顶字符为')'，这是不存在的，因为遇到')'，按要求不让进栈
	                }
                  }
            }
        }
        
        while(!Operator.empty() && !Value.empty()){   //此时，字符栈中还存在运算符的情况
            char temp1 = Operator.pop();
            char temp2 ;
            int result = 0 ;
            int tempA = Value.pop();
            int tempB = Value.pop();
            switch(temp1){
	            case '<':
	            	temp2 = Operator.pop();
	            	if(temp2 == '<') {
	            	result = tempB<<tempA ;
	            	}
	            	else 
	            		System.out.println("出现移位运算不对应错误！！！");
	            	break;
	            case '>':
	            	temp2 = Operator.pop();
	            	if(temp2 == '>') {
	            	result = tempB>>tempA ;
	            	}
	            	else 
	            		System.out.println("出现移位运算不对应错误！！！");
	            	break;
	            default:
	            	result = computeNumber(tempB,tempA,temp1);
            }
            Value.push(result);
        }
        System.out.println(Value.pop());   //此时运算符栈为空，数字栈中只存在表达式计算最终结果
    }
    //计算a operator b的值，operator = {+,-,*,/,%,<<,}
    public int computeNumber(int a,int b,char operator){
        int result;
        switch(operator){
        case '+': 
            result = a+b;
            break;
        case '-':
            result = a-b;
            break;
        case '*': 
            result = a*b;
            break;
        case '/': 
            result = a/b;
            break;        
        case '%':
        	result = a%b;
        	break;
        default:  
            result = 0;
            break;
        }
        return result;
    }
    //判断运算符a和b的优先级 返回1表示 栈顶字符a优先级大于等于b当前字符.  comparePriority(temp1,arrayA[i]); 
    public int comparePriority(char a,char b){
        //使用二维数组表达运算符之间的优先级，行用字符a表示，列用字符b表示,
        int[][] Value = {
        		{1,1,0,0,0,3,0,1,1},
                {1,1,0,0,0,3,0,1,1},
                {1,1,1,1,0,3,1,1,1},  	// 第七列是 %,第八列是 <,第九列是> 
                {1,1,1,1,0,3,1,1,1},
                {0,0,0,0,0,2,0,1,1},
                {-1,-1,-1,-1,-1,-1,-1,-1,-1},//此时，说明当前栈顶字符为')'，这是不存在的，因为遇到')'，按要求不让进栈
                {1,1,1,1,0,3,1,1,1},   	// %的优先级和乘除一样
                {0,0,0,0,0,3,0,4,-1}, 	// <的优先级比目前所有都低,所以第八列第九列是1 
                {0,0,0,0,0,3,0,-1,5}		// >的优先级比目前所有都低,所以第八列第九列是1  遇到自己要特殊处理
        };
        int i = 0,j = 0;// a= +, b = * 那就是[0][2]   返回0  ,栈顶字符a也就是temp1优先级小于b当前字符arrayA[i]
        switch(a) {
        	case '+' :
        		i = 0;// a= +, b = < 那就是[0][7]   返回1  ,栈顶字符a也就是temp1优先级大于b当前字符arrayA[i]
        		break;
        	case '-' :
        		i = 1;
        		break;
        	case '*' :
        		i = 2;
        		break;
        	case '/' :
        		i = 3;
        		break;
        	case '(' :  // a= (, b = * 那就是[4][2] ,也就是0  栈顶字符a也就是temp1优先级小于b当前字符arrayA[i] ,括号优先级比较高
        		i = 4;
        		break;
        	case ')' :
        		i = 5;
        		break;
        	case '%' :
        		i = 6;
        		break;
        	case '<' :
        		i = 7;// a= <, b = * 那就是[7][2]   返回0    ,栈顶字符a也就是temp1优先级小于b当前字符arrayA[i]
        		break;
        	case '>' :
        		i = 8;// temp  = '>', 当前字符arrayA[i]  都比他大
        		break;
        }
     
        switch(b) {
    	case '+' :
    		j = 0;
    		break;
    	case '-' :
    		j = 1;
    		break;
    	case '*' :
    		j = 2;
    		break;
    	case '/' :
    		j = 3;
    		break;
    	case '(' :
    		j = 4;
    		break;
    	case ')' :
    		j = 5;
    		break;
    	case '%' :
    		j = 6;
    		break;
    	case '<' :
    		i = 7;
    		break;
    	case '>' :
    		i = 8;
    		break;
        }
        return Value[i][j];
    }
    
    public static void main(String[] args){
        calculater test = new calculater(); 
        Scanner in = new Scanner(System.in);
        System.out.println("请输入一个算法表达式：");
        String A = in.nextLine();
        test.getExpressionValue(A);
        in.close();
    }
}