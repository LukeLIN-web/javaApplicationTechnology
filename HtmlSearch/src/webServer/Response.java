package webServer;
import java.io.*;

/*  Response对象是在HttpServer类的await()方法中通过传入套接字中获取的OutputStream来创建。
Response类有两个公共方法：setRequest()和sendStaticResource()
*/
public class Response {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }
    
 //   setRequest()方法会接收一个Request对象为参数，
    public void setRequest(Request request) {
        this.request = request;
    }
    // for jsp use .
    public class test {
        public String sd(){
            return "sd";
        }
    }
//sendStaticResource()方法用于发送一个静态资源到浏览器，如Html文件。
    
    public void sendStaticResource() throws IOException{
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {            
            File file = new File(
                    HttpServer.WEB_ROOT, request.getUri());
            if(file.exists()) {
            	 String filename = file.getName();
                 String extname = filename.substring(filename.lastIndexOf(".")+1);
                 System.out.println("file extended name = "+extname);
                 fis = new FileInputStream(file);
            	switch(extname){
	            	case "txt":
	            	case "java":
	            	case "htm":
	            	case "html":
	            	case "jsp":
		            	System.out.println("file extended name = "+extname);
		            	System.out.println("原样文本传送");
		                output.write("HTTP/1.1 200 OK\n".getBytes());
		                output.write("Content-Type: text/html; charset=UTF-8\n\n".getBytes()); //http 头 
		                
		                int readLength;
		                while((readLength = fis.read(buffer, 0, BUFFER_SIZE)) > 0 ) {
		                	output.write("<pre>".getBytes());
		                    output.write(buffer, 0, readLength);
		                	output.write("</pre>".getBytes()); // Guarantee the present correctly 
		                }
		                break;
	            	case "jzup":			//jzup is a custom extended name 
	            		String line = null;
	            		String code = " ";
	            		BufferedReader br = new BufferedReader(new FileReader(file)); // bufferReader的构造方法可以接收InputStreamReader作为参数
	            		while( ( line = br.readLine() ) != null ){
		       			    code += line;
	            		}
	            		// read the code
	            		String filejavaname = code.substring(1, 4)+ "_jzup";
	            		File filejava = new File(Constants.BASEDIR,"javarun"+"/"+filejavaname+"_jzup.java"); 
	          	        if(filejava.exists()) {
	          	        	Tranjavarun.run(filejavaname); //运行class文件
	          	        }
	          	        else {
	          	        	//先编译
	          	        	Tranjavarun.compile(code);
		            		Tranjavarun.run(filejavaname);
	          	        }
	            	default: // serialized them and send 
	            		System.out.println("传送字节流");
            	}
            }
            else {
                String errMsg = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                        + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
                output.write(errMsg.getBytes());
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(fis != null) {
                fis.close();
            }
        }
    }
}