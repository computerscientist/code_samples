<DOCTYPE html>
<html lang = "en">
<head>
	<meta charset = "utf-8">
	<title>PHP/MySQL tests</title>
</head>
<body>
	<div id = "wrapper">
	<div id = "content">
	<?php
	error_reporting(E_ALL);
	ini_set("display_errors", 1);
	$dhandle = opendir('upload');
	$files = array();
	while(false != ($fname = readdir($dhandle))){
		if(($fname !='.')&&($fname != '..')){
			$files[] = $fname;
		}
	}
	closedir($dhandle);

	$username = "root";
	$password = "";
	$host = "localhost";
	$database = "comp523p1db";

	mysql_connect($host, $username, $password) or
	die("Can not connect to database: ".mysql_error());
	mysql_select_db($database) or die("Can not select the database: ".mysql_error());
	//get decoded image data from database 

	$data = mysql_query("SELECT image_url FROM tbl_images") or die(mysql_error());
	$result = count($files);

	for($i = 0; $result>$i; $i++){
		echo "<image src=upload/".$files[$i]."> <br>";
	}
	
	//end 
	?>
	
	</div>
	</div>
</body>
</html>