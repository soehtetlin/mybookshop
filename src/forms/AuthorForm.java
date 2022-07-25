package forms;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entities.Author;
import entities.Publisher;
import services.AuthorService;
import services.PublisherService;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Color;

public class AuthorForm extends JPanel {
	private JButton btnSave, btnCancel, btnUpdate, btnDelete;
	private JTextField txtAuthorName;

	private Author author;
	private AuthorService authorService;
	private List<Author> originalAuthorList = new ArrayList<>();

	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	private DefaultTableModel dtm = new DefaultTableModel();
	private JScrollPane scrollPane;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public AuthorForm() {
		setBackground(Color.WHITE);

		authorService = new AuthorService();
		initialize();
		setTableDesign();
		loadAllAuthors(Optional.empty());
	}

	private void initialize() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 795, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
		);

		JLabel lblAuthorName = new JLabel("Author Name");
		cLayout.setLabel(lblAuthorName);

		txtAuthorName = new JTextField();
		cLayout.setTextField(txtAuthorName);

		btnSave = new JButton("Save");
		cLayout.setButton(btnSave);

		btnUpdate = new JButton("Update");
		cLayout.setButton(btnUpdate);
		btnUpdate.setVisible(false);

		btnDelete = new JButton("Delete");
		cLayout.setButton(btnDelete);
		btnDelete.setVisible(false);

		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(172, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblAuthorName)
							.addGap(72))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addGap(18)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addGap(36)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtAuthorName, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
					.addGap(157))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
					.addGap(3))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(33)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAuthorName)
						.addComponent(txtAuthorName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE))
		);
		
		table = new JTable();
		cLayout.setTable(table);
		
		scrollPane.setViewportView(table);
		
		panel.setLayout(gl_panel);
		setLayout(groupLayout);


		this.table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {

			if (!table.getSelectionModel().isSelectionEmpty()) {

				btnSave.setVisible(false);
				btnUpdate.setVisible(true);
				btnDelete.setVisible(true);

				String id = table.getValueAt(table.getSelectedRow(), 0).toString();

				author = authorService.findById(id);
				
				txtAuthorName.setText(author.getName());

			}

		});
		
		buttonOnClick();
	}

	private void setTableDesign() {

		dtm.addColumn("Author ID");
		dtm.addColumn("Author Name");
		
		this.table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Author ID", "Author Name" }));

		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);

		table.getColumnModel().getColumn(0).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(1).setCellRenderer(dfcr);

	}

	private void buttonVisible() {
		btnSave.setVisible(true);
		btnUpdate.setVisible(false);
		btnCancel.setVisible(true);
		btnDelete.setVisible(false);
	}

	private void loadAllAuthors(Optional<List<Author>> optionalAuthor) {
		
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalAuthorList = this.authorService.findAllAuthors();
		List<Author> authorList = optionalAuthor.orElseGet(() -> originalAuthorList);
		authorList.forEach(e -> {
			Object[] row = new Object[2];
			row[0] = e.getId();
			row[1] = e.getName();

			dtm.addRow(row);
		});

	}

	private void buttonOnClick() {

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				Author author = new Author();
				if (!txtAuthorName.getText().isBlank())
					author.setName(txtAuthorName.getText());
				else
					JOptionPane.showMessageDialog(null, "Enter Author Name");

				if (!author.getName().isBlank()) {

					authorService.saveAuthor(author);
					txtAuthorName.setText("");

					loadAllAuthors(Optional.empty());

					author = null;
				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");
				}
			}

		});

		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (author != null && author.getId() != null) {

					author.setName(txtAuthorName.getText());

					if (!author.getName().isBlank()) {

						authorService.updateAuthor(author.getId(), author);
						txtAuthorName.setText("");

						loadAllAuthors(Optional.empty());

						author = null;
					} else {
						JOptionPane.showMessageDialog(null, "Enter Required Field!");
					}
				}
				buttonVisible();
			}

		});

		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (author != null) {
					authorService.deleteAuthor(String.valueOf(author.getId()));
					txtAuthorName.setText("");
					buttonVisible();
					loadAllAuthors(Optional.empty());
					author = null;
				} else {
					JOptionPane.showMessageDialog(null, "Choose Author!");
				}

				buttonVisible();
			}

		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				txtAuthorName.setText("");
				loadAllAuthors(Optional.empty());
				buttonVisible();
			}

		});
	}
}
