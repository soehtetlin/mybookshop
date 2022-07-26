package shared.mapper;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Employee;
import entities.Publisher;
import entities.Purchase;
import entities.PurchaseDetails;
import repositories.BookRepo;
import repositories.PurchaseRepo;

public class PurchaseMapper {

	private BookRepo bookRepo;
	private PurchaseRepo purchaseRepo;

	public void setBookRepo(BookRepo bookRepo) {
		this.bookRepo = bookRepo;
	}

	public void setPurchaseRepo(PurchaseRepo purchaseRepo) {
		this.purchaseRepo = purchaseRepo;
	}

	public Purchase mapToSale(Purchase purchase, ResultSet rs) {
		try {

			purchase.setId(rs.getString("id"));
			purchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchase_date"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Employee employee = new Employee();
			employee.setId(rs.getString("employee.id"));
			employee.setName(rs.getString("employee.name"));
			employee.setAddress(rs.getString("employee.address"));
			employee.setEmail(rs.getString("employee.email"));
			Publisher publisher = new Publisher();
			publisher.setId(rs.getString("publisher.id"));
			publisher.setName(rs.getString("publisher.name"));
			publisher.setContact_no(rs.getString("publisher.contact_no"));
			publisher.setEmail(rs.getString("publisher.email"));
			publisher.setAddress(rs.getString("publisher.email"));
			purchase.setDescription(rs.getString("description"));
			purchase.setPublisher(publisher);
			purchase.setEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchase;
	}

	public Purchase mapPurchaseID(Purchase purchase, ResultSet rs) {
		try {

			purchase.setId(rs.getString("id"));
			purchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchase_date"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Employee employee = new Employee();
			employee.setId(rs.getString("employee_id"));
			Publisher publisher = new Publisher();
			publisher.setId(rs.getString("publisher_id"));
			purchase.setDescription(rs.getString("description"));
			purchase.setPublisher(publisher);
			purchase.setEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchase;
	}

	public PurchaseDetails mapToPurchaseDetails(PurchaseDetails purchaseDetails, ResultSet rs) {
		try {
			purchaseDetails.setQuantity(rs.getInt("quantity"));
			purchaseDetails.setBook(this.bookRepo.findById(String.valueOf(rs.getString("book_id"))));
			purchaseDetails.setPurchase(this.purchaseRepo.findPurchaseById(rs.getString("purchase_id")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseDetails;
	}

	public PurchaseDetails mapAllPurchaseDetails(PurchaseDetails purchaseDetails, ResultSet rs) {
		try {
			Purchase puchase = new Purchase();
			puchase.setId(rs.getString("purchase_id"));
			puchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchase.purchase_date"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Book book = new Book();
			book.setName(rs.getString("book_name"));

			Publisher publisher = new Publisher();
			publisher.setName(rs.getString("publisher_name"));

			Employee employee = new Employee();
			employee.setName(rs.getString("employee_name"));

			purchaseDetails.setQuantity(rs.getInt("quantity"));

			purchaseDetails.setPrice(rs.getInt("purchase_price"));

			Author author = new Author();
			author.setName(rs.getString("author_name"));

			Category category = new Category();
			category.setName(rs.getString("category_name"));

			puchase.setDescription(rs.getString("purchase_description"));

			book.setPublisher(publisher);
			book.setAuthor(author);
			book.setCategory(category);
			puchase.setEmployee(employee);
			purchaseDetails.setBook(book);
			purchaseDetails.setPurchase(puchase);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseDetails;
	}

}
