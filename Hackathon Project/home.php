<html>
 <head>
  <title>EMAIL GAME</title>
 </head>
 <body>
 
 <?php
   //FOR SENDGRID
   include 'sendgrid-php/lib/SendGrid.php';
   require_once 'unirest-php/lib/Unirest.php';
   require_once 'sendgrid-php/lib/SendGrid.php';
   require_once 'smtpapi-php/lib/Smtpapi.php';
   SendGrid::register_autoloader();
   Smtpapi::register_autoloader();
   
   
   //READ JSON
   $str_data = file_get_contents("data.json");
   $data = json_decode($str_data,true);
   
   $username = $data["login"]["username"];
   $password = $data["login"]["password"];
   $sendTo = 'EFox2413@gmail.com';
   $sendFrom = $data["send"]["from"];
   
   //INITIALIZE SENDGRID
   $sendgrid = new SendGrid($username, $password);
   
   //MAKE AND SEND EMAIL
   $mail = new SendGrid\Email();
   $mail->
      addTo($sendTo)->
      setFrom($sendFrom)->
      setSubject('Subject goes here')->
      setText('Hello World!')->
      setHtml('<strong>Hello World!</strong>');
   
   $sendgrid->send($mail);
         
   echo 'PAGE';
   
?>

 </body>
</html>