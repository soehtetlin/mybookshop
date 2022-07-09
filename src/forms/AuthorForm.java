package forms;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import entities.Author;
import services.AuthorService;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class AuthorForm extends JPanel {

	private JTextField txtName, txtSearch;
	private JTable tblshowAuthor;
	private JLabel lblAuthorId, lblAu;
	private Author author;
	private AuthorService auService = new AuthorService();
	private DefaultTableModel dtm = new DefaultTableModel();
	private List<Author> originalAuthorList = new ArrayList<>();

	/**
	 * Create the panel.
	 */

	public AuthorForm() {
		initialize();
		this.setTableDesign();
		this.loadAllAuthors(Optional.empty());
	}

	private void loadAllAuthors(Optional<List<Author>> optionalAuthors) {
		this.dtm = (DefaultTableModel) this.tblshowAuthor.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalAuthorList = this.auService.findAllAuthors();
		List<Author> authors = optionalAuthors.orElseGet(() -> originalAuthorList);

		authors.forEach(b -> {
			Object[] row = new Object[2];
			row[0] = b.getId();
			row[1] = b.getName();

			dtm.addRow(row);
		});

		tblshowAuthor.setModel(dtm);

	}

	private void setTableDesign() {
		dtm.addColumn("Author ID");
		dtm.addColumn("Author Name");
		tblshowAuthor.setModel(dtm);

	}

	private void resetFormData() {
		txtName.setText("");
	}

	private void initialize() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(null);
		setBounds(42, 11, 809, 450);

		JLabel lblAuthorName = new JLabel("Author Name");
		lblAuthorName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAuthorName.setBounds(10, 52, 116, 39);
		add(lblAuthorName);

		txtName = new JTextField();
		txtName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (null != author && !author.getId().isBlank()) {
					author.setName(txtName.getText());
					if (!author.getName().isBlank()) {
						auService.updateAuthor(String.valueOf(author.getId()), author);
						resetFormData();
						loadAllAuthors(Optional.empty());
						author = null;
					} else {
						JOptionPane.showMessageDialog(null, "Please enter Author Name!");
					}
				} else {
					Author author = new Author();
					author.setName(txtName.getText());
					if (null != author.getName() && !author.getName().isBlank()) {

						auService.saveAuthor(author);
						resetFormData();
						loadAllAuthors(Optional.empty());
					} else {
						JOptionPane.showMessageDialog(null, "Enter Required Field!");
					}
				}

			}
		});
		txtName.setBounds(136, 52, 222, 39);
		add(txtName);
		txtName.setColumns(10);

		lblAuthorId = new JLabel("Author ID");
		lblAuthorId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAuthorId.setBounds(10, 2, 116, 39);
		add(lblAuthorId);

		lblAu = new JLabel("Show Author ID");
		lblAu.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAu.setBounds(136, 2, 116, 39);
		add(lblAu);

		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSearch.setBounds(422, 9, 62, 39);
		add(lblSearch);

		txtSearch = new JTextField();
		txtSearch.setColumns(10);
		txtSearch.setBounds(484, 11, 173, 39);
		add(txtSearch);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
					Author author = new Author();
					author.setName(txtName.getText());
					if (null != author.getName() && !author.getName().isBlank()) {

						auService.saveAuthor(author);
						resetFormData();
						loadAllAuthors(Optional.empty());
					} else {
						JOptionPane.showMessageDialog(null, "Enter Required Field!");
					
				}
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSave.setBounds(10, 102, 116, 41);
		add(btnSave);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (null != author) {
					auService.deleteAuthor(author.getId() + "");
					resetFormData();
					loadAllAuthors(Optional.empty());
					author = null;
				} else {
					JOptionPane.showMessageDialog(null, "Please Select Author");
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBounds(149, 102, 116, 41);
		add(btnDelete);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (null != author && !author.getId().isBlank()) {
					author.setName(txtName.getText());
					if (!author.getName().isBlank()) {
						auService.updateAuthor(String.valueOf(author.getId()), author);
						resetFormData();
						loadAllAuthors(Optional.empty());
						author = null;
					} else {
						JOptionPane.showMessageDialog(null, "Please enter Author Name!");
					}

				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUpdate.setBounds(285, 102, 116, 41);
		add(btnUpdate);

		JPanel showtablepanel = new JPanel();
		showtablepanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		showtablepanel.setBounds(10, 154, 789, 285);
		add(showtablepanel);
		showtablepanel.setLayout(null);

		tblshowAuthor = new JTable();
		tblshowAuthor.setSize(new Dimension(789, 288));
		tblshowAuthor.setRowHeight(30);
		tblshowAuthor.setFont(new Font("Tahoma", Font.BOLD, 14));
		tblshowAuthor.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.tblshowAuthor.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowAuthor.getSelectionModel().isSelectionEmpty()) {

				String id = tblshowAuthor.getValueAt(tblshowAuthor.getSelectedRow(), 0).toString();
				author = auService.findById(id);

				lblAu.setText(String.valueOf(author.getId()));
				txtName.setText(author.getName());

			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 789, 288);
		showtablepanel.add(scrollPane);
		scrollPane.setViewportView(tblshowAuthor);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String keyword = txtSearch.getText();

				loadAllAuthors(Optional.of(originalAuthorList.stream()
						.filter(b -> b.getName().toLowerCase(Locale.ROOT).startsWith(keyword.toLowerCase(Locale.ROOT)))
						.collect(Collectors.toList())));
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSearch.setBounds(667, 8, 108, 41);
		add(btnSearch);

	}
}
