package br.com.sqg.rules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sqg.config.QualityGateReporter;
import br.com.sqg.config.Rule;
import br.com.sqg.exception.NotStartOrEndWithParenthesesException;
import br.com.sqg.rules.utils.CodeAnalysisCleaner;

public class QuantityOfMethodParametersRule extends Rule {

	public QuantityOfMethodParametersRule(int threshold) {
		super(threshold);
	}

	@Override
	public String displayName() {
		return "Quantity of Parameters Rule";
	}

	@Override
	public void evaluate(File java, List<String> lines, QualityGateReporter report) {
		String joinedCode = CodeAnalysisCleaner.clean(String.join(" ", lines));
		for (String parenthesesBlock : extractParenthesesBlock(joinedCode)) {
			int quantityOfParameters = quantityOfParameters(parenthesesBlock);
			if (quantityOfParameters > threshold()) {
				report.write(this, java, quantityOfParameters);
			}
		}
	}

	protected int quantityOfParameters(String parenthesesBlock) {
		if (!doesBlockStartAndEndWithParentheses(parenthesesBlock)) throw new NotStartOrEndWithParenthesesException(parenthesesBlock);
		
		if (doesBlockContainsLiterals(parenthesesBlock) || parenthesesBlock.length() == 2 || parenthesesBlock.contains("{")) return 0;
		
		String[] parameters = parenthesesBlock.split(",");
		
		if (!parameters[0].matches("\\(\\w+\\s+\\w+,?\\)?")) return 0;
		
		return parameters.length;
	}

	private boolean doesBlockContainsLiterals(String parenthesesBlock) {
		return parenthesesBlock.contains("\"") || parenthesesBlock.split("\\d").length > 1;
	}

	private boolean doesBlockStartAndEndWithParentheses(String parenthesesBlock) {
		return parenthesesBlock.startsWith("(") && parenthesesBlock.endsWith(")");
	}
	
	protected List<String> extractParenthesesBlock(String code) {
		List<String> methodParameters = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\(.*?\\)");
		Matcher matcher = pattern.matcher(code);
		while (matcher.find()) {
			methodParameters.add(matcher.group());
		}
		
		return methodParameters;
	}
}
