package entities;

import java.util.Objects;

public class SaleDetails {

	private String id;

	private Sale sale;

	private Book book;

	private int price;

	private int quantity;

	public SaleDetails() {
	}

	public SaleDetails(String id, Sale sale, Book book, int quantity, int price) {
		super();
		this.id = id;
		this.sale = sale;
		this.book = book;
		this.quantity = quantity;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Sale getSale() {
		System.out.println("Entities Sale getMethod :" + sale.getId());

		return sale;
	}

	public void setSale(Sale sale) {
		System.out.println("Entities Sale :" + sale.getId());
		this.sale = sale;
	}

	public Book getBook() {
		System.out.println("Entities saledetail get Book get name p:" + book.getName());
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SaleDetails that = (SaleDetails) o;
		return Objects.equals(book, that.book);
	}

	@Override
	public int hashCode() {
		return Objects.hash(book);
	}
}
