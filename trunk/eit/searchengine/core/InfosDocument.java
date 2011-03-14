/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eit.searchengine.core;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author swop
 */

public class InfosDocument  implements Serializable {
	private static int _cptInstances = 0;
	private int id;
	private String url;
	
	private ArrayList<String> liensVersAutresDocs;
	private ArrayList<String> liensPointantVersCeDoc;
	
	// Je sais pas trop ce qu'il faut mettre ici .. Nombre de mots dans le doc ?

	public static int getNewId() {
		_cptInstances++;
		return _cptInstances;
	}

	public InfosDocument() {
		this.liensVersAutresDocs = new ArrayList<String>();
		this.liensPointantVersCeDoc = new ArrayList<String>();
		this.id =  getNewId();
	}

	//public void addLienVersAutreDoc(String url);
	public int getId() {
		return id;
	}
	public void setUrl(String url){
		this.url= url;
	}
	
	public String getUrl(){
		return url;
	}
	
}
