//package forms;
//
//import entities.*;
//import services.BookService;
//import services.PurchaseService;
//import services.PublisherService;
//import shared.utils.CurrentUserHolder;
//
//import java.awt.EventQueue;
//
//import javax.swing.*;
//import java.awt.Font;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.table.DefaultTableModel;
//import java.awt.Panel;
//import java.awt.Color;
//
//public class PurchaseEditForm {
//
//	public JFrame frmPurchase;
//	private final JTable tblPurchaseDetails = new JTable();
//	JComboBox<String> cboSupplier = new JComboBox<String>();
//	JComboBox<String> cboProduct = new JComboBox<String>();
//	JLabel lblEmployee = new JLabel("Employee");
//	private JButton btnAdd;
//	private PurchaseService purchaseService;
//	private BookService bookService;
//	private PublisherService publisherService;
//	private DefaultTableModel dtm = new DefaultTableModel();
//	private List<Book> productList = new ArrayList<>();
//	private List<Publisher> supplierList = new ArrayList<>();
//	JTextArea txtADescription = new JTextArea();
//	private List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
//	JLabel lblDate = new JLabel("Date");
//	private JPanel panel;
//	private JTextField txtBrand;
//	private JTextField txtCategory;
//	private JTextField txtQuantity;
//	private JTextField txtPrice;
//	private Optional<Book> selectedProduct;
//	private PurchaseDetails selectedPurchaseDetails;
//	private boolean editPurchaseDetails = false;
//	private long total;
//	private Employee employee;
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					PurchaseEditForm window = new PurchaseEditForm();
//					window.frmPurchase.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public PurchaseEditForm() {
//		initialize();
//		initializeDependency();
//		this.loadBrandsForComboBox();
//		this.loadProductsForComboBox();
//		this.setTableDesign();
//		this.setTodayDate();
//		this.loadAllPurchaseDetails();
//		this.initializeLoggedInUser();
//	}
//
//	private void initializeLoggedInUser() {
//		employee = CurrentUserHolder.getCurrentUser();
//		if (employee != null)
//			lblEmployee.setText(employee.getName());
//	}
//
//	private void setTodayDate() {
//		lblDate.setText(new SimpleDateFormat("MM-dd-yyyy").format(new java.util.Date()));
//	}
//
//	private void setTableDesign() {
//		dtm.addColumn("Book");
//		dtm.addColumn("Category");
//		dtm.addColumn("Quantity");
//		dtm.addColumn("Price");
//		dtm.addColumn("Total");
//		this.tblPurchaseDetails.setModel(dtm);
//	}
//
//	private void loadAllPurchaseDetails() {
//		this.dtm = (DefaultTableModel) this.tblPurchaseDetails.getModel();
//		this.dtm.getDataVector().removeAllElements();
//		this.dtm.fireTableDataChanged();
//
//		this.purchaseDetailsList.forEach(pd -> {
//			Object[] row = new Object[6];
//			row[0] = pd.getProduct().getName();
//			row[1] = pd.getProduct().getCategory().getName();
//			row[2] = pd.getQuantity();
//			row[3] = pd.getPrice();
//			row[4] = pd.getPrice() * pd.getQuantity();
//			dtm.addRow(row);
//		});
//
//		dtm.addRow(new Object[] { "", "", "", "", "", total + " MMK" });
//
//		this.tblPurchaseDetails.setModel(dtm);
//	}
//
//	private void calculateTotal() {
//		total = 0;
//		this.purchaseDetailsList.forEach(pd -> {
//			total += (long) pd.getPrice() * pd.getQuantity();
//		});
//	}
//
//	public void input_productFromPopover(Book book) {
//		PurchaseDetails purchaseDetails = new PurchaseDetails();
//		purchaseDetails.setProduct(book);
//		purchaseDetails.setQuantity(book.getQuantity());
//		purchaseDetails.setPrice(book.getPrice());
//		this.purchaseDetailsList.add(purchaseDetails);
//		this.loadAllPurchaseDetails();
//	}
//
//	private void resetProductData() {
//		txtPrice.setText("");
//		txtQuantity.setText("");
//		txtCategory.setText("");
//		cboProduct.setSelectedIndex(0);
//	}
//
//	private void initializeDependency() {
//		this.purchaseService = new PurchaseService();
//		this.bookService = new BookService();
//		this.publisherService = new PublisherService();
//		this.bookService.setPurchaseRepo(this.purchaseService);
//		this.publisherService.setPurchaseRepo(this.purchaseService);
//	}
//
//	private void loadBrandsForComboBox() {
//		this.cboSupplier.addItem("- Select -");
//		supplierList = this.publisherService.findAllSuppliers();
//		supplierList.forEach(s -> this.cboSupplier.addItem(s.getName()));
//	}
//
//	private void loadProductsForComboBox() {
//		this.cboProduct.addItem("- Select -");
//		this.productList = this.bookService.findAllProducts();
//		this.productList.forEach(p -> this.cboProduct.addItem(p.getName()));
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		frmPurchase = new JFrame();
//		frmPurchase.setTitle("Purchase");
//		frmPurchase.setBounds(100, 100, 1000, 724);
//		frmPurchase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frmPurchase.getContentPane().setLayout(null);
//
//		JLabel lblSupplier = new JLabel("Supplier");
//		lblSupplier.setHorizontalAlignment(SwingConstants.RIGHT);
//		lblSupplier.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblSupplier.setBounds(10, 86, 85, 29);
//		frmPurchase.getContentPane().add(lblSupplier);
//
//		cboSupplier.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		cboSupplier.setBounds(105, 86, 197, 29);
//		frmPurchase.getContentPane().add(cboSupplier);
//
//		lblEmployee.setHorizontalAlignment(SwingConstants.LEFT);
//		lblEmployee.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		lblEmployee.setBounds(38, 27, 143, 29);
//		frmPurchase.getContentPane().add(lblEmployee);
//
//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setBounds(354, 154, 587, 435);
//		frmPurchase.getContentPane().add(scrollPane);
//
//		tblPurchaseDetails.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		scrollPane.setViewportView(tblPurchaseDetails);
//		this.tblPurchaseDetails.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
//			if (!tblPurchaseDetails.getSelectionModel().isSelectionEmpty()) {
//
//				String productName = tblPurchaseDetails.getValueAt(tblPurchaseDetails.getSelectedRow(), 0).toString();
//
//				selectedPurchaseDetails = purchaseDetailsList.stream()
//						.filter(pd -> pd.getProduct().getName().equals(productName)).findFirst().get();
//
//				txtQuantity.setText(String.valueOf(selectedPurchaseDetails.getQuantity()));
//				txtPrice.setText(String.valueOf(selectedPurchaseDetails.getPrice()));
//				txtCategory.setText(selectedPurchaseDetails.getProduct().getCategory().getName());
//				//txtBrand.setText(selectedPurchaseDetails.getProduct().getBrand().getName());
//
//				editPurchaseDetails = true;
//			}
//		});
//
//		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
//		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		lblDate.setBounds(741, 27, 218, 29);
//		frmPurchase.getContentPane().add(lblDate);
//
//		JButton btnSave = new JButton("Save");
//		btnSave.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Optional<Publisher> selectedSupplier = supplierList.stream()
//						.filter(s -> s.getName().equals(cboSupplier.getSelectedItem())).findFirst();
//				if (selectedSupplier.isPresent()) {
//					if (employee != null) {
//						Purchase purchase = new Purchase();
//						purchase.setEmployee(employee);
//						purchase.setPublisher(selectedSupplier.get());
//						purchase.setDescription(txtADescription.getText());
//						purchase.setPurchaseDate(LocalDateTime.now());
//						purchaseService.createPurchase(purchase);
//
//						if (purchaseDetailsList.size() != 0) {
//							purchaseService.createPurchaseDetails(purchaseDetailsList);
//							JOptionPane.showMessageDialog(null, "Success");
//						} else {
//							JOptionPane.showMessageDialog(null, "Add some product for making purchase");
//						}
//					}
//
//				} else {
//					JOptionPane.showMessageDialog(null, "Choose Supplier");
//				}
//			}
//		});
//		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		btnSave.setBounds(842, 638, 99, 29);
//		frmPurchase.getContentPane().add(btnSave);
//
//		txtADescription.setFont(new Font("Monospaced", Font.PLAIN, 15));
//		txtADescription.setBounds(35, 599, 906, 29);
//		frmPurchase.getContentPane().add(txtADescription);
//
//		JButton btnCancel = new JButton("Cancel");
//		btnCancel.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
//		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		btnCancel.setBounds(733, 638, 99, 29);
//		frmPurchase.getContentPane().add(btnCancel);
//
//		panel = new JPanel();
//		panel.setBackground(Color.LIGHT_GRAY);
//		panel.setToolTipText("");
//		panel.setBounds(35, 154, 309, 435);
//		frmPurchase.getContentPane().add(panel);
//		panel.setLayout(null);
//
//		JLabel lblProduct = new JLabel("Product");
//		lblProduct.setBounds(0, 10, 62, 29);
//		panel.add(lblProduct);
//		lblProduct.setHorizontalAlignment(SwingConstants.RIGHT);
//		lblProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
//
//		cboProduct.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		cboProduct.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				selectedProduct = productList.stream().filter(p -> p.getName().equals(cboProduct.getSelectedItem()))
//						.findFirst();
//				txtPrice.setText("0");
//				txtQuantity.setText("0");
//				txtCategory.setText(selectedProduct.map(product -> product.getCategory().getName()).orElse(""));
//				//txtBrand.setText(selectedProduct.map(product -> product.getBrand().getName()).orElse(""));
//			}
//		});
//		cboProduct.setBounds(20, 49, 248, 29);
//		panel.add(cboProduct);
//
//		JLabel lblBrand = new JLabel("Brand");
//		lblBrand.setHorizontalAlignment(SwingConstants.LEFT);
//		lblBrand.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblBrand.setBounds(10, 88, 85, 29);
//		panel.add(lblBrand);
//
//		txtBrand = new JTextField("");
//		txtBrand.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		txtBrand.setEditable(false);
//		txtBrand.setColumns(10);
//		txtBrand.setBounds(20, 121, 252, 29);
//		panel.add(txtBrand);
//
//		JLabel lblCategory = new JLabel("Category");
//		lblCategory.setHorizontalAlignment(SwingConstants.LEFT);
//		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblCategory.setBounds(10, 160, 85, 29);
//		panel.add(lblCategory);
//
//		txtCategory = new JTextField("");
//		txtCategory.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		txtCategory.setEditable(false);
//		txtCategory.setColumns(10);
//		txtCategory.setBounds(20, 199, 252, 29);
//		panel.add(txtCategory);
//
//		JLabel lblQuantity = new JLabel("Quantity");
//		lblQuantity.setHorizontalAlignment(SwingConstants.LEFT);
//		lblQuantity.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblQuantity.setBounds(10, 238, 85, 29);
//		panel.add(lblQuantity);
//
//		txtQuantity = new JTextField("0");
//		txtQuantity.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		txtQuantity.setColumns(10);
//		txtQuantity.setBounds(20, 268, 252, 29);
//		panel.add(txtQuantity);
//
//		JLabel lblPrice = new JLabel("Price");
//		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
//		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblPrice.setBounds(10, 307, 85, 29);
//		panel.add(lblPrice);
//
//		txtPrice = new JTextField("0");
//		txtPrice.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		txtPrice.setColumns(10);
//		txtPrice.setBounds(20, 341, 248, 29);
//		panel.add(txtPrice);
//
//		btnAdd = new JButton(selectedPurchaseDetails != null ? "Update" : "Add");
//		btnAdd.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//
//				PurchaseDetails purchaseDetails = new PurchaseDetails();
//				purchaseDetails.setPrice(Integer.parseInt(txtPrice.getText()));
//				purchaseDetails.setQuantity(Integer.parseInt(txtQuantity.getText()));
//				purchaseDetails.setProduct(selectedProduct.orElse(null));
//				if (editPurchaseDetails) {
//					purchaseDetails.setProduct(selectedPurchaseDetails.getProduct());
//				}
//
//				if (purchaseDetails.getPrice() > 0 && purchaseDetails.getQuantity() > 0
//						&& purchaseDetails.getProduct() != null) {
//
//					if (!editPurchaseDetails) {
//						if (!purchaseDetailsList.contains(purchaseDetails)) {
//							purchaseDetailsList.add(purchaseDetails);
//							calculateTotal();
//							loadAllPurchaseDetails();
//							resetProductData();
//						} else {
//							JOptionPane.showMessageDialog(null, "Already Exists");
//						}
//
//						if (purchaseDetailsList.size() == 0) {
//							purchaseDetailsList.add(purchaseDetails);
//							calculateTotal();
//							loadAllPurchaseDetails();
//							resetProductData();
//						}
//					} else {
//						purchaseDetailsList = purchaseDetailsList.stream().map(pd -> {
//							PurchaseDetails target = new PurchaseDetails();
//							if (pd.getProduct() == selectedPurchaseDetails.getProduct()) {
//
//								target.setQuantity(purchaseDetails.getQuantity());
//								target.setPrice(purchaseDetails.getPrice());
//								target.setProduct(purchaseDetails.getProduct());
//
//							} else
//								target = pd;
//							return target;
//						}).collect(Collectors.toList());
//						editPurchaseDetails = false;
//						selectedPurchaseDetails = null;
//						resetProductData();
//						calculateTotal();
//						loadAllPurchaseDetails();
//					}
//
//				} else {
//					JOptionPane.showMessageDialog(null, "Fill all Required fields");
//				}
//			}
//		});
//		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		btnAdd.setBounds(183, 390, 85, 29);
//		panel.add(btnAdd);
//
//		JButton btnRemove = new JButton("Remove");
//		btnRemove.setBounds(74, 390, 99, 29);
//		panel.add(btnRemove);
//		btnRemove.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				purchaseDetailsList.remove(selectedPurchaseDetails);
//				resetProductData();
//				calculateTotal();
//				loadAllPurchaseDetails();
//			}
//		});
//		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 15));
//
//	}
//}
