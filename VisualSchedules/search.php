<?php
/*
Selects all the images in the database that match the search keywords gotten
from a post.
Willis Kennedy created 10/29 edited through 11/9
*/
//error_reporting(E_ALL);
//ini_set('display_errors', 1);
    $mysqli = new mysqli("localhost", "root", "", "comp523p1db");
    if(mysqli_connect_errno()) {
        echo "Connection Failed: " . mysqli_connect_errno();
        die();
    }
    $search = "";
    if(isset($_POST['keywords']))
    	$search = "%" . str_replace("'", "''", $_POST['keywords']) . "%";
    $searchStmt=$mysqli->prepare("SELECT tbl_images.image_url FROM tbl_images, tbl_tags WHERE tag_name LIKE ? AND tag_id = image_id");
    $searchStmt->bind_param('s', $search);
    $searchStmt->bind_result($data);
    $searchStmt->execute();
    //$searchStmt->bind_result($data);
    //$searchStmt->store_result();
    $images = array();
	$i=0;
    while($searchStmt->fetch()){
        $dataClone = false;
        while($i<count($images)){
            if($images[$i] == $data){
                $dataClone = true;
                break;
            }
        $i++;
        }
        if(!$dataClone){
            if(file_exists("upload/".$data)&&!empty($data)){
                $images[] = array($data);
            }
        }
    }
    $searchStmt->close();
    echo json_encode($images);
?>
