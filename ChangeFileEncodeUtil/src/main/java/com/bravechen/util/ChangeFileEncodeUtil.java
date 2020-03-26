package com.bravechen.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import com.bravechen.util.FileEncodeUtil;

/**
 * 改变文件编码工具
 * 
 * @author bravechen
 */
public class ChangeFileEncodeUtil {

	/**
	 * 改变文件编码为charsetName编码
	 * @param file
	 * @param charsetName
	 * @param newFile
	 * @throws IOException
	 */
	public static void change(File file, String charsetName, File newFile) throws IOException {
		Charset.forName(charsetName);
		if (file == null) {
			throw new IllegalArgumentException("文件对象不能为空！");
		}
		String encode = FileEncodeUtil.getEncode(file);
		if (!encode.equals(charsetName)) {
			String readFileToString = FileUtils.readFileToString(file, encode);
			
			if(newFile == null) {
				FileUtils.writeStringToFile(file, readFileToString, charsetName);
			}else {
				FileUtils.writeStringToFile(newFile, readFileToString, charsetName);
			}
		}
	}

	/**
	 * 改变文件编码为charsetName编码
	 * @param file
	 * @param charsetName
	 * @throws IOException
	 */
	public static void change(File file, String charsetName) throws IOException {
		change(file, charsetName, null);
	}

	/**
	 * 改变文件编码为UTF-8编码
	 * @param file
	 * @param newFile
	 * @throws IOException
	 */
	public static void change2UTF8(File file, File newFile) throws IOException {
		change(file, "UTF-8", newFile);
	}
	
	/**
	 * 改变文件编码为UTF-8编码
	 * @param file
	 * @throws IOException
	 */
	public static void change2UTF8(File file) throws IOException {
		change(file, "UTF-8", null);
	}
	
	public static void main(String[] args) throws Exception {
		File path = new File("D:\\workspaces\\common\\CommonUtils\\src");
		for(File file : FileUtils.listFiles(path, null, true)) {
			change2UTF8(file);
		}
	}
}
