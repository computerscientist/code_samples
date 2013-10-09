<?php
session_start();
?>

<html>
<head>
  <!--This is the schedule creator it gets pictures from a search and then allows the 
  pictures to be referenced in the schedule made.
  Willis Kennedy Created 11/9 modified through 12/8
  Will Woliver-Jones assisted on design decisions-->
    <title>Schedule Creator</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" >
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
    <link rel="stylesheet" href="picSelection.css">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <link rel="stylesheet" href= "stylesheet.css" />
    <style>
    	input[type='text']:focus{
			border: 2px solid orange;
		}
    </style>
    <!--Search.js is the search function for the search bar. It returns all of the pictures
    matching the search field-->
    <script type = "text/javascript" src = "search.js"></script>
    <script type = "text/javascript">
    var i=0;
    var images = new Array();
    /*This script adds the images information to the hidden inputs in the form to be 
    submitted below.*/
    $(document).ready(function(){
    	$("#choices").change(function(data){
    		var imageLimit=$('#choices').val();
    		var children=$('#leftBox').children();
    		var originalLength=children.length;

    		$(".pictures:gt("+(imageLimit-1)+")").remove();

    		if($('#leftBox').children().length<originalLength)
    			i=imageLimit;

		var hiddenImageElements = ["#pic_1", "#pic_2", "#pic_3", "#pic_4"];

		while(hiddenImageElements.length>originalLength)
			hiddenImageElements.pop();

		while(images.length>i)
		{
			$(hiddenImageElements[images.length-1]).val("");
			images.pop();
		}
    	});

        $.get("readUser.php", function(data){
        	$i = 0;
                $.each(data, function(){
                        $("select[name='user']").append("<option value=" + data[$i] + ">" + data[$i] + "</option>");
			$i++;
                });

		if("<?php echo $_GET['user'] ?>"!=="")
        		$('select[name="user"]>option[value="<?php echo $_GET['user'] ?>"]').prop('selected', true);
        }, "json");

	$.get("currentUser.php", function(data){
    		if(data.length>0)
		{
    			$("#welcomePlaceHolder").html("<p class=\"welcome\">Welcome, "+data+"</p>");
			$("#logoutPlaceHolder").html("<button class=\"logout\">Logout</button>");
		}
    		else
		{
    			$("#welcomePlaceHolder").empty();
			$("#logoutPlaceHolder").empty();
		}
    	});

	i=0;

	if("<?php echo $_GET['pic1'] ?>"!=="")
        {
                $("#leftBox").append('<div class="pictures"><img alt="fun" src="<?php echo $_GET['pic1'] ?>"></div>');
		images[i] = "<?php echo substr($_GET['pic1'], 7) ?>";
                i++;
        }

	if("<?php echo $_GET['pic2'] ?>"!=="")
        {
                $("#leftBox").append('<div class="pictures"><img alt="fun" src="<?php echo $_GET['pic2'] ?>"></div>');
		images[i] = "<?php echo substr($_GET['pic2'], 7) ?>";
                i++;
        }

	if("<?php echo $_GET['pic3'] ?>"!=="")
        {
                $("#leftBox").append('<div class="pictures"><img alt="fun" src="<?php echo $_GET['pic3'] ?>"></div>');
		images[i] = "<?php echo substr($_GET['pic3'], 7) ?>";
                i++;
        }

	if("<?php echo $_GET['pic4'] ?>"!=="")
        {
                $("#leftBox").append('<div class="pictures"><img alt="fun" src="<?php echo $_GET['pic4'] ?>"></div>');
		images[i] = "<?php echo substr($_GET['pic4'], 7) ?>";
                i++;
        }

	if("<?php echo $_GET['search'] ?>"!=="")
		$('.keywords').val("<?php echo $_GET['search'] ?>");

	if("<?php echo $_GET['numPictures'] ?>"!=="")
		$('#choices').val("<?php echo $_GET['numPictures'] ?>");

	if("<?php echo $_GET['chooseType'] ?>"!=="")
	{
		if("<?php echo $_GET['chooseType'] ?>"==="0")
                	$('input:radio[id="schedule"]').prop('checked', true);

		else
			$('input:radio[id="choice"]').prop('checked', true);
	}

	if("<?php echo $_GET['error'] ?>"!=="")
                alert("<?php echo $_GET['error'] ?>");
    });

    $(document).on('click', '.images', function(){
        maxPics= $("#choices").val();
        if(i<maxPics){
        $(this).unbind('click');
        $imgSrc = $(this).attr('src');
        $imgAlt = $(this).attr('alt');
        $("#leftBox").append("<div class = 'pictures'><img src="+$imgSrc +" alt="+ $imgAlt +"></div>");
        $("#selected_pics").addClass("dropPictures");
        images[i] = $imgSrc.substring(7); //Leave out "upload/" part
        //alert(images[i]);
        i++;
        //alert(i);
        if(i==1)
          $("#pic_1").val(images[0]);
        if(i==2)
          $("#pic_2").val(images[1]);
        if(i==3)
          $("#pic_3").val(images[2]);
         if(i==4)
          $("#pic_4").val(images[3]);
      }
      $("#pic_1").val(images[0]); //Synchronize current values of "hidden pictures" in form
      $("#pic_2").val(images[1]);
      $("#pic_3").val(images[2]);
      $("#pic_4").val(images[3]);
    });
    /*Clears the schedule being currently developed, useful for fixing mistakes*/
    $(document).on('click', '#clear', function(){
      $("#leftBox").empty();
      $("#pic_1").val("");
      $("#pic_2").val("");
      $("#pic_3").val("");
      $("#pic_4").val("");

      images = new Array();
      i = 0;
    });
    /*Allows the current user to logout without redirecting to another page*/
    $(document).on('click', '.logout', function(){
	$.get("asynchronousLogout.php", function(data){
		$("#welcomePlaceHolder").empty();
        	$("#logoutPlaceHolder").empty();
	});
    });
    </script>
</head>
<body>
	<div class="page" id = "broken" data-theme="b">
	<div id="welcomePlaceHolder"></div>
        <div id="logoutPlaceHolder"></div>

    	<div class = "header"><h1>Build a Schedule!</h1></div>
   		 <div class = "content">
        <p>Select Number of Pictures first</p>
        <label for="search-basic">Search:</label>
        <input type = "text" name = "search" class = "keywords" placeholder="List all pictures">
        <input type = "submit" value = "Submit" class = "search"><br>
        <button id = "clear">Clear</button><br>
        <div class = "pic_boxes" id = "leftBox"></div>
        <!--This form submits all the relevant information to the schedules. The four hiddens
        represent the images, the radio buttons represent the schedule type, the first drop down
        represents the number of images, the title represents a title field, the second drop
        down is for all the users, and submit posts all the information to createSchedule.php-->
        <form name = "selectedPics" id = "selected_pics" enctype = "multipart/form-data"
          action = "createSchedule.php" method = "post">
          <br>
          <input type = "hidden" name = "pic1" id = "pic_1" value = "<?php echo substr($_GET['pic1'], 7) ?>">
          <input type = "hidden" name = "pic2" id = "pic_2" value = "<?php echo str_replace("\"", "&quot;", substr($_GET['pic2'], 7)) ?>">
          <input type = "hidden" name = "pic3" id = "pic_3" value = "<?php echo substr($_GET['pic3'], 7) ?>">
          <input type = "hidden" name = "pic4" id = "pic_4" value = "<?php echo substr($_GET['pic4'], 7) ?>">
          <input type = "radio" name="chooseType" value = "0" id = "schedule" checked="true">Schedule</input>
          <input type = "radio" name ="chooseType" value = "1" id = "choice">Task Selection</input><br>
          <label>Number of Pictures:</label>
          <select id = "choices" name="numPictures">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
          </select>
          <label>Title:</label>
          <input type = "text" name = "tags" value = "<?php echo str_replace('"', '\'', $_GET['tags']) ?>">
          <label> Select User:</label>
          <select name = "user">
          </select>
          <input type = "submit" value = "Submit">
        </form>
        <div id = "image_display">
      </div>
        <div><br><br><a href = "index.html" class="button">Back Home</a></div>
      </div>
    </div>
 </body>
</html>
