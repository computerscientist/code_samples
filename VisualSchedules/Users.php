<?php
/*A file to deal with requests regarding users. It will select, delete, and insert into the
user table to make the necessary changes by call.
Willis Kennedy created 12/5 modified through 12/8
*/
$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
if($_SERVER['REQUEST_METHOD'] == 'GET'){
	if(!isset($_SERVER['PATH_INFO'])){
		$result = $mysqli->query("SELECT user FROM users");
		$users = array();
		if($result){
			while($data = $result->fetch_row()){
				$users[] = $data[0];
			}
	}
	header("Content-type: application/json");
	print(json_encode($users));
	exit();
	} else {
		$user = substr($_SERVER['PATH_INFO'], 1); //Leave out '/' at beginning of PATH_INFO
		if(isset($_GET['delete'])){
			$userDeleteStmt=$mysqli->prepare("DELETE FROM users WHERE user=?");
			$userDeleteStmt->bind_param('s', $user);
			$userDeleteStmt->execute();
			$userDeleteStmt->close();

			$scheduleDeleteStmt=$mysqli->prepare("DELETE FROM tbl_sched WHERE user=?");
			$scheduleDeleteStmt->bind_param('s', $user);
			$scheduleDeleteStmt->execute();
			$scheduleDeleteStmt->close();

			header("Content-type: application/json");
			print(json_encode(true));
			exit();
		}
	}
} else if($_SERVER['REQUEST_METHOD'] == 'POST') {
	if(!isset($_SERVER['PATH_INFO'])){
			if(!isset($_POST['user']) || trim($_POST['user'])=='')
			{
				header("HTTP/1.1 400 Bad Request");
				print "Error: Must type in a user name!";
				exit();
			}
			$user=trim($_POST['user']);

			$duplicateDetectionStmt=$mysqli->prepare("SELECT user FROM users WHERE user=?");
                        $duplicateDetectionStmt->bind_param('s', $user);
                        $duplicateDetectionStmt->execute();
			$duplicateDetectionStmt->store_result();
			$num_rows=$duplicateDetectionStmt->num_rows;
                        $duplicateDetectionStmt->close();

			if($num_rows>0)
			{
				header("HTTP/1.1 400 Bad Request");
				print "Error: User '$user' already exists!";
				exit();
			}

			$insertStmt=$mysqli->prepare("INSERT INTO users (user) VALUES (?)");
                        $insertStmt->bind_param('s', $user);
                        $insertStmt->execute();
                        $insertStmt->close();
		exit();
	}
}
?>
