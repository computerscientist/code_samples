<?php


//GET DATA FROM EMAIL
$from = $_POST["from"];          //email of sender
$subject = $_POST["subject"];    //subject of email
$cmd = strtolower($_POST["text"]);          //text msg sent
$isValidCmd = true;           //flag for valid cmd

//UPDATE EVENT LOG
$file = 'eventlog.txt';
$contents =  print_r($_POST, true);
file_put_contents($file, $contents, FILE_APPEND);
$contents = "";

//READ JSON
$str_data = file_get_contents("data.json");
$data = json_decode($str_data,true);

//PARSE COMMAND
$cmd1 = "";
$cmd2 = "";

$cmds = explode(" ", $cmd, 2);
$cmd1 = $cmds[0];
if(sizeof($cmds) == 2)
   $cmd2 = $cmds[1];

//reset first commands
$data['left']=false;
$data['right']=false;
$data['up']=false;
$data['down']=false;

$data['normal'] = false;
$data['hacknc'] = false;
$data['doge'] = false;
$data['zelda'] = false;

//DOUBLE COMMANDS
if($cmd1 == 'player') {
//RESET
  $data[$cmd1]['red'] = false;
  $data[$cmd1]['blue'] = false; 
  $data[$cmd1]['green'] = false;
  $data[$cmd1]['yellow'] = false;
  $data[$cmd1]['black'] = false;
  $data[$cmd1]['white'] = false; 
  $data[$cmd1]['orange'] = false;
  $data[$cmd1]['purple'] = false;
  $data[$cmd1]['normal'] = false;
  $data[$cmd1]['hacknc'] = false;
  $data[$cmd1]['doge'] = false;
  $data[$cmd1]['zelda'] = false;
  
   //CHECK
   if($cmd2 == 'red') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'blue') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'green') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'yellow') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'black') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'white') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'orange') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'purple') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'normal') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'pokemon') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'hacknc') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'doge') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'zelda') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'green') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'green') {
     $data[$cmd1][$cmd2] = true;
   } else {
    //$isValidCmd = false;
  }
}
if($cmd1 == 'wall') {
//RESET
  $data[$cmd1]['red'] = false;
  $data[$cmd1]['blue'] = false; 
  $data[$cmd1]['green'] = false;
  $data[$cmd1]['yellow'] = false;
  $data[$cmd1]['black'] = false;
  $data[$cmd1]['white'] = false; 
  $data[$cmd1]['orange'] = false;
  $data[$cmd1]['purple'] = false;
  $data[$cmd1]['normal'] = false;
  $data[$cmd1]['hacknc'] = false;
  $data[$cmd1]['doge'] = false;
  $data[$cmd1]['zelda'] = false;
  
   //CHECK
   if($cmd2 == 'red') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'blue') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'green') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'yellow') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'black') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'white') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'orange') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'purple') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'normal') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'pokemon') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'hacknc') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'doge') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'zelda') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'green') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'green') {
     $data[$cmd1][$cmd2] = true;
   } else {
    //$isValidCmd = false;
  }
}
if($cmd1 == 'ground') {
//RESET
  $data[$cmd1]['red'] = false;
  $data[$cmd1]['blue'] = false; 
  $data[$cmd1]['green'] = false;
  $data[$cmd1]['yellow'] = false;
  $data[$cmd1]['black'] = false;
  $data[$cmd1]['white'] = false; 
  $data[$cmd1]['orange'] = false;
  $data[$cmd1]['purple'] = false;
  $data[$cmd1]['normal'] = false;
  $data[$cmd1]['hacknc'] = false;
  $data[$cmd1]['doge'] = false;
  $data[$cmd1]['zelda'] = false;
  
   //CHECK
   if($cmd2 == 'red') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'blue') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'green') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'yellow') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'black') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'white') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'orange') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'purple') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'normal') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'pokemon') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'hacknc') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'doge') {
     $data[$cmd1][$cmd2] = true;
   } else if($cmd2 == 'zelda') {
     $data[$cmd1][$cmd2] = true;
   } 
}
if($cmd1 == 'left') {
  $data[$cmd1] = true;
} else if($cmd1 == 'right') {
  $data[$cmd1] = true;
} else if($cmd1 == 'up') {
  $data[$cmd1] = true;
} else if($cmd1 == 'down') {
  $data[$cmd1] = true;
} else if($cmd1 == 'normal') {
  $data[$cmd1] = true;
} else if($cmd1 == 'pokemon') {
  $data[$cmd1] = true;
} else if($cmd1 == 'hacknc') {
  $data[$cmd1] = true;
} else if($cmd1 == 'doge') {
  $data[$cmd1] = true;
} else if($cmd1 == 'zelda') {
  $data[$cmd1] = true;
} else {
   $isValidCmd = false;
}

//STORE JSON
$str_data = json_encode($data);
file_put_contents('data.json', $str_data);
file_put_contents('dataTemp.json', $str_data);

//UPDATE COMMAND LOG
$file = 'commandLog.txt';
$contents = print_r($cmd, true);
if($isValidCmd) {
   $contents = 'VALID COMMAND!! =>    ' . $contents . PHP_EOL;
} else {
   $contents = '----invalid---- =>    '  . $contents . PHP_EOL;
}

file_put_contents($file, $contents, FILE_APPEND);

//reply with HTTP 200 Response so SendGrid doesn't retry the post
header("HTTP/1.1 200 OK");
?>
