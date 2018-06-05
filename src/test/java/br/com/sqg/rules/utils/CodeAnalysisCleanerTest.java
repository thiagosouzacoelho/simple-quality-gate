package br.com.sqg.rules.utils;

import org.junit.Assert;
import org.junit.Test;

import br.com.sqg.rules.utils.CodeAnalysisCleaner;

public class CodeAnalysisCleanerTest {

	@Test
	public void shouldRemoveComments() {
		String code = "//Teste";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("", result);
	}
	
	@Test
	public void shouldRemoveStringContents() {
		String code = "\"Teste\"";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("\"\"", result);
	}
	
	@Test
	public void shouldRemoveStringInternalQuotes() {
		String code = "String a = \"Um \\\"Teste\\\" complicado\";";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("String a = \"\";", result);
	}
	
	@Test
	public void shouldRemoveMultilineCommentInOneLine() {
		String code = "/* Testando */";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("", result);
	}
	
	@Test
	public void shouldRemoveMultilineComments() {
		String code = "Texto \n/* Testando \n * outra \n */acabou";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("Texto \nacabou", result);
	}
	
	@Test
	public void shouldRemoveTwoMultilinaComments() {
		String code = "Texto \n/* Testando \n * outra \n */acabou \n /* \ncontinua \ncodigo */ foi";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("Texto \nacabou \n  foi", result);
	}
	
	@Test
	public void shouldRemoveCharacterContent() {
		String code = "Text 'A' text";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("Text ' ' text", result);
	}
	
	@Test
	public void shouldRemoveMultiLineCommentEndingInsideOneLineComment() {
		String code = "//* Comment \n // end comment */";
		String result = CodeAnalysisCleaner.clean(code);
		Assert.assertEquals("\n ", result);
	}
	
	
	
}
