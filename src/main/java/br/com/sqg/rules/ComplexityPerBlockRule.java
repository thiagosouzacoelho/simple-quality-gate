package br.com.sqg.rules;

import java.io.File;
import java.util.List;

import br.com.sqg.config.QualityGateReporter;
import br.com.sqg.config.Rule;
import br.com.sqg.rules.utils.BlockUtils;
import br.com.sqg.rules.utils.ComplexityCalculator;

public class ComplexityPerBlockRule extends Rule {


	public ComplexityPerBlockRule(int threshold) {
		super(threshold);
	}
	
	@Override
	public String displayName() {
		return "Complexity Per Block Rule";
	}

	@Override
	public void evaluate(File java, List<String> lines, QualityGateReporter report) {
		String classFile = String.join("\n", lines);
		for (String block : BlockUtils.extractBlocks(classFile)) {
			int complexity = ComplexityCalculator.complexityOf(block);
			if (complexity > threshold()) {
				report.write(this, java, complexity);
			}
		}
	}
}