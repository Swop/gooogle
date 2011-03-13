package eit.searchengine.view;

import eit.searchengine.controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class RobotPanel extends Container implements ActionListener {

	private JTextArea _logViewer;
	private JButton _launchRobotButton;
	private JButton _chooseCorpusFolderButton;
	private JLabel _chooseCorpusFolderLabel;
	private JButton _chooseOutputFileButton;
	private JLabel _chooseOutputFileLabel;

	public RobotPanel() {
		this.setLayout(new BorderLayout());

		JPanel upPanel = new JPanel(new GridLayout(3, 1, 5, 5));

		JPanel chooseCorpusFolderPanel = new JPanel(new BorderLayout());
		chooseCorpusFolderPanel.add(new JLabel("Corpus Folder :"), BorderLayout.WEST);
		_chooseCorpusFolderLabel = new JLabel();
		_chooseCorpusFolderLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		chooseCorpusFolderPanel.add(_chooseCorpusFolderLabel, BorderLayout.CENTER);
		_chooseCorpusFolderButton = new JButton("Browse...");
		_chooseCorpusFolderButton.addActionListener(this);
		chooseCorpusFolderPanel.add(_chooseCorpusFolderButton, BorderLayout.EAST);

		JPanel chooseOutputFilePanel = new JPanel(new BorderLayout());
		chooseOutputFilePanel.add(new JLabel("Output File :"), BorderLayout.WEST);
		_chooseOutputFileLabel = new JLabel();
		_chooseOutputFileLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		chooseOutputFilePanel.add(_chooseOutputFileLabel, BorderLayout.CENTER);
		_chooseOutputFileButton = new JButton("Browse...");
		_chooseOutputFileButton.addActionListener(this);
		chooseOutputFilePanel.add(_chooseOutputFileButton, BorderLayout.EAST);


		_launchRobotButton = new JButton("Launch indexation");
		_launchRobotButton.addActionListener(this);

		upPanel.add(chooseCorpusFolderPanel);
		upPanel.add(chooseOutputFilePanel);
		upPanel.add(_launchRobotButton);

		_logViewer = new JTextArea();
		JScrollPane scroll = new JScrollPane(_logViewer);

		this.add(upPanel, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(_chooseCorpusFolderButton)) {
			String wd = System.getProperty("user.dir");
			JFileChooser fc = new JFileChooser(wd);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					return false;
				}

				@Override
				public String getDescription() {
					return "Directory";
				}
			});
			fc.setMultiSelectionEnabled(false);


			//int returnVal = fc.showDialog(MainWindow.getInstance(), "Open");
			int returnVal = fc.showOpenDialog(MainWindow.getInstance());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				_chooseCorpusFolderLabel.setText(fc.getSelectedFile().getAbsolutePath());
			}
		} else if (e.getSource().equals(_chooseOutputFileButton)) {
			String wd = System.getProperty("user.dir");
			JFileChooser fc = new JFileChooser(wd);
			//fc.setAcceptAllFileFilterUsed(true);
			fc.setMultiSelectionEnabled(false);

			int returnVal = fc.showSaveDialog(MainWindow.getInstance());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				_chooseOutputFileLabel.setText(fc.getSelectedFile().getAbsolutePath());
			}
		} else if (e.getSource().equals(_launchRobotButton)) {
			try {
				Controller.getInstance().getModel().indexation(_chooseCorpusFolderLabel.getText(), _chooseOutputFileLabel.getText());
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(MainWindow.getInstance(),
					ex.getMessage(),
					"Warning",
					JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void log(String txt) {
		_logViewer.append("\n" + txt);
	}

	public void clearLog() {
		_logViewer.setText("");
	}

	public void setLaunchButtonEnable(boolean state) {
		if(!state)
			_launchRobotButton.setEnabled(false);
		else
			_launchRobotButton.setEnabled(true);
	}
}
