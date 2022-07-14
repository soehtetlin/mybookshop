package entities;

import java.util.ArrayList;
import java.util.List;

public class Author {

	private String id;

	private String name;
	

	private List<Book> books = new ArrayList<>();
	
	public Author() {
	}

	public Author(String id, String name,List<Book> books) {
		super();
		this.id = id;
		this.name = name;
		this.books = books;
	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Book> getBooks(){
		return books;
	}
	
	public void setBooks(List<Book> books) {
		this.books = books;
	}



}
