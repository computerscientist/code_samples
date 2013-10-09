<DOCTYPE html>
<html lang = "en">
<head>
	<meta charset = "utf-8">
	<title>PHP/MySQL tests</title>
</head>
<body>
	<div id = "wrapper">
	<div id = "content">
	<form enctype="multipart/form-data" action="insert.php" method="POST" name = "changer">
		<input name="MAX_FILE_SIZE" value = "1000000" type = "hidden">
		Please choose a file: <br><input name="image" accept = "image/jpeg" type="file"><br>
		Please choose some tags:<input name="tags" type ="text"><br><br>
		<input value="Submit" type="submit">
	</form> 
	</div>
	</div>
</body>
</html>