package forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Customer;
import entities.Publisher;
import services.AuthorService;
import services.BookService;
import services.CategoryService;
import services.CustomerService;
import services.PublisherService;
import shared.checker.Checking;

public class CustomerFormNew extends JPanel implements PropertyChangeListener {
	private JTextField txtCustomerName;
	private JTextField txtContactNo;
	private JTextField txtEmail;
	private JTextField txtAddress;
	private JPanel panel;
	private JFormattedTextField txtRegisterDate;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	private JLabel lblNewCustomers, lblCusName,lblContactNo,lblEmail,lblAddress,lblRegisterDate;
	private JButton btnUpdate, btnDelete, btnCancel;
	private JButton btnCalendar;
	private JButton btnSave;
	private Checking checking= new Checking();
	private CustomerService customerService;
//	private CalendarView calendarView=new CalendarView();
	
	/**
	 * Create the panel.
	 */
	public CustomerFormNew() {

		customerService = new CustomerService();
		
		initialize();
		buttonOnClick();
	}
	

	private void initialize() {

		panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		lblNewCustomers = new JLabel("Add New Customer");
		cLayout.setLabel(lblNewCustomers);
		
		lblCusName = new JLabel("Customer Name");
		cLayout.setLabel(lblCusName);
		
		lblContactNo = new JLabel("Contact No");
		cLayout.setLabel(lblContactNo);
		
		lblEmail = new JLabel("Email");
		cLayout.setLabel(lblEmail);
		
		lblAddress = new JLabel("Address");
		cLayout.setLabel(lblAddress);
		
		lblRegisterDate = new JLabel("Registered Date");
		cLayout.setLabel(lblRegisterDate);
		
		txtCustomerName = new JTextField();
		cLayout.setTextField(txtCustomerName);
		
		txtContactNo = new JTextField();
		cLayout.setTextField(txtContactNo);
		
		txtEmail = new JTextField();
		cLayout.setTextField(txtEmail);

		txtAddress = new JTextField();
		cLayout.setTextField(txtAddress);

		txtRegisterDate = new  JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
		txtRegisterDate.setValue(new Date());

//		Image img=new ImageIcon(this.getClass().getResource("/add-20.png")).getImage();
//		txtRegisterDate.setIcon(new ImageIcon(img));
		cLayout.setTextField(txtRegisterDate);
		
		btnUpdate = new JButton("Update");
		cLayout.setButton(btnUpdate);
		
		btnDelete = new JButton("Delete");
		cLayout.setButton(btnDelete);
		
		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);
		
		btnCalendar = new JButton("");
//		cLayout.setButton(btnCalendar);
		btnCalendar.setBounds(new Rectangle(580, 0, 0, 0));
		btnCalendar.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnCalendar.setHorizontalAlignment(SwingConstants.CENTER);

	    Image img=new ImageIcon(this.getClass().getResource("/calendar-20.png")).getImage();
		btnCalendar.setIcon(new ImageIcon(img));
		
		btnSave = new JButton("Save");
		cLayout.setButton(btnSave);

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(56)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnUpdate)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(txtRegisterDate, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCalendar))
						.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtContactNo, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAddress, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblContactNo, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewCustomers, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtCustomerName, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRegisterDate, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCusName, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(318, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(28)
					.addComponent(lblNewCustomers)
					.addGap(18)
					.addComponent(lblCusName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtCustomerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(17)
					.addComponent(lblContactNo, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtContactNo, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblAddress, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblRegisterDate, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtRegisterDate, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCalendar))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(67, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);
			
	}
	
	private void clearForm() {
		txtCustomerName.setText("");
		txtContactNo.setText("");
		txtAddress.setText("");
		txtEmail.setText("");
		txtRegisterDate.setValue(new Date());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
    	//get the selected date from the calendar control and set it to the text field
		if (evt.getPropertyName().equals("selectedDate")) {
            
			java.util.Calendar cal = (java.util.Calendar)evt.getNewValue();
			Date selDate =  cal.getTime();
			txtRegisterDate.setValue(selDate);
        }
		
	}
	
	private void buttonVisible() {
		btnSave.setVisible(true);
		btnCancel.setVisible(true);
		
		btnDelete.setVisible(false);
		btnUpdate.setVisible(false);
	}

	private void buttonOnClick() {
		//wire a listener for the PropertyChange event of the calendar window
//		calendarView.addPropertyChangeListener(this);
//		
//		btnCalendar.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				//render the calendar window below the text field
//				calendarView.setLocation(txtRegisterDate.getLocationOnScreen().x, (txtRegisterDate.getLocationOnScreen().y + txtRegisterDate.getHeight()));
//				//get the Date and assign it to the calendar
//				Date d = (Date)txtRegisterDate.getValue();				
//					
//				System.out.println("txtRegisterDate : "+txtRegisterDate.getText());
//				calendarView.dispose();
//				calendarView.resetSelection(d);				
//				calendarView.setUndecorated(true);
//				calendarView.setVisible(true);
//			}
//			
//		});
		
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Customer customer=new Customer();
				
				customer.setName(txtCustomerName.getText());
				customer.setAddress(txtAddress.getText());
				if(checking.IsAllDigit(txtContactNo.getText()))
					customer.setContact_no(txtContactNo.getText());
				else
					JOptionPane.showMessageDialog(null, "Phone number should be only digits.");
				
				customer.setEmail(txtEmail.getText());
				customer.setRegister_date(LocalDateTime.parse(txtRegisterDate.getText()));
				
				Calendar cal = Calendar.getInstance();
				Date today = cal.getTime();
				cal.add(Calendar.YEAR, 2); // to get previous year add -1
				Date nextYear = cal.getTime();
				System.out.println("next year: "+nextYear);
				
				if(!customer.getName().isBlank() && !customer.getContact_no().isBlank()) {
					customerService.saveCustomer(customer);
					clearForm();
					panel.setVisible(false);
					CustomerListFormNew customerListForm= new CustomerListFormNew();
					customerListForm.panel.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");
				}
			}
			
		});
	}	
			
			
//			private void buttonOnClick() {
//				btnSave.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						
//						 Book book=new Book();
//		                   
//		                    toSaveBookDataFromForm(book);
//		                    
//		                    if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null && book.getPublisher() != null) {
//
//		                        bookService.saveBooks(book);
//		                        clearForm();
////		                        loadAllBooks(Optional.empty());
//		                    } else {
//		                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
//		                    }		              
//		            }
//				});
//				
//				btnUpdate.addActionListener(new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						if (book != null && book.getId() != null) {
//
//			                   toSaveBookDataFromForm(book);
//			                    
//			                    if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null && book.getPublisher() != null) {
//
//			                    	bookService.updateBooks(book.getId(), book);
//			                        clearForm();
////			                        loadAllBooks(Optional.empty());
//			                        book = null;
//			                    } else {
//			                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
//			                    }
//			                }
//					}
//					
//				});
//				
//				btnCancel.addActionListener(new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						clearForm();
//					}
//					
//				});
//				
//				btnDelete.addActionListener(new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						
//						if(book != null) {
//							System.out.println("delete get book id"+book.getId());
//							
////							pubService.deletePublisher(publisher.getId());
////							clearForm();
////							loadAllPublishers(Optional.empty());
////							publisher=null;
//						} else {
//							JOptionPane.showMessageDialog(null, "Choose Publisher!");
//						}
//						
//					}
//					
//				});
//				
////				lblAddPhoto.addActionListener(new ActionListener() {
////
////					@Override
////					public void actionPerformed(ActionEvent e) {
////						// TODO Auto-generated method stub
////						
////					}
////					
////				});
//			}
			
//			private void loadAllBooks(Optional<List<Book>> optionalBook) {
//				this.dtm = (DefaultTableModel) this.tblBooks.getModel();
//				this.dtm.getDataVector().removeAllElements();
//				this.dtm.fireTableDataChanged();
//				
//				this.originalBookList = this.bookService.findAllBooks();
//				List<Book> bookList= optionalBook.orElseGet(() -> originalBookList);
//				bookList.forEach(e -> {
//					Object[] row=new Object[7];
//					 row[0] = e.getId();
//				      row[1] = e.getName();
//				      row[2] = e.getPrice();
//				      row[4] = e.getStockamount();
//				      row[5] = e.getShelf_number();
//				      row[6] = e.getPhoto();
//					dtm.addRow(row);
//				});
//				this.tblBooks.setModel(dtm);
//			}
	

}
