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
	private HashMap weights;

	public InfosWord(String word) {
		this.word = word;
		weights = new HashMap();
	}

	public void setWeight(double weight, int documentId) {
		weights.put(documentId, weight);
	}

	public double getWeight(int documentId) {
		return (Double)weights.get(documentId);
	}

	public String getWord() {
		return word;
	}

}
