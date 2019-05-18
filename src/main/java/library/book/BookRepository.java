package library.book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public List<Book> getAllBooks() {
		return jdbcTemplate.query("SELECT * FROM book", bookMapper);
	}

	public Book getBookByISBN13(String ISBN13) {
		return jdbcTemplate.queryForObject("SELECT * FROM book WHERE ISBN13 = '" + ISBN13 + "'", bookMapper);
	}

	public List<BookResult> getSearchResults(String searchQuery) {
		return jdbcTemplate.query(
				"SELECT DISTINCT B.ISBN10, B.ISBN13, B.title, B.cover, B.publisher, B.pages FROM book as B, author as A, book_authors as BA WHERE B.ISBN13 = BA.ISBN13 AND BA.author_id = A.author_id AND ( B.ISBN10 LIKE '%"
						+ searchQuery + "%' OR B.ISBN13 LIKE '%" + searchQuery + "%' OR B.title LIKE '%" + searchQuery
						+ "%' OR B.publisher LIKE '%" + searchQuery + "%' OR A.name LIKE '%" + searchQuery + "%')",
				bookResultMapper);
	}

	public int[] insertBooks(final List<Book> books) {
		List<Object[]> batch = new ArrayList<Object[]>();
		for (Book book : books) {
			Object[] values = new Object[] { book.getISBN10(), book.getISBN13(), book.getTitle(), book.getCover(),
					book.getPublisher(), book.getPages() };
			batch.add(values);
		}
		int[] updateCount = jdbcTemplate.batchUpdate("INSERT INTO book VALUES(?, ?, ?, ?, ?, ?)", batch);
		return updateCount;
	}

	public static final RowMapper<BookResult> bookResultMapper = new RowMapper<BookResult>() {
		public BookResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			BookResult bookResult = new BookResult(rs.getString("ISBN10"), rs.getString("ISBN13"),
					rs.getString("title"), rs.getString("cover"), rs.getString("publisher"), rs.getInt("pages"));
			return bookResult;
		}
	};

	public static final RowMapper<Book> bookMapper = new RowMapper<Book>() {
		public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
			Book book = new Book(rs.getString("ISBN10"), rs.getString("ISBN13"), rs.getString("title"),
					rs.getString("cover"), rs.getString("publisher"), rs.getInt("pages"));
			return book;
		}
	};

}
