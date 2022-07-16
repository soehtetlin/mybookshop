package forms;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import entities.Book;
import entities.Category;
import entities.Employee;
import entities.Publisher;
import entities.Purchase;
import entities.PurchaseDetails;
import services.BookService;
import services.CategoryService;
import services.EmployeeService;
import services.PublisherService;
import services.PurchaseService;

import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PurchaseDetailForm extends JPanel {
	private JTable table;
	private JTextField txtSearchPurchase;
	private PublisherService publisherService;
	private EmployeeService employeeService;
	private CategoryService categoryService;
	private BookService bookService;
	private Employee employee;
	private Book book;
	private DefaultTableModel dtm = new DefaultTableModel();
	private List<PurchaseDetails> purchaseList = new ArrayList<>();
	private PurchaseService purchaseServices;
	private Vector<String> vno = new Vector<String>();
	private JComboBox<String> cboPublisher,cboCategory,cboEmployee,cboPurchase;
	private List<Category> categoryList;
	private List<Employee> employeelist;
	private List<Publisher> publisherList;
	private List<Purchase> purchaseListName;
	
	/**
	 * Create the panel.
	 */
	public PurchaseDetailForm() {
		initialize();
		initializeDependency();
		this.setTableDesign();
		this.loadAllPurchase(Optional.empty());
		this.loadEmployeeForComboBox();
		this.loadCategoryForComboBox();
		this.loadPublisherForComboBox();
		this.loadPurchaseForComboBox();

	}

	private void initializeDependency() {
		this.publisherService = new PublisherService();
		this.employeeService = new EmployeeService();
		this.categoryService = new CategoryService();
		this.bookService = new BookService();
		this.employee = new Employee();
		this.book = new Book();
		this.purchaseServices = new PurchaseService();
	}

	private void setTableDesign() {
		// TODO Auto-generated method stub
		dtm.addColumn("No");
		dtm.addColumn("Purchase\n ID");
		dtm.addColumn("Purchase\n Date");
		dtm.addColumn("Book Name");
		dtm.addColumn("Publisher\n Name");
		dtm.addColumn("Employee\n Name");
		dtm.addColumn("Quantity");
		dtm.addColumn("Price");
		dtm.addColumn("Author Name");
		dtm.addColumn("Category Name");
		dtm.addColumn("Description");
		table.setRowHeight(40);
		table.setModel(dtm);
		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(2).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(3).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(4).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(5).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(6).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(7).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(8).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(9).setCellRenderer(dfcr);
		table.getColumnModel().getColumn(10).setCellRenderer(dfcr);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setPreferredWidth(130);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(150);
		table.getColumnModel().getColumn(9).setPreferredWidth(130);
		table.getColumnModel().getColumn(10).setPreferredWidth(150);

//		table.setModel(new DefaultTableModel(new Object[][] {
//
//		}, new String[] { "No", "Purchase ID", "Purchase Date", "Book Name", "Publisher Name", "Employee Name",
//				"Quantity", "Price", "Author Name", "Category Name", "Description" }));

	}

	private void loadAllPurchase(Optional<List<PurchaseDetails>> optionalBook) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.purchaseList = this.purchaseServices.loadAllPurchaseDetails();
		List<PurchaseDetails> purcahaseListShow = optionalBook.orElseGet(() -> purchaseList);
		purcahaseListShow.forEach(e -> {
			Object[] row = new Object[11];
			row[0] = (vno.size() + 1);
			row[1] = e.getPurchase().getId();
			vno.addElement(e.getPurchase().getId());
			System.out.println("Indside purchse detail form purchase id : " + e.getPurchase().getId());
			row[2] = e.getPurchase().getPurchaseDate();
			System.out.println("Indside purchse detail form purchase id : " + e.getPurchase().getPurchaseDate());
			row[3] = e.getBook().getName();
			System.out.println("Indside purchse detail form purchase id : " + e.getBook().getName());
			row[4] = e.getBook().getPublisher().getName();
			row[5] = e.getPurchase().getEmployee().getName();
			row[6] = e.getQuantity();
			row[7] = e.getBook().getPrice();
			row[8] = e.getBook().getAuthor().getName();
			row[9] = e.getBook().getCategory().getName();
			row[10] = e.getPurchase().getDescription();
			dtm.addRow(row);
		});
		this.table.setModel(dtm);

	}

	private void initialize() {
		// TODO Auto-generated method stub
		setVisible(true);
		revalidate();
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setBounds(42, 11, 809, 458);
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 149, 789, 309);
		add(scrollPane);

		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setDefaultEditor(Object.class, null);
		table.setAutoCreateRowSorter(true);
		JTableHeader jtableheader = table.getTableHeader();
		jtableheader.setBackground(SystemColor.textHighlight);
		jtableheader.setForeground(Color.white);
		jtableheader.setFont(new Font("Tahoma", Font.BOLD, 14));
		jtableheader.setPreferredSize(new Dimension(100,40));
		scrollPane.setViewportView(table);

//		VBox vbox = new VBox(20);
//        vbox.setStyle("-fx-padding: 10;");
//        Scene scene = new Scene(vbox, 400, 400);
//        stage.setScene(scene);
//
//        checkInDatePicker = new DatePicker();
//
//        GridPane gridPane = new GridPane();
//        gridPane.setHgap(10);
//        gridPane.setVgap(10);
//
//        Label checkInlabel = new Label("Check-In Date:");
//        gridPane.add(checkInlabel, 0, 0);
//
//        GridPane.setHalignment(checkInlabel, HPos.LEFT);
//        gridPane.add(checkInDatePicker, 0, 1);
//        vbox.getChildren().add(gridPane);
//	purchaseList.forEach(a ->{
//			Object[] row = new Object[10];
//			row[0] = a.getPurchase().getId();
//			System.out.println("Indside purchse detail new purchsselistform purchase id : " + a.getPurchase().getId());
//			row[1] = a.getPurchase().getPurchaseDate();
//			System.out.println("Indside purchse detail new purchsselist form purchase date : " + a.getPurchase().getPurchaseDate());
//			row[2] = a.getBook().getName();
//			System.out.println("Indside purchse detailnew purchsselist form purchase id : " + a.getBook().getName());
//			row[3] = a.getBook().getPublisher().getName();
//			row[4] = a.getPurchase().getEmployee().getName();
//			row[5] = a.getQuantity();
//			row[6] = a.getBook().getPrice();
//			row[7] = a.getBook().getAuthor().getName();
//			row[8] = a.getBook().getCategory().getName();
//			row[9] = a.getPurchase().getDescription();
//			
//		});

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 0, 789, 138);
		add(panel);
		panel.setLayout(null);

		JButton btnSearchBook = new JButton("Search Book");
		btnSearchBook.setBounds(171, 102, 118, 25);
		btnSearchBook.setMnemonic('s');
		btnSearchBook.setMargin(new Insets(2, 2, 2, 2));
		btnSearchBook.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(btnSearchBook);

		txtSearchPurchase = new JTextField();
		txtSearchPurchase.setBounds(10, 103, 136, 23);
		txtSearchPurchase.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtSearchPurchase.setColumns(10);
		panel.add(txtSearchPurchase);

		JLabel lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPublisher.setBounds(10, 11, 100, 30);
		panel.add(lblPublisher);

		cboPublisher = new JComboBox<String>();
		cboPublisher.setBounds(10, 52, 100, 30);
		panel.add(cboPublisher);

		cboCategory = new JComboBox<String>();
		cboCategory.setBounds(120, 52, 100, 30);
		panel.add(cboCategory);

		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(120, 11, 100, 30);
		panel.add(lblCategory);

		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmployee.setBounds(230, 11, 100, 30);
		panel.add(lblEmployee);

		cboEmployee = new JComboBox<String>();
		cboEmployee.setBounds(230, 52, 100, 30);
		panel.add(cboEmployee);

		JButton btnShowAll = new JButton("Show All");
		btnShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vno.removeAllElements();
				loadAllPurchase(Optional.empty());
			}
		});
		btnShowAll.setMnemonic('a');
		btnShowAll.setMargin(new Insets(2, 2, 2, 2));
		btnShowAll.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnShowAll.setBounds(314, 102, 118, 25);
		panel.add(btnShowAll);

		cboPurchase = new JComboBox<String>();
		cboPurchase.setBounds(340, 52, 100, 30);
		panel.add(cboPurchase);

		JLabel lblPurchaseId = new JLabel("Purchase ID");
		lblPurchaseId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPurchaseId.setBounds(340, 11, 100, 30);
		panel.add(lblPurchaseId);

		JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
		timeSpinner.setBounds(488, 76, 150, 23);

//		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner,
//		 "HH:mm:ss");
JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner,"yy:mm:dd");
		timeSpinner.setEditor(timeEditor);
		timeSpinner.setValue(new Date()); // will only show the current time
		panel.add(timeSpinner);
		
		JDateChooser dcto = new JDateChooser();
		Date date = new Date();
		dcto.setBounds(671, 18, 118, 23);
		dcto.setDate(date);
		panel.add(dcto);

		JDateChooser dcfrom = new JDateChooser();
		dcfrom.setBounds(509, 18, 118, 23);
		dcfrom.setDate(date);
		panel.add(dcfrom);

		JLabel lbldatefrom = new JLabel("From");
		lbldatefrom.setBounds(468, 18, 46, 20);
		panel.add(lbldatefrom);

		JLabel lbldateto = new JLabel("to");
		lbldateto.setBounds(637, 21, 28, 20);
		panel.add(lbldateto);

		JButton btnPrint = new JButton("Print");
		btnPrint.setBounds(661, 102, 118, 25);
		panel.add(btnPrint);
		btnPrint.setMnemonic('p');
		btnPrint.setMargin(new Insets(2, 2, 2, 2));
		btnPrint.setFont(new Font("Tahoma", Font.BOLD, 14));

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
	
	private void loadPurchaseForComboBox() {
		cboPurchase.addItem("- Select -");
		// System.out.println("pub count " +
		// publisherService.findAllPublishers().size());

		this.purchaseListName = this.purchaseServices.loadAllPurchaseID();
		this.purchaseListName.forEach(p -> cboPurchase.addItem(p.getId()));
	}
}
