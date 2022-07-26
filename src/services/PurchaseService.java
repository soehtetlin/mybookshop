package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import database_config.DBconnector;
import entities.Book;
import entities.Employee;
import entities.Publisher;
import entities.Purchase;
import entities.PurchaseDetails;
import repositories.PurchaseRepo;
import shared.mapper.PurchaseMapper;

public class PurchaseService implements PurchaseRepo {

	private final DBconnector dbConfig;
	private final PurchaseMapper purchaseMapper;
	private final BookService bookService;
	private PublisherService publisherService = new PublisherService();
	private EmployeeService employeeService = new EmployeeService();
	private Purchase purchase = new Purchase();
	private Employee emp;
	private Publisher pub;

	public PurchaseService() {
		this.dbConfig = new DBconnector();
		this.purchaseMapper = new PurchaseMapper();
		this.purchaseMapper.setPurchaseRepo(this);
		this.bookService = new BookService();
		this.purchaseMapper.setBookRepo(new BookService());

	}

	@Override
	public void createPurchase(Purchase purchase) {
		try {
			System.out.println("Gnerereted ID " + purchase.getId());
			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"INSERT INTO purchase (id,purchase_Date, description, employee_id, publisher_id) VALUES (?, ?, ?, ?, ?)");
			ps.setString(1, purchase.getId());
			ps.setString(2, LocalDateTime.now().toString());
			ps.setString(3, "Hello");
			ps.setString(4, purchase.getEmployee().getId());
			ps.setString(5, purchase.getPublisher().getId());

			ps.executeUpdate();

			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPurchase(String[] data) {
		try {
			purchase.setId(generateID("id", "Purchase", "PC"));
			emp = employeeService.findEmployeeByName(data[2]);
			pub = publisherService.findByName(data[0]);
			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"INSERT INTO purchase (id,purchase_Date, description, employee_id, publisher_id) VALUES (?, ?, ?, ?, ?)");
			ps.setString(1, purchase.getId());
			ps.setString(2, LocalDateTime.now().toString());
			ps.setString(3, "Hello");
			ps.setString(4, emp.getId());
			ps.setString(5, pub.getId());

			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createPurchaseDetails(List<PurchaseDetails> purchaseDetailsList) {

		purchaseDetailsList.forEach(pd -> {

			try {

				PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
						"INSERT INTO purchase_detail (quantity, product_id, purchase_id) VALUES (?, ?, ?)");

				Book originalbook = bookService.findById(pd.getBook().getId());
				pd.getBook().setSale_price((pd.getBook().getPrice() / 10) + pd.getBook().getPrice());
				pd.getBook().setStockamount(pd.getBook().getStockamount() + originalbook.getStockamount());
				bookService.updateBooks(originalbook.getId(), originalbook);
				ps.setInt(1, pd.getQuantity());
				ps.setString(2, pd.getBook().getId());
				ps.setString(3, purchase.getId());

				ps.executeUpdate();

				ps.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		});

	}

	public void createPurchaseDetails(String[] data) {

		try {
			{

				Book storedBook = bookService.findById(data[2]);
				storedBook.setPrice(Integer.valueOf(data[1]));
				storedBook.setSale_price(((Integer.valueOf(data[1]) / 10) + Integer.valueOf(data[1])));
				storedBook.setStockamount(storedBook.getStockamount() + Integer.valueOf(data[0]));
				bookService.updateBooks(storedBook.getId(), storedBook);
				PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
						"INSERT INTO purchase_detail (purchase_price,quantity, book_id, purchase_id) VALUES (?, ?, ?, ?)");
				ps.setInt(1, Integer.valueOf(data[1]));
				ps.setInt(2, Integer.valueOf(data[0]));
				ps.setString(3, data[2]);
				ps.setString(4, purchase.getId());
				ps.executeUpdate();

				ps.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getLatestPurchaseId() {
		String id = "";
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT id FROM Purchase ORDER BY id DESC";

			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getString("id");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	@Override
	public List<PurchaseDetails> findPurchaseDetailsListByProductId(String bookID) {
		List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase_detail WHERE book_id = '" + bookID + "';";

			String que = "SELECT id,purchasedate,book.name,publisher.name,employee.name,quantity,price,author.name,category,name,purchase.description FROM purchase_detail\n"
					+ "INNER JOIN category\n" + "ON category.id = book.category_id\n" + "INNER JOIN publisher\n"
					+ "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id\n" + "WHERE purchase_id = '" + bookID + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				PurchaseDetails purchaseDetails = new PurchaseDetails();
				purchaseDetailsList.add(this.purchaseMapper.mapToPurchaseDetails(purchaseDetails, rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseDetailsList;
	}

	@Override
	public Purchase findPurchaseById(String id) {
		Purchase purchase = new Purchase();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
					+ "ON supplier.sup_id = purchase.supplier_id\n" + "WHERE purchase_id=" + id + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				this.purchaseMapper.mapToSale(purchase, rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchase;
	}

	@Override
	public List<Purchase> findAllPurchases() {

		List<Purchase> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
					+ "ON supplier.sup_id = purchase.supplier_id\n";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Purchase purchase = new Purchase();
				purchaseList.add(this.purchaseMapper.mapToSale(purchase, rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseList;
	}

	public List<PurchaseDetails> loadAllPurchaseDetails() {

		List<PurchaseDetails> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select publisher.name as publisher_name,purchase.id as purchase_id\r\n"
					+ ",purchase.purchase_date,book.name as book_name,employee.name as employee_name,\r\n"
					+ "purchase_detail.purchase_price,purchase_detail.quantity,author.name as author_name,category.name as category_name, purchase.description as purchase_description  from purchase inner join purchase_detail on purchase_detail.purchase_id = purchase.id \r\n"
					+ "inner join book on purchase_detail.book_id = book.id inner join author on author.id = book.author_id\r\n"
					+ "inner join category on category.id = book.category_id\r\n"
					+ "inner join publisher on publisher.id = purchase.publisher_id \r\n"
					+ "left join employee on purchase.employee_id = employee.id order by purchase.purchase_date desc;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				PurchaseDetails purchase = new PurchaseDetails();
				purchaseList.add(this.purchaseMapper.mapAllPurchaseDetails(purchase, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseList;

	}

	public List<PurchaseDetails> findPurchaseDetailByPublisher(String name) {

		PublisherService publisherService = new PublisherService();
		Publisher publisher = publisherService.findByName(name);

		System.out.println("Return publisher name :" + publisher.getName());
		System.out.println("Return publisher id :" + publisher.getId());

		List<PurchaseDetails> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select publisher.name as publisher_name,purchase.id as purchase_id\r\n"
					+ ",purchase.purchase_date,book.name as book_name,employee.name as employee_name,\r\n"
					+ "purchase_detail.purchase_price as purchase_price,purchase_detail.quantity,author.name as author_name,category.name as category_name,purchase.description as purchase_description "
					+ "from purchase inner join purchase_detail on purchase_detail.purchase_id = purchase.id \r\n"
					+ "inner join book on purchase_detail.book_id = book.id inner join author on author.id = book.author_id\r\n"
					+ "inner join category on category.id = book.category_id\r\n"
					+ "inner join publisher on publisher.id = purchase.publisher_id \r\n"
					+ "left join employee on purchase.employee_id = employee.id where purchase.publisher_id='"
					+ publisher.getId() + "' order by purchase.purchase_date desc;";
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				PurchaseDetails purchase = new PurchaseDetails();
				purchaseList.add(this.purchaseMapper.mapAllPurchaseDetails(purchase, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseList;

	}

	public List<PurchaseDetails> findPurchaseDetailByEmployee(String name) {

		PublisherService publisherService = new PublisherService();
		Employee employee = employeeService.findEmployeeByName(name);

		List<PurchaseDetails> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select publisher.name as publisher_name,purchase.id as purchase_id\r\n"
					+ ",purchase.purchase_date,book.name as book_name,employee.name as employee_name,\r\n"
					+ "purchase_detail.purchase_price as purchase_price,purchase_detail.quantity,author.name as author_name,category.name as category_name,purchase.description as purchase_description "
					+ "from purchase inner join purchase_detail on purchase_detail.purchase_id = purchase.id \r\n"
					+ "inner join book on purchase_detail.book_id = book.id inner join author on author.id = book.author_id\r\n"
					+ "inner join category on category.id = book.category_id\r\n"
					+ "inner join publisher on publisher.id = purchase.publisher_id \r\n"
					+ "left join employee on purchase.employee_id = employee.id where purchase.employee_id='"
					+ employee.getId() + "' order by purchase.purchase_date desc;";
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				PurchaseDetails purchase = new PurchaseDetails();
				purchaseList.add(this.purchaseMapper.mapAllPurchaseDetails(purchase, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseList;

	}

	public int TotalPurchase() {
		int purchasetotal = 0;

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select sum(purchase_price*quantity) from purchase_detail inner join purchase on purchase.id = purchase_detail.purchase_id where day(purchase.purchase_date)=day(localtime());";

			ResultSet rs = st.executeQuery(query);
			rs.next();
			purchasetotal = rs.getInt("sum(purchase_price*quantity)");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchasetotal;
	}

	@Override
	public List<PurchaseDetails> findAllPurchaseDetailsByPurchaseId(String purchaseId) {

		List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase_details WHERE purchase_id = " + purchaseId + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				PurchaseDetails purchaseDetails = new PurchaseDetails();
				purchaseDetailsList.add(this.purchaseMapper.mapToPurchaseDetails(purchaseDetails, rs));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseDetailsList;
	}

	public List<Purchase> loadAllPurchaseID() {

		List<Purchase> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase ";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Purchase purchase = new Purchase();
				purchaseList.add(this.purchaseMapper.mapPurchaseID(purchase, rs));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseList;
	}

	@Override
	public List<Purchase> findPurchaseListBySupplierId(String supplierId) {

		List<Purchase> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
					+ "ON supplier.sup_id = purchase.supplier_id\n" + "WHERE supplier_id=" + supplierId + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Purchase purchase = new Purchase();

				purchaseList.add(this.purchaseMapper.mapToSale(purchase, rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseList;
	}

	@Override
	public List<Purchase> findPurchaseListByEmployeeId(String employeeId) {
		List<Purchase> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {
			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
					+ "ON supplier.sup_id = purchase.supplier_id\n" + "WHERE employee_id=" + employeeId + ";";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Purchase purchase = new Purchase();

				purchaseList.add(this.purchaseMapper.mapToSale(purchase, rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseList;
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
