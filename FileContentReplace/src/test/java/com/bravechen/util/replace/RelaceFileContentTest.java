package com.bravechen.util.replace;

import java.io.File;

import org.junit.Test;

import junit.framework.Assert;

public class RelaceFileContentTest {

	@Test
	public void test() {
		String scan_dir = this.getClass().getClassLoader().getResource("files").getPath();
		String out_dir = new File(scan_dir).getParent() + "/outfiles";
		ReplaceFileContentWithPattern replaceFileContentWithPattern = new ReplaceFileContentWithPattern(scan_dir, out_dir);
		
		//.txt结尾文件
		Rule rule = new Rule(".*\\.txt$");
		
		//文件名包含exclude的文件，不替换
		rule.addExclusionFilePattern("exclude");
		
		//替换中文为Chinese，但是行内包含exclude的行不替换
		rule.addRule("中文", "Chinese", "exclude");
		
		//替换文件为File
		rule.addRule("文件", "File");
		
		//添加规则
		replaceFileContentWithPattern.addFileRule(rule);;
		
		//执行替换
		replaceFileContentWithPattern.execute();
		
		Assert.assertEquals(new File(out_dir).listFiles().length, 2);
	}

}
