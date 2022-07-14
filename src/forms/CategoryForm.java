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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import entities.Category;
import services.CategoryService;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.ListSelectionModel;

public class CategoryForm extends JPanel {

	private JTextField txtName, txtSearch;
	private JTable tblshowCategory;
	private JLabel lblCategoryID, lblCategory;
	private Category category;
	private CategoryService categoryService = new CategoryService();
	private DefaultTableModel dtm = new DefaultTableModel();
	private List<Category> originalCategoryList = new ArrayList<>();

	/**
	 * Create the panel.
	 */

	public CategoryForm() {
		initialize();
		this.setTableDesign();
		this.loadAllCategories(Optional.empty());
	}

	private void loadAllCategories(Optional<List<Category>> optionalCategories) {
		this.dtm = (DefaultTableModel) this.tblshowCategory.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalCategoryList = this.categoryService.findAllCategories();
		List<Category> authors = optionalCategories.orElseGet(() -> originalCategoryList);

		authors.forEach(b -> {
			Object[] row = new Object[2];
			row[0] = b.getId();
			row[1] = b.getName();

			dtm.addRow(row);
		});

		tblshowCategory.setModel(dtm);

	}

	private void setTableDesign() {
		dtm.addColumn("Category ID");
		dtm.addColumn("Category Name");
		tblshowCategory.setModel(dtm);
		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		tblshowCategory.getColumnModel().getColumn(0).setCellRenderer(dfcr);
		tblshowCategory.getColumnModel().getColumn(1).setCellRenderer(dfcr);


	}

	private void resetFormData() {
		txtName.setText("");
	}

	private void initialize() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(null);
		setBounds(42, 11, 809, 450);

		JLabel lblAuthorName = new JLabel("Category Name");
		lblAuthorName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAuthorName.setBounds(10, 52, 116, 39);
		add(lblAuthorName);

		txtName = new JTextField();
		txtName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (null != category && !category.getId().isBlank()) {
					category.setName(txtName.getText());
					if (!category.getName().isBlank()) {
						categoryService.updateCategory(String.valueOf(category.getId()), category);
						resetFormData();
						loadAllCategories(Optional.empty());
						category = null;
					} else {
						JOptionPane.showMessageDialog(null, "Please enter Author Name!");
					}
				} else {
					Category author = new Category();
					author.setName(txtName.getText());
					if (null != author.getName() && !author.getName().isBlank()) {

						categoryService.saveCategory(author);
						resetFormData();
						loadAllCategories(Optional.empty());
					} else {
						JOptionPane.showMessageDialog(null, "Enter Required Field!");
					}
				}

			}
		});
		txtName.setBounds(136, 52, 222, 39);
		add(txtName);
		txtName.setColumns(10);

		lblCategoryID = new JLabel("Category ID");
		lblCategoryID.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategoryID.setBounds(10, 2, 116, 39);
		add(lblCategoryID);

		lblCategory = new JLabel("Show Category ID");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(136, 2, 116, 39);
		add(lblCategory);

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
			
				Category category = new Category();
					category.setName(txtName.getText());
					if (null != category.getName() && !category.getName().isBlank()) {

						categoryService.saveCategory(category);
						resetFormData();
						loadAllCategories(Optional.empty());
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
				if (null != category) {
					categoryService.deleteCategory(category.getId() + "");
					resetFormData();
					loadAllCategories(Optional.empty());
					category = null;
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
				if (null != category && !category.getId().isBlank()) {
					category.setName(txtName.getText());
					if (!category.getName().isBlank()) {
						categoryService.updateCategory(String.valueOf(category.getId()), category);
						resetFormData();
						loadAllCategories(Optional.empty());
						category = null;
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

		tblshowCategory = new JTable();
		tblshowCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblshowCategory.setSize(new Dimension(789, 288));
		tblshowCategory.setRowHeight(30);
		tblshowCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		tblshowCategory.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.tblshowCategory.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowCategory.getSelectionModel().isSelectionEmpty()) {

				String id = tblshowCategory.getValueAt(tblshowCategory.getSelectedRow(), 0).toString();
				category = categoryService.findById(id);

				lblCategory.setText(String.valueOf(category.getId()));
				txtName.setText(category.getName());

			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 789, 288);
		showtablepanel.add(scrollPane);
		scrollPane.setViewportView(tblshowCategory);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String keyword = txtSearch.getText();

				loadAllCategories(Optional.of(originalCategoryList.stream()
						.filter(b -> b.getName().toLowerCase(Locale.ROOT).startsWith(keyword.toLowerCase(Locale.ROOT)))
						.collect(Collectors.toList())));
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSearch.setBounds(667, 8, 108, 41);
		add(btnSearch);

	}
}
