package br.com.sqg.config;

import java.io.File;
import java.util.List;

public abstract class Rule {
	
	private int threshold;
	
	public Rule(int threshold) {
		this.threshold = threshold;
	}
	
	public int threshold() {
		return threshold;
	}
	
	public final String key() {
		return this.displayName().toLowerCase().replace(' ', '_');
	}
	
	public abstract String displayName();

	public abstract void evaluate(File java, List<String> lines, QualityGateReporter report);
}
