package br.com.sqg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.com.sqg.config.QualityGateReporter;
import br.com.sqg.config.Rule;
import br.com.sqg.rules.BlockSizeRule;
import br.com.sqg.rules.ComplexityPerBlockRule;
import br.com.sqg.rules.ComplexityPerFileRule;
import br.com.sqg.rules.FileSizeRule;
import br.com.sqg.rules.QuantityOfMethodParametersRule;

public class Starter {

	private static final String APP_PROPERTIES = "app.properties";
	
	private static final String REPORT_EXPORT = "report.export";
	private static final String REPORT_BLOCK_FROM_GETTING_WORSE = "report.block_from_getting_worse";
	
	private static final String RULE_COMPLEXITY_PER_BLOCK = "rule.complexity_per_block";
	private static final String RULE_COMPLEXITY_PER_FILE = "rule.complexity_per_file";
	private static final String RULE_BLOCK_SIZE = "rule.block_size";
	private static final String RULE_FILE_SIZE = "rule.file_size";
	private static final String RULE_QUANTITY_OF_PARAMETERS = "rule.rule_quantity_of_parameters";
	
	private static final String SOURCE_PROPERTY = "source.root";
	private static final String SOURCE_ENCODING = "source.encoding";
	
	private static final String BREAK_BUILD_PROPERTY = "build.break";
	
	private static List<Rule> rules;
	private static Properties properties;
	private static QualityGateReporter qualityGateReporter;
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		
		loadPropertiesFile();
		createQualityGateReporter();
		for (File java : loadAllJavaFiles()) {
			evaluateFile(java);
		}
		printReport();

		long end = System.currentTimeMillis();
		logAnalysisDuration(end - start);
		
		if (Boolean.parseBoolean(properties.getProperty(BREAK_BUILD_PROPERTY)) && Starter.qualityGateReporter.wasSomethingReported()) {
			System.err.println("The build is broken!");
			System.exit(1);
		}
	}

	private static void logAnalysisDuration(long duration) {
		System.out.println(String.format("This analysis took %,d miliseconds", duration));
	}

	private static void printReport() throws IOException {
		Starter.qualityGateReporter.print();
	}

	private static void evaluateFile(File java) throws IOException {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(java.toPath(), Charset.forName(properties.getProperty(SOURCE_ENCODING)));
		} catch (Exception e) {
			System.err.format("Class %s could not be loaded: %s \n", java.getAbsolutePath(), e.getMessage());
			return;
		}
		
		for (Rule rule : rules()) {
			try {
				rule.evaluate(java, lines, Starter.qualityGateReporter);
			} catch (Exception e) {
				System.err.format("%s not aplied to %s because: %s \n", rule.displayName(), java.getAbsolutePath(), e.getMessage(), e.getStackTrace()[1]);
			}
		}
	}

	private static List<File> loadAllJavaFiles() {
		return new JavaFileLoader(new File(properties.getProperty(SOURCE_PROPERTY))).listJavas();
	}

	private static void loadPropertiesFile() throws IOException, FileNotFoundException {
		Starter.properties = new Properties();
		Starter.properties.load(new FileInputStream(APP_PROPERTIES));
	}

	private static void createQualityGateReporter() throws IOException {
		Starter.qualityGateReporter = new QualityGateReporter(new File(properties.getProperty(SOURCE_PROPERTY)));
		if (properties.containsKey(REPORT_BLOCK_FROM_GETTING_WORSE)) {
			Starter.qualityGateReporter.blockFromGettingWorse(properties.getProperty(REPORT_BLOCK_FROM_GETTING_WORSE));
		}
		if (properties.containsKey(REPORT_EXPORT)) {
			Starter.qualityGateReporter.exportTo(properties.getProperty(REPORT_EXPORT));
		}
	}

	private static List<Rule> rules() {
		if (Starter.rules != null) {
			return Starter.rules;
		}
		
		Starter.rules = new ArrayList<>();
		Starter.rules.add(new FileSizeRule(Integer.parseInt(properties.getProperty(RULE_FILE_SIZE))));
		Starter.rules.add(new BlockSizeRule(Integer.parseInt(properties.getProperty(RULE_BLOCK_SIZE))));
		Starter.rules.add(new ComplexityPerFileRule(Integer.parseInt(properties.getProperty(RULE_COMPLEXITY_PER_FILE))));
		Starter.rules.add(new ComplexityPerBlockRule(Integer.parseInt(properties.getProperty(RULE_COMPLEXITY_PER_BLOCK))));
		Starter.rules.add(new QuantityOfMethodParametersRule(Integer.parseInt(properties.getProperty(RULE_QUANTITY_OF_PARAMETERS))));
		
		return Starter.rules;
	}
}
