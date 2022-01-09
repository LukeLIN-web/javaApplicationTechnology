package server;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;

public class Response {
	private static final int BUFFER_SIZE = 1024;
	OutputStream output;
	int errcode = 0;
	File outputcsv;
	Request request;
	Set<String> operations;
	String conju;
	ArrayList<String> allQueries;

	public Response(OutputStream output, Request req) throws IOException {
		this.output = output;
		this.request = req;
//		outputcsv = new File(HttpServer.WEB_ROOT + "\\output.csv");
		outputcsv = File.createTempFile("tmp-", ".csv");
		outputcsv.deleteOnExit();
		allQueries = new ArrayList<String>();
		operations = new HashSet<String>();
		operations.add("==");
		operations.add("!=");
		operations.add("$=");
		operations.add("&=");
	}

	class lineNumString implements Comparable {
		private String str;
		private int lineNum;

		public lineNumString(String s, int num) {
			this.lineNum = num;
			this.str = s;
		}

		@Override
		public int compareTo(Object obj) {
			lineNumString s = (lineNumString) obj;
			if (this.lineNum < s.lineNum) {
				return -1;
			} else if (this.lineNum > s.lineNum) {
				return 1;
			} else {
				return 0;
			}

		}
	}

	private void findResult() throws IOException {
		// search each.
		File file = new File(HttpServer.WEB_ROOT + "\\test.csv");
		String line = "";
		String csvSplit = ",";
		String[] linetmp = new String[0];
		if (file.exists()) {
			System.out.println("searching csv...");
			BufferedReader br = new BufferedReader(new FileReader(file));
			FileOutputStream fos = new FileOutputStream(outputcsv);
			String headerLine = br.readLine();
			String[] colName = headerLine.split(csvSplit);
			for (String col : colName) {
				fos.write(col.getBytes());
				fos.write(",".getBytes());
			}
			fos.write("\r\n".getBytes());
			Set<String> colSet = new HashSet<String>(Arrays.asList(colName));
			TreeSet<lineNumString> res = new TreeSet<lineNumString>();
			if (conju == "or") {
				for (String q : allQueries) {
					String[] query = q.split(" ");
					if (query.length != 3 || operations.contains(query[1]) == false) {
						System.out.println("queryFormError...");
						errcode = 3;
						return;
					}
					if (colSet.contains(query[0]) == false && query[0] != "*") {
						System.out.println("column name error!");
						errcode = 4;
						return;
					}
					query[2] = query[2].replace("\"", "");
					String condition = query[1];
					int lineNum = 1;
					int position = -1; // default is "*"
					if (query[0] != "*") {
						position = Arrays.binarySearch(colName, query[0]);
					}
					while ((line = br.readLine()) != null) {
						linetmp = line.split(csvSplit);
						if (position == -1) {
							for (String data : linetmp) {
								if (condition.equals("==") && data.equals(query[2])) {
									res.add(new lineNumString(line, lineNum));
									break;
								} else if (condition.equals("&=") && data.contains(query[2])) {
									res.add(new lineNumString(line, lineNum));
									break;
								} else if (condition.equals("!=") && !data.equals(query[2])) {
									res.add(new lineNumString(line, lineNum));
									break;
								} else if (condition.equals("$=")
										&& data.toLowerCase().equals(query[2].toLowerCase())) {
									res.add(new lineNumString(line, lineNum));
									break;
								}
							}
						} else {
							if (condition.equals("==") && linetmp[position].equals(query[2])) {
								res.add(new lineNumString(line, lineNum));
							} else if (condition.equals("&=") && linetmp[position].contains(query[2])) {
								res.add(new lineNumString(line, lineNum));
							} else if (condition.equals("!=") && !linetmp[position].equals(query[2])) {
								res.add(new lineNumString(line, lineNum));
							} else if (condition.equals("$=")
									&& linetmp[position].toLowerCase().equals(query[2].toLowerCase())) {
								res.add(new lineNumString(line, lineNum));
							}
						}
						lineNum++;
					}
				}
			} else {
				int lineNum = 1;
				// conjunctions are all "and"
				while ((line = br.readLine()) != null) {
					linetmp = line.split(csvSplit);
					boolean flag = true;
					for (String q : allQueries) {
						String[] query = q.split(" ");
						if (query.length != 3 || operations.contains(query[1]) == false) {
							System.out.println("queryFormError...");
							errcode = 3;
							return;
						}
						if (colSet.contains(query[0]) == false && !query[0].equals("*")) {
							System.out.println("column name error!");
							errcode = 4;
							return;
						}
						query[2] = query[2].replace("\"", "");
						String condition = query[1];
						int position = -1; // default is "*"
						if (query[0] != "*") {
							position = Arrays.binarySearch(colName, query[0]);
						}
						if (position == -1) {
							boolean any = false;
							for (String data : linetmp) {
								if (condition.equals("==") && data.equals(query[2])) {
									any = true;
								} else if (condition.equals("&=") && data.contains(query[2])) {
									any = true;
								} else if (condition.equals("!=") && data.equals(query[2])) {
									flag = false;
								} else if (condition.equals("$=")
										&& data.toLowerCase().equals(query[2].toLowerCase())) {
									any = true ;
								}
							}
							if (any == false && !condition.equals("!=") ) flag = false;
						} else {
							if (condition.equals("==") && !linetmp[position].equals(query[2])) {
								flag = false;
							} else if (condition.equals("&=") && !linetmp[position].contains(query[2])) {
								flag = false;
							} else if (condition.equals("!=") && linetmp[position].equals(query[2])) {
								flag = false;
							} else if (condition.equals("$=")
									&& !linetmp[position].toLowerCase().equals(query[2].toLowerCase())) {
								flag = false;
							}
						}
					}
					if (flag == true) {
						res.add(new lineNumString(line, lineNum));
					}
					lineNum++;
				}
			}
			for (lineNumString lns : res) {
				System.out.println(lns.str);
				fos.write(lns.str.getBytes());
				fos.write("\r\n".getBytes());
			}
			fos.write("\r\n".getBytes());
		} else {
			errcode = -1;
			System.out.println("Don't have data!");
		}
	}

	private void processquery() throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		// process query and write into file
		String querystr = request.getUri();
		System.out.println(querystr);
		// split "and" "or"
		String[] temp1;
		if (querystr.indexOf("and") == -1) {
			if (querystr.indexOf("or") == -1) {
				allQueries.add(querystr.trim());
			} else {
				conju = "or";
				temp1 = querystr.split("or"); // 分割字符串
				for (String s : temp1)
					allQueries.add(s.trim());
			}
		} else {
			if (querystr.indexOf("or") == -1) {
				conju = "and";
				temp1 = querystr.split("and"); // 分割字符串
				for (String s : temp1)
					allQueries.add(s.trim());
			} else {
				errcode = 2;
			}
		}
		System.out.println(allQueries);
	}

	// send scv to browser
	public void sendResult() throws IOException {
		String querystr = request.getUri();
		if (querystr == "URLerror" || querystr == "queryFormError") {
			String content = "<h1>" + querystr
					+ "</h1>\r\n<h2>correct sample :127.0.0.1:9527/?query=&lt;C1 ==\"A\"orC2&=\"B\"&gt;</h2>";
			int totallength = content.length();
			String errMsg = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: "
					+ totallength + "\r\n" + "\r\n" + content;
			output.write(errMsg.getBytes());
			return;
		}
		processquery();
		if (errcode == 0)
			findResult();
		byte[] buffer = new byte[BUFFER_SIZE];
		FileInputStream fis = new FileInputStream(outputcsv);
		try {
			int readLength;
			if (errcode == 0) {
//				output.write("HTTP/1.1 200 OK\n".getBytes());
//				output.write("Content-Type: text/csv; charset=UTF-8\n\n".getBytes()); // http header
//				while ((readLength = fis.read(buffer, 0, BUFFER_SIZE)) > 0) {
//					output.write(buffer, 0, readLength);
//				}
			}
			if (errcode == -1) {
				String errMsg = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
						+ "Content-Length: 23\r\n" + "\r\n" + "<h1>Data File Not Found</h1>";
				output.write(errMsg.getBytes());
			}
			if (errcode == 4) {
				String content = "<h1>Property does not exist, You can find available Property in csv file</h1>\r\n";
				int totallength = content.length();
				String errMsg = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: "
						+ totallength + "\r\n" + "\r\n" + content;
				output.write(errMsg.getBytes());
				output.write("HTTP/1.1 200 OK\n".getBytes());
				output.write("Content-Type: text/csv; charset=UTF-8\n\n".getBytes()); // http header
				while ((readLength = fis.read(buffer, 0, BUFFER_SIZE)) > 0) {
					output.write(buffer, 0, readLength);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}
}