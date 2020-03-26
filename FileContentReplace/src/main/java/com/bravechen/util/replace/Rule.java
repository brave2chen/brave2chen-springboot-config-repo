package com.bravechen.util.replace;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	private String filenameMatchPattern;
	private List<String[]> rules;
	private List<String> exclusionFileNamePattern;

	/**
	 * @param filenameMatchPattern
	 *            文件名正则匹配
	 */
	public Rule(String filenameMatchPattern) {
		this.filenameMatchPattern = filenameMatchPattern;
		this.rules = new ArrayList<String[]>();
		this.exclusionFileNamePattern = new ArrayList<String>();
	}

	/**
	 * @param pattern
	 *            文件内容正则匹配
	 * @param content
	 *            匹配内容替换为content
	 */
	public void addRule(String pattern, String content) {
		if (pattern != null && content != null) {
			this.rules.add(new String[] { pattern, content });
		}
	}

	/**
	 * @param pattern
	 *            文件内容正则匹配,行内匹配
	 * @param content
	 *            匹配内容替换为content
	 * @param exclusionPattern
	 *            文件内容匹配的不替换，行内匹配
	 */
	public void addRule(String pattern, String content, String exclusionPattern) {
		if (pattern != null && content != null) {
			this.rules.add(new String[] { pattern, content, exclusionPattern });
		}
	}

	/**
	 * @param pattern
	 *            文件名匹配的不进行内容替换
	 */
	protected void addExclusionFilePattern(String pattern) {
		this.exclusionFileNamePattern.add(pattern);
	}

	protected String getFilenameMatchPattern() {
		return filenameMatchPattern;
	}

	protected List<String[]> getRules() {
		return rules;
	}

	protected List<String> getExclusionFileNamePattern() {
		return exclusionFileNamePattern;
	}

}