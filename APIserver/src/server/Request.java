package server;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/* The Request class represents an encapsulated HTTP request. 
 * You can pass an InputStream object to create a Request object. * 
 * You can call the read() method in the InputStream object to read the original data of the HTTP request.
The parse() method in the above source code is used to parse the raw data of the Http request. 
The parse() method will call the private method parseUrI() to parse the URI of the HTTP request. 
In addition, it does not do much work, parseUri()  The method stores the URI in the variable uri, 
and calling the public method getUri() returns the requested uri.
*/
public class Request {
	private final static int BUFFER_SIZE = 1024;
	private InputStream input;

	// find query , such as “127.0.0.1:9527/index.html” ，we find /index.html
	private String uri;

	public Request(InputStream input) {
		this.input = input;
	}

	// Read character information from socket
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
		for (int i = 0; i < readLength; i++) {
			request.append((char) buffer[i]);
		}
		System.out.print(request.toString());
		uri = parseUri(request.toString());
	}

	// extract URL
	private String parseUri(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1) {
				String s = requestString.substring(index1 + 1, index2);
				try {
					s = URLDecoder.decode(s, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				int left = s.indexOf("<");
				int right = s.indexOf(">");
				System.out.println(s);
				if (left == -1 || right == -1) {
					System.out.println("queryFormError");
					return "queryFormError";
				}
				String query = s.substring(left + 1, right);
				return query;
			} else {
				return "URLerror";
			}
		}
		return "URLerror";
	}

	public String getUri() {
		return uri;
	}
}