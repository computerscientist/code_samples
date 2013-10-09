<?php
	session_start();

	$user=isset($_SESSION['user']) ? $_SESSION['user'] : '';
	print $user;
?>