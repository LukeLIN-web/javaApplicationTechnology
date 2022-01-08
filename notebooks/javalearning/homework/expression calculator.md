# 表达式计算

编程对于可能含有以下运算符的表达式计算结果。

1：+、-、*、/、%：四则运算  %优先级高于 加减 ,等于乘除. 已完成

2：>>、<<：移位运算 , <<  优先级低于 加减 ;

3：High、Low：常数的高、低16位

4：(、)：括号内优先，可嵌套

*：允许 0x 为前缀的16进制数。

*：本结果可用于WEB网站服务器中，嵌入程序的处理。

测试集

```
1-2*((2+3)*2-(2+3))  = -9
1%2-2*((2+3)*2-(2+3)) = -9
1-2*((2+3)*(2+3)) = -49
1-2+3*(4-5)  = -4
1%2-2+3*(4-5)  = -4
先算术运算，后移位运算，最后位运算。请特别注意：1<<3+2 & 7等价于 (1 << (3 + 2))&7.
```



<< 的 处理方法: 

遇到< ,如果下一个是< ,那就 

如果不是< , 那就报错

```java
	if(arrayA[i] == '<' || arrayA[i] == '>' ) {
                    		Operator.push(arrayA[i]);  //如果< 或 >就直接存放操作符.
                      	}

```

错误1:Exception processing async thread queue 

方法:  只要关掉'expressions'视图就可以了
