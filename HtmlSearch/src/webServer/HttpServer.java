package webServer;

import java.io.*;
import java.net.*;

/*应用程序的入口在HttpServer类中，main()方法创建一个HttpServer实例，
 * 然后调用其await()方法，顾名思义，await()方法会在指定端口上等待HTTP请求，对其进行处理，
 * 然后发送响应信息回客户端，在接收到关闭命令前，它会保持等待状态。
该应用程序仅发送位于指定目录的静态资源的请求，如html文件和图像，它也可以将传入到的http请求字节流显示到控制台
 这个类表示一个Web服务器，这个Web服务器可以处理对指定目录的静态资源的请求，
 该目录包括由公有静态变量final WEB_ROOT指明的目录及其所有子目录。
*/

public class HttpServer {
    private static final int SERVER_PORT = 9090;
    public static final String WEB_ROOT = System.getProperty("user.dir")
            + File.separator + "webroot";
    private static final String SHUTDOWN_COMMAND = "/QUIT";

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        System.out.println("服务器开始等待...");
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("服务器启动监听在"+SERVER_PORT+"端口...");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        while(true) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                // 创建Request对象并解析
                Request request = new Request(input);
                request.parse();
                System.out.println("创建Request对象并解析");
                // 检查是否是关闭服务命令
                if (request.getUri().equals(SHUTDOWN_COMMAND)) {
                	System.out.println("关闭服务命令");
                    break;
                }

                // 创建 Response 对象
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();//发送静态 html
                System.out.println("服务器发送静态资源...");
                // 关闭 socket 对象
                System.out.println("关闭 socket 对象");
                socket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}