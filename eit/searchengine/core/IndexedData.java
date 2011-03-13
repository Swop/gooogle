package eit.searchengine.core;

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

public class IndexedData implements Serializable {

	private static final long serialVersionUID = 1L;
	//TODO Stocker toutes les infos de l'indexation ici
	private HashMap<String, InfosWord> words;
	private HashMap<Integer, InfosDocument> documents;

	private IndexedData() {
		// Divers champs ... a voir ce qu'on stoke (occurences, lemmes, etc ...)
		words = new HashMap<String, InfosWord>();
		documents = new HashMap<Integer, InfosDocument>();
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

				List<Entry<Integer, Double>> scores = IndexedData.calculScore(me, keywords);
				
				for (final Entry<Integer, Double> entry : scores) {
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

	static IndexedData indexData(File pathToCorpusFile) {
		IndexedData data = new IndexedData();
		data.initData(pathToCorpusFile);
		//TODO indexationnnnnnnnnnnn



		return data;
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
					MainWindow.getInstance().getRobotPanel().log("Analyse " + f.getName());
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

					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		}
	}

	public void calculPoid() {
		int nbDocs = documents.size();
		for (String word : words.keySet()) {
			InfosWord infos = words.get(word);
			for (int docId : documents.keySet()) {
				double poids = infos.getOccurence(docId) * Math.log(nbDocs / infos.getNbDocsOccurences());
				infos.setPoids(docId, poids);
			}
		}
	}

	public static List<Entry<Integer, Double>> calculScore(IndexedData data, List<String> requestWords) {
		HashMap<Integer, Double> scores = new HashMap<Integer, Double>();
		//sim(dj, dk) = somme( poid(i,j) * poid(i,k) ) / ( racine( somme(w(i,j)² ) ) * racine( somme(w(i,k)²) )

		for (int docId : data.documents.keySet()) {
			InfosDocument infosDoc = data.documents.get(docId);
			HashMap<String, InfosWord> words = data.words;

			double somme = 0;
			for (String word : requestWords) {
				InfosWord infos = words.get(word);
				if (infos != null) {
					somme += infos.getPoids(docId);
				}
			}
			scores.put(docId, somme);
		}

		final List<Entry<Integer, Double>> entries = new ArrayList<Entry<Integer, Double>>(scores.entrySet());

		Collections.sort(entries, new Comparator<Entry<Integer, Double>>() {

			public int compare(final Entry<Integer, Double> e1, final Entry<Integer, Double> e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		});

		Collections.reverse(entries);

		return entries;
	}
}
