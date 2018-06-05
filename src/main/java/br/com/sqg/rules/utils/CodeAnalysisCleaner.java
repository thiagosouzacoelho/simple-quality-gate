package br.com.sqg.rules.utils;

public class CodeAnalysisCleaner {

	/**
	 * Remove unnecessary parts for analysis of the code.
	 * This method removes:
	 * - one-line comments
	 * - multi-line comments
	 * - string contents
	 * 
	 * @param code
	 * @return
	 */
	public static String clean(String code) {
		String replacedText = removeStringInternalQuote(code);
		replacedText = removeStringContent(replacedText);
		replacedText = removeCharactersContent(replacedText);
		replacedText = separateOneLineCommentsAndMultiLineCommentsInDifferentLines(replacedText);
		replacedText = removeMultiLineComments(replacedText);
		replacedText = removeOneLineComments(replacedText);
		replacedText = transformTabulationIntoSpaces(replacedText);
		
		return replacedText;
	}

	private static String transformTabulationIntoSpaces(String replacedText) {
		return replacedText.replaceAll("\t", " ");
	}

	private static String separateOneLineCommentsAndMultiLineCommentsInDifferentLines(String replacedText) {
		return replacedText.replaceAll("//\\*", "// *");
	}

	private static String removeCharactersContent(String replacedText) {
		return replacedText.replaceAll("'.'", "' '");
	}

	private static String removeMultiLineComments(String replacedText) {
		while (replacedText.indexOf("/*") != -1) {
			int inicio = replacedText.indexOf("/*");
			int fim = replacedText.indexOf("*/");
			if (fim < inicio) {
				replacedText = new StringBuilder(replacedText).delete(fim, fim + 2).toString();
				fim = replacedText.indexOf("*/");
			}
			replacedText = new StringBuilder(replacedText).delete(inicio, fim + 2).toString();
		}
		return replacedText;
	}

	private static String removeOneLineComments(String replacedText) {
		return replacedText.replaceAll("//.*", "");
	}

	private static String removeStringContent(String replacedText) {
		return replacedText.replaceAll("\".*?\"", "\"\"");
	}

	private static String removeStringInternalQuote(String code) {
		return code.replaceAll("\\\\\"", ""); 
	}
}
