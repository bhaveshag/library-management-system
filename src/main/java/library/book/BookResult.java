package library.book;

import java.util.ArrayList;
import java.util.List;

public class BookResult extends Book {

	private List<String> authors;
	private Boolean availability;

	public BookResult(String ISBN10, String ISBN13, String title, String cover, String publisher, int pages) {
		super(ISBN10, ISBN13, title, cover, publisher, pages);
		this.authors = null;
		this.availability = null;
	}

	public void setAuthors(List<String> authors) {
		this.authors = new ArrayList<>(authors);
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public Boolean getAvailability() {
		return availability;
	}

}
