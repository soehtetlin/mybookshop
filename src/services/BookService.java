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
import entities.Category;
import entities.Publisher;

public class BookService {

	private final DBconnector dbConfig = new DBconnector();
	

	public void saveBooks(Book book) {
		try {
			AuthorService authService= new AuthorService();
			
			book.setId(authService.generateID("id", "Book", "BK"));
			
			PreparedStatement ps= this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO book (id, photo, name, author_id, category_id, publisher_id, price, sale_price, "
							+ "stockamount, shelf_number, remark) VALUES (?,?,?,?,?,?,?,?,?,?,?);");
			
			ps.setString(1, book.getId());
            ps.setString(2, book.getPhoto());
            ps.setString(3, book.getName());
            ps.setString(4, book.getAuthor().getId());
            ps.setString(5, book.getCategory().getId());
            ps.setString(6, book.getPublisher().getId());
            ps.setInt(7, book.getPrice());
            ps.setInt(8, book.getSale_price());
            ps.setInt(9, book.getStockamount());
            ps.setInt(10, book.getShelf_number());
            ps.setString(11, book.getRemark());

            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null,"Record Saved Successfully.");
		} catch(SQLException e) {
			 if (e instanceof SQLIntegrityConstraintViolationException){
	                JOptionPane.showMessageDialog(null,e.getMessage());
	            }
		}
	}

	public void updateBooks(String id, Book book) {
		try {
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE book SET photo = ? , name = ? , author_id = ? , category_id = ? , publisher_id = ?, price = ?"
							+ ", sale_price = ?, stockamount = ?, shelf_number = ?, remark = ? WHERE id = ?");

            ps.setString(1, book.getPhoto());
            ps.setString(2, book.getName());
            ps.setString(3, book.getAuthor().getId());
            ps.setString(4, book.getCategory().getId());
            ps.setString(5, book.getPublisher().getId());
            ps.setInt(6, book.getPrice());
            ps.setInt(7, book.getSale_price());
            ps.setInt(8, book.getStockamount());
            ps.setInt(9, book.getShelf_number());
            ps.setString(10, book.getRemark());
            
			ps.setString(11, book.getId());

			ps.execute();

			ps.close();
		}catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());

		}
	}
	
	public List<Book> findAllBooks(){
		List<Book> bookList=new ArrayList<>();
		
		try(Statement st= this.dbConfig.getConnection().createStatement()){
			
//			String query = "SELECT * FROM book";
			
			 String query = "SELECT * FROM book\n" +
	                    "INNER JOIN category\n" +
	                    "ON category.id = book.category_id\n" +
	                    "INNER JOIN publisher\n" +
	                    "ON publisher.id = book.publisher_id\n" +
	                    "INNER JOIN author\n" +
	                    "ON author.id = book.author_id;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Book b = new Book();
				b.setId(rs.getString("id"));
				b.setPhoto(rs.getString("photo"));
				b.setName(rs.getString("name"));
				
				Author author=new Author();
				b.setAuthor(author);
				
				Category category=new Category();
				b.setCategory(category);
				
				Publisher publisher=new Publisher();
				b.setPublisher(publisher);
				
				b.setPrice(rs.getInt("price"));
				b.setSale_price(rs.getInt("sale_price"));
				b.setStockamount(rs.getInt("stockamount"));
				b.setShelf_number(rs.getInt("shelf_number"));
				b.setRemark(rs.getString("remark"));
				
				bookList.add(b);
				
			}
			
		} catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());

		}
		return bookList;
	}
	
//	public Publisher findById(String id) {
//		Publisher publisher = new Publisher();
//
//		try (Statement st = this.dbConfig.getConnection().createStatement()) {
//
//			String query = "SELECT * FROM publisher WHERE id = " + id + ";";
//
//			ResultSet rs = st.executeQuery(query);
//
//			while (rs.next()) {
//				publisher.setId(rs.getString("id"));
//				publisher.setName(rs.getString("name"));
//				publisher.setContact_no(rs.getString("contact_no"));
//				publisher.setAddress(rs.getString("address"));
//				publisher.setEmail(rs.getString("email"));
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return publisher;
//	}
//
//	public void delete(String id) {
//		try {
//
////			List<Product> productsByCategoryId = this.productRepo.findProductsByBrandId(id);
////
////			if (productsByCategoryId.size() > 0) {
////				throw new AppException("This brand cannot be deleted");
////			}
//
//			String query = "DELETE FROM brand WHERE brand_id = ?";
//
//			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(query);
//			ps.setString(1, id);
//
//			ps.executeUpdate();
//			ps.close();
//
//		} catch (Exception e) {
//			if (e instanceof AppException)
//				JOptionPane.showMessageDialog(null, e.getMessage());
//			else
//				e.printStackTrace();
//		}
//	}

}
