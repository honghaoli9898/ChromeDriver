package com.tl.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	public static List<String> getAllFileName(String path) {
		List<String> fileNameList = new ArrayList<String>();
		File file = new File(path);
		if(file.isFile()){
			fileNameList.add(file.toString());
		}
		if(file.isDirectory()){
			File[] fileArray = file.listFiles();
			for (File file2 : fileArray) {
				List<String> fileNameList1 =getAllFileName(file2.toString());
				fileNameList.addAll(fileNameList1);
			}
		}
		return fileNameList;
	}
	public static void main(String[] args) {
		System.out.println(getAllFileName("plugins/images"));
	}
}
