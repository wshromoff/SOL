
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
//			$('#summaryCount').html(splitArray[0]);
			$('#content').html(splitArray[1]);
//			alert(splitArray[0]);
//			alert(splitArray[1]);
			displayCount("#summaryCount", splitArray[0]);
		}
	});
	
}

function initSearch()
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
//		type : 'post',
//		data : {
//			query : $('#jemmSearch').val()
//		},
		success : function(responseText) {
			var splitArray = responseText.split(",");
//			$('#summaryCount').html(splitArray[0]);
			$('#content').html(splitArray[1]);
			displayCount("#resultCount", splitArray[0]);
		}
	});
}

function assetSearch()
{
	var docSelected = document.querySelector('input[name="docSearch"]:checked').value;
//	alert(docSelected);
	
	$.ajax({
		url : 'search',
		type : 'post',
		data : {
			query : $('#jemmSearch').val(),
			document : docSelected
		},
		success : function(responseText) {
			var splitArray = responseText.split(",");
//			$('#summaryCount').html(splitArray[0]);
//			$('#content').html(splitArray[1]);
			displayCount("#resultCount", splitArray[0]);
		}
	});
}

function newSearch()
{
//	alert("HEW ");
	$.ajax({
		url : 'search',
		type : 'post',
		data : {
			newSearch: 'true'
//			query : $('#jemmSearch').val()
		},
		success : function(responseText) {
			var splitArray = responseText.split(",");
//			$('#summaryCount').html(splitArray[0]);
			$('#content').html(splitArray[1]);
			displayCount("#resultCount", splitArray[0]);
		}
	});
	
}

function searchResults()
{
	clearTabInterval();
	hidedet1 = true;

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

$('jemmSearch').on('keyup', function()
{
//	alert("HI");
    if (this.value.length > 1) {
         // do search for this.value here
    }
});

var divShowing = null;
function showPartDetails(whatToShow)
{
//	if (divShowing != null)
//	{
//		document.getElementById(divShowing).style.display = 'none';
//	}
//	alert(whatToShow);
//	whatToShow.style.visibility='visible';
	document.getElementById(whatToShow).style.display = 'block';
//	divShowing = whatToShow;
}

function clearPartDetails(whatToShow)
{
//	if (divShowing != null)
//	{
		document.getElementById(whatToShow).style.display = 'none';
//	}
//	alert(whatToShow);
//	whatToShow.style.visibility='visible';
//	document.getElementById(whatToShow).style.display = 'block';
//	divShowing = whatToShow;
}

var hidedet1 = true;
function assetDetails(assetID)
{
	$.ajax({
		url : 'AssetDetails',
//		type : 'post',
		data : {
			ID : assetID
		},
		success : function(responseText) {
			$('#det1').html(responseText);
			if (hidedet1 == false)
			{	// Show detail actions
				document.getElementById('detailActions').style.display = 'block';
			}
			else
			{	// Hide detail actions
				document.getElementById('detailActions').style.display = 'none';
			}
			document.getElementById('det1').style.display = 'block';
			

//			var splitArray = responseText.split(",");
////			$('#summaryCount').html(splitArray[0]);
////			$('#content').html(splitArray[1]);
//			displayCount("#resultCount", splitArray[0]);
		}
	});
}

function assetDetailsHide()
{
	if (hidedet1 == true)
	{
		document.getElementById('det1').style.display = 'none';
	}
}

// Toggle the locking of the details area of the page
function lockDet1()
{
	if (hidedet1 == true)
	{
		hidedet1 = false;
	}
	else
	{
		hidedet1 = true;
	}

}
