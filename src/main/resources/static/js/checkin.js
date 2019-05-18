window.onload = attach;

var loanId = [];

function attach() {
	$("#searchLoaned").on("click", searchLoaned);
}

$("#searchQueryLoaned").on("keyup", function(event) {
	if(event.keyCode == 13) {
		searchLoaned();
	}
});

function searchLoaned() {
	var searchQuery = $("#searchQueryLoaned").val();
	console.log(searchQuery);
	
	$.ajax({
		type: "GET",
		url: "/searchloaned",
		data: {
			query: searchQuery
		},
		dataType: "json",
		success: function(data) {
			console.log(data.length);
			var res = '<input type="button" id="checkin" value="Check In" />';
			for(var i = 0; i < data.length; i++) {
				loanId.push(data[i][0]);
				var div = '';
				div = '<div class="book">';
				div += '<img src="' + data[i][4] + '" alt="' + data[i][2] + '" />';
				div += '<input type="checkbox" id="' + data[i][0] + '" />';
				div += '<label for="'+ data[i][0] + '"><h4 class="title">' + data[i][3] + '</h4></label>';
				div += '<p class="authors">';
				for(var j = 0; j < data[i][7].length; j++) {
					div += data[i][7][j];
					div += ', ';
				}
				div = div.slice(0, -2);
				div += '</p>';
				div += '<p><span class="isbn13"><span class="isbn">ISBN13:</span>' + data[i][2] + '</span><span class="isbn10"><span class="isbn">ISBN10:</span>' + data[i][1] + '</span></p>'; 
				div += '<p><span class="pages"><span class="pagepub">Pages:</span>' + data[i][6] + '</span><span class="publisher"><span class="pagepub">Publisher:</span>' + data[i][5] + '</span></p>'; 
				div += '</div>';
				res += div;
			}
			if(data.length > 0){
				$("#content").html(res);
				$("#checkin").on("click", checkIn);
			}
			console.log(loanId);
		}
	});
}

function checkIn() {
	var selectedLoanIds = [];
	var temp = loanId;
	var len = temp.length;
	for(var i = 0; i < len; i++) {
		var x = "#" + temp[i];
		if($(x).prop("checked")) {
			selectedLoanIds.push(temp[i]);
		}
	}
	console.log(JSON.stringify(selectedLoanIds));
	$.ajax({
		headers: {
			'Accept': 'application/json',
			'Content-type': 'application/json'
		},
		type: "POST",
		url: "/checkbooksin",
		data: JSON.stringify(selectedLoanIds),
		dataType: "json",
		success: function(data){
			var res = "";
			if(data){
				res = "Checked In!";
			}
			else{
				res = "Not checked in";
			}
			$("#content").html(res);
		}
	});
}
