<?php
/*read user just selects all the users in the database specifically for select.js
Willis created 12/3*/
	$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
	$result = $mysqli->query("SELECT user FROM users");
	$users = array();
	if($result){
		while($data = $result->fetch_row()){
			$users[] = $data[0];
		}
	}
	print(json_encode($users));
?>