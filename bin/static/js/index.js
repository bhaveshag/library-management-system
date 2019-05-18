window.onload = attach;

function attach() {
	$("#search").on("click", search);
	$("#updateFine").on("click", updateFine);
}

$("#searchQuery").on("keyup", function(event) {
	if(event.keyCode == 13) {
		search();
	}
});

function search() {
	var searchQuery = $("#searchQuery").val();
	$.ajax({
		type: "GET",
		url: "/search",
		data: {
			query: searchQuery
		},
		dataType: "json",
		success: function(data) {
			console.log(data.length);
			var res = "";
			for(var i = 0; i < data.length; i++) {
				var availability = "";
				var availabilityClass = "";
				if(data[i]["availability"]){
					var availability = "Available";
					var availabilityClass = "available";
					var availCheckOut = 1;
				}
				else{
					availability = "Not Available";
					availabilityClass = "notavailable";
					var availCheckOut = 0;
				}
				var div = '';
				div = '<div class="book">';
				div += '<img src="' + data[i]["cover"] + '" alt="' + data[i]["isbn13"] + '" />';
				div += '<h4 class="title">' + data[i]["title"] + '</h4>';
				div += '<p class="authors">';
				for(var j = 0; j < data[i]["authors"].length; j++) {
					div += data[i]["authors"][j];
					div += ', ';
				}
				div = div.slice(0, -2);
				div += '</p>';
				div += '<p><span class="isbn13"><span class="isbn">ISBN13:</span><span id="' + i + '">' + data[i]["isbn13"] + '</span></span><span class="isbn10"><span class="isbn">ISBN10:</span>' + data[i]["isbn10"] + '</span></p>'; 
				div += '<p><span class="pages"><span class="pagepub">Pages:</span>' + data[i]["pages"] + '</span><span class="publisher"><span class="pagepub">Publisher:</span>' + data[i]["publisher"] + '</span></p>'; 
				div += '<p><span class="' + availabilityClass + '">' + availability + '</span></p>';
				div += '<button type="button" onclick="checkAvailability(' + i + ',' + availCheckOut + ')">Checkout</button>';
				div += '</div>';
				res += div;
			}
			$("#content").html(res);
		}
	});
}

function checkAvailability(id, availability) {
	var id = "#" + id;
	var span = $(id);
	var isbn13 = span.text();
	console.log(availability);
	console.log(isbn13);
	if(availability){
		var res = '<h4>Enter Borrower Id</h4>';
		res += '<input type="text" id="borrower" placeholder="Borrower Id" />';
		res += '<input type="hidden" id="isbn13" value="' + isbn13 + '" />';
		res += '<input type="button" id="checkout" value="Checkout" />';
		$("#content").html(res);
		$("#checkout").on("click", checkOut);
		$("#borrower").on("keyup", function(event) {
			if(event.keyCode == 13) {
				checkOut();
			}
		});
	}
	else{
		alert("Book not available");
	}
}

function checkOut() {
	var borrower = $("#borrower").val();
	var isbn13 = $("#isbn13").val();
	$.ajax({
		type: "GET",
		url: "/checkout",
		data: {
			borrower: borrower,
			isbn13: isbn13
		},
		dataType: "json",
		success: function(data) {
			if(data[0] == 1) {
				$("#content").html("<h5>Book checked out!</h5>");
			}
			else {
				var res = "";
				if(data[1] < 1){
					res += "<h5>Borrower does not exist</h5>";
				}
				if(!data[2]){
					res += "<h5>Book not available</h5>";
				}
				if(data[3] >= 3){
					res += "<h5>Borrower cannot borrow more than three books</h5>";
				}
				$("#content").html(res);
			}
		}
	});
}

function updateFine() {
	$.ajax({
		url: "/updatefine",
		dataType: "json",
		success: function(data){
			var res = '<div><h5>Fines Updated</h5>';
			res += '<select id="fineOption" name="fineOption">';
			res += '<option value="1">Unpaid</option>';
			res += '<option value="2">All</option>';
			res += '</select>';
			res += '<button type="button" id="displayFine">Display Fines</button></div>';
			$("#content").html(res);
			$("#displayFine").on("click", displayFine);
		}
	});
}

function displayFine() {
	var option = $("#fineOption").val();
	$.ajax({
		type: "GET",
		url: "/displayfine",
		data: {
			fineOption: option
		},
		dataType: "json",
		success: function(data){
			var res = '<table><tr><th>Borrower Id</th><th>Fine</th></tr>';
			for(var i = 0; i < data.length; i++){
				res += '<tr>';
				for(var j = 0; j < data[i].length; j++){
					res += '<td>' + data[i][j]+ '</td>';
				}
				res += '</tr>';
			}
			res += '</table>';
			var form = '<input type="text" id="payFineBorrower" placeholder="Enter Borrower Id" />'
			form += '<button type="button" id="payFine">Pay Fines</button><br />';
			$("#finesMsg").html(res);
			$("#finesForm").html(form);
			$("#payFine").on("click", payFine);
			$("#payFineBorrower").on("keyup", function(event) {
				if(event.keyCode == 13) {
					payFines();
				}
			});
		}
	});
}

function payFine() {
	var borrowerId = $("#payFineBorrower").val();
	$.ajax({
		type: "GET",
		url: "/payfines",
		data: {
			borrowerId: borrowerId
		},
		dataType: "json",
		success: function(data){
			console.log(data);
			var res = "";
			if(data == 0){
				res += "<h5>Borrower does not have any pending fines</h5>";
			}
			else{
				res += "<h5>Fines paid</h5>";
			}
			res += "Note: Borrower should return books before being able to pay for them";
			$("#finesMsg").html(res);
		}
	});
}
