package data.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CSVUtil {
	public static List<String[]> readCSV(String path) throws Exception {
		CSVReader rd = new CSVReader(new FileReader(new File(path)));
		List<String[]> res = new ArrayList<>();
		res = rd.readAll();
		rd.close();
		return res;
	}
	public static List<String[]> readCSV(File f) throws Exception {
		CSVReader rd = new CSVReader(new FileReader(f));
		List<String[]> res = new ArrayList<>();
		res = rd.readAll();
		rd.close();
		return res;
	}
	public static void writeCSV(List<String[]> contents,String path) throws Exception {
		CSVWriter wt = new CSVWriter(new FileWriter(new File(path)));
		wt.writeAll(contents);
		wt.close();
		return;
	}
}
