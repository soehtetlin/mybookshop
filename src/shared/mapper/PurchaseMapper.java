package shared.mapper;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Employee;
import entities.Purchase;
import entities.PurchaseDetails;
import entities.Publisher;
import repositories.BookRepo;
import repositories.PurchaseRepo;
import services.BookService;
import services.PurchaseService;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class PurchaseMapper {

	private BookRepo bookRepo;
	private PurchaseRepo purchaseRepo;
	

	public void setBookRepo(BookRepo bookRepo) {
		this.bookRepo = bookRepo;
	}

	public void setPurchaseRepo(PurchaseRepo purchaseRepo) {
		this.purchaseRepo = purchaseRepo;
	}

	public Purchase mapToPurchase(Purchase purchase, ResultSet rs) {
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
			puchase.setId(rs.getString("purchase.id"));// 1
			System.out.println("Purchase id : 	" + puchase.getId());
			puchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchase.purchase_date"), // 2
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Book book = new Book();
			book.setName(rs.getString("book.name"));// 3

			Publisher publisher = new Publisher();
			publisher.setName(rs.getString("publisher_name"));// 4

			Employee employee = new Employee();
			employee.setName(rs.getString("employee_name"));// 5
			
			purchaseDetails.setQuantity(rs.getInt("quantity"));// 6

			book.setPrice(rs.getInt("book.price"));// 7
			System.out.println("Price id : 	" + book.getPrice());
			Author author = new Author();
			author.setName(rs.getString("author_name"));// 8

			Category category = new Category();
			category.setName(rs.getString("category_name"));// 9

			puchase.setDescription(rs.getString("purchase_description"));// 10
			// purchase.setPublisher(publisher);
			
			book.setPublisher(publisher);
			book.setAuthor(author);
			book.setCategory(category);
			puchase.setEmployee(employee);
			purchaseDetails.setBook(book);
			purchaseDetails.setPurchase(puchase);
		
			//System.out.println("Inside purchasemapper purchase id : " + purchaseDetails.getPurchase().getId() );
			// DateTimeFormatter df = new DateTimeFormatterBuilder()
//			    // case insensitive to parse JAN and FEB
//			    .parseCaseInsensitive()
//			    // add pattern
//			    .appendPattern("dd-MMM-yyyy")
//			    // create formatter (use English Locale to parse month names)
//			    .toFormatter(Locale.ENGLISH);
//		purchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchase_date"),
//				df));
//		purchaseDetails.setBook(this.bookRepo.findById(String.valueOf(rs.getString("book_id"))));
//		purchaseDetails.setPurchase(this.purchaseRepo.findPurchaseById(rs.getString("purchase_id")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("Inside purchasemapper return purchase id : " + purchaseDetails.getPurchase().getId() );
		return purchaseDetails;
	}

}
