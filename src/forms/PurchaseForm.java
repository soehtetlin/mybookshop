package forms;

import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PurchaseForm extends JPanel {
	private JTextField txtsearchbook;
	private JTable table;
	private JTable table_1;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the panel.
	 */
	public PurchaseForm() {
		initialize();
		this.setTableDesign();
		this.loadAllAuthors(Optional.empty());
	}

	private void loadAllAuthors(Optional<Object> empty) {
		// TODO Auto-generated method stub
		
	}

	private void setTableDesign() {
		// TODO Auto-generated method stub
		
	}

	private void initialize() {
		// TODO Auto-generated method stub
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setBounds(42, 11, 809, 450);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 11, 380, 428);
		add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 258, 380, 170);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setRowHeight(25);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column"
			}
		));
		scrollPane.setViewportView(table);
		
		JComboBox cboPublisher = new JComboBox();
		cboPublisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		cboPublisher.setBounds(10, 49, 100, 30);
		panel.add(cboPublisher);
		
		JComboBox cboCategory = new JComboBox();
		cboCategory.setBounds(121, 49, 100, 30);
		panel.add(cboCategory);
		
		JComboBox cboAuthor = new JComboBox();
		cboAuthor.setBounds(230, 49, 100, 30);
		panel.add(cboAuthor);
		
		JLabel lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPublisher.setBounds(10, 11, 100, 30);
		panel.add(lblPublisher);
		
		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(121, 11, 100, 30);
		panel.add(lblCategory);
		
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAuthor.setBounds(230, 8, 100, 30);
		panel.add(lblAuthor);
		
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(137, 90, 100, 30);
		panel.add(lblQuantity);
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		textField = new JTextField();
		textField.setBounds(270, 91, 100, 30);
		panel.add(textField);
		textField.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField.setColumns(10);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(137, 130, 100, 30);
		panel.add(lblPrice);
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		textField_1 = new JTextField();
		textField_1.setBounds(270, 131, 100, 30);
		panel.add(textField_1);
		textField_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField_1.setColumns(10);
		
		JLabel lblPrice_1 = new JLabel("CA00000008");
		lblPrice_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPrice_1.setBounds(10, 91, 122, 30);
		panel.add(lblPrice_1);
		
		JButton btnNewButton_1_1 = new JButton("Edit");
		btnNewButton_1_1.setBounds(137, 172, 100, 30);
		panel.add(btnNewButton_1_1);
		btnNewButton_1_1.setMargin(new Insets(2, 2, 2, 2));
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JButton btnNewButton_1_2 = new JButton("Remove");
		btnNewButton_1_2.setBounds(270, 172, 100, 30);
		panel.add(btnNewButton_1_2);
		btnNewButton_1_2.setMargin(new Insets(2, 2, 2, 2));
		btnNewButton_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JButton btnNewButton_1_1_1 = new JButton("Add Book");
		btnNewButton_1_1_1.setMargin(new Insets(2, 2, 2, 2));
		btnNewButton_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1_1_1.setBounds(10, 173, 100, 30);
		panel.add(btnNewButton_1_1_1);
		
		txtsearchbook = new JTextField();
		txtsearchbook.setBounds(10, 217, 227, 30);
		panel.add(txtsearchbook);
		txtsearchbook.setName("");
		txtsearchbook.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtsearchbook.setColumns(10);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setBounds(270, 217, 100, 30);
		panel.add(btnNewButton);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setMargin(new Insets(2, 2, 2, 2));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(400, 11, 399, 428);
		add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 399, 227);
		panel_1.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setRowHeight(25);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
				{"", "", "", "", ""},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"No", "Book Name", "Price", "Quantity", "Amount"
			}
		));
		scrollPane_1.setViewportView(table_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 238, 399, 190);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnSave = new JButton("Save");
		btnSave.setMargin(new Insets(2, 2, 2, 2));
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSave.setBounds(10, 118, 100, 30);
		panel_2.add(btnSave);
		
		JLabel lblTotalAmount = new JLabel("Total Amount");
		lblTotalAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotalAmount.setBounds(10, 64, 100, 30);
		panel_2.add(lblTotalAmount);
		
		JLabel lblTotalQuantity = new JLabel("Total Quantity");
		lblTotalQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotalQuantity.setBounds(10, 23, 100, 30);
		panel_2.add(lblTotalQuantity);
		
		JLabel lblTotalQuantity_1 = new JLabel("100");
		lblTotalQuantity_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTotalQuantity_1.setBounds(187, 23, 151, 30);
		panel_2.add(lblTotalQuantity_1);
		
		JLabel lblTotalQuantity_2 = new JLabel("10000000000");
		lblTotalQuantity_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTotalQuantity_2.setBounds(187, 64, 127, 30);
		panel_2.add(lblTotalQuantity_2);
		
		JLabel lblTotalQuantity_1_1 = new JLabel("Kyats");
		lblTotalQuantity_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalQuantity_1_1.setBounds(319, 64, 80, 30);
		panel_2.add(lblTotalQuantity_1_1);
	}
}
