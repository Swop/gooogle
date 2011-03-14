package eit.searchengine.view;

import eit.searchengine.core.Result;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ResultsListCellRenderer implements ListCellRenderer {

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Result result = (Result)value;
		
		JPanel pan = new JPanel(new GridLayout(2, 1));
		pan.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		JLabel title = new JLabel("<html><font color=\"blue\">"+result.getTitle()+"</font></html>");
		pan.add(title);

		//JLabel extract = new JLabel(result.getExtract());
		//pan.add(extract);

		JLabel address = new JLabel("<html><font color=\"green\">"+result.getOriginAddress()+"</font></html>");
		pan.add(address);

		if(isSelected) {
			pan.setBackground(new Color(230, 230, 230));
			title.setFont( new Font( "Roman", Font.BOLD, 15 ) );
		} else {
			pan.setBackground(Color.WHITE);
			title.setFont( new Font( "Roman", Font.PLAIN, 15 ) );
		}
		
		return pan;
	}

}
