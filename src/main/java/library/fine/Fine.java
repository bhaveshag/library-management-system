package library.fine;

public class Fine {

	private int loanId;
	private java.math.BigDecimal amount;
	private Boolean paid;

	public Fine(int loanId, java.math.BigDecimal amount, Boolean paid) {
		this.loanId = loanId;
		this.amount = amount;
		this.paid = paid;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public int getLoanId() {
		return loanId;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public Boolean getPaid() {
		return paid;
	}

}
