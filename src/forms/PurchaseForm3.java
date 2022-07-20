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

public class PurchaseForm3 extends JPanel {
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
	private List<Book> bookList = new ArrayList<>();
	private List<Employee> employeelist;
	private List<Publisher> publisherList;
	private DefaultTableModel dtm = new DefaultTableModel();
	private DefaultTableModel dtmpurchase = new DefaultTableModel();
	private CategoryService categoryService;
	private EmployeeService employeeService;
	private PublisherService publisherService;
	private BookService bookService;
	private Book selectedbook;
	private Purchase purchase;
	private JButton btnshowAll;
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
	private PurchaseDetailForm purchaseDetailForm;
	private List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
	private JLabel lblemployee, lblshowdate;
	private JButton btnaddbook;
	private long total;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	/**
	 * Create the panel.
	 */
	public PurchaseForm3() {
		initialize();
		initializeDependency();
		this.setTableDesign();
		this.setTableDesignForPurchaseTable();
		this.showTodayDate();
		this.loadAllBooks(Optional.empty());
		this.loadAllPurchaseDetails();
		this.loadEmployeeForComboBox();
		this.loadCategoryForComboBox();
		this.loadPublisherForComboBox();
		this.initializeLoggedInUser();

	}

	private void initializeLoggedInUser() {
		employee = CurrentUserHolder.getCurrentUser();
		employee = new Employee();
		System.out.println("Employee Name initialize " + employee.getName());
		if (employee != null)
			System.out.println("Employee Name initialize inside if con :" + employee.getName());
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
		this.tblshowbooklist.setRowHeight(25);
		this.tblshowbooklist.setModel(dtm);
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

	}

	private void loadAllBooks(Optional<List<Book>> optionalBook) {
		this.dtm = (DefaultTableModel) this.tblshowbooklist.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalBookList = this.bookService.findAllBooks();
		bookList = optionalBook.orElseGet(() -> originalBookList);
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

	private void setTableDesignForPurchaseTable() {
		dtmpurchase.addColumn("No");
		dtmpurchase.addColumn("Book");
		dtmpurchase.addColumn("Quantity");
		dtmpurchase.addColumn("Price");
		dtmpurchase.addColumn("Amount");
		this.tblshowPurchase.setModel(dtmpurchase);
		this.tblshowPurchase.setRowHeight(40);
		this.tblshowPurchase.setModel(dtmpurchase);
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

	private void loadAllPurchaseDetails() {
		this.dtm = (DefaultTableModel) this.tblshowPurchase.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.purchaseDetailsList.forEach(pd -> {
			Object[] row = new Object[6];
			row[0] = "1";
			row[1] = pd.getBook().getName();
			row[2] = pd.getQuantity();
			row[3] = pd.getPrice();
			row[4] = pd.getPrice() * pd.getQuantity();
			dtm.addRow(row);
		});

		this.tblshowPurchase.setModel(dtm);
	}

	private void calculateTotal() {
		// TODO Auto-generated method stub
		total = 0;
		totalquantity = 0;
		this.purchaseDetailsList.forEach(pd -> {
			total += (long) pd.getPrice() * pd.getQuantity();
			totalquantity += pd.getQuantity();
		});

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
	}

	private void deleteRow() {
		// TODO Auto-generated method stub
		int i = tblshowPurchase.getSelectedRow();
		System.out.println("Get selected row index in delete row method: " + i);
		String serialno = vid.elementAt(i);
		System.out.println("Vid.elementAt before remove in delete row : " + serialno);
		vtotalPrice.remove(i);
		vtotalquantity.remove(i);

		if (!vno.lastElement().equals(vno.get(i))) {

			vno.remove(i);
			vid.remove(i);

			System.out.println("vid after remove in delete row :" + vid.elementAt(i));
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

	public void input_productFromPopover(Book books) {
		PurchaseDetails purchaseDetails = new PurchaseDetails();
		purchaseDetails.setBook(books);
		purchaseDetails.setQuantity(books.getStockamount());
		purchaseDetails.setPrice(books.getPrice());
		this.purchaseDetailsList.add(purchaseDetails);
	}

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
		setBounds(42, 11, 790, 435);

		JPanel plshowBook = new JPanel();
		plshowBook.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane = new JScrollPane();

		cboPublisher = new JComboBox();
		cboPublisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		cboCategory = new JComboBox<String>();

		cboEmployee = new JComboBox<String>();
		cboEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblemployee.setText((String) cboEmployee.getSelectedItem());
			}
		});

		JLabel lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtStockAmount = new JTextField();
		txtStockAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBookAction();
			}
		});
		txtStockAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtStockAmount.setColumns(10);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtPrice = new JTextField();
		txtPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBookAction();
			}
		});
		txtPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtPrice.setColumns(10);

		lblBookID = new JLabel("Show Book ID");
		lblBookID.setFont(new Font("Tahoma", Font.BOLD, 14));

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
					// addBookToPurchaseTable();
					lbltotalprice.setText(sumAmount(vtotalPrice, 1));
					lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
					resetBookData();
				}
			}
		});
		btnedit.setMargin(new Insets(2, 2, 2, 2));
		btnedit.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnremove = new JButton("Remove");
		btnremove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblshowPurchase.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(null, "Please select book to delete!");
				} else {

					purchaseDetailsList.remove(selectedPurchaseDetails);
					resetBookData();
					calculateTotal();
					loadAllPurchaseDetails();
					// deleteRow();
				}
			}

		});
		btnremove.setMargin(new Insets(2, 2, 2, 2));
		btnremove.setFont(new Font("Tahoma", Font.BOLD, 14));

		btnaddbook = new JButton(selectedPurchaseDetails != null ? "Update" : "Add");
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

		JPanel plshowPurchase = new JPanel();
		plshowPurchase.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

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

		

				String bookName = tblshowPurchase.getValueAt(tblshowPurchase.getSelectedRow(), 1).toString();
				selectedPurchaseDetails = purchaseDetailsList.stream()
						.filter(pd -> pd.getBook().getName().equals(bookName)).findFirst().get();
				lblBookID.setText(selectedPurchaseDetails.getId());
				txtStockAmount.setText(String.valueOf(selectedPurchaseDetails.getQuantity()));
				txtStockAmount.requestFocus();
				txtStockAmount.selectAll();
				txtPrice.setText(String.valueOf(selectedPurchaseDetails.getPrice()));
				cboCategory.setSelectedItem(selectedPurchaseDetails.getBook().getCategory().getName());
				cboPublisher.setSelectedItem(selectedPurchaseDetails.getBook().getPublisher().getName());
				editPurchaseDetails = true;

			}
		});

		scrollPane_1.setViewportView(tblshowPurchase);
		lblshowdate = new JLabel("Show Current Date");
		lblshowdate.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblshowdate.setVisible(false);

		lblemployee = new JLabel("Employee Name");
		lblemployee.setFont(new Font("Tahoma", Font.BOLD, 14));

		tblshowbooklist = new JTable();
		tblshowbooklist.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		cLayout.setTable(tblshowbooklist);
		this.tblshowbooklist.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowbooklist.getSelectionModel().isSelectionEmpty()) {
			
				String id = tblshowbooklist.getValueAt(tblshowbooklist.getSelectedRow(), 0).toString();
				selectedbook = bookList.stream().filter(pd -> pd.getId().equals(id)).findFirst().get();
				lblBookID.setText(selectedbook.getId());
				System.out.println("Selected book bname : " + selectedbook.getName());
				txtStockAmount.setText(String.valueOf(selectedbook.getStockamount()));
				txtStockAmount.requestFocus();
				txtStockAmount.selectAll();

				txtPrice.setText(String.valueOf(selectedbook.getPrice()));
				cboCategory.setSelectedItem(selectedbook.getCategory().getName());
				cboPublisher.setSelectedItem(selectedbook.getPublisher().getName());
				System.out.println("selectdp product id " + selectedbook);

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
				resetBookData();
			}
		});
		btnshowAll.setMargin(new Insets(2, 2, 2, 2));
		btnshowAll.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(plshowBook, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(plshowPurchase, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(plshowPurchase, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 431,
												Short.MAX_VALUE)
										.addComponent(plshowBook, 0, 0, Short.MAX_VALUE))
								.addGap(0)));
		GroupLayout gl_plshowBook = new GroupLayout(plshowBook);
		gl_plshowBook.setHorizontalGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_plshowBook.createSequentialGroup().addGap(1)
						.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_plshowBook.createSequentialGroup().addGap(1)
						.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(cboEmployee, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_plshowBook.createSequentialGroup().addGap(3).addComponent(lblBookID)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
								.addComponent(lblQuantity, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_plshowBook.createSequentialGroup().addGap(96).addComponent(txtStockAmount,
										GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_plshowBook.createSequentialGroup().addGap(96).addComponent(txtPrice,
										GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
				.addGroup(gl_plshowBook.createSequentialGroup().addGap(10)
						.addComponent(btnaddbook, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(16).addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(btnremove, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_plshowBook.createSequentialGroup()
						.addComponent(txtsearchbook, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addGap(2).addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_plshowBook.createSequentialGroup().addGap(1)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE).addGap(5)));
		gl_plshowBook.setVerticalGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_plshowBook.createSequentialGroup().addGap(6)
						.addGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
								.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(cboEmployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_plshowBook.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblQuantity, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblBookID, GroupLayout.PREFERRED_SIZE, 42,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_plshowBook.createSequentialGroup().addGap(1).addComponent(txtStockAmount,
										GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
						.addGap(10)
						.addGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
								.addComponent(txtPrice, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(17)
						.addGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
								.addComponent(btnaddbook, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnremove, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(9)
						.addGroup(gl_plshowBook.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_plshowBook.createSequentialGroup().addGap(2).addComponent(txtsearchbook,
										GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(27).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
						.addGap(5)));
		plshowBook.setLayout(gl_plshowBook);

		JPanel pnshowpurchaseitem = new JPanel();

		JButton btnSave = new JButton("Save");
		btnSave.setMnemonic('s');

		btnSave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					Optional<Publisher> selectedPublisher = publisherList.stream()
							.filter(s -> s.getName().equals(cboPublisher.getSelectedItem())).findFirst();
					System.out.println("Selected Publisher :" + selectedPublisher.get().getName());
					employee.setId("EM000000029");
					employee.setName("Soe Htet Linn");
					System.out.println("Employee " + employee.getName());
					if (selectedPublisher.isPresent()) {
						if (employee != null) {
							Purchase purchase = new Purchase();
							purchase.setEmployee(employee);
							purchase.setPublisher(selectedPublisher.get());
							purchase.setPurchaseDate(LocalDateTime.now());
							purchaseService.createPurchase(purchase);

							if (purchaseDetailsList.size() != 0) {
								purchaseService.createPurchaseDetails(purchaseDetailsList);
								JOptionPane.showMessageDialog(null, "Success");
								resetBookData();
								while (dtmpurchase.getRowCount() > 0) {
									dtmpurchase.removeRow(0);
								}
								tblshowPurchase.setModel(dtmpurchase);

							} else {
								JOptionPane.showMessageDialog(null, "Add some book for making purchase");
							}
						}

					}
				} catch (Exception ex) {
					// TODO: handle exception
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
		GroupLayout gl_plshowPurchase = new GroupLayout(plshowPurchase);
		gl_plshowPurchase.setHorizontalGroup(gl_plshowPurchase.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_plshowPurchase.createSequentialGroup()
						.addGroup(gl_plshowPurchase.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane_1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addComponent(pnshowpurchaseitem, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 390,
										Short.MAX_VALUE)
								.addGroup(gl_plshowPurchase.createSequentialGroup().addGap(10)
										.addComponent(lblshowdate, GroupLayout.PREFERRED_SIZE, 182,
												GroupLayout.PREFERRED_SIZE)
										.addGap(47).addComponent(lblemployee, GroupLayout.PREFERRED_SIZE, 151,
												GroupLayout.PREFERRED_SIZE)))
						.addGap(51)));
		gl_plshowPurchase.setVerticalGroup(gl_plshowPurchase.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_plshowPurchase.createSequentialGroup()
						.addGroup(gl_plshowPurchase.createParallelGroup(Alignment.LEADING)
								.addComponent(lblshowdate, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblemployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(7).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(pnshowpurchaseitem, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		plshowPurchase.setLayout(gl_plshowPurchase);
		setLayout(groupLayout);

	}

	private void tableselection() {
		// TODO Auto-generated method stub

	}

	protected void addBookAction() {
		if (cboEmployee.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(btnaddbook, "Please select employee name");
			cboEmployee.requestFocus();
		} else {
			PurchaseDetails purchaseDetails = new PurchaseDetails();
			purchaseDetails.setPrice(Integer.parseInt(txtPrice.getText()));
			purchaseDetails.setQuantity(Integer.parseInt(txtStockAmount.getText()));
			purchaseDetails.setBook(selectedbook);
			System.out.println("PurchaseDetails book object " + purchaseDetails.getBook());
			if (editPurchaseDetails) {
				purchaseDetails.setBook(selectedPurchaseDetails.getBook());
			}

			if (purchaseDetails.getPrice() > 0 && purchaseDetails.getQuantity() > 0
					&& purchaseDetails.getBook() != null) {

				if (!editPurchaseDetails) {
					if (!purchaseDetailsList.contains(purchaseDetails)) {
						System.out.println("Selected Book ID " + selectedbook);
						System.out.println("PurchaseDet inside !editpurc " + purchaseDetails.getBook());
//					System.out.println("Purchase Detail size " + purchaseDetailsList.size());
//					System.out.println("Add new item" + purchaseDetailsList.contains(purchaseDetails));
//					System.out.println();
						purchaseDetailsList.add(purchaseDetails);
						for (int i = 0; i < purchaseDetailsList.size(); i++) {
							System.out.println(
									" purchasl list " + i + " value " + purchaseDetailsList.get(i).getBook().getName());
							System.out.println(
									"purchase list object " + i + " value " + purchaseDetailsList.get(i).getBook());

						}
						calculateTotal();
						lbltotalprice.setText(String.valueOf(total));
						lbltotalquantity.setText(String.valueOf(totalquantity));
						loadAllPurchaseDetails();
						resetBookData();
					} else {
						JOptionPane.showMessageDialog(null, "Already Exists");
					}

					if (purchaseDetailsList.size() == 0) {
						purchaseDetailsList.add(purchaseDetails);
						calculateTotal();
						loadAllPurchaseDetails();
						resetBookData();
					}
				} else {
					purchaseDetailsList = purchaseDetailsList.stream().map(pd -> {
						PurchaseDetails target = new PurchaseDetails();
						if (pd.getBook() == selectedPurchaseDetails.getBook()) {

							target.setQuantity(purchaseDetails.getQuantity());
							target.setPrice(purchaseDetails.getPrice());
							target.setBook(purchaseDetails.getBook());

						} else
							target = pd;
						return target;
					}).collect(Collectors.toList());
					editPurchaseDetails = false;
					selectedPurchaseDetails = null;
					resetBookData();
					calculateTotal();
					loadAllPurchaseDetails();
				}

			} else {
				JOptionPane.showMessageDialog(null, "Fill all Required fields");
			}

		}
	}

	private void resetBookData() {
		// TODO Auto-generated method stub
		txtPrice.setText("");
		txtStockAmount.setText("");
		lblBookID.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);
		cboPublisher.setSelectedIndex(0);
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
	}

	private void loadCategoryForComboBox() {
		cboCategory.addItem("- Select -");
		this.categoryList = this.categoryService.findAllCategories();
		this.categoryList.forEach(c -> cboCategory.addItem(c.getName()));
	}

	private void loadEmployeeForComboBox() {
		cboEmployee.addItem("- Select -");
		this.employeelist = this.employeeService.findAllEmployees();
		this.employeelist.forEach(a -> cboEmployee.addItem(a.getName()));
	}

	private void loadPublisherForComboBox() {
		cboPublisher.addItem("- Select -");
		this.publisherList = this.publisherService.findAllPublishers();
		this.publisherList.forEach(p -> cboPublisher.addItem(p.getName()));
	}

}
