package library.book;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookLoading {

	@Autowired
	private BookRepository bookRepo;
	
	@RequestMapping("/loadbook")
	public List<Book> loadBooks() {
		
		String fileName = "src/main/resources/books.csv";
		Book book = null;
		List<Book> books = new ArrayList<Book>();

		try {
			
			CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
			CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).withCSVParser(parser).build();
			
			String[] line = null;

			while((line = reader.readNext()) != null) {
				book = new Book(line[0], line[1], line[2], line[4], line[5], Integer.parseInt(line[6]));
				books.add(book);
			}
			bookRepo.insertBooks(books);
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return books;
	}

}
