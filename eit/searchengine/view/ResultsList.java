package eit.searchengine.view;

import java.awt.Dimension;
import javax.swing.JList;

public class ResultsList extends JList {
	public ResultsList() {
		super();

		this.setModel(new ResultsListModel());
		this.setCellRenderer(new ResultsListCellRenderer());
		this.setPreferredSize(new Dimension(500, 500));
		this.setMinimumSize(new Dimension(500, 500));
	}
}
