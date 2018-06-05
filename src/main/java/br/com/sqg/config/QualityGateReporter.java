package br.com.sqg.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class QualityGateReporter {
	
	private File baseFolder;

	private Map<Rule, Set<FileMetric>> rulesMap;
	private Map<Rule, FileMetric> highestOffenders;
	private Map<String, Integer> blockedFromGettingWorse;
	
	private String reportFilePath;
	
	public QualityGateReporter(File baseFolder) {
		this.rulesMap = new HashMap<>();
		this.highestOffenders = new HashMap<>();
		this.blockedFromGettingWorse = new HashMap<>();
		this.baseFolder = baseFolder;
	}
	
	public void write(Rule rule, File evaluated, long metric) {
		if (isFileGettingWorse(rule, evaluated, metric)) return;
		
		if (!this.rulesMap.containsKey(rule)) {
			this.rulesMap.put(rule, new TreeSet<QualityGateReporter.FileMetric>());
		}
		
		FileMetric fileMetric = new FileMetric(rule, evaluated, metric);
		this.rulesMap.get(rule).add(fileMetric);
		
		if (!this.highestOffenders.containsKey(rule) || this.highestOffenders.get(rule).metric < fileMetric.metric) {
			this.highestOffenders.put(rule, fileMetric);
		}
	}
	
	public void print() throws IOException {
		List<Entry<Rule, Set<FileMetric>>> rules = new ArrayList<>(rulesMap.entrySet());
		rules.sort((Entry<Rule, Set<FileMetric>> o1, Entry<Rule, Set<FileMetric>> o2) -> o1.getKey().displayName().compareTo(o2.getKey().displayName()));
		
		for (Entry<Rule, Set<FileMetric>> entry : rules) {
			Set<FileMetric> metrics = entry.getValue();
			Rule rule = entry.getKey();
			System.out.println(rule.displayName() + ": " + metrics.size() + " violations");
			for (FileMetric fileMetric : metrics) {
				System.out.println(String.format("\t %s: %s > %s", getPathAfterBaseFolder(fileMetric.file), fileMetric.metric, rule.threshold()));
			}
		}
		
		System.out.println("\n\nList of the highest offenders:");
		for (Entry<Rule, FileMetric> rule : this.highestOffenders.entrySet()) {
			FileMetric value = rule.getValue();
			System.out.println(String.format("\t %s: %s: %s", rule.getKey().displayName(), getPathAfterBaseFolder(value.file), value.metric));
		}
		
		if (this.reportFilePath != null) {
			this.export();
		}
	}
	
	protected String getPathAfterBaseFolder(File file) {
		return file.getAbsolutePath().replace(this.baseFolder.getAbsolutePath(), "");
	}
	
	public boolean wasSomethingReported() {
		return !rulesMap.isEmpty();
	}
	
	public void export() throws IOException {
		StringBuilder reportContent = new StringBuilder();
		for (Set<FileMetric> metrics : rulesMap.values()) {
			for (FileMetric metric : metrics) {
				reportContent.append(String.format("%s;%s;%s\n", metric.rule.key(), getPathAfterBaseFolder(metric.file), metric.metric));
			}
		}
		
		Files.write(Paths.get(this.reportFilePath), reportContent.toString().getBytes());
	}

	public void blockFromGettingWorse(String reportFile) throws IOException {
		List<String> pastReport = Files.readAllLines(Paths.get(reportFile));
		for (String line : pastReport) {
			String[] fields = line.split(";");
			String key = key(fields[0], fields[1]);
			if (!this.blockedFromGettingWorse.containsKey(key)) {
				this.blockedFromGettingWorse.put(key, Integer.parseInt(fields[2]));
			}
		}
	}

	public void exportTo(String reportFilePath) {
		this.reportFilePath = reportFilePath;
	}
	
	private String key(String ruleKey, String fileAbsolutePath) {
		return ruleKey + "_" + fileAbsolutePath;
	}
	
	private boolean isFileGettingWorse(Rule rule, File evaluated, long metric) {
		String key = key(rule.key(), getPathAfterBaseFolder(evaluated));
		return this.blockedFromGettingWorse.containsKey(key) && metric <= this.blockedFromGettingWorse.get(key);
	}
	
	private class FileMetric implements Comparable<FileMetric> {
		private Rule rule;
		private File file;
		private long metric;
		
		public FileMetric(Rule rule, File file, long metric) {
			this.rule = rule;
			this.file = file;
			this.metric = metric;
		}
		
		@Override
		public int compareTo(FileMetric o) {
			int comparison = getPathAfterBaseFolder(file).compareTo(getPathAfterBaseFolder(o.file));
			return comparison == 0 ? Long.valueOf(o.metric).compareTo(Long.valueOf(this.metric)) : comparison;
		}
	}
}
