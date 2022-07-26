package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database_config.DBconnector;
import entities.Book;
import entities.Customer;
import entities.Employee;
import entities.Sale;
import entities.SaleDetails;
import repositories.SaleRepo;
import shared.mapper.SaleMapper;

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

	@Override
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

	@Override
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

	public List<SaleDetails> findtoptenbooks() {
		List<SaleDetails> saleDetailsList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select book.photo,book.name,sum(quantity)"
					+ " from sale_detail inner join book on book.id = sale_detail.book_id"
					+ " inner join sale on sale.id = sale_detail.vouncher_id "
					+ "where month(sale.sale_date)=month(localtime())" + " group by book_id  "
					+ "order by sum(quantity) desc limit 10;";
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails saledetail = new SaleDetails();

				saleDetailsList.add(this.saleMapper.mapToSaleDetailstopten(saledetail, rs));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "You cannot delete this publisher");

		}
		return saleDetailsList;
	}

	public void createSaleDetails(String[] data, int status) {

		try {
			int newprice = 0;
			Book storedBook = bookService.findById(data[2]);

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

	@Override
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

	public List<SaleDetails> findSaleDetailByEmployee(String name) {

		CustomerService customerService = new CustomerService();
		Employee employee = employeeService.findEmployeeByName(name);

		List<SaleDetails> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select sale.id as sale_id,author.name as author_name,category.name as category_name,sale_detail.sale_price,customer.name as customer_name,\r\n"
					+ "sale.sale_date,sale_detail.quantity,book.name as book_name,employee.name as employee_name\r\n"
					+ "from sale_detail inner join sale on sale.id = sale_detail.vouncher_id\r\n"
					+ "inner join employee on employee.id = sale.employee_id \r\n"
					+ "inner join customer on customer.id = sale.customer_id\r\n"
					+ "inner join book on book.id = sale_detail.book_id\r\n"
					+ "inner join category on book.category_id = category.id \r\n"
					+ "inner join author on book.author_id = author.id\r\n" + "where employee.id ='" + employee.getId()
					+ "' order by sale.sale_date desc;";
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails sale = new SaleDetails();
				saleList.add(this.saleMapper.mapAllSaleDetails(sale, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleList;

	}

	public List<SaleDetails> findSaleDetailByCustomer(String name) {
		System.out.println("Name Customer " + name);

		CustomerService customerService = new CustomerService();
		Customer customer = customerService.findByName(name);
		System.out.println("Customer ID " + customer.getId());
		System.out.println("Return customer name :" + customer.getName());
		System.out.println("Return customer id :" + customer.getId());

		List<SaleDetails> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select sale.id as sale_id,author.name as author_name,category.name as category_name,sale_detail.sale_price,customer.name as customer_name,\r\n"
					+ "sale.sale_date,sale_detail.quantity,book.name as book_name,employee.name as employee_name\r\n"
					+ "from sale_detail inner join sale on sale.id = sale_detail.vouncher_id\r\n"
					+ "inner join employee on employee.id = sale.employee_id \r\n"
					+ "inner join customer on customer.id = sale.customer_id\r\n"
					+ "inner join book on book.id = sale_detail.book_id\r\n"
					+ "inner join category on book.category_id = category.id \r\n"
					+ "inner join author on book.author_id = author.id\r\n" + "where customer.id ='" + customer.getId()
					+ "' order by sale.sale_date desc;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails sale = new SaleDetails();
				saleList.add(this.saleMapper.mapAllSaleDetails(sale, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleList;

	}

	public int TotalSale() {
		int saletotal = 0;

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select sum(sale_price*quantity) from sale_detail inner join sale on sale.id = sale_detail.vouncher_id where day(sale.sale_date)=day(localtime());";

			ResultSet rs = st.executeQuery(query);
			rs.next();
			saletotal = rs.getInt("sum(sale_price*quantity)");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return saletotal;
	}

	@Override
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

	@Override
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

			String query = "select customer.name as customer_name,sale.id as sale_id\r\n"
					+ ",sale.sale_date,book.name as book_name,employee.name as employee_name,\r\n"
					+ "sale_detail.sale_price,sale_detail.quantity,author.name as author_name,category.name as category_name  from sale inner join sale_detail on sale_detail.vouncher_id = sale.id \r\n"
					+ "inner join book on sale_detail.book_id = book.id inner join author on author.id = book.author_id\r\n"
					+ "inner join category on category.id = book.category_id\r\n"
					+ "inner join customer on customer.id = sale.customer_id \r\n"
					+ "left join employee on sale.employee_id = employee.id order by sale.sale_date desc;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails sale = new SaleDetails();
				saleList.add(this.saleMapper.mapAllSaleDetails(sale, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleList;

	}

	public List<SaleDetails> loadAllSaleDetailsbyCustomerID(String customerid) {

		List<SaleDetails> saleList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select customer.name as customer_name,sale.id as sale_id\r\n"
					+ ",sale.sale_date,book.name as book_name,employee.name as employee_name,\r\n"
					+ "sale_detail.sale_price,sale_detail.quantity,author.name as author_name,category.name as category_name  from sale inner join sale_detail on sale_detail.vouncher_id = sale.id \r\n"
					+ "inner join book on sale_detail.book_id = book.id inner join author on author.id = book.author_id\r\n"
					+ "inner join category on category.id = book.category_id\r\n"
					+ "inner join customer on customer.id = sale.customer_id \r\n"
					+ "left join employee on sale.employee_id = employee.id order by sale.sale_date desc where customer.name='\"\r\n"
					+ "					+ customerid + \"'";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				SaleDetails sale = new SaleDetails();
				saleList.add(this.saleMapper.mapAllSaleDetails(sale, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return saleList;

	}

	@Override
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

	@Override
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
