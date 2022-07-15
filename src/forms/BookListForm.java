package forms;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableModel;

import entities.Book;
import services.BookService;

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

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import java.awt.Insets;
import java.awt.Rectangle;
<<<<<<< HEAD
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
=======
>>>>>>> 5de18dd01ed0a43a40b5e1c0bf4897ec55848f40

public class BookListForm extends JPanel {
	
	private JTable table;
	private JLabel lbladd;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JButton btnAdd;
	private JComboBox comboBooks,comboAuthors;
    private DefaultTableModel dtm = new DefaultTableModel();
    private BookService bookService;
	private List<Book> originalBookList = new ArrayList<>();
	private JpanelLoader jloader = new JpanelLoader();
<<<<<<< HEAD
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
=======

	public BookListForm() {
		initialize();
		setTableDesign();
		loadAllBooks(Optional.empty());
		buttonOnClick();
	}
	
	private void initialize() {
		
        this.bookService= new BookService();


		panel = new JPanel();
		
		scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblFilter = new JLabel("Filter By : ");
		cLayout.setLabel(lblFilter);
		
		comboBooks = new JComboBox();
		cLayout.setComboBox(comboBooks);
				
		comboAuthors = new JComboBox();
		cLayout.setComboBox(comboAuthors);
		
		
		btnAdd = new JButton("Add New Book");
		cLayout.setButton(btnAdd);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 725, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 438, Short.MAX_VALUE)
					.addContainerGap())
		);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addGap(20)
					.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addGap(30)
					.addComponent(comboBooks, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addGap(29)
					.addComponent(comboAuthors, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
					.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBooks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboAuthors, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
		);
//		gl_panel.setAutoCreateContainerGaps(true);
//		gl_panel.setAutoCreateGaps(true);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

		
	}
	
	private void setTableDesign() {
		
		table = new JTable();
		table.setSelectionBackground(new Color(153, 51, 255));
		table.setShowVerticalLines(false);
		table.setFocusable(false);
		
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setBounds(12, 254, 404, -216);
//		table.setBackground(new Color(0,0,0));
		
		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(153, 51, 204));
		table.getTableHeader().setForeground(new Color(245, 245, 245));
//		table.getTableHeader().setColumnModel(null);
		table.setRowHeight(25);
	    scrollPane.setViewportView(table);
	    
        dtm.addColumn("ID");
        dtm.addColumn("Name");
        dtm.addColumn("Price");
        dtm.addColumn("Quantity");
        dtm.addColumn("Shelf No");
        dtm.addColumn("Remark");
        dtm.addColumn("Photo");

        table.setModel(dtm);

               
		}
	
	private void loadAllBooks(Optional<List<Book>> optionalBook) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();
		
		this.originalBookList = this.bookService.findAllBooks();
		List<Book> bookList= optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row=new Object[7];
			 row[0] = e.getId();
		      row[1] = e.getName();
		      row[2] = e.getPrice();
		      row[4] = e.getStockamount();
		      row[5] = e.getShelf_number();
		      row[6] = e.getPhoto();
			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}
	
	private void buttonOnClick() {
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel, bookForm);
			}
		});	}
}