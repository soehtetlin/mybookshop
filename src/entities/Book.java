package entities;

public class Book {

	private String id;
	private String photo;
	private String name;
	private Author author;
	private Category category;
	private Publisher publisher;
	private int price, sale_price, stockamount, shelf_number;
	private String remark;

	public Book() {
		super();
	}

	public Book(String id, String photo, String name, Author author, Category category, Publisher publisher, int price,
			int sale_price, int stockamount, int shelf_number, String remark) {
		super();
		this.id = id;
		this.photo = photo;
		this.name = name;
		this.author = author;
		this.category = category;
		this.publisher = publisher;
		this.price = price;
		this.sale_price = sale_price;
		this.stockamount = stockamount;
		this.shelf_number = shelf_number;
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSale_price() {
		return sale_price;
	}

	public void setSale_price(int sale_price) {
		this.sale_price = sale_price;
	}

	public int getStockamount() {
		return stockamount;
	}

	public void setStockamount(int stockamount) {
		this.stockamount = stockamount;
	}

	public int getShelf_number() {
		return shelf_number;
	}

	public void setShelf_number(int shelf_number) {
		this.shelf_number = shelf_number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
