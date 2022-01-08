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
			}
			return 1;
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
					q = q.trim();
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
					System.out.println( position +condition);
					if (condition.equals("==")) {
						while ((line = br.readLine()) != null) {
							linetmp = line.split(csvSplit);
							if (position == -1) {
								for (String data : linetmp) {
									if (data.equals(query[2])) {
										res.add(new lineNumString(data, lineNum));
										break;
									}
								}
							} else {
								System.out.println(linetmp[position] + query[2] + lineNum);
								if (linetmp[position].equals(query[2])) {
									System.out.println("res.add(new lineNumString(data, lineNum))");
									res.add(new lineNumString(line, lineNum));
								}
							}
							lineNum++;
						}
					} else if (condition.equals("&=")) {
						// contain substring
						while ((line = br.readLine()) != null) {
							linetmp = line.split(csvSplit);
							if (position == -1) {
								for (String data : linetmp) {
									System.out.println(data + position + lineNum);
									if (data.contains(query[2])) {
										res.add(new lineNumString(line, lineNum));
										break;
									}
								}
							} else {
								if (linetmp[position].contains(query[2])) {
									res.add(new lineNumString(line, lineNum));
								}
							}
							lineNum++;
						}
					} else if (condition.equals("!=")) {
						while ((line = br.readLine()) != null) {
							linetmp = line.split(csvSplit);
							if (position == -1) {
								for (String data : linetmp) {
									System.out.println(data + position + lineNum);
									if (! data.equals(query[2])) {
										res.add(new lineNumString(data, lineNum));
										break;
									}
								}
							} else {
								System.out.println(linetmp[position] + position + lineNum);
								if (! linetmp[position].equals(query[2])) {
									res.add(new lineNumString(line, lineNum));
								}
							}
							lineNum++;
						}
					} else if(condition.equals("$=")){
						// $= not sensitive.
						while ((line = br.readLine()) != null) {
							linetmp = line.split(csvSplit);
							if (position == -1) {
								for (String data : linetmp) {
									System.out.println(data + position + lineNum);
									if (data.toLowerCase().equals(query[2].toLowerCase())) {
										res.add(new lineNumString(data, lineNum));
										break;
									}
								}
							} else {
								System.out.println(linetmp[position] + position + lineNum);
								if ( linetmp[position].toLowerCase().equals(query[2].toLowerCase())) {
									res.add(new lineNumString(line, lineNum));
								}
							}
							lineNum++;
						}
					}
				}
			} else {

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
				allQueries.add(querystr);
			} else {
				conju = "or";
				temp1 = querystr.split("or"); // 分割字符串
				for (String s : temp1)
					allQueries.add(s);
			}
		} else {
			if (querystr.indexOf("or") == -1) {
				conju = "and";
				temp1 = querystr.split("and"); // 分割字符串
				for (String s : temp1)
					allQueries.add(s);
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
				output.write("HTTP/1.1 200 OK\n".getBytes());
				output.write("Content-Type: text/csv; charset=UTF-8\n\n".getBytes()); // http header
				while ((readLength = fis.read(buffer, 0, BUFFER_SIZE)) > 0) {
					output.write(buffer, 0, readLength);
				}
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