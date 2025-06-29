package forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entities.Author;
import entities.Book;
import services.AuthorService;
import services.BookService;

public class BookListForm extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JButton btnAdd;
	private JComboBox<String> cboAuthors;
	private DefaultTableModel dtm = new DefaultTableModel();
	private BookService bookService;
	private List<Book> originalBookList = new ArrayList<>();
	private JpanelLoader jloader = new JpanelLoader();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	private BookForm bookForm = new BookForm();
	private Book book = new Book();
	private AuthorService authorService = new AuthorService();
	private List<Author> authorList;

	private JTextField txtSearch;

	public BookListForm() {
		setBackground(Color.WHITE);
		initialize();
		setTableDesign();
		loadAllBooks(Optional.empty());
		loadAuthorForComboBox();
		buttonOnClick();
	}

	private void initialize() {

		this.bookService = new BookService();

		panel = new JPanel();
		panel.setBackground(Color.WHITE);

		table = new JTable();
		table.setBackground(Color.WHITE);

		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JLabel lblFilter = new JLabel("Filter By : ");
		cLayout.setLabel(lblFilter);
		cboAuthors = new JComboBox<>();
		cLayout.setComboBox(cboAuthors);
		cboAuthors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cboAuthors.getSelectedIndex() == 0) {
					loadAllBooks(Optional.empty());
				} else {
					loadAllBooksByAuthors(cboAuthors.getSelectedItem().toString());
				}
			}
		});

		btnAdd = new JButton("Add New Book");
		cLayout.setButton(btnAdd);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 725, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 438, Short.MAX_VALUE).addContainerGap()));

		txtSearch = new JTextField();
		txtSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchBook();
			}
		});
		txtSearch.setColumns(10);

		JButton btnSearch = new JButton("Search By Book Name");
		cLayout.setButton(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (txtSearch.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Enter Book Name!");
					txtSearch.requestFocus();
				} else {
					searchBook();
				}

			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(20)

						.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(cboAuthors, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
						.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSearch).addGap(18)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())

				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(15)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)

								.addComponent(cboAuthors, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSearch))

						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}

	protected void searchBook() {

		String keyword = txtSearch.getText();

		loadAllBooks(Optional.of(originalBookList.stream()
				.filter(e -> e.getName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getCategory().getName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getPublisher().getName().toLowerCase(Locale.ROOT)
								.contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getAuthor().getName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT)))
				.collect(Collectors.toList())));

	}

	private void setTableDesign() {

		table.setSelectionBackground(new Color(191, 148, 228));
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
		table.getTableHeader().setPreferredSize(new Dimension(80, 35));
		table.setRowHeight(65);

		table.setFont(new Font("Tahoma", Font.PLAIN, 13));

		scrollPane.setViewportView(table);

		dtm.addColumn("Book Cover");
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Author Name");
		dtm.addColumn("Price");
		dtm.addColumn("Quantity");
		dtm.addColumn("Shelf No");
		dtm.addColumn("Remark");
		dtm.addColumn("Edit");
		dtm.addColumn("Delete");

		table.setModel(dtm);

		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);

		table.getColumnModel().getColumn(0).setCellRenderer(new ImagerRender());
		table.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(2).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(4).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(5).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(6).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(7).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JTextField()));
		table.getColumnModel().getColumn(9).setCellRenderer(new DeleteButtonRenderer());
		table.getColumnModel().getColumn(9).setCellEditor(new DeleteButtonEditor(new JTextField()));

	}

	private void loadAllBooksByAuthors(String s) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();
		List<Book> bookList = new ArrayList<>();
		bookList = bookService.findBookByAuthorName(s);
		bookList.forEach(e -> {
			Object[] row = new Object[10];
			row[0] = e.getPhoto();
			row[1] = e.getId();
			row[2] = e.getName();
			row[3] = e.getAuthor().getName();
			row[4] = e.getPrice();
			row[5] = e.getStockamount();
			row[6] = e.getShelf_number();
			row[7] = e.getRemark();

			dtm.addRow(row);
		});
		this.table.setModel(dtm);

		table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));

	}

	private void loadAllBooks(Optional<List<Book>> optionalBook) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalBookList = this.bookService.findAllBooksforList();
		List<Book> bookList = optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row = new Object[10];
			row[0] = e.getPhoto();
			row[1] = e.getId();
			row[2] = e.getName();
			row[3] = e.getAuthor().getName();
			row[4] = e.getPrice();
			row[5] = e.getStockamount();
			row[6] = e.getShelf_number();
			row[7] = e.getRemark();

			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}

	private void buttonOnClick() {
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel, bookForm);
			}
		});
	}

	private class ImagerRender extends DefaultTableCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable arg0, Object photo, boolean arg2, boolean arg3, int arg4,
				int arg5) {

			ImageIcon imageIcon = null;

			imageIcon = new ImageIcon(
					new ImageIcon(photo.toString()).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

			return new JLabel(imageIcon);
		}

	}

	private void loadAuthorForComboBox() {
		cboAuthors.addItem("All Author");
		System.out.println("author count " + authorService.findAllAuthors().size());

		this.authorList = this.authorService.findAllAuthors();
		this.authorList.forEach(a -> cboAuthors.addItem(a.getName()));
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
				int row, int column) {

			ImageIcon img = new ImageIcon(new ImageIcon(this.getClass().getResource("/edit.png")).getImage());
			setIcon(img);
			setContentAreaFilled(false);
			setBorder(new EmptyBorder(5, 5, 5, 5));
			setBackground(Color.red);
			return this;
		}

	}

	class ButtonEditor extends DefaultCellEditor {

		protected JButton btn;
		private String lbl;
		private Boolean clicked;

		public ButtonEditor(JTextField textField) {
			super(textField);
			btn = new JButton();

			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String id = table.getValueAt(table.getSelectedRow(), 1).toString();
					book = bookService.findById(id);
					jloader.jPanelLoader(panel, new BookForm(book));

				}

			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row,
				int column) {
			lbl = (obj == null) ? "" : obj.toString();
			System.out.println("lbl " + lbl);
			btn.setText(lbl);

			clicked = true;
			return btn;
		}

		@Override
		public Object getCellEditorValue() {

			if (clicked) {
				JOptionPane.showMessageDialog(btn, lbl + "Clicked");
			}

			clicked = false;
			return new String(lbl);
		}

		@Override
		public boolean stopCellEditing() {

			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {

			super.fireEditingStopped();
		}

	}

	class DeleteButtonRenderer extends JButton implements TableCellRenderer {

		public DeleteButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus,
				int row, int column) {

			ImageIcon img = new ImageIcon(new ImageIcon(this.getClass().getResource("/delete.png")).getImage());
			setIcon(img);
			setContentAreaFilled(false);
			setBorder(new EmptyBorder(5, 5, 5, 5));
			setBackground(Color.red);
			return this;
		}

	}

	class DeleteButtonEditor extends DefaultCellEditor {

		protected JButton btndelete;
		private String lbl;
		private Boolean clicked;

		public DeleteButtonEditor(JTextField textField) {
			super(textField);
			btndelete = new JButton();

			btndelete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					JOptionPane.showMessageDialog(null, "Are you sure you want to delte!!!");

					String id = table.getValueAt(table.getSelectedRow(), 1).toString();
					bookService.deletBooks(id);

					loadAllBooks(Optional.empty());
					book = null;

				}

			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row,
				int column) {
			lbl = (obj == null) ? "" : obj.toString();
			System.out.println("lbl " + lbl);
			btndelete.setText(lbl);

			clicked = true;
			return btndelete;
		}

		@Override
		public Object getCellEditorValue() {

			if (clicked) {
				JOptionPane.showMessageDialog(btndelete, lbl + "Clicked");
			}

			clicked = false;
			return new String(lbl);
		}

		@Override
		public boolean stopCellEditing() {

			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {

			super.fireEditingStopped();
		}

	}
}
