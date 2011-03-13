/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eit.searchengine.core;

import java.util.HashMap;

/**
 *
 * @author swop
 */
public class InfosWord {
	private String word;
	private HashMap<Integer, Integer> occurences;

	public InfosWord(String word) {
		this.word = word;
		occurences = new HashMap<Integer, Integer>();
	}

	public void setOccurence(int occurence, int documentId) {
		occurences.put(documentId, occurence);
	}
	
	public void addOccurence(int occurence, int documentId) {
		if(occurences.containsKey(documentId)) {
			occurences.put(documentId, occurence + occurences.get(documentId));
		} else {
			occurences.put(documentId, occurence);
		}
	}

	public int getOccurence(int documentId) {
		return occurences.get(documentId);
	}

	public String getWord() {
		return word;
	}

}
