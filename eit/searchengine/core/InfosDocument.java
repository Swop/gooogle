/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eit.searchengine.core;

/**
 *
 * @author swop
 */

public class InfosDocument {
	private static int _cptInstances = 0;
	private int id;
	
	// Je sais pas trop ce qu'il faut mettre ici .. Nombre de mots dans le doc ?

	public static int getNewId() {
		_cptInstances++;
		return _cptInstances;
	}

	public InfosDocument() {
		this.id =  getNewId();
	}

	public int getId() {
		return id;
	}
	
}
