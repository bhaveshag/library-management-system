package library.book.loan;

public class BookLoan {

	private int loanId;
	private String ISBN13;
	private int cardId;
	private String dateOut;
	private String dueDate;
	private String dateIn;

	public BookLoan(int loanId, String ISBN13, int cardId, String dateOut, String dueDate, String dateIn) {
		this.loanId = loanId;
		this.ISBN13 = ISBN13;
		this.cardId = cardId;
		this.dateOut = dateOut;
		this.dueDate = dueDate;
		this.dateIn = dateIn;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public void setISBN13(String ISBN13) {
		this.ISBN13 = ISBN13;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public void setDateOut(String dateOut) {
		this.dateOut = dateOut;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}
	
	public int getLoanId(){
		return loanId;
	}

	public String getISBN13() {
		return ISBN13;
	}

	public int getCardId() {
		return cardId;
	}

	public String getDateOut() {
		return dateOut;
	}

	public String getDueDate() {
		return dueDate;
	}

	public String getDateIn() {
		return dateIn;
	}

}
