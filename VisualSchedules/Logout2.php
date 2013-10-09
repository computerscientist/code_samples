<?php
session_start(); //Remind server of current session
session_unset(); //Free all session variables
session_destroy(); //Destroy the current session

header('Content-type: application/json');
print(json_encode(true));
?>
