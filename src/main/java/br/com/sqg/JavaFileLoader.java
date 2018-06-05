package br.com.sqg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaFileLoader {

	private File root;
	private List<File> javas;
	
	public JavaFileLoader(File root) {
		this.root = root;
		this.javas = new ArrayList<>();
	}

	public List<File> listJavas() {
		this.findJavas(root);
		return javas;
	}

	private void findJavas(File file) {
		if (file.getName().endsWith(".java")) {
			this.javas.add(file);
			return;
		}
		
		if (file.isFile() || file.listFiles() == null) return;
		
		for (File subFile : file.listFiles()) {
			this.findJavas(subFile);
		}
	}
}
