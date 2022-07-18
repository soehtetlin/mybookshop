package forms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.JToggleButton;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import forms.JpanelLoader;
import forms.CreateLayoutProperties;

public class HomeFormNew {

	private JFrame frame;
	private JpanelLoader jploader = new JpanelLoader();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	private final Action action = new SwingAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeFormNew window = new HomeFormNew();
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
	public HomeFormNew() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 949, 504);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel_tag = new JPanel();
		panel_tag.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JPanel panel_loader = new JPanel();
		panel_loader.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(panel_tag, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_loader, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_loader, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
								.addComponent(panel_tag, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))));
		GroupLayout gl_panel_loader = new GroupLayout(panel_loader);
		gl_panel_loader.setHorizontalGroup(
				gl_panel_loader.createParallelGroup(Alignment.LEADING).addGap(0, 720, Short.MAX_VALUE));
		gl_panel_loader.setVerticalGroup(
				gl_panel_loader.createParallelGroup(Alignment.LEADING).addGap(0, 431, Short.MAX_VALUE));
		panel_loader.setLayout(gl_panel_loader);

		JToggleButton btnNewButton = new JToggleButton("Home");
//		btnNewButton.setForeground(SystemColor.desktop);
//		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
//		btnNewButton.setOpaque(false);
//		btnNewButton.setFocusPainted(false);
//		btnNewButton.setBorderPainted(false);
//		btnNewButton.setContentAreaFilled(false);
		cLayout.setToggleButton(btnNewButton);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JToggleButton btnSale = new JToggleButton("Sale");
		cLayout.setToggleButton(btnSale);

		JToggleButton btnPurchase = new JToggleButton("Purchase");
		btnPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PurchaseForm puchase = new PurchaseForm();
				jploader.jPanelLoader(panel_loader, puchase);
			}
		});
		cLayout.setToggleButton(btnPurchase);

		JToggleButton btnCustomer = new JToggleButton("Customer");
		btnCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PurchaseDetailForm purchaseDetail = new PurchaseDetailForm();
				jploader.jPanelLoader(panel_loader, purchaseDetail);
			}
		});
		cLayout.setToggleButton(btnCustomer);

		JToggleButton btnAuthor = new JToggleButton("Author");
		btnAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		cLayout.setToggleButton(btnAuthor);

		JToggleButton btnPublisher = new JToggleButton("Publisher");
		cLayout.setToggleButton(btnPublisher);

		JToggleButton btnBook = new JToggleButton("Book");
		btnBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BookListForm booklist = new BookListForm();
				jploader.jPanelLoader(panel_loader, booklist);
			}
		});
		cLayout.setToggleButton(btnBook);

		JToggleButton btnEmployee = new JToggleButton("Employee");
		btnEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EmployeeForm femployee = new EmployeeForm();
				jploader.jPanelLoader(panel_loader, femployee);
			}
		});
		cLayout.setToggleButton(btnEmployee);
		GroupLayout gl_panel_tag = new GroupLayout(panel_tag);
		gl_panel_tag.setHorizontalGroup(gl_panel_tag.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_tag
				.createSequentialGroup().addGap(10)
				.addGroup(gl_panel_tag.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_tag.createSequentialGroup()
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addGap(4))
						.addComponent(btnSale, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
						.addComponent(btnPurchase, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
						.addComponent(btnCustomer, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
						.addComponent(btnAuthor, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
						.addComponent(btnPublisher, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
						.addComponent(btnBook, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEmployee, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
				.addGap(15)));
		gl_panel_tag.setVerticalGroup(gl_panel_tag.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_tag.createSequentialGroup().addGap(11)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addGap(11)
						.addComponent(btnSale, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addGap(11)
						.addComponent(btnPurchase, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addGap(11)
						.addComponent(btnCustomer, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addGap(18)
						.addComponent(btnAuthor, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addGap(18)
						.addComponent(btnPublisher, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE).addGap(18)
						.addComponent(btnBook, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE).addGap(11)
						.addComponent(btnEmployee, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE).addGap(23)));
		panel_tag.setLayout(gl_panel_tag);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		frame.getContentPane().setLayout(groupLayout);

	}

	private void setBorder(Border createEmptyBorder) {
		// TODO Auto-generated method stub

	}


	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}
}
