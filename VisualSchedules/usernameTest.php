<?php
/*Creates the session for a user to stay logged in so it doesn't have to
be relogged in all the time
Willis Kennedy created 12/3 modified through 12/5*/
session_start();
$user = $_GET['user'];
header('Content-type: application/json');
$_SESSION['user'] = $user;
//$auth_cookie_val = md5($_SESSION['user'] . $_SERVER['REMOTE_ADDR']);
//setcookie('willis', $auth_cookie_val, time()+3600, '/~ankhar/demo2noJqueryMobile', 'wwwp.cs.unc.edu');
print(json_encode(true));
?>