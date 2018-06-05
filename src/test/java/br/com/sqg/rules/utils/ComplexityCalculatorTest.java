package br.com.sqg.rules.utils;

import org.junit.Assert;
import org.junit.Test;

import br.com.sqg.rules.utils.ComplexityCalculator;

public class ComplexityCalculatorTest {

	@Test
	public void shouldHaveComplexityOfOne() {
		String method = "void a() { String helps;); }";
		
		int complexity = ComplexityCalculator.complexityOf(method);
		
		Assert.assertEquals(1, complexity);
	}
	
	@Test
	public void shouldIgnoreNameWithTheKeywords() {
		String method = "void a() { String ifHelps;); }";
		
		int complexity = ComplexityCalculator.complexityOf(method);
		
		Assert.assertEquals(1, complexity);
	}
	
	@Test
	public void shouldFindIfAfterTabulationAndBeforeParentheses() {
		String method = "void a() { \tif(true) method(); }";
		
		int complexity = ComplexityCalculator.complexityOf(method);
		
		Assert.assertEquals(2, complexity);
	}
	
	@Test
	public void shouldHaveComplexityOfTwo() {
		String method = "void a() { if (true) method(); }";
		
		int complexity = ComplexityCalculator.complexityOf(method);
		
		Assert.assertEquals(2, complexity);
	}
	
	@Test
	public void shouldIgnoreWordsInsideString() {
		String method = "void a() { String a = \"if (true) method()\"; }";
		
		int complexity = ComplexityCalculator.complexityOf(method);
		
		Assert.assertEquals(1, complexity);
	}
	
	@Test
	public void shouldHaveAllComplexityKeywords() {
		String method = "void a() { if (true && true || true ) for (;;) while (true) switch (1) case 1: throw new Exception(); return; }";
		
		int complexity = ComplexityCalculator.complexityOf(method);
		
		Assert.assertEquals(9, complexity);
	}
}
