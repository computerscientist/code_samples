$(document).ready(function(){
      $(".search").click(function(){
        /*search.php actually matches by the keyword, and search.js retrieves those matches
        and displays them.
        Willis Kennedy created 10/29 modified through 11/9*/
	if($('.keywords').val().indexOf("<")>=0 || $('.keywords').val().indexOf(">")>=0)
	{
	    alert("Search query cannot contain '<' or '>'!");
	    return;
	}
        $.post("search.php", {keywords: $(".keywords").val().trim()}, function(data){
          $i = 0;
          $("#image_display").empty();
          $.each(data, function(){
	    edited_data=data[$i].toString().replace("\'", "&#39;");
            $("#image_display").append("<img src = 'upload/" + edited_data + "' class = 'images' alt = 'fun'>");
            $i++;
          });
        }, "json");
      });
    });
