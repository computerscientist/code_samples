<?php
session_start();
?>

<!--Form to upload a picture:
Willis Kennedy created 10/2 modified until 11/9-->
<!DOCTYPE html>
<html>
<head>
    <title>Upload a Picture</title>
    <meta charset="utf-8" >
    <link rel="stylesheet" href="stylesheet.css">
    <style>
    	input[type='text']:focus{
			border: 2px solid orange;
		}
    </style>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <script>
	$(document).ready(function(){

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

	    if("<?php echo $_GET['error'] ?>"!=="")
		$("#errorDiv").html("<?php echo '<p>' . $_GET['error'] . '</p>' ?>");
	});

    function checkTags()
    {
	var tagString=document.forms["changer"]["tags"].value;
	if(/[^a-zA-Z0-9\s,.]/.test(tagString))
	{
	    alert("Tags must only contain letters, numbers, spaces, commas, and periods!");
	    document.forms["changer"]["tags"].value="<?php echo $_GET['tags'] ?>";
	    return false;
	}
	return true;
    }
    </script>
</head>
<body>
<div id="welcomePlaceHolder"></div>
<div id="logoutPlaceHolder"></div>
<div class = "header"><h1>Upload A Picture</h1></div>
	<div id = "wrapper">
	<div class = "content">
	<!--Sends the basic info to insert.php to be processed for upload.-->
	<form class="form" enctype="multipart/form-data" action="insert.php" method="POST" name = "changer" onsubmit="return checkTags()">
		<input name="MAX_FILE_SIZE" value = "1000000" type = "hidden">
		Please choose a file: <br><input name="image" accept = "image/*" type="file"><br>
		Please choose some tags:<input name="tags" type ="text" value="<?php echo $_GET['tags'] ?>"><br><br>
		<input value="Submit" type="submit">
	</form>
	<div>
		<div id="errorDiv"></div>
		<br><br>
		<a href = "index.html" class="button"  data-theme="e">Back Home</a></div> 
	</div>
	</div>
</body>
</html>
