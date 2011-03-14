package eit.searchengine.core;

import eit.searchengine.view.MainWindow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.annolab.tt4j.TokenAdapter;
import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

public class Model {

	private IndexedData data = null;
	private String outputFilePath;
	private TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<String>();
	
	public boolean closeModel() {
		return true;
	}

	public void search(String text) throws IndexedDataNotLoaded, IOException, TreeTaggerException {
		if (data == null) {
			throw new IndexedDataNotLoaded();
		}
		List<String> keywords = new ArrayList<String>();

	
		System.setProperty("treetagger.home", "./TreeTagger");
		// TODO : faire un test sur l'os
//		tt.setModel("/french-par-linux-3.2-utf8.bin");
		tt.setModel("/tag-french.bat");
		tt.setHandler(new TokenHandler<String>() {
		    public void token(String token, String pos, String lemma) {
		    	System.out.println(token+"\t"+pos+"\t"+lemma);
			}
		});
/*		tt.setAdapter(new TokenAdapter<String>() {
			public String getText(String e) {
				return e;
			}
		});
*/		StringTokenizer st = new StringTokenizer(text);
		while (st.hasMoreTokens()) {
			keywords.add(st.nextToken());
		}
		tt.process(keywords);
		
		// T0D0 : lecture de tt a passer a search
		data.search(keywords);
	}

	public void indexation(String pathToCorpus, String outputFilePath) throws IOException {
		File pathToCorpusFile = new File(pathToCorpus);
		File outputFilePathFile = new File(outputFilePath);

		checkPrivilege(pathToCorpusFile, outputFilePathFile);
		MainWindow.getInstance().getRobotPanel().setLaunchButtonEnable(false);
		MainWindow.getInstance().getRobotPanel().clearLog();
		this.outputFilePath = outputFilePath;

		IndexedData.indexData(pathToCorpusFile);
	}

	public void finishedIndexing(IndexedData data) {
		//this.data = data;
		MainWindow.getInstance().getRobotPanel().log("Save indexed data to file...");
		saveIndexedData(new File(outputFilePath));
		MainWindow.getInstance().getRobotPanel().log("Done!");
		MainWindow.getInstance().getRobotPanel().setLaunchButtonEnable(true);
	}

	public void loadIndexedData(File pathToBinaryFile) throws IOException, DeserializationException {
		data = null;
		try {
			FileInputStream fis = new FileInputStream(pathToBinaryFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
				data = (IndexedData) ois.readObject();
				ois.close();
				fis.close();
		/*} catch (IOException ioe) {
			throw new IOException("Can't read selected file!");
		*/} catch (Exception ex) {
			throw new DeserializationException();
		}
	}

	private void saveIndexedData(File outputFilePathFile) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = new FileOutputStream(outputFilePathFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void checkPrivilege(File pathToCorpusFile, File outputFilePathFile) throws IOException {

		if (!pathToCorpusFile.canRead() || !pathToCorpusFile.canRead()) {
			throw new IOException("Can't read into the corpus folder!");
		}


		if ((outputFilePathFile.exists() && !outputFilePathFile.canWrite()) || !outputFilePathFile.getParentFile().canWrite()) {
			throw new IOException("Can't write into the output file!");
		}
	}

	public boolean isIndexedDataLoaded() {
		return data != null;
	}
}
