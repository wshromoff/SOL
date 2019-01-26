
var selected = null;
var count = 0;
// This interval will run continuously to update the tab totals.  It's started when the page is loaded
var totalsInterval = null;

$(document).ready(function()
{
//			alert("WHATUP");
//			loadData();
	totalsInterval = setInterval(getTabTotals, 15000);
		});

// getTabTotals - Generates all the tab totals and displays their values
function getTabTotals()
{
	$.ajax({
		url : 'tabTotals',
		success : function(responseText) {
			var splitArray = responseText.split(",");
			displayCount("#summaryCount", splitArray[0]);
			displayCount("#resultCount", splitArray[1]);
		}
	});

//	alert(count);
//	count++;
//	if (count > 5)
//	{
//		clearInterval(totalsInterval);
//	}
}
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
