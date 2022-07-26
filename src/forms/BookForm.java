package forms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Publisher;
import services.AuthorService;
import services.BookService;
import services.CategoryService;
import services.PublisherService;
import shared.checker.Checking;

public class BookForm extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Create the panel.
	 */
	private JTextField txtBookName;
	private JTextField txtShelfNo;
	private JTextField txtRemark;
	private JTable tblBooks;
	private JLabel lblAddPhoto;
	JButton btnSave, btnCancel, btnUpdate, btnDelete;
	JComboBox<String> cboAuthor, cboCategory, cboPublisher;

	private List<Category> categoryList;
	private List<Author> authorList;
	private List<Publisher> publisherList;

	private CategoryService categoryService;
	private AuthorService authorService;
	private PublisherService publisherService;
	private BookService bookService;
	private Book book = new Book();
	private CreateLayoutProperties cLayout = new CreateLayoutProperties();
	String path = "C:\\KMD\\Eclipse Git Clone\\BookShop\\img\\DCR.png", storPath = null;

	String filename = null;
	File destinationFile = null;

	private JpanelLoader jLoader = new JpanelLoader();
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	/**
	 * Create the frame.
	 */
	public BookForm() {
		setBackground(Color.WHITE);
		initialize();
		initializeDependency();
		this.loadAuthorForComboBox();
		this.loadCategoryForComboBox();
		this.loadPublisherForComboBox();
	}

	public BookForm(Book book) {
		this.book = book;
		initialize();
		initializeDependency();
		this.loadAuthorForComboBox();
		this.loadCategoryForComboBox();
		this.loadPublisherForComboBox();
		this.setData();

	}

	private void setData() {

		cboAuthor.setSelectedItem(book.getAuthor().getName());
		System.out.println("CBO Author Name " + book.getAuthor().getName());
		cboCategory.setSelectedItem(book.getCategory().getName());
		cboPublisher.setSelectedItem(book.getPublisher().getName());
		txtBookName.setText(book.getName());
		txtShelfNo.setText(String.valueOf(book.getShelf_number()));
		txtRemark.setText(book.getRemark());

		if (!book.getPhoto().isEmpty()) {
			lblAddPhoto.setText("");
			ImageIcon imageIcon = new ImageIcon(new ImageIcon(book.getPhoto().toString()).getImage()
					.getScaledInstance(171, 169, Image.SCALE_SMOOTH));
			lblAddPhoto.setIcon(imageIcon);

		} else if (book.getPhoto().isEmpty() || book.getPhoto() == null) {

			lblAddPhoto.setOpaque(true);
			ImageIcon imageIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("/defautcover.jpg"))
					.getImage().getScaledInstance(171, 169, Image.SCALE_DEFAULT));
			lblAddPhoto.setIcon(imageIcon);

		}

	}

	private void initialize() {

		setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);

		JLabel lblAuthorId = new JLabel("Aurthor Id");
		lblAuthorId.setVerticalAlignment(SwingConstants.TOP);
		cLayout.setLabel(lblAuthorId);

		txtBookName = new JTextField();
		txtBookName.setHorizontalAlignment(SwingConstants.LEFT);
		txtBookName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtBookName.setColumns(10);

		txtShelfNo = new JTextField();
		txtShelfNo.setHorizontalAlignment(SwingConstants.LEFT);
		txtShelfNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtShelfNo.setColumns(10);

		txtRemark = new JTextField();
		txtRemark.setHorizontalAlignment(SwingConstants.LEFT);
		txtRemark.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtRemark.setColumns(10);

		btnCancel = new JButton("Cancel");
		buttonGroup.add(btnCancel);
		cLayout.setButton(btnCancel);

		JPanel panelItemList = new JPanel();
		panelItemList.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		panelItemList.add(scrollPane);

		tblBooks = new JTable();
		scrollPane.setViewportView(tblBooks);

		tblBooks.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tblBooks.setBackground(new Color(255, 250, 240));
		tblBooks.setForeground(Color.DARK_GRAY);
		cboCategory = new JComboBox<String>();

		cLayout.setComboBox(cboCategory);

		cboPublisher = new JComboBox<String>();
		cLayout.setComboBox(cboPublisher);

		cboAuthor = new JComboBox<String>();
		lblAuthorId.setLabelFor(cboAuthor);
		cLayout.setComboBox(cboAuthor);

		btnDelete = new JButton("Delete");
		buttonGroup.add(btnDelete);
		btnDelete.setVisible(false);
		cLayout.setButton(btnDelete);

		JLabel lblCategoryId = new JLabel("Category Id");
		cLayout.setLabel(lblCategoryId);

		JLabel lblPublisherId = new JLabel("Publisher Id");
		cLayout.setLabel(lblPublisherId);

		JLabel lblBookName = new JLabel("Book Name");
		cLayout.setLabel(lblBookName);

		JLabel lblShelfNo = new JLabel("Shelf No");
		cLayout.setLabel(lblShelfNo);

		JLabel lblRemark = new JLabel("Remark");
		cLayout.setLabel(lblRemark);

		btnUpdate = new JButton("Update");
		buttonGroup.add(btnUpdate);
		btnUpdate.setVisible(false);
		cLayout.setButton(btnUpdate);

		btnSave = new JButton("Save");
		buttonGroup.add(btnSave);
		cLayout.setButton(btnSave);
		System.out.println("Book is " + (book != null));

		if (book.getId() != null) {
			btnSave.setVisible(false);
			btnUpdate.setVisible(true);
		}

		JPanel photoPanel = new JPanel();
		photoPanel.setBackground(Color.WHITE);
		lblAddPhoto = new JLabel("");
		lblAddPhoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				JFileChooser jchooser = new JFileChooser("C:\\Users\\User\\Pictures");
				FileNameExtensionFilter fnf = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
				jchooser.addChoosableFileFilter(fnf);
				jchooser.setAcceptAllFileFilterUsed(false);

				int result = jchooser.showDialog(BookForm.this, "Choose Photo");

				if (result == JFileChooser.APPROVE_OPTION) {
					path = jchooser.getSelectedFile().getAbsolutePath();
					System.out.println("Selected file address : " + path);
					filename = jchooser.getSelectedFile().getName();
					System.out.println("Selected File Name : " + filename);

					BufferedImage img = null;
					try {

						destinationFile = new File("resources/" + filename);

						try {
							img = ImageIO.read(new File(path));
							destinationFile.createNewFile();
							ImageIO.write(img, "png", destinationFile);

							JOptionPane.showMessageDialog(null, "Successfully Added Image");
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Failed to added Image");
						}

						lblAddPhoto.setText("");
						lblAddPhoto.getHeight();
						ImageIcon imageIcon = new ImageIcon(
								new ImageIcon(destinationFile.getAbsolutePath()).getImage().getScaledInstance(
										lblAddPhoto.getWidth(), lblAddPhoto.getHeight(), Image.SCALE_SMOOTH));
						lblAddPhoto.setIcon(imageIcon);

					}

					catch (Exception e) {
						clearForm();
					}
				}
			}

		});

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
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.CENTER)
				.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPublisherId, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblShelfNo, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
								.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(19, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup().addComponent(lblCategoryId).addContainerGap(305,
						Short.MAX_VALUE))
				.addGroup(Alignment.LEADING,
						gl_panel.createSequentialGroup()
								.addComponent(lblBookName, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addGap(422))
				.addGroup(gl_panel.createSequentialGroup().addComponent(lblAuthorId).addContainerGap(472,
						Short.MAX_VALUE))
				.addGroup(Alignment.LEADING,
						gl_panel.createSequentialGroup()
								.addComponent(lblRemark, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtRemark, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
						.addComponent(txtShelfNo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
						.addComponent(txtBookName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
						.addComponent(cboPublisher, Alignment.LEADING, 0, 360, Short.MAX_VALUE)
						.addComponent(cboCategory, Alignment.LEADING, 0, 360, Short.MAX_VALUE)
						.addComponent(cboAuthor, Alignment.LEADING, 0, 360, Short.MAX_VALUE)).addGap(162)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.CENTER).addGroup(gl_panel
				.createSequentialGroup()
				.addComponent(lblAuthorId, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(cboAuthor, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(lblCategoryId, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(cboCategory, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(lblPublisherId, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(cboPublisher, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(lblBookName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGap(4).addComponent(txtBookName, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(lblShelfNo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(txtShelfNo, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(lblRemark, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(txtRemark, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addGap(10)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
				.addGap(10)));

		panel.setLayout(gl_panel);

		GroupLayout gl_photoPanel = new GroupLayout(photoPanel);
		gl_photoPanel.setHorizontalGroup(gl_photoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(24)
						.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE).addGap(89))
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(24).addComponent(lblLine,
						GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(93).addComponent(lblAddPhoto)
						.addContainerGap(115, Short.MAX_VALUE)));
		gl_photoPanel.setVerticalGroup(gl_photoPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_photoPanel.createSequentialGroup().addGap(30)
						.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE).addGap(10)
						.addComponent(lblLine, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE).addGap(59)
						.addComponent(lblAddPhoto, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
						.addGap(84)));
		photoPanel.setLayout(gl_photoPanel);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(photoPanel, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE).addGap(9)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(photoPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 422, Short.MAX_VALUE))
				.addGap(11)));
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

	private void buttonOnClick() {
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					toSaveBookDataFromForm();

					if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null
							&& book.getPublisher() != null) {
						if (Checking.IsAllDigit(txtShelfNo.getText())) {

							bookService.saveBooks(book);

							jLoader.jPanelLoader(BookForm.this, new BookListForm());
							clearForm();
						} else {
							JOptionPane.showMessageDialog(null, "Please Enter Number Only!");
						}
					}
				} catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Enter Required Field!");

				} catch (NumberFormatException e2) {

					JOptionPane.showMessageDialog(null, e2.getMessage());
				}

			}

		});

		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (book != null && book.getId() != null) {

					toSaveBookDataFromForm();

					if (!book.getName().isBlank() && book.getAuthor() != null && book.getCategory() != null
							&& book.getPublisher() != null) {

						bookService.updateBooks(book.getId(), book);
						jLoader.jPanelLoader(BookForm.this, new BookListForm());
						clearForm();
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

				clearForm();
				BookListForm booklistform = new BookListForm();
				jLoader.jPanelLoader(BookForm.this, booklistform);
			}

		});

		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (book != null) {

				} else {
					JOptionPane.showMessageDialog(null, "Choose Publisher!");
				}

			}

		});

	}

	private void toSaveBookDataFromForm() {

		if (destinationFile == null) {
			book.setPhoto("resources/defaultcover.jpg");

		} else {
			book.setPhoto(destinationFile.toString());

		}

		book.setName(txtBookName.getText());
		book.setRemark(txtRemark.getText());
		book.setShelf_number(Integer.parseInt(txtShelfNo.getText().isBlank() ? "0" : txtShelfNo.getText()));

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
