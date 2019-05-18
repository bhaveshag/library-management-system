package library.borrower;

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
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class BorrowerService {

	@Autowired
	private BorrowerRepository borrowerRepo;

	@RequestMapping("/loadborrower")
	public List<Borrower> loadBorrowers() {

		String fileName = "src/main/resources/borrowers.csv";
		Borrower borrower = null;
		List<Borrower> borrowers = new ArrayList<Borrower>();

		try {

			CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
			CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).withCSVParser(parser).build();
			
			String[] line = null;
			
			while((line = reader.readNext()) != null) {
				borrower = new Borrower(Integer.parseInt(line[0]), line[1], line[2], line[3], line[4], line[5], line[6], line[7]);
				borrowers.add(borrower);
			}
			borrowerRepo.insertBorrowers(borrowers);
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}

		return borrowers;
	}

	@RequestMapping(value = "/addborrower", method = RequestMethod.POST)
	public Object[] addBorrower(
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="lastName") String lastName,
			@RequestParam(value="email") String email,
			@RequestParam(value="address") String address,
			@RequestParam(value="city") String city,
			@RequestParam(value="state") String state,
			@RequestParam(value="phone") String phone) {
				
				Borrower borrower = new Borrower(0, firstName, lastName, email, address, city, state, phone);
				int status = borrowerRepo.insertBorrower(borrower);
				Object[] object = new Object[] {status};
				return object;
			}

}
