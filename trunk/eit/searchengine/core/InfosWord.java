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
	private int nbDocsOccurences;
	private String word;
	private HashMap<Integer, Integer> occurences;
	private HashMap<Integer, Double> poids;

	public InfosWord(String word) {
		this.word = word;
		occurences = new HashMap<Integer, Integer>();
		poids = new HashMap<Integer, Double>();
		nbDocsOccurences = 0;
	}

	public void setPoids(int DocId, double poids) {
		this.poids.put(DocId, poids);
	}

	public double getPoids(int docId) {
		return poids.get(docId);
	}

	public int getNbDocsOccurences() {
		return nbDocsOccurences;
	}

	public void setNbDocsOccurences(int nbDocsOccurences) {
		this.nbDocsOccurences = nbDocsOccurences;
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
