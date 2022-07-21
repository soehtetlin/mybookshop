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
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import entities.Author;
import entities.Book;
import entities.Customer;
import services.BookService;
import services.CustomerService;
import shared.checker.Checking;

import java.awt.Component;
import javax.swing.SwingConstants;
import com.toedter.calendar.JDateChooser;
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
	private JDateChooser dateChooser;

	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	private JTextField txtCustomerName, txtContactNo, txtEmail, txtAddress, txtSearch;

	private JButton btnSave, btnUpdate, btnDelete, btnCancel, btnSearch;

	private JLabel lblRegisterDate, lblCustomerName, lblContactNo, lblEmail, lblAddress;

	private Checking checking = new Checking();

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

		JLabel lblFilter = new JLabel("Filter By : ");
		cLayout.setLabel(lblFilter);

		comboMemberActive = new JComboBox();
//		comboMemberActive.setModel(new DefaultComboBoxModel(new String[] {"-Select-", "All", "Active", "No Active"}));
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

		dateChooser = new JDateChooser();

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
		cLayout.setButton(btnUpdate);

		btnDelete = new JButton("Delete");
		cLayout.setButton(btnDelete);

		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);

		txtSearch = new JTextField();
		cLayout.setTextField(txtSearch);

		btnSearch = new JButton("Search");
		cLayout.setButton(btnSearch);

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
									.addComponent(lblFilter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboMemberActive, 0, 0, Short.MAX_VALUE)
									.addGap(32)
									.addComponent(txtSearch, GroupLayout.DEFAULT_SIZE, 8, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 92, Short.MAX_VALUE)
									.addGap(27))
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
											.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
											.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
											.addGap(0, 0, Short.MAX_VALUE))
										.addGroup(gl_panel.createSequentialGroup()
											.addComponent(lblCustomerName)
											.addComponent(txtCustomerName, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
											.addGap(0, 0, Short.MAX_VALUE)))
									.addGap(123)))
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblRegisterDate)
										.addGroup(gl_panel.createSequentialGroup()
											.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)))
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
											.addGap(22)
											.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnDelete)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(btnCancel))))
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
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE))
					.addGap(20))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(23)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblRegisterDate)
							.addGap(29)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCustomerName)
								.addComponent(txtContactNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblContactNo)
								.addComponent(txtCustomerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblEmail)
								.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAddress)
								.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(dateChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
					.addContainerGap())
		);
//		gl_panel.setAutoCreateContainerGaps(true);
//		gl_panel.setAutoCreateGaps(true);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}

	private void setTableDesign() {

		table = new JTable();
		table.setSelectionBackground(new Color(153, 51, 255));
		table.setShowVerticalLines(false);
		table.setFocusable(false);

		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setBounds(12, 254, 404, -216);
//		table.setBackground(new Color(0,0,0));

		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(153, 51, 204));
		table.getTableHeader().setForeground(new Color(245, 245, 245));
		table.setRowHeight(25);
		scrollPane.setViewportView(table);

		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Contact No");
		dtm.addColumn("Email");
		dtm.addColumn("Address");
		dtm.addColumn("Register Date");
		dtm.addColumn("Expire Date");
		dtm.addColumn("Active");

		table.setModel(dtm);
	}

	private void loadAllCustomers(Optional<List<Customer>> optionalCustomer) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalCustomerList = this.customerService.findAllCustomers();
		List<Customer> customerList = optionalCustomer.orElseGet(() -> originalCustomerList);

		customerList.forEach(e -> {
			Object[] row = new Object[8];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getContact_no();
			row[3] = e.getEmail();
			row[4] = e.getAddress();
			row[5] = e.getRegister_date();
			row[6] = e.getExpired_date();
//			row[7] = e.getLast_date_use();
			row[7] = e.getActive();
			System.out.println("e" + e.getId() + " " + e.getRegister_date());

			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}

	private void loadCustomersByActive(Boolean active) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		List<Customer> customerList = this.customerService.findCustomersByActive(active);

		customerList.forEach(e -> {
			Object[] row = new Object[8];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getContact_no();
			row[3] = e.getEmail();
			row[4] = e.getAddress();
			row[5] = e.getRegister_date();
			row[6] = e.getExpired_date();
			row[7] = e.getActive();

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
			loadCustomersByActive(true);

		} else if (comboMemberActive.getSelectedIndex() == 3) {
			loadCustomersByActive(false);

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

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

				String registerDate = simpleDateFormat.format(dateChooser.getDate());

				System.out.println("Register date " + registerDate + " , " + LocalDate.parse(registerDate));
				customer.setRegister_date(LocalDateTime.parse(registerDate));
				customer.setExpired_date(LocalDateTime.parse(registerDate));
				customer.setLast_date_use(LocalDateTime.parse(registerDate));
				System.out.println("After set : " + customer.getRegister_date());
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

			}
		});

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearForm();
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
