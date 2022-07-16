package forms;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import entities.Employee;
import services.EmployeeService;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class EmployeeForm extends JPanel {

	private JTextField txtName, txtSearch;
	private JTable tblshowEmployee;
	private JLabel lblEmployeeID, lblEmployee;
	private Employee employee;
	private EmployeeService employeeService = new EmployeeService();
	private DefaultTableModel dtm = new DefaultTableModel();
	private List<Employee> originalEmployeeList = new ArrayList<>();
	private JTextField txtusername;
	private JTextField txtpwd;
	private JTextField txtlevel;
	private JTextField txtgender;
	private JTextField txtphonno;
	private JTextField txtaddress;

	/**
	 * Create the panel.
	 */

	public EmployeeForm() {
		initialize();
		this.setTableDesign();
		this.loadAllEmployee(Optional.empty());
	}

	private void loadAllEmployee(Optional<List<Employee>> optionalEmployee) {
		this.dtm = (DefaultTableModel) this.tblshowEmployee.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();

		this.originalEmployeeList = this.employeeService.findAllEmployees();
		List<Employee> employee = optionalEmployee.orElseGet(() -> originalEmployeeList);

		employee.forEach(b -> {
			Object[] row = new Object[7];
			row[0] = b.getId();
			row[1] = b.getName();
			row[2] = b.getUsername();
			row[3] = b.getPassword();
			row[4] = b.getgender();
			row[5] = b.getPhone();
			row[6] = b.getAddress();
			

			dtm.addRow(row);
		});

		tblshowEmployee.setModel(dtm);

	}

	private void setTableDesign() {
		dtm.addColumn("Employee ID");
		dtm.addColumn("Employee Name");
		dtm.addColumn("Username");
		dtm.addColumn("Password");
		dtm.addColumn("Gender");
		dtm.addColumn("PhoneNo");
		dtm.addColumn("Address");
		tblshowEmployee.setModel(dtm);

	}

	private void resetFormData() {
		txtName.setText("");
		txtSearch.setText("");
	}

	private void initialize() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setBounds(42, 11, 809, 450);

		JLabel lblAuthorName = new JLabel("Employee Name");
		lblAuthorName.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtName = new JTextField();
		txtName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (null != employee && !employee.getId().isBlank()) {
					employee.setName(txtName.getText());
					if (!employee.getName().isBlank()) {
						employeeService.updateEmployee(String.valueOf(employee.getId()), employee);
						resetFormData();
						loadAllEmployee(Optional.empty());
						employee = null;
					} else {
						JOptionPane.showMessageDialog(null, "Please enter Author Name!");
					}
				} else {
					Employee author = new Employee();
					author.setName(txtName.getText());
					if (null != author.getName() && !author.getName().isBlank()) {

						employeeService.createEmployee(employee);
						resetFormData();
						loadAllEmployee(Optional.empty());
					} else {
						JOptionPane.showMessageDialog(null, "Enter Required Field!");
					}
				}

			}
		});
		txtName.setColumns(10);

		lblEmployeeID = new JLabel("Employee ID");
		lblEmployeeID.setFont(new Font("Tahoma", Font.BOLD, 14));

		lblEmployee = new JLabel("Show Employee ID");
		lblEmployee.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtSearch = new JTextField();
		txtSearch.setColumns(10);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				Employee employee = new Employee();
					employee.setName(txtName.getText());
					if (null != employee.getName() && !employee.getName().isBlank()) {

						employeeService.createEmployee(employee);
						resetFormData();
						loadAllEmployee(Optional.empty());
					} else {
						JOptionPane.showMessageDialog(null, "Enter Required Field!");
					
				}
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (null != employee) {
					employeeService.blockEmployee(employee.getId() + "");
					resetFormData();
					loadAllEmployee(Optional.empty());
					employee = null;
				} else {
					JOptionPane.showMessageDialog(null, "Please Select Author");
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (null != employee && !employee.getId().isBlank()) {
					employee.setName(txtName.getText());
					if (!employee.getName().isBlank()) {
						employeeService.updateEmployee(String.valueOf(employee.getId()), employee);
						resetFormData();
						loadAllEmployee(Optional.empty());
						employee = null;
					} else {
						JOptionPane.showMessageDialog(null, "Please enter Author Name!");
					}

				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel showtablepanel = new JPanel();
		showtablepanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		showtablepanel.setLayout(null);

		tblshowEmployee = new JTable();
		tblshowEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblshowEmployee.setSize(new Dimension(789, 288));
		tblshowEmployee.setRowHeight(30);
		tblshowEmployee.setFont(new Font("Tahoma", Font.BOLD, 14));
		tblshowEmployee.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.tblshowEmployee.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			if (!tblshowEmployee.getSelectionModel().isSelectionEmpty()) {

				String id = tblshowEmployee.getValueAt(tblshowEmployee.getSelectedRow(), 0).toString();
				employee = employeeService.findEmployeeById(id);

				lblEmployee.setText(employee.getId());
				txtName.setText(employee.getName());
				txtusername.setText(employee.getUsername());
				txtpwd.setText(employee.getPassword());
				txtgender.setText(employee.getgender());
				txtphonno.setText(employee.getPhone());
				txtaddress.setText(employee.getAddress());
				

			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 0, 388, 380);
		showtablepanel.add(scrollPane);
		scrollPane.setViewportView(tblshowEmployee);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollPane.setColumnHeaderView(scrollBar);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String keyword = txtSearch.getText();

				loadAllEmployee(Optional.of(originalEmployeeList.stream()
						.filter(b -> b.getName().toLowerCase(Locale.ROOT).startsWith(keyword.toLowerCase(Locale.ROOT)))
						.collect(Collectors.toList())));
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		txtusername = new JTextField();
		txtusername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		txtpwd = new JTextField();
		txtpwd.setColumns(10);
		
		JLabel lblEmplevel = new JLabel("Emp_Level");
		lblEmplevel.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		txtlevel = new JTextField();
		txtlevel.setColumns(10);
		
		JLabel lblAge = new JLabel("Gender");
		lblAge.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		txtgender = new JTextField();
		txtgender.setColumns(10);
		
		JLabel lblPhoneNo = new JLabel("Phone No");
		lblPhoneNo.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		txtphonno = new JTextField();
		txtphonno.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		txtaddress = new JTextField();
		txtaddress.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(8)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEmployeeID, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
							.addGap(113)
							.addComponent(lblSearch, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
							.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
							.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAuthorName, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEmplevel, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAge, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPhoneNo, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblAddress, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtusername, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtpwd, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtlevel, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtgender, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtphonno, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtaddress, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(13)
									.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
									.addGap(20)
									.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)))
							.addGap(10)
							.addComponent(showtablepanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(32))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblEmployeeID, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmployee, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(7)
							.addComponent(lblSearch, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(9)
							.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
					.addGap(2)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblAuthorName, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(lblEmplevel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(lblAge, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(lblPhoneNo, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(lblAddress, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(txtusername, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addComponent(txtpwd, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(txtlevel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(txtgender, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(txtphonno, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(txtaddress, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(showtablepanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
		);
		setLayout(groupLayout);

	}
}
