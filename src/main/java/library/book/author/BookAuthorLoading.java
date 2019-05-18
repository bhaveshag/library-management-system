package library.book.author;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@RestController
public class BookAuthorLoading {
	private static Logger logger = LoggerFactory.getLogger(AuthorLoading.class);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private AuthorRepository authorRepo;

	@RequestMapping("/loadbookauthor")
	public List<BookAuthor> loadBookAuthors() {

		String fileName = "src/main/resources/books.csv";
		BookAuthor bookAuthor = null;
		List<BookAuthor> bookAuthors = new ArrayList<>();

		try {

			CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
			CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).withCSVParser(parser)
					.build();

			String[] line = null;

			while ((line = reader.readNext()) != null) {
				String[] tempAuthors = line[3].split(",");
				String ISBN13 = line[1];
				for (String s : tempAuthors) {
					Integer author_id;
					if ((author_id = authorRepo.getAuthorId(s)) != null) {
						bookAuthor = new BookAuthor(author_id, ISBN13);
						bookAuthors.add(bookAuthor);
					}
				}
			}
		} catch (Exception exception) {
			logger.error("exception occured while loading book author!!!", exception);
		}

		for (BookAuthor author : bookAuthors) {
			try {
				jdbcTemplate.update("INSERT INTO book_authors VALUES(?, ?)", author.getAuthorId(), author.getISBN13());
			} catch (Exception e) {
				logger.error("record exists", e);
			}
		}
		return bookAuthors;
	}

}
