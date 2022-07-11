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
		setLayout(null);
		setBounds(42, 11, 809, 450);

		JLabel lblAuthorName = new JLabel("Employee Name");
		lblAuthorName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAuthorName.setBounds(10, 52, 116, 39);
		add(lblAuthorName);

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
		txtName.setBounds(136, 52, 222, 39);
		add(txtName);
		txtName.setColumns(10);

		lblEmployeeID = new JLabel("Employee ID");
		lblEmployeeID.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmployeeID.setBounds(10, 2, 116, 39);
		add(lblEmployeeID);

		lblEmployee = new JLabel("Show Employee ID");
		lblEmployee.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmployee.setBounds(136, 2, 173, 39);
		add(lblEmployee);

		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSearch.setBounds(422, 9, 62, 39);
		add(lblSearch);

		txtSearch = new JTextField();
		txtSearch.setColumns(10);
		txtSearch.setBounds(484, 11, 173, 39);
		add(txtSearch);

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
		btnSave.setBounds(10, 398, 116, 41);
		add(btnSave);

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
		btnDelete.setBounds(149, 398, 116, 41);
		add(btnDelete);

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
		btnUpdate.setBounds(285, 398, 116, 41);
		add(btnUpdate);

		JPanel showtablepanel = new JPanel();
		showtablepanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		showtablepanel.setBounds(411, 58, 388, 374);
		add(showtablepanel);
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
		btnSearch.setBounds(667, 8, 108, 41);
		add(btnSearch);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUsername.setBounds(10, 102, 116, 39);
		add(lblUsername);
		
		txtusername = new JTextField();
		txtusername.setColumns(10);
		txtusername.setBounds(136, 102, 222, 39);
		add(txtusername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPassword.setBounds(10, 150, 116, 39);
		add(lblPassword);
		
		txtpwd = new JTextField();
		txtpwd.setColumns(10);
		txtpwd.setBounds(136, 150, 222, 39);
		add(txtpwd);
		
		JLabel lblEmplevel = new JLabel("Emp_Level");
		lblEmplevel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmplevel.setBounds(10, 200, 116, 39);
		add(lblEmplevel);
		
		txtlevel = new JTextField();
		txtlevel.setColumns(10);
		txtlevel.setBounds(136, 200, 222, 39);
		add(txtlevel);
		
		JLabel lblAge = new JLabel("Gender");
		lblAge.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAge.setBounds(10, 246, 116, 39);
		add(lblAge);
		
		txtgender = new JTextField();
		txtgender.setColumns(10);
		txtgender.setBounds(136, 246, 222, 39);
		add(txtgender);
		
		JLabel lblPhoneNo = new JLabel("Phone No");
		lblPhoneNo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPhoneNo.setBounds(10, 296, 116, 39);
		add(lblPhoneNo);
		
		txtphonno = new JTextField();
		txtphonno.setColumns(10);
		txtphonno.setBounds(136, 296, 222, 39);
		add(txtphonno);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAddress.setBounds(10, 348, 116, 39);
		add(lblAddress);
		
		txtaddress = new JTextField();
		txtaddress.setColumns(10);
		txtaddress.setBounds(136, 348, 222, 39);
		add(txtaddress);

	}
}
