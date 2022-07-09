package repositories;

import entities.Book;

import java.util.List;

public interface BookRepo {
	List<Book> findProductsByCategoryId(String categoryId);

	List<Book> findProductsByBrandId(String brandId);

	void createProduct(Book book);

	void updateProduct(String id, Book book);

	void deleteProduct(String id);

	Book findById(String productId);

	List<Book> findAllProducts();
}
