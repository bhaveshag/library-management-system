package library.book.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Repository
public class BookLoanRepository {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public Boolean getAvailability(String ISBN13) {
		List<String> dates = jdbcTemplate.query("SELECT date_in FROM book_loan WHERE ISBN13 = '" + ISBN13 + "'", availabilityMapper); 
		if(dates.isEmpty()){
			return true;
		}
		else {
			for(String date: dates) {
				if(date == null){
					return false;
				}
			}
			return true;
		}
	}

	public Integer checkBorrower(int cardId) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM borrower WHERE card_id = ?", Integer.class, cardId);
	}

	public Integer checkBorrowerLimit(int cardId) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM book_loan WHERE card_id = ?", Integer.class, cardId);
	}
	
	public int insertBookLoan(final BookLoan bookLoan) {
		try {
			return jdbcTemplate.update("INSERT INTO book_loan(ISBN13, card_id, date_out, due_date) VALUES(?, ?, ?, ?)", new Object[] {
				bookLoan.getISBN13(),
				bookLoan.getCardId(),
				bookLoan.getDateOut(),
				bookLoan.getDueDate()
			});
		}
		catch(Exception e) {
			return 0;
		}
	}

	public List<String> getSearchLoanedResults(String searchQuery) {
		return jdbcTemplate.query("SELECT L.ISBN13 FROM borrower as B, book_loan as L WHERE B.card_id = L.card_id AND L.date_in IS NULL AND (L.card_id LIKE '%" + searchQuery + "%' OR L.ISBN13 LIKE '%" + searchQuery + "%' OR B.first_name LIKE '%" + searchQuery + "%' OR B.last_name LIKE '%" + searchQuery + "%')", isbnMapper);
	}

	public Integer getLoanIdByISBN13(String ISBN13) {
		return jdbcTemplate.queryForObject("SELECT loan_id FROM book_loan WHERE ISBN13 = ?", Integer.class, ISBN13);
	}

	public void updateBookLoan(Object[] loanIds, String date) {
		for(int i = 0; i < loanIds.length; i++){
			jdbcTemplate.update("UPDATE book_loan SET date_in ='" + date + "'  WHERE loan_id = " + loanIds[i] );
		}
	}

	public List<BookLoan> getPendingLoanIds() {
		return jdbcTemplate.query("SELECT loan_id, due_date, date_in FROM book_loan WHERE date_in > due_date OR (date_in IS NULL AND now() > due_date)", pendingLoanMapper);
	}

	public static final RowMapper<BookLoan> pendingLoanMapper = new RowMapper<BookLoan>() {
		public BookLoan mapRow(ResultSet rs, int rowNum) throws SQLException {
			String dateIn, dueDate;
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			dueDate = sdf.format(rs.getDate("due_date"));
			if(rs.getDate("date_in") == null) {
				dateIn = null;
			}
			else {
				dateIn = sdf.format(rs.getDate("date_in"));
			}
			BookLoan bookLoan = new BookLoan(rs.getInt("loan_id"), null, 0, null, dueDate, dateIn);
			return bookLoan;
		}
	};

	public static final RowMapper<String> isbnMapper = new RowMapper<String>() {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("ISBN13");
		}
	};

	public static final RowMapper<String> availabilityMapper = new RowMapper<String>() {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String date;
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
			if(rs.getDate("date_in") == null) {
				date = null;
			}
			else {
				date = sdf.format(rs.getDate("date_in"));
			}
			return date;
		}
	};

}
