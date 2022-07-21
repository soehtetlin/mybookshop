package repositories;

import java.util.List;

import entities.Sale;
import entities.SaleDetails;

public interface SaleRepo {
	
	void createSale(Sale purchase);

	void createSaleDetails(List<SaleDetails> purchaseDetailsList);

	Sale findSaleById(String id);

	List<Sale> findAllSales();

	List<Sale> findSaleListByCustomerId(String supplierId);

	List<Sale> findSaleListByEmployeeId(String employeeId);

	List<SaleDetails> findSaleDetailsListByProductId(String productId);

	List<SaleDetails> findAllSaleDetailsBySaleId(String purchaseId);
	
}
