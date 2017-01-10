$(document).ready(function() {
	$("#q").blur(function() {
		$.ajax({
			url : 'Crawler',
			data : {
				q : $("#q").val()
			},
			success : function(responseText) {
				$("#ajaxGetUserServletResponse").text(responseText);
			}
		});
	});
});