package com.bravechen.util;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class FileEncodeUtilTest {

	@Test
	public void testGB_Charset() {
		URL url = this.getClass().getClassLoader().getResource("GBK.txt");
		Assert.assertFalse(FileEncodeUtil.isUTF8(url));
	}

	@Test
	public void testUTF8() {
		URL url = this.getClass().getClassLoader().getResource("UTF-8.txt");
		Assert.assertTrue(FileEncodeUtil.isUTF8(url));
	}

}
