package entities;

import java.util.Objects;

public class PurchaseDetails {

	private int id;

	private Purchase purchase;

	private Book book;

	private int price;

	private int quantity;

	public PurchaseDetails() {
	}

	public PurchaseDetails(int id, Purchase purchase, Book book, int quantity, int price) {
		super();
		this.id = id;
		this.purchase = purchase;
		this.book = book;
		this.quantity = quantity;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public Book getProduct() {
		return book;
	}

	public void setProduct(Book book) {
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
		PurchaseDetails that = (PurchaseDetails) o;
		return Objects.equals(book, that.book);
	}

	@Override
	public int hashCode() {
		return Objects.hash(book);
	}
}
