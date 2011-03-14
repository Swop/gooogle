/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eit.searchengine.core;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author swop
 */
public class InfosWord implements Serializable {
	private int nbDocsOccurences;
	private String word;
	private HashMap<Integer, Integer> occurences;
	private HashMap<Integer, Float> poids;

	public InfosWord(String word) {
		this.word = word;
		occurences = new HashMap<Integer, Integer>();
		poids = new HashMap<Integer, Float>();
		nbDocsOccurences = 0;
	}

	public void setPoids(int DocId, float poids) {
		this.poids.put(DocId, poids);
	}

	public float getPoids(int docId) {
		Float p = poids.get(docId);
		if(p == null)
			return 0;
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
			++nbDocsOccurences;
		}
	}

	public int getOccurence(int documentId) {
		if(occurences.containsKey(documentId))
			return occurences.get(documentId);
		else
			return 0;
	}

	public String getWord() {
		return word;
	}

	public void clearOccurenceData() {
		occurences = null;
	}

}
