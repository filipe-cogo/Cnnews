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
        $("#queryResult").html("");
	$.ajax({
        	url : 'Search',
                dataType: 'json',
		data : {
                    q : $("#querySelect").val()
		},
                success : function(data) {
                    if (data == null || data.length == 0){
                        $("#queryResult").html("No results found.");
                    } else{
                        $.each(data, function(i, item) {
                            var id = "";
                            var link = "";
                            var title = "";
                            var description = "";     
                            
                            $.each(item, function(key, value) {
                                if (key == "id")
                                    id = value;
                                if (key == "link")
                                    link = value;
                                if (key == "title")
                                    title = value;
                                if (key == "description")
                                    description = value;                                
                            });
                                                        
                            //create new html element for each news
                            var element = "<div class=\"result\" id=\"result" + i + "\"><div class=\"title\" id=\"title" + i + "\"><a target=\"_blank\" href=\"" + link + "\">" + title + "</a></div><div class=\"description\" id=\"description" + i + "\">" + description + "</div></div>";
                            $("#queryResult").append(element);
                        });
                    }
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