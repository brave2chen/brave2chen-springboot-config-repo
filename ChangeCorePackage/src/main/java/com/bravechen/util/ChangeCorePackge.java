package com.bravechen.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.bravechen.util.replace.ReplaceFileContentWithPattern;
import com.bravechen.util.replace.Rule;

/**
 * 改变代码包路径
 * 
 * @author bravechen
 *
 */
public class ChangeCorePackge {
	private String baseSrcPath;
	private String srcPackge;
	private String srcPath;
	private String destPackge;
	private String destPath;
	private String webSrcPath;

	/**
	 * 
	 * @param baseSrcPath
	 *            代码路径
	 * @param srcPackge
	 *            待替换的包名
	 * @param destPackge
	 *            替换后的包名
	 */
	public ChangeCorePackge(String baseSrcPath, String srcPackge, String destPackge) {
		super();
		this.baseSrcPath = baseSrcPath;
		this.srcPackge = srcPackge;
		this.srcPath = srcPackge.replace(".", "/");
		this.destPackge = destPackge;
		this.destPath = destPackge.replace(".", "/");
	}

	/**
	 * 
	 * @param baseSrcPath
	 *            代码路径
	 * @param srcPackge
	 *            待替换的包名
	 * @param destPackge
	 *            替换后的包名
	 * @param webSrcPath
	 *            要替换的JSP目录路径
	 */
	public ChangeCorePackge(String baseSrcPath, String srcPackge, String destPackge, String webSrcPath) {
		super();
		this.baseSrcPath = baseSrcPath;
		this.srcPackge = srcPackge;
		this.srcPath = srcPackge.replace(".", "/");
		this.destPackge = destPackge;
		this.destPath = destPackge.replace(".", "/");
		this.webSrcPath = webSrcPath;
	}

	public void execute() {
		this.move();
		ReplaceFileContentWithPattern replaceFileContentWithPattern = new ReplaceFileContentWithPattern(
				this.baseSrcPath);
		Rule rule = new Rule(".+\\.java$");
		rule.addRule(this.srcPackge, this.destPackge);
		replaceFileContentWithPattern.addFileRule(rule);
		replaceFileContentWithPattern.execute();

		if (this.webSrcPath != null) {
			replaceFileContentWithPattern = new ReplaceFileContentWithPattern(this.webSrcPath);
			rule = new Rule(".+\\.jsp$");
			rule.addRule(this.srcPackge, this.destPackge);
			replaceFileContentWithPattern.addFileRule(rule);
			replaceFileContentWithPattern.execute();
		}
	}

	private void move() {
		File oldPackge = new File(this.baseSrcPath, this.srcPath);
		File newPackge = new File(this.baseSrcPath, this.destPath);
		try {
			if (oldPackge.exists()) {
				FileUtils.copyDirectory(oldPackge, newPackge);
				FileUtils.deleteDirectory(oldPackge);
			}
			cleanDir(oldPackge);
			System.out.println("packge移动完成" + this.srcPackge + " >> " + this.destPackge);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cleanDir(File oldPackge) {
		String baseSrcPath = new File(this.baseSrcPath).getAbsolutePath();

		while (!oldPackge.getAbsolutePath().equals(baseSrcPath)) {
			if (!oldPackge.exists()) {
				oldPackge = oldPackge.getParentFile();
				continue;
			}
			if (oldPackge.listFiles().length == 0) {
				oldPackge.delete();
				oldPackge = oldPackge.getParentFile();
			} else {
				break;
			}
		}
	}
}
