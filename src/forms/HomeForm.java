package forms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.JToggleButton;
import java.awt.Font;
import java.awt.PopupMenu;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import forms.JpanelLoader;
import menu.MenuItem;
import forms.CreateLayoutProperties;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

public class HomeForm {

	JFrame frame;
	private JpanelLoader jploader = new JpanelLoader();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	private final Action action = new SwingAction();
	private JPanel menuPanel;
	private JPanel panel_loader;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeForm window = new HomeForm();
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
	public HomeForm() {
		initialize();
		addMenuItem();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBackground(Color.WHITE);
		frame.setBounds(0, 0, 1000, 500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuPanel = new JPanel();
		menuPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		menuPanel.setBackground(new Color(153, 51, 204));

		panel_loader = new JPanel();
		panel_loader.setBackground(Color.WHITE);

		panel_loader.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(menuPanel, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_loader, GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(menuPanel, GroupLayout.PREFERRED_SIZE, 695, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addComponent(panel_loader, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)))
		);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		GroupLayout gl_panel_loader = new GroupLayout(panel_loader);
		gl_panel_loader.setHorizontalGroup(
				gl_panel_loader.createParallelGroup(Alignment.LEADING).addGap(0, 720, Short.MAX_VALUE));
		gl_panel_loader.setVerticalGroup(
				gl_panel_loader.createParallelGroup(Alignment.LEADING).addGap(0, 431, Short.MAX_VALUE));
		panel_loader.setLayout(gl_panel_loader);
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		frame.getContentPane().setLayout(groupLayout);

	}

	private void setBorder(Border createEmptyBorder) {
		// TODO Auto-generated method stub

	}

	private void addMenuItem() {

        ImageIcon iconHome = new ImageIcon(getClass().getResource("/home-20.png"));
        ImageIcon iconSale = new ImageIcon(getClass().getResource("/salefill-20.png"));
        ImageIcon iconPurchase = new ImageIcon(getClass().getResource("/puchase-20.png"));
        ImageIcon iconCustomer = new ImageIcon(getClass().getResource("/customer.png"));
        ImageIcon iconBook = new ImageIcon(getClass().getResource("/book-20.png"));
        ImageIcon iconAuthor = new ImageIcon(getClass().getResource("/author-20.png"));
        ImageIcon iconPublisher = new ImageIcon(getClass().getResource("/publisher-18.png"));
        ImageIcon iconEmployee = new ImageIcon(getClass().getResource("/employee-20.png"));
        ImageIcon iconCategory = new ImageIcon(getClass().getResource("/category-19.png"));

		// create sub menu
		MenuItem menuAuthor = new MenuItem(iconAuthor, "Author", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				AuthorFormnew authForm = new AuthorFormnew();
				jploader.jPanelLoader(panel_loader, authForm);
			}
		});
		menuAuthor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		menuAuthor.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));

		MenuItem menuPublisher = new MenuItem(iconPublisher, "Publisher", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				PublisherForm publisherForm = new PublisherForm();
				jploader.jPanelLoader(panel_loader, publisherForm);
			}
		});
		menuPublisher.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		menuPublisher.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));

		MenuItem menuCategory = new MenuItem(iconCategory, "Category", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CategoryForm categoryForm = new CategoryForm();
				jploader.jPanelLoader(panel_loader, categoryForm);
			}
		});
		menuCategory.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		menuCategory.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));

		MenuItem menuBookList = new MenuItem(iconBook, "Book", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				BookListForm bookListForm = new BookListForm();
				jploader.jPanelLoader(panel_loader, bookListForm);
			}
		});
		menuBookList.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		menuBookList.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));

		// create main menu
		MenuItem menuHome = new MenuItem(iconHome, "Home", null);

		MenuItem menuSale = new MenuItem(iconSale, "Sale", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SaleForm3 saleform = new SaleForm3();
				jploader.jPanelLoader(panel_loader, saleform);
			}

		});

		MenuItem menuPurchase = new MenuItem(iconPurchase, "Purchase", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				PurchaseForm puchase = new PurchaseForm();
				jploader.jPanelLoader(panel_loader, puchase);
			}
		});

		MenuItem menuCustomer = new MenuItem(iconCustomer, "Customer", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CustomerListFormNew cusListForm = new CustomerListFormNew();
				jploader.jPanelLoader(panel_loader, cusListForm);
			}

		});

		MenuItem menuBookManage = new MenuItem(iconBook, "Book Manage", null, menuAuthor, menuPublisher, menuCategory, menuBookList);

		MenuItem menuEmployee = new MenuItem(iconEmployee, "Employee", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				EmployeeForm femployee = new EmployeeForm();
				jploader.jPanelLoader(panel_loader, femployee);
			}

		});

		addMenu(menuHome, menuSale, menuPurchase, menuCustomer, menuBookManage, menuEmployee);
	}

	private void addMenu(MenuItem... menu) {
		for (int i = 0; i < menu.length; i++) {
			menuPanel.add(menu[i]);
			ArrayList<MenuItem> subMenu = menu[i].getSubMenu();
			for (MenuItem m : subMenu) {
				addMenu(m);
			}
		}
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
