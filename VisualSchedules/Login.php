<?php
session_start();
?>
<html lang=en>
<!--This is the initial splash page for the ipad. It pops up with a select menu of the
current users and a login button. However if a user has been cached for the session then
the user instead directly continues on to their schedules if any are available. 
Willis Kennedy created 12/3 modified through 12/8-->
<head>
<meta charset=utf-8>
<title>User login</title>
<link rel = stylesheet href = "Login.css" type = "text/css">
<link rel = 'stylesheet' href = "stylesheet.css" type = "text/css">
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.7/jquery-ui.min.js"></script>
<script src = "jquery.ui.touch-punch.min.js" type= "text/javascript"></script>
<!-- Select.js builds the user select menu -->
<script type = "text/javascript" src = "select.js"></script>
<script type="text/javascript">
/*Grabs the current user and relocates if a schedule is active*/
<?php echo 'var user = '. (isset($_SESSION['user']) ? json_encode($_SESSION['user']) : 'null').';';?>
$(document).ready(function(){
fillUsers();
if(user!=null){
	var jqxhr = $.get("Schedules.php", {user: user, active: 2}, function(data){
		if(data.type==0){
			window.location.replace("ScheduleDisplay.php");
		}
		else if(data.type==1){
			window.location.replace("Choice.php");
		}
	});
	jqxhr.error(function(){
		window.location.replace("ScheduleDisplay.php");
	});
}
/*When you sign in as a user it redirects you to the page with the current schedule*/
$('#userSubmit').click(function(){
	$.get("usernameTest.php", {user: $("select[name='user']").val()});
	var jqxhr = $.get("Schedules.php", {user: $("select[name='user']").val(), active: 2}, function(data){
		if(data.type==0){
			window.location.replace("ScheduleDisplay.php");
		}
		else if(data.type==1){
			window.location.replace("Choice.php");
		}
	});
	jqxhr.error(function(){
		window.location.replace("ScheduleDisplay.php");
	});
});
});
</script>

</head>

<body>
	<div class="content">
	<div class="header">
		<h1>Login Page</h1>
	</div>
	<div id = "wrapper" class="content">
		<div id = "login">
			<label>Select a user:</label>
			<select name = "user">
			</select>
			<input type="submit" value="Login" id = "userSubmit" name="submit">
		</div>
		<div>
	        <a href="index.html" class = "button">
	        	Back Home
	        </a>
    	</div>
	</div>
	</div>
</body>
</html>
