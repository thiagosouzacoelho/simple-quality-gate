package br.com.sqg.config;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;


public class QualityGateReporterTest {

	@Test
	public void relativePathShouldRemoveBaseFolder() {
		QualityGateReporter qgr = new QualityGateReporter(new File(File.separator + "base" + File.separator + "folder"));
		String pathAfterBaseFolder = qgr.getPathAfterBaseFolder(new File(File.separator + "base" + File.separator + "folder" + File.separator + "relative"));
		Assert.assertEquals(File.separator + "relative", pathAfterBaseFolder);
	}
	
}
