package com.bravechen.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import junit.framework.Assert;

public class ChangeFileEncodeUtilTest {
	@Test
	public void change2UTF8Test() {
		String path = this.getClass().getClassLoader().getResource("DataDomain.thrift").getPath();
File file = new File(path);
		Assert.assertFalse(FileEncodeUtil.isUTF8(file));
		try {
			File file2 = new File(path + ".UTF8");
			FileUtils.deleteQuietly(file2);
			ChangeFileEncodeUtil.change2UTF8(file, file2);
			Assert.assertTrue(FileEncodeUtil.isUTF8(file2));
		} catch (IOException e) {
			Assert.fail();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		File dir = new File("D:\\workspaces\\houyixue\\weiexamDoc\\code\\WebRoot\\admin\\app\\chatnew");
		Collection<File> files = FileUtils.listFiles(dir, null, true);
		for(File file :files) {
			ChangeFileEncodeUtil.change2UTF8(file);
		}
	}

}
