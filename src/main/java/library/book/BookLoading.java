package library.book;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@RestController
public class BookLoading {

	private static Logger logger = LoggerFactory.getLogger(BookLoading.class);

	@Autowired
	private BookRepository bookRepo;

	@RequestMapping("/loadbook")
	public List<Book> loadBooks() {

		String fileName = "src/main/resources/books.csv";
		Book book = null;
		List<Book> books = new ArrayList<>();

		try {

			CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
			CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).withCSVParser(parser)
					.build();

			String[] line = null;

			while ((line = reader.readNext()) != null) {
				book = new Book(line[0], line[1], line[2], line[4], line[5], Integer.parseInt(line[6]));
				books.add(book);
			}
			
			bookRepo.insertBooks(books);
		} catch (Exception exception) {
			logger.error("Exception occured while loading books !!", exception);
		}

		return books;
	}

}
