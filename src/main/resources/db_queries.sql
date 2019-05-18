CREATE TABLE book
(
   isbn10 VARCHAR(36) NOT NULL,
   isbn13 VARCHAR(36) NOT NULL,
   title VARCHAR(256) NOT NULL,
   cover VARCHAR(100) NOT NULL,
   publisher VARCHAR(100) NOT NULL,
   pages INT NOT NULL,

   PRIMARY KEY(isbn10, isbn13)
);


CREATE TABLE author
(
   author_id INT AUTO_INCREMENT,
   name VARCHAR(100) NOT NULL,

   PRIMARY KEY(author_id)
);


CREATE TABLE book_authors
(
   author_id INT NOT NULL,
   isbn13 VARCHAR(36) NOT NULL,

   PRIMARY KEY(isbn13)
);


CREATE TABLE book_loan
(
   loan_id INT AUTO_INCREMENT,
   isbn13 VARCHAR(36) NOT NULL,
   card_id INT NOT NULL,
   date_in VARCHAR(36),
   date_out VARCHAR(36) NOT NULL,
   due_date VARCHAR(36),

   PRIMARY KEY(loan_id)
);


CREATE TABLE borrower
(
	card_id INT AUTO_INCREMENT,
   first_name VARCHAR(36) NOT NULL,
   last_name VARCHAR(36) NOT NULL,
   email VARCHAR(36) NOT NULL,
   address VARCHAR(100) NOT NULL,
   city VARCHAR(36) NOT NULL,
   state VARCHAR(36) NOT NULL,
   phone VARCHAR(16) NOT NULL,

   PRIMARY KEY(card_id)
);

CREATE TABLE fine
(
   
   amount DECIMAL NOT NULL,
   paid BIT NOT NULL,

   PRIMARY KEY(load_id)
);