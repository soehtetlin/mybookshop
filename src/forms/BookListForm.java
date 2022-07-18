package forms;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entities.Author;
import entities.Book;
import entities.Category;
import services.AuthorService;
import services.BookService;
import services.CategoryService;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.ColorUIResource;
import forms.CreateLayoutProperties;
import forms.JpanelLoader;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class BookListForm extends JPanel {

	private JTable table;
	private JLabel lbladd;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JButton btnAdd;
	private JComboBox<String> cboCategory, cboAuthors;
	private DefaultTableModel dtm = new DefaultTableModel();
	private BookService bookService;
	private List<Book> originalBookList = new ArrayList<>();
	private JpanelLoader jloader = new JpanelLoader();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	private BookForm bookForm = new BookForm();
	private Book book = new Book();
	private JLabel showPhoto = new JLabel();
	private AuthorService authorService = new AuthorService();
	private CategoryService categoryService = new CategoryService();
	private List<Category> categoryList;
	private List<Author> authorList;
	

	public BookListForm() {
		initialize();
		setTableDesign();
		loadAllBooks(Optional.empty());
		buttonOnClick();
	}

	private void initialize() {

		this.bookService = new BookService();

		panel = new JPanel();

		scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JLabel lblFilter = new JLabel("Filter By : ");
		cLayout.setLabel(lblFilter);

		cboCategory = new JComboBox();
		cLayout.setComboBox(cboCategory);
		loadCategoryForComboBox();

		cboAuthors = new JComboBox();
		cLayout.setComboBox(cboAuthors);
		loadAuthorForComboBox();

		btnAdd = new JButton("Add New Book");
		cLayout.setButton(btnAdd);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 725, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 438, Short.MAX_VALUE).addContainerGap()));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(20)
					.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(cboCategory, 0, 100, Short.MAX_VALUE)
					.addGap(29)
					.addComponent(cboAuthors, 0, 100, Short.MAX_VALUE)
					.addGap(243)
					.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboAuthors, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}

	private void setTableDesign() {

		table = new JTable();
		cLayout.setTable(table);
		scrollPane.setViewportView(table);
		
		dtm.addColumn("Book Cover");
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Price");
		dtm.addColumn("Quantity");
		dtm.addColumn("Shelf No");
		dtm.addColumn("Remark");
		table.setModel(dtm);
		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		
		table.getColumnModel().getColumn(0).setCellRenderer(new ImagerRender());
		table.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(4).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(5).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(6).setCellRenderer(dfcr);
	

	}

	private void loadAllBooks(Optional<List<Book>> optionalBook) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalBookList = this.bookService.findAllBooks();
		List<Book> bookList = optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row = new Object[7];
			row[0] = e.getPhoto();
			row[1] = e.getId();
			row[2] = e.getName();
			row[3] = e.getPrice();
			row[4] = e.getStockamount();
			row[5] = e.getShelf_number();
			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}

	private void buttonOnClick() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel, bookForm);
			}
		});
	}

	private class ImagerRender extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object photo, boolean arg2, boolean arg3, int arg4,
			int arg5) {
		
		System.out.println("Show store file address :" + photo.toString());

		ImageIcon imageIcon = null;
		
			imageIcon = new ImageIcon(new ImageIcon(photo.toString()).getImage().getScaledInstance(160, 115, Image.SCALE_SMOOTH));

		return new JLabel(imageIcon);
	}

	}
	
	
	private void loadCategoryForComboBox() {
		cboCategory.addItem("All Category");
		System.out.println("Cate count " + categoryService.findAllCategories().size());
		this.categoryList = this.categoryService.findAllCategories();
		this.categoryList.forEach(c -> cboCategory.addItem(c.getName()));
	}

	private void loadAuthorForComboBox() {
		cboAuthors.addItem("All Author");
		System.out.println("author count " + authorService.findAllAuthors().size());

		this.authorList = this.authorService.findAllAuthors();
		this.authorList.forEach(a -> cboAuthors.addItem(a.getName()));
	}
	
	
	private class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
	    WordWrapCellRenderer() {
	        setLineWrap(true);
	        setWrapStyleWord(true);
	        setAlignmentX(CENTER_ALIGNMENT);
	          setAlignmentY(CENTER_ALIGNMENT);
	          setFont(new Font("Tahoma", Font.BOLD, 12));
	          
	        
	        
	    }

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    	  setText( (value == null) ? "" : value.toString() );
	          setSize(160, 120);
	      

	          //  Recalculate the preferred height now that the text and renderer width have been set.

//	          int preferredHeight = getPreferredSize().height;
//
//	          if (table.getRowHeight(row) != preferredHeight)
//	          {
//	              table.setRowHeight(row, preferredHeight);
//	          }

	          return this;
	      }
	
	    
	}
}

