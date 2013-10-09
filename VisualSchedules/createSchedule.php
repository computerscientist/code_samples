<?php
  /*This page creates the actual schedule for AdminPictures.php and posts it to the database
  was made before schedules.php and thus used as the primary create option
  Willis Kennedy created 11/9 modified through 11/30*/
  		//error_reporting(E_ALL);
		//ini_set('display_errors', 1);
	  $mysqli = new mysqli("localhost", "root", "", "comp523p1db");
          if(mysqli_connect_errno()) {
        	  echo "Connection Failed: " . mysqli_connect_errno();
        	  die();
    	  }

          $pictures = array();
		  if($_POST['pic1']=='')
		  {
			header("HTTP/1.1 400 Bad Request");
			header("Location: AdminPictures.php?pic1=" . ($_POST['pic1']!='' ? "upload%2F" : "") . urlencode($_POST['pic1']) . "&pic2=" . ($_POST['pic2']!='' ? "upload%2F" : "") . urlencode($_POST['pic2']) . "&pic3=" . ($_POST['pic3']!='' ? "upload%2F" : "") . urlencode($_POST['pic3']) . "&pic4=" . ($_POST['pic4']!='' ? "upload%2F" : "") . urlencode($_POST['pic4']) . "&chooseType=" . $_POST['chooseType'] . "&tags=" . urlencode($_POST['tags']) . "&user=" . urlencode($_POST['user']) . "&numPictures=" . $_POST['numPictures'] . "&error=Error:%20Schedule%20missing%20pictures%21");
			//print json_encode("Error: Schedule missing pictures!");
			exit();
		  }

		  if(trim($_POST['tags'])=='')
		  {
			$needles=array("\"", "'");
			$replacements=array("&quot;", "&#39;");

			header("HTTP/1.1 400 Bad Request");
			header("Location: AdminPictures.php?pic1=" . ($_POST['pic1']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic1'])) . "&pic2=" . ($_POST['pic2']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic2'])) . "&pic3=" . ($_POST['pic3']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic3'])) . "&pic4=" . ($_POST['pic4']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic4'])) . "&chooseType=" . $_POST['chooseType'] . "&tags=" . urlencode($_POST['tags']) . "&user=" . urlencode($_POST['user']) . "&numPictures=" . $_POST['numPictures'] . "&error=Error:%20Schedule%20missing%20title%21");
			//print json_encode("Error: Schedule missing title!");
			exit();
		  }
		  if(!ctype_alnum(str_replace("'", "", str_replace(' ', '', $_POST['tags']))))
		  {
			$needles=array("\"", "'");
			$replacements=array("&quot;", "&#39;");

			header("HTTP/1.1 400 Bad Request");
			header("Location: AdminPictures.php?pic1=" . ($_POST['pic1']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic1'])) . "&pic2=" . ($_POST['pic2']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic2'])) . "&pic3=" . ($_POST['pic3']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic3'])) . "&pic4=" . ($_POST['pic4']!='' ? "upload%2F" : "") . urlencode(str_replace($needles, $replacements, $_POST['pic4'])) . "&chooseType=" . $_POST['chooseType'] . "&tags=" . urlencode($_POST['tags']) . "&user=" . urlencode($_POST['user']) . "&numPictures=" . $_POST['numPictures'] . "&error=Error:%20Schedule%20title%20must%20only%20contain%20letters%2C%20numbers%2C%20spaces%2C%20and%20apostrophes%21");
			//print json_encode("Error: Schedule title must only contain letters, numbers, spaces, and apostrophes!");
			exit();
		  }

          $pictures[0] = str_replace("'", "''", urldecode($_POST['pic1']));
          $pictures[1] = str_replace("'", "''", urldecode($_POST['pic2']));
          $pictures[2] = str_replace("'", "''", urldecode($_POST['pic3']));
          $pictures[3] = str_replace("'", "''", urldecode($_POST['pic4']));
          $tags = str_replace("'", "''", trim(urldecode($_POST['tags'])));
		  $user=urldecode($_POST['user']);
		  $chooseType=isset($_POST['chooseType']) ? $_POST['chooseType'] : 0;

		  $duplicateDetectionStmt=$mysqli->prepare("SELECT tags FROM tbl_sched WHERE user=? AND tags=?");
                  $duplicateDetectionStmt->bind_param('ss', $user, $tags);
                  $duplicateDetectionStmt->execute();
		  $duplicateDetectionStmt->store_result();
		  $num_rows=$duplicateDetectionStmt->num_rows;
                  $duplicateDetectionStmt->close();

		  if($num_rows>0)
		  {
			$i=0;

			while($num_rows>0)
			{
				$i++;
				$tags_appended=$tags."(".$i.")";

				$duplicateDetectionStmt=$mysqli->prepare("SELECT tags FROM tbl_sched WHERE user=? AND tags=?");
                  		$duplicateDetectionStmt->bind_param('ss', $user, $tags_appended);
                  		$duplicateDetectionStmt->execute();
                  		$duplicateDetectionStmt->store_result();
                  		$num_rows=$duplicateDetectionStmt->num_rows;
                  		$duplicateDetectionStmt->close();
			}

			$tags.="(".$i.")";
		  }

	    $creationStmt=$mysqli->prepare("INSERT INTO tbl_sched (image_one, image_two, image_three, image_four, user, tags, type) VALUES ('$pictures[0]', '$pictures[1]', '$pictures[2]', '$pictures[3]', '$user', '$tags', '$chooseType');");
            $creationStmt->bind_param('sssssss', $pictures[0], $pictures[1], $pictures[2], $pictures[3], $user, $tags, $chooseType);
	    $creationStmt->execute();
            $creationStmt->close();

            header("Location: AdminManageSchedule.php");
?>
