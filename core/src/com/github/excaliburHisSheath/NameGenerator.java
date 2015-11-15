// This code arrives from https://github.com/excaliburHisSheath/NameGenerator
package com.github.excaliburHisSheath;

import java.util.*;

public class NameGenerator {

	private int[][][] probabilities;
	private ArrayList<Character> characters;
	private TreeMap<String, String> input;
	//private TreeMap<String, String> results;

	private static final char TERMINATOR = '\0';
	private static final Character[] defaultChars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', TERMINATOR };

	// constructors ------------------------------------------------------------------------------------------------

	public NameGenerator(String[] sourceNames) {
		probabilities = new int[defaultChars.length][defaultChars.length][defaultChars.length];
		characters = new ArrayList<Character>();
		for (Character s : defaultChars)
			characters.add(s);
		//results = new TreeMap<String, String>();
		input = new TreeMap<String, String>();
		generateProbabilities(sourceNames);
	}

	// private methods ---------------------------------------------------------------------------------------------

	private void addStringToProbability(String name) {
		String lowerName = name.toLowerCase();
		input.put(lowerName, lowerName);
		char last1 = TERMINATOR, last2 = TERMINATOR;
		int index = 0;
		while (index < lowerName.length()) {
			if (charIndex(lowerName.charAt(index)) != -1) {
				char current = lowerName.charAt(index);
				probabilities[charIndex(last1)][charIndex(last2)][charIndex(current)]++;
				last1 = last2;
				last2 = current;
				index++;
			} else {
				index++;
			}
		}
		char current = TERMINATOR;
		probabilities[charIndex(last1)][charIndex(last2)][charIndex(current)]++;
	}

	// chooses a character from the probability matrix based on the previous two chars
	// precondition: $last1 and $last2 are recognized characters that have previously appeared in that order
	private char nextCharByLast(char last1, char last2) {
		int total = 0;
		for (int i : probabilities[charIndex(last1)][charIndex(last2)]) {
			total += i;
		}
		total = (new Random()).nextInt(total);
		int index = 0, subTotal = 0;
		do {
			subTotal += probabilities[charIndex(last1)][charIndex(last2)][index++];
		} while (subTotal <= total);
		return (characters.get(--index));
	}

	// returns the index in $chars for $c
	// Note: if the character is a letter it must be lower case
	private int charIndex(char c) {
		for (int i = 0; i < characters.size(); i++) {
			if (characters.get(i).equals(c))
				return i;
		}
		return -1;
	}

	// public methods ----------------------------------------------------------------------------------------------

	// generates the probabilities matrix from $source
	// precondition: $source is a correctly formated .txt file
	private void generateProbabilities(String[] sourceNames) {
		for (String name : sourceNames) {
			addStringToProbability(name);
		}
	}
	
	public String generateName() {
		String result = "";
		char last1 = TERMINATOR, last2 = TERMINATOR;
		do {
			char temp = nextCharByLast(last1, last2);
			last1 = last2;
			last2 = temp;
			if (last2 != TERMINATOR)
				result += Character.toString(last2);
		} while (last2 != TERMINATOR);
		return result;
	}

	// returns true if the name generator recognizes $c
	public boolean recognizesCharacter(char c) {
		for (Character character : characters)
			if (character.equals(c))
				return true;
		return false;
	}

}
