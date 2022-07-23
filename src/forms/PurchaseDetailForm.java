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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

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
	private JComboBox<String> cboPublisher, cboCategory, cboEmployee, cboPurchase;
	private List<Category> categoryList;
	private List<Employee> employeelist;
	private List<Publisher> publisherList;
	private List<Purchase> purchaseListName;
	private JTextField txttime;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

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

	
//	private void loadAllPurchasebyPublisher(Optional<List<PurchaseDetails>> optionalBook) {
//		this.dtm = (DefaultTableModel) this.table.getModel();
//		this.dtm.getDataVector().removeAllElements();
//		this.dtm.fireTableDataChanged();
//		
//		//this.purchaseList = this.purchaseServices.loadAllPurchaseDetailsbyPublisherID((String)cboPublisher.getSelectedItem());
//
//		//this.purchaseServices.loadAllPurchaseDetails();
//		List<PurchaseDetails> purcahaseListShow = optionalBook.orElseGet(() -> purchaseList);
//		purcahaseListShow.forEach(e -> {
//			Object[] row = new Object[11];
//			row[0] = (vno.size() + 1);
//			row[1] = e.getPurchase().getId();
//			vno.addElement(e.getPurchase().getId());
//			System.out.println("Indside purchse detail form purchase id : " + e.getPurchase().getId());
//			row[2] = e.getPurchase().getPurchaseDate();
//			System.out.println("Indside purchse detail form purchase id : " + e.getPurchase().getPurchaseDate());
//			row[3] = e.getBook().getName();
//			System.out.println("Indside purchse detail form purchase id : " + e.getBook().getName());
//			row[4] = e.getBook().getPublisher().getName();
//			row[5] = e.getPurchase().getEmployee().getName();
//			row[6] = e.getQuantity();
//			row[7] = e.getBook().getPrice();
//			row[8] = e.getBook().getAuthor().getName();
//			row[9] = e.getBook().getCategory().getName();
//			row[10] = e.getPurchase().getDescription();
//			dtm.addRow(row);
//		});
//		this.table.setModel(dtm);
//
//	}
	private void initialize() {
		// TODO Auto-generated method stub
		setVisible(true);
		revalidate();
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setBounds(42, 11, 769, 433);

		JScrollPane scrollPane = new JScrollPane();

		table = new JTable();
		cLayout.setTable(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);



		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JButton btnSearchBook = new JButton("Search Book");
		btnSearchBook.setMnemonic('s');
		btnSearchBook.setMargin(new Insets(2, 2, 2, 2));
		btnSearchBook.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtSearchPurchase = new JTextField();
		txtSearchPurchase.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtSearchPurchase.setColumns(10);

		JLabel lblPublisher = new JLabel("Publisher");
		lblPublisher.setFont(new Font("Tahoma", Font.BOLD, 14));

		cboPublisher = new JComboBox<String>();
		cboPublisher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			//	loadAllPurchasebyPublisher(Optional.empty());
				
			}
		});

		cboCategory = new JComboBox<String>();

		JLabel lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setFont(new Font("Tahoma", Font.BOLD, 14));

		cboEmployee = new JComboBox<String>();

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

		cboPurchase = new JComboBox<String>();

		JLabel lblPurchaseId = new JLabel("Purchase ID");
		lblPurchaseId.setFont(new Font("Tahoma", Font.BOLD, 14));

		JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());

//		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner,
//		 "HH:mm:ss");
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "yy:mm:dd");
		timeSpinner.setEditor(timeEditor);
		timeSpinner.setValue(new Date());

		JDateChooser dcto = new JDateChooser();
		Date date = new Date();
		dcto.setDate(date);

		JDateChooser dcfrom = new JDateChooser();
		dcfrom.setDate(date);

		JLabel lbldatefrom = new JLabel("From");

		JLabel lbldateto = new JLabel("to");

		JButton btnPrint = new JButton("Print");
		btnPrint.setMnemonic('p');
		btnPrint.setMargin(new Insets(2, 2, 2, 2));
		btnPrint.setFont(new Font("Tahoma", Font.BOLD, 14));

		txttime = new JTextField();
		txttime.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 745, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
		);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(8)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(lblPurchaseId, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lbldatefrom, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_panel.createSequentialGroup()
											.addGap(41)
											.addComponent(dcfrom, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)))
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lbldateto, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcto, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(txtSearchPurchase, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(25)
									.addComponent(btnSearchBook, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
									.addGap(25)
									.addComponent(btnShowAll, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
									.addGap(46)
									.addComponent(txttime, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
									.addGap(65)
									.addComponent(btnPrint, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(cboEmployee, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(cboPurchase, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addGap(38)
							.addComponent(timeSpinner, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
					.addGap(18))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(9)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblPurchaseId, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(lbldatefrom, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addComponent(dcfrom, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lbldateto, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
								.addComponent(dcto, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))))
					.addGap(11)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboEmployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboPurchase, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(4)
							.addComponent(timeSpinner, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
					.addGap(20)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(1)
							.addComponent(txtSearchPurchase, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSearchBook)
						.addComponent(btnShowAll)
						.addComponent(txttime, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPrint)))
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

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
