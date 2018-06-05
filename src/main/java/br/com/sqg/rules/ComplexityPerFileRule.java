package br.com.sqg.rules;

import java.io.File;
import java.util.List;

import br.com.sqg.config.QualityGateReporter;
import br.com.sqg.config.Rule;
import br.com.sqg.rules.utils.ComplexityCalculator;

public class ComplexityPerFileRule extends Rule {

	public ComplexityPerFileRule(int threshold) {
		super(threshold);
	}
	
	@Override
	public String displayName() {
		return "Complexity per file";
	}

	@Override
	public void evaluate(File java, List<String> lines, QualityGateReporter report) {
		String joined = String.join("\n", lines);
		int complexity = ComplexityCalculator.complexityOf(joined);
		if (complexity > threshold()) {
			report.write(this, java, complexity);
		}
	}
}
