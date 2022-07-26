package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database_config.DBconnector;
import entities.Book;
import entities.Purchase;
import entities.PurchaseDetails;
import repositories.BookRepo;
import repositories.PurchaseRepo;
import shared.exception.AppException;
import shared.mapper.BookMapper;
import shared.mapper.GeneratePrimaryKey;
import shared.mapper.PurchaseMapper;

public class BookService implements BookRepo, PurchaseRepo {

	private final DBconnector dbConfig = new DBconnector();
	private final BookMapper bookMapper = new BookMapper();

	private final PurchaseMapper purchaseMapper = new PurchaseMapper();
	private GeneratePrimaryKey genPrimaryKey = new GeneratePrimaryKey();

	public void saveBooks(Book book) {
		try {
			AuthorService authService = new AuthorService();

			book.setId(genPrimaryKey.generateID("id", "Book", "BK"));

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"INSERT INTO book (id, photo, name, author_id, category_id, publisher_id, price, sale_price, "
							+ "stockamount, shelf_number, remark) VALUES (?,?,?,?,?,?,?,?,?,?,?) ;");

			ps.setString(1, book.getId());
			ps.setString(2, book.getPhoto());
			ps.setString(3, book.getName());
			ps.setString(4, book.getAuthor().getId());
			ps.setString(5, book.getCategory().getId());
			ps.setString(6, book.getPublisher().getId());
			ps.setInt(7, book.getPrice());
			ps.setInt(8, book.getSale_price());
			ps.setInt(9, book.getStockamount());
			ps.setInt(10, book.getShelf_number());
			ps.setString(11, book.getRemark());

			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Record Saved Successfully.");
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

	@Override
	public void updateBooks(String id, Book book) {
		try {
			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"UPDATE book SET photo = ? , name = ? , author_id = ? , category_id = ? , publisher_id = ?, price = ?"
							+ ", sale_price = ?, stockamount = ?, shelf_number = ?, remark = ? WHERE id = ?");

			ps.setString(1, book.getPhoto());
			ps.setString(2, book.getName());
			ps.setString(3, book.getAuthor().getId());
			ps.setString(4, book.getCategory().getId());
			ps.setString(5, book.getPublisher().getId());
			ps.setInt(6, book.getPrice());
			ps.setInt(7, book.getSale_price());
			ps.setInt(8, book.getStockamount());
			ps.setInt(9, book.getShelf_number());
			ps.setString(10, book.getRemark());

			ps.setString(11, book.getId());

			ps.execute();

			ps.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		}
	}

	@Override
	public List<Book> findAllBooks() {
		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id  where book.price > 0 and book.stockamount > 0 ORDER BY book.id DESC;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Book book = new Book();

				bookList.add(this.bookMapper.mapToProduct(book, rs));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		}
		return bookList;
	}

	public List<Book> findAllBooksforList() {
		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id ORDER BY book.id DESC;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Book book = new Book();

				bookList.add(this.bookMapper.mapToProduct(book, rs));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "You cannot delete this publisher");

		}
		return bookList;
	}

	@Override
	public Book findById(String bookID) {
		Book book = new Book();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id\n" + "WHERE book.id = '" + bookID + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				book = this.bookMapper.mapToProduct(book, rs);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		return book;
	}

	public Book findByName(String name) {
		Book book = new Book();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id\n" + "WHERE book.name = '" + name + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				book = this.bookMapper.mapToProduct(book, rs);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		return book;
	}

	@Override
	public List<Book> findBookByCategoryID(String categoryId) {

		return null;
	}

	public List<Book> findBookByCategoryName(String categoryName) {

		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id where category.name='" + categoryName
					+ "' and book.price > 0 and book.stockamount > 0 ORDER BY book.id DESC ;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Book book = new Book();

				bookList.add(this.bookMapper.mapToProduct(book, rs));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		}
		return bookList;
	}

	public List<Book> findBookByAuthorName(String authorname) {

		AuthorService authService = new AuthorService();
		String id = authService.findByName(authorname);

		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id where author_id='" + id + "' ORDER BY book.id DESC ;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Book book = new Book();

				bookList.add(this.bookMapper.mapToProduct(book, rs));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		}

		return bookList;

	}

	@Override
	public List<Book> findBookByPublisherID(String publisherId) {

		return null;
	}

	@Override
	public void createBooks(Book book) {

	}

	@Override
	public void deletBooks(String id) {

		try {

			if (findPurchaseDetailsListByProductId(id).size() > 0) {

				throw new AppException("This book cannot be deleted");
			}

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement("DELETE FROM book where id = ?");

			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You cannot delete this publisher");
		}

	}

	@Override
	public void createPurchase(Purchase purchase) {

	}

	@Override
	public void createPurchaseDetails(List<PurchaseDetails> purchaseDetailsList) {

	}

	@Override
	public Purchase findPurchaseById(String id) {

		return null;
	}

	@Override
	public List<Purchase> findAllPurchases() {

		return null;
	}

	@Override
	public List<Purchase> findPurchaseListBySupplierId(String supplierId) {

		return null;
	}

	@Override
	public List<Purchase> findPurchaseListByEmployeeId(String employeeId) {

		return null;
	}

	@Override
	public List<PurchaseDetails> findPurchaseDetailsListByProductId(String bookId) {
		List<PurchaseDetails> purchaseDetailList = new ArrayList<>();
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase_detail WHERE book_id = " + bookId + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {

				PurchaseDetails purchaseDetails = new PurchaseDetails();
				purchaseDetailList.add(this.purchaseMapper.mapToPurchaseDetails(purchaseDetails, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseDetailList;
	}

	@Override
	public List<PurchaseDetails> findAllPurchaseDetailsByPurchaseId(String purchaseId) {

		return null;
	}

	public int CountBook() {
		int bookcount = 0;

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select count(*) from book;";

			ResultSet rs = st.executeQuery(query);
			rs.next();
			bookcount = rs.getInt("count(*)");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookcount;
	}

}
