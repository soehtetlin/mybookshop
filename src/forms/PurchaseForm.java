package forms;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.mysql.cj.util.StringUtils;
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
	private JComboBox<String> cboEmployee, cboCategory, cboPublisher;
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
	private Employee employee;
	private PurchaseDetails purchaseDetail;
	private PurchaseDetailsForm purchaseDetailForm;
	private List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
	private JLabel lblemployee, lblshowdate;
	private JButton btnaddbook;

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
		this.loadEmployeeForComboBox();
		this.loadCategoryForComboBox();
		this.loadPublisherForComboBox();
		this.initializeLoggedInUser();

	}

	private void initializeLoggedInUser() {
		// employee = CurrentUserHolder.getCurrentUser();
		System.out.println("Employee Name " + employee.getName());
		if (employee != null)
			lblemployee.setText(employee.getName());
	}

	private void showTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar now = Calendar.getInstance();
				lblshowdate.setText(dateFormat.format(now.getTime()));
			}
		}).start();
	}

	private void setTableDesign() {
		// TODO Auto-generated method stub

		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Photo");
		dtm.addColumn("Price");
		dtm.addColumn("Quantity");
		dtm.addColumn("Shelf No");
		dtm.addColumn("Author Name");
		dtm.addColumn("Category Name");
		dtm.addColumn("Publisher Name");
		dtm.addColumn("Remark");
		tblshowbooklist.setRowHeight(25);
		tblshowbooklist.setModel(dtm);
		tblshowbooklist.setDefaultEditor(getClass(), null);
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
		tblshowbooklist.getColumnModel().getColumn(9).setCellRenderer(dfcr);

//		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
//		dfcr.setHorizontalAlignment(JLabel.CENTER);
//		//tblshowbooklist.getColumnModel().getColumn(0).setCellRenderer(dfcr);
//		tblshowbooklist.setDefaultRenderer(String.class, dfcr);
//		tblshowbooklist.setModel(new DefaultTableModel(new Object[][] {
//
//		}, new String[] { "ID", "Name", "Photo", "Price", "Quantity", "Shelf No", "Author Name", "Category Name",
//				"Publihser Name", "Remark" }));

	}

	private void loadAllBooks(Optional<List<Book>> optionalBook) {
		this.dtm = (DefaultTableModel) this.tblshowbooklist.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalBookList = this.bookService.findAllBooks();
		List<Book> bookList = optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row = new Object[10];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getPhoto();
			row[3] = e.getPrice();
			row[4] = e.getStockamount();
			row[5] = e.getShelf_number();
			row[6] = e.getAuthor().getName();
			row[7] = e.getCategory().getName();
			row[8] = e.getPublisher().getName();
			row[9] = e.getRemark();
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
		// cboEmployee.setSelectedIndex(0);
		cboPublisher.setSelectedIndex(0);

	}

	private void clearAll() {
		// TODO Auto-generated method stub

		lblBookID.setText("");
		txtStockAmount.setText("");
		txtPrice.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);
		cboEmployee.setSelectedIndex(0);
		cboPublisher.setSelectedIndex(0);
		lbltotalprice.setText("");
		lbltotalquantity.setText("");

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

	// private void setTableDesignForPurchaseTable() {

//		dtmpurchase.addColumn("No");
//		dtmpurchase.addColumn("Name");
//		dtmpurchase.addColumn("Photo");
//		dtmpurchase.addColumn("Price");
//		dtmpurchase.addColumn("Quantity");
//		dtmpurchase.addColumn("Publisher Name");
//		dtmpurchase.addColumn("Remark");
//		this.tblshowPurchase.setModel(dtmpurchase);

	private void setTableDesignForPurchaseTable() {
		dtmpurchase.addColumn("No");
		dtmpurchase.addColumn("Book");
		//dtmpurchase.addColumn("Category");
		//dtmpurchase.addColumn("Author");
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

		// DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		// dfcr.setHorizontalAlignment(JLabel.CENTER);
		// dtmpurchase.getColumnModel().getColumn(0).setCellRenderer(dfcr);
//		tblshowPurchase.setDefaultRenderer(String.class, dfcr);

//		tblshowPurchase.setModel(new DefaultTableModel(new Object[][] {
//
//		}, new String[] { "No", "Book", "Category", "Author", "Quantity", "Price", "Total" }));

	}

	private void deleteRow() {
		// TODO Auto-generated method stub
		int i = tblshowPurchase.getSelectedRow();
		int serialno;
		vtotalPrice.remove(i);
		vtotalquantity.remove(i);
		if (!vno.lastElement().equals(vno.get(i))) {
			vno.remove(i);
			vid.remove(i);
			serialno = vno.indexOf(vno.get(i));
			dtmpurchase.removeRow(i);
			dtmpurchase.setValueAt(serialno + 1, i, 0);

		} else {
			vno.remove(i);
			vid.remove(i);
			dtmpurchase.removeRow(i);
		}
		tblshowPurchase.setModel(dtmpurchase);
		lbltotalprice.setText(sumAmount(vtotalPrice, 1));
		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));

	}

//	private void loadAllPurchaseDetails() {
//		this.dtmpurchase = (DefaultTableModel) this.tblshowPurchase.getModel();
//		this.dtmpurchase.getDataVector().removeAllElements();
//		this.dtmpurchase.fireTableDataChanged();
//
//		this.purchaseDetailsList.forEach(pd -> {
//			Object[] row = new Object[6];
//			row[0] = pd.getProduct().getName();
//			row[1] = pd.getProduct().getCategory().getName();
//			//row[2] = pd.getProduct().getBrand().getName();
//			row[2] = pd.getQuantity();
//			row[3] = pd.getPrice();
//			row[4] = pd.getPrice() * pd.getQuantity();
//			dtmpurchase.addRow(row);
//		});

//		dtmpurchase.addRow(new Object[] { "", "", "", "", "", total + " MMK" });
//
//		this.tblshowPurchase.setModel(dtmpurchase);
//	}

//	private void calculateTotal() {
//		total = 0;
//		this.purchaseDetailsList.forEach(pd -> {
//			total += (long) pd.getPrice() * pd.getQuantity();
//		});
//	}

	public void input_productFromPopover(Book books) {
		PurchaseDetails purchaseDetails = new PurchaseDetails();
		purchaseDetails.setBook(books);
		purchaseDetails.setQuantity(books.getStockamount());
		purchaseDetails.setPrice(books.getPrice());
		this.purchaseDetailsList.add(purchaseDetails);
		// this.loadAllPurchaseDetails();
	}

//	private void btnadd() {
//		PurchaseDetails purchaseDetails = new PurchaseDetails();
//		purchaseDetails.setPrice(book.getPrice());
//		purchaseDetails.setQuantity(book.getStockamount());
//		purchaseDetails.setBook(book);
////		if (editPurchaseDetails) {
////			purchaseDetails.setBook(selectedPurchaseDetails.getProduct());
////		}
//
//		if (purchaseDetails.getPrice() > 0 && purchaseDetails.getQuantity() > 0
//				&& purchaseDetails.getProduct() != null) {
//
////			if (!editPurchaseDetails) {
////				if (!purchaseDetailsList.contains(purchaseDetails)) {
////					purchaseDetailsList.add(purchaseDetails);
//					calculateTotal();
//					loadAllPurchaseDetails();
//					resetBookData();
//				} else {
//					JOptionPane.showMessageDialog(null, "Already Exists");
//				}
//
//				if (purchaseDetailsList.size() == 0) {
//					purchaseDetailsList.add(purchaseDetails);
//					calculateTotal();
//					loadAllPurchaseDetails();
//					resetBookData();
//				}
//			} else {
//				purchaseDetailsList = purchaseDetailsList.stream().map(pd -> {
//					PurchaseDetails target = new PurchaseDetails();
//					if (pd.getProduct() == selectedPurchaseDetails.getProduct()) {
//
//						target.setQuantity(purchaseDetails.getQuantity());
//						target.setPrice(purchaseDetails.getPrice());
//						target.setBook(purchaseDetails.getProduct());
//
//					} else
//						target = pd;
//					return target;
//				}).collect(Collectors.toList());
//				editPurchaseDetails = false;
//				selectedPurchaseDetails = null;
//				resetBookData();
//				calculateTotal();
//				loadAllPurchaseDetails();
//			}
//
//		} else {
//			JOptionPane.showMessageDialog(null, "Fill all Required fields");
//		}
//	}

	private void addBookToPurchaseTable() {

		showPurchase[0] = String.valueOf(vno.size() + 1);// show no
		// System.out.println("Item ID " + vno.elementAt(1));
		// showPurchase[1] = book.getId();

		vid.addElement(book.getId());
		//System.out.println("This is book.getiD 	 " + book.getId());

		showPurchase[1] = book.getName();// show name
		vno.addElement(showPurchase[1]);

		//showPurchase[2] = book.getCategory().getName();

		//showPurchase[3] = book.getAuthor().getName();
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
		setBounds(42, 11, 809, 458);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(0, 0, 422, 458);
		add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 258, 422, 200);
		panel.add(scrollPane);

		cboPublisher = new JComboBox();
		cboPublisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		cboPublisher.setBounds(1, 49, 100, 30);
		panel.add(cboPublisher);

		cboCategory = new JComboBox<String>();
		cboCategory.setBounds(111, 49, 100, 30);
		panel.add(cboCategory);

		cboEmployee = new JComboBox<String>();
		cboEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblemployee.setText((String) cboEmployee.getSelectedItem());
			}
		});
		cboEmployee.setBounds(221, 49, 100, 30);
		panel.add(cboEmployee);

		JLabel lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPublisher.setBounds(1, 8, 100, 30);
		panel.add(lblPublisher);

		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(111, 8, 100, 30);
		panel.add(lblCategory);

		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmployee.setBounds(221, 8, 100, 30);
		panel.add(lblEmployee);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(113, 90, 100, 30);
		panel.add(lblQuantity);
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtStockAmount = new JTextField();
		txtStockAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBookAction();
			}
		});
		txtStockAmount.setBounds(209, 91, 182, 30);
		panel.add(txtStockAmount);
		txtStockAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtStockAmount.setColumns(10);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(113, 131, 100, 30);
		panel.add(lblPrice);
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtPrice = new JTextField();
		txtPrice.setBounds(209, 131, 182, 30);
		panel.add(txtPrice);
		txtPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtPrice.setColumns(10);

		lblBookID = new JLabel("Show Book ID");
		lblBookID.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBookID.setBounds(10, 91, 100, 42);
		panel.add(lblBookID);

		JButton btnedit = new JButton("Edit");
		btnedit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

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
		});
		btnedit.setBounds(126, 178, 100, 30);
		panel.add(btnedit);
		btnedit.setMargin(new Insets(2, 2, 2, 2));
		btnedit.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnremove = new JButton("Remove");
		btnremove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblshowPurchase.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(null, "Please select row to delete!");
				} else {
					deleteRow();
					// clearAll();
					clearform();
					// System.out.println("This is remove item vid 0 value : " + vid.get(0));
					// for(int i=0;i<)
					lbltotalprice.setText(sumAmount(vtotalPrice, 1));
					lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
				}
			}

		});
		btnremove.setBounds(236, 178, 100, 30);
		panel.add(btnremove);
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
		btnaddbook.setBounds(10, 178, 100, 30);
		panel.add(btnaddbook);

		txtsearchbook = new JTextField();
		txtsearchbook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchBook();
				
			}
		});
		txtsearchbook.setBounds(0, 219, 124, 30);
		panel.add(txtsearchbook);
		txtsearchbook.setName("");
		txtsearchbook.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtsearchbook.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchBook();

			}
		});
		btnSearch.setBounds(126, 217, 100, 30);
		panel.add(btnSearch);
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSearch.setMargin(new Insets(2, 2, 2, 2));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(420, 0, 390, 285);
		add(panel_1);
		panel_1.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 37, 390, 248);
		panel_1.add(scrollPane_1);
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

				// String id = tblshowPurchase.getValueAt(tblshowPurchase.getSelectedRow(),
				// 0).toString();
				int i = tblshowPurchase.getSelectedRow();
				System.out.println("Show selected data " + (String) tblshowPurchase.getValueAt(i, 4));

				txtStockAmount.setText((String) tblshowPurchase.getValueAt(i, 2));
				txtPrice.setText((String) tblshowPurchase.getValueAt(i, 3));

			}
		});

		scrollPane_1.setViewportView(tblshowPurchase);
		lblshowdate = new JLabel("Show Current Date");
		lblshowdate.setBounds(10, 0, 182, 30);
		panel_1.add(lblshowdate);
		lblshowdate.setFont(new Font("Tahoma", Font.BOLD, 16));

		lblemployee = new JLabel("Employee Name");
		lblemployee.setBounds(239, 0, 151, 30);
		panel_1.add(lblemployee);
		lblemployee.setFont(new Font("Tahoma", Font.BOLD, 14));

		tblshowbooklist = new JTable();
		tblshowbooklist.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblshowbooklist.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tblshowbooklist.setBackground(new Color(255, 250, 240));
		tblshowbooklist.setForeground(Color.DARK_GRAY);
		tblshowbooklist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblshowbooklist.setDefaultEditor(Object.class, null);
		tblshowbooklist.setAutoCreateRowSorter(true);
		
		this.tblshowbooklist.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowbooklist.getSelectionModel().isSelectionEmpty()) {

				String id = tblshowbooklist.getValueAt(tblshowbooklist.getSelectedRow(), 0).toString();
				book = bookService.findById(id);
				lblBookID.setText(book.getId());
				// txtStockAmount.setText(String.valueOf(book.getStockamount()));
				txtStockAmount.requestFocus();
				txtPrice.setText(String.valueOf(book.getPrice()));
				cboCategory.setSelectedItem(book.getCategory().getName());
				System.out.println("Publisher CBO Selected Return value : " + book.getCategory().getName());
				cboPublisher.setSelectedItem(book.getPublisher().getName());
				// cboEmployee.setSelectedItem(employee.getName());

			}
		});

		scrollPane.setViewportView(tblshowbooklist);
		jtableheader = tblshowbooklist.getTableHeader();
		jtableheader.setBackground(SystemColor.textHighlight);
		jtableheader.setForeground(Color.WHITE);
		jtableheader.setFont(new Font("Tahoma", Font.BOLD, 12));

		tableselection();

		JButton btnshowAll = new JButton("Show All");
		btnshowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadAllBooks(Optional.empty());
				clearform();
			}
		});
		btnshowAll.setMargin(new Insets(2, 2, 2, 2));
		btnshowAll.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnshowAll.setBounds(236, 217, 100, 30);
		panel.add(btnshowAll);

		// lblshowdate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new
		// java.util.Date()));

		JPanel pnshowpurchaseitem = new JPanel();
		pnshowpurchaseitem.setBounds(420, 285, 390, 173);
		add(pnshowpurchaseitem);
		pnshowpurchaseitem.setLayout(null);

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
						// purchaseService.createPurchase();
						boolean save;
						String[] savedata1 = new String[3], savedata2 = new String[3];
						System.out.println("Show Book ID in save action " + lblBookID.getText());
						System.out.println("CBO Selected item " + cboPublisher.getSelectedItem().toString());
						savedata1[0] = cboPublisher.getSelectedItem().toString();
						savedata1[1] = lblshowdate.getText();
						savedata1[2] = cboEmployee.getSelectedItem().toString();

						System.out.println("Savedata 1 " + savedata1.toString());
						System.out.println("Show date " + lblshowdate.getText());
						purchaseService.createPurchase(savedata1);
						for (int i = 0; i < vno.size(); i++) {
							savedata2[0] = (String) tblshowPurchase.getValueAt(i, 2);// get quantity
							savedata2[1] = (String) tblshowPurchase.getValueAt(i, 3);// get price
							savedata2[2] = (String) vid.elementAt(i);
							System.out.println("Book id " + i + " show " + (savedata2[2]));
							purchaseService.createPurchaseDetails(savedata2);
						}

						clearAll();
						loadAllBooks(Optional.empty());

						// Purchase purchaseitem;
//					vid.size();
//					for(int i=0;i<vid.size();i++) {
//						book.setId((String) vid.elementAt(i));
//						purchaseDetail.setQuantity(Integer.valueOf(vtotalquantity.elementAt(i)));
//						purchaseService.createPurchase();
//					}
//					
//					purchaseitem.setPurchaseDate(LocalDateTime.now());
//					
//					Optional<Publisher> selectedPublisher = publisherList.stream()
//							.filter(s -> s.getName().equals(cboPublisher.getSelectedItem())).findFirst();
//					if (selectedPublisher.isPresent()) {
//						if (employee != null) {
//							Purchase purchase = new Purchase();
//							purchase.setEmployee(employee);
//							purchase.setPublisher(selectedPublisher.get());
//							purchase.setDescription("Testing");
//							purchase.setPurchaseDate(LocalDateTime.now());
//							purchaseService.createPurchase(purchase);
//							purchaseService.createPurchaseDetails(null);
////							if (purchaseDetailsList.size() != 0) {
////								purchaseService.createPurchaseDetails(purchaseDetailsList);
//								JOptionPane.showMessageDialog(null, "Success");
//							} else {
//								JOptionPane.showMessageDialog(null, "Add some product for making purchase");
//							}
//						}

					} else {
						JOptionPane.showMessageDialog(null, "Choose Supplier");
					}
				}

				// save = purchaseService.createPurchase(purchase);

			}

		});
		btnSave.setMargin(new Insets(2, 2, 2, 2));
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSave.setBounds(10, 115, 100, 30);
		pnshowpurchaseitem.add(btnSave);

		JLabel lblTotalAmount = new JLabel("Total Amount");
		lblTotalAmount.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalAmount.setBounds(10, 64, 202, 30);
		pnshowpurchaseitem.add(lblTotalAmount);

		JLabel lblTotalQuantity = new JLabel("Total Quantity");
		lblTotalQuantity.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalQuantity.setBounds(10, 23, 202, 30);
		pnshowpurchaseitem.add(lblTotalQuantity);

		lbltotalquantity = new JLabel("0");
		lbltotalquantity.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbltotalquantity.setBounds(182, 23, 151, 30);
		pnshowpurchaseitem.add(lbltotalquantity);

		lbltotalprice = new JLabel("0");

		lbltotalprice.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbltotalprice.setBounds(182, 64, 127, 30);
		pnshowpurchaseitem.add(lbltotalprice);

		JLabel lblTotalQuantity_1_1 = new JLabel("Kyats");
		lblTotalQuantity_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalQuantity_1_1.setBounds(314, 64, 80, 30);
		pnshowpurchaseitem.add(lblTotalQuantity_1_1);

	}

	private void tableselection() {
		// TODO Auto-generated method stub

	

	}

	protected void addBookAction() {
	
			if (cboPublisher.getSelectedItem().equals("- Select -") || cboCategory.getSelectedIndex() == 0
					|| cboEmployee.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(btnaddbook,
						"Please select both publisher name, category name and employee name!");
				cboPublisher.requestFocus();
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

	private void loadEmployeeForComboBox() {
		cboEmployee.addItem("- Select -");
		// System.out.println("author count " +
		// employeeService.findAllAuthors().size());

		this.employeelist = this.employeeService.findAllEmployees();
		this.employeelist.forEach(a -> cboEmployee.addItem(a.getName()));
	}

	private void loadPublisherForComboBox() {
		cboPublisher.addItem("- Select -");
		// System.out.println("pub count " +
		// publisherService.findAllPublishers().size());

		this.publisherList = this.publisherService.findAllPublishers();
		this.publisherList.forEach(p -> cboPublisher.addItem(p.getName()));
	}
}
