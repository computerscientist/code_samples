<DOCTYPE html>
	<!--Place to insert uploaded files into the database and relocate uploaded files to web storage.
Willis Kennedy 10/1 modified through 12/7-->
<html lang = "en">
<head>
	<meta charset = "utf-8">
	<title>PHP/MySQL tests</title>
</head>
<body>
	<div id = "wrapper">
		<?php
		/*Inserts information from the form*/
		error_reporting(E_ALL); //Report all errors
		ini_set("display_errors", 1);
		/*You choose the target upload then md5 the file to give it a unique name based on
		the pictures contents. update the target upload to include the full name.*/
		$target = "upload/";
		$filehash = md5_file($_FILES['image']['tmp_name']);
		$name = basename($_FILES['image']['name']); //Get just trailing component of file name (not directory stuff...)
		$name = str_replace(" ", "_", $name);
		$target = $target . $filehash . $name;
		/*Grab the tags and make the image equal to the name*/
		$image = $filehash . $name;
		$tags = trim(urldecode($_POST['tags']));

		if(file_exists($target))
		{
			if($target=="upload/")
			{
				header("HTTP/1.1 400 Bad Request");
				header("Location: AdminUploadAPicture.php?tags=" . urlencode($_POST['tags']) . "&error=Must%20choose%20a%20file%20to%20upload%21");
				//print json_encode("Must choose a file to upload!");
				exit();
			}

			$i=0;
			$target_appended="";

			do
			{
				$i++;
				$target_appended=substr($target, 0, strripos($target, '.')) . "(" . $i . ")". substr($target, strripos($target, '.'));
			}
			while(file_exists($target_appended));

			$target=substr($target, 0, strripos($target, '.')) . "(" . $i . ")". substr($target, strripos($target, '.'));
			$image=substr($image, 0, strripos($image, '.')) . "(" . $i . ")". substr($image, strripos($image, '.'));
		}

		/*Move the uploaded file to the target location*/
		if(move_uploaded_file($_FILES['image']['tmp_name'], $target))
		{
			//Put file in database once it has been moved to its target
			$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
                	if(mysqli_connect_errno()) {
                        	echo "Uploaded image, but connection to database failed: " . mysqli_connect_errno();
                        	die();
                	}

			$imageStmt=$mysqli->prepare("INSERT INTO tbl_images (image_url) VALUES (?)");
			if(!$imageStmt)
                                die("Cannot insert image name into database");
			$imageStmt->bind_param('s', $image);
			$imageStmt->execute();
			$imageStmt->close();

			$tagStmt=$mysqli->prepare("INSERT INTO tbl_tags (tag_name) VALUES (?)");
			if(!$tagStmt)
				die("Cannot insert image tags into database");
			$tagStmt->bind_param('s', $tags);
                        $tagStmt->execute();
                        $tagStmt->close();

			$mysqli->close();

			/*Redirects you back home after successful upload.*/
			header("Location: index.html");
			echo "The file ". $target."has been uploaded,
			and your information has been added to the directory";
		}
		else
		{
			$error_code=$_FILES['image']['error'];
			$error_message="";

			if($error_code==UPLOAD_ERR_INI_SIZE || $error_code==UPLOAD_ERR_FORM_SIZE)
				$error_message="Sorry, the file is too big to be uploaded.";

			else if($error_code==UPLOAD_ERR_PARTIAL)
				$error_message="Sorry, the file did not upload completely.";

			else if($error_code==UPLOAD_ERR_NO_FILE)
				$error_message="Sorry, you didn't specify a file to upload.";

			else if($error_code==UPLOAD_ERR_NO_TMP_DIR)
				$error_message="Sorry, the file did not upload because PHP cannot access a temporary folder for file upload.";

			else if($error_code==UPLOAD_ERR_CANT_WRITE)
				$error_message="Sorry, the file could not be written to disk.";

			else if($error_code==UPLOAD_ERR_EXTENSION)
				$error_message="Sorry, the file could not be uploaded because of its file extension.";

			else
				$error_message="Sorry, there was a problem uploading your file.";

			header("HTTP/1.1 400 Bad Request");
			header("Location: AdminUploadAPicture.php?tags=" . urlencode($_POST['tags']) . "&image=" . urlencode($_FILES['image']['name']) . "&error=" . urlencode($error_message));
			//print json_encode($error_message);
			exit();

			//echo " <a href = 'AdminUploadAPicture.php'>Try again</a>";
		}
		?>
	</div>
</body>
</html>
