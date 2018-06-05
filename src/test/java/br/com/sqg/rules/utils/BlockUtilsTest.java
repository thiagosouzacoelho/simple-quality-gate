package br.com.sqg.rules.utils;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.sqg.rules.utils.BlockUtils;


public class BlockUtilsTest {

	@Test
	public void shouldNotExtractAnyBlock() {
		String classFile = "class A { class B { String classTest; String testClass; System.out.println(\"Test class\"); } }";
		List<String> extractBlocks = BlockUtils.extractBlocks(classFile);
		
		Assert.assertEquals(0, extractBlocks.size());
	}
	
	@Test
	public void shouldExtractOneBlock() {
		String classFile = "class A { void test() { System.out.println(\"Test\"); } }";
		List<String> extractBlocks = BlockUtils.extractBlocks(classFile);
		
		Assert.assertEquals(1, extractBlocks.size());
		Assert.assertEquals("{ System.out.println(\"\"); }", extractBlocks.get(0));
	}
	
	@Test
	public void shouldExtractThreeBlocks() {
		String classFile = "class A { void test() { System.out.println(\"Test\"); } class B { void a() { if (true) { exit(0); } } } }";
		
		List<String> extractBlocks = BlockUtils.extractBlocks(classFile);
		
		Assert.assertEquals(3, extractBlocks.size());
		Assert.assertTrue(extractBlocks.contains("{ System.out.println(\"\"); }"));
		Assert.assertTrue(extractBlocks.contains("{ if (true) { exit(0); } }"));
		Assert.assertTrue(extractBlocks.contains("{ exit(0); }"));
	}
	
	@Test
	public void shouldExtractThreeBlocksFromOneClass() {
		String classFile = "class A { void test() { System.out.println(\"Test\"); } public b() { if (true) { exit(0); } } }";
		
		List<String> extractBlocks = BlockUtils.extractBlocks(classFile);
		
		Assert.assertEquals(3, extractBlocks.size());
		Assert.assertTrue(extractBlocks.contains("{ System.out.println(\"\"); }"));
		Assert.assertTrue(extractBlocks.contains("{ if (true) { exit(0); } }"));
		Assert.assertTrue(extractBlocks.contains("{ exit(0); }"));
	}
	
	@Test
	public void shouldFindTwoClassesPositions() {
		String classFile = "class A { class B { String classTest; String testclass = \"a\"; System.out.println(\"Test class\"); } }";
		
		List<Integer> classesPositions = BlockUtils.findClassesPositions(classFile);
		
		Assert.assertEquals(2, classesPositions.size());
		Assert.assertTrue(classesPositions.contains(0));
		Assert.assertTrue(classesPositions.contains(10));
	}
	
	@Test
	public void shouldIgnoreCommentedBracket() {
		String classFile = "class A { int B { String classTest = \"{\"; String testClass; System.out.println(\"Test class\"); } }";
		List<String> extractBlocks = BlockUtils.extractBlocks(classFile);
		
		Assert.assertEquals(1, extractBlocks.size());
	}
	
}
 