
var selected = null;
var count = 0;
// This interval will run continuously to update the tab totals.  It's started when the page is loaded
var totalsInterval = null;
// This interval is the tab interval currently running
var tabInterval = null;

$(document).ready(function()
{
//			alert("WHATUP");
//			loadData();
	totalsInterval = setInterval(getTabTotals, 150000);
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

// Clear the current tab interval if one is running.  All tab functions should call this to reset another tabs functionality
function clearTabInterval()
{
	if (tabInterval != null)
	{
		clearInterval(tabInterval);
	}
	tabInterval = null;
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
	clearTabInterval();
	tabInterval = setInterval(summary, 30000);
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
	clearTabInterval();

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
	clearTabInterval();

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

