package fangT;

import java.io.InputStream;
/* 2021.1.3 这个和request两个是当时想改成http的, 但是改了半天没搞懂应该怎么收怎么发. 格式太复杂了 . 
 * 想了想算了, 还是先tcp交了作业再说, http以后再探索. 
 * Request类表示一个封装的HTTP请求，可以传递InputStream对象来创建Request对象，
 * 可以调用InputStream对象中的read()方法来读取HTTP请求的原始数据。
源码中的parse()方法用于解析Http请求的原始数据，parse()方法会调用私有方法parseUrI()来解析HTTP请求的URI
除此之外，并没有做太多的工作，parseUri()方法将URI存储在变量uri中，调用公共方法getUri()会返回请求的uri。
*/
public class Request implements FangTangConstants{
    private InputStream input;
    //截取uri,如http://localhost:8080/index.html ，截取部分为 /index.html
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    //从套接字中读取字符信息
    public String parse() {
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
        return request.toString();
    }

    //截取请求的url 截取index.html
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}