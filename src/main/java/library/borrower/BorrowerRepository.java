package library.borrower;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BorrowerRepository {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public int[] insertBorrowers(final List<Borrower> borrowers) {
		List<Object[]> batch = new ArrayList<>();
		for (Borrower borrower : borrowers) {
			Object[] values = new Object[] { borrower.getCardId(), borrower.getFirstName(), borrower.getLastName(),
					borrower.getEmail(), borrower.getAddress(), borrower.getCity(), borrower.getState(),
					borrower.getPhone() };
			batch.add(values);
		}
		int[] updateCount = jdbcTemplate.batchUpdate("INSERT INTO borrower VALUES(?, ?, ?, ?, ?, ?, ?, ?)", batch);
		return updateCount;
	}

	public int insertBorrower(final Borrower borrower) {
		try {
			return jdbcTemplate.update(
					"INSERT INTO borrower(first_name, last_name, email, address, city, state, phone) VALUES(?, ?, ?, ?, ?, ?, ?)",
					new Object[] { borrower.getFirstName(), borrower.getLastName(), borrower.getEmail(),
							borrower.getAddress(), borrower.getCity(), borrower.getState(), borrower.getPhone() });
		} catch (Exception e) {
			return -1;
		}
	}

}
