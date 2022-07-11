package shared.mapper;

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
			purchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchaseDate"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
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

	public PurchaseDetails mapToPurchaseDetails(PurchaseDetails purchaseDetails, ResultSet rs) {
		try {
			purchaseDetails.setQuantity(rs.getInt("quantity"));
			purchaseDetails.setProduct(this.bookRepo.findById(String.valueOf(rs.getString("book_id"))));
			purchaseDetails.setPurchase(this.purchaseRepo.findPurchaseById(rs.getString("purchase_id")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseDetails;
	}

}
