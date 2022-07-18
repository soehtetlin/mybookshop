package forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;

import entities.Book;
import entities.Customer;
import services.BookService;
import services.CustomerService;
import java.awt.Component;
import javax.swing.SwingConstants;

public class CustomerListFormNew extends JPanel {
	
	private JTable table;
	private JLabel lbladd;
	public JPanel panel;
	private JScrollPane scrollPane;
	private JButton btnAdd;
	private JComboBox comboMemberActive,combo;
    private DefaultTableModel dtm = new DefaultTableModel();
    private CustomerService customerService;
	private List<Customer> originalCustomerList = new ArrayList<>();
	private JpanelLoader jloader = new JpanelLoader();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	/**
	 * Create the panel.
	 */
	public CustomerListFormNew() {
        this.customerService= new CustomerService();

		initialize();
		setTableDesign();
		loadAllCustomers(Optional.empty());
		buttonOnClick();
	}
	
	private void initialize() {

		panel = new JPanel();
		
		scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblFilter = new JLabel("Filter By : ");
		cLayout.setLabel(lblFilter);
		
		comboMemberActive = new JComboBox();
		cLayout.setComboBox(comboMemberActive);
				
		combo = new JComboBox();
		cLayout.setComboBox(combo);
		
		
		btnAdd = new JButton("Add New");
		btnAdd.setAlignmentX(Component.RIGHT_ALIGNMENT);
	    Image img=new ImageIcon(this.getClass().getResource("/add-20.png")).getImage();
		btnAdd.setIcon(new ImageIcon(img));
		cLayout.setButton(btnAdd);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 725, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 438, Short.MAX_VALUE)
					.addContainerGap())
		);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 849, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(61)
							.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addGap(30)
							.addComponent(comboMemberActive, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addGap(29)
							.addComponent(combo, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 327, Short.MAX_VALUE)
							.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFilter, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboMemberActive, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(combo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE))
		);
//		gl_panel.setAutoCreateContainerGaps(true);
//		gl_panel.setAutoCreateGaps(true);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

		
	}
	
	private void setTableDesign() {
		
		table = new JTable();
		table.setSelectionBackground(new Color(153, 51, 255));
		table.setShowVerticalLines(false);
		table.setFocusable(false);
		
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setBounds(12, 254, 404, -216);
//		table.setBackground(new Color(0,0,0));
		
		table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(new Color(153, 51, 204));
		table.getTableHeader().setForeground(new Color(245, 245, 245));
		table.setRowHeight(25);
	    scrollPane.setViewportView(table);
	    
        dtm.addColumn("ID");
        dtm.addColumn("Name");
        dtm.addColumn("Contact No");
        dtm.addColumn("Email");
        dtm.addColumn("Address");
        dtm.addColumn("Register Date");
        dtm.addColumn("Expire Date");
        dtm.addColumn("Active");

        table.setModel(dtm);           
		}
	
	private void loadAllCustomers(Optional<List<Customer>> optionalCustomer) {
		this.dtm = (DefaultTableModel) this.table.getModel();
		this.dtm.getDataVector().removeAllElements();
		this.dtm.fireTableDataChanged();
		
		this.originalCustomerList= this.customerService.findAllCustomers();
		List<Customer> customerList= optionalCustomer.orElseGet(() -> originalCustomerList);
		System.out.println("Table customer list "+customerList.size());
		customerList.forEach(e -> {
			Object[] row= new Object[9];
			row[0] = e.getId();
			row[1] = e.getName();
			row[2] = e.getContact_no();
			row[3] = e.getEmail();
			row[4] = e.getAddress();
			row[5] = e.getRegister_date();
			row[6] = e.getExpired_date();
			row[7] = e.getLast_date_use();
			row[8] = e.getActive();
			
			dtm.addRow(row);
		});
		this.table.setModel(dtm);
	}
	
	private void buttonOnClick() {
		CustomerFormNew customerForm= new CustomerFormNew();
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jloader.jPanelLoader(panel, customerForm);
			}
		});	}




}
