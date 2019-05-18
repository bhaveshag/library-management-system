package library.borrower;

public class Borrower {

	private int cardId;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String city;
	private String state;
	private String phone;

	public Borrower(int cardId, String firstName, String lastName, String email, String address, String city, String state, String phone) {
		this.cardId = cardId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.city = city;
		this.state = state;
		this.phone = phone;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getCardId() {
		return cardId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}
	
	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getPhone() {
		return phone;
	}

}
