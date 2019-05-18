package library.book.loan;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import library.book.Book;
import library.book.BookRepository;
import library.book.BookResult;
import library.book.author.AuthorLoading;
import library.book.author.AuthorRepository;

@RestController
public class BookLoanService {
	private static Logger logger = LoggerFactory.getLogger(AuthorLoading.class);

	@Autowired
	BookLoanRepository bookLoanRepo;

	@Autowired
	BookRepository bookRepo;

	@Autowired
	AuthorRepository authorRepo;

	@RequestMapping("/checkout")
	public Object[] checkOut(@RequestParam(value = "borrower") int cardId,
			@RequestParam(value = "isbn13") String ISBN13) {
		int borrowerExists = bookLoanRepo.checkBorrower(cardId);
		Boolean bookAvailable = bookLoanRepo.getAvailability(ISBN13);
		int borrowers = bookLoanRepo.checkBorrowerLimit(cardId);
		int status;
		// Only 2 books allowed for a user
		if (borrowerExists == 1 && bookAvailable && borrowers < 2) {
			logger.info("Adding book loan entry for borrower {}", cardId);
			java.util.Date today = new java.util.Date();
			java.util.Date due = new java.util.Date(today.getTime() + (1000 * 60 * 60 * 24 * 14));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			String dateOut = sdf.format(today);
			String dueDate = sdf.format(due);
			BookLoan bookLoan = new BookLoan(0, ISBN13, cardId, dateOut, dueDate, null);
			status = bookLoanRepo.insertBookLoan(bookLoan);
			Object[] object = new Object[] { status, borrowerExists, bookAvailable, borrowers };
			return object;
		} else {
			logger.info("book loan entry is not eligible for borrower {}", cardId);
			status = 0;
			Object[] object = new Object[] { status, borrowerExists, bookAvailable, borrowers };
			return object;
		}
	}

	@RequestMapping("/searchloaned")
	public List<Object[]> searchLoaned(@RequestParam(value = "query") String query) {
		List<String> isbns = new ArrayList<>(bookLoanRepo.getSearchLoanedResults(query));
		Book book = null;
		BookResult bookResult = null;
		int loanId;
		List<Object[]> batch = new ArrayList<>();
		for (String isbn : isbns) {
			book = bookRepo.getBookByISBN13(isbn);
			bookResult = new BookResult(book.getISBN10(), book.getISBN13(), book.getTitle(), book.getCover(),
					book.getPublisher(), book.getPages());
			bookResult.setAuthors(authorRepo.getAuthorNamesByISBN13(bookResult.getISBN13()));
			bookResult.setAvailability(bookLoanRepo.getAvailability(bookResult.getISBN13()));
			loanId = bookLoanRepo.getLoanIdByISBN13(isbn);
			Object[] values = { loanId, bookResult.getISBN10(), bookResult.getISBN13(), bookResult.getTitle(),
					bookResult.getCover(), bookResult.getPublisher(), bookResult.getPages(), bookResult.getAuthors(),
					bookResult.getAvailability() };
			batch.add(values);
		}
		return batch;
	}

	@RequestMapping(value = "/checkbooksin", method = RequestMethod.POST)
	public int checkIn(@RequestBody Object[] loanIds) {
		java.util.Date today = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String dateIn = sdf.format(today);
		logger.info("load ids {}", loanIds);
		bookLoanRepo.updateBookLoan(loanIds, dateIn);
		return 1;
	}
}
