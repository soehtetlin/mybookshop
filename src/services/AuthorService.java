package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database_config.DBconnector;
import entities.Author;
import entities.Book;
import repositories.AuthorRepo;
import shared.exception.AppException;
import shared.mapper.BookMapper;
import shared.mapper.GeneratePrimaryKey;

public class AuthorService implements AuthorRepo {

	private GeneratePrimaryKey genPrimaryKey = new GeneratePrimaryKey();
	private final DBconnector dbConfig = new DBconnector();

	private BookMapper bookMapper = new BookMapper();

	@Override
	public void saveAuthor(Author author) {

		try {
			author.setId(genPrimaryKey.generateID("id", "Author", "AU"));
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO Author (id,name)  VALUES (?,?);");
			ps.setString(1, author.getId());
			ps.setString(2, author.getName());
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
	public void updateAuthor(String id, Author category) {

		try {

			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE Author SET name = ? WHERE id = ?");

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
	public List<Author> findAllAuthors() {

		List<Author> authorlist = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM Author";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Author author = new Author();
				author.setId(rs.getString("id"));
				author.setName(rs.getString("name"));
				authorlist.add(author);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return authorlist;

	}

	@Override
	public Author findById(String id) {

		Author author = new Author();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM Author WHERE id = '" + id + "';";

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

	@Override
	public void deleteAuthor(String id) {

		try {
			List<Book> bookByAuthorId = this.findBookListByAuthorId(id);
			if (bookByAuthorId.size() > 0) {
				throw new AppException("This author cannnot be deleted");
			}

			String query = "DELETE FROM Author WHERE id = ?";
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

	public List<Book> findBookListByAuthorId(String authorId) {

		List<Book> bookList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM book\n" + "INNER JOIN category\n" + "ON category.id = book.category_id\n"
					+ "INNER JOIN publisher\n" + "ON publisher.id = book.publisher_id\n" + "INNER JOIN author\n"
					+ "ON author.id = book.author_id where author_id='" + authorId
					+ "' and book.price > 0 and book.stockamount > 0 ORDER BY book.id DESC ;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Book book = new Book();

				bookList.add(this.bookMapper.mapToProduct(book, rs));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}

	public String findByName(String authorname) {

		String id = null;

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT id FROM Author WHERE name = '" + authorname + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				id = rs.getString("id");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

}
