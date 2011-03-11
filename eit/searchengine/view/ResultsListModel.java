package eit.searchengine.view;

import eit.searchengine.core.Result;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ResultsListModel implements ListModel {
	private List<Result> results;
	private List<ListDataListener> listeners;

	public ResultsListModel() {
		results = new ArrayList<Result>();
		listeners = new ArrayList<ListDataListener>();
	}

	public int getSize() {
		return results.size();
	}

	public Object getElementAt(int index) {
		return results.get(index);
	}

	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	public void setResults(List<Result> results) {
		int sizeBefore = this.results.size();
		this.results.clear();
		notifyEmptyList(sizeBefore);

		int sizeAfter = results.size();
		this.results = results;
		notifyFullList(sizeAfter);
	}

	public void emptyList() {
		setResults(new ArrayList<Result>());
	}

	private void notifyEmptyList(int sizeBefore) {
		for(ListDataListener l : listeners) {
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, sizeBefore);
			l.intervalRemoved(e);
		}
	}

	private void notifyFullList(int sizeAfter) {
		for(ListDataListener l : listeners) {
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, sizeAfter);
			l.intervalAdded(e);
		}
	}
}
