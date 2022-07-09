package repositories;

import entities.Purchase;
import entities.PurchaseDetails;

import java.util.List;

public interface PurchaseRepo {

	void createPurchase(Purchase purchase);

	void createPurchaseDetails(List<PurchaseDetails> purchaseDetailsList);

	Purchase findPurchaseById(String id);

	List<Purchase> findAllPurchases();

	List<Purchase> findPurchaseListBySupplierId(String supplierId);

	List<Purchase> findPurchaseListByEmployeeId(String employeeId);

	List<PurchaseDetails> findPurchaseDetailsListByProductId(String productId);

	List<PurchaseDetails> findAllPurchaseDetailsByPurchaseId(String purchaseId);
}
