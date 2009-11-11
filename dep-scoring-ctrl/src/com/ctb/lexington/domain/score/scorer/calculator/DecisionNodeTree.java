/*
 * Created on Nov 9, 2004
 *
 * DecisionNodeTree.java
 */
package com.ctb.lexington.domain.score.scorer.calculator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.ctb.lexington.exception.CTBSystemException;
import com.ctb.lexington.exception.SystemException;
import java.util.Enumeration;
import java.util.ResourceBundle;


public class DecisionNodeTree {
	private DecisionNode root = null;
	
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final String DONT_CARE = "-";
    private final String propsName;
	
    public DecisionNodeTree (String name) throws CTBSystemException, IOException {
    	this.propsName = name;
    	ResourceBundle rb = ResourceBundle.getBundle(propsName);
    	
    	ArrayList keys = new ArrayList();
        Enumeration rbkeys = rb.getKeys();
        while(rbkeys.hasMoreElements()) {
            keys.add(rbkeys.nextElement());
        }
    	Collections.sort(keys);
    	Iterator iterator = keys.iterator();
    	System.out.println("+DecisionNodeTree.DecisionNodeTree(" + propsName + ")");
    	while(iterator.hasNext()) {
    		String key = (String) iterator.next();
    		String value = rb.getString(key);
    		System.out.println("  " + key + " : " + value);
    		List path = createPathFromString(value, '|');
    		String target = (String) path.get(path.size() - 1);
    		//System.out.println("  target = " + target);
    		populateTreeWithPath(path.subList(0, path.size() - 1), target, key);
    	}
    }
    
	//root-level retrieval call
	public Object getValueForPath(List decisionPath) {
		System.out.println("+DecisionNodeTree.getValueForPath(decisionPath)");
		System.out.print("  ");
		if(this.root != null) {
			return getValueForPath(this.root, decisionPath, this.propsName);
		} else {
			System.out.println("Tree not initialized: " + this.propsName + "\n");
			return null;
		}
	}
	
	//recursive retrieval call
	private static Object getValueForPath(DecisionNode localRoot, List decisionPath, String name) {
		if(localRoot == null) {
			System.out.print(": NO VALUE FOUND: " + name + "\n");
			return null;
		}else if(localRoot.leaf) {
			System.out.print(": " + localRoot.value + ": " + name + "\n");
			return localRoot.value;
		} else {
    		boolean nextDecision = ((Boolean) decisionPath.get(0)).booleanValue();
			if(nextDecision) {
				System.out.print("YES|");
				return getValueForPath(localRoot.yesFork, decisionPath.subList(1, decisionPath.size()), name);
			} else {
				System.out.print("NO |");
				return getValueForPath(localRoot.noFork, decisionPath.subList(1, decisionPath.size()), name);
			}
		}
	}
	
	//root-level set-up call
	private DecisionNode populateTreeWithPath(List decisionPath, Object value, String source) throws CTBSystemException {
		this.root = populateTreeWithPath(this.root, decisionPath, value, source);
		return this.root;
	}
	
	//recursive set-up call
	private static DecisionNode populateTreeWithPath(DecisionNode localRoot, List decisionPath, Object value, String source) throws CTBSystemException {
		//System.out.println("+DecisionNodeTree.populateTreeWithPath()");
		//System.out.println("  processing decisionPath size: " +  decisionPath.size());
		if(localRoot == null) localRoot = new DecisionNode(false, null);
		if(decisionPath.size() == 0) {
			if(localRoot.leaf && !localRoot.value.equals(value)) {
				String errorMessage = "  ERROR! Replacing leaf value " + localRoot.source + ": " + localRoot.value + "\n    with " + source + ": " + value + "\n";
				System.out.println(errorMessage);
				throw new SystemException(errorMessage);
			} else {
				//System.out.print(" : Creating leaf value " + value + "\n");
			}
			localRoot.leaf = true;
			localRoot.value = value;
			localRoot.source = source;
		} else {
			String nextDecision = (String) decisionPath.get(0);
			// System.out.print(" " + nextDecision );
			if(nextDecision.equals(YES) || nextDecision.equals(DONT_CARE)) {
				localRoot.yesFork = populateTreeWithPath(localRoot.yesFork, decisionPath.subList(1, decisionPath.size()), value, source);
			}
			if(nextDecision.equals(NO) || nextDecision.equals(DONT_CARE)) {
				localRoot.noFork = populateTreeWithPath(localRoot.noFork, decisionPath.subList(1, decisionPath.size()), value, source);
			}
		}
		return localRoot;
	}
	
	private static class DecisionNode {
		private boolean leaf;
		private Object value;
		private String source;
		private DecisionNode yesFork;
		private DecisionNode noFork;
		
		private DecisionNode(boolean leaf, Object value) {
			this.leaf = leaf;
			this.value = value;
		}
	}
	
	 // string tokenizer parses String into sub-string list
	private List createPathFromString( String data, char delimiter ) {
		int newLines = 0;
		//	 determine number of delimiter characters in String
		for ( int i = 0; i < data.length(); i++ ) {
			//	 increase number of delimiters by one
			if ( data.charAt( i ) == delimiter ) newLines++;
		}
		//	 create new String array
		ArrayList list = new ArrayList();
		int oldNewLineIndex = 0;
		int currentNewLineIndex;
		//	 store Strings into array based on demiliter
		//System.out.println("+DecisionNodeTree.createPathFromString(data, delimiter)");
		for ( int i = 0; i < newLines; i++ ) {
			//	 determine index where delimiter occurs
			currentNewLineIndex = data.indexOf( delimiter, oldNewLineIndex );
			//	 extract String within delimiter characters
			String token = data.substring( oldNewLineIndex, currentNewLineIndex );
			if(token == null || token.trim().equals("")) token = "-";
			//System.out.println("  token = " + token);
			list.add(token.trim());
			oldNewLineIndex = currentNewLineIndex + 1;
		}
		return list;
	}
}