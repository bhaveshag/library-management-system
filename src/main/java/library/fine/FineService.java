package library.fine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import library.book.loan.BookLoan;
import library.book.loan.BookLoanRepository;

@RestController
public class FineService {

	@Autowired
	FineRepository fineRepo;

	@Autowired
	BookLoanRepository bookLoanRepo;

	@RequestMapping("/updatefine")
	public int fines() {
		List<BookLoan> bookLoans = new ArrayList<BookLoan>(bookLoanRepo.getPendingLoanIds());
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
				java.math.BigDecimal fineAmt = new java.math.BigDecimal(days * 0.25);
				if (fineRepo.checkLoanId(bookLoan.getLoanId()) == 0) {
					fineRepo.insertFine(bookLoan.getLoanId(), fineAmt);
				} else {
					if (fineRepo.checkPaid(bookLoan.getLoanId()) == 1) {
						fineRepo.updateFine(bookLoan.getLoanId(), fineAmt);
					}
				}
			} catch (Exception exception) {
				System.out.println("Date parse exception");
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
