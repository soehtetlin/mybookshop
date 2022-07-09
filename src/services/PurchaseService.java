//package services;
//
//import database_config.DBconnector;
//import entities.*;
//import repositories.ProductRepo;
//import repositories.PurchaseRepo;
//import shared.mapper.PurchaseMapper;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PurchaseService implements PurchaseRepo {
//
//	private final DBconnector dbConfig;
//	private final PurchaseMapper purchaseMapper;
//	private final ProductService productService;
//
//	public PurchaseService() {
//		this.dbConfig = new DBconnector();
//		this.purchaseMapper = new PurchaseMapper();
//		this.purchaseMapper.setPurchaseRepo(this);
//		this.productService = new ProductService();
//		this.purchaseMapper.setProductRepo(new ProductService());
//	}
//
//	public void createPurchase(Purchase purchase) {
//		try {
//
//			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
//					"INSERT INTO purchase (purchaseDate, employee_id, supplier_id, description) VALUES (?, ?, ?, ?)");
//
//			ps.setString(1, String.valueOf(purchase.getPurchaseDate()));
//			ps.setString(3, String.valueOf(purchase.getEmployee().getId()));
//			ps.setString(4, String.valueOf(purchase.getPublisher().getId()));
//			ps.setString(2, String.valueOf(purchase.getDescription()));
//
//			ps.executeUpdate();
//
//			ps.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void createPurchaseDetails(List<PurchaseDetails> purchaseDetailsList) {
//
//		purchaseDetailsList.forEach(pd -> {
//
//			try {
//
//				PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
//						"INSERT INTO purchase_details (purchase_id, product_id, purchase_price,quantity) VALUES (?, ?, ?, ?)");
//
//				ps.setInt(1, pd.getQuantity());
//				ps.setInt(2, pd.getPrice());
//				ps.setString(3, pd.getProduct().getId());
//				ps.setInt(4, this.getLatestPurchaseId());
//
//				ps.executeUpdate();
//
//				ps.close();
//
//				// Update Product Quantity and Price (by raising 10 %)
//				Product storedProduct = productService.findById(pd.getProduct().getId() + "");
//				storedProduct.setPrice(((pd.getPrice() / 10) + pd.getPrice()));
//				storedProduct.setQuantity(storedProduct.getQuantity() + pd.getQuantity());
//				productService.updateProduct(String.valueOf(storedProduct.getId()), storedProduct);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		});
//
//	}
//
//	public int getLatestPurchaseId() {
//		int id = 0;
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//			String query = "SELECT purchase_id FROM Purchase ORDER BY purchase_id DESC";
//
//			ResultSet rs = st.executeQuery(query);
//			rs.next();
//			id = rs.getInt("purchase_id");
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return id;
//	}
//
//	public List<PurchaseDetails> findPurchaseDetailsListByProductId(String productId) {
//		List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//			String query = "SELECT * FROM purchase_detials WHERE product_id = " + productId + ";";
//
//			ResultSet rs = st.executeQuery(query);
//
//			while (rs.next()) {
//				PurchaseDetails purchaseDetails = new PurchaseDetails();
//				purchaseDetailsList.add(this.purchaseMapper.mapToPurchaseDetails(purchaseDetails, rs));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchaseDetailsList;
//	}
//
//	public Purchase findPurchaseById(String id) {
//		Purchase purchase = new Purchase();
//
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
//					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
//					+ "ON supplier.sup_id = purchase.supplier_id\n" + "WHERE purchase_id=" + id + ";";
//
//			ResultSet rs = st.executeQuery(query);
//
//			while (rs.next()) {
//				this.purchaseMapper.mapToPurchase(purchase, rs);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchase;
//	}
//
//	public List<Purchase> findAllPurchases() {
//
//		List<Purchase> purchaseList = new ArrayList<>();
//
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
//					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
//					+ "ON supplier.sup_id = purchase.supplier_id\n";
//
//			ResultSet rs = st.executeQuery(query);
//
//			while (rs.next()) {
//				Purchase purchase = new Purchase();
//				purchaseList.add(this.purchaseMapper.mapToPurchase(purchase, rs));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchaseList;
//	}
//
//	public List<PurchaseDetails> findAllPurchaseDetailsByPurchaseId(String purchaseId) {
//
//		List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
//
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//			String query = "SELECT * FROM purchase_details WHERE purchase_id = " + purchaseId + ";";
//
//			ResultSet rs = st.executeQuery(query);
//
//			while (rs.next()) {
//				PurchaseDetails purchaseDetails = new PurchaseDetails();
//				purchaseDetailsList.add(this.purchaseMapper.mapToPurchaseDetails(purchaseDetails, rs));
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchaseDetailsList;
//	}
//
//	@Override
//	public List<Purchase> findPurchaseListBySupplierId(String supplierId) {
//
//		List<Purchase> purchaseList = new ArrayList<>();
//
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
//					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
//					+ "ON supplier.sup_id = purchase.supplier_id\n" + "WHERE supplier_id=" + supplierId + ";";
//
//			ResultSet rs = st.executeQuery(query);
//
//			while (rs.next()) {
//				Purchase purchase = new Purchase();
//
//				purchaseList.add(this.purchaseMapper.mapToPurchase(purchase, rs));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchaseList;
//	}
//
//	public List<Purchase> findPurchaseListByEmployeeId(String employeeId) {
//		List<Purchase> purchaseList = new ArrayList<>();
//
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
//					+ "on employee.emp_id = purchase.employee_id\n" + "INNER JOIN supplier\n"
//					+ "ON supplier.sup_id = purchase.supplier_id\n" + "WHERE employee_id=" + employeeId + ";";
//
//			ResultSet rs = st.executeQuery(query);
//
//			while (rs.next()) {
//				Purchase purchase = new Purchase();
//
//				purchaseList.add(this.purchaseMapper.mapToPurchase(purchase, rs));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return purchaseList;
//	}
//}
