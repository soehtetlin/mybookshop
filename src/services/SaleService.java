package services;

import database_config.DBconnector;
import entities.*;
import repositories.*;

import shared.mapper.SaleMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class SaleService implements SaleRepo {

	private final DBconnector dbConfig;
	private final SaleMapper saleMapper;
	private final BookService bookService;
	private CustomerService customerService = new CustomerService();
	private EmployeeService employeeService = new EmployeeService();
	private Sale sale = new Sale();
	private Employee emp;
	private Customer customer;

	public SaleService() {
		this.dbConfig = new DBconnector();
		this.saleMapper = new SaleMapper();
		this.saleMapper.setSaleRepo(this);
		this.bookService = new BookService();
		this.saleMapper.setBookRepo(new BookService());
	}

	public void createSale(Sale sale) {
		try {
			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"INSERT INTO sale (id,sale_Date, description, employee_id, customer_id) VALUES (?, ?, ?, ?, ?)");
			ps.setString(1, sale.getId());
			ps.setString(2, LocalDateTime.now().toString());
			ps.setString(3, "Hello");
			ps.setString(4, sale.getEmployee().getId());
			ps.setString(5, sale.getCustomer().getId());

			ps.executeUpdate();

			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createSale(String[] data) {
		try {
			sale.setId(generateID("id", "Sale", "SL"));

			emp = employeeService.findEmployeeByName(data[2]);
			// customer = customerService.findByName(data[0]);
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO sale (id,sale_date, employee_id, customer_id) VALUES (?, ?, ?, ?)");
			ps.setString(1, sale.getId());
			ps.setString(2, LocalDateTime.now().toString());
			ps.setString(3, emp.getId());
			ps.setString(4, data[0]);

			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createSaleDetails(List<SaleDetails> saleDetailsList) {

		saleDetailsList.forEach(pd -> {

			try {
				PreparedStatement ps = this.dbConfig.getConnection()
						.prepareStatement("INSERT INTO sale_detail (quantity, product_id, sale_id) VALUES (?, ?, ?)");

				Book originalbook = bookService.findById(pd.getBook().getId());
				pd.getBook().setSale_price((pd.getBook().getPrice() / 10) + pd.getBook().getPrice());
				pd.getBook().setStockamount(pd.getBook().getStockamount() + originalbook.getStockamount());
				bookService.updateBooks(originalbook.getId(), originalbook);
				ps.setInt(1, pd.getQuantity());
				ps.setString(2, pd.getBook().getId());
				ps.setString(3, sale.getId());

				ps.executeUpdate();

				ps.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		});

	}

	public void createSaleDetails(String[] data, int status) {

		try {
			int newprice = 0;
			Book storedBook = bookService.findById(data[2]);
			// storedBook.setPrice(Integer.valueOf(data[1]));
			// storedBook.setSale_price(((Integer.valueOf(data[1]) / 10) +
			// Integer.valueOf(data[1])));
			storedBook.setStockamount(storedBook.getStockamount() - Integer.valueOf(data[0]));

			bookService.updateBooks(storedBook.getId(), storedBook);

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"INSERT INTO sale_detail (vouncher_id, book_id, sale_price,quantity) VALUES (?, ?, ?,?)");

			ps.setString(1, sale.getId());
			ps.setString(2, storedBook.getId());
			if (status == 1) {
				newprice = (int) (Integer.valueOf(data[1]) - (Integer.valueOf(data[1]) * 0.05));
				ps.setInt(3, newprice);
			} else {
				ps.setString(3, data[1]);
			}

			ps.setString(4, data[0]);
			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getLatestSaleId() {
		String id = "";
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT id FROM Sale ORDER BY id DESC";

			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getString("id");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	public List<SaleDetails> findSaleDetailsListByProductId(String bookID) {
		List<SaleDetails> saleDetailsList = new ArrayList<>();
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM sale_detail WHERE book_id = '" + bookID + "';";

			String que = "SELECT id,saledate,book.name,customer.name,employee.name,quantity,price,author.name,category,name,sale.description FROM sale_detail\n"
					+ "INNER JOIN category\n" + "ON category.id = book.category_id\n" + "INNER JOIN customer\n"
					+ "ON customer.id = book.customer_id\n" + "INNER JOIN author\n" + "ON author.id = book.author_id\n"
					+ "WHERE sale_id = '" + bookID + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails saleDetails = new SaleDetails();
				saleDetailsList.add(this.saleMapper.mapToSaleDetails(saleDetails, rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleDetailsList;
	}

	public Sale findSaleById(String id) {
		Sale sale = new Sale();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM sale\n" + "INNER JOIN employee\n" + "on employee.emp_id = sale.employee_id\n"
					+ "INNER JOIN customer\n" + "ON customer.sup_id = sale.customer_id\n" + "WHERE sale_id=" + id + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				this.saleMapper.mapToSale(sale, rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sale;
	}

	public List<Sale> findAllSales() {

		List<Sale> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM sale\n" + "INNER JOIN employee\n" + "on employee.emp_id = sale.employee_id\n"
					+ "INNER JOIN customer\n" + "ON customer.sup_id = sale.customer_id\n";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Sale sale = new Sale();
				saleList.add(this.saleMapper.mapToSale(sale, rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleList;
	}

	public List<SaleDetails> loadAllSaleDetails() {

		List<SaleDetails> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

//			String query = "select sale_detail.id,sale_detail.sale_date,book.name,\r\n"
//					+ "customer.name as customer_name,\r\n"
//					+ "employee.name as employee_name,\r\n"
//					+ "sale_detail.quantity,book.price,author.name as author_name,\r\n"
//					+ "sale.description as sale_description\r\n"
//					+ "from sale \r\n"
//					+ "inner join sale_detail on sale.id = sale_detail.sale_id \r\n"
//					+ "inner join employee on employee.id = sale.employee_id\r\n"
//					+ "inner join book on book.id = sale_detail.book_id\r\n"
//					+ "inner join customer on customer.id = book.customer_id\r\n"
//					+ "inner join author on author.id = book.author_id";

			String query = "select sale.id,sale.sale_date,book.name,\r\n" + "customer.name as customer_name,\r\n"
					+ "employee.name as employee_name,\r\n"
					+ "sale_detail.quantity,book.price,author.name as author_name,\r\n"
					+ "category.name as category_name,\r\n" + "sale.description as sale_description\r\n"
					+ "from sale \r\n" + "inner join sale_detail on sale.id = sale_detail.sale_id \r\n"
					+ "inner join employee on employee.id = sale.employee_id\r\n"
					+ "inner join book on book.id = sale_detail.book_id\r\n"
					+ "inner join customer on customer.id = book.customer_id\r\n"
					+ "inner join author on author.id = book.author_id\r\n"
					+ "inner join category on category.id = book.category_id order by sale.sale_date desc;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails sale = new SaleDetails();
				saleList.add(this.saleMapper.mapAllSaleDetails(sale, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleList;

		// System.out.println("inside purchse service sale id : " + saleList);}
		// System.out.println("sizeo of return purhasedetail : " + saleList.size());
		// System.out.println(saleList.get(0));

		// String[] requestNos = new String[saleList.size()];

//		for (int i = 0; i < saleList.size(); i++) {
//		    requestNos[i] = saleList.get(i).getId();
//		    System.out.println("Sale sevice purcahseliest purchsseid : " + requestNos[i]);
//		}
//		saleList.forEach(e -> {
//			Object[] row = new Object[10];
//			row[0] = e.getSale().getId();
//			System.out.println("Indside purchse servcie salelist loop sale id : " + e.getSale().getId());
//			row[1] = e.getSale().getSaleDate();
//			row[2] = e.getBook().getName();
//			row[3] = e.getBook().getCustomer().getName();
//			row[4] = e.getSale().getEmployee().getName();
//			row[5] = e.getQuantity();
//			row[6] = e.getBook().getPrice();
//			row[7] = e.getBook().getAuthor().getName();
//			row[8] = e.getBook().getCategory().getName();
//			row[9] = e.getSale().getDescription();
//		});

	}

	public List<SaleDetails> loadAllSaleDetailsbyCustomerID(String customerid) {

		List<SaleDetails> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

//			String query = "select sale_detail.id,sale_detail.sale_date,book.name,\r\n"
//					+ "customer.name as customer_name,\r\n"
//					+ "employee.name as employee_name,\r\n"
//					+ "sale_detail.quantity,book.price,author.name as author_name,\r\n"
//					+ "sale.description as sale_description\r\n"
//					+ "from sale \r\n"
//					+ "inner join sale_detail on sale.id = sale_detail.sale_id \r\n"
//					+ "inner join employee on employee.id = sale.employee_id\r\n"
//					+ "inner join book on book.id = sale_detail.book_id\r\n"
//					+ "inner join customer on customer.id = book.customer_id\r\n"
//					+ "inner join author on author.id = book.author_id";

			String query = "select sale.id,sale.sale_date,book.name,\r\n" + "customer.name as customer_name,\r\n"
					+ "employee.name as employee_name,\r\n"
					+ "sale_detail.quantity,book.price,author.name as author_name,\r\n"
					+ "category.name as category_name,\r\n" + "sale.description as sale_description\r\n"
					+ "from sale \r\n" + "inner join sale_detail on sale.id = sale_detail.sale_id \r\n"
					+ "inner join employee on employee.id = sale.employee_id\r\n"
					+ "inner join book on book.id = sale_detail.book_id\r\n"
					+ "inner join customer on customer.id = book.customer_id\r\n"
					+ "inner join author on author.id = book.author_id\r\n"
					+ "inner join category on category.id = book.category_id order by sale.sale_date desc where customer.name='"
					+ customerid + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails sale = new SaleDetails();
				saleList.add(this.saleMapper.mapAllSaleDetails(sale, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleList;

		// System.out.println("inside purchse service sale id : " + saleList);}
		// System.out.println("sizeo of return purhasedetail : " + saleList.size());
		// System.out.println(saleList.get(0));

		// String[] requestNos = new String[saleList.size()];

//		for (int i = 0; i < saleList.size(); i++) {
//		    requestNos[i] = saleList.get(i).getId();
//		    System.out.println("Sale sevice purcahseliest purchsseid : " + requestNos[i]);
//		}
//		saleList.forEach(e -> {
//			Object[] row = new Object[10];
//			row[0] = e.getSale().getId();
//			System.out.println("Indside purchse servcie salelist loop sale id : " + e.getSale().getId());
//			row[1] = e.getSale().getSaleDate();
//			row[2] = e.getBook().getName();
//			row[3] = e.getBook().getCustomer().getName();
//			row[4] = e.getSale().getEmployee().getName();
//			row[5] = e.getQuantity();
//			row[6] = e.getBook().getPrice();
//			row[7] = e.getBook().getAuthor().getName();
//			row[8] = e.getBook().getCategory().getName();
//			row[9] = e.getSale().getDescription();
//		});

	}

	public List<SaleDetails> findAllSaleDetailsBySaleId(String saleId) {

		List<SaleDetails> saleDetailsList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM sale_details WHERE sale_id = " + saleId + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails saleDetails = new SaleDetails();
				saleDetailsList.add(this.saleMapper.mapToSaleDetails(saleDetails, rs));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleDetailsList;
	}

	public List<Sale> loadAllSaleID() {

		List<Sale> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM sale ";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Sale sale = new Sale();
				saleList.add(this.saleMapper.mapSaleID(sale, rs));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleList;
	}

	@Override
	public List<Sale> findSaleListByCustomerId(String customerId) {

		List<Sale> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM sale\n" + "INNER JOIN employee\n" + "on employee.emp_id = sale.employee_id\n"
					+ "INNER JOIN customer\n" + "ON customer.sup_id = sale.customer_id\n" + "WHERE customer_id="
					+ customerId + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Sale sale = new Sale();

				saleList.add(this.saleMapper.mapToSale(sale, rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleList;
	}

	public List<Sale> findSaleListByEmployeeId(String employeeId) {
		List<Sale> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {
			String query = "SELECT * FROM sale\n" + "INNER JOIN employee\n" + "on employee.emp_id = sale.employee_id\n"
					+ "INNER JOIN customer\n" + "ON customer.sup_id = sale.customer_id\n" + "WHERE employee_id="
					+ employeeId + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Sale sale = new Sale();

				saleList.add(this.saleMapper.mapToSale(sale, rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleList;
	}

	public String generateID(String field, String table, String prefix) {
		ResultSet rs = null;
		int current;

		try {
			Statement smt = dbConfig.getConnection().createStatement();
			rs = smt.executeQuery("SELECT " + field + " FROM " + table + " ORDER BY id");
			ArrayList<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString(field));

			}

			if (result.size() > 0) {
				current = Integer.parseInt(result.get(result.size() - 1).substring(2, 10)) + 1;
				if (current > 0 && current <= 9) {
					return prefix + "0000000" + current;
				} else if (current > 9 && current <= 99) {
					return prefix + "000000" + current;
				} else if (current > 99 && current <= 999) {
					return prefix + "00000" + current;
				} else if (current > 999 && current <= 9999) {
					return prefix + "0000" + current;
				} else if (current > 9999 && current <= 99999) {
					return prefix + "000" + current;
				} else if (current > 99999 && current <= 999999) {
					return prefix + "00" + current;
				} else if (current > 999999 && current <= 9999999) {
					return prefix + "0" + current;
				} else if (current > 9999999 && current <= 9999999) {
					return prefix + current;
				}
			}
		} catch (SQLException e) {
		}
		return prefix + "00000001";
	}

	public String generateID2(String field, String table, String prefix) {
		ResultSet rs = null;
		int current;

		try {
			Statement smt = dbConfig.getConnection().createStatement();
			rs = smt.executeQuery("SELECT " + field + " FROM " + table + " ORDER BY id");
			ArrayList<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString(field));

			}

			System.out.println("Result size " + result.size());

			if (result.size() > 0) {

				System.out.println(
						"Current ; " + Integer.parseInt(result.get(result.size() - 1).toString().substring(3, 10)) + 1);

				current = Integer.parseInt(result.get(result.size() - 1).toString().substring(3, 10)) + 1;
				if (current > 0 && current <= 9) {
					return prefix + "000000" + current;
				} else if (current > 9 && current <= 99) {
					return prefix + "00000" + current;
				} else if (current > 99 && current <= 999) {
					return prefix + "0000" + current;
				} else if (current > 999 && current <= 9999) {
					return prefix + "000" + current;
				} else if (current > 9999 && current <= 99999) {
					return prefix + "00" + current;
				} else if (current > 99999 && current <= 999999) {
					return prefix + "0" + current;
				} else if (current > 999999 && current <= 999999) {
					return prefix + current;
				}
			}
		} catch (SQLException e) {
		}
		return prefix + "0000001";
	}
}
