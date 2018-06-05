package br.com.sqg.rules;

import java.io.File;
import java.util.List;

import br.com.sqg.config.QualityGateReporter;
import br.com.sqg.config.Rule;
import br.com.sqg.rules.utils.BlockUtils;

public class BlockSizeRule extends Rule {

	public BlockSizeRule(int threshold) {
		super(threshold);
	}
	
	@Override
	public String displayName() {
		return "Block Size Rule";
	}

	@Override
	public void evaluate(File java, List<String> lines, QualityGateReporter report) {
		String classFile = String.join("\n", lines);
		for (String block : BlockUtils.extractBlocks(classFile)) {
			int blockSize = block.split("\n").length;
			if (blockSize > threshold()) {
				report.write(this, java, blockSize);
			}
		}
	}
}
