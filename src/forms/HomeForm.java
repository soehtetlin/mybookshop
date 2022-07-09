package forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JToggleButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class HomeForm extends JFrame {

	private JPanel contentPane;
	private JpanelLoader jloader = new JpanelLoader();
	private AuthorForm auform = new AuthorForm();
	private PurchaseForm purchaseform = new PurchaseForm();
	private CategoryForm categoryform = new CategoryForm();
	private EmployeeForm employeeform = new EmployeeForm();
	private BookForm bookform = new BookForm();
	private PublisherForm publisherForm = new PublisherForm();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeForm frame = new HomeForm();
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HomeForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setOpaque(true);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 11, 156, 450);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_load = new JPanel();
		panel_load.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_load.setBounds(176, 11, 809, 450);
		contentPane.add(panel_load);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Home");
		tglbtnNewToggleButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		tglbtnNewToggleButton.setBounds(10, 11, 136, 42);
		panel.add(tglbtnNewToggleButton);
		
		JToggleButton tglbtnManageCategory = new JToggleButton("Sales");
		tglbtnManageCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel_load, auform);
			}
		});
		tglbtnManageCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		tglbtnManageCategory.setBounds(10, 64, 136, 42);
		panel.add(tglbtnManageCategory);
		
		JToggleButton tglbtnManageAuthor = new JToggleButton("Purchase");
		tglbtnManageAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel_load, purchaseform);
			}
		});
		tglbtnManageAuthor.setFont(new Font("Tahoma", Font.BOLD, 14));
		tglbtnManageAuthor.setBounds(10, 117, 136, 42);
		panel.add(tglbtnManageAuthor);
		
		JToggleButton tglbtnReports = new JToggleButton("Customers");
		tglbtnReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel_load, publisherForm);
			}
		});
		tglbtnReports.setFont(new Font("Tahoma", Font.BOLD, 14));
		tglbtnReports.setBounds(10, 170, 136, 42);
		panel.add(tglbtnReports);
		
		JToggleButton tglbtnManageAccount = new JToggleButton("Books");
		tglbtnManageAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel_load, bookform);
			}
		});
		tglbtnManageAccount.setFont(new Font("Tahoma", Font.BOLD, 14));
		tglbtnManageAccount.setBounds(10, 223, 136, 42);
		panel.add(tglbtnManageAccount);
		
		JToggleButton tglbtnManageBook = new JToggleButton("Category");
		tglbtnManageBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel_load, categoryform);
			}
		});
		tglbtnManageBook.setFont(new Font("Tahoma", Font.BOLD, 14));
		tglbtnManageBook.setBounds(10, 276, 136, 42);
		panel.add(tglbtnManageBook);
		
		JToggleButton tglbtnManageSupplier = new JToggleButton("Employee");
		tglbtnManageSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel_load, employeeform);
			}
		});
		tglbtnManageSupplier.setFont(new Font("Tahoma", Font.BOLD, 14));
		tglbtnManageSupplier.setBounds(10, 329, 136, 42);
		panel.add(tglbtnManageSupplier);
		
		JMenu mnNewMenu = new JMenu("Books");
		mnNewMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
		mnNewMenu.setBounds(10, 384, 123, 36);
		panel.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Authors");
		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.BOLD, 14));
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Category");
		mntmNewMenuItem_1.setFont(new Font("Segoe UI", Font.BOLD, 14));
		mnNewMenu.add(mntmNewMenuItem_1);
		
		
	}
}
