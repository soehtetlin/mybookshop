package forms;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import entities.Employee;
import services.EmployeeService;
import shared.checker.Checking;

import javax.swing.JComboBox;
import java.awt.Color;

public class EmployeeFormNew extends JPanel {
	private JTextField txtEmployeeName;
	private JTextField txtUserName;
	private JTextField txtPassword;
	private JTextField txtGender;
	private JTextField txtPhoneNo;
	private JTextField txtAddress;
	private JButton btnSave, btnCancel;
	private JComboBox comboBox;
	private Employee employee;
	private EmployeeService employeeService;
	private DefaultTableModel dtm = new DefaultTableModel();
	private List<Employee> originalEmployeeList = new ArrayList<>();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	private JTextField txtAge;
	private JTextField txtEmail;
	private JLabel lblImg;

	/**
	 * Create the panel.
	 */
	public EmployeeFormNew() {
		setBackground(Color.WHITE);
		employeeService = new EmployeeService();
		initialize();

	}

	private void initialize() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);

		JPanel panelImage = new JPanel();
		panelImage.setBackground(Color.WHITE);
		
		lblImg = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/empImg.png")).getImage().getScaledInstance(355, 443,
				Image.SCALE_SMOOTH);
		lblImg.setIcon(new ImageIcon(img));
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 458, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelImage, GroupLayout.PREFERRED_SIZE, 355, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panelImage, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		
		GroupLayout gl_panelImage = new GroupLayout(panelImage);
		gl_panelImage.setHorizontalGroup(
			gl_panelImage.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelImage.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblImg, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panelImage.setVerticalGroup(
			gl_panelImage.createParallelGroup(Alignment.LEADING)
				.addComponent(lblImg, GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
		);
		panelImage.setLayout(gl_panelImage);

		JLabel lblEmployeeName = new JLabel("Employee Name");
		cLayout.setLabel(lblEmployeeName);

		txtEmployeeName = new JTextField();
		cLayout.setTextField(txtEmployeeName);

		JLabel lblUserName = new JLabel("User Name");
		cLayout.setLabel(lblUserName);

		txtUserName = new JTextField();
		cLayout.setTextField(txtUserName);

		JLabel lblPassword = new JLabel("Password");
		cLayout.setLabel(lblPassword);

		JLabel lblEmpLevel = new JLabel("Employee Level");
		cLayout.setLabel(lblEmpLevel);

		JLabel lblGender = new JLabel("Gender");
		cLayout.setLabel(lblGender);

		JLabel lblPhoneNo = new JLabel("Phone No");
		cLayout.setLabel(lblPhoneNo);

		JLabel lblAddress = new JLabel("Address");
		cLayout.setLabel(lblAddress);

		txtPassword = new JTextField();
		cLayout.setTextField(txtPassword);

		txtGender = new JTextField();
		cLayout.setTextField(txtGender);

		txtPhoneNo = new JTextField();
		cLayout.setTextField(txtPhoneNo);

		txtAddress = new JTextField();
		cLayout.setTextField(txtAddress);

		comboBox = new JComboBox();
		cLayout.setComboBox(comboBox);
		comboBox.addItem("Admin");
		comboBox.addItem("User");

		btnSave = new JButton("Save");
		cLayout.setButton(btnSave);

		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);
		
		JLabel lblEmail = new JLabel("Email");
		cLayout.setLabel(lblEmail);
		
		JLabel lblAge = new JLabel("Age");
		cLayout.setLabel(lblAge);
		
		txtAge = new JTextField();
		cLayout.setTextField(txtAge);
		
		txtEmail = new JTextField();
		cLayout.setTextField(txtEmail);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblEmployeeName, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblEmpLevel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblGender, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblAge, Alignment.LEADING)
										.addComponent(lblPhoneNo, Alignment.LEADING))
									.addComponent(lblAddress, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblUserName, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(txtAddress, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtPhoneNo, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, 0, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtUserName, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtEmployeeName, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtGender, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtEmail, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtAge, GroupLayout.DEFAULT_SIZE, 220, GroupLayout.PREFERRED_SIZE))
							.addGap(87))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addGap(21))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(40)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEmployeeName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmployeeName))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUserName))
					.addGap(26)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
					.addGap(27)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmpLevel))
					.addGap(28)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtGender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGender))
					.addGap(34)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAge)
						.addComponent(txtAge, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPhoneNo)
						.addComponent(txtPhoneNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail))
					.addGap(27)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblAddress)
						.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(23)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

		buttonOnClick();
	}

	private void clearForm() {
		txtEmployeeName.setText("");
		txtUserName.setText("");
		txtAddress.setText("");
		txtPhoneNo.setText("");
		txtPassword.setText("");
		txtGender.setText("");
		txtAge.setText("");
		txtEmail.setText("");
		comboBox.setSelectedIndex(0);
	}

	private void buttonOnClick() {
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Employee employee = new Employee();
				
				toSaveDataCheck(employee);
				
				if (employee.getName() != null && !employee.getName().isBlank() &&
						!employee.getUsername().isBlank() && employee.getUsername() != null &&
						!employee.getPassword().isBlank() && employee.getPassword() != null &&
						!employee.getAddress().isBlank() && employee.getAddress() != null &&
						!employee.getPhone().isBlank() && employee.getPhone() != null) {

					employeeService.createEmployee(employee);
					employee = null;
					clearForm();
				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearForm();
			}
			
		});
	}

	private void toSaveDataCheck(Employee employee) {
		
		employee.setName(txtEmployeeName.getText());
		employee.setUsername(txtUserName.getText());
		employee.setAddress(txtAddress.getText());
		employee.setgender(txtGender.getText());
		employee.setPassword(txtPassword.getText());
		employee.setEmail(txtEmail.getText());
		
		if(Checking.IsAllDigit(txtPhoneNo.getText()))
			employee.setPhone(txtPhoneNo.getText());
		else
			JOptionPane.showMessageDialog(null, "Phone number must be digit.");
		
		if(Checking.IsAllDigit(txtAge.getText()))
			employee.setage(Integer.parseInt(txtAge.getText()));
		else
			JOptionPane.showMessageDialog(null, "Age must be digit.");

		
		if(comboBox.getSelectedIndex() == 0) {
			employee.setEmp_level("Admin");
		} else if(comboBox.getSelectedIndex() == 1)
			employee.setEmp_level("User");
	}
}
