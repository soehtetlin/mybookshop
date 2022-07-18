package repositories;

import entities.Category;

import java.util.List;

public interface CategoryRepo {

	void saveCategory(Category category);

	void updateCategory(String id, Category category);

	List<Category> findAllCategories();

	Category findById(String id);

	void deleteCategory(String id);
}
