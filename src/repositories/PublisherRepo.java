package repositories;

import entities.Publisher;

import java.util.List;

public interface PublisherRepo {

	void createSupplier(Publisher publisher);

	void updateSupplier(String id, Publisher publisher);

	Publisher findById(String id);

	List<Publisher> findAllSuppliers();

	void deleteSupplier(String id);
}
