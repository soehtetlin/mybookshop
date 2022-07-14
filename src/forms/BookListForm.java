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

	private BookForm bookForm= new BookForm();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookListForm frame = new BookListForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the panel.
	 */
	public BookListForm() {
		initialize();
		setTableDesign();
		loadAllBooks(Optional.empty());
		buttonOnClick();
	}
	
	private void initialize() {
		
        this.bookService= new BookService();

		setLayout(null);

		panel = new JPanel();
//		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(0, 0, 787, 472);
		add(panel);
		panel.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
		scrollPane.setBounds(new Rectangle(10, 100, 780, 420));
		scrollPane.setBounds(0, 50, 787, 422);
		panel.add(scrollPane);
		
		JLabel lblFilter = new JLabel("Filter By : ");
		lblFilter.setForeground(new Color(51, 51, 51));
		lblFilter.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblFilter.setBounds(20, 15, 67, 24);
		panel.add(lblFilter);
		
		comboBooks = new JComboBox();
		comboBooks.setBorder(null);
		comboBooks.setBackground(new Color(255, 255, 255));
		comboBooks.setModel(new DefaultComboBoxModel(new String[] {"All Books", "categor1", "Category2", ""}));
		comboBooks.setFont(new Font("Tahoma", Font.BOLD, 13));
		comboBooks.setForeground(new Color(153, 0, 255));
		comboBooks.setBounds(130, 15, 85, 22);
		comboBooks.setOpaque(true);

		panel.add(comboBooks);
		
		comboAuthors = new JComboBox();
		comboAuthors.setModel(new DefaultComboBoxModel(new String[] {"All Authors"}));
		comboAuthors.setOpaque(true);
		comboAuthors.setForeground(new Color(153, 51, 204));
		comboAuthors.setFont(new Font("Tahoma", Font.PLAIN, 13));
		comboAuthors.setBackground(Color.WHITE);
		comboAuthors.setBounds(246, 15, 90, 22);
		panel.add(comboAuthors);
		
		btnAdd = new JButton("Add New Book");
		btnAdd.setBounds(new Rectangle(600, 0, 0, 0));
		btnAdd.setMargin(new Insets(2, 1, 2, 1));
		btnAdd.setIconTextGap(1);
		btnAdd.setBorderPainted(false);

		btnAdd.setForeground(SystemColor.textHighlightText);
		btnAdd.setHorizontalAlignment(SwingConstants.CENTER);
		btnAdd.setBackground(new Color(153, 51, 204));
		Image img=new ImageIcon(this.getClass().getResource("/add-20.png")).getImage();
		btnAdd.setIcon(new ImageIcon(img));
		btnAdd.setAlignmentX(CENTER_ALIGNMENT);
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAdd.setBounds(600, 13, 115, 29);
		btnAdd.setOpaque(true);
		panel.add(btnAdd);
		
		
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
        
        this.table.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
	        		"ID", "Name", "Price", "Quantity", "Shelf No","Remark","Photo"
        	}
        ));
               
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