package forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import entities.Employee;
import services.AuthService;
import services.EmployeeService;
import shared.utils.CurrentUserHolder;

public class LoginForm {

	public JFrame frame;
	private Employee employee;
	private EmployeeService employeeService;
	private AuthService authService;
	private JTextField txtUserName;
	private JPasswordField passwordField;
	private JButton btnLogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LoginForm window = new LoginForm();

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
	public LoginForm() {
		initialize();
		initializeDependency();
	}

	private void initializeDependency() {
		this.employeeService = new EmployeeService();
		this.authService = new AuthService();
	}

	public LoginForm(Employee employee) {
		this.employee = employee;
		initialize();
		initializeDependency();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(400, 150, 629, 459);
		frame.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				txtUserName.requestFocusInWindow();
			}
		});
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();

		JPanel imgPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				groupLayout.createSequentialGroup()
						.addComponent(imgPanel, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
				.addComponent(imgPanel, GroupLayout.PREFERRED_SIZE, 420, GroupLayout.PREFERRED_SIZE));

		JLabel lblImage = new JLabel("");
		lblImage.setForeground(Color.WHITE);
		lblImage.setBackground(Color.WHITE);
		Image img = new ImageIcon(this.getClass().getResource("/login.png")).getImage().getScaledInstance(275, 343,
				Image.SCALE_SMOOTH);
		lblImage.setIcon(new ImageIcon(img));

		GroupLayout gl_imgPanel = new GroupLayout(imgPanel);
		gl_imgPanel.setHorizontalGroup(gl_imgPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_imgPanel.createSequentialGroup()
						.addComponent(lblImage, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_imgPanel.setVerticalGroup(gl_imgPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_imgPanel.createSequentialGroup()
						.addComponent(lblImage, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE).addContainerGap()));
		imgPanel.setLayout(gl_imgPanel);

		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.DARK_GRAY);
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 25));

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));

		txtUserName = new JTextField();
		txtUserName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				passwordField.requestFocus();
			}
		});
		txtUserName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtUserName.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));

		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnLogin.requestFocus();

			}
		});
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JLabel lblMessage = new JLabel();
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setForeground(Color.RED);
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnLogin = new JButton("Login");

		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (null != employee) {
					employee.setUsername(txtUserName.getText());
					employee.setPassword(String.valueOf(passwordField.getPassword()));

					if (!employee.getUsername().isBlank() && !employee.getPassword().isBlank()) {
						employeeService.updateEmployee(String.valueOf(employee.getId()), employee);
						frame.setVisible(false);
						EmployeeForm employeeForm = new EmployeeForm();
						employeeForm.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "Fill required fields");
					}
				} else {
					String username = txtUserName.getText();
					String password = String.valueOf(passwordField.getPassword());

					if (!username.isBlank() && !password.isBlank()) {
						String loggedInUserId = authService.login(username, password);
						if (!loggedInUserId.isBlank()) {
							CurrentUserHolder.setLoggedInUser(employeeService.findEmployeeById(loggedInUserId));
							JOptionPane.showMessageDialog(null, "Successfully Login");
							frame.setVisible(false);
							HomeForm homeform = new HomeForm();
							homeform.frame.setVisible(true);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Enter required Fields");
					}
				}

			}

		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JCheckBox chckboxshowpwd = new JCheckBox("Show Password");
		chckboxshowpwd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckboxshowpwd.isSelected()) {
					passwordField.setEchoChar((char) 0);

				} else {
					passwordField.setEchoChar('●');
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup()
						.addGap(3)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createSequentialGroup().addGap(72).addComponent(lblNewLabel,
										GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel.createSequentialGroup().addGap(3).addComponent(lblPassword,
								GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup().addComponent(chckboxshowpwd)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 346, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(passwordField, Alignment.LEADING).addComponent(txtUserName,
										Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup().addGap(49).addComponent(btnLogin,
								GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addGap(66).addComponent(lblUsername,
										GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
						.addGap(8).addComponent(txtUserName, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addGap(13)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addGap(10).addComponent(lblMessage,
										GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(chckboxshowpwd)))
						.addGap(10).addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(67, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
}
