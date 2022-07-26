package forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entities.Book;
import entities.Category;
import entities.Employee;
import entities.Publisher;
import entities.PurchaseDetails;
import services.BookService;
import services.CategoryService;
import services.PublisherService;
import services.PurchaseService;
import shared.checker.Checking;
import shared.utils.CurrentUserHolder;

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
	private List<Publisher> publisherList;
	private DefaultTableModel dtm = new DefaultTableModel();
	private DefaultTableModel dtmpurchase = new DefaultTableModel();
	private CategoryService categoryService;
	private JLabel lblshowTime;
	private PublisherService publisherService;
	private BookService bookService;
	private Book book;
	private JButton btnshowAll;
	private PurchaseService purchaseService = new PurchaseService();
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
	private List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
	private JLabel lblemployee;
	private JButton btnaddbook;
	private JLabel lblshowBookCover;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	/**
	 * Create the panel.
	 */
	public PurchaseForm() {
		setBackground(Color.WHITE);
		initialize();
		initializeDependency();
		this.setTableDesign();
		this.setTableDesignForPurchaseTable();

		this.showTodayDate();
		this.loadAllBooks(Optional.empty());

		this.loadCategoryForComboBox();
		this.loadPublisherForComboBox();
		this.initializeLoggedInUser();

	}

	private void showTodayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar now = Calendar.getInstance();
				lblshowTime.setText(dateFormat.format(now.getTime()));
			}
		}).start();
	}

	private void initializeLoggedInUser() {
		employee = CurrentUserHolder.getCurrentUser();
		if (employee != null)
			lblemployee.setText(employee.getName());
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

		lblBookID.setText("");
		txtStockAmount.setText("");
		txtPrice.setText("");
		txtsearchbook.setText("");
		cboCategory.setSelectedIndex(0);

	}

	private void clearAll() {

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

	}

	private void setTableDesignForPurchaseTable() {
		dtmpurchase.addColumn("No");
		dtmpurchase.addColumn("Book");

		dtmpurchase.addColumn("Quantity");
		dtmpurchase.addColumn("Price");
		dtmpurchase.addColumn("Amount");

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

			dtmpurchase.removeRow(i);

			for (int y = i; y < dtmpurchase.getRowCount(); y++) {
				System.out.println("inside loop y value " + y);

				dtmpurchase.setValueAt(y + 1, y, 0);
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

	private void addBookToPurchaseTable() {

		showPurchase[0] = String.valueOf(vno.size() + 1);

		vid.addElement(book.getId());

		showPurchase[1] = book.getName();
		vno.addElement(showPurchase[1]);

		showPurchase[2] = txtStockAmount.getText();

		totalquantity = Integer.valueOf(showPurchase[2]) + (book.getStockamount());
		book.setStockamount(totalquantity);

		showPurchase[3] = txtPrice.getText();

		int totalamount = (Integer.valueOf(showPurchase[2]) * Integer.valueOf(showPurchase[3]));
		showPurchase[4] = String.valueOf(totalamount);
		vtotalPrice.addElement((showPurchase[4]));
		vtotalquantity.addElement(showPurchase[2]);
		lbltotalquantity.setText(sumAmount(vtotalquantity, 1));

		lbltotalprice.setText(sumAmount(vtotalPrice, 1));

		dtmpurchase.addRow(showPurchase);
		this.tblshowPurchase.setModel(dtmpurchase);
	}

	private String sumAmount(Vector<String> storeQTY2, int t) {

		long sum = 0;
		for (int i = 0; i < storeQTY2.size(); i++) {
			sum += Long.parseLong(storeQTY2.elementAt(i));

		}
		if (t == 1) {
			int len = String.valueOf(sum).length(), index = 0;
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
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane = new JScrollPane();
		cboPublisher = new JComboBox<String>();

		cLayout.setComboBox(cboPublisher);
		cboPublisher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		cboCategory = new JComboBox<String>();
		cLayout.setComboBox(cboCategory);

		JLabel lblPublisher = new JLabel("Publisher");
		cLayout.setLabel(lblPublisher);
		JLabel lblCategory = new JLabel("Category");
		cLayout.setLabel(lblCategory);
		JLabel lblQuantity = new JLabel("Quantity");
		cLayout.setLabel(lblQuantity);
		txtStockAmount = new JTextField();
		txtStockAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtPrice.requestFocus();
				txtPrice.selectAll();

			}
		});
		txtStockAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtStockAmount.setColumns(10);

		JLabel lblPrice = new JLabel("Price");
		cLayout.setLabel(lblPrice);
		txtPrice = new JTextField();
		txtPrice.addActionListener(new ActionListener() {
			@Override
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
		cLayout.setLabel(lblBookID);
		btnedit = new JButton("Edit");
		btnedit.setVisible(false);
		btnedit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				editPurchase();

			}
		});
		cLayout.setButton(btnedit);
		JButton btnremove = new JButton("Remove");
		btnremove.setVisible(false);
		btnremove.addActionListener(new ActionListener() {
			@Override
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
		cLayout.setButton(btnremove);
		btnaddbook = new JButton("Add Book");
		btnaddbook.setMnemonic('a');
		btnaddbook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addBookAction();

			}

		});

		btnaddbook.setMargin(new Insets(2, 2, 2, 2));
		cLayout.setButton(btnaddbook);
		txtsearchbook = new JTextField();
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
		txtsearchbook.setName("");
		txtsearchbook.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtsearchbook.setColumns(10);

		JButton btnSearch = new JButton("Search");

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
		cLayout.setButton(btnSearch);
		btnSearch.setMargin(new Insets(2, 2, 2, 2));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JScrollPane scrollPane_1 = new JScrollPane();
		tblshowPurchase = new JTable();
		tblshowPurchase.setBackground(Color.WHITE);
		cLayout.setTable(tblshowPurchase);

		this.tblshowPurchase.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowPurchase.getSelectionModel().isSelectionEmpty()) {

				tblshowbooklist.clearSelection();
				btnedit.setVisible(true);
				btnremove.setVisible(true);
				btnaddbook.setVisible(false);
				btnshowAll.setVisible(false);

				int i = tblshowPurchase.getSelectedRow();
				System.out.println("Show selected data " + (String) tblshowPurchase.getValueAt(i, 4));

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

		tblshowbooklist = new JTable();
		tblshowbooklist.setBackground(Color.WHITE);

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

		tableselection();

		btnshowAll = new JButton("Show All");
		btnshowAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadAllBooks(Optional.empty());
				clearform();
			}
		});
		cLayout.setButton(btnshowAll);

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

		JPanel pnshowpurchaseitem = new JPanel();
		pnshowpurchaseitem.setBackground(Color.WHITE);

		JButton btnSave = new JButton("Save");
		cLayout.setButton(btnSave);
		btnSave.setMnemonic('s');

		btnSave.addActionListener(new ActionListener() {
			@Override
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
							savedata2[0] = (String) tblshowPurchase.getValueAt(i, 2);
							savedata2[1] = (String) tblshowPurchase.getValueAt(i, 3);
							savedata2[2] = vid.elementAt(i);
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

		lblshowTime = new JLabel("Show Time");
		lblshowTime.setFont(new Font("Tahoma", Font.BOLD, 12));

		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(pnshowpurchaseitem, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 379,
								Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(lblshowTime, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 83, Short.MAX_VALUE).addComponent(
										lblemployee, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)))
						.addGap(51)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblemployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblshowTime, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGap(7).addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(pnshowpurchaseitem, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		panel_1.setLayout(gl_panel_1);
		setLayout(groupLayout);

	}

	private void editPurchase() {

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

		} else if (check(book.getName(), vno)) {
			System.out.println(book.getName() + "inside the check if " + vno);
			JOptionPane.showMessageDialog(null, "This item is already added");
			clearform();
		} else if (null != book && !book.getId().isBlank()) {

			addBookToPurchaseTable();

			clearform();
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
		this.publisherService = new PublisherService();
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

	private void loadPublisherForComboBox() {
		cboPublisher.addItem("- Select -");

		this.publisherList = this.publisherService.findAllPublishers();
		this.publisherList.forEach(p -> cboPublisher.addItem(p.getName()));
	}
}
