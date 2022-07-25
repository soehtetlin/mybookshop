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
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.JToggleButton;

import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import entities.Book;
import entities.SaleDetails;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import forms.JpanelLoader;
import forms.BookListForm.ButtonEditor;
import forms.BookListForm.ButtonRenderer;
import menu.MenuItem;
import forms.CreateLayoutProperties;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JTextField;

import services.BookService;
import services.CustomerService;
import services.PurchaseService;
import services.SaleService;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeForm {

	JFrame frame;
	private JpanelLoader jploader = new JpanelLoader();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	private final Action action = new SwingAction();
	private JPanel menuPanel;
	private JPanel panel_loader;
	
	private JLabel showBookCover;
	private JTable tbltopten;
	private DefaultTableModel dtmtblopen = new DefaultTableModel();
	private BookService bookservice = new BookService();
	private SaleService saleservice = new SaleService();
	private PurchaseService purchaseService = new PurchaseService();
	
	private List<SaleDetails> originalSaleDetails = new ArrayList<>();

	private CustomerService customerService = new CustomerService();

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

	private void setTableForTopTenBooks() {
		dtmtblopen.addColumn("No");
		//dtmtblopen.addColumn("Cover Photo");
		dtmtblopen.addColumn("Book Name");
		dtmtblopen.addColumn("Quantity");
		tbltopten.setRowHeight(40);
		tbltopten.setModel(dtmtblopen);
		
		DefaultTableCellRenderer dfcr = new DefaultTableCellRenderer();
		dfcr.setHorizontalAlignment(JLabel.CENTER);
		tbltopten.getColumnModel().getColumn(0).setCellRenderer(dfcr);
		//tbltopten.getColumnModel().getColumn(1).setCellRenderer(new ImagerRender());
		tbltopten.getColumnModel().getColumn(1).setCellRenderer(dfcr);
		tbltopten.getColumnModel().getColumn(2).setCellRenderer(dfcr);

	}

	public HomeForm() {
		initialize();
		addMenuItem();
		setTableForTopTenBooks();
		loadAllBooks(Optional.empty());
		showPhoto();
	}

	private void showPhoto() {
		// TODO Auto-generated method stub
		String name = tbltopten.getValueAt(0, 1).toString();
		Book book = bookservice.findByName(name);
		ImageIcon imageIcon = new ImageIcon(
				new ImageIcon(book.getPhoto()).getImage().getScaledInstance(297,270, Image.SCALE_SMOOTH));
		showBookCover.setIcon(imageIcon);
	}

	private void loadAllBooks(Optional<List<SaleDetails>> optionalSale) {
		this.dtmtblopen = (DefaultTableModel) this.tbltopten.getModel();
		this.dtmtblopen.getDataVector().removeAllElements();
		this.dtmtblopen.fireTableDataChanged();

		this.originalSaleDetails = this.saleservice.findtoptenbooks();
		int i=1;
		List<SaleDetails> salelist = optionalSale.orElseGet(() -> originalSaleDetails);
		Vector<String> vno = new Vector<String>();
		salelist.forEach(e -> {

			Object[] row = new Object[4];
			row[0] = (vno.size() + 1);
			System.out.println("Vno sie" + vno.size());
			//row[1] = e.getBook().getPhoto();
			row[1] = e.getBook().getName();
			vno.addElement(e.getBook().getName());
			row[2] = e.getQuantity();
			
			dtmtblopen.addRow(row);
			
		
		});
		this.tbltopten.setModel(dtmtblopen);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setBackground(Color.WHITE);
		frame.setBounds(0, 0, 1321, 653);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//frame.setLocationRelativeTo(panel_loader);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		menuPanel = new JPanel();
		menuPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		menuPanel.setBackground(new Color(153, 51, 204));

		panel_loader = new JPanel();
		panel_loader.setBackground(Color.WHITE);

		panel_loader.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(menuPanel, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_loader, GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(menuPanel, GroupLayout.PREFERRED_SIZE, 695,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addComponent(panel_loader, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))));

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
				.addComponent(panel_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE));
		
		showBookCover = new JLabel();
		
		showBookCover.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(68)
					.addComponent(showBookCover, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(131, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(34)
					.addComponent(showBookCover, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(267, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);

		JLabel lbltopten = new JLabel("Top 10 Books for July");
		lbltopten.setFont(new Font("Tahoma", Font.BOLD, 16));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2
				.setHorizontalGroup(
						gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup()
										.addComponent(lbltopten, GroupLayout.PREFERRED_SIZE, 220,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap(316, Short.MAX_VALUE))
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
						.addComponent(lbltopten, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)));

		tbltopten = new JTable();
		cLayout.setTable(tbltopten);
		tbltopten.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String name = tbltopten.getValueAt(tbltopten.getSelectedRow(), 1).toString();
				System.out.println("Baok name in homeformName " + name);
				Book book = bookservice.findByName(name);
				System.out.println("ID homeform "+ book.getId());
				System.out.println("Name homeform "+ book.getName());

				showBookCover.setText("");
				ImageIcon imageIcon = new ImageIcon(
						new ImageIcon(book.getPhoto()).getImage().getScaledInstance(showBookCover.getWidth(),showBookCover.getHeight(), Image.SCALE_SMOOTH));
				showBookCover.setIcon(imageIcon);
				System.out.println("Wid" + showBookCover.getWidth());
				System.out.println("He" + showBookCover.getHeight());
				showBookCover.setHorizontalAlignment(SwingConstants.CENTER);

				// txtStockAmount.setText(String.valueOf(book.getStockamount()));
			}
		});
//		tbltopten.setFont(new Font("Tahoma", Font.BOLD, 14));
//		tbltopten.setBackground(Color.WHITE);
//		// tbltopten.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		tbltopten.setForeground(Color.DARK_GRAY);
		tbltopten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbltopten.setDefaultEditor(Object.class, null);
		tbltopten.setAutoCreateRowSorter(true);
//		JTableHeader jtableheader = tbltopten.getTableHeader();
//		jtableheader.setBackground(SystemColor.textHighlight);
//		jtableheader.setForeground(Color.white);
//		jtableheader.setFont(new Font("Tahoma", Font.BOLD, 14));
		scrollPane.setViewportView(tbltopten);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);

		JLabel lblbookquantity = new JLabel("Display Total Book");
		lblbookquantity.setForeground(new Color(245, 245, 245));
		lblbookquantity.setBackground(new Color(153, 50, 204));
		lblbookquantity.setOpaque(true);
//		lblbookquantity.setBorder(BorderFactory.createEtchedBorder(new Color(128, 0, 128), Color.black));
		lblbookquantity.setText(
				"<html>Total Books<br></hmtl><html><h1>" + Integer.toString(bookservice.CountBook()) + "</h1><br>View all books-></html>");
		lblbookquantity.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				BookListForm bl = new BookListForm();
				jploader.jPanelLoader(panel_loader, bl);
			}
		});
		lblbookquantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblbookquantity.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblDisplayCustomer = new JLabel("Display Customer");
		lblDisplayCustomer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				CustomerListFormNew cln = new CustomerListFormNew();
				jploader.jPanelLoader(panel_loader, cln);
				
			}
		});
		lblDisplayCustomer.setForeground(new Color(245, 245, 245));
		lblDisplayCustomer.setBackground(new Color(153, 50, 204));
		lblDisplayCustomer.setOpaque(true);
		lblDisplayCustomer.setBorder(BorderFactory.createEtchedBorder(new Color(128, 0, 128), Color.black));

		lblDisplayCustomer.setText("<html>Total Customers<br></hmtl><html><h1>"
				+ Integer.toString(customerService.CountCustomer()) + "</h1><br>View all customer-></br></html>");
		lblDisplayCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblDisplayCustomer.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblDisplaySaleTotal = new JLabel("Display Sale Total");
		lblDisplaySaleTotal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SaleDetailForm saledetail = new SaleDetailForm();
				jploader.jPanelLoader(panel_loader, saledetail);
				
			}
		});
		lblDisplaySaleTotal.setForeground(SystemColor.text);
		lblDisplaySaleTotal.setBackground(new Color(153, 50, 204));
		lblDisplaySaleTotal.setOpaque(true);
		lblDisplaySaleTotal.setBorder(BorderFactory.createEtchedBorder(new Color(128, 0, 128), Color.black));
		String s = Integer.toString(saleservice.TotalSale());
	

		lblDisplaySaleTotal.setText(
				"<html>Today's total sale<br></hmtl><html><h1>" + changeFormat(s) + " Kyats </h1><br>View sale details-></html>");
		lblDisplaySaleTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblDisplaySaleTotal.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout gl_panel_loader = new GroupLayout(panel_loader);
		gl_panel_loader.setHorizontalGroup(
			gl_panel_loader.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
				.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
		);
		gl_panel_loader.setVerticalGroup(
			gl_panel_loader.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_loader.createSequentialGroup()
					.addGap(7)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
					.addGap(7))
		);
		
		
		String ps = Integer.toString(purchaseService.TotalPurchase());
		
		JLabel lblshowPurchase = new JLabel("<html>Today's total Purchase<br></hmtl><html><h1>"+ changeFormat(ps)+" Kyats </h1><br>View purchase details-></html>");
		lblshowPurchase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				PurchaseDetailForm pdf = new PurchaseDetailForm();
				jploader.jPanelLoader(panel_loader, pdf);
			}
		});
		lblshowPurchase.setOpaque(true);
		lblshowPurchase.setHorizontalAlignment(SwingConstants.CENTER);
		lblshowPurchase.setForeground(Color.WHITE);
		lblshowPurchase.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblshowPurchase.setBorder(BorderFactory.createEtchedBorder(new Color(128, 0, 128), Color.black));
		lblshowPurchase.setBackground(new Color(153, 50, 204));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(54)
					.addComponent(lblbookquantity, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
					.addGap(54)
					.addComponent(lblDisplayCustomer, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
					.addGap(41)
					.addComponent(lblDisplaySaleTotal, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
					.addGap(65)
					.addComponent(lblshowPurchase, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)

					.addContainerGap(76, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()

					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblbookquantity, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
						.addComponent(lblDisplayCustomer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblDisplaySaleTotal, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblshowPurchase, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		panel_loader.setLayout(gl_panel_loader);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
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
				AuthorForm authForm = new AuthorForm();
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
		MenuItem menuHome = new MenuItem(iconHome, "Home", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				HomeForm hf = new HomeForm();
				jploader.jPanelLoader(panel_loader, hf.panel_loader);
			}

		});

		MenuItem menuSale = new MenuItem(iconSale, "Sale", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SaleForm saleform = new SaleForm();
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

		MenuItem menuBookManage = new MenuItem(iconBook, "Book Manage", null, menuAuthor, menuPublisher, menuCategory,
				menuBookList);

		MenuItem menuEmployee = new MenuItem(iconEmployee, "Employee", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				EmployeeFormNew employee = new EmployeeFormNew();
				jploader.jPanelLoader(panel_loader, employee);
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
	
	private class ImagerRender extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable arg0, Object photo, boolean arg2, boolean arg3, int arg4,
				int arg5) {
			
			System.out.println("Show store file address :" + photo.toString());

			ImageIcon imageIcon = null;
			
				imageIcon = new ImageIcon(new ImageIcon(photo.toString()).getImage().getScaledInstance(130, 120, Image.SCALE_SMOOTH));

			return new JLabel(imageIcon);
		}

		}
	
	private String changeFormat(String s) {
		int len = s.length(), index = 0;
		StringBuffer str = new StringBuffer("");
		for (int i = 0; i < len; i++) {
			if (index == 3) {
				str.append(",");
				index = 0;
				i--;
			} else {
				str.append(String.valueOf(s).charAt(len - i - 1));
				index++;
			}
		}
		return str.reverse().toString();
	}
}
