package repositories;

import entities.Book;

import java.util.List;

public interface BookRepo {
	List<Book> findBookByCategoryID(String categoryId);

	List<Book> findBookByPublisherID(String publisherId);

	void createBooks(Book book);

	void updateBooks(String id, Book book);

	void deletBooks(String id);

	Book findById(String bookID);

	List<Book> findAllBooks();
}
