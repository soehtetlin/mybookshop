package forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entities.Book;
import entities.Category;
import entities.Customer;
import entities.Employee;
import entities.SaleDetails;
import services.BookService;
import services.CategoryService;
import services.CustomerService;
import services.SaleService;
import shared.checker.Checking;
import shared.utils.CurrentUserHolder;

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
	private List<Customer> customerList;
	private DefaultTableModel dtm = new DefaultTableModel();
	private DefaultTableModel dtmsale = new DefaultTableModel();
	private CategoryService categoryService;
	private CustomerService customerService;
	private BookService bookService;
	private Book book;
	private JButton btnshowAll;
	private JLabel lblshowdate = new JLabel();
	private JLabel lblshowBookCover, lbldiscount, lbldisplaydiscount, lblshowCustomer;
	private SaleService saleService = new SaleService();
	private List<Book> originalBookList = new ArrayList<>();
	private Vector<String> vno = new Vector<>(), vtotalPrice = new Vector<>(), vtotalquantity = new Vector<>(),
			vid = new Vector<>();
	private String[] showSale = new String[11];
	private JLabel lbltotalprice;
	private JLabel lbltotalquantity;
	private int totalquantity;
	private JLabel lblBookID, lblbeforeprice;
	private int serialno;
	private Employee employee;
	private SaleDetails saleDetail;
	private List<SaleDetails> saleDetailsList = new ArrayList<>();
	private JLabel lblemployee;
	private JButton btnaddbook;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	List<Book> b = new ArrayList<>();
	private JLabel lblNewLabel_1;

	/**
	 * Create the panel.
	 */
	public SaleForm() {
		setBackground(Color.WHITE);
		initialize();
		initializeDependency();
		this.setTableDesign();
		this.setTableDesignForSaleTable();

		this.showTodayDate();
		this.loadAllBooks(Optional.empty());

		this.loadCategoryForComboBox();
		new AutoCompleteComboBox(customerList);
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

		this.originalBookList = this.bookService.findAllBooks();
		List<Book> bookList = optionalBook.orElseGet(() -> originalBookList);
		bookList.forEach(e -> {
			Object[] row = new Object[9];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getSale_price();
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

	private void loadAllBooksByCategory(String s) {
		this.dtm = (DefaultTableModel) this.tblshowbooklist.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();
		List<Book> bookList = new ArrayList<>();
		bookList = bookService.findBookByCategoryName(s);
		bookList.forEach(e -> {

			Object[] row = new Object[9];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getSale_price();
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

		lblBookID.setText("");
		txtStockAmount.setText("");
		txtPrice.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);
		lblshowBookCover.setIcon(null);
		lblshowBookCover.setText("Show selected book cover");

	}

	private void clearAll() {

		lblBookID.setText("");
		txtStockAmount.setText("");
		txtPrice.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);
		cboCustomer.setSelectedIndex(0);
		lbltotalprice.setText("0");
		lbltotalquantity.setText("0");
		lblbeforeprice.setText("");
		lblshowBookCover.setIcon(null);
		lblshowBookCover.setText("Show Selected Book Cover");

		while (dtmsale.getRowCount() > 0) {
			dtmsale.removeRow(0);
		}
		tbldisplaysaleitem.setModel(dtmsale);
		vtotalquantity.removeAllElements();
		vno.removeAllElements();
		vid.removeAllElements();
		vtotalPrice.removeAllElements();

	}

	private void setTableDesignForSaleTable() {
		dtmsale.addColumn("No");
		dtmsale.addColumn("Book");

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

	}

	private void deleteRow() {

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

			dtmsale.removeRow(i);

			for (int y = i; y < dtmsale.getRowCount(); y++) {
				System.out.println("inside loop y value " + y);

				dtmsale.setValueAt(y + 1, y, 0);
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

		showSale[0] = String.valueOf(vno.size() + 1);

		vid.addElement(book.getId());

		showSale[1] = book.getName();
		vno.addElement(showSale[1]);
		showSale[2] = txtStockAmount.getText();
		totalquantity = Integer.valueOf(showSale[2]) + (book.getStockamount());
		book.setStockamount(totalquantity);

		showSale[3] = txtPrice.getText();

		int totalamount = (Integer.valueOf(showSale[2]) * Integer.valueOf(showSale[3]));
		showSale[4] = String.valueOf(totalamount);
		vtotalPrice.addElement((showSale[4]));
		vtotalquantity.addElement(showSale[2]);

		calculateDiscountPrice();

		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));

		dtmsale.addRow(showSale);
		this.tbldisplaysaleitem.setModel(dtmsale);

	}

	private void calculateDiscountPrice() {
		String i = sumAmount(vtotalPrice, 0);
		lblbeforeprice.setText(sumAmount(vtotalPrice, 1));
		System.out.println("Sum total " + i);
		if (lbldisplaydiscount.getText().equals("5")) {
			double s = Integer.valueOf(i) * 0.05;
			System.out.println("0.05 double value " + s);
			Integer ii = (int) (Integer.valueOf(i) - (Math.round(s)));
			System.out.println("subtraction value " + ii);
			lbltotalprice.setText(ii.toString());
		} else {

			lbltotalprice.setText(sumAmount(vtotalPrice, 1));
		}
	}

	private String sumAmount(Vector<String> storeQTY2, int t) {

		long sum = 0;
		for (int i = 0; i < storeQTY2.size(); i++) {
			sum += Long.parseLong(storeQTY2.elementAt(i));
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
		setBounds(42, 11, 1051, 487);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);

		cboCustomer = new JComboBox();
		cLayout.setComboBox(cboCustomer);

		cboCustomer.addActionListener(new ActionListener() {
			private int totalamount;

			@Override
			public void actionPerformed(ActionEvent arg0) {

				totalamount = customerService.sumCustomerAmountForOneMonth(cboCustomer.getSelectedItem().toString());
				System.out.println("Total Amount " + totalamount);
				if (totalamount > 20000) {
					lbldisplaydiscount.setText("5");
				} else {
					lbldisplaydiscount.setText("0");
				}

			}
		});

		cboCategory = new JComboBox<String>();
		cLayout.setComboBox(cboCategory);

		cboCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cboCategory.getSelectedIndex() == 0) {
					loadAllBooks(Optional.empty());
				} else {
					loadAllBooksByCategory(cboCategory.getSelectedItem().toString());
				}

			}
		});

		JLabel lblcustomer = new JLabel("Customer");
		cLayout.setLabel(lblcustomer);

		JLabel lblCategory = new JLabel("Category");
		cLayout.setLabel(lblCategory);

		JLabel lblQuantity = new JLabel("Quantity");
		cLayout.setLabel(lblQuantity);

		txtStockAmount = new JTextField();
		cLayout.setTextField(txtStockAmount);

		txtStockAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addBookAction();
			}
		});

		JLabel lblPrice = new JLabel("Price");
		cLayout.setLabel(lblPrice);

		txtPrice = new JTextField();
		txtPrice.setEditable(false);
		cLayout.setTextField(txtPrice);

		lblBookID = new JLabel("");
		cLayout.setLabel(lblBookID);

		JButton btnedit = new JButton("Update");
		cLayout.setButton(btnedit);
		btnedit.setVisible(false);

		btnedit.addActionListener(new ActionListener() {
			@Override
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
					System.out.println(
							"Quantity = " + txtStockAmount.getText() + " and StockAmount =" + book.getStockamount());
					JOptionPane.showMessageDialog(null, "Quantity is over stock amount!");
					txtStockAmount.requestFocus();
				} else {

					deleteRow();
					addBookToSaleTable();
					calculateDiscountPrice();

					lbltotalquantity.setText(sumAmount(vtotalquantity, 1));
					clearform();
				}
			}
		});

		JButton btnremove = new JButton("Remove");
		cLayout.setButton(btnremove);
		btnremove.setVisible(false);
		btnremove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tbldisplaysaleitem.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(null, "Please select book to delete!");
				} else {
					deleteRow();
					clearform();
					lbltotalprice.setText(sumAmount(vtotalPrice, 1));
					calculateDiscountPrice();
				}
			}

		});

		btnaddbook = new JButton("Add Book");
		cLayout.setButton(btnaddbook);
		btnaddbook.setMnemonic('a');
		btnaddbook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addBookAction();

			}

		});

		txtsearchbook = new JTextField("");
		cLayout.setTextField(txtsearchbook);
		txtsearchbook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (txtsearchbook.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Enter Book Name!");
					txtsearchbook.requestFocus();
				} else {
					searchBook();
				}

			}
		});

		JButton btnSearch = new JButton("Search");
		cLayout.setButton(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (txtsearchbook.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Enter Book Name!");
					txtsearchbook.requestFocus();
				} else {
					searchBook();
				}

			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBackground(Color.WHITE);
		tbldisplaysaleitem = new JTable();
		tbldisplaysaleitem.setBackground(Color.WHITE);
		cLayout.setTable(tbldisplaysaleitem);

		tbldisplaysaleitem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbldisplaysaleitem.setDefaultEditor(Object.class, null);
		tbldisplaysaleitem.setAutoCreateRowSorter(true);

		this.tbldisplaysaleitem.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tbldisplaysaleitem.getSelectionModel().isSelectionEmpty()) {

				tblshowbooklist.clearSelection();
				btnedit.setVisible(true);
				btnremove.setVisible(true);
				btnaddbook.setVisible(false);
				btnshowAll.setVisible(false);

				int i = tbldisplaysaleitem.getSelectedRow();
				System.out.println("Show selected data " + (String) tbldisplaysaleitem.getValueAt(i, 4));

				txtStockAmount.setText((String) tbldisplaysaleitem.getValueAt(i, 2));
				txtStockAmount.requestFocus();
				txtStockAmount.selectAll();
				txtPrice.setText((String) tbldisplaysaleitem.getValueAt(i, 3));
				lblBookID.setText(vid.elementAt(i));

				book = bookService.findById(vid.elementAt(i));
				System.out.println(
						"Sale Item Table Book ID " + lblBookID.getText() + " Quantity = " + book.getStockamount());

				book.setId(vid.elementAt(i));
				book.setName(vno.elementAt(i));

			}
		});

		scrollPane_1.setViewportView(tbldisplaysaleitem);

		lblemployee = new JLabel("Employee Name");
		cLayout.setLabel(lblemployee);

		tblshowbooklist = new JTable();
		tblshowbooklist.setBackground(Color.WHITE);

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
				lblshowBookCover.setText("");
				ImageIcon imageIcon = new ImageIcon(
						new ImageIcon(book.getPhoto()).getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH));
				lblshowBookCover.setIcon(imageIcon);
				lblshowBookCover.setHorizontalAlignment(SwingConstants.CENTER);

				lblBookID.setText(book.getId());

				txtStockAmount.requestFocus();

				txtPrice.setText(String.valueOf(book.getSale_price()));

				System.out.println("Customer CBO Selected Return value : " + book.getCategory().getName());

			}
		});

		if (tblshowbooklist.getSelectedColumnCount() < 0) {
			cboCategory.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					if (cboCategory.getSelectedIndex() == 0) {
						loadAllBooks(Optional.empty());
					} else {
						loadAllBooksByCategory(cboCategory.getSelectedItem().toString());
					}

				}
			});

		}

		scrollPane.setViewportView(tblshowbooklist);

		tableselection();

		btnshowAll = new JButton("Show All");
		cLayout.setButton(btnshowAll);
		btnshowAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadAllBooks(Optional.empty());
				clearform();
			}
		});

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

		lblshowBookCover = new JLabel("Show Selected Book Cover");
		cLayout.setLabel(lblshowBookCover);

		lblshowBookCover.setHorizontalAlignment(SwingConstants.CENTER);

		lblshowCustomer = new JLabel("Show Customer");
		cLayout.setLabel(lblshowCustomer);
		lblshowCustomer.setVisible(false);

		lblNewLabel_1 = new JLabel("Sale");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(txtsearchbook, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addGap(2).addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel.createSequentialGroup().addGap(1)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE).addGap(5))
				.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
								.createSequentialGroup().addGap(1)
								.addComponent(cboCustomer, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_panel.createSequentialGroup().addGap(38).addComponent(lblCategory,
												GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel.createSequentialGroup().addGap(10)
												.addComponent(lblshowCustomer, GroupLayout.DEFAULT_SIZE, 124,
														Short.MAX_VALUE)
												.addGap(11)
												.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 109,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED))))
								.addGroup(gl_panel.createSequentialGroup().addGap(3).addComponent(lblBookID)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblQuantity, GroupLayout.PREFERRED_SIZE, 100,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_panel.createSequentialGroup().addGap(96).addComponent(
														txtStockAmount, GroupLayout.PREFERRED_SIZE, 182,
														GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_panel.createSequentialGroup().addGap(96).addComponent(
														txtPrice, GroupLayout.PREFERRED_SIZE, 182,
														GroupLayout.PREFERRED_SIZE))
												.addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 100,
														GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel.createSequentialGroup().addGap(10)
										.addComponent(btnaddbook, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)
										.addGap(16)
										.addComponent(btnedit, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)
										.addGap(10).addComponent(btnremove, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup().addGap(1)
								.addComponent(lblcustomer, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGap(254)))
						.addGroup(
								gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup().addGap(60)
												.addComponent(lblshowBookCover, GroupLayout.DEFAULT_SIZE, 141,
														Short.MAX_VALUE)
												.addGap(74))
										.addGroup(gl_panel
												.createSequentialGroup().addGap(35).addComponent(lblNewLabel_1,
														GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
												.addContainerGap()))));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
				.createSequentialGroup().addGap(6)
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel
						.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblcustomer, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 32,
												GroupLayout.PREFERRED_SIZE)))
						.addGap(11)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblshowCustomer, GroupLayout.PREFERRED_SIZE, 32,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(cboCustomer, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(9)
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
						.addComponent(lblshowBookCover, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE))
				.addGap(9)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(2).addComponent(txtsearchbook,
								GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnshowAll, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
				.addGap(27).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE).addGap(5))
				.addGroup(gl_panel.createSequentialGroup().addGap(48)
						.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(401, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		JPanel pnshowsaleitem = new JPanel();
		pnshowsaleitem.setBackground(Color.WHITE);

		JButton btnSave = new JButton("Save");
		cLayout.setButton(btnSave);
		btnSave.setMnemonic('s');

		btnSave.addActionListener(new ActionListener() {
			@Override
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
							savedata2[0] = (String) tbldisplaysaleitem.getValueAt(i, 2);
							savedata2[1] = (String) tbldisplaysaleitem.getValueAt(i, 3);
							savedata2[2] = vid.elementAt(i);
							System.out.println("Book id " + i + " show " + (savedata2[2]));
							if (lbldisplaydiscount.getText().equals("5")) {
								saleService.createSaleDetails(savedata2, 1);
								System.out.println("Hello I am discount");

							} else {
								System.out.println("Hell I am normal");
								saleService.createSaleDetails(savedata2, 0);

							}

						}

						clearAll();

						lblshowCustomer.setVisible(false);
						cboCustomer.setVisible(true);
						cboCustomer.setEnabled(true);
						loadAllBooks(Optional.empty());

					} else {
						JOptionPane.showMessageDialog(null, "Choose Supplier");
					}
				}

			}

		});

		JLabel lblTotalAmount = new JLabel("Total Amount");
		cLayout.setLabel(lblTotalAmount);

		JLabel lblTotalQuantity = new JLabel("Total Quantity");
		cLayout.setLabel(lblTotalQuantity);

		lbltotalquantity = new JLabel("0");
		lbltotalquantity.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		lbltotalquantity.setFont(new Font("Tahoma", Font.BOLD, 18));

		lbltotalprice = new JLabel("0");
		lbltotalprice.setForeground(UIManager.getColor("ToolBar.dockingForeground"));

		lbltotalprice.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel lblTotalQuantity_1_1 = new JLabel("Kyats");
		lblTotalQuantity_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));

		lbldiscount = new JLabel("Discount");
		lbldiscount.setFont(new Font("Tahoma", Font.BOLD, 16));

		lbldisplaydiscount = new JLabel("0");
		lbldisplaydiscount.setForeground(Color.RED);
		lbldisplaydiscount.setFont(new Font("Tahoma", Font.BOLD, 18));

		JLabel lblTotalQuantity_1_1_1 = new JLabel("%");
		lblTotalQuantity_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 16));

		JLabel lblNewLabel = new JLabel("Price Before Discount");

		lblbeforeprice = new JLabel("0");

		GroupLayout gl_pnshowsaleitem = new GroupLayout(pnshowsaleitem);
		gl_pnshowsaleitem.setHorizontalGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnshowsaleitem.createSequentialGroup()
						.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING,
										gl_pnshowsaleitem.createSequentialGroup().addGap(10).addComponent(btnSave,
												GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.TRAILING,
										gl_pnshowsaleitem.createSequentialGroup().addContainerGap()
												.addComponent(lblTotalAmount, GroupLayout.PREFERRED_SIZE, 202,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(lbltotalprice, GroupLayout.PREFERRED_SIZE, 84,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
												.addComponent(lblTotalQuantity_1_1))
								.addGroup(Alignment.TRAILING, gl_pnshowsaleitem.createSequentialGroup()
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblTotalQuantity, GroupLayout.PREFERRED_SIZE, 202,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lbltotalquantity, GroupLayout.PREFERRED_SIZE, 151,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_pnshowsaleitem.createSequentialGroup().addContainerGap()
										.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
												.addComponent(lbldiscount, GroupLayout.PREFERRED_SIZE, 202,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 124,
														GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING, false)
												.addGroup(gl_pnshowsaleitem.createSequentialGroup()
														.addComponent(lbldisplaydiscount, GroupLayout.PREFERRED_SIZE,
																46, GroupLayout.PREFERRED_SIZE)
														.addGap(51).addComponent(lblTotalQuantity_1_1_1,
																GroupLayout.PREFERRED_SIZE, 45,
																GroupLayout.PREFERRED_SIZE))
												.addComponent(lblbeforeprice, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
						.addContainerGap()));
		gl_pnshowsaleitem.setVerticalGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnshowsaleitem.createSequentialGroup()
						.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTotalQuantity, GroupLayout.PREFERRED_SIZE, 30,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lbltotalquantity, GroupLayout.PREFERRED_SIZE, 30,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_pnshowsaleitem.createSequentialGroup().addGap(1).addComponent(lbldiscount,
										GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.LEADING)
										.addComponent(lblTotalQuantity_1_1_1, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lbldisplaydiscount, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel)
								.addComponent(lblbeforeprice))
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_pnshowsaleitem.createParallelGroup(Alignment.BASELINE)
								.addComponent(lbltotalprice, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTotalAmount, GroupLayout.PREFERRED_SIZE, 30,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTotalQuantity_1_1, GroupLayout.PREFERRED_SIZE, 30,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
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

	}

	protected void addBookAction() {

		if (cboCustomer.getSelectedItem().equals("- Select -")) {
			JOptionPane.showMessageDialog(btnaddbook, "Please select customer id!");
			cboCustomer.requestFocus();
		} else if (lblBookID.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please Select Book");
			txtsearchbook.requestFocus();
		} else if (!Checking.checktxtquantity(txtStockAmount.getText())) {
			txtStockAmount.requestFocus();
			txtStockAmount.selectAll();
			JOptionPane.showMessageDialog(null, "Please enter Quantity Correctly!");
		} else if (check(book.getName(), vno)) {
			JOptionPane.showMessageDialog(null, "This item is already added");
			clearform();
		} else if (Integer.valueOf(txtStockAmount.getText()) > book.getStockamount()) {
			JOptionPane.showMessageDialog(null, "Quantity is over stock amount!");
			txtStockAmount.requestFocus();
		} else if (null != book && !book.getId().isBlank()) {
			System.out.println("Book Name : " + book.getName());

			addBookToSaleTable();

			clearform();

			cboCustomer.setVisible(false);
			lblshowCustomer.setVisible(true);
			lblshowCustomer.setText(cboCustomer.getSelectedItem().toString());

			loadAllBooks(Optional.empty());
			txtsearchbook.requestFocus();

		} else {

			JOptionPane.showMessageDialog(null, "Check Required Field");
		}

	}

	private boolean check(String name, Vector<String> vno) {

		System.out.print("Vecoter Data ");
		for (int i = 0; i < vno.size(); i++) {
			System.out.print(" " + vno.elementAt(i));
			if (name.equals(vno.elementAt(i)))
				return true;
		}
		return false;
	}

	protected void searchBook() {

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
		this.categoryService = new CategoryService();
		this.bookService = new BookService();
		this.employee = new Employee();
		this.book = new Book();
	}

	private void loadCategoryForComboBox() {
		cboCategory.addItem("- Select -");

		this.categoryList = this.categoryService.findAllCategories();
		this.categoryList.forEach(c -> cboCategory.addItem(c.getName()));
	}

	private void loadCustomerForComboBox() {
		cboCustomer.addItem("- Select -");

		this.customerList = this.customerService.findbyactive();
		this.customerList.forEach(p -> cboCustomer.addItem(p.getId()));
	}

	public void comboFilter(String enteredText) {

		List<String> filterArray = new ArrayList<String>();
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getName().toLowerCase().contains(enteredText.toLowerCase())) {
				filterArray.add(customerList.get(i).getName());
			}
		}
		if (filterArray.size() > 0) {
			DefaultComboBoxModel model = (DefaultComboBoxModel) cboCategory.getModel();
			model.removeAllElements();
			for (String s : filterArray)
				model.addElement(s);

			JTextField textfield = (JTextField) cboCategory.getEditor().getEditorComponent();
			textfield.setText(enteredText);
		}
	}

	class AutoCompleteComboBox extends JComboBox {
		public int caretPos = 0;
		public JTextField tfield = null;

		public AutoCompleteComboBox(List<Customer> customerList) {
			super();
			setEditor(new BasicComboBoxEditor());
			setEditable(true);
		}

		@Override
		public void setSelectedIndex(int index) {
			super.setSelectedIndex(index);
			tfield.setText(getItemAt(index).toString());
			tfield.setSelectionEnd(caretPos + tfield.getText().length());
			tfield.moveCaretPosition(caretPos);
		}

		@Override
		public void setEditor(ComboBoxEditor editor) {
			super.setEditor(editor);
			if (editor.getEditorComponent() instanceof JTextField) {
				tfield = (JTextField) editor.getEditorComponent();
				tfield.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent ke) {
						char key = ke.getKeyChar();
						if (!(Character.isLetterOrDigit(key) || Character.isSpaceChar(key)))
							return;
						caretPos = tfield.getCaretPosition();
						String text = "";
						try {
							text = tfield.getText(0, caretPos);
						} catch (javax.swing.text.BadLocationException e) {
							e.printStackTrace();
						}
						for (int i = 0; i < getItemCount(); i++) {
							String element = (String) getItemAt(i);
							if (element.startsWith(text)) {
								setSelectedIndex(i);
								return;
							}
						}
					}
				});
			}
		}
	}
}
