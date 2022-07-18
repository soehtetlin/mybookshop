package forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class CreateLayoutProperties {

	public void setToggleButton(JToggleButton jtButton) {
		jtButton.setForeground(SystemColor.desktop);
		jtButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		jtButton.setOpaque(false);
		jtButton.setFocusPainted(false);
		jtButton.setBorderPainted(false);
		jtButton.setContentAreaFilled(false);

	}

	public void setTextField(JTextField textField) {
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField.setColumns(10);
	}

	public void setComboBox(JComboBox<String> comboBox) {

		comboBox.setForeground(new Color(153, 0, 255));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBox.setEditable(false);
		comboBox.setBorder(null);
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setOpaque(true);
	}

	public void setButton(JButton button) {
		button.setBounds(new Rectangle(600, 0, 0, 0));
//		button.setMargin(new Insets(2, 1, 2, 1));
//		button.setIconTextGap(1);
		button.setBorderPainted(false);
		button.setForeground(SystemColor.textHighlightText);
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setBackground(new Color(153, 51, 204));
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setOpaque(true);
		button.setForeground(Color.WHITE);
		button.setBorder(new LineBorder(new Color(0, 0, 0)));
		

		// Image img=new
		// ImageIcon(this.getClass().getResource("/add-20.png")).getImage();
		// button.setIcon(new ImageIcon(img));
		// button.setAlignmentX(CENTER_ALIGNMENT);
		// button.setBounds(600, 13, 115, 29);
		// button.setHorizontalAlignment(SwingConstants.RIGHT);

	}

	public void setLabel(JLabel label) {
		label.setForeground(new Color(51, 51, 51));
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
	}
	
	public void setTable(JTable table) {
		table.setSelectionBackground(new Color(153, 51, 255));
		table.setShowVerticalLines(false);
		table.setFocusable(false);
		table.setBounds(12, 254, 404, -216);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultEditor(Object.class, null);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(153, 51, 204));
		table.getTableHeader().setForeground(new Color(245, 245, 245));
		table.getTableHeader().setPreferredSize(new Dimension(100, 40));
		table.setRowHeight(120);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
	}

}
