package eit.searchengine.core;

import java.util.ArrayList;
import java.util.List;

public class Result {
	private String title;
	private String extract;
	private List<String> keywords;
	private String originAddress;
	private String localAddress;

	public Result() {
		keywords = new ArrayList<String>();
	}

	public String getOriginAddress() {
		return originAddress;
	}

	public void setOriginAddress(String originAddress) {
		this.originAddress = originAddress;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public String getExtract() {
		return extract;
	}

	public void setExtract(String extract) {
		this.extract = extract;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
