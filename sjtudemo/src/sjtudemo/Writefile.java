package sjtudemo;

import java.io.*;

public class Writefile {
	public void name(File infile,String msg) throws IOException {
		File file = new File("result.txt");
		ProcessFile proc = new ProcessFile();
		String res = proc.process(infile,msg);
		System.out.println(file);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		// 如果不存在则创建一个新文件
		System.out.println(file);
		OutputStream outPutStream;
		try {
			outPutStream = new FileOutputStream(file);
            StringBuilder stringBuilder = new StringBuilder();//使用长度可变的字符串对象；
            stringBuilder.append("文件内容");//追加文件内容
            stringBuilder.append(res);
            //TODO 这里写你的代码逻辑;

            String context = stringBuilder.toString();//将可变字符串变为固定长度的字符串，方便下面的转码；
            byte[]  bytes = context.getBytes("UTF-8");//因为中文可能会乱码，这里使用了转码，转成UTF-8；
            outPutStream.write(bytes);//开始写入内容到文件；
            outPutStream.close();//一定要关闭输出流；
			System.out.println(res);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

