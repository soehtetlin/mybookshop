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
import javax.swing.JFileChooser;
import javax.swing.JTable;
import java.awt.Rectangle;
import java.awt.Label;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

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
	JButton btnSave, btnCancel, btnUpdate, btnDelete;
	private DefaultTableModel dtm = new DefaultTableModel();
	JComboBox<String> cboAuthor, cboCategory, cboPublisher;

	private List<Category> categoryList;
	private List<Author> authorList;
	private List<Publisher> publisherList;

	private CategoryService categoryService;
	private AuthorService authorService;
	private PublisherService publisherService;
	private BookService bookService;
	private Book book;
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();

	private List<Book> originalBookList = new ArrayList<>();

	/**
	 * Launch the application.
	 */
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

		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 800, 450);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel panel = new JPanel();

		JLabel lblAuthorId = new JLabel("Aurthor Id");
		cLayout.setLabel(lblAuthorId);

		txtBookName = new JTextField();
		txtBookName.setHorizontalAlignment(SwingConstants.CENTER);
		txtBookName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtBookName.setColumns(10);

		txtShelfNo = new JTextField();
		txtShelfNo.setHorizontalAlignment(SwingConstants.CENTER);
		txtShelfNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtShelfNo.setColumns(10);

		txtRemark = new JTextField();
		txtRemark.setHorizontalAlignment(SwingConstants.CENTER);
		txtRemark.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtRemark.setColumns(10);

		btnCancel = new JButton("Cancel");
		cLayout.setButton(btnCancel);

		JPanel panelItemList = new JPanel();
		// panelItemList.setBounds(267, 0, 428, 445);
//				add(panelItemList);
		panelItemList.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		// scrollPane.setBounds(0, 0, 428, 445);
		panelItemList.add(scrollPane);

		tblBooks = new JTable();
		scrollPane.setViewportView(tblBooks);

		tblBooks.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tblBooks.setBackground(new Color(255, 250, 240));
		tblBooks.setForeground(Color.DARK_GRAY);
		// tblBooks.setBounds(150, 251, 555, -184);

		cboCategory = new JComboBox<String>();

		cLayout.setComboBox(cboCategory);

		cboPublisher = new JComboBox<String>();
		// cboPublisher.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cLayout.setComboBox(cboPublisher);

		cboAuthor = new JComboBox<String>();
		lblAuthorId.setLabelFor(cboAuthor);
		cLayout.setComboBox(cboAuthor);

		btnDelete = new JButton("Delete");
		cLayout.setButton(btnDelete);

		JLabel lblCategoryId = new JLabel("Category Id");
		cLayout.setLabel(lblCategoryId);
//				Border roundedBorder=new LineBorder(Color.DARK_GRAY,1,true);

		JLabel lblPublisherId = new JLabel("Publisher Id");
		cLayout.setLabel(lblPublisherId);

		JLabel lblBookName = new JLabel("Book Name");
		cLayout.setLabel(lblBookName);

		JLabel lblShelfNo = new JLabel("Shelf No");
		cLayout.setLabel(lblShelfNo);

		JLabel lblRemark = new JLabel("Remark");
		cLayout.setLabel(lblRemark);

		btnUpdate = new JButton("Update");
		cLayout.setButton(btnUpdate);

		btnSave = new JButton("Save");
		cLayout.setButton(btnSave);

		JPanel photoPanel = new JPanel();

		lblAddPhoto = new JLabel("");
		lblAddPhoto.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddPhoto.setForeground(new Color(153, 0, 255));
		lblAddPhoto.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAddPhoto.setBackground(new Color(211, 211, 211));
		lblAddPhoto.setOpaque(true);
		lblAddPhoto.setBorder(BorderFactory.createDashedBorder(new Color(128, 0, 128), 5, 5));

		Image img = new ImageIcon(this.getClass().getResource("/upload-20.png")).getImage();
		lblAddPhoto.setIcon(new ImageIcon(img));
		lblAddPhoto.setText("Choose Book Photos");
		lblAddPhoto.setAlignmentX(CENTER_ALIGNMENT);
		lblAddPhoto.setAlignmentY(CENTER_ALIGNMENT);

		JLabel lblTitle = new JLabel("Add New Book");
		cLayout.setLabel(lblTitle);

		JLabel lblLine = new JLabel("Add some information for the book you want \n to create");
		cLayout.setLabel(lblLine);

		GroupLayout groupLayout = new GroupLayout(this);
//		groupLayout.setAutoCreateContainerGaps(true);
//		groupLayout.setAutoCreateGaps(true);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(photoPanel, GroupLayout.PREFERRED_SIZE, 365, Short.MAX_VALUE).addGap(13)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.CENTER, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
						.addComponent(photoPanel, Alignment.CENTER, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
				.addGap(0)));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtBookName, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
						.addComponent(cboAuthor, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblBookName, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtShelfNo, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblShelfNo, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnUpdate, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
							.addGap(14)
							.addComponent(btnDelete, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
							.addGap(11)
							.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
						.addComponent(lblRemark, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtRemark, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAuthorId, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCategoryId, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPublisherId, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblAuthorId, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cboAuthor, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblCategoryId, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblPublisherId, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblBookName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(txtBookName, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(30)
							.addComponent(txtShelfNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblShelfNo, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblRemark, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(30)
							.addComponent(txtRemark, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(30)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
						.addComponent(btnDelete, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
						.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
						.addComponent(btnUpdate, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
					.addGap(20))
		);
		gl_panel.setAutoCreateGaps(true);
		gl_panel.setAutoCreateContainerGaps(true);
		panel.setLayout(gl_panel);
		GroupLayout gl_photoPanel = new GroupLayout(photoPanel);
		gl_photoPanel.setHorizontalGroup(gl_photoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(24).addComponent(lblTitle,
						GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(24).addComponent(lblLine,
						GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(93)
						.addComponent(lblAddPhoto, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE).addGap(115)));
		gl_photoPanel.setVerticalGroup(gl_photoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(30)
						.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addGap(10)
						.addComponent(lblLine, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addGap(59)
						.addComponent(lblAddPhoto, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE).addGap(103)));
		photoPanel.setLayout(gl_photoPanel);
		setLayout(groupLayout);

		buttonOnClick();
	}

	private void initializeDependency() {
		this.publisherService = new PublisherService();
		this.authorService = new AuthorService();
		this.categoryService = new CategoryService();
		this.bookService = new BookService();
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
		System.out.println("Cate count " + categoryService.findAllCategories().size());
		this.categoryList = this.categoryService.findAllCategories();
		this.categoryList.forEach(c -> cboCategory.addItem(c.getName()));
	}

	private void loadAuthorForComboBox() {
		cboAuthor.addItem("- Select -");
		System.out.println("author count " + authorService.findAllAuthors().size());

		this.authorList = this.authorService.findAllAuthors();
		this.authorList.forEach(a -> cboAuthor.addItem(a.getName()));
	}

	private void loadPublisherForComboBox() {
		cboPublisher.addItem("- Select -");
		System.out.println("pub count " + publisherService.findAllPublishers().size());

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

				Book book = new Book();

				toSaveBookDataFromForm(book);

				if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null
						&& book.getPublisher() != null) {

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

					if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null
							&& book.getPublisher() != null) {

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

				if (book != null) {
					System.out.println("delete get book id" + book.getId());

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

		Optional<Publisher> selectedPublisher = publisherList.stream()
				.filter(p -> p.getName().equals(cboPublisher.getSelectedItem())).findFirst();
		book.setPublisher(selectedPublisher.orElse(null));

	}
}
