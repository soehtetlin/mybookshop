//package shared.mapper;
//
//import entities.Employee;
//import entities.Purchase;
//import entities.PurchaseDetails;
//import entities.Publisher;
//import repositories.BookRepo;
//import repositories.PurchaseRepo;
//import java.sql.ResultSet;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class PurchaseMapper {
//
//	private BookRepo bookRepo;
//	private PurchaseRepo purchaseRepo;
//
//	public void setProductRepo(BookRepo bookRepo) {
//		this.bookRepo = bookRepo;
//	}
//
//	public void setPurchaseRepo(PurchaseRepo purchaseRepo) {
//		this.purchaseRepo = purchaseRepo;
//	}
//
//	public Purchase mapToPurchase(Purchase purchase, ResultSet rs) {
//		try {
//			purchase.setId(rs.getString("id"));
//			purchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchaseDate"),
//					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
//			purchase.setDescription(rs.getString("description"));
//			Employee employee = new Employee();
//			employee.setId(rs.getString("employee_id"));
//			employee.setName(rs.getString("emp_name"));
//			employee.setAddress(rs.getString("emp_address"));
//			employee.setEmail(rs.getString("emp_email"));
//			Publisher publisher = new Publisher();
//			publisher.setId(rs.getString("supplier_id"));
//			publisher.setName(rs.getString("sup_name"));
//			publisher.setPhone(rs.getString("sup_phone"));
//			publisher.setEmail(rs.getString("sup_email"));
//			publisher.setAddress(rs.getString("sup_address"));
//			purchase.setPublisher(publisher);
//			purchase.setEmployee(employee);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchase;
//	}
//
//	public PurchaseDetails mapToPurchaseDetails(PurchaseDetails purchaseDetails, ResultSet rs) {
//		try {
//			purchaseDetails.setId(rs.getInt("pd_id"));
//			purchaseDetails.setQuantity(rs.getInt("quantity"));
//			purchaseDetails.setPrice(rs.getInt("price"));
//			purchaseDetails.setProduct(this.bookRepo.findById(String.valueOf(rs.getInt("product_id"))));
//			purchaseDetails.setPurchase(this.purchaseRepo.findPurchaseById(String.valueOf(rs.getInt("purchase_id"))));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchaseDetails;
//	}
//}
