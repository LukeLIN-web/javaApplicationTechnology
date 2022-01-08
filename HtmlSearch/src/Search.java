
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
import org.htmlparser.filters.*;
import org.htmlparser.*;
import org.htmlparser.nodes.*;
import org.htmlparser.tags.*;
import org.htmlparser.util.*;
import org.htmlparser.visitors.*;

 * java实现爬虫跟踪特定网页，下载该网页中所有链接的指定内容，去除广告等无关内容，组合成单一文件。主要作广度搜索，深度暂为1。
实现：
1、(基本)不使用第三方工具，自己作String处理。
2、(提高)可用第三方工具，如：HttpClient、HtmlParser等。

 */
public class Search {
	
	//爬取该页的所有html写入Spider_content文件, 正则提取目录链接到Spider_URL文件.
	public static void spiderURL(String url,String regex,String filename){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		URL realURL = null;// 利用url 类 
		URLConnection connection = null;
		BufferedReader br=null;
		PrintWriter pw=null;
		PrintWriter pw1=null;
		int i  ;
		Pattern pattern = Pattern.compile(regex);//正则
		try{
			realURL = new URL(url);
			connection = realURL.openConnection();
			File fileDir = new File("HTMLparaser/"+time);
			if (!fileDir.exists()) 
				fileDir.mkdirs();
			//将爬取到的内容放到当前目录下 , 为什么是当前目录下?   
			pw = new PrintWriter(new FileWriter("HTMLparaser/"+time+"/"+filename+"_content.txt"),true);//他到底写入到哪里?
			pw1 = new PrintWriter(new FileWriter("HTMLparaser/"+time+"/"+filename+"_URL.txt"),true);
			i  = 0;
			br = new BufferedReader(new InputStreamReader(connection.getInputStream())); // bufferReader的构造方法可以接收InputStreamReader作为参数
			String line = null;
			while( ( line = br.readLine() ) != null ){
				 i = line.indexOf("<li><a href=\"http://www.yuedu88.com/longzu1/27429.html\">前言</a></li>"); 
			    if(i >= 0)
			    	System.out.println(line.substring(i));
			    pw.println(line);//写入全部内容
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()){
					pw1.println(matcher.group()); //写入url
				}
			}
			System.out.println("爬取url成功！");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				br.close();
				pw.close();
				pw1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//打开存储有url的文件,每一行读入tempStr,如果有http://www.yuedu88.com/longzu1/的前缀,那么很可能就是需要的url, 那就访问它,把所有内容写入longzu1文件
	//如果不是,那就下一行.
	//每一行都读完了,那就关闭其他文件,关闭网络,处理longzu1
	
	public static void searchThroughUrlFile(String regex,String filename) throws Exception {
		// open the file which contains the url , and open these url one by one ,spider the text.
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());
		URL realURL = null;
		URLConnection connection = null;
		
		PrintWriter pw3=null;
		PrintWriter pw2=null;
		int i ,j = 0 ;
			// Open the file which contains the url, readURL will use these url string.
			 //	File file = new File(filename);
			    BufferedReader reader = null;
			    try {
			    	pw3 = new PrintWriter(new FileWriter("HTMLparaser/"+time+"/"+filename+"_simple.txt"),true);////将爬取到的内容放到C盘相应目录下   
					pw2 = new PrintWriter(new FileWriter("HTMLparaser/"+time+"/"+filename+"_all.txt"),true);//去掉<>里面的内容
			        reader = new BufferedReader(new FileReader("HTMLparaser/"+time+"/"+filename+"_URL.txt"));
			        String tempStr;
			        while ((tempStr = reader.readLine()) != null) {
			        	 	i = tempStr.indexOf("http://www.yuedu88.com/longzu1/"); 
						    if(i >= 0) { //如果是以一段特定字符串开头,那就使用这条URL
						    	System.out.println(tempStr); // 测试
						    	realURL = new URL(tempStr);
								connection = realURL.openConnection();//connection.connect();
								i = j = 0;
								BufferedReader br = null;
								br = new BufferedReader(new InputStreamReader(connection.getInputStream())); // bufferReader的构造方法可以接收InputStreamReader作为参数
						    	try {
						         StringBuffer sb = new StringBuffer();	
						         StringBuffer sbf = new StringBuffer();
						         String inputLine;
						         while ((inputLine = br.readLine()) != null) {
						                 sb.append(inputLine);
						                 sb.append("\n");
						    
						         //那就访问它,把所有内容写入longzu1文件　
						            // my String process 
						         i = inputLine.indexOf("<span class=\"calibre8\">"); 
								    while(i >= 0){
								    	j = inputLine.indexOf("</span>",i); 
								    	sbf.append(inputLine.substring(i+"<span class=\"calibre8\">".length() , j) );
								    	sbf.append("\n");
								    	i = inputLine.indexOf("<span class=\"calibre",j); 
								    }
								    	pw2.println(inputLine);//写入全部内容
						         }
								    	pw3.println(sbf);//写入简化后的文本
										System.out.println("爬取内容成功一篇！");
										// use  htmlparaser to process
								//		String result = sb.toString();
									//	readTextAndLinkAndTitle(result,"Spider");
										//use  htmlparaser to process finished
										br.close();
						    	}catch(Exception e){
									e.printStackTrace();
						    	}
						    }
						    //这条URL使用完毕，下一条URL．
			        }
			        reader.close();	
	}catch (FileNotFoundException e) {  
        e.printStackTrace();  
    } catch (IOException e) {  
        e.printStackTrace();  
    }finally{ //最后关闭
		pw3.close();
		pw2.close();
	}
			    
	}
	
/*  利用htmlparser来解析html文本,然后写入给定的文件中
	
	public static void readTextAndLinkAndTitle(String result,String filename) throws Exception {
		   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		   String time = sdf.format(new Date());
           Parser parser;
           NodeList nodelist;
           parser = Parser.createParser(result, "utf8");
           NodeFilter textFilter = new NodeClassFilter(TextNode.class);
           NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
           NodeFilter titleFilter = new NodeClassFilter(TitleTag.class);
           OrFilter lastFilter = new OrFilter();
           lastFilter.setPredicates(new NodeFilter[] { textFilter, linkFilter, titleFilter });
           nodelist = parser.parse(lastFilter);
           Node[] nodes = nodelist.toNodeArray();
           String line = "";
           
           // Print in the file 
           PrintWriter pw1 = null;
           pw1 = new PrintWriter(new FileWriter("HTMLparaser/"+time+"/"+filename+"_ByParaser.txt",true),true);////将爬取到的内容放到C盘相应目录下   
			
           for (int i = 0; i < nodes.length; i++) {
                   Node node = nodes[i];
                   if (node instanceof TextNode) {
                           TextNode textnode = (TextNode) node;
                           line = textnode.getText();
                   } 
                   /*
                   else if (node instanceof LinkTag) {
                           LinkTag link = (LinkTag) node;
                           line = link.getLink();
                   } else if (node instanceof TitleTag) {
                           TitleTag titlenode = (TitleTag) node;
                           line = titlenode.getTitle();
                   }
                   
              
                   if (isTrimEmpty(line))
                           continue;
                   pw1.println(line);//写入简化后的文本
           }
           pw1.close();
           System.out.println("htmlparser 解析完成");
   }
   */
  
   /* 去掉左右空格后字符串是否为空
    **/
   public static boolean isTrimEmpty(String astr) {
           if ((null == astr) || (astr.length() == 0)) {
                   return true;
           }
           if (isBlank(astr.trim())) {
                   return true;
           }
           return false;
   }
   /** 字符串是否为空:null或者长度为0.
    */
   public static boolean isBlank(String astr) {
           if ((null == astr) || (astr.length() == 0)) {
                   return true;
           } else {
                   return false;
           }
   }
	
    public static void main(String[] args) throws Exception {
    	String url="http://www.yuedu88.com/longzu1/";
    	String regex= "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+"; //http或https开头,一个词, 
    	spiderURL(url,regex,"Spider");
    	searchThroughUrlFile(regex,"Spider");
    	System.out.println("爬取结束!");
    }
}
