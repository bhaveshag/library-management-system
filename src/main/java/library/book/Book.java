package library.book;

public class Book {

	private String ISBN10;
	private String ISBN13;
	private String title;
	private String cover;
	private String publisher;
	private int pages;

	public Book(String ISBN10, String ISBN13, String title, String cover, String publisher, int pages) {
		this.ISBN10 = ISBN10;
		this.ISBN13 = ISBN13;
		this.title = title;
		this.cover = cover;
		this.publisher = publisher;
		this.pages = pages;
	}

	public void setISBN10(String ISBN10) {
		this.ISBN10 = ISBN10;
	}

	public void setISBN13(String ISBN13) {
		this.ISBN13 = ISBN13;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getISBN10() {
		return ISBN10;
	}

	public String getISBN13() {
		return ISBN13;
	}

	public String getTitle() {
		return title;
	}

	public String getCover() {
		return cover;
	}

	public String getPublisher() {
		return publisher;
	}

	public int getPages() {
		return pages;
	}
}
