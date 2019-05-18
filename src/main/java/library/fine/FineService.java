package library.fine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import library.book.BookLoading;
import library.book.loan.BookLoan;
import library.book.loan.BookLoanRepository;

@RestController
public class FineService {
	private static Logger logger = LoggerFactory.getLogger(BookLoading.class);
	
	@Autowired
	FineRepository fineRepo;

	@Autowired
	BookLoanRepository bookLoanRepo;

	@RequestMapping("/updatefine")
	public int fines() {
		List<BookLoan> bookLoans = new ArrayList<>(bookLoanRepo.getPendingLoanIds());
		for (BookLoan bookLoan : bookLoans) {
			try {
				java.util.Date date = null;
				java.util.Date dueDate = null;
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dueDate = sdf.parse(bookLoan.getDueDate());
				if (bookLoan.getDateIn() == null) {
					date = new java.util.Date();
					String dt = sdf.format(date);
					date = sdf.parse(dt);
				} else {
					date = sdf.parse(bookLoan.getDateIn());
				}

				long diffInMillies = date.getTime() - dueDate.getTime();
				long days = diffInMillies / (1000 * 60 * 60 * 24);
				
				// 10 Rs fine per day 
				java.math.BigDecimal fineAmt = new java.math.BigDecimal(days * 10);
				if (fineRepo.checkLoanId(bookLoan.getLoanId()) == 0) {
					fineRepo.insertFine(bookLoan.getLoanId(), fineAmt);
				} else {
					if (fineRepo.checkPaid(bookLoan.getLoanId()) == 1) {
						fineRepo.updateFine(bookLoan.getLoanId(), fineAmt);
					}
				}
			} catch (Exception exception) {
				logger.error("Date parse exception", exception);
			}
		}
		return 1;
	}

	@RequestMapping("/displayfine")
	public List<Object[]> displayFines(@RequestParam(value = "fineOption") int fineOption) {
		return fineRepo.getFines(fineOption);
	}

	@RequestMapping("/payfines")
	public int payFines(@RequestParam(value = "borrowerId") int cardId) {
		return fineRepo.payFines(cardId);
	}

}
