package sjtudemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ProcessFile {
	public String process(File infile,String msg) {
		
		return infile.toString() +msg;
	}
}
