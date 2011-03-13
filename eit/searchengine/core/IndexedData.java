package eit.searchengine.core;

import eit.searchengine.view.MainWindow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	public void search(List<String> keywords) {
		Thread t = new Thread() {

			@Override
			public void run() {
				List<Result> results = new ArrayList<Result>();

				// **** A SUPPRIMER APRES ******
				try {
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
				// *****************************

				MainWindow.getInstance().getSearchPanel().endSearching(results);
			}
		};

		t.start();
	}

	static IndexedData indexData(File pathToCorpusFile) {
		IndexedData data = new IndexedData();

		//TODO indexationnnnnnnnnnnn
		data.initData(pathToCorpusFile);
		

		return data;
	}

	public void initData(File pathToCorpusFile) {
		File lemmesDir = new File(pathToCorpusFile.getAbsolutePath() + "/lemmes_seulement");
		if(lemmesDir.canRead()) {
			analyzeDir(lemmesDir);
		}
	}

	private void analyzeDir(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				MainWindow.getInstance().getRobotPanel().log("Enter '' directory");
				analyzeDir(f);
			} else {
				if (f.getName().endsWith("lemmas.txt")) {
					MainWindow.getInstance().getRobotPanel().log("Analyse "+f.getName());
					InfosDocument docInfos = new InfosDocument();
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
						String line;
						while ((line = br.readLine()) != null) {
							StringTokenizer st = new StringTokenizer(line);
							while (st.hasMoreTokens()) {
								String word = st.nextToken();
								InfosWord wdInfos;
								if(this.words.containsKey(word))
									wdInfos = words.get(word);
								else {
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
	}
}