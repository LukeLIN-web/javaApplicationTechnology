package server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static final int SERVER_PORT = 9527;
    public static final String WEB_ROOT = System.getProperty("user.dir")
            + File.separator + "webroot";
    private static final String SHUTDOWN_COMMAND = "/QUIT";

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        System.out.println("web server is awaiting...");
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("server listen at"+SERVER_PORT+"port...");
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

                Request request = new Request(input);
                request.parse();
                System.out.println("URL parsed");
                
                if (request.getUri().equals(SHUTDOWN_COMMAND)) {
                	System.out.println("close server");
                    break;
                }

                Response response = new Response(output,request);
                response.sendResult();// send csv
                System.out.println("sent result...");
      
                socket.close();
                System.out.println("close socket ");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}