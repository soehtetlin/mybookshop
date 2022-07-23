package forms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import shared.checker.Checking;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import com.mysql.cj.util.StringUtils;
import entities.Author;
import entities.Book;
import entities.Category;
import entities.Employee;
import entities.Publisher;
import entities.Purchase;
import entities.PurchaseDetails;
import services.EmployeeService;
import services.BookService;
import services.CategoryService;
import services.PublisherService;
import services.PurchaseService;
import shared.utils.CurrentUserHolder;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.toedter.calendar.JDateChooser;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class PurchaseForm extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtsearchbook;
	private JTable tblshowbooklist;
	private JTable tblshowPurchase;
	private JTextField txtStockAmount;
	private JTextField txtPrice;
	private JButton btnedit;
	private JComboBox<String> cboCategory, cboPublisher;
	private List<Category> categoryList;
	private List<Employee> employeelist;
	private List<Publisher> publisherList;
	private DefaultTableModel dtm = new DefaultTableModel();
	private DefaultTableModel dtmpurchase = new DefaultTableModel();
	private CategoryService categoryService;
	private EmployeeService employeeService;
	private PublisherService publisherService;
	private BookService bookService;
	private Book book;
	private JButton btnshowAll;
	private Purchase purchase;
	private PurchaseDetails selectedPurchaseDetails;
	private PurchaseService purchaseService = new PurchaseService();
	private boolean editPurchaseDetails = false; // private PurchaseService purchaseService;
	private List<Book> originalBookList = new ArrayList<>();
	private Vector<String> vno = new Vector<>(), vtotalPrice = new Vector<>(), vtotalquantity = new Vector<>(),
			vid = new Vector<>();
	private String[] showPurchase = new String[11];
	private JLabel lbltotalprice;
	private JLabel lbltotalquantity;
	private int totalquantity;
	private JLabel lblBookID;
	private int serialno;
	private Employee employee;
	private PurchaseDetails purchaseDetail;
	private PurchaseDetailForm purchaseDetailForm;
	private List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
	private JLabel lblemployee;
	private JButton btnaddbook;
	private JLabel lblshowBookCover;
	private JDateChooser dateChooser;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	/**
	 * Create the panel.
	 */
	public PurchaseForm() {
		initialize();
		initializeDependency();
		this.setTableDesign();
		this.setTableDesignForPurchaseTable();
		// txtsearchbook.requestFocusInWindow();
		this.showTodayDate();
		this.loadAllBooks(Optional.empty());
		// this.loadAllPurchaseDetails();
		this.loadCategoryForComboBox();
		this.loadPublisherForComboBox();
		this.initializeLoggedInUser();

	}

	private void initializeLoggedInUser() {
		employee = CurrentUserHolder.getCurrentUser();
		if (employee != null)
			lblemployee.setText(employee.getName());
	}

	private void showTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar now = Calendar.getInstance();
				// lblshowdate.setText(dateFormat.format(now.getTime()));
			}
		}).start();
	}

	private void setTableDesign() {
		// TODO Auto-generated method stub

		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Price");
		dtm.addColumn("Quantity");
		dtm.addColumn("Shelf No");
		dtm.addColumn("Author Name");
		dtm.addColumn("Category Name");
		dtm.addColumn("Publisher Name");
		dtm.addColumn("Remark");
		tblshowbooklist.setRowHeight(25);
		tblshowbooklist.setModel(dtm);
		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		tblshowbooklist.getColumnModel().getColumn(0).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(2).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(4).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(5).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(6).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(7).setCellRenderer(dfcr);
		tblshowbooklist.getColumnModel().getColumn(8).setCellRenderer(dfcr);

	}

	private void loadAllBooks(Optional<List<Book>> optionalBook) {
		this.dtm = (DefaultTableModel) this.tblshowbooklist.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalBookList = this.bookService.findAllBooksforList();
		List<Book> bookList = optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row = new Object[9];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getPrice();
			row[3] = e.getStockamount();
			row[4] = e.getShelf_number();
			row[5] = e.getAuthor().getName();
			row[6] = e.getCategory().getName();
			row[7] = e.getPublisher().getName();
			row[8] = e.getRemark();
			dtm.addRow(row);
		});
		this.tblshowbooklist.setModel(dtm);

	}

	private void clearform() {
		// TODO Auto-generated method stub

		lblBookID.setText("");
		txtStockAmount.setText("");
		txtPrice.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);

	}

	private void clearAll() {
		// TODO Auto-generated method stub

		lblBookID.setText("");
		txtStockAmount.setText("");
		txtPrice.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);
		cboPublisher.setSelectedIndex(0);
		lbltotalprice.setText("0");
		lbltotalquantity.setText("0");
		lblshowBookCover.setIcon(null);
		lblshowBookCover.setText("Show Selected Book Cover");

		while (dtmpurchase.getRowCount() > 0) {
			dtmpurchase.removeRow(0);
		}
		tblshowPurchase.setModel(dtmpurchase);
		vtotalquantity.removeAllElements();
		vno.removeAllElements();
		vid.removeAllElements();
		vtotalPrice.removeAllElements();

		// cboBrand.setSelectedIndex(0);

	}



	private void setTableDesignForPurchaseTable() {
		dtmpurchase.addColumn("No");
		dtmpurchase.addColumn("Book");
		// dtmpurchase.addColumn("Category");
		// dtmpurchase.addColumn("Author");
		dtmpurchase.addColumn("Quantity");
		dtmpurchase.addColumn("Price");
		dtmpurchase.addColumn("Amount");
		this.tblshowPurchase.setModel(dtmpurchase);

		tblshowPurchase.setRowHeight(40);

		tblshowPurchase.setModel(dtmpurchase);

		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		tblshowPurchase.getColumnModel().getColumn(0).setCellRenderer(dfcr);
		tblshowPurchase.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		tblshowPurchase.getColumnModel().getColumn(2).setCellRenderer(dfcr);
		tblshowPurchase.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		tblshowPurchase.getColumnModel().getColumn(4).setCellRenderer(dfcr);

		tblshowPurchase.getColumnModel().getColumn(0).setPreferredWidth(50);
		tblshowPurchase.getColumnModel().getColumn(1).setPreferredWidth(80);
		tblshowPurchase.getColumnModel().getColumn(2).setPreferredWidth(80);
		tblshowPurchase.getColumnModel().getColumn(3).setPreferredWidth(80);
		tblshowPurchase.getColumnModel().getColumn(4).setPreferredWidth(100);

	}


	private void deleteRow() {
		// TODO Auto-generated method stub
		int i = tblshowPurchase.getSelectedRow();

		System.out.println("vid.size" + vid.size());
		System.out.println();
		System.out.println("Get selected row index in delete row method: " + i);
		serialno = vid.indexOf(vid.get(i));
		System.out.println("Vid.elementAt before remove in delete row : " + serialno);
		vtotalPrice.remove(i);
		vtotalquantity.remove(i);

		if (!vno.lastElement().equals(vno.get(i))) {

			vno.remove(i);
			vid.remove(i);

			System.out.println("vid after remove in delete row :" + vid.elementAt(i));

			// int row = mtt.getSelectedRows()[0];// returns row position
			dtmpurchase.removeRow(i);

			for (int y = i; y < dtmpurchase.getRowCount(); y++) {
				System.out.println("inside loop y value " + y);

				dtmpurchase.setValueAt(y + 1, y, 0); // setValueAt(data,row,column)
			}

		} else {
			vno.remove(i);
			vid.remove(i);
			dtmpurchase.removeRow(i);
		}

		tblshowPurchase.setModel(dtmpurchase);
		lbltotalprice.setText(sumAmount(vtotalPrice, 1));
		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
	}

	private void editRow() {

		int i = tblshowPurchase.getSelectedRow();
		System.out.println("Delet Row index:" + i);
		vtotalquantity.remove(i);
		vtotalPrice.remove(i);
		int no;

		if (!vno.lastElement().equals(vno.get(i))) {
			vno.remove(i);
			vid.remove(i);
			no = vno.indexOf(vno.get(i));
			System.out.println("VNO index no : " + no);
			dtmpurchase.removeRow(i);
			dtmpurchase.setValueAt(no + 1, i, 0);

		} else {
			vno.remove(i);
			vid.remove(i);
			dtmpurchase.removeRow(i);
		}
		tblshowPurchase.setModel(dtmpurchase);
		lbltotalprice.setText(sumAmount(vtotalPrice, 1));
		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
	}


	public void input_productFromPopover(Book books) {
		PurchaseDetails purchaseDetails = new PurchaseDetails();
		purchaseDetails.setBook(books);
		purchaseDetails.setQuantity(books.getStockamount());
		purchaseDetails.setPrice(books.getPrice());
		this.purchaseDetailsList.add(purchaseDetails);
		// this.loadAllPurchaseDetails();
	}

	private void addBookToPurchaseTable() {

		showPurchase[0] = String.valueOf(vno.size() + 1);// show no
		System.out.println("Serial no in add purchase table method: " + showPurchase[0]);
		// showPurchase[1] = book.getId();
		System.out.println("Book.get Id in addpurchase :" + book.getId());
		System.out.println("Serial nO book id " + serialno);
		vid.addElement(book.getId());
		System.out.println("Book.get Id in addpurchase :" + book.getId());

		System.out.println("This is book.getiD " + book.getId());

		showPurchase[1] = book.getName();// show name
		System.out.println("Book Name in add purchase method getpuchase arry index 1 " + showPurchase[1]);
		vno.addElement(showPurchase[1]);

		// showPurchase[2] = book.getCategory().getName();

		// showPurchase[3] = book.getAuthor().getName();
		// showPurchase[2] = book.getPhoto();// show photo

		showPurchase[2] = txtStockAmount.getText();// show quantity

		totalquantity = Integer.valueOf(showPurchase[2]) + (book.getStockamount());
		book.setStockamount(totalquantity);

		showPurchase[3] = txtPrice.getText();// show price

//		System.out.println(
//				"Multiply quantity and price " + Integer.valueOf(showPurchase[5]) * Integer.valueOf(showPurchase[4]));
		int totalamount = (Integer.valueOf(showPurchase[2]) * Integer.valueOf(showPurchase[3]));
		showPurchase[4] = String.valueOf(totalamount);// show total amount
		vtotalPrice.addElement((showPurchase[4]));
		vtotalquantity.addElement(showPurchase[2]);
		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));

		lbltotalprice.setText(sumAmount(vtotalPrice, 1));// show total amount
		// book.setPrice(Integer.valueOf(sumAmount(vtotalquantity, 1)));
		// showPurchase[4] = String.valueOf(book.getStockamount());

		System.out.println("QTY Count" + vtotalPrice);
//		showPurchase[6] = String.valueOf(book.getShelf_number());

		// showPurchase[5] = book.getPublisher().getName();
		// showPurchase[6] = book.getRemark();

//		for (int i = 0; i < 11; i++) {
//			System.out.println("This is show purchase value " + showPurchase[i]);
//		}
		for (int i = 0; i < vno.size(); i++) {
			System.out.println("ALL Vno data : " + i + " value " + vno.get(i));
		}

		for (int i = 0; i < vid.size(); i++) {
			System.out.println("ALL vid data : " + i + " value " + vid.get(i));
		}

		dtmpurchase.addRow(showPurchase);
		this.tblshowPurchase.setModel(dtmpurchase);
	}
//		book.setName(txtProductName.getText());
//		book.setQuantity(Integer.parseInt(txtQuantity.getText().isBlank() ? "0" : txtQuantity.getText()));
//		book.setPrice(Integer.parseInt(txtPrice.getText().isBlank() ? "0" : txtPrice.getText()));
//		Optional<Category> selectedCategory = categoryList.stream()
//				.filter(c -> c.getName().equals(cboCategory.getSelectedItem())).findFirst();
//		book.setCategory(selectedCategory.orElse(null));
//		Optional<Author> selectedBrand = brandList.stream().filter(b -> b.getName().equals(cboBrand.getSelectedItem()))
//				.findFirst();
//		book.setBrand(selectedBrand.orElse(null));

	private String sumAmount(Vector<String> storeQTY2, int t) {
		// TODO Auto-generated method stub
		long sum = 0;
		for (int i = 0; i < storeQTY2.size(); i++) {
			sum += Long.parseLong((String) storeQTY2.elementAt(i));
			System.out.println("inside for loop sum resutl " + sum);

		}
		if (t == 1) {
			int len = String.valueOf(sum).length(), index = 0;
			System.out.println("Lengih of Len " + len);
			System.out.println("sum rsutl" + sum);
			StringBuffer str = new StringBuffer("");
			for (int i = 0; i < len; i++) {
				if (index == 3) {
					str.append(",");
					index = 0;
					i--;
				} else {
					str.append(String.valueOf(sum).charAt(len - i - 1));
					index++;
				}
			}
			return str.reverse().toString();
		} else {

			return String.valueOf(sum);
		}
	}

	private void initialize() {

		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setBounds(42, 11, 1000, 495);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane = new JScrollPane();

		cboPublisher = new JComboBox();
		cboPublisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		cboCategory = new JComboBox<String>();

		JLabel lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtStockAmount = new JTextField();
		txtStockAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtPrice.requestFocus();
				txtPrice.selectAll();
				// addBookAction();
			}
		});
		txtStockAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtStockAmount.setColumns(10);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtPrice = new JTextField();
		txtPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (tblshowbooklist.getSelectedRowCount() > 0) {
					addBookAction();
				} else {
					editPurchase();
				}
			}
		});
		txtPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtPrice.setColumns(10);

		lblBookID = new JLabel("");
		lblBookID.setFont(new Font("Tahoma", Font.BOLD, 14));

		btnedit = new JButton("Edit");
		btnedit.setVisible(false);
		btnedit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				editPurchase();

			}
		});
		btnedit.setMargin(new Insets(2, 2, 2, 2));
		btnedit.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnremove = new JButton("Remove");
		btnremove.setVisible(false);
		btnremove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblshowPurchase.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(null, "Please select book to delete!");
				} else {
					deleteRow();
					clearform();
					lbltotalprice.setText(sumAmount(vtotalPrice, 1));
					lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
				}
			}

		});
		btnremove.setMargin(new Insets(2, 2, 2, 2));
		btnremove.setFont(new Font("Tahoma", Font.BOLD, 14));

		btnaddbook = new JButton("Add Book");
		btnaddbook.setMnemonic('a');
		btnaddbook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBookAction();

			}

		});

		btnaddbook.setMargin(new Insets(2, 2, 2, 2));
		btnaddbook.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtsearchbook = new JTextField();
		txtsearchbook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchBook();

			}
		});
		txtsearchbook.setName("");
		txtsearchbook.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtsearchbook.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchBook();

			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSearch.setMargin(new Insets(2, 2, 2, 2));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane_1 = new JScrollPane();
		tblshowPurchase = new JTable();
		tblshowPurchase.setFont(new Font("Tahoma", Font.BOLD, 14));
		tblshowPurchase.setBackground(new Color(255, 250, 240));
		tblshowPurchase.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblshowPurchase.setForeground(Color.DARK_GRAY);
		tblshowPurchase.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblshowPurchase.setDefaultEditor(Object.class, null);
		tblshowPurchase.setAutoCreateRowSorter(true);
		JTableHeader jtableheader = tblshowPurchase.getTableHeader();
		jtableheader.setBackground(SystemColor.textHighlight);
		jtableheader.setForeground(Color.white);
		jtableheader.setFont(new Font("Tahoma", Font.BOLD, 14));

		this.tblshowPurchase.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowPurchase.getSelectionModel().isSelectionEmpty()) {

				tblshowbooklist.clearSelection();
				btnedit.setVisible(true);
				btnremove.setVisible(true);
				btnaddbook.setVisible(false);
				btnshowAll.setVisible(false);
				// String id = tblshowPurchase.getValueAt(tblshowPurchase.getSelectedRow(),
				// 0).toString();
				int i = tblshowPurchase.getSelectedRow();
				System.out.println("Show selected data " + (String) tblshowPurchase.getValueAt(i, 4));
				// book.setId(((String)tblshowPurchase.getValueAt(i, 1)));
				// System.out.println("Book ID in click action" + book.getId());
				txtStockAmount.setText((String) tblshowPurchase.getValueAt(i, 2));
				txtStockAmount.requestFocus();
				txtStockAmount.selectAll();
				txtPrice.setText((String) tblshowPurchase.getValueAt(i, 3));
				lblBookID.setText(vid.elementAt(i));
				book.setId(vid.elementAt(i));
				book.setName(vno.elementAt(i));

			}
		});

		scrollPane_1.setViewportView(tblshowPurchase);

		lblemployee = new JLabel("Employee Name");
		lblemployee.setFont(new Font("Tahoma", Font.BOLD, 14));

		tblshowbooklist = new JTable();
		tblshowbooklist.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		cLayout.setTable(tblshowbooklist);

		this.tblshowbooklist.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowbooklist.getSelectionModel().isSelectionEmpty()) {
				tblshowPurchase.clearSelection();
				btnedit.setVisible(false);
				btnremove.setVisible(false);
				btnaddbook.setVisible(true);
				btnshowAll.setVisible(true);

				String id = tblshowbooklist.getValueAt(tblshowbooklist.getSelectedRow(), 0).toString();
				book = bookService.findById(id);
				lblBookID.setText(book.getId());
				txtStockAmount.setText(String.valueOf(book.getStockamount()));
				txtStockAmount.requestFocus();
				txtStockAmount.selectAll();
				txtPrice.setText(String.valueOf(book.getPrice()));
				lblshowBookCover.setText("");
				ImageIcon imageIcon = new ImageIcon(
						new ImageIcon(book.getPhoto()).getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH));
				lblshowBookCover.setIcon(imageIcon);
				lblshowBookCover.setHorizontalAlignment(SwingConstants.CENTER);

				cboCategory.setSelectedItem(book.getCategory().getName());

			}
		});

		scrollPane.setViewportView(tblshowbooklist);
		jtableheader = tblshowbooklist.getTableHeader();
		jtableheader.setBackground(SystemColor.textHighlight);
		jtableheader.setForeground(Color.WHITE);
		jtableheader.setFont(new Font("Tahoma", Font.BOLD, 12));

		tableselection();

		btnshowAll = new JButton("Show All");
		btnshowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadAllBooks(Optional.empty());
				clearform();
			}
		});
		btnshowAll.setMargin(new Insets(2, 2, 2, 2));
		btnshowAll.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 431,
												Short.MAX_VALUE)
										.addComponent(panel, 0, 0, Short.MAX_VALUE))
								.addGap(0)));

		lblshowBookCover = new JLabel("Show Selected Book Cover");
		// lblshowBookCover.setIcon(new ImageIcon("C:\\Users\\User\\Pictures\\book
		// cover\\BK (13).jpg"));

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(1)
				.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE).addGap(10)
				.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE).addGap(110))
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(txtsearchbook, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addGap(2).addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup().addGap(1)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE).addGap(5))
				.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(1)
								.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup().addGap(3).addComponent(lblBookID)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblQuantity, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(
												gl_panel.createSequentialGroup().addGap(96).addComponent(txtStockAmount,
														GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel.createSequentialGroup().addGap(96).addComponent(txtPrice,
												GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
										.addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup().addGap(10)
								.addComponent(btnaddbook, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGap(16)
								.addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnremove, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
						.addGap(41).addComponent(lblshowBookCover, GroupLayout.PREFERRED_SIZE, 179, Short.MAX_VALUE)
						.addGap(30)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(6)
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblQuantity, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblBookID, GroupLayout.PREFERRED_SIZE, 42,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addGap(1).addComponent(txtStockAmount,
										GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
						.addGap(10)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(txtPrice, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(17)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnaddbook, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnremove, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblshowBookCover, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
				.addGap(9)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(txtsearchbook,
								GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
				.addGap(27).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE).addGap(5)));
		panel.setLayout(gl_panel);

		// lblshowdate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new
		// java.util.Date()));

		JPanel pnshowpurchaseitem = new JPanel();

		JButton btnSave = new JButton("Save");
		btnSave.setMnemonic('s');

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (vno.size() == 0) {
					JOptionPane.showMessageDialog(null, "There is no item for purchase!");
					txtsearchbook.requestFocus();
				} else {
					if (JOptionPane.showConfirmDialog(null, "Are you sure you want to save!", "Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						String[] savedata1 = new String[3], savedata2 = new String[3];
						savedata1[0] = cboPublisher.getSelectedItem().toString();
						savedata1[1] = LocalDateTime.now().toString();
						savedata1[2] = lblemployee.getText();
						
						System.out.println("Publisher Name :" + savedata1[0]);
						System.out.println("Local Time :" + savedata1[1]);
						System.out.println("Employee Name :" + savedata1[2]);
						
						purchaseService.createPurchase(savedata1);
						for (int i = 0; i < vno.size(); i++) {
							savedata2[0] = (String) tblshowPurchase.getValueAt(i, 2);// get quantity
							savedata2[1] = (String) tblshowPurchase.getValueAt(i, 3);// get price
							savedata2[2] = (String) vid.elementAt(i);
							purchaseService.createPurchaseDetails(savedata2);
						}

						clearAll();
						loadAllBooks(Optional.empty());

					} else {
						JOptionPane.showMessageDialog(null, "Choose Supplier");
					}
				}

			}

		});
		btnSave.setMargin(new Insets(2, 2, 2, 2));
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblTotalAmount = new JLabel("Total Amount");
		lblTotalAmount.setFont(new Font("Tahoma", Font.BOLD, 16));

		JLabel lblTotalQuantity = new JLabel("Total Quantity");
		lblTotalQuantity.setFont(new Font("Tahoma", Font.BOLD, 16));

		lbltotalquantity = new JLabel("0");
		lbltotalquantity.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		lbltotalquantity.setFont(new Font("Tahoma", Font.BOLD, 18));

		lbltotalprice = new JLabel("0");
		lbltotalprice.setForeground(UIManager.getColor("ToolBar.dockingForeground"));

		lbltotalprice.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel lblTotalQuantity_1_1 = new JLabel("Kyats");
		lblTotalQuantity_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		GroupLayout gl_pnshowpurchaseitem = new GroupLayout(pnshowpurchaseitem);
		gl_pnshowpurchaseitem.setHorizontalGroup(gl_pnshowpurchaseitem.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnshowpurchaseitem.createSequentialGroup().addGap(10).addGroup(gl_pnshowpurchaseitem
						.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_pnshowpurchaseitem.createSequentialGroup().addGroup(gl_pnshowpurchaseitem
								.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTotalQuantity, GroupLayout.PREFERRED_SIZE, 202,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_pnshowpurchaseitem.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblTotalAmount,
												GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_pnshowpurchaseitem.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_pnshowpurchaseitem.createSequentialGroup()
												.addComponent(lbltotalprice, GroupLayout.PREFERRED_SIZE, 84,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
												.addComponent(lblTotalQuantity_1_1))
										.addComponent(lbltotalquantity, GroupLayout.PREFERRED_SIZE, 151,
												GroupLayout.PREFERRED_SIZE))))
						.addContainerGap()));
		gl_pnshowpurchaseitem
				.setVerticalGroup(gl_pnshowpurchaseitem.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnshowpurchaseitem.createSequentialGroup().addGap(23)
								.addGroup(gl_pnshowpurchaseitem.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblTotalQuantity, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lbltotalquantity, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE))
								.addGap(11)
								.addGroup(gl_pnshowpurchaseitem.createParallelGroup(Alignment.BASELINE)
										.addComponent(lbltotalprice, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblTotalAmount, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblTotalQuantity_1_1, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)));
		pnshowpurchaseitem.setLayout(gl_pnshowpurchaseitem);

		 dateChooser = new JDateChooser();
		dateChooser.setDate(new Date());
		Date date = dateChooser.getDate();
		System.out.println("Selected date :" + date);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(pnshowpurchaseitem, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 531,
								Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 233, Short.MAX_VALUE).addComponent(
										lblemployee, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)))
						.addGap(51)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(lblemployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addGap(7).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(pnshowpurchaseitem, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		panel_1.setLayout(gl_panel_1);
		setLayout(groupLayout);

	}

	private void editPurchase() {
		// TODO Auto-generated method stub

		if (tblshowPurchase.getSelectedRow() < 0) {
			JOptionPane.showMessageDialog(btnedit, "Please select to edit!");

		} else if (txtPrice.getText().equals("") || txtPrice.getText().isEmpty()) {
			JOptionPane.showMessageDialog(btnedit, "Please Enter Quantity!");
			txtPrice.requestFocus();
			txtPrice.selectAll();
		} else if (txtStockAmount.getText().equals("") || txtStockAmount.getText().isEmpty()) {
			JOptionPane.showMessageDialog(btnedit, "Please Enter Quantity!");
			txtStockAmount.requestFocus();
			txtStockAmount.selectAll();
		} else {

			deleteRow();
			addBookToPurchaseTable();
			lbltotalprice.setText(sumAmount(vtotalPrice, 1));
			lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
			clearform();
		}

	}

	private void tableselection() {
		// TODO Auto-generated method stub

	}

	protected void addBookAction() {

		if (cboPublisher.getSelectedItem().equals("- Select -") || cboCategory.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(btnaddbook, "Please select both publisher name, category name!");
			cboPublisher.requestFocus();
		} else if (lblBookID.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please Select Book");
			txtsearchbook.requestFocus();
		} else if (!Checking.checktxtprice(txtPrice.getText())) {
			txtPrice.requestFocus();
			JOptionPane.showMessageDialog(null, "Please enter Price Correctly!");
		} else if (!Checking.checktxtquantity(txtStockAmount.getText())) {
			txtStockAmount.requestFocus();
			JOptionPane.showMessageDialog(null, "Please enter Quantity Correctly!");
			// System.out.println("Inside the txtprice listener");
		} else if (check(book.getName(), vno)) {
			System.out.println(book.getName() + "inside the check if " + vno);
			JOptionPane.showMessageDialog(null, "This item is already added");
			clearform();
		} else if (null != book && !book.getId().isBlank()) {
			System.out.println("Book Name : " + book.getName());
			Date date = dateChooser.getDate();
			System.out.println("Selected date :" + date);
			addBookToPurchaseTable();
			// loadAllPurchaseDetails();

			clearform();
			loadAllBooks(Optional.empty());
			txtsearchbook.requestFocus();

			// PurchaseDetails purchaseDetails = new PurchaseDetails();
			// purchaseDetails.setPrice();

//			if (!book.getName().isBlank() && book.getPrice() >= 0 && book.getStockamount() >= 0
//					&& null != book.getAuthor() && null != book.getCategory()) {
//
//				productService.updateProduct(String.valueOf(products.getId()), products);
//				resetFormData();
//				ProductListForm productListForm = new ProductListForm();
//				productListForm.frmProductlist.setVisible(true);
//				frame.setVisible(false);

		} else {

			JOptionPane.showMessageDialog(null, "Check Required Field");
		}

	}

	private boolean check(String name, Vector<String> vno) {
		// TODO Auto-generated method stub
		System.out.print("Vecoter Data ");
		for (int i = 0; i < vno.size(); i++) {
			System.out.print(" " + (String) vno.elementAt(i));
			if (name.equals((String) vno.elementAt(i)))
				return true;
		}
		return false;
	}

	protected void searchBook() {
		// TODO Auto-generated method stub
		String keyword = txtsearchbook.getText();

		loadAllBooks(Optional.of(originalBookList.stream()
				.filter(e -> e.getName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getCategory().getName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getPublisher().getName().toLowerCase(Locale.ROOT)
								.contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getAuthor().getName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT)))
				.collect(Collectors.toList())));

	}

	private void initializeDependency() {
		this.publisherService = new PublisherService();
		this.employeeService = new EmployeeService();
		this.categoryService = new CategoryService();
		this.bookService = new BookService();
		this.employee = new Employee();
		this.book = new Book();
	}

	private void loadCategoryForComboBox() {
		cboCategory.addItem("- Select -");
		// System.out.println("Cate count " +
		// categoryService.findAllCategories().size());
		this.categoryList = this.categoryService.findAllCategories();
		this.categoryList.forEach(c -> cboCategory.addItem(c.getName()));
	}

	private void loadPublisherForComboBox() {
		cboPublisher.addItem("- Select -");
		// System.out.println("pub count " +
		// publisherService.findAllPublishers().size());

		this.publisherList = this.publisherService.findAllPublishers();
		this.publisherList.forEach(p -> cboPublisher.addItem(p.getName()));
	}
}
