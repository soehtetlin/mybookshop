package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLIntegrityConstraintViolationException;

import entities.Book;
import entities.Category;
import repositories.BookRepo;
import repositories.CategoryRepo;
import database_config.DBconnector;
import shared.exception.AppException;
import shared.mapper.BookMapper;

import javax.swing.*;

public class CategoryService implements CategoryRepo {

	private final DBconnector dbConfig = new DBconnector();
	private BookMapper bookMapper=new BookMapper();
	
	@Override
	public void saveCategory(Category category) {
		// TODO Auto-generated method stub
		try {
			category.setId(generateID("id", "Category", "CA"));
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO Category (id,name)  VALUES (?,?);");
			ps.setString(1, category.getId());
			ps.setString(2, category.getName());
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Record Saved Successfully.");

		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, "Please Enter Name");
			}

		}

	}

	@Override
	public void deleteCategory(String id) {
		// TODO Auto-generated method stub
		try {
			List<Book> booksByCategoryId = this.findBookByCategoryID(id);
			
			if(booksByCategoryId.size() > 0) {
				throw new AppException("This category cannot be deleted.");
			}
			
			String query = "DELETE FROM Category WHERE id = ?";
			
			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(query);
			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Delete Successfully!");
		} catch (Exception e) {
			if (e instanceof AppException)
				JOptionPane.showMessageDialog(null, e.getMessage());
			else
				e.printStackTrace();
		}
	}

	@Override
	public void updateCategory(String id, Category category) {
		// TODO Auto-generated method stub
		try {

			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE Category SET name = ? WHERE id = ?");

			ps.setString(1, category.getName());
			ps.setString(2, id);
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Record Update Successfully.");

		} catch (Exception e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, "Already Exists");
			}
		}
	}

	@Override
	public List<Category> findAllCategories() {
		// TODO Auto-generated method stub
		List<Category> categoryList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM Category ORDER BY category.id DESC;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Category category = new Category();
				category.setId(rs.getString("id"));
				category.setName(rs.getString("name"));
				categoryList.add(category);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoryList;

	}

	@Override
	public Category findById(String id) {
		// TODO Auto-generated method stub
		Category author = new Category();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM Category WHERE id = '" + id + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				author.setId(rs.getString("id"));
				author.setName(rs.getString("name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return author;
	}
	
	public List<Book> findBookByCategoryID(String categoryId) {

		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

//				String query = "SELECT * FROM book";

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id where category_id='" + categoryId
					+ "' and book.price > 0 and book.stockamount > 0 ORDER BY book.id DESC ;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Book book = new Book();

				bookList.add(this.bookMapper.mapToProduct(book, rs));

			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());

		}
		return bookList;
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

}
