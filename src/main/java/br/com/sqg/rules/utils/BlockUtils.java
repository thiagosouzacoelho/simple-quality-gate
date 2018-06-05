package br.com.sqg.rules.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sqg.exception.InvalidBlocksException;

public class BlockUtils {

	public static List<String> extractBlocks(String classFile) {
		String cleannedClassFile = CodeAnalysisCleaner.clean(classFile);
		validateIfClassHasAllBracketsPaired(cleannedClassFile);

		List<Integer> classKeywordsIndexes = findClassesPositions(cleannedClassFile);
		Collections.sort(classKeywordsIndexes);
		Collections.reverse(classKeywordsIndexes);
		
		List<String> blocks = new ArrayList<String>();
		for (int startClassIndex : classKeywordsIndexes) {
			Stack<Entry> pairedOpennedClosedBracketsIndexes = pairedBrackets(cleannedClassFile, startClassIndex);
			
			if (pairedOpennedClosedBracketsIndexes.isEmpty()) continue;
			
			Entry lastEntry = pairedOpennedClosedBracketsIndexes.pop();
			while (!pairedOpennedClosedBracketsIndexes.isEmpty()) {
				Entry entry = pairedOpennedClosedBracketsIndexes.pop();
				blocks.add(cleannedClassFile.substring(entry.key(), entry.value() + 1));
			}
			
			cleannedClassFile = new StringBuilder(cleannedClassFile).delete(lastEntry.key(), lastEntry.value() + 1).toString();
		}
		
		return blocks;
	}

	private static void validateIfClassHasAllBracketsPaired(String cleannedClassFile) {
		int countOpening = cleannedClassFile.length() - cleannedClassFile.replace("{", "").length();
		int countClosing = cleannedClassFile.length() - cleannedClassFile.replace("}", "").length();
		
		if (countOpening != countClosing) throw new InvalidBlocksException(countOpening, countClosing);
	}

	private static Stack<Entry> pairedBrackets(String cleannedClassFile, int startClassIndex) {
		Stack<Entry> pairedOpennedClosedBracketsIndexes = new Stack<>();
		
		Stack<Entry> openedEntries = new Stack<>();
		int openBracketIndex = findFirstOpenBracketAfter(cleannedClassFile, startClassIndex);
		openedEntries.push(new Entry(openBracketIndex++));
		
		for (int i = openBracketIndex; !openedEntries.isEmpty(); i++) {
			if (cleannedClassFile.charAt(i) == '{') {
				openedEntries.push(new Entry(i));
			} else if (cleannedClassFile.charAt(i) == '}') {
				pairedOpennedClosedBracketsIndexes.add(openedEntries.pop().value(i));
			}
		}
		return pairedOpennedClosedBracketsIndexes;
	}

	private static int findFirstOpenBracketAfter(String classFile, int startPosition) {
		for (int i = startPosition; i < classFile.length(); i++) {
			if (classFile.charAt(i) == '{') {
				return i;
			}
		}
		return -1;
	}

	protected static List<Integer> findClassesPositions(String classFile) {
		List<Integer> classesPositions = new ArrayList<>();
		
		Pattern pattern = Pattern.compile("(\\s|^)class\\s");
		populateListWithfindPositions(classesPositions, pattern, classFile);
		
		return classesPositions;
	}

	private static <T extends Collection<Integer>> void populateListWithfindPositions(T positions, Pattern pattern, String text) {
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			int addIfStartsWithSpace = matcher.group().startsWith(" ") ? 1 : 0;
			positions.add(addIfStartsWithSpace + matcher.start());
		}
	}
	
	private static class Entry {
		
		private int key;
		private int value;
		
		public Entry(int key) {
			this.key = key;
		}

		public Entry value(int i) {
			this.value = i;
			return this;
		}
		
		public int value() {
			return value;
		}

		public int key() {
			return key;
		}
	}
}
