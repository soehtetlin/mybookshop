//package shared.mapper;
//
//import entities.Author;
//import entities.Category;
//import entities.Book;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class BookMapper {
//	public Book mapToProduct(Book book, ResultSet rs) {
//		try {
//			book.setId(rs.getString("product_id"));
//			book.setName(rs.getString("productName"));
//			book.setQuantity(rs.getInt("quantity"));
//			book.setPrice(rs.getInt("price"));
//			Category category = new Category();
//			category.setId(rs.getString("category_id"));
//			category.setName(rs.getString("categoryName"));
//			Author author = new Author();
//			author.setId(rs.getString("brand_id"));
//			author.setName(rs.getString("brandName"));
//			book.setCategory(category);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return book;
//
//	}
//}
