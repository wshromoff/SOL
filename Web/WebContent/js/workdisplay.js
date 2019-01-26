
$(document).ready(function()
{
//	alert("WHATUP");
	loadData();
	setInterval(loadData, 65000);
});

function loadData()
{
//	alert("HELLO");
	// Make ajax call for current work assets
	$.ajax({
		url : 'CurrentAssetsServlet',
//		data : {
//			userName : $('#userName').val()
//		},
		success : function(responseText) {
			$('#ajaxCurrentAssetsResponse').html(responseText);
		}
	});
	
	// Make ajax call for processed assets
	$.ajax({
		url : 'CompletedAssetsServlet',
//		data : {
//			userName : $('#userName').val()
//		},
		success : function(responseText) {
			$('#ajaxCompletedAssetsResponse').html(responseText);
		}
	});

}