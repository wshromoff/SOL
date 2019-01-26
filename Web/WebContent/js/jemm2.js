
var selected = null;

function clearSelected()
{
	if (selected != null)
	{
		document.getElementById(selected).className = "global";
	}
}

function setSelected()
{
	if (selected != null)
	{
		document.getElementById(selected).className = "active";
	}
}

function displayCount(area, value)
{
	if (value == '0')
	{
		$(area).html('');
	}
	else
	{
		$(area).html(value);
	}
	
}

function summary()
{
//	alert("HELLO " + selected);
	clearSelected();
	selected = "summary";
	setSelected();
//	document.getElementById(selected).className = "active";
//	alert("HELLO");
//	document.getElementById("logging").className = "global";
//	$('#content').html("WOW");
	// Make ajax call for current work assets
	$.ajax({
		url : 'summary',
//		data : {
//			userName : $('#userName').val()
//		},
		success : function(responseText) {
//			$('#content').html(responseText);
			var splitArray = responseText.split(",");
			$('#summaryCount').html(splitArray[0]);
			$('#content').html(splitArray[1]);
//			alert(splitArray[0]);
//			alert(splitArray[1]);
			displayCount("#summaryCount", splitArray[0]);
		}
	});
	
	$('#resultCount').html("123");
}


function assetSearch()
{
//	alert("HELLO2 " + selected);
	clearSelected();
	selected = "search";
	setSelected();
//	document.getElementById(selected).className = "active";
//	alert("HELLO");
//	document.getElementById("logging").className = "global";
//	$('#content').html("WOW");
	// Make ajax call for current work assets
	$.ajax({
		url : 'search',
//		data : {
//			userName : $('#userName').val()
//		},
		success : function(responseText) {
			$('#content').html(responseText);
		}
	});
}
function searchResults()
{
//	alert("HELLO2 " + selected);
	clearSelected();
	selected = "searchResults";
	setSelected();
//	document.getElementById(selected).className = "active";
//	alert("HELLO");
//	document.getElementById("logging").className = "global";
//	$('#content').html("WOW");
	// Make ajax call for current work assets
	$.ajax({
		url : 'searchResults',
//		data : {
//			userName : $('#userName').val()
//		},
		success : function(responseText) {
			$('#content').html(responseText);
		}
	});
}

