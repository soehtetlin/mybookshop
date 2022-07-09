package repositories;

import entities.Author;


import java.util.List;

public interface AuthorRepo {
	
	void saveAuthor(Author category);
	
	void deleteAuthor(String id);

	void updateAuthor(String id, Author category);

	List<Author> findAllAuthors();

	Author findById(String id);


}
