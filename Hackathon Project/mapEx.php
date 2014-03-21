<?php

$input = 'player blue';

$cmd = preg_split ("/ /", $input);

print("cmd is {$cmd[0]}");

$colors = [
        "player" => [ "blue", "yellow", "green", "red", "black", "white"],
        "background" => [ "blue", "yellow", "green", "red", "black" ]
        ];

$match1 = preg_grep("/$cmd[0]/", $colors);

print ("matches1 is {$match1[0]}");

//$match2 = preg_grep("/$cmd[1]/", $colors[$match1]);

//print ("matches is {$match2[1]}");
?>
