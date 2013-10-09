<?php
session_start();
?>

<!--
Page to manage the users in the mysql database
Willis Kennedy created 12/3 modified through 12/8-->
<html>
<head>
    <!-- -->
    <title>Manage Users</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" >
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <link rel="stylesheet" href= "stylesheet.css">
    <link rel="stylesheet" href= "manage.css">
    <script type = "text/javascript">
    /*gets all the users and adds a delete button so that a user can be removed.*/
    	$(document).ready(function(){
    		$.get('Users.php', function(data){
    			var i = 0;
    			$.each(data, function(){
    				$('#users').append("<p>"+data[i]+" <button type = 'button' class = 'delete' value ="+data[i]+">Delete</button></p>");
    				i++;
    			});
    		});

		//Add login info as needed
		<?php echo 'var user="'.(isset($_SESSION['user']) ? $_SESSION['user'] : '').'";';?>

		if(user!='')
        	{
        		$("#welcomePlaceHolder").html("<p class=\"welcome\">Welcome, "+user+"</p>");
        		$("#logoutPlaceHolder").html("<a href=\"Logout.php\" class=\"logout\">Logout</a>");
        	}

		else
		{
			$("#welcomePlaceHolder").empty();
                	$("#logoutPlaceHolder").empty();
		}

    	});
        /*makes the newuser post to the mysql database*/
    	$(document).on('click', '#newUser', function(data){
		if(/[^a-zA-Z0-9]/.test($("#userName").val().trim()))
		{
			alert("User name must only contain letters and numbers!");
			return;
		}
    		var jqxhr=$.post("Users.php", {user: $("#userName").val()}, function(data)
    		{
    			$('#users').append("<p>"+$("#userName").val()+" <button type = 'button' class = 'delete' value ="+$("#userName").val()+">Delete</button></p>");
    		});

    		jqxhr.error(function(data)
    		{
    			alert(data.responseText);
    		});
    	});
        /*adds functionality to the delete button*/
    	$(document).on('click', '.delete', function(data){
    		var parentElement=$(this).parent();
        	$.get("Users.php/"+$(this).val(), {delete: true}, function(){
        		parentElement.remove();
        	});
    	});
        /*attempts to delete cookie, however currently the cookie seems to be saved in
        as a session variable rather than a permanent cookie, so it will not actually delete
        as it stands.*/
    	/*$(document).on('click', '#cookie', function(data){
    		$.get('deleteCookie.php', function(){
    			alert('Cookie deleted');
    		});
    	});*/
    </script>
    </head>
<body>
    <!--html largely filled in by JS-->
    <div class="page" id = "broken" data-theme="b"><div class = "content">
	<div id="welcomePlaceHolder"></div>
	<div id="logoutPlaceHolder"></div>
        <div class = "header"><h1>Manage Users</h1></div>
    	<div id = "users"></div><br><br>
    	<!-- <label>Delete current users cookie</label> -->
    	<!-- <input type = "submit" value = "Reset Cookie" id ="cookie"></input><br><br> -->
    	<label>Make a new user</label>
    	<input type = "text" id = "userName"></input>
    	<input type = "submit" value = "Make User" id = 'newUser'></input>
    <div><br><br><a href = "index.html" class="button">Back Home</a></div>
    </div>
	</div>
</body>
</html>
