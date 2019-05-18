package library.book;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import library.book.author.AuthorRepository;
import library.book.loan.BookLoanRepository;

@RestController
public class BookService {

	@Autowired
	private BookRepository bookRepo;

	@Autowired
	private AuthorRepository authorRepo;

	@Autowired
	private BookLoanRepository bookLoanRepo;

	@RequestMapping("/book")
	public List<Book> sendBooks() {
		return bookRepo.getAllBooks();
	}

	@RequestMapping("/search")
	public List<BookResult> search(@RequestParam(value = "query") String query) {
		List<BookResult> bookResults = new ArrayList<BookResult>();
		List<BookResult> tempBookResults = new ArrayList<BookResult>(bookRepo.getSearchResults(query));
		for (BookResult bookResult : tempBookResults) {
			bookResult.setAuthors(authorRepo.getAuthorNamesByISBN13(bookResult.getISBN13()));
			bookResult.setAvailability(bookLoanRepo.getAvailability(bookResult.getISBN13()));
			bookResults.add(bookResult);
		}
		return bookResults;
	}

}
