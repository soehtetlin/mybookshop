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
import entities.Author;
import entities.Book;
import entities.Category;
import repositories.*;
import entities.Publisher;
import entities.Purchase;
import entities.PurchaseDetails;
import shared.exception.AppException;
import shared.mapper.*;

public class BookService implements BookRepo, PurchaseRepo {

	private final DBconnector dbConfig = new DBconnector();
	private final BookMapper bookMapper = new BookMapper();
	private final PurchaseMapper purchaseMapper = new PurchaseMapper();
	private GeneratePrimaryKey genPrimaryKey= new GeneratePrimaryKey();

//	private PurchaseService purchaseService= new PurchaseService();

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

	public List<Book> findAllBooks() {
		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

//			String query = "SELECT * FROM book";

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

//			String query = "SELECT * FROM book";

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id ORDER BY book.id DESC;";

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

	@Override
	public List<Book> findBookByCategoryID(String categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Book> findBookByCategoryName(String categoryId) {

		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

//				String query = "SELECT * FROM book";

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id where category_id='" + categoryId
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

	@Override
	public List<Book> findBookByPublisherID(String publisherId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createBooks(Book book) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletBooks(String id) {
		// TODO Auto-generated method stub
		try {

			if (findPurchaseDetailsListByProductId(id).size() > 0) {

				throw new AppException("this book cannot be deleted");
			}

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement("DELETE FROM book where id = ?");

			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@Override
	public void createPurchase(Purchase purchase) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPurchaseDetails(List<PurchaseDetails> purchaseDetailsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public Purchase findPurchaseById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Purchase> findAllPurchases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Purchase> findPurchaseListBySupplierId(String supplierId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Purchase> findPurchaseListByEmployeeId(String employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PurchaseDetails> findPurchaseDetailsListByProductId(String bookId) {
		List<PurchaseDetails> purchaseDetailList = new ArrayList<>();
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase_detial WHERE book_id = " + bookId + ";";

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
		// TODO Auto-generated method stub
		return null;
	}

//	
//	   public Purchase findPurchaseById(String id) {
//	        Purchase purchase = new Purchase();
//
//	        try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//	            String query = "SELECT * FROM purchase\n" +
//	                    "INNER JOIN employee\n" +
//	                    "on employee.emp_id = purchase.employee_id\n" +
//	                    "INNER JOIN supplier\n" +
//	                    "ON supplier.sup_id = purchase.supplier_id\n" +
//	                    "WHERE purchase_id=" + id + ";";
//
//	            ResultSet rs = st.executeQuery(query);
//
//	            while (rs.next()) {
////	                this.purchaseMapper.mapToPurchase(purchase, rs);
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//
//	        return purchase;
//	    }

}
