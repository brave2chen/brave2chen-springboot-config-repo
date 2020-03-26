package com.bravechen.util.replace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.bravechen.util.FileEncodeUtil;

/**
 * 1、指定目录 2、正则匹配文件名 3、正则匹配文件内容，并替换
 */
public class ReplaceFileContentWithPattern {
	/**
	 * 扫描目录
	 */
	private String scan_dir;

	/**
	 * 输出目录
	 */
	private String out_dir;

	/**
	 * 规则
	 */
	private List<Rule> rules;

	/**
	 * @param scan_dir
	 *            扫描目录
	 */
	public ReplaceFileContentWithPattern(String scan_dir) {
		this(scan_dir, null);
	}

	/**
	 * @param scan_dir
	 *            扫描目录
	 * @param out_dir
	 *            输出目录
	 */
	public ReplaceFileContentWithPattern(String scan_dir, String out_dir) {
		this.scan_dir = new File(scan_dir).getAbsolutePath().replace("\\", "/");
		if(out_dir != null) {
			this.out_dir = new File(out_dir).getAbsolutePath().replace("\\", "/");
		}
		this.rules = new ArrayList<Rule>();
	}
	
	/**
	 * 执行替换操作
	 */
	public void execute() {
		for(File file : FileUtils.listFiles(new File(this.scan_dir), null, true)) {
			List<Rule> rules = getFileRules(file);
			if (!rules.isEmpty()) {
				replaceWithRules(file, rules);
			}
		}
	}

	/**
	 * 添加替换规则
	 * 
	 * @param rule
	 */
	public void addFileRule(Rule rule) {
		this.rules.add(rule);
	}

	private List<Rule> getFileRules(File file) {
		List<Rule> rules = new ArrayList<Rule>();
		String filepath = file.getAbsolutePath().replace("\\", "/");
		
		for (Rule rule : this.rules) {
			String filenameMatchPattern = ".*" + rule.getFilenameMatchPattern() + ".*";
			if (Pattern.matches(filenameMatchPattern, filepath)) {
				boolean exclusion = false;
				for (String pattern : rule.getExclusionFileNamePattern()) {
					if (Pattern.matches(".*" + pattern + ".*", filepath)) {
						exclusion = true;
						break;
					}
				}
				if (!exclusion) {
					rules.add(rule);
				}
			}
		}
		return rules;
	}

	private void replaceWithRules(File file, List<Rule> rules) {
		String absolutePath = file.getAbsolutePath().replace("\\", "/");
		File newFile = new File(absolutePath + ".replace");

		try {
			FileOutputStream out = new FileOutputStream(newFile);
			String encode = FileEncodeUtil.getEncode(file);
			PrintStream p = new PrintStream(out, true, encode);
			List<String> lines = FileUtils.readLines(file, encode);
			for (int i = 0, len = lines.size(); i < len; i++) {
				String line = lines.get(i);
				for (Rule rule : rules) {
					for (String[] matcher : rule.getRules()) {
						String pattern = matcher[0];
						String content = matcher[1];
						String exclusionPattern = null;
						if (matcher.length > 2) {
							exclusionPattern = ".*" + matcher[2] + ".*";
						}
						if (exclusionPattern == null || !Pattern.matches(exclusionPattern, line)) {
							line = line.replaceAll(pattern, content);
						}
					}
				}
				p.print(line);
				if (i != len - 1) {
					p.println();
				}
			}
			p.close();
			out.close();

			if(this.out_dir == null) {
				FileUtils.forceDelete(file);
				FileUtils.copyFile(newFile, new File(absolutePath));
				FileUtils.forceDelete(newFile);
			}else {
				FileUtils.copyFile(newFile, new File(absolutePath.replace(scan_dir, out_dir)));
				FileUtils.forceDelete(newFile);
			}
			System.out.println("已完成:" + absolutePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
