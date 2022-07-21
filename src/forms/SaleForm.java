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
import entities.Customer;
import entities.Employee;
import entities.Customer;
import entities.Sale;
import entities.SaleDetails;
import services.EmployeeService;
import services.BookService;
import services.CategoryService;
import services.CustomerService;
import services.CustomerService;
import services.SaleService;
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

import javax.swing.DefaultComboBoxModel;
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

public class SaleForm extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtsearchbook;
	private JTable tblshowbooklist;
	private JTable tbldisplaysaleitem;
	private JTextField txtStockAmount;
	private JTextField txtPrice;
	private JComboBox<String> cboCategory, cboCustomer;
	private List<Category> categoryList;
	private List<Employee> employeelist;
	private List<Customer> customerList;
	private DefaultTableModel dtm = new DefaultTableModel();
	private DefaultTableModel dtmsale = new DefaultTableModel();
	private CategoryService categoryService;
	private EmployeeService employeeService;
	private CustomerService customerService;
	private BookService bookService;
	private Book book;
	private JButton btnshowAll;
	private Sale sale;
	private JLabel lblshowdate = new JLabel();
	private SaleDetails selectedSaleDetails;
	private SaleService saleService = new SaleService();
	private boolean editSaleDetails = false; // private SaleService saleService;
	private List<Book> originalBookList = new ArrayList<>();
	private Vector<String> vno = new Vector<>(), vtotalPrice = new Vector<>(), vtotalquantity = new Vector<>(),
			vid = new Vector<>();
	private String[] showSale = new String[11];
	private JLabel lbltotalprice;
	private JLabel lbltotalquantity;
	private int totalquantity;
	private JLabel lblBookID;
	private int serialno;
	private Employee employee;
	private SaleDetails saleDetail;
	private SaleDetailForm saleDetailForm;
	private List<SaleDetails> saleDetailsList = new ArrayList<>();
	private JLabel lblemployee, lblshowStatus;
	private JButton btnaddbook;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	List<Book> b = new  ArrayList<>();

	/**
	 * Create the panel.
	 */
	public SaleForm() {
		initialize();
		initializeDependency();
		this.setTableDesign();
		this.setTableDesignForSaleTable();
		// txtsearchbook.requestFocusInWindow();
		this.showTodayDate();
		this.loadAllBooks(Optional.empty());
		// this.loadAllSaleDetails();
		this.loadCategoryForComboBox();
		this.loadCustomerForComboBox();
		this.initializeLoggedInUser();

	}

	private void initializeLoggedInUser() {
		employee = CurrentUserHolder.getCurrentUser();
		if (employee != null)
			lblemployee.setText(employee.getName());
		else {
			lblemployee.setText("Soe Htet Linn");
		}
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
		List<Book> bookList = optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row = new Object[10];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getPhoto();
			row[3] = e.getSale_price();
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
	
	private void loadAllBooksByCategory(String s) {
		this.dtm = (DefaultTableModel) this.tblshowbooklist.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();
		List<Book> bookList = new ArrayList<>();
		bookList = bookService.findBookByCategoryName(s);
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

	}

	private void clearAll() {
		// TODO Auto-generated method stub

		lblBookID.setText("");
		txtStockAmount.setText("");
		txtPrice.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);
		cboCustomer.setSelectedIndex(0);
		lbltotalprice.setText("");
		lbltotalquantity.setText("");

		while (dtmsale.getRowCount() > 0) {
			dtmsale.removeRow(0);
		}
		tbldisplaysaleitem.setModel(dtmsale);
		vtotalquantity.removeAllElements();
		vno.removeAllElements();
		vid.removeAllElements();
		vtotalPrice.removeAllElements();

		// cboBrand.setSelectedIndex(0);

	}

	// private void setTableDesignForSaleTable() {

//		dtmsale.addColumn("No");
//		dtmsale.addColumn("Name");
//		dtmsale.addColumn("Photo");
//		dtmsale.addColumn("Price");
//		dtmsale.addColumn("Quantity");
//		dtmsale.addColumn("Customer Name");
//		dtmsale.addColumn("Remark");
//		this.tbldisplaysaleitem.setModel(dtmsale);

	private void setTableDesignForSaleTable() {
		dtmsale.addColumn("No");
		dtmsale.addColumn("Book");
		// dtmsale.addColumn("Category");
		// dtmsale.addColumn("Author");
		dtmsale.addColumn("Quantity");
		dtmsale.addColumn("Price");
		dtmsale.addColumn("Amount");
		this.tbldisplaysaleitem.setModel(dtmsale);

		tbldisplaysaleitem.setRowHeight(40);

		tbldisplaysaleitem.setModel(dtmsale);

		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		tbldisplaysaleitem.getColumnModel().getColumn(0).setCellRenderer(dfcr);
		tbldisplaysaleitem.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		tbldisplaysaleitem.getColumnModel().getColumn(2).setCellRenderer(dfcr);
		tbldisplaysaleitem.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		tbldisplaysaleitem.getColumnModel().getColumn(4).setCellRenderer(dfcr);

		tbldisplaysaleitem.getColumnModel().getColumn(0).setPreferredWidth(50);
		tbldisplaysaleitem.getColumnModel().getColumn(1).setPreferredWidth(80);
		tbldisplaysaleitem.getColumnModel().getColumn(2).setPreferredWidth(80);
		tbldisplaysaleitem.getColumnModel().getColumn(3).setPreferredWidth(80);
		tbldisplaysaleitem.getColumnModel().getColumn(4).setPreferredWidth(100);

		// DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		// dfcr.setHorizontalAlignment(JLabel.CENTER);
		// dtmsale.getColumnModel().getColumn(0).setCellRenderer(dfcr);
//		tbldisplaysaleitem.setDefaultRenderer(String.class, dfcr);

//		tbldisplaysaleitem.setModel(new DefaultTableModel(new Object[][] {
//
//		}, new String[] { "No", "Book", "Category", "Author", "Quantity", "Price", "Total" }));

	}

//for (int i = tbldisplaysaleitem.getRowCount() - 1; i >= 0; i--) {
//   dtmsale.removeRow(i);
//}
//
//for (int i = 0; i < list.size(); i++) {
//   Student s = list.get(i);
//   Object[] newRow = new Object[] {i, s.getName(),s.getAge(), s.getClass()};
//   model.addRow(newRow);
//}
//}

	private void deleteRow() {
		// TODO Auto-generated method stub
		int i = tbldisplaysaleitem.getSelectedRow();

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
			dtmsale.removeRow(i);

			for (int y = i; y < dtmsale.getRowCount(); y++) {
				System.out.println("inside loop y value " + y);

				dtmsale.setValueAt(y + 1, y, 0); // setValueAt(data,row,column)
			}

		} else {
			vno.remove(i);
			vid.remove(i);
			dtmsale.removeRow(i);
		}

		tbldisplaysaleitem.setModel(dtmsale);
		lbltotalprice.setText(sumAmount(vtotalPrice, 1));
		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
	}

	public void input_productFromPopover(Book books) {
		SaleDetails saleDetails = new SaleDetails();
		saleDetails.setBook(books);
		saleDetails.setQuantity(books.getStockamount());
		saleDetails.setPrice(books.getPrice());
		this.saleDetailsList.add(saleDetails);
	}

	private void addBookToSaleTable() {

		showSale[0] = String.valueOf(vno.size() + 1);// show no

		vid.addElement(book.getId());

		showSale[1] = book.getName();// show name
		vno.addElement(showSale[1]);
		showSale[2] = txtStockAmount.getText();// show quantity
		totalquantity = Integer.valueOf(showSale[2]) + (book.getStockamount());
		book.setStockamount(totalquantity);

		showSale[3] = txtPrice.getText();// show price

		int totalamount = (Integer.valueOf(showSale[2]) * Integer.valueOf(showSale[3]));
		showSale[4] = String.valueOf(totalamount);// show total amount
		vtotalPrice.addElement((showSale[4]));
		vtotalquantity.addElement(showSale[2]);
		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));

		lbltotalprice.setText(sumAmount(vtotalPrice, 1));// show total amount

		dtmsale.addRow(showSale);
		this.tbldisplaysaleitem.setModel(dtmsale);

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

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane = new JScrollPane();

		cboCustomer = new JComboBox();
		
		
		
		
		cboCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Customer c = customerService.findAllCustomersByCustomerName(cboCustomer.getSelectedItem().toString());
				if(c.getActive()==1) {
					lblshowStatus.setText("Active");
				}else {
					lblshowStatus.setText("Expire");
				}
				
				
				
				
			}
		});

		cboCategory = new JComboBox<String>();
		cboCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				 if(cboCategory.getSelectedIndex()==0) {
					 loadAllBooks(Optional.empty());
				 }else {
					 loadAllBooksByCategory(cboCategory.getSelectedItem().toString());
				 }
				
			}
		});
		
		//comboFilter(cboCategory.getSelectedItem().toString());

		JLabel lblcustomer = new JLabel("Customer");
		lblcustomer.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));

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
		txtPrice.setEditable(false);
		txtPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtPrice.setColumns(10);

		lblBookID = new JLabel("");
		lblBookID.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnedit = new JButton("Edit");
		btnedit.setVisible(false);
		btnedit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (tbldisplaysaleitem.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(btnedit, "Please select to edit!");

				} else if (txtPrice.getText().equals("") || txtPrice.getText().isEmpty()) {
					JOptionPane.showMessageDialog(btnedit, "Please Enter Price!");
					txtPrice.requestFocus();
					txtPrice.selectAll();
				} else if (txtStockAmount.getText().equals("") || txtStockAmount.getText().isEmpty()) {
					JOptionPane.showMessageDialog(btnedit, "Please Enter Quantity!");
					txtStockAmount.requestFocus();
					txtStockAmount.selectAll();
				} else if (Integer.valueOf(txtStockAmount.getText()) > book.getStockamount()) {
					System.out.println("Quantity = " + txtStockAmount.getText() + " and StockAmount =" + book.getStockamount());
					JOptionPane.showMessageDialog(null, "Quantity is over stock amount!");
					txtStockAmount.requestFocus();
				} else {

					deleteRow();
					addBookToSaleTable();
					lbltotalprice.setText(sumAmount(vtotalPrice, 1));
					lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
					clearform();
				}
			}
		});
		btnedit.setMargin(new Insets(2, 2, 2, 2));
		btnedit.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnremove = new JButton("Remove");
		btnremove.setVisible(false);
		btnremove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tbldisplaysaleitem.getSelectedRow() < 0) {
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
		tbldisplaysaleitem = new JTable();
		tbldisplaysaleitem.setFont(new Font("Tahoma", Font.BOLD, 14));
		tbldisplaysaleitem.setBackground(new Color(255, 250, 240));
		tbldisplaysaleitem.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbldisplaysaleitem.setForeground(Color.DARK_GRAY);
		tbldisplaysaleitem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbldisplaysaleitem.setDefaultEditor(Object.class, null);
		tbldisplaysaleitem.setAutoCreateRowSorter(true);
		JTableHeader jtableheader = tbldisplaysaleitem.getTableHeader();
		jtableheader.setBackground(SystemColor.textHighlight);
		jtableheader.setForeground(Color.white);
		jtableheader.setFont(new Font("Tahoma", Font.BOLD, 14));

		this.tbldisplaysaleitem.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tbldisplaysaleitem.getSelectionModel().isSelectionEmpty()) {

				tblshowbooklist.clearSelection();
				btnedit.setVisible(true);
				btnremove.setVisible(true);
				btnaddbook.setVisible(false);
				btnshowAll.setVisible(false);
				// String id =
				// tbldisplaysaleitem.getValueAt(tbldisplaysaleitem.getSelectedRow(),
				// 0).toString();
				int i = tbldisplaysaleitem.getSelectedRow();
				System.out.println("Show selected data " + (String) tbldisplaysaleitem.getValueAt(i, 4));
				// book.setId(((String)tbldisplaysaleitem.getValueAt(i, 1)));
				// System.out.println("Book ID in click action" + book.getId());
				txtStockAmount.setText((String) tbldisplaysaleitem.getValueAt(i, 2));
				txtStockAmount.requestFocus();
				txtStockAmount.selectAll();
				txtPrice.setText((String) tbldisplaysaleitem.getValueAt(i, 3));
				lblBookID.setText(vid.elementAt(i));
				cboCategory.setSelectedItem(book.getCategory().getName());
				book = bookService.findById(vid.elementAt(i));
				System.out.println("Sale Item Table Book ID " + lblBookID.getText()+" Quantity = " + book.getStockamount());
				
				book.setId(vid.elementAt(i));
				book.setName(vno.elementAt(i));

			}
		});

		scrollPane_1.setViewportView(tbldisplaysaleitem);

		lblemployee = new JLabel("Employee Name");
		lblemployee.setFont(new Font("Tahoma", Font.BOLD, 14));

		tblshowbooklist = new JTable();
		tblshowbooklist.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		cLayout.setTable(tblshowbooklist);

		this.tblshowbooklist.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowbooklist.getSelectionModel().isSelectionEmpty()) {

				tbldisplaysaleitem.clearSelection();
				btnedit.setVisible(false);
				btnremove.setVisible(false);
				btnaddbook.setVisible(true);
				btnshowAll.setVisible(true);

				String id = tblshowbooklist.getValueAt(tblshowbooklist.getSelectedRow(), 0).toString();
				book = bookService.findById(id);
				
				lblBookID.setText(book.getId());
				// txtStockAmount.setText(String.valueOf(book.getStockamount()));
				txtStockAmount.requestFocus();

				txtPrice.setText(String.valueOf(book.getPrice()));
				//txtPrice.setEditable(false);
				cboCategory.setEnabled(false);
				cboCategory.setSelectedItem(book.getCategory().getName());
				System.out.println("Customer CBO Selected Return value : " + book.getCategory().getName());
				// cboCustomer.setSelectedItem(book.getCustomer().getName());
				// cboEmployee.setSelectedItem(employee.getName());

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
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 388, Short.MAX_VALUE).addGap(13)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel, 0, 0, Short.MAX_VALUE)
								.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE))
						.addGap(0)));

		lblshowStatus = new JLabel("Active or Expire");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(1)
						.addComponent(lblcustomer, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(71)
						.addComponent(lblshowStatus, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup().addGap(1)
						.addComponent(cboCustomer, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(110))
				.addGroup(gl_panel.createSequentialGroup().addGap(3).addComponent(lblBookID)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblQuantity, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createSequentialGroup().addGap(96).addComponent(txtStockAmount,
										GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addGap(96).addComponent(txtPrice,
										GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
				.addGroup(gl_panel.createSequentialGroup().addGap(10)
						.addComponent(btnaddbook, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(16).addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(btnremove, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(txtsearchbook, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addGap(2).addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup().addGap(1)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE).addGap(5)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(6)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblcustomer, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblshowStatus, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)))
						.addGap(11)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(cboCustomer, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
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
								.addComponent(btnremove, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(9)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(txtsearchbook,
										GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(27).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
						.addGap(5)));
		panel.setLayout(gl_panel);

		JPanel pnshowsaleitem = new JPanel();

		JButton btnSave = new JButton("Save");
		btnSave.setMnemonic('s');

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (vno.size() == 0) {
					JOptionPane.showMessageDialog(null, "There is no item for sale!");
					txtsearchbook.requestFocus();
				} else {
					if (JOptionPane.showConfirmDialog(null, "Are you sure you want to save!", "Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						String[] savedata1 = new String[3], savedata2 = new String[3];
						System.out.println("Show Book ID in save action " + lblBookID.getText());
						System.out.println("CBO Selected item " + cboCustomer.getSelectedItem().toString());
						savedata1[0] = cboCustomer.getSelectedItem().toString();
						savedata1[1] = LocalDateTime.now().toString();
						savedata1[2] = lblemployee.getText();
						saleService.createSale(savedata1);
						customerService.updateCustomerLatestDateUse(cboCustomer.getSelectedItem().toString());
						for (int i = 0; i < vno.size(); i++) {
							savedata2[0] = (String) tbldisplaysaleitem.getValueAt(i, 2);// get quantity
							savedata2[1] = (String) tbldisplaysaleitem.getValueAt(i, 3);// get price
							savedata2[2] = (String) vid.elementAt(i);
							System.out.println("Book id " + i + " show " + (savedata2[2]));
							saleService.createSaleDetails(savedata2);
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
		GroupLayout gl_pnshowsaleitem = new GroupLayout(pnshowsaleitem);
		gl_pnshowsaleitem.setHorizontalGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnshowsaleitem.createSequentialGroup().addGap(10).addGroup(gl_pnshowsaleitem
						.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_pnshowsaleitem.createSequentialGroup().addGroup(gl_pnshowsaleitem
								.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTotalQuantity, GroupLayout.PREFERRED_SIZE, 202,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_pnshowsaleitem.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblTotalAmount,
												GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_pnshowsaleitem.createSequentialGroup()
												.addComponent(lbltotalprice, GroupLayout.PREFERRED_SIZE, 84,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
												.addComponent(lblTotalQuantity_1_1))
										.addComponent(lbltotalquantity, GroupLayout.PREFERRED_SIZE, 151,
												GroupLayout.PREFERRED_SIZE))))
						.addContainerGap()));
		gl_pnshowsaleitem
				.setVerticalGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnshowsaleitem.createSequentialGroup().addGap(23)
								.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblTotalQuantity, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lbltotalquantity, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE))
								.addGap(11)
								.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.BASELINE)
										.addComponent(lbltotalprice, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblTotalAmount, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblTotalQuantity_1_1, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)));
		pnshowsaleitem.setLayout(gl_pnshowsaleitem);

		lblshowdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(pnshowsaleitem, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(lblshowdate, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE).addComponent(
										lblemployee, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)))
						.addGap(51))
				.addGroup(Alignment.LEADING,
						gl_panel_1.createSequentialGroup()
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup()
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblemployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblshowdate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(pnshowsaleitem, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		panel_1.setLayout(gl_panel_1);
		setLayout(groupLayout);

	}

	private void tableselection() {
		// TODO Auto-generated method stub

	}

	protected void addBookAction() {

		if (cboCustomer.getSelectedItem().equals("- Select -")) {
			JOptionPane.showMessageDialog(btnaddbook, "Please select customer id!");
			cboCustomer.requestFocus();
		} else if(lblBookID.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please Select Book");
			txtsearchbook.requestFocus();
		}
		else if (!Checking.checktxtquantity(txtStockAmount.getText())) {
			txtStockAmount.requestFocus();
			txtStockAmount.selectAll();
			JOptionPane.showMessageDialog(null, "Please enter Quantity Correctly!");
			// System.out.println("Inside the txtprice listener");
		} else if (check(book.getName(), vno)) {
			System.out.println(book.getName() + "inside the check if " + vno);
			JOptionPane.showMessageDialog(null, "This item is already added");
			clearform();
		} else if (Integer.valueOf(txtStockAmount.getText()) > book.getStockamount()) {
			JOptionPane.showMessageDialog(null, "Quantity is over stock amount!");
			txtStockAmount.requestFocus();
		} else if (null != book && !book.getId().isBlank()) {
			System.out.println("Book Name : " + book.getName());

			addBookToSaleTable();

			clearform();
			loadAllBooks(Optional.empty());
			txtsearchbook.requestFocus();

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
		this.customerService = new CustomerService();
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
		this.categoryList.forEach(c -> cboCategory.addItem(c.getId()));
	}

	private void loadCustomerForComboBox() {
		cboCustomer.addItem("- Select -");
		// System.out.println("pub count " +
		// customerService.findAllCustomers().size());

		this.customerList = this.customerService.findAllCustomers();
		this.customerList.forEach(p -> cboCustomer.addItem(p.getId()));
	}
	
	public void comboFilter(String enteredText) {


	    List<String> filterArray= new ArrayList<String>();
	    for (int i = 0; i < customerList.size(); i++) {
	        if (customerList.get(i).getName().toLowerCase().contains(enteredText.toLowerCase())) {
	            filterArray.add(customerList.get(i).getName());
	        }
	    }
	    if (filterArray.size() > 0) {
	        DefaultComboBoxModel model = (DefaultComboBoxModel) cboCategory.getModel();
	        model.removeAllElements();
	        for (String s: filterArray)
	            model.addElement(s);

	        JTextField textfield = (JTextField) cboCategory.getEditor().getEditorComponent();
	        textfield.setText(enteredText);
	    }
	}
}
