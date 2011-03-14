package eit.searchengine.view;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {
	private static MainWindow _instance = null;

	public static MainWindow getInstance() {
		if(_instance == null)
			_instance = new MainWindow();
		return _instance;
	}

	private SearchPanel searchPanel;
	private RobotPanel robotPanel;
	
	private MainWindow() {
		searchPanel = new SearchPanel();
		robotPanel = new RobotPanel();

		Container mainPane = this.getContentPane();
		JTabbedPane tabbed = new JTabbedPane();
		tabbed.insertTab("Search", null, searchPanel, "Search Panel", 0);
		tabbed.insertTab("Indexation", null, robotPanel, "Indexation Panel", 1);

		mainPane.setLayout(new BorderLayout());
		mainPane.add(tabbed, BorderLayout.CENTER);
	}

	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

	public RobotPanel getRobotPanel() {
		return robotPanel;
	}

	public void init() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.pack();
		this.setSize(1024, 868);
		this.setLocation(0,0);

		this.setVisible(true);
	}

	public boolean closeView() {
		this.setVisible(false);
		return true;
	}
}
