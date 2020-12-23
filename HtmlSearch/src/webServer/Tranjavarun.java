package webServer;
	import javax.tools.JavaCompiler;
	import javax.tools.ToolProvider;
	import java.io.File;
	import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
	import java.io.PrintWriter;
	import java.lang.reflect.Method;
	import java.net.URI;
	import java.net.URISyntaxException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;

	/*java 动态编译。将文本写到文件，用Java的类加载动态运行。
	 * 1.使用jdk自带rt.jar中的javax.tools包提供的编译器（也可以用runtime运行cmd）进行编译java源文件。
	2.重写类加载器达到加载指定文件夹下的类。 */
public class  Tranjavarun {

	    public static void main(String[] args) {
	        int i = 10;
	        String code = "System.out.println(\"Hello World!\"+(13+2*5/3));";
	        code += "for(int i=0;i<" + i + ";i++){";
	        code += " System.out.println(Math.pow(i,2));";
	        code += "}";
	        try {
	        	String classname = compile(code);
	            run(classname);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	         // we just use codes from the .juzp file 
	    }
	    
	    //先编译,返回文件
	     synchronized static String compile(String code) throws Exception {
	    	String filename = code.substring(1, 4)+ "_jzup";
	    	File file = new File(Constants.BASEDIR+"/"+filename+".java"); //选择文件夹
	    	if(file.exists()){
	             System.out.println("java文件已经存在！");
	         }
	       // 获得类名
	        String classname = filename; 
	        // 将代码输出到文件
	        PrintWriter out = new PrintWriter(new FileWriter(file),true); //FileWriter true是追加
	        out.println(getClassCode(code, classname));
	        out.close();
	        
	        // 编译生成的java文件
	        System.out.println("usr dir = "+System.getProperty("user.dir"));
	        //动态编译
	        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
	        int status = javac.run(null, null, null, "-d", Constants.BASEDIR,Constants.BASEDIR+"/"+filename+".java");
	        if (status != 0) {
	            throw new Exception("语法错误！");
	        }
	        return classname;
	    }
	    
	    // don't use 'private' for Response evoke run method
	    static synchronized void run(String classname) throws Exception {
	        
	        new File(Constants.BASEDIR + classname +Constants.SUFFIX)
	                .deleteOnExit();
	        try {
	            MyClassLoader loader = new MyClassLoader();

	            Class cls =  loader.findClass(classname);
	            Method main = cls.getMethod("method", null);
	            main.invoke(cls, null);
	        } catch (Exception se) {
	            se.printStackTrace();
	        }
	    }
	    
	    //将一块代码封装到 method函数中
	    private static String getClassCode(String code, String className) {
	        StringBuffer text = new StringBuffer();
	        text.append("public class " + className + "{\n");//我后缀要加上jzup,有public会报错误: 类 是公共的, 应在名为 的文件中声明,我删除了public.但是会报错cannot access a member of class yst with modifiers "public static"
	        text.append(" public static void method(){\n");
	        text.append(" " + code + "\n");
	        text.append(" }\n");
	        text.append("}");
	        return text.toString();
	    }
	    private static String getBaseFileName(File file) {
	        String fileName = file.getName();
	        if(fileName.contains(".")){
	            return fileName.split("\\.")[0];
	        }else {
	            return fileName;
	        }

	    }
}

	//重写类加载器达到加载指定目录的类
class MyClassLoader extends ClassLoader{

	    @Override
	    protected Class<?> findClass(String name) {

	        String myPath =new  File( Constants.BASEDIR+"/"+ name + Constants.SUFFIX).toURI().toString();
	        System.out.println(myPath);
	        byte[] cLassBytes = null;
	        Path path = null;
	        try {
	            path = Paths.get(new URI(myPath));
	            cLassBytes = Files.readAllBytes(path);
	        } catch (IOException | URISyntaxException e) {
	            e.printStackTrace();
	        }
	        Class clazz = defineClass(name, cLassBytes, 0, cLassBytes.length);
	        return clazz;
	    }
}
	
	//常量
interface Constants{
	    String BASEDIR="D:\\eclipse\\HtmlSearch\\webroot";
	    String SUFFIX=".class";
}

