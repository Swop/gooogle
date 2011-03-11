package eit.searchengine.core;

public class DeserializationException extends Exception {
	public DeserializationException() {
		super("An error happened during data loading.\n\nDo you have enought permitions to read the file ?\nIs it a correct indexed data file ?");
	}
}
