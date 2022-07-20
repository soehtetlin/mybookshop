package forms;

import entities.Employee;
import services.AuthService;
//import services.AuthService;
import services.EmployeeService;
import shared.utils.CurrentUserHolder;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout.Alignment;

public class CredentialInfoForm {

	public JFrame frame;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private Employee employee;
	private EmployeeService employeeService;
	private AuthService authService;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CredentialInfoForm window = new CredentialInfoForm();
					
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CredentialInfoForm() {
		initialize();
		initializeDependency();
	}
	
	

	private void initializeDependency() {
		this.employeeService = new EmployeeService();
		this.authService = new AuthService();
	}

	public CredentialInfoForm(Employee employee) {
		this.employee = employee;
		initialize();
		initializeDependency();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(400, 150, 553, 408);
		frame.addWindowFocusListener((WindowFocusListener) new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        txtUsername.requestFocusInWindow();
		    }
		});
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblNewLabel = new JLabel(employee != null ? "Employee : " + employee.getName() : "Login");
		lblNewLabel.setForeground(Color.DARK_GRAY);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Lithos Pro Regular", Font.PLAIN, 25));

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));

		txtUsername = new JTextField();
		txtUsername.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtPassword.requestFocus();
				
			}
		});
		txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtUsername.setColumns(10);

		txtPassword = new JPasswordField();
		txtPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadmain();
			}
		});
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnLogin = new JButton(employee != null ? "Create Account" : "Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (null != employee) {
					employee.setUsername(txtUsername.getText());
					employee.setPassword(String.valueOf(txtPassword.getPassword()));

					if (!employee.getUsername().isBlank() && !employee.getPassword().isBlank()) {
						employeeService.updateEmployee(String.valueOf(employee.getId()), employee);
						frame.setVisible(false);
						
						EmployeeForm employeeForm = new EmployeeForm();
						employeeForm.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "Fill required fields");
					}
				} else {
					String username = txtUsername.getText();
					String password = String.valueOf(txtPassword.getPassword());

					if (!username.isBlank() && !password.isBlank()) {
						String loggedInUserId = authService.login(username, password);
						if (!loggedInUserId.isBlank()) {
							CurrentUserHolder.setLoggedInUser(employeeService.findEmployeeById(loggedInUserId));
							JOptionPane.showMessageDialog(null, "Successfully Login");
							frame.setVisible(false);
							HomeFormNew homeformnew = new HomeFormNew();
							homeformnew.frame.setVisible(true);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Enter required Fields");
					}
				}

			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblMessage = new JLabel();
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMessage.setForeground(Color.RED);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(101)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(72)
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
						.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
					.addGap(164))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(98)
					.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 346, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(101)
					.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(98)
					.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 346, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(98)
					.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 346, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(147)
					.addComponent(btnLogin, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
					.addGap(150))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
							.addGap(17))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(66)
							.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)))
					.addGap(8)
					.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(13)
					.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(57))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	
	private void loadmain() {
		
		if (null != employee) {
			employee.setUsername(txtUsername.getText());
			employee.setPassword(String.valueOf(txtPassword.getPassword()));

			if (!employee.getUsername().isBlank() && !employee.getPassword().isBlank()) {
				employeeService.updateEmployee(String.valueOf(employee.getId()), employee);
				frame.setVisible(false);
				
				EmployeeForm employeeForm = new EmployeeForm();
				//employeeForm.frame.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "Fill required fields");
			}
		} else {
			String username = txtUsername.getText();
			String password = String.valueOf(txtPassword.getPassword());

			if (!username.isBlank() && !password.isBlank()) {
				String loggedInUserId = authService.login(username, password);
				if (!loggedInUserId.isBlank()) {
					CurrentUserHolder.setLoggedInUser(employeeService.findEmployeeById(loggedInUserId));
					JOptionPane.showMessageDialog(null, "Successfully Login");
					frame.setVisible(false);
					HomeFormNew homeformnew = new HomeFormNew();
					homeformnew.frame.setVisible(true);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Enter required Fields");
			}
		}

	}
	
	
}
