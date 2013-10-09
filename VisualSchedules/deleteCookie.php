<?php
/*Meant to delete cookie by setting to a negative time, but since the cookie is saving in the state instead of to
the url it doesn't really get use.
Willis Kennedy created 12/7*/
session_start();
setcookie('willis', "", time()-3600, '/~ankhar/demo2noJqueryMobile', 'wwwp.cs.unc.edu');
?>