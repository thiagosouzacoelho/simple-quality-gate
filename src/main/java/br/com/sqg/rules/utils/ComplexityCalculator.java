package br.com.sqg.rules.utils;

public class ComplexityCalculator {

	public static int complexityOf(String block) {
		String clearBlock = CodeAnalysisCleaner.clean(block);
		
		int complexity = 1;
		complexity += clearBlock.split("\\Wif\\W").length - 1;
		complexity += clearBlock.split("\\Wfor\\W").length - 1;
		complexity += clearBlock.split("\\Wwhile\\W").length - 1;
		complexity += clearBlock.split("\\Wcase\\W").length - 1;
		complexity += clearBlock.split("\\Wcatch\\W").length - 1;
		complexity += clearBlock.split("\\Wthrow\\W").length - 1;
		complexity += clearBlock.split("\\Wreturn\\W").length - 1;
		complexity += clearBlock.split("\\W&&\\W").length - 1;
		complexity += clearBlock.split("\\W\\|\\|\\W").length - 1;
		complexity += clearBlock.split("\\W\\?\\W").length - 1;

		return complexity;
	}
}
