package eit.searchengine.view;

import eit.searchengine.controller.Controller;
import eit.searchengine.core.DeserializationException;
import eit.searchengine.core.IndexedDataNotLoaded;
import eit.searchengine.core.Result;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;

import org.annolab.tt4j.TreeTaggerException;

public class SearchPanel extends Container implements ActionListener, ListSelectionListener, KeyListener, ItemListener {

	private boolean instantSearchActivated = true;
	private boolean ttActivated = true;

	private boolean enterKeyAllreadyPressed = false;

	private JTextField _searchField;
	private ResultsList _resultsList;
	private JButton _launchSearchButton;
	private JTextArea _documentViewer;
	private JButton _chooseIndexedDataFileButton;
	private JLabel _chooseIndexedDataFileLabel;
	private JCheckBox _activateInstantSearchCheckbox;
	private JCheckBox _activateTTCheckbox;
	private JLabel _searchSpinner;

	public SearchPanel() {
		this.setLayout(new BorderLayout());

		JPanel leftPane = new JPanel(new BorderLayout());

		JPanel upPanel = new JPanel(new GridLayout(2, 1, 5, 5));

		JPanel indexedDataFilePanel = new JPanel(new BorderLayout());
		indexedDataFilePanel.setBorder(BorderFactory.createTitledBorder("Indexed data file"));
		_chooseIndexedDataFileLabel = new JLabel();
		_chooseIndexedDataFileLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		indexedDataFilePanel.add(_chooseIndexedDataFileLabel, BorderLayout.CENTER);
		_chooseIndexedDataFileButton = new JButton("Browse...");
		_chooseIndexedDataFileButton.addActionListener(this);
		indexedDataFilePanel.add(_chooseIndexedDataFileButton, BorderLayout.EAST);

		upPanel.add(indexedDataFilePanel);

		JPanel searchFromPanel = new JPanel(new BorderLayout());
		_searchField = new JTextField();
		_searchField.addKeyListener(this);
		searchFromPanel.setBorder(BorderFactory.createTitledBorder("Search form"));
		searchFromPanel.add(_searchField, BorderLayout.CENTER);
		_launchSearchButton = new JButton("Search!");
		_launchSearchButton.addActionListener(this);
		searchFromPanel.add(_launchSearchButton, BorderLayout.EAST);
		
		JPanel InstantSearchPanel = new JPanel(new BorderLayout());
		_searchSpinner = new JLabel("");
		InstantSearchPanel.add(_searchSpinner, BorderLayout.WEST);
		_activateTTCheckbox = new JCheckBox("Use tree-tagger", true);
		_activateTTCheckbox.addItemListener(this);
		InstantSearchPanel.add(_activateTTCheckbox, BorderLayout.CENTER);
		_activateInstantSearchCheckbox = new JCheckBox("Instant Search", true);
		_activateInstantSearchCheckbox.addItemListener(this);
		InstantSearchPanel.add(_activateInstantSearchCheckbox, BorderLayout.EAST);

		searchFromPanel.add(InstantSearchPanel, BorderLayout.SOUTH);

		upPanel.add(searchFromPanel);

		leftPane.add(upPanel, BorderLayout.NORTH);

		JPanel titlePan = new JPanel(new BorderLayout());
		titlePan.setBorder(BorderFactory.createTitledBorder("Results"));

		_resultsList = new ResultsList();
		_resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_resultsList.addListSelectionListener(this);
		JScrollPane scroll = new JScrollPane(_resultsList);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		titlePan.add(scroll);

		leftPane.add(titlePan, BorderLayout.CENTER);
		leftPane.setPreferredSize(new Dimension(500, 500));

		_documentViewer = new JTextArea();
		_documentViewer.setEditable(false);
		clearDocumentViewer();

		JPanel rightPane = new JPanel(new BorderLayout());
		rightPane.setBorder(BorderFactory.createTitledBorder("Document"));
		JScrollPane scrollDocument = new JScrollPane(_documentViewer);
		rightPane.add(scrollDocument, BorderLayout.CENTER);

		this.add(leftPane, BorderLayout.WEST);
		this.add(rightPane, BorderLayout.CENTER);

	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(_launchSearchButton))
			makeSearch();
		else if(e.getSource().equals(_chooseIndexedDataFileButton)) {
			String wd = System.getProperty("user.dir");
			JFileChooser fc = new JFileChooser(wd);
			fc.setMultiSelectionEnabled(false);

			this.setEnabled(false);
			_chooseIndexedDataFileLabel.setText("Loading file...");

			int returnVal = fc.showOpenDialog(MainWindow.getInstance());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					Controller.getInstance().getModel().loadIndexedData(fc.getSelectedFile());
					_chooseIndexedDataFileLabel.setText(fc.getSelectedFile().getName());
					ResultsListModel model = (ResultsListModel) _resultsList.getModel();
					model.emptyList();
					this.setEnabled(true);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(MainWindow.getInstance(),
					ex.getMessage(),
					"Warning",
					JOptionPane.WARNING_MESSAGE);
					_chooseIndexedDataFileLabel.setText("");
					this.setEnabled(true);
				} catch (DeserializationException ex) {
					JOptionPane.showMessageDialog(MainWindow.getInstance(),
					ex.getMessage(),
					"Warning",
					JOptionPane.WARNING_MESSAGE);
					_chooseIndexedDataFileLabel.setText("");
					this.setEnabled(true);
				}
			}
		}
	}

	private void loadArticle(String address) {
		InputStream ips = null;
		try {
			//TODO ovrir document et afficher texte dans _documentViewer
			File file = new File(address);
			String fileContent = "";

			ips = new FileInputStream(file);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);

			String line;
			while ((line = br.readLine()) != null) {
				fileContent += line + "\n";
			}
			br.close();
			_documentViewer.setText(fileContent);
			_documentViewer.setEnabled(true);
		} catch (Exception ex) {
		}
	}

	private void clearDocumentViewer() {
		_documentViewer.setText("");
		_documentViewer.setEnabled(false);
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = _resultsList.getSelectedIndex();
		if (index == -1) {
			clearDocumentViewer();
		} else {
			Result r = (Result) _resultsList.getModel().getElementAt(index);
			loadArticle(r.getLocalAddress());
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if(enterKeyAllreadyPressed) {
			enterKeyAllreadyPressed = false;
			return;
		}
		if(!this.instantSearchActivated ||
				!Controller.getInstance().getModel().isIndexedDataLoaded()) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				enterKeyAllreadyPressed = true;
				makeSearch();
			}
		} else {
			makeSearch();
		}
	}

	private void makeSearch() {
		clearDocumentViewer();

		if(_searchField.getText().length() == 0) {
			ResultsListModel model = (ResultsListModel) _resultsList.getModel();
			model.emptyList();
			return;
		}
		List<Result> lst;
		this._searchSpinner.setText("Searching...");
		try {
			Controller.getInstance().getModel().search(_searchField.getText(), ttActivated);
		} catch (IndexedDataNotLoaded ex) {
			this._searchSpinner.setText("");
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					ex.getMessage(),
					"Warning",
					JOptionPane.WARNING_MESSAGE);
		} catch (IOException e) {
			this._searchSpinner.setText("");
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					e.getMessage(),
					"Warning",
					JOptionPane.WARNING_MESSAGE);
		} catch (TreeTaggerException e) {
			this._searchSpinner.setText("");
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					e.getMessage(),
					"Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object src = e.getItemSelectable();
		if(src.equals(_activateInstantSearchCheckbox)) {
			instantSearchActivated = _activateInstantSearchCheckbox.isSelected();
		} else if(src.equals(_activateTTCheckbox)) {
			ttActivated = _activateTTCheckbox.isSelected();
		}
	}

	public void endSearching(List<Result> lst) {
		ResultsListModel model = (ResultsListModel) _resultsList.getModel();

		model.setResults(lst);
		this._searchSpinner.setText(model.getSize()+" result(s)");
	}
}
