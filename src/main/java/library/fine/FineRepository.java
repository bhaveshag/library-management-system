package library.fine;

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
public class FineRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public int checkLoanId(int loanId){
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM fine WHERE loan_id = ?", Integer.class, loanId);
	}

	public int checkPaid(int loanId){
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM fine WHERE loan_id = ? AND paid = 0", Integer.class, loanId);
	}

	public int insertFine(int loanId, java.math.BigDecimal fineAmt){
		return jdbcTemplate.update("INSERT INTO fine VALUES(?, ?, ?)", loanId, fineAmt, 0);
	}

	public int updateFine(int loanId, java.math.BigDecimal fineAmt){
		return jdbcTemplate.update("UPDATE fine SET amount = ? WHERE loan_id = ?", fineAmt, loanId);
	}

	public List<Object[]> getFines(int fineOption){
		if(fineOption == 1){
			return jdbcTemplate.query("SELECT L.card_id, SUM(F.amount) AS fine_amt FROM book_loan as L, fine as F WHERE L.loan_id = F.loan_id AND F.paid = 0 GROUP BY L.card_id", fineMapper);
		}
		else {
			return jdbcTemplate.query("SELECT L.card_id, SUM(F.amount) AS fine_amt FROM book_loan as L, fine as F WHERE L.loan_id = F.loan_id GROUP BY L.card_id", fineMapper);
		}
	}

	public int payFines(int cardId){
		return jdbcTemplate.update("UPDATE fine SET paid = 1 WHERE loan_id IN(SELECT loan_id FROM book_loan WHERE card_id = ? AND date_in IS NOT NULL)", cardId);
	}

	public static final RowMapper<Object[]> fineMapper = new RowMapper<Object[]>() {
		public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			Object[] object = {rs.getInt("card_id"), rs.getBigDecimal("fine_amt")};
			return object;
		}
	};

}
