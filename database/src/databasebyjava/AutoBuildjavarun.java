package databasebyjava;

	import javax.tools.JavaCompiler;
	import javax.tools.ToolProvider;
	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.lang.reflect.Method;
	import java.net.URI;
	import java.net.URISyntaxException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.sql.*;
	
	/*
	 * automatically generated class,  
	 *  building time: 2020.12.17-12.20, 
	 * manual: you just change the string variable "table", it will  generate the .class and .java from the database . 
	 * then  you can use in your program. 
	 */
	/*java 动态编译。将文本写到文件，用Java的类加载动态运行。
	 * 1.使用jdk自带rt.jar中的javax.tools包提供的编译器（也可以用runtime运行cmd）进行编译java源文件。
	2.重写类加载器达到加载指定文件夹下的类。 */
public class  AutoBuildjavarun {
	    public static void main(String[] args)  {
	    	String table = "people" ; // change the table to change what table you will create 
	        String code = getTable(table);
	        try {
	        	compile(code,table);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private static String getTable(String table) {
	    	String code = "package databasebyjava;\n import java.sql.*;\n";
	    	try{
		    	String url = "jdbc:derby:Derby_data\\\\dedb";
		    	String username = "db_user1";
			    String password = "111111";
		    	Connection conn = DriverManager.getConnection(url,username,password);
			    Statement s = conn.createStatement();
			    ResultSet rs  = s.executeQuery(" select * from "+ table) ;
			    ResultSetMetaData mta = rs.getMetaData();
			    
			    int col = mta.getColumnCount();
		        code += "import java.util.Vector;";code+="\r\n"; // for newline
		        code += "public class ";  
		        code += table;
		        code += " {  \n";
		        code = code +  "	Statement s = null;\n	String username = \"db_user1\";\n" + 
		        		"	String password = \"111111\";\n";
		        code +=	"	String url = \"jdbc:derby:Derby_data\\\\\\\\dedb\";\n";
		        //from metadata extract variable name.  such as int id;
			    for(int i =1 ; i<= col; i ++) {
			        	if(mta.getColumnType(i) == 4) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
			        		code += "	int\t";
			        		code += mta.getColumnName(i);  code += ";\n";
			        	}
			        	if(mta.getColumnType(i) == 12) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
			        		code += "	String\t";
			        		code += mta.getColumnName(i);  code += ";\n";  
			        	}// define VAR   Varchar: 12 
			    }
			 	code = code +"	"+table +"(){}\n"; //constructor 
			 	
			 	
	        	//******************************** parameter  constructor ********************************
	        	code = code + "	"+ table+"(int id){\n";
	        	code += "		Init();\n";
	        	code += "		try{\n";
	        	code = code+ "		ResultSet rs  = s.executeQuery(\"select * from " +table +"\");  \r\n";
	        	code += "		while( rs.next() ) { \r\n";
	        	// such as  id = rs.getInt("id"); 
	        	for(int i =1 ; i<= col; i ++) {
	        		
			        	if(mta.getColumnType(i) == 4) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
			        		code = code +"	"+ mta.getColumnName(i) + "= rs.getInt(" + i + ");\n";
			        	}
			        	if(mta.getColumnType(i) == 12) { // Integer: 12 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
			        		code = code +"	"+ mta.getColumnName(i) + " = rs.getString(" + i + ");\n";
			        	}// define VAR   Varchar: 12 
			    }
	        	code += "		}\n";
	        	code += "	}catch (Exception e) {\r\n";
	        	code += "		e.printStackTrace();}\n" ;
	        	code +=	"	}\n";
	        	
	        	// ********************************Init method of class********************************
	        	code +=	"	private void Init() {\n";
	        	
	        	code +=	"	try {\n";
	        	code +=	"	Connection conn = DriverManager.getConnection(url,username,password);\n";
	        	code += "	s = conn.createStatement();\n";
	        	code = code + "		} catch (SQLException e) { \n" + 
	        			"		e.printStackTrace();\n" + 
	        			"		}\n" + 
	        			"	}\n" ;// Init() method finished
	        
	        	// ********************************toString method of class********************************
	        	code += "     public String toString() {\n";
	        	code += "return  ";  
	        	for(int i = 1 ; i< col; i ++) 
		        	code = code + mta.getColumnName(i) + "+";
	        	code = code + mta.getColumnName(col) +" ;";
	        	code = code + "\t}\n";
	        	
	        	
	        	//******************************** ADD  method  of class ********************************
	        	code = code + "public void add(";
	    	    for(int i =1 ; i<= col; i ++) {
		        	if(mta.getColumnType(i) == 4) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
		        		code += "	int\t";
		        		code += mta.getColumnName(i);  
		        	}
		        	if(mta.getColumnType(i) == 12) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
		        		code += "	String\t";
		        		code += mta.getColumnName(i);  
		        	}// define VAR   Varchar: 12 
		        	if(i < col)code += ",";  
		    }
	    	    code += ")\n{";
	        	code += "	String sql = \"insert into "+ table +" (" ;
	        	 for(int i =1 ; i<= col; i ++) {
			        	if(mta.getColumnType(i) == 4) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
			        	
			        		code += "\\\"" + mta.getColumnName(i);  
			        	}
			        	if(mta.getColumnType(i) == 12) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
			        		
			        		code += "\\\"" +  mta.getColumnName(i);  
			        	}// define VAR   Varchar: 12 
			        	if(i < col)code += "\\\",";  
			    }
	        			code +="\\\"  ) values (\"+";
	        			//the format is String sql = "insert into people (\"ID\" ) values (" + String.valueOf(id) + ") "; 
	        		for(int i =1 ; i<= col; i ++) {
	    		        	if(mta.getColumnType(i) == 4) { // Integer: 4 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
	    		        		code += "String.valueOf(";
	    		        		code += mta.getColumnName(i) +")"; 
	    		        	}
	    		        	if(mta.getColumnType(i) == 12) { // string :12 you can find inhttp://www.openoffice.org/api/docs/common/ref/com/sun/star/sdbc/DataType.html
	    		        		
	    		        		code += mta.getColumnName(i);
	    		        	}// define VAR   Varchar: 12 
	    		        	code += "+";  
	    		        	if(i < col)code += "\",\"+";  
	    		    }
	        		code +="  \") \" ; ";
	        		/*
	        		 *  method usage :  usr.add("'uiwe'","\'ljy\'",21,"'hagnzhou'");  or 	p1.add(19,"'weiu'",20,"'shier'") 
	        		 *  don't ignore the '' when you input string.
	        		 */
	        		
	        	
	        	code +=  
	        			"	\n	System.out.print(sql);\n" ;
	        	code += "		try{\n";
	        	code = code+ "		Connection conn = DriverManager.getConnection(url,username,password);\n" + 
	        			"			 Statement s  = conn.createStatement();\n" + 
	        			"	s.executeUpdate(sql);	} catch (SQLException e) { \n" + 
	        			"		e.printStackTrace();";
	        	code += "\n" + 
	        			"		}";
	        	code +=	"	\n}\n";
	        	
	        	//******************************** delete method  of class********************************
	        	code = code + "public void delete(int id){\n";
	        	code += "String sql = \"DELETE FROM people WHERE id = \" +  String.valueOf(id); \n";
	        	
	        	code +=  "System.out.print(sql);" + 
	        			"\n" + 
	        			"		try {" ;
	        	code += "\n" + 
	        			"		Connection conn = DriverManager.getConnection(url,username,password);\n" + 
	        			"		Statement s  = conn.createStatement();\n" + 
	        			"	int line = s.executeUpdate(sql);\nif(line > 0)\n" + 
	        			"			 System.out.print(\" delete  success! change \"+line+\" lines\");\n" + 
	        			"			else \n" + 
	        			"				System.out.print(\"\\n don't delete anything! change \"+line+\" lines\");\n" + 
	        			"			} catch (SQLException e) { \n" + 
	        			"		e.printStackTrace();\n" + 
	        			"		}\n" ; 
	        	code +=	"	\n}\n";
	 
	        	code +=	"\n}\n";//  class all finished
	    	}
	    	catch (Exception e) {
	    		 e.printStackTrace();
			}
	        return code;
	    }
	    
	    //先编译,返回文件
	     synchronized static void compile(String code,String table) throws Exception {
	    	File file = new File(Constants.BASEDIR+"/"+table+".java"); //选择文件夹
	    	if(file.exists()){
	             System.out.println("java文件已经存在！");
	        }
	       // 获得类名
	        String classname = table; 
	        // 将代码输出到文件
	        PrintWriter out = new PrintWriter(new FileWriter(file),true); //FileWriter true是追加
	        out.println(code);
	        out.close();
	        
	        // 编译生成的java文件
	        System.out.println("usr dir = "+System.getProperty("user.dir"));
	        //动态编译
	        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
	        int status = javac.run(null, null, null, "-d", Constants.BASEDIR,Constants.BASEDIR+"/"+table+".java");
	        if (status != 0) {
	            throw new Exception("systax error!");
	        }
	        System.out.println("compile success! The classname is "+classname);
	    }
}

	//常量
interface Constants{
	    String BASEDIR=".\\AutoBuildClassRoot";// project当前目录下的文件夹.
	    String SUFFIX=".class";//后缀
}

