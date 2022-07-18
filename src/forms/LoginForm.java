package forms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class LoginForm {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblusername = new JLabel("Username");
		lblusername.setBounds(33, 53, 116, 41);
		panel.add(lblusername);

		JButton btnNewButton = new JButton("Login");
		btnNewButton.setBounds(119, 159, 89, 23);
		panel.add(btnNewButton);

		textField = new JTextField();
		textField.setBounds(103, 63, 86, 20);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblusername_1 = new JLabel("Username");
		lblusername_1.setBounds(22, 105, 116, 41);
		panel.add(lblusername_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(103, 105, 86, 20);
		panel.add(textField_1);

		JButton btnNewButton_1 = new JButton("Login");
		btnNewButton_1.setBounds(248, 159, 89, 23);
		panel.add(btnNewButton_1);
	}
}
