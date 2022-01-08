package server;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
/* Request类表示一个封装的HTTP请求，可以传递InputStream对象来创建Request对象，
 * 可以调用InputStream对象中的read()方法来读取HTTP请求的原始数据。
上述源码中的parse()方法用于解析Http请求的原始数据，parse()方法会调用私有方法parseUrI()来解析HTTP请求的URI
除此之外，并没有做太多的工作，parseUri()方法将URI存储在变量uri中，调用公共方法getUri()会返回请求的uri。
*/
public class Request {
    private final static int BUFFER_SIZE = 1024;
    private InputStream input;

    //find query , such as  “127.0.0.1:9527/index.html” ，we find /index.html
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    //从套接字中读取字符信息
    public void parse() {
        StringBuffer request = new StringBuffer();
        int readLength;
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            readLength = input.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            readLength = -1;
        }
        for(int i = 0; i < readLength; i++) {
            request.append((char)buffer[i]);
        }
        System.out.print(request.toString());
        uri = parseUri(request.toString());
    }

    // extract URL
    private String parseUri(String requestString){
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
            	String s =  requestString.substring(index1 + 1, index2);
            	try {
					s = URLDecoder.decode(s, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
            	int left = s.indexOf("<");
            	int right = s.indexOf(">");
            	System.out.println(s);
            	if (left == -1 || right == -1  ) {
            		System.out.println("queryFormError");
					return "queryFormError";
				}
            	String query = s.substring(left+1,right);
            	return query;
            }
            else {
            	return "URLerror";
            }
        }
        return "URLerror";
    }

    public String getUri() {
        return uri;
    }
}