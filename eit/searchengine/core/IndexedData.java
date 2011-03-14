package eit.searchengine.core;

import eit.searchengine.controller.Controller;
import eit.searchengine.view.MainWindow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexedData implements Serializable {

	private static final long serialVersionUID = 1L;
	//TODO Stocker toutes les infos de l'indexation ici
	private HashMap<String, InfosWord> words;
	private HashMap<Integer, InfosDocument> documents;

	//Associe un id de fichier a une url
	private HashMap<String,Integer> url;
	
	
	private IndexedData() {
		// Divers champs ... a voir ce qu'on stoke (occurences, lemmes, etc ...)
		words = new HashMap<String, InfosWord>();
		documents = new HashMap<Integer, InfosDocument>();
		url = new HashMap<String,Integer>();
	}

	private void addDocument(InfosDocument doc) {
		documents.put(doc.getId(), doc);
	}

	private void addNewWord(InfosWord word) {
		words.put(word.getWord(), word);
	}

	public void search(final List<String> keywords) {
		final IndexedData me = this;
		
		Thread t = new Thread() {

			@Override
			public void run() {
				List<Result> results = new ArrayList<Result>();

				List<Entry<Integer, Float>> scores = IndexedData.calculScore(me, keywords);

				float scoreMax = scores.get(0).getValue();
				float seuil = scoreMax*6/10;
				
				for (final Entry<Integer, Float> entry : scores) {
					int docId = entry.getKey();
					float score = entry.getValue();
					if(score < seuil)
						break;
					
					InfosDocument infosDoc = me.documents.get(docId);
					
					//infosDoc.

					Result res = new Result();
					res.setTitle("Super titre");
					res.setOriginAddress("http://www.google.com/article");
					res.setLocalAddress("/Users/swop/Desktop/page.html");
					res.setExtract("Voilà un super extrait du titre qui reflète bien la personnalité de notre auteur.zfzefzef ezf zff ezfze f zefzefzefz ezfze fezef");
					res.getKeywords().add("titre");

					results.add(res);
				}

				// **** A SUPPRIMER APRES ******
				/*try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
				}

				Result res = new Result();
				res.setTitle("Super titre");
				res.setOriginAddress("http://www.google.com/article");
				res.setLocalAddress("/Users/swop/Desktop/page.html");
				res.setExtract("Voilà un super extrait du titre qui reflète bien la personnalité de notre auteur.zfzefzef ezf zff ezfze f zefzefzefz ezfze fezef");
				res.getKeywords().add("titre");

				results.add(res);
				// ******************************/

				MainWindow.getInstance().getSearchPanel().endSearching(results);
			}
		};

		t.start();
	}

	public static void indexData(final File pathToCorpusFile) {
		Thread t = new Thread() {

			@Override
			public void run() {

				IndexedData data = new IndexedData();
				data.initData(pathToCorpusFile);
				data.calculPoid();

				data.clearUnusedData();

				MainWindow.getInstance().getRobotPanel().log("----- Done! -----");

				Controller.getInstance().getModel().finishedIndexing(data);
				}
		};

		t.start();
	}

	public void initData(File pathToCorpusFile) {
		File lemmesDir = new File(pathToCorpusFile.getAbsolutePath() + "/lemmes_seulement");
		if (lemmesDir.canRead()) {
			analyzeDir(lemmesDir);
		}
	}

	private void analyzeDir(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				MainWindow.getInstance().getRobotPanel().log("Enter '' directory");
				analyzeDir(f);
			} else {
				//Ouverture du fichier
				if (f.getName().endsWith("lemmas.txt")) {
					
					
					

					MainWindow.getInstance().getRobotPanel().log("Analyse "+f.getName());
					InfosDocument docInfos = new InfosDocument();
					try {
						
						
						
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
						String line;

						//Lecture ligne par ligne
						while ((line = br.readLine()) != null) {
							
							StringTokenizer st = new StringTokenizer(line);

							//Decoupage de la ligne en tokens
							while (st.hasMoreTokens()) {
								String word = st.nextToken();
								InfosWord wdInfos;

								//Soit le mot est deja dans la hashmap
								if (this.words.containsKey(word)) {
									wdInfos = words.get(word);
								} else { //Soit il n'y ait pas et on l'ajoute

									wdInfos = new InfosWord(word);
									this.addNewWord(wdInfos);
								}
								wdInfos.addOccurence(1, docInfos.getId());
							}
						}
						br.close();
						this.addDocument(docInfos);
						
						//********On ouvre le meme fichier mais html
						String html = f.getAbsolutePath().replaceFirst("lemmes_seulement", "html");
						html = html.replaceFirst("-lemmas.txt", ".html");
						
						File htmlFile = new File (html);
						
						try{
							br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile)));
							line = "";
							String urlTmp= "";
							if((line = br.readLine()) != "") {						
								urlTmp = line.replaceFirst("<base href=\"", "");
								urlTmp = urlTmp.replaceFirst("\">", "");
								//System.out.println(urlTmp);
								
								//On ajoute l'url au document courant
								docInfos.setUrl(urlTmp);
								this.url.put(urlTmp, docInfos.getId());
							}
							while((line = br.readLine()) !=  null){
								Pattern p = Pattern.compile("(<a (href)*)"); 
								Pattern p2 = Pattern.compile("href=\"(.*)\""); 
								
								Matcher m = p.matcher(line); 
							//	Matcher m2 = p2.matcher(m.group(0));
								
								System.out.println(m.group(0));
							}
							
							br.close();
							
						}
						catch(Exception e){
							//System.out.println(e.toString());
						}	
						
						
						

					} catch (Exception e) {
						System.out.println(e.toString());
					}
					
				}
			}
		}
	}

	public void calculPoid() {
		int nbDocs = documents.size();
		int nbWords = words.size();
		float poids;
		int cpt = 1;
		for (String word : words.keySet()) {
			float pourc = (float)cpt/(float)nbWords;
			if(cpt % 200 == 0)
				MainWindow.getInstance().getRobotPanel().log("Compute word weight ...("+Math.floor(pourc*10000)/100+" %)");
			InfosWord infos = words.get(word);
			for (int docId : documents.keySet()) {
				poids = (float)(infos.getOccurence(docId) * Math.log(nbDocs / infos.getNbDocsOccurences()));
				if(poids != 0)
					infos.setPoids(docId, poids);
			}
			cpt++;
		}
		MainWindow.getInstance().getRobotPanel().log("Compute word weight ...(100.00 %)");
	}

	public static List<Entry<Integer, Float>> calculScore(IndexedData data, List<String> requestWords) {
		HashMap<Integer, Float> scores = new HashMap<Integer, Float>();
		//sim(dj, dk) = somme( poid(i,j) * poid(i,k) ) / ( racine( somme(w(i,j)² ) ) * racine( somme(w(i,k)²) )

		for (int docId : data.documents.keySet()) {
			InfosDocument infosDoc = data.documents.get(docId);
			HashMap<String, InfosWord> words = data.words;

			float somme = 0;
			for (String word : requestWords) {
				InfosWord infos = words.get(word);
				if (infos != null) {
					somme += infos.getPoids(docId);
				}
			}
			scores.put(docId, somme);
		}

		final List<Entry<Integer, Float>> entries = new ArrayList<Entry<Integer, Float>>(scores.entrySet());

		Collections.sort(entries, new Comparator<Entry<Integer, Float>>() {

			public int compare(final Entry<Integer, Float> e1, final Entry<Integer, Float> e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		});

		Collections.reverse(entries);

		/*float score = entries.get(0).getValue();
		float seuil = score*6/10;
		int index = -1;

		for(Entry<Integer, Float> e : entries) {
			index++;
			if(e.getValue() > seuil)
				continue;
			break;
		}

		while(true) {
			try {
				entries.remove(index);
				index++;
			} catch(IndexOutOfBoundsException e) {
				break;
			}
		}*/
		
		return entries;
	}

	private void clearUnusedData() {
		for (String word : words.keySet()) {
			InfosWord infosWord = words.get(word);
			infosWord.clearOccurenceData();
		}
	}
}
