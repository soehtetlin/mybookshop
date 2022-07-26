package forms;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entities.Book;
import entities.Category;
import entities.Employee;
import entities.Publisher;
import entities.Purchase;
import entities.PurchaseDetails;
import services.EmployeeService;
import services.PublisherService;
import services.PurchaseService;

public class PurchaseDetailForm extends JPanel {
	private JTable table;
	private JTextField txtSearchPurchase;
	private PublisherService publisherService;
	private EmployeeService employeeService;
	private DefaultTableModel dtm = new DefaultTableModel();
	private List<PurchaseDetails> purchaseList = new ArrayList<>();
	private PurchaseService purchaseServices;
	private JComboBox<String> cboPublisher, cboEmployee;
	private List<Category> categoryList;
	private List<Employee> employeelist;
	private List<Publisher> publisherList;
	private List<Purchase> purchaseListName;
	private List<Book> originalBookList = new ArrayList<>();

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
		this.loadPublisherForComboBox();

	}

	private void initializeDependency() {
		this.publisherService = new PublisherService();
		this.employeeService = new EmployeeService();
		this.purchaseServices = new PurchaseService();

	}

	private void setTableDesign() {

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
		Vector<String> vnoo = new Vector<String>();

		purcahaseListShow.forEach(e -> {
			Object[] row = new Object[11];
			row[0] = (vnoo.size() + 1);
			row[1] = e.getPurchase().getId();
			vnoo.addElement(e.getPurchase().getId());
			System.out.println("Indside purchse detail form purchase id : " + e.getPurchase().getId());
			row[2] = e.getPurchase().getPurchaseDate();
			System.out.println("Indside purchse detail form purchase id : " + e.getPurchase().getPurchaseDate());
			row[3] = e.getBook().getName();
			System.out.println("Indside purchse detail form purchase id : " + e.getBook().getName());
			row[4] = e.getBook().getPublisher().getName();
			row[5] = e.getPurchase().getEmployee().getName();
			row[6] = e.getQuantity();
			row[7] = e.getPrice();
			row[8] = e.getBook().getAuthor().getName();
			row[9] = e.getBook().getCategory().getName();
			row[10] = e.getPurchase().getDescription();
			dtm.addRow(row);
		});
		this.table.setModel(dtm);

	}

	private void loadPurchaseDetailByPublisher(String s) {

		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();
		List<PurchaseDetails> purchasedetail = new ArrayList<>();
		purchasedetail = purchaseServices.findPurchaseDetailByPublisher(s);
		Vector<String> vnono = new Vector<String>();

		purchasedetail.forEach(e -> {
			Object[] row = new Object[11];
			row[0] = (vnono.size() + 1);
			row[1] = e.getPurchase().getId();
			vnono.addElement(e.getPurchase().getId());
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

	private void loadPurchaseDetailByEmployee(String s) {

		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();
		List<PurchaseDetails> purchasedetail = new ArrayList<>();
		purchasedetail = purchaseServices.findPurchaseDetailByEmployee(s);
		Vector<String> vnono = new Vector<String>();

		purchasedetail.forEach(e -> {
			Object[] row = new Object[11];
			row[0] = (vnono.size() + 1);
			row[1] = e.getPurchase().getId();
			vnono.addElement(e.getPurchase().getId());
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

		JButton btnSearchBook = new JButton("Search");
		btnSearchBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchBook();
			}
		});
		btnSearchBook.setMnemonic('s');
		btnSearchBook.setMargin(new Insets(2, 2, 2, 2));
		cLayout.setButton(btnSearchBook);
		txtSearchPurchase = new JTextField();
		cLayout.setTextField(txtSearchPurchase);
		txtSearchPurchase.setColumns(10);

		JLabel lblPublisher = new JLabel("Publisher");
		cLayout.setLabel(lblPublisher);
		cboPublisher = new JComboBox<String>();
		cLayout.setComboBox(cboPublisher);
		cboPublisher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cboPublisher.getSelectedIndex() == 0) {
					loadAllPurchase(Optional.empty());
				} else {
					cboEmployee.setSelectedIndex(0);
					loadPurchaseDetailByPublisher(cboPublisher.getSelectedItem().toString());
				}

			}
		});

		JLabel lblEmployee = new JLabel("Employee");
		cLayout.setLabel(lblEmployee);
		cboEmployee = new JComboBox<String>();
		cLayout.setComboBox(cboEmployee);
		cboEmployee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cboEmployee.getSelectedIndex() == 0) {
					loadAllPurchase(Optional.empty());
				} else {
					cboPublisher.setSelectedIndex(0);
					loadPurchaseDetailByEmployee(cboEmployee.getSelectedItem().toString());
				}
			}
		});

		JButton btnShowAll = new JButton("Show All");
		cLayout.setButton(btnShowAll);
		btnShowAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtSearchPurchase.setText("");
				cboPublisher.setSelectedIndex(0);
				cboEmployee.setSelectedIndex(0);
				loadAllPurchase(Optional.empty());
			}
		});
		btnShowAll.setMnemonic('a');
		btnShowAll.setMargin(new Insets(2, 2, 2, 2));
		btnShowAll.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				groupLayout.createSequentialGroup().addGap(10)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 745,
										Short.MAX_VALUE)
								.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 745,
										Short.MAX_VALUE))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(8)
						.addGroup(gl_panel
								.createParallelGroup(
										Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(txtSearchPurchase, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(25)
										.addComponent(btnSearchBook, GroupLayout.PREFERRED_SIZE, 118,
												GroupLayout.PREFERRED_SIZE)
										.addGap(25)
										.addComponent(btnShowAll, GroupLayout.PREFERRED_SIZE, 118,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 100,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 100,
														GroupLayout.PREFERRED_SIZE))
										.addGap(35)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 100,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_panel.createSequentialGroup()
														.addComponent(cboEmployee, GroupLayout.PREFERRED_SIZE, 100,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED, 468,
																Short.MAX_VALUE)))
										.addGap(50)))
						.addGap(18)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(lblPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addGap(11)
								.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addGap(20))
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addGap(11)
								.addComponent(cboEmployee, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addGap(18)))
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(1).addComponent(txtSearchPurchase,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSearchBook).addComponent(btnShowAll))));

		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}

	protected void searchBook() {

		String keyword = txtSearchPurchase.getText();

		loadAllPurchase(Optional.of(purchaseList.stream()
				.filter(e -> e.getPurchase().getId().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getBook().getName().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getBook().getPublisher().getName().toLowerCase(Locale.ROOT)
								.contains(keyword.toLowerCase(Locale.ROOT))
						|| e.getBook().getAuthor().getName().toLowerCase(Locale.ROOT)
								.contains(keyword.toLowerCase(Locale.ROOT)))
				.collect(Collectors.toList())));

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
