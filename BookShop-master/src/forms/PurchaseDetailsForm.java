//package forms;
//
//import entities.Purchase;
//import entities.PurchaseDetails;
//import services.PurchaseService;
//
//import java.awt.EventQueue;
//
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import java.awt.Font;
//import java.time.format.DateTimeFormatter;
//import javax.swing.SwingConstants;
//import javax.swing.JTable;
//import javax.swing.JScrollPane;
//import javax.swing.table.DefaultTableModel;
//
//public class PurchaseDetailsForm {
//
//	JFrame frmPurchasedetails;
//	private JTable tblDetails;
//	private JLabel lblDate = new JLabel("Product Name");
//	private JLabel lblEmployee = new JLabel("Product Name");
//	private JLabel lblSupplier = new JLabel("Product Name");
//	private Purchase purchase;
//	private PurchaseService purchaseService;
//	private DefaultTableModel dtm = new DefaultTableModel();
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					PurchaseDetailsForm window = new PurchaseDetailsForm();
//					window.frmPurchasedetails.setVisible(true);
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
//	public PurchaseDetailsForm() {
//		initialize();
//		initializeDependency();
//		setTableDesign();
//	}
//
//	public PurchaseDetailsForm(Purchase purchase) {
//		this.purchase = purchase;
//		initializeDependency();
//		initialize();
//		setTableDesign();
//		loadAllPurchaseDetails();
//		lblDate.setText(purchase.getPurchaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//		lblEmployee.setText("Employee : " + purchase.getEmployee().getName());
//		lblSupplier.setText("Supplier : " + purchase.getPublisher().getName());
//
//	}
//
//	private void setTableDesign() {
//		dtm.addColumn("Product");
//		dtm.addColumn("Quantity");
//		dtm.addColumn("Price");
//		dtm.addColumn("Total");
//		this.tblDetails.setModel(dtm);
//	}
//
//	private void loadAllPurchaseDetails() {
//		this.dtm = (DefaultTableModel) this.tblDetails.getModel();
//		this.dtm.getDataVector().removeAllElements();
//		this.dtm.fireTableDataChanged();
//
//		purchaseService.findAllPurchaseDetailsByPurchaseId(String.valueOf(purchase.getId())).forEach(p -> {
//			Object[] row = new Object[4];
//			row[0] = p.getProduct().getName();
//			row[1] = p.getQuantity();
//			row[2] = p.getPrice();
//			row[3] = p.getQuantity() * p.getPrice();
//
//			dtm.addRow(row);
//		});
//
//		this.tblDetails.setModel(dtm);
//	}
//
//	private void initializeDependency() {
//		this.purchaseService = new PurchaseService();
//	}
//
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		frmPurchasedetails = new JFrame();
//		frmPurchasedetails.setTitle("PurchaseDetails");
//		frmPurchasedetails.setBounds(100, 100, 1000, 500);
//		frmPurchasedetails.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		frmPurchasedetails.getContentPane().setLayout(null);
//
//		lblDate.setHorizontalAlignment(SwingConstants.LEFT);
//		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblDate.setBounds(45, 35, 129, 29);
//		frmPurchasedetails.getContentPane().add(lblDate);
//
//		lblSupplier.setHorizontalAlignment(SwingConstants.LEFT);
//		lblSupplier.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblSupplier.setBounds(45, 72, 129, 29);
//		frmPurchasedetails.getContentPane().add(lblSupplier);
//
//		lblEmployee.setHorizontalAlignment(SwingConstants.LEFT);
//		lblEmployee.setFont(new Font("Tahoma", Font.PLAIN, 15));
//		lblEmployee.setBounds(45, 111, 129, 29);
//		frmPurchasedetails.getContentPane().add(lblEmployee);
//
//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setBounds(32, 183, 926, 233);
//		frmPurchasedetails.getContentPane().add(scrollPane);
//
//		tblDetails = new JTable();
//		scrollPane.setViewportView(tblDetails);
//		tblDetails.setFont(new Font("Tahoma", Font.PLAIN, 15));
//	}
//}
