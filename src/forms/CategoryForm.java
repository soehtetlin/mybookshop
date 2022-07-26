package forms;

import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entities.Author;
import entities.Category;
import services.CategoryService;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;

public class CategoryForm extends JPanel {

	private Category category;
	private CategoryService categoryService;
	private List<Category> originalCategoryList = new ArrayList<>();

	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	private DefaultTableModel dtm = new DefaultTableModel();

	private JTextField txtSearch;
	private JTextField txtCategoryName;
	private JLabel lblCategoryName;
	private JButton btnUpdate, btnSave, btnDelete, btnCancel, btnSearch;

	private JScrollPane scrollPane;

	private JTable table;

	/**
	 * Create the panel.
	 */
	public CategoryForm() {
		setBackground(Color.WHITE);

		categoryService = new CategoryService();
		initialize();
		setTableDesign();
		loadAllCategories(Optional.empty());
	}

	private void initialize() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				groupLayout.createSequentialGroup().addGap(2).addComponent(panel, GroupLayout.DEFAULT_SIZE, 758,
						Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE));

		txtSearch = new JTextField();
		cLayout.setTextField(txtSearch);

		btnSearch = new JButton("Search");
		cLayout.setButton(btnSearch);

		txtCategoryName = new JTextField();
		cLayout.setTextField(txtCategoryName);

		lblCategoryName = new JLabel(" Category Name");
		cLayout.setLabel(lblCategoryName);

		btnUpdate = new JButton("Update");
		cLayout.setButton(btnUpdate);
		btnUpdate.setVisible(false);

		btnSave = new JButton("Save");
		cLayout.setButton(btnSave);

		btnDelete = new JButton("Delete");
		cLayout.setButton(btnDelete);
		btnDelete.setVisible(false);

		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);

		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup().addGap(21)
				.addComponent(lblCategoryName, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup()
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE).addGap(12)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE).addGap(12)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtCategoryName, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
				.addGap(333))
				.addGroup(gl_panel.createSequentialGroup().addContainerGap(596, Short.MAX_VALUE)
						.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE).addGap(49))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(22)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblCategoryName).addComponent(
						txtCategoryName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)));

		table = new JTable();
		scrollPane.setViewportView(table);

		panel.setLayout(gl_panel);

		setLayout(groupLayout);

		this.table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {

			if (!table.getSelectionModel().isSelectionEmpty()) {

				btnSave.setVisible(false);
				btnUpdate.setVisible(true);
				btnDelete.setVisible(true);

				String id = table.getValueAt(table.getSelectedRow(), 0).toString();

				category = categoryService.findById(id);

				txtCategoryName.setText(category.getName());

			}

		});

		buttonOnClick();
	}

	private void setTableDesign() {

		table.setBounds(150, 251, 555, -184);
		cLayout.setTable(table);

		dtm.addColumn("Category ID");
		dtm.addColumn("Category Name");

		this.table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Category ID", "Category Name" }));

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

	private void loadAllCategories(Optional<List<Category>> optionalCategories) {

		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalCategoryList = this.categoryService.findAllCategories();
		List<Category> categoryList = optionalCategories.orElseGet(() -> originalCategoryList);

		categoryList.forEach(b -> {
			Object[] row = new Object[2];
			row[0] = b.getId();
			row[1] = b.getName();

			dtm.addRow(row);
		});

		table.setModel(dtm);

	}

	private void searchCategory() {

		String keyword = txtSearch.getText();

		loadAllCategories(Optional.of(originalCategoryList.stream()
				.filter(b -> b.getName().toLowerCase(Locale.ROOT).startsWith(keyword.toLowerCase(Locale.ROOT)))
				.collect(Collectors.toList())));

	}

	private void buttonOnClick() {

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (txtCategoryName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Name!");
					txtCategoryName.requestFocus();
				} else {

					categoryService.saveCategory(category);
					txtCategoryName.setText("");

					loadAllCategories(Optional.empty());

					category = null;
				}
			}

		});

		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (category != null && category.getId() != null) {

					category.setName(txtCategoryName.getText());

					if (!category.getName().isBlank()) {

						categoryService.updateCategory(category.getId(), category);
						txtCategoryName.setText("");

						loadAllCategories(Optional.empty());

						category = null;
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
				if (category != null) {
					categoryService.deleteCategory(String.valueOf(category.getId()));
					txtCategoryName.setText("");
					buttonVisible();
					loadAllCategories(Optional.empty());
					category = null;
				} else {
					JOptionPane.showMessageDialog(null, "Choose Category!");
				}

				buttonVisible();
			}

		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				txtCategoryName.setText("");
				loadAllCategories(Optional.empty());
				buttonVisible();
			}

		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchCategory();
			}
		});

		txtSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				searchCategory();
			}
		});
	}
}
