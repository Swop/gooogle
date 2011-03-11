package eit.searchengine.core;

public class IndexedDataNotLoaded extends Exception {

    public IndexedDataNotLoaded() {
		super("No indexed data was loaded. \nPlease load data file before searching something...");
	}
}
