import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * java实现爬虫
 */
public class Search {
	public static void spiderURL(String url,String regex,String filename){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
		String time=sdf.format(new Date());
		URL realURL=null;
		URLConnection connection=null;
		BufferedReader br=null;
		PrintWriter pw=null;
		PrintWriter pw1=null;
		PrintWriter pw2=null;
		int i ,j ;
		Pattern pattern=Pattern.compile(regex);
		try{
			realURL=new URL(url);
			connection=realURL.openConnection();
			//connection.connect();
 
			File fileDir = new File("C:\\Users\\Administrator\\Downloads/"+time);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			//将爬取到的内容放到E盘相应目录下   
			pw = new PrintWriter(new FileWriter("C:\\Users\\Administrator\\Downloads/"+time+"/"+filename+"_content.txt"),true);
			pw1 = new PrintWriter(new FileWriter("C:\\Users\\Administrator\\Downloads/"+time+"/"+filename+"_URL.txt"),true);
			pw2 = new PrintWriter(new FileWriter("C:\\Users\\Administrator\\Downloads/"+time+"/"+filename+"_URL.txt"),true);
			i = j = 0;
			br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			String buf ;
			while((line=br.readLine())!=null){
				buf = new String(line.getBytes(),"utf-8") ;
			    i = buf.indexOf("<",j)
				pw.println(line);
				Matcher matcher=pattern.matcher(line);
				while(matcher.find()){
					pw1.println(matcher.group());
				}
				
			}
			System.out.println("爬取成功！");
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
    public static void main(String[] args) {
    	String url="https://blog.csdn.net/weixin_38409425/article/details/78616688";
    	String regex= "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+";
    	spiderURL(url,regex,"8btc");
    }
}
	
	/*
	
	private static void bolgBody( ) throws IOException{
		Document doc = Jsoup.connect("").get();//.NoClassDefFoundError: org/jsoup/Jsoup
		String title = doc.title();
	    System.out.println(title);
	}
	
	  */ 
