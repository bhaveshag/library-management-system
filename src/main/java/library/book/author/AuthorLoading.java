package library.book.author;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
public class AuthorLoading {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	@RequestMapping("/loadauthor")
	public Set<String> loadAuthors() {
		
		String fileName = "src/main/resources/books.csv";
		Set<String> authors = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

		try {

			CSVParser parser = new CSVParserBuilder().withSeparator('\t').build();
			CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).withCSVParser(parser).build();

			String[] line = null;

			while((line = reader.readNext()) != null) {
				String[] tempAuthors = line[3].split(",");
				for (String s: tempAuthors) {
					authors.add(s);
				}
			}

		}
		catch(Exception exception) {
			exception.printStackTrace();
		}

		for(String author: authors) {
			try {
				jdbcTemplate.update("INSERT INTO author(name) VALUES(?)", author);
			}
			catch(Exception e) {
				System.out.println("record exists");
			}
		}
		
		return authors;
	}

}
		
