package forms;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Publisher;
import services.AuthorService;
import services.BookService;
import services.CategoryService;
import services.PublisherService;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.Rectangle;
import java.awt.Label;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
public class BookForm extends JPanel {

	/**
	 * Create the panel.
	 */
			private JPanel contentPane;
			private JTextField txtBookName;
			private JTextField txtShelfNo;
			private JTextField txtRemark;
			private JTable tblBooks;
			private JLabel lblAddPhoto;
			JButton btnSave,btnCancel, btnUpdate, btnDelete;
		    private DefaultTableModel dtm = new DefaultTableModel();
		    JComboBox<String> cboAuthor,cboCategory,cboPublisher;


		    private List<Category> categoryList;
		    private List<Author> authorList;
		    private List<Publisher> publisherList;
		    
		    private CategoryService categoryService;
		    private AuthorService authorService;
		    private PublisherService publisherService;
		    private BookService bookService;
		    private Book book;
		    
			private List<Book> originalBookList = new ArrayList<>();

			/**
			 * Launch the application.
			 */
			public static void main(String[] args) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							BookForm frame = new BookForm();
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
			public BookForm() {
				initialize();
//				setTableDesign();
				initializeDependency();
				this.loadAuthorForComboBox();
		        this.loadCategoryForComboBox();
		        this.loadPublisherForComboBox();
//		        loadAllBooks(Optional.empty());
			}
			
			private void initialize() {

				//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setBounds(100, 100, 723, 484);
				setBorder(new EmptyBorder(5, 5, 5, 5));
				//setContentPane(contentPane);
				setLayout(null);
				
				JPanel panel = new JPanel();
				panel.setBounds(361, 0, 362, 484);
				add(panel);
				panel.setLayout(null);
				
				JLabel lblAuthorId = new JLabel("Aurthor Id");
				lblAuthorId.setForeground(Color.DARK_GRAY);
				lblAuthorId.setFont(new Font("Tahoma", Font.BOLD, 13));
				lblAuthorId.setBounds(12, 23, 80, 30);
				panel.add(lblAuthorId);
				
				txtBookName = new JTextField();
				txtBookName.setFont(new Font("Tahoma", Font.PLAIN, 14));
				txtBookName.setBounds(12, 239, 258, 25);
//				txtBookName.setBorder(roundedBorder);
				panel.add(txtBookName);
				txtBookName.setColumns(10);
				
				txtShelfNo = new JTextField();
				txtShelfNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
				txtShelfNo.setColumns(10);
				txtShelfNo.setBounds(12, 301, 258, 25);
				panel.add(txtShelfNo);
				
				txtRemark = new JTextField();
				txtRemark.setFont(new Font("Tahoma", Font.PLAIN, 14));
				txtRemark.setColumns(10);
				txtRemark.setBounds(12, 361, 258, 25);
				panel.add(txtRemark);
				
				btnCancel = new JButton("Cancel");
				btnCancel.setBackground(Color.WHITE);
				btnCancel.setBorderPainted(false);
				btnCancel.setForeground(Color.DARK_GRAY);
				btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
				btnCancel.setBounds(154, 418, 91, 25);
				panel.add(btnCancel);
				
				JPanel panelItemList = new JPanel();
				panelItemList.setBounds(267, 0, 428, 445);
//				add(panelItemList);
				panelItemList.setLayout(null);
				
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setBounds(0, 0, 428, 445);
			    panelItemList.add(scrollPane);
			    
			    tblBooks = new JTable();
			    scrollPane.setViewportView(tblBooks);

			    tblBooks.setFont(new Font("Tahoma", Font.PLAIN, 13));
			    tblBooks.setBackground(new Color(255, 250, 240));
			    tblBooks.setForeground(Color.DARK_GRAY);
			    tblBooks.setBounds(150, 251, 555, -184);
		        
		        cboCategory = new JComboBox<String>();
		        cboCategory.setFont(new Font("Tahoma", Font.PLAIN, 14));
		        cboCategory.setBounds(12, 113, 258, 25);
		        panel.add(cboCategory);
		        
		        cboPublisher = new JComboBox<String>();
		        cboPublisher.setFont(new Font("Tahoma", Font.PLAIN, 14));
		        cboPublisher.setBounds(12, 175, 258, 25);
		        panel.add(cboPublisher);
		        
		        cboAuthor = new JComboBox<String>();
		        cboAuthor.setBackground(SystemColor.textHighlightText);
		        cboAuthor.setFont(new Font("Tahoma", Font.PLAIN, 13));
		        cboAuthor.setBounds(12, 51, 258, 25);
		        panel.add(cboAuthor);
		        
		        btnDelete = new JButton("Delete");
		        btnDelete.setBackground(Color.WHITE);
		        btnDelete.setBorderPainted(false);
		        btnDelete.setForeground(Color.DARK_GRAY);
		        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		        btnDelete.setBounds(154, 418, 91, 25);
		        panel.add(btnDelete);
		        
		        JLabel lblCategoryId = new JLabel("Category Id");
		        lblCategoryId.setForeground(Color.DARK_GRAY);
		        lblCategoryId.setBounds(12, 86, 104, 30);
		        panel.add(lblCategoryId);
		        lblCategoryId.setFont(new Font("Tahoma", Font.BOLD, 13));
		        
//				Border roundedBorder=new LineBorder(Color.DARK_GRAY,1,true);
		        
		        JLabel lblPublisherId = new JLabel("Publisher Id");
		        lblPublisherId.setForeground(Color.DARK_GRAY);
		        lblPublisherId.setBounds(12, 148, 104, 30);
		        panel.add(lblPublisherId);
		        lblPublisherId.setFont(new Font("Tahoma", Font.BOLD, 13));
		        
		        JLabel lblBookName = new JLabel("Book Name");
		        lblBookName.setForeground(Color.DARK_GRAY);
		        lblBookName.setBounds(12, 210, 80, 30);
		        panel.add(lblBookName);
		        lblBookName.setFont(new Font("Tahoma", Font.BOLD, 13));
		        
		        JLabel lblShelfNo = new JLabel("Shelf No");
		        lblShelfNo.setForeground(Color.DARK_GRAY);
		        lblShelfNo.setBounds(12, 274, 80, 30);
		        panel.add(lblShelfNo);
		        lblShelfNo.setFont(new Font("Tahoma", Font.BOLD, 13));
		        
		        JLabel lblRemark = new JLabel("Remark");
		        lblRemark.setBounds(12, 336, 80, 30);
		        panel.add(lblRemark);
		        lblRemark.setForeground(Color.DARK_GRAY);
		        lblRemark.setFont(new Font("Tahoma", Font.BOLD, 13));
		        
		        btnUpdate = new JButton("Update");
		        btnUpdate.setBounds(51, 418, 91, 25);
		        panel.add(btnUpdate);
		        btnUpdate.setBorderPainted(false);
		        btnUpdate.setForeground(Color.WHITE);
		        btnUpdate.setBackground(new Color(153, 51, 204));
		        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		        
		        btnSave = new JButton("Save");
		        btnSave.setBounds(51, 418, 91, 25);
		        panel.add(btnSave);
		        btnSave.setBorderPainted(false);
		        btnSave.setBorder(new LineBorder(new Color(0, 0, 0)));
		        btnSave.setBackground(new Color(153, 51, 204));
		        btnSave.setForeground(Color.WHITE);
		        btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		        
		        JPanel photoPanel = new JPanel();
		        photoPanel.setBounds(0, 0, 362, 484);
		        add(photoPanel);
		        photoPanel.setLayout(null);
		        
		        lblAddPhoto = new JLabel("");
		        lblAddPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		        lblAddPhoto.setForeground(new Color(153, 0, 255));
		        lblAddPhoto.setFont(new Font("Tahoma", Font.BOLD, 13));
		        lblAddPhoto.setBackground(new Color(211, 211, 211));
		        lblAddPhoto.setOpaque(true);
		        lblAddPhoto.setBounds(59, 155, 180, 183);
		        lblAddPhoto.setBorder(BorderFactory.createDashedBorder(new Color(128, 0, 128),5,5));
		        
		        Image img=new ImageIcon(this.getClass().getResource("/upload-20.png")).getImage();
				lblAddPhoto.setIcon(new ImageIcon(img));
				lblAddPhoto.setText("Choose Book Photos");
				lblAddPhoto.setAlignmentX(CENTER_ALIGNMENT);
				lblAddPhoto.setAlignmentY(CENTER_ALIGNMENT);
		        photoPanel.add(lblAddPhoto);
		        
		        JLabel lblTitle = new JLabel("Add New Book");
		        lblTitle.setForeground(Color.DARK_GRAY);
		        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		        lblTitle.setBounds(24, 30, 235, 28);
		        photoPanel.add(lblTitle);
		        
		        JLabel lblLine = new JLabel("Add some information for the book you want \n to create");
		        lblLine.setFont(new Font("Tahoma", Font.PLAIN, 13));
		        lblLine.setBounds(24, 68, 324, 30);
		        photoPanel.add(lblLine);

		        buttonOnClick();
			}
			
			private void initializeDependency() {
		        this.publisherService= new PublisherService();
		        this.authorService= new AuthorService();
		        this.categoryService = new CategoryService();
		        this.bookService= new BookService();
		    }
			
			private void clearForm() {
				cboAuthor.setSelectedIndex(0);
				cboCategory.setSelectedIndex(0);
				cboPublisher.setSelectedIndex(0);
				txtBookName.setText("");
				txtRemark.setText("");
				txtShelfNo.setText("");
			}
			
			private void loadCategoryForComboBox() {
		        cboCategory.addItem("- Select -");
		        System.out.println("Cate count "+categoryService.findAllCategories().size());
		        this.categoryList = this.categoryService.findAllCategories();
		        this.categoryList.forEach(c -> cboCategory.addItem(c.getName()));
		    }
			
			private void loadAuthorForComboBox() {
		        cboAuthor.addItem("- Select -");
		        System.out.println("author count "+authorService.findAllAuthors().size());

		        this.authorList = this.authorService.findAllAuthors();
		        this.authorList.forEach(a -> cboAuthor.addItem(a.getName()));
			}
			
			private void loadPublisherForComboBox() {
		        cboPublisher.addItem("- Select -");
		        System.out.println("pub count "+publisherService.findAllPublishers().size());

		        this.publisherList = this.publisherService.findAllPublishers();
		        this.publisherList.forEach(p -> cboPublisher.addItem(p.getName()));
			}

//			private void setTableDesign() {
//		        dtm.addColumn("ID");
//		        dtm.addColumn("Name");
//		        dtm.addColumn("Price");
//		        dtm.addColumn("Quantity");
//		        dtm.addColumn("Shelf No");
//		        dtm.addColumn("Remark");
//		        dtm.addColumn("Photo");
//		        
//		        this.tblBooks.setModel(new DefaultTableModel(
//		        	new Object[][] {
//		        	},
//		        	new String[] {
//		        		"ID", "Name", "Price", "Quantity", "Shelf No","Remark","Photo"
//		        	}
//		        ));
//		               
//				}
			
			private void buttonOnClick() {
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						 Book book=new Book();
		                   
		                    toSaveBookDataFromForm(book);
		                    
		                    if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null && book.getPublisher() != null) {

		                        bookService.saveBooks(book);
		                        clearForm();
//		                        loadAllBooks(Optional.empty());
		                    } else {
		                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
		                    }		              
		            }
				});
				
				btnUpdate.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (book != null && book.getId() != null) {

			                   toSaveBookDataFromForm(book);
			                    
			                    if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null && book.getPublisher() != null) {

			                    	bookService.updateBooks(book.getId(), book);
			                        clearForm();
//			                        loadAllBooks(Optional.empty());
			                        book = null;
			                    } else {
			                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
			                    }
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
				
				btnDelete.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(book != null) {
							System.out.println("delete get book id"+book.getId());
							
//							pubService.deletePublisher(publisher.getId());
//							clearForm();
//							loadAllPublishers(Optional.empty());
//							publisher=null;
						} else {
							JOptionPane.showMessageDialog(null, "Choose Publisher!");
						}
						
					}
					
				});
				
//				lblAddPhoto.addActionListener(new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//				});
			}
			
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
				
			  private void toSaveBookDataFromForm(Book book) {
				  book.setName(txtBookName.getText());
//				  book.setPrice(Integer.parseInt(txtPrice.getText().isBlank() ? "0" : txtPrice.getText()));
				  book.setRemark(txtRemark.getText());
				  book.setShelf_number(Integer.parseInt(txtShelfNo.getText().isBlank() ? "0" : txtShelfNo.getText()));
				  book.setStockamount(100);
				  
			      Optional<Category> selectedCategory = categoryList.stream()
			                .filter(c -> c.getName().equals(cboCategory.getSelectedItem())).findFirst();
			      book.setCategory(selectedCategory.orElse(null));
			      
			      Optional<Author> selectedAuthor = authorList.stream()
			                .filter(a -> a.getName().equals(cboAuthor.getSelectedItem())).findFirst();
			      book.setAuthor(selectedAuthor.orElse(null));
			      
			      Optional<Publisher> selectedPublisher= publisherList.stream()
			    		  .filter(p -> p.getName().equals(cboPublisher.getSelectedItem())).findFirst();
			      book.setPublisher(selectedPublisher.orElse(null));
			    		 
			    }
}
