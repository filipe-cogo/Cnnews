$(document).ready(function() {
    //action for sync button
    $("#sync").click(function() {
                $(document).ajaxStart(function(){
                    $("#sync").prop( "disabled", true);
                });
                $(document).ajaxComplete(function(){
                    $("#sync").prop( "disabled", false);
                });
		$.ajax({
			url : 'Crawler',
                        data: {},
			success : function() {
                                //shows search form
                                $("#searchDiv").css("visibility", "visible");
			},
                        error : function(errorText){
                            $("#errorDiv").html(errorText);
                            $("#errorDiv").show(5000, function(){
                                $("#errorDiv").html("");
                            });
                        }
		});
	});
    
    //action for search button
    $("#search").click(function() {
	$.ajax({
        	url : 'Search',
                dataType: 'json',
		data : {
                    q : $("#querySelect").val()
		},
                success : function(data) {
                    //alert(JSONObject);
                    $.each(data, function(i, item) {
                        $.each(item, function(key, value) {
                            alert(key + ":" + value);
                            $("#"+key).html(value);
                        }); 
                    });
                    //$("#queryResult").text(data);
                },
                error : function(errorText){
                        $("#errorDiv").html(errorText);
                        $("#errorDiv").show(5000, function(){
                        $("#errorDiv").html("");
                    });
                }
            });
	});
});