package br.com.sqg.rules;

import java.io.File;
import java.util.List;

import br.com.sqg.config.QualityGateReporter;
import br.com.sqg.config.Rule;

public class FileSizeRule extends Rule {

	
	public FileSizeRule(int threshold) {
		super(threshold);
	}

	@Override
	public String displayName() {
		return "File Size Rule";
	}

	@Override
	public void evaluate(File java, List<String> lines, QualityGateReporter report) {
		if (lines.size() > threshold()) {
			report.write(this, java, lines.size());
		}
	}
}
