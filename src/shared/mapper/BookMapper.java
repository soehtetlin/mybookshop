package shared.mapper;

import entities.Author;
import entities.Category;
import entities.Publisher;
import entities.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper {
	public Book mapToProduct(Book book, ResultSet rs) {
		try {
			book.setId(rs.getString("id"));
			book.setName(rs.getString("name"));
			book.setPrice(rs.getInt("price"));
			book.setSale_price(rs.getInt("sale_price"));
			book.setStockamount(rs.getInt("stockamount"));
			book.setShelf_number(rs.getInt("shelf_number"));
			book.setPhoto(rs.getString("photo"));
			Author author = new Author();
			author.setId(rs.getString("author.id"));
			author.setName(rs.getString("author.name"));
			Category category = new Category();
			category.setId(rs.getString("category.id"));
			category.setName(rs.getString("category.name"));
			Publisher publisher = new Publisher();
			publisher.setId(rs.getString("publisher.id"));
			publisher.setName(rs.getString("publisher.name"));
			book.setAuthor(author);
			book.setPublisher(publisher);
			book.setCategory(category);
			book.setRemark(rs.getString("remark"));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return book;

	}
}
