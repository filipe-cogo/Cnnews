$(document).ready(function() {
    $("#sync").click(function() {
		$.ajax({
			url : 'Crawler',
			data : {
				action : "sync"
			},
			success : function(responseText) {
				$("#title").html(responseText);
			}
		});
	});
    
	$("#q").blur(function() {
		$.ajax({
			url : 'Crawler',
			data : {
				q : $("#q").val()
			},
			success : function(responseText) {
				$("#ajaxServletResponse").text(responseText);
			}
		});
	});
});