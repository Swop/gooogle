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
	private String title;
	private String urlLocal;
	
	private ArrayList<String> liensVersAutresDocs;
	private ArrayList<String> liensPointantVersCeDoc;
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setUrlLocal(String urlLocal) {
		this.urlLocal = urlLocal;
	}
	
	public String getUrlLocal() {
		return urlLocal;
	}
	
	public void addLiensVersAutresDocs(String lien) {
		liensVersAutresDocs.add(lien);
	}

	public static int getNewId() {
		_cptInstances++;
		return _cptInstances;
	}

	public InfosDocument(String urlLocal) {
		this.liensVersAutresDocs = new ArrayList<String>();
		this.liensPointantVersCeDoc = new ArrayList<String>();
		this.id =  getNewId();
		this.urlLocal = urlLocal;
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
