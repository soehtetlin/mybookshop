package shared.mapper;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import entities.Author;
import entities.Book;
import entities.Category;
import entities.Customer;
import entities.Employee;
import entities.Sale;
import entities.SaleDetails;
import repositories.BookRepo;
import repositories.SaleRepo;

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

	public SaleDetails mapToSaleDetailstopten(SaleDetails saleDetails, ResultSet rs) {
		try {
			saleDetails.setQuantity(rs.getInt("sum(quantity)"));
			Book book = new Book();
			book.setPhoto(rs.getString("book.photo"));
			book.setName(rs.getString("book.name"));
			saleDetails.setBook(book);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleDetails;
	}

	public SaleDetails mapAllSaleDetails(SaleDetails saleDetails, ResultSet rs) {
		try {
			Sale sale = new Sale();
			sale.setId(rs.getString("sale_id"));
			sale.setSaleDate(LocalDateTime.parse(rs.getString("sale.sale_date"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			Book book = new Book();
			book.setName(rs.getString("book_name"));

			Customer customer = new Customer();
			customer.setName(rs.getString("customer_name"));
			sale.setCustomer(customer);

			Employee employee = new Employee();
			employee.setName(rs.getString("employee_name"));

			saleDetails.setQuantity(rs.getInt("quantity"));

			saleDetails.setPrice(rs.getInt("sale_price"));
			System.out.println("Price id : 	" + book.getPrice());

			Author author = new Author();
			author.setName(rs.getString("author_name"));

			Category category = new Category();
			category.setName(rs.getString("category_name"));

			book.setAuthor(author);
			book.setCategory(category);

			sale.setEmployee(employee);
			saleDetails.setBook(book);
			saleDetails.setSale(sale);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleDetails;
	}

}
