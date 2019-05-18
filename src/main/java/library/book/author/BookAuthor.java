package library.book.author;

public class BookAuthor {

	private Integer author_id;
	private String ISBN13;

	public BookAuthor(Integer author_id, String ISBN13) {
		this.author_id = author_id;
		this.ISBN13 = ISBN13;
	}

	public void setAuthorId(Integer author_id) {
		this.author_id = author_id;
	}

	public void setISBN13(String ISBN13) {
		this.ISBN13 = ISBN13;
	}

	public Integer getAuthorId() {
		return author_id;
	}

	public String getISBN13() {
		return ISBN13;
	}

}
