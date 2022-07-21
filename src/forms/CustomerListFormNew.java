package forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entities.Author;
import entities.Book;
import entities.Customer;
import entities.Purchase;

import services.BookService;
import services.CustomerService;
import shared.checker.Checking;

import java.awt.Component;
import javax.swing.SwingConstants;
//import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;

public class CustomerListFormNew extends JPanel {

	private JTable table;
	public JPanel panel;
	private JScrollPane scrollPane;
	private JComboBox<String> comboMemberActive;
	private DefaultTableModel dtm = new DefaultTableModel();
	private CustomerService customerService;
	private List<Customer> originalCustomerList = new ArrayList<>();
	private JpanelLoader jloader = new JpanelLoader();
	private Customer customer;

	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	private JTextField txtCustomerName, txtContactNo, txtEmail, txtAddress, txtSearch;

	private JButton btnSave, btnUpdate, btnDelete, btnCancel, btnSearch;

	private JLabel lblRegisterDate, lblCustomerName, lblContactNo, lblEmail, lblAddress;

	private Checking checking = new Checking();
	private JTextField txtRegisterDate;
	
	/**
	 * Create the panel.
	 */
	public CustomerListFormNew() {
		this.customerService = new CustomerService();

		initialize();
		setTableDesign();
		loadAllCustomers(Optional.empty());
		buttonOnClick();
		loadCustomersForComboBox();
	}

	private void initialize() {
		
		panel = new JPanel();

		scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		table = new JTable();
		
		JLabel lblFilter = new JLabel("Filter By : ");
		cLayout.setLabel(lblFilter);

		comboMemberActive = new JComboBox();
		cLayout.setComboBox(comboMemberActive);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE).addGap(4)));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE).addContainerGap()));

		lblRegisterDate = new JLabel("Register Date");
		cLayout.setLabel(lblRegisterDate);

		lblCustomerName = new JLabel("Customer Name");
		cLayout.setLabel(lblCustomerName);

		txtCustomerName = new JTextField();
		cLayout.setTextField(txtCustomerName);

		lblContactNo = new JLabel("Contact No");
		cLayout.setLabel(lblContactNo);

		txtContactNo = new JTextField();
		cLayout.setTextField(txtContactNo);

		lblEmail = new JLabel("Email");
		cLayout.setLabel(lblEmail);

		txtEmail = new JTextField();
		cLayout.setTextField(txtEmail);

		lblAddress = new JLabel("Address");
		cLayout.setLabel(lblAddress);

		txtAddress = new JTextField();
		cLayout.setTextField(txtAddress);

		btnSave = new JButton("Save");
		cLayout.setButton(btnSave);

		btnUpdate = new JButton("Update");
		btnUpdate.setVisible(false);
		cLayout.setButton(btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setVisible(false);
		cLayout.setButton(btnDelete);

		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);

		txtSearch = new JTextField();
		cLayout.setTextField(txtSearch);

		btnSearch = new JButton("Search");
		cLayout.setButton(btnSearch);
		
		txtRegisterDate = new JTextField();
		String registerDate = new SimpleDateFormat("MM-dd-yyyy").format(new java.util.Date());
		txtRegisterDate.setText(registerDate);
		cLayout.setTextField(txtRegisterDate);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_panel.createSequentialGroup()
								.addGap(21)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addGroup(gl_panel.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblFilter, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(comboMemberActive, 0, 90, Short.MAX_VALUE)
										.addGap(32)
										.addComponent(txtSearch, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
										.addGap(18)
										.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 96, Short.MAX_VALUE)
										.addGap(27))
									.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_panel.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
												.addGap(18)
												.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE))
											.addGroup(gl_panel.createSequentialGroup()
												.addComponent(lblCustomerName)
												.addGap(18)
												.addComponent(txtCustomerName, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)))
										.addGap(123)))
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(lblRegisterDate)
											.addGroup(gl_panel.createSequentialGroup()
												.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)))
										.addGap(18)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_panel.createSequentialGroup()
												.addComponent(btnDelete)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(btnCancel))
											.addComponent(txtRegisterDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addGroup(gl_panel.createSequentialGroup()
										.addGap(4)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addComponent(lblContactNo)
											.addComponent(lblAddress, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
										.addGap(42)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
											.addComponent(txtContactNo, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))))
								.addGap(74))
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE))
						.addGap(20))
			);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(23)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRegisterDate)
						.addComponent(txtRegisterDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCustomerName)
						.addComponent(txtContactNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblContactNo)
						.addComponent(txtCustomerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAddress)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(comboMemberActive, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(5)
							.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(btnSearch))
						.addComponent(btnSave)
						.addComponent(btnUpdate)
						.addComponent(btnDelete)
						.addComponent(btnCancel))
					.addPreferredGap(ComponentPlacement.RELATED)

					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
					.addContainerGap())
		);

		panel.setLayout(gl_panel);
		setLayout(groupLayout);

		this.table.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			
			if(!table.getSelectionModel().isSelectionEmpty()) {
				
				btnSave.setVisible(false);
				btnUpdate.setVisible(true);
				btnDelete.setVisible(true);
				
				String id = table.getValueAt(table.getSelectedRow(), 0).toString();
				System.out.println("Selsct id "+ id+", "+ table.getSelectedRow());

				customer = customerService.findCustomerById(id);
				
				txtCustomerName.setText(customer.getName());
				txtContactNo.setText(customer.getContact_no());
				txtEmail.setText(customer.getEmail());
				txtAddress.setText(customer.getAddress());
			}

		});

	}

	private void setTableDesign() {

		table.setSelectionBackground(new Color(153, 51, 255));
		table.setShowVerticalLines(false);
		table.setFocusable(false);

		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setBounds(12, 254, 404, -216);

		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(153, 51, 204));
		table.getTableHeader().setForeground(new Color(245, 245, 245));
		table.setRowHeight(40);
		scrollPane.setViewportView(table);

		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Contact No");
		dtm.addColumn("Email");
		dtm.addColumn("Address");
		dtm.addColumn("Register Date");
		dtm.addColumn("Last Used Date");
		dtm.addColumn("Expire Date");
		dtm.addColumn("Active");

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
		table.getColumnModel().getColumn(8).setCellRenderer(new IconRenderer());

	}

	private void loadAllCustomers(Optional<List<Customer>> optionalCustomer) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalCustomerList = this.customerService.findAllCustomers();
		List<Customer> customerList = optionalCustomer.orElseGet(() -> originalCustomerList);

		customerList.forEach(e -> {
			Object[] row = new Object[9];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getContact_no();
			row[3] = e.getEmail();
			row[4] = e.getAddress();
			row[5] = e.getRegister_date();
			row[6] = e.getLast_date_use();
			row[7] = e.getExpired_date();
			row[8] = e.getActive();

			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}

	private void loadCustomersByActive(int active) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		List<Customer> customerList = this.customerService.findCustomersByActive(active);

		customerList.forEach(e -> {
			Object[] row = new Object[9];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getContact_no();
			row[3] = e.getEmail();
			row[4] = e.getAddress();
			row[5] = e.getRegister_date();
			row[6] = e.getLast_date_use();
			row[7] = e.getExpired_date();
			row[8] = e.getActive();

			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}
	
	private void clearForm() {
		txtCustomerName.setText("");
		txtContactNo.setText("");
		txtAddress.setText("");
		txtEmail.setText("");
		txtSearch.setText("");
		comboMemberActive.setSelectedIndex(0);
		loadAllCustomers(Optional.empty());
	}
	
	private void buttonVisible() {
		btnSave.setVisible(true);
		btnUpdate.setVisible(false);
		btnCancel.setVisible(true);
		btnDelete.setVisible(false);
	}

	private void loadCustomersForComboBox() {
		comboMemberActive.addItem("-Select");
		comboMemberActive.addItem("All");
		comboMemberActive.addItem("Active");
		comboMemberActive.addItem("No Active");
	}

	private void filterByComboBox() {

		if (comboMemberActive.getSelectedIndex() == 1) {
			loadAllCustomers(Optional.empty());

		} else if (comboMemberActive.getSelectedIndex() == 2) {
			loadCustomersByActive(1);

		} else if (comboMemberActive.getSelectedIndex() == 3) {
			loadCustomersByActive(0);

		} else if (comboMemberActive.getSelectedIndex() == 0)
			loadAllCustomers(Optional.empty());;

	}

	private void searchCustomer() {

		// TODO Auto-generated method stub
		String keyword = txtSearch.getText();

		loadAllCustomers(Optional.of(originalCustomerList.stream()
				.filter(c -> c.getName().toLowerCase(Locale.ROOT).startsWith(keyword.toLowerCase(Locale.ROOT)))
				.collect(Collectors.toList())));
	}

	private void buttonOnClick() {

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Customer customer = new Customer();

				customer.setName(txtCustomerName.getText());
				customer.setAddress(txtAddress.getText());
				if (Checking.IsAllDigit(txtContactNo.getText()))
					customer.setContact_no(txtContactNo.getText());
				else
					JOptionPane.showMessageDialog(null, "Phone number should be only digits.");

				customer.setEmail(txtEmail.getText());
				customer.setActive(1);

//				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				LocalDateTime registerDate = LocalDateTime.now();
				LocalDateTime expireDate = registerDate.plusYears(2);

				customer.setRegister_date(registerDate);
				customer.setExpired_date(expireDate);
				
				customer.setLast_date_use(expireDate);

				if (!customer.getName().isBlank() && !customer.getContact_no().isBlank()) {
					customerService.saveCustomer(customer);
					System.out.println("To Save " + customer.getRegister_date());
					clearForm();
					loadAllCustomers(Optional.empty());

				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");
				}
			}

		});

		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				customer.setName(txtCustomerName.getText());
				customer.setAddress(txtAddress.getText());
				if (Checking.IsAllDigit(txtContactNo.getText()))
					customer.setContact_no(txtContactNo.getText());
				else
					JOptionPane.showMessageDialog(null, "Phone number should be only digits.");

				customer.setEmail(txtEmail.getText());
				customer.setActive(1);

				LocalDateTime registerDate = LocalDateTime.now();
				LocalDateTime expireDate = registerDate.plusYears(2);

				
				customer.setRegister_date(registerDate);
				customer.setExpired_date(expireDate);
				
				customer.setLast_date_use(expireDate);
				
				if (!customer.getName().isBlank() && !customer.getContact_no().isBlank()) {
					customerService.updateCustomer(customer.getId(), customer);

					clearForm();
					loadAllCustomers(Optional.empty());
					customer = null;
				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");
				}
				buttonVisible();
			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearForm();
				buttonVisible();
			}
		});

		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				searchCustomer();
			}
		});

		txtSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				searchCustomer();
			}
		});

		comboMemberActive.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
					filterByComboBox();
			}
		});
	}
}

class IconRenderer extends DefaultTableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image imgActive = new ImageIcon(this.getClass().getResource("/active-24.png")).getImage();
	Image imgNoActive = new ImageIcon(this.getClass().getResource("/inActive-24.png")).getImage();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		
		if(value.toString().equals("1")) {
			return new JLabel(new ImageIcon(imgActive));
		} else {
			return new JLabel(new ImageIcon(imgNoActive));

		}
	}
    
     
}