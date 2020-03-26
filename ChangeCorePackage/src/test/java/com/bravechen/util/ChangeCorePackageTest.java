package com.bravechen.util;

import java.io.File;

import org.junit.Test;

import junit.framework.Assert;

public class ChangeCorePackageTest {
	@Test
	public void test() {
		String baseSrcPath = new File("src/test/java").getAbsolutePath();
		String srcPackge = "com" + ".parent";
		String destPackge = "com" + ".father";
		if (!new File(baseSrcPath, srcPackge.replace(".", "/")).exists()) {
			String tmp = srcPackge;
			srcPackge = destPackge;
			destPackge = tmp;
		}
		new ChangeCorePackge(baseSrcPath, srcPackge, destPackge).execute();

		Assert.assertFalse(new File(baseSrcPath, srcPackge.replace(".", "/")).exists());
		Assert.assertTrue(new File(baseSrcPath, destPackge.replace(".", "/")).exists());
	}

}