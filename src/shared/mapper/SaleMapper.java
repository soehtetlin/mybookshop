package shared.mapper;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Customer;
import entities.Employee;
import entities.Sale;
import entities.SaleDetails;
import entities.Sale;
import entities.Customer;
import repositories.*;

import services.BookService;
import services.SaleService;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class SaleMapper {

	private BookRepo bookRepo;
	private SaleRepo saleRepo;

	public void setBookRepo(BookRepo bookRepo) {
		this.bookRepo = bookRepo;
	}

	public void setSaleRepo(SaleRepo saleRepo) {
		this.saleRepo = saleRepo;
	}

	public Sale mapToSale(Sale sale, ResultSet rs) {
		try {

			sale.setId(rs.getString("id"));
			sale.setSaleDate(
					LocalDateTime.parse(rs.getString("sale_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Employee employee = new Employee();
			employee.setId(rs.getString("employee.id"));
			employee.setName(rs.getString("employee.name"));
			employee.setAddress(rs.getString("employee.address"));
			employee.setEmail(rs.getString("employee.email"));
			Customer customer = new Customer();
			customer.setId(rs.getString("customer.id"));
			customer.setName(rs.getString("customer.name"));
			customer.setContact_no(rs.getString("customer.contact_no"));
			customer.setEmail(rs.getString("customer.email"));
			customer.setAddress(rs.getString("customer.address"));
			sale.setCustomer(customer);
			sale.setEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sale;
	}

	public Sale mapSaleID(Sale sale, ResultSet rs) {
		try {

			sale.setId(rs.getString("id"));
			sale.setSaleDate(
					LocalDateTime.parse(rs.getString("sale_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Employee employee = new Employee();
			employee.setId(rs.getString("employee_id"));
			Customer customer = new Customer();
			customer.setId(rs.getString("customer_id"));
			sale.setCustomer(customer);
			sale.setEmployee(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sale;
	}

	public SaleDetails mapToSaleDetails(SaleDetails saleDetails, ResultSet rs) {
		try {
			saleDetails.setQuantity(rs.getInt("quantity"));
			saleDetails.setBook(this.bookRepo.findById(String.valueOf(rs.getString("book_id"))));
			saleDetails.setSale(this.saleRepo.findSaleById(rs.getString("sale_id")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleDetails;
	}

	public SaleDetails mapAllSaleDetails(SaleDetails saleDetails, ResultSet rs) {
		try {
			Sale puchase = new Sale();
			puchase.setId(rs.getString("sale.id"));// 1
			System.out.println("Sale id : 	" + puchase.getId());
			puchase.setSaleDate(LocalDateTime.parse(rs.getString("sale.sale_date"), // 2
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Book book = new Book();
			book.setName(rs.getString("book.name"));// 3

			Customer customer = new Customer();
			customer.setName(rs.getString("customer_name"));// 4

			Employee employee = new Employee();
			employee.setName(rs.getString("employee_name"));// 5

			saleDetails.setQuantity(rs.getInt("quantity"));// 6

			book.setPrice(rs.getInt("book.price"));// 7
			System.out.println("Price id : 	" + book.getPrice());

//			book.setSale_price(rs.getInt("book.sale_price"));
//			System.out.println("SalePrce : " + book.getSale_price());
			Author author = new Author();
			author.setName(rs.getString("author_name"));// 8

			Category category = new Category();
			category.setName(rs.getString("category_name"));// 9

			// sale.setCustomer(customer);

			// book.setCustomer(customer);
			book.setAuthor(author);
			book.setCategory(category);
			puchase.setEmployee(employee);
			saleDetails.setBook(book);
			saleDetails.setSale(puchase);

			// System.out.println("Inside salemapper sale id : " +
			// saleDetails.getSale().getId() );
			// DateTimeFormatter df = new DateTimeFormatterBuilder()
//			    // case insensitive to parse JAN and FEB
//			    .parseCaseInsensitive()
//			    // add pattern
//			    .appendPattern("dd-MMM-yyyy")
//			    // create formatter (use English Locale to parse month names)
//			    .toFormatter(Locale.ENGLISH);
//		sale.setSaleDate(LocalDateTime.parse(rs.getString("sale_date"),
//				df));
//		saleDetails.setBook(this.bookRepo.findById(String.valueOf(rs.getString("book_id"))));
//		saleDetails.setSale(this.saleRepo.findSaleById(rs.getString("sale_id")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("Inside salemapper return sale id : " +
		// saleDetails.getSale().getId() );
		return saleDetails;
	}

}
