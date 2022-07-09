package forms;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.Rectangle;
public class BookForm extends JPanel {

	/**
	 * Create the panel.
	 */
			private JPanel contentPane;
			private JTextField txtBookName;
			private JTextField txtPrice;
			private JTextField txtShelfNo;
			private JTextField txtRemark;
			private JTable tblBooks;
			JButton btnSave,btnCancel;
		    private DefaultTableModel dtm = new DefaultTableModel();
		    JComboBox<String> cboAuthor,cboCategory,cboPublisher;
		    private JTextField txtPhoto;


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
				setTableDesign();
				initializeDependency();
				this.loadAuthorForComboBox();
		        this.loadCategoryForComboBox();
		        this.loadPublisherForComboBox();
		        loadAllBooks(Optional.empty());
			}
			
			private void initialize() {

				//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setBounds(100, 100, 723, 484);
				setBorder(new EmptyBorder(5, 5, 5, 5));
				//setContentPane(contentPane);
				setLayout(null);
				
				JPanel panel = new JPanel();
				panel.setBounds(0, 0, 255, 445);
				add(panel);
				panel.setLayout(null);
				
				JLabel lblAuthorId = new JLabel("Aurthor Id");
				lblAuthorId.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblAuthorId.setBounds(12, 23, 80, 30);
				panel.add(lblAuthorId);
				
				JLabel lblCategoryId = new JLabel("Category Id");
				lblCategoryId.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblCategoryId.setBounds(12, 63, 104, 30);
				panel.add(lblCategoryId);
				
				JLabel lblBookId = new JLabel("Book Id");
				lblBookId.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblBookId.setBounds(12, 308, 80, 30);
				panel.add(lblBookId);
				
				JLabel lblBookName = new JLabel("Book Name");
				lblBookName.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblBookName.setBounds(12, 143, 80, 30);
				panel.add(lblBookName);
				
				JLabel lblPrice = new JLabel("Price");
				lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblPrice.setBounds(12, 186, 80, 30);
				panel.add(lblPrice);
				
				JLabel lblShelfNo = new JLabel("Shelf No");
				lblShelfNo.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblShelfNo.setBounds(12, 226, 80, 30);
				panel.add(lblShelfNo);
				
				JLabel lblRemark = new JLabel("Remark");
				lblRemark.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblRemark.setBounds(12, 266, 80, 30);
				panel.add(lblRemark);
				
				JLabel lblPublisherId = new JLabel("Publisher Id");
				lblPublisherId.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblPublisherId.setBounds(12, 103, 104, 30);
				panel.add(lblPublisherId);
				
				txtBookName = new JTextField();
				txtBookName.setFont(new Font("Tahoma", Font.PLAIN, 14));
				txtBookName.setBounds(128, 150, 104, 25);
				panel.add(txtBookName);
				txtBookName.setColumns(10);
				
				txtPrice = new JTextField();
				txtPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
				txtPrice.setColumns(10);
				txtPrice.setBounds(128, 193, 104, 25);
				panel.add(txtPrice);
				
				txtShelfNo = new JTextField();
				txtShelfNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
				txtShelfNo.setColumns(10);
				txtShelfNo.setBounds(128, 226, 104, 25);
				panel.add(txtShelfNo);
				
				txtRemark = new JTextField();
				txtRemark.setFont(new Font("Tahoma", Font.PLAIN, 14));
				txtRemark.setColumns(10);
				txtRemark.setBounds(128, 266, 104, 25);
				panel.add(txtRemark);
				
				btnSave = new JButton("Save");
				btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
				btnSave.setBounds(23, 365, 91, 25);
				panel.add(btnSave);
				
				btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
				btnCancel.setBounds(121, 365, 91, 25);
				panel.add(btnCancel);
				
				JPanel panelItemList = new JPanel();
				panelItemList.setBounds(267, 0, 428, 445);
				add(panelItemList);
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
		        cboCategory.setBounds(128, 63, 115, 25);
		        panel.add(cboCategory);
		        
		        cboPublisher = new JComboBox<String>();
		        cboPublisher.setFont(new Font("Tahoma", Font.PLAIN, 14));
		        cboPublisher.setBounds(128, 104, 115, 25);
		        panel.add(cboPublisher);
		        
		        txtPhoto = new JTextField();
		        txtPhoto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		        txtPhoto.setColumns(10);
		        txtPhoto.setBounds(128, 308, 104, 25);
		        panel.add(txtPhoto);
		        
		        cboAuthor = new JComboBox<String>();
		        cboAuthor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		        cboAuthor.setBounds(128, 29, 115, 25);
		        panel.add(cboAuthor);

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
				txtPrice.setText("");
				txtRemark.setText("");
				txtShelfNo.setText("");
				txtPhoto.setText("");
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

			private void setTableDesign() {
		        dtm.addColumn("ID");
		        dtm.addColumn("Name");
		        dtm.addColumn("Price");
		        dtm.addColumn("Quantity");
		        dtm.addColumn("Shelf No");
		        dtm.addColumn("Remark");
		        dtm.addColumn("Photo");
		        
		        this.tblBooks.setModel(new DefaultTableModel(
		        	new Object[][] {
		        	},
		        	new String[] {
		        		"ID", "Name", "Price", "Quantity", "Shelf No","Remark","Photo"
		        	}
		        ));
		               
				}
			
			private void buttonOnClick() {
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

		                if (book != null && book.getId() != null) {

		                   toSaveBookDataFromForm(book);
		                    
		                    if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null && book.getPublisher() != null) {

		                    	bookService.updateBooks(book.getId(), book);
		                        clearForm();
		                        loadAllBooks(Optional.empty());
		                        book = null;
		                    } else {
		                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
		                    }
		                } else {
		                    Book book=new Book();
		                   
		                    toSaveBookDataFromForm(book);
		                    
		                    if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null && book.getPublisher() != null) {

		                        bookService.saveBooks(book);
		                        clearForm();
		                        loadAllBooks(Optional.empty());
		                    } else {
		                        JOptionPane.showMessageDialog(null, "Enter Required Field!");
		                    }
		                    
		                }
		            }
				});
			}
			
			private void loadAllBooks(Optional<List<Book>> optionalBook) {
				this.dtm = (DefaultTableModel) this.tblBooks.getModel();
				this.dtm.getDataVector().removeAllElements();
				this.dtm.fireTableDataChanged();
				
				this.originalBookList = this.bookService.findAllBooks();
				List<Book> bookList= optionalBook.orElseGet(() -> originalBookList);
				bookList.forEach(e -> {
					Object[] row=new Object[7];
					 row[0] = e.getId();
				      row[1] = e.getName();
				      row[2] = e.getPrice();
				      row[4] = e.getStockamount();
				      row[5] = e.getShelf_number();
				      row[6] = e.getPhoto();
					dtm.addRow(row);
				});
				this.tblBooks.setModel(dtm);
			}
				
			  private void toSaveBookDataFromForm(Book book) {
				  book.setName(txtBookName.getText());
				  book.setPrice(Integer.parseInt(txtPrice.getText().isBlank() ? "0" : txtPrice.getText()));
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
