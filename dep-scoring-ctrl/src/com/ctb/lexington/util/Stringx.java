/*
 * Created on Apr 15, 2004
 *
 */
package com.ctb.lexington.util;

/**
 * @author arathore
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

/**
 * Helper class for methods that should exist on the String class, but don't.
 */
public class Stringx {

	private static String EMPTY_STRING = "";
	
	public static StringWalker walker(List list) {
		return new StringWalker(list.listIterator());
	}

    public static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    public static class StringWalker {
		private final ListIterator i;
		private String current;
		private boolean hasCurrent = false;
		StringWalker(ListIterator i) {
			this.i = i;
		}
		public boolean stepIfNecessary() {
			if (! i.hasNext()) return false; 
			current = (String) i.next();
			hasCurrent = true;
			return true;
		}
		public String string() {
			if (! hasCurrent)
				throw new RuntimeException("current element has been removed.");
			return current;
		}
		public boolean isLast() {
			return !i.hasNext();
		}
		public void remove() {
			i.remove();
			hasCurrent = false;
		}

		public void set(String s) {
			current = s;
			i.set(current);
		}
	}
	
	/**
	 * returns an empty string
	 * @return the static varibale EMPTY_STRING
	 */
	public static String empty() {
		return EMPTY_STRING;
	}

	/**
	 * return the string with parentheses.
	 * @param s the string to paren
	 * @return the parenthesized string
	 */
	public static String paren(String s) {
		return "(" + s + ")";
	}

	/**
	 * Returns true if the content string contains the expected string.
	 * @param content String in which to search
	 * @param expected String to be found
	 * @return true if the content string contains the expected string
	 */
	public static boolean contains(String search, String find) {
		if (search == null) throw new IllegalArgumentException("Content cannot be null");
		if (find == null) throw new IllegalArgumentException("Expected cannot be null");
		return search.indexOf(find) != -1;
	}
	
	/**
	 * Returns the string formed by placing the delimeter between the elements of strings. 
	 * @param list the list of objects to be joined into a single string
	 * @param delimiter the string to place between elements
	 * @return delimited string 
	 */
	public static String join(final List list, final String delimeter) {
		StringBuffer b = new StringBuffer();
		for (Iterator i = list.iterator(); i.hasNext();) {
			b.append(i.next());
			if (i.hasNext() && b.length() > 0)
                b.append(delimeter);
		}
		return b.toString();
	}

	/**
	 * Returns true if the content string does not contain the expected string.
	 * @param content String in which to search
	 * @param expected String to be found
	 * @return true if the content string does not contain the expected string
	 */
	public static boolean doesNotContain(String search, String doNotFind) {
		return !contains(search, doNotFind);
	}

	/**
	 * formats a string for use in javascript output.
	 * @param string to be quoted
	 * @return quoted string
	 */
	public static String jsQuote(String string) {
		if (string == null) return "''";
		return "'" + Stringx.replace(string, "'", "\\'") + "'";
	}

	/**
	 * replaces instances of one string with another in a string.
	 * @param string
	 * @param toReplace
	 * @param replacement
	 * @return
	 */
	public static String replace(String string, String toReplace, String replacement) {
		StringBuffer result = new StringBuffer();
		
		for(StringTokenizer t = new StringTokenizer(string, toReplace); t.hasMoreTokens(); ) {
			result.append(t.nextToken());
			if (t.hasMoreTokens()) result.append(replacement);
		}
		return result.toString();
	}

	/**
	 * Return a modification of the original String that is trimmed and has all
	 * multiple white space characters (blanks, tabs, carriage returns, line feeds) replaced
	 * with a single space.
	 * @param original to collapse
	 * @return trimmed original with no multiple white space characters imbedded in it
	 */
	public static String innerTrim(String original) {
		if (original == null) return null;
		StringBuffer result = new StringBuffer();
		for (StringTokenizer t = new StringTokenizer(original, " \t\n\r"); t.hasMoreTokens();) {
			String word = t.nextToken();
			if (word.length() == 0) continue;
			result.append(word);
			if (t.hasMoreTokens()) result.append(" ");
		}
		return result.toString().trim();
	}

	/**
	 * Returns true if the string is null or contains nothing but white space.
	 * @param string to test
	 * @return true if string is empty
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}

	/**
	 * Returns true if the string is null or contains nothing but white space.
	 * @param string to test
	 * @return true if string is empty
	 */
	public static boolean isEmpty(Object o) {
		if(o == null)
			return true;
		if (! (o instanceof String))
			throw new RuntimeException("object o: " + o + " is not a String object.");
		return isEmpty((String)o);
	}

	/**
	 * Returns true if the string contains characters other than white space.
	 * @param string to test
	 * @return true if string is not empty
	 */
	public static boolean hasContent(String string) {
		return !isEmpty(string);
	}

	/**
	 * return up to the first i characters of string.
	 * @param i max number of chars
	 * @param string original string
	 * @return substring of the first i chars or less
	 */
	public static String maxChars(int i, String string) {
		if (string == null) return "";
		if (string.length() <= i) return string;
		return string.substring(0, i);
	}

	/**
	 * appends demarkation to a string if the string is not empty.
	 * @param string demarkation that has to be appended 
	 * @param string original string
	 * @return  demarktion and string combined if string is not empty
	 */	
	public static String appendIfNotEmpty(String separator, String value) {
		if (Stringx.isEmpty(value)) return "";
		return separator + value;
	}

	/**
	 * @param buf - the stringbuffer to append into.
	 * @see appendIfNotEmpty
	 */	
	public static StringBuffer appendIfNotEmpty(StringBuffer buf, String separator, String value) {
		if (Stringx.isEmpty(value)) return buf;
		if (Stringx.hasContent(buf.toString()))
			buf.append(separator);
		return buf.append(value);
	}
	
	/**
	 * Return the first element of a list as a string.
	 * @param l the list
	 * @return the first element
	 */
	public static String first(List l) {
		Object o = Listx.first(l);
		if (! (o instanceof String))
			throw new RuntimeException("First element of " + l + " is not a string!");
		return (String) o;
	}

	public static String bracket(String value) {
		return "[" + value + "]";
	}

	/**
	 * returns a string with any space delimited words longer than maxLength broken up
	 * into maxLength size chunks with a hyphen-space combo added.
	 * @param maxLength
	 * @param string
	 * @return hyphenated string
	 */
	public static String hyphenated(int maxLength, String string) {
		if (string == null) return null;
		if (string.length() < maxLength) return string;
		StringBuffer buf = new StringBuffer();
		for(StringTokenizer i = new StringTokenizer(string, " "); i.hasMoreTokens(); ) {
			String word = i.nextToken();
			buf.append(
				word.length() > maxLength ? join(chunksWithSize(maxLength, word), "- ") : word);
			if (i.hasMoreTokens()) buf.append(" ");			
		}
		return buf.toString();
	}

	static List chunksWithSize(int size, String word) {
		if (word.length() < size)
			throw new RuntimeException("no chunks of size " + size + " in " + word);
		List results = Listx.list();
		int endIndex = size;
		while (endIndex < word.length()) {
			results.add(word.substring(endIndex - size, endIndex));
			endIndex += size;
		}
		results.add(word.substring(endIndex - size));
		return results;
	}

	public static String name(String first, String middle, String last) {
		List nonEmptyNames = Listx.list();
		if (hasContent(first)) nonEmptyNames.add(first);
		if (hasContent(middle)) nonEmptyNames.add(middle);
		if (hasContent(last)) nonEmptyNames.add(last);
		return join(nonEmptyNames, " ");
	}

	public static String nameLastFirst(String first, String middle, String last) {
		StringBuffer buf = new StringBuffer();
		if (hasContent(last)) {
			buf.append(last.trim());
			if (isEmpty(middle) && isEmpty(first))
				return buf.toString();
			buf.append(",");
		}
		appendIfNotEmpty(buf, " ", first.trim());
		appendIfNotEmpty(buf, " ", middle.trim());
		return buf.toString().trim();
	}

	public static List split(String original, String delimiter) {
		List results = new ArrayList();
		int start = 0;
		int end = 0;
		while(true){
			end = original.indexOf(delimiter, start);
			if(end == -1) break;
			results.add(original.substring(start, end));
			start = end + delimiter.length();
		}
		results.add(original.substring(start));
		return results;
	}
	
	public static String split(String original, String delimiter, String elementToAdd) {
		List words = split(original , delimiter);
		if (words.isEmpty()) return original;
		return join(words, elementToAdd);
	}
	

	/**
	 * Return the phrase with the proper capitalization.  Certain words
	 * (like "a", "the", and "and") are not capitalized if they occur first.
	 * @param phrase Phrase or name to be capitalized
	 * @return phrase with proper capitalization
	 */
	public static String toTitleCase(String phrase) {
		List megaWords = split(phrase, " ");
		if (megaWords.isEmpty())
			return phrase;
		for (ListIterator i = megaWords.listIterator(); i.hasNext();) {
			List words = split((String) i.next(), ".");
			if (words.size() > 1)
			for (ListIterator j = words.listIterator(); j.hasNext();)
					j.set(basicCapitalize((String) j.next()));
			else
				words.set(0, smartCapitalize((String) words.get(0)));
			i.set(words);
		}
		((List) megaWords.get(0)).set(0, basicCapitalize(Stringx.first((List) megaWords.get(0))));
		for (ListIterator i = megaWords.listIterator(); i.hasNext();)
			i.set(join((List) i.next(), "."));
		return join(megaWords, " ");
	}
	
	private static String basicCapitalize(String word) {
		if (word.length() == 0) return word;
		return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}
	
	private static String smartCapitalize(String word) {
		if (word.length() == 0) return word;
		String lowerCaseWord = word.toLowerCase();
		if (CAPITALIZE_EXCEPTIONS.contains(lowerCaseWord)) return lowerCaseWord;
		return basicCapitalize(lowerCaseWord);
	}
	private final static List CAPITALIZE_EXCEPTIONS =
		Listx.list("a", "an", "and", "as", "at", "but", "for", "in", "not", "or", "the");

	public static String[] splitPhrase(String words, int max) {
		String[] results = new String[2];
		if (words.length() <= max){
			results[0] = words;
			results[1] = "";
			return results;
		}
		int splitIndex = words.lastIndexOf(" ", max);
		results[0] = splitIndex == -1 ? "" : words.substring(0, splitIndex);
		results[1] = words.substring(splitIndex + 1);
		return results;
	}

	/**
	 * naive quoting for error messages.
	 * @param s the string to quote
	 * @return the string with single 's around it 
	 */
	public static String quote(String s) {
		return "'" + s + "'";
	}

	/**
	 * @param defalt return if value is null
	 * @param value return if not null
	 * @return defalt if value is null, else the value. Mimics the behaviour of the Sql function "nvl".
	 */
	public static String nullValue(String defalt, String value) {
		return value == null ? defalt : value;
	}
	
	/**
	 * @param defalt return if value is null
	 * @param value return if not null
	 * @return defalt if value is null, else the value. Mimics the behaviour of the Sql function "nvl".
	 */
	public static String notNullValue(Object value) {
		return value == null ? EMPTY_STRING : value.toString();
	}

	/**
	 * @param list a list with a single String element
	 * @return the only element of the list as a String
	 */
	public static String only(List strings) {
		return (String) Listx.only(strings);
	}

	/**
	 * joins together a list of strings with nothing in between them
	 * @param list list to join
	 * @return the string all stuck together
	 */
	public static String join(List list) {
		return join(list, "");
	}

	public static List upcased(Collection c) {
		List results = Listx.list();
		for(Iterator i = c.iterator(); i.hasNext(); )
			results.add(i.next().toString().toUpperCase());
		return results;
	}

	public static String first(String s) {
		return s.substring(0, 1);
	}

	public static String doubleQuote(String value) {
		return "\"" + value + "\"";
	}

	public static List doubleQuote(List list) {
		List results = Listx.list(list);
		for(ListIterator i = results.listIterator(); i.hasNext(); ) {
			i.set(doubleQuote((String)i.next()));
		}
		return results;
	}

	public static long asNum(String num) {
		if (Stringx.isEmpty(num)) 
			return 0;
		return Long.parseLong(num);
	}

	public static String safeSubstring(String s, int start, int num) {
		if (s == null) return "";
		if (s.length() < start) return "";
		if (s.length() < start + num) return s.substring(start);
		return s.substring(start, start + num);
	}

	public static String yOrN(boolean value) {
		return value ? "Y" : "N";
	}

	public static boolean isTrue( String value ) {
		boolean result = false;
		
		if( value != null && ( value.equalsIgnoreCase("T") || Boolean.valueOf(value).booleanValue() ) )
			result = true;
		
		return result;
	}
	
	public static String toUpperCamel(String tableName) {
		String lowerCaseName = tableName.toLowerCase();
		StringTokenizer st = new StringTokenizer(lowerCaseName,"_ ");
		StringBuffer result = new StringBuffer();
		while (st.hasMoreTokens()){
			String token = st.nextToken();
			String temp = token.substring(0,1).toUpperCase();
			temp = temp+token.substring(1);
			result.append(temp);
		}
		return result.toString().trim();
	}

	public static String word(String string, int maxLength, String delim) {
		if (!contains(string, delim)) return safeSubstring(string, 0, maxLength);
		List words = split(string, delim);
		String word = "";
		while (!words.isEmpty()) { 
			if (word.length() + first(words).length() < 35)
				word += first(words);
			words.remove(0);
		}
		return word;
	}

	public static boolean isDigits(String value) {
		if (isEmpty(value))
			return true;
		for (int i = 0; i < value.length(); i++) {
			if (!Character.isDigit(value.charAt(i)))
				return false;
		}
		return true;
	}
	
	public static String string(long l) {
		return new Long(l).toString();
	}
	
	/**
	 * Return a new String that has stripped all the specified tokens
	 * from it.
	 * @param originalString
	 * @param tokens String with each token in it (treated as a list of tokens)
	 * @return new String stripped of tokens
	 */
	public static String removeTokens(String originalString, String tokens) {
		if (originalString == null) return null;
		StringBuffer result = new StringBuffer();
		StringTokenizer st = new StringTokenizer(originalString.trim(), tokens);
		while (st.hasMoreTokens())
			result.append(st.nextToken());
		return result.toString();
	}

	public static String maybeOnly(List list) {
		return (String)Listx.maybeOnly(list);
	}
	public static String addDashAfterSecondCharacter(String id) {
		return Stringx.safeSubstring(id, 0, 2) + "-" + Stringx.safeSubstring(id, 2, 8);
	}

	public static boolean isZero(String value) {
		if (isEmpty(value)) return true;
		return Double.parseDouble(value) < 0.00001;
	}

	public static String trunc(String s, int length) {
		int end = (s.length() > length) ? length : s.length();
		return s.substring(0, end);
	}
		
}

