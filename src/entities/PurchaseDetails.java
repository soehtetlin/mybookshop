package entities;

import java.util.Objects;

public class PurchaseDetails {

	private String id;

	private Purchase purchase;

	private Book book;

	private int price;

	private int quantity;

	public PurchaseDetails() {
	}

	public PurchaseDetails(String id, Purchase purchase, Book book, int quantity, int price) {
		super();
		this.id = id;
		this.purchase = purchase;
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

	public Purchase getPurchase() {
		System.out.println("Entities Purchase getMethod :" + purchase.getId());

		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		System.out.println("Entities Purchase :" + purchase.getId());
		this.purchase = purchase;
	}

	public Book getBook() {
		System.out.println("Entities purchasedetail get Book get name p:" + book.getName());
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
		PurchaseDetails that = (PurchaseDetails) o;
		return Objects.equals(book, that.book);
	}

	@Override
	public int hashCode() {
		return Objects.hash(book);
	}
}
