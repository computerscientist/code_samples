<!--The manage schedules page for looking at schedules and making them active.
    Willis Kennedy Created 11/30 modified since
    Will Woliver-Jones assisted on design decisions
--><html>
<head>
    <!-- -->
    <title>Schedule Management</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" >
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
    <script src = "jquery.ui.touch-punch.min.js" type= "text/javascript"></script>
    <link rel="stylesheet" href= "stylesheet.css">
    <link rel="stylesheet" href= "manage.css">
    <!--Adds users to the user select dropdown menu-->
 	<script type = "text/javascript" src = "select.js"></script>
 	<script type = "text/javascript">
 	$(document).ready(function(){
		fillUsers("<?php echo $_GET['user'] ?>");
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

        if("<?php echo $_GET['tags'] ?>"!==""){
            fillSchedulesUsingTags();
        }

        else if("<?php echo $_GET['user'] ?>"!==""){
            fillSchedulesUsingUser();
        }

        /*When you click on selecting a user it emptys all the current divs that 
        contain information from a user and refills them with all the schedules from the user selected.*/
 		$("#selectUser").click(function(){
 			window.location.replace("AdminManageSchedule.php?user="+encodeURIComponent($("select[name='user']").val())+"&page=1");
 		});
        /*Submit search does about the same thing as submit user, but it also allows for searching by title on that user. So only the schedules where there is a title matching the search field and user show up.*/
        $("#submitSearch").click(function(){
            if(/[^a-zA-Z0-9\s()']/.test($('#search').val())){
                alert("Search query must only contain letters, numbers, spaces, apostrophes, \"(\", and \")\"!");
                return;
            }
		console.log(encodeURIComponent($('#search').val().trim()));
	        window.location.replace("AdminManageSchedule.php?tags="+encodeURIComponent($('#search').val().trim())+"&user="+encodeURIComponent($("select[name='user']").val())+"&page=1");
        });
 	});
    var $id;
    /*On clicking one of the schedules gotten by search or submit user this gives detailed information and options about the schedule that was clicked.*/
    $(document).on('click', '.schedules', function(){
        $id = $(this).attr('id');
        $elem = $(this);
        $(".scheduleEncloser").removeClass("activated");
        $elem.parent().addClass("activated");
        $.get("Schedules.php/"+$id, function(data){
            $(".options").empty();
            var type;
            if(data.type==0){
                type = "Schedule";
            }
            else if(data.type==1){
                type = "Task Selection";
            }
            /*This could be broken down better, but having four separate sections also works*/
            if(data.image_four != ''){
                $("#options"+$id).append("<div class = 'images'><img src = 'upload/"+ data.image_one.replace("\'", "&#39;") +"'alt = 'first_image'><img src = 'upload/" + data.image_two.replace("\'", "&#39;") + "'alt = 'second_image'><img src = 'upload/" + data.image_three.replace("\'", "&#39;") + "'alt = 'third_image'><img src = 'upload/" + data.image_four.replace("\'", "&#39;") + "'alt = 'fourth_image'><button type = 'button' class = 'delete' value ="+data.id+" >Delete</button></div><div class ='activeSelect'><p class='title'>"+type+"</p><input type = 'radio' name = 'chooseActive' value = '2'>Activate this schedule<input type ='radio' name = 'chooseActive' value = '1'>Make this schedule next<input type = 'submit' id = 'submit' value = 'Submit'></div>");
            }
            else if(data.image_three != ''){
                $("#options"+$id).append("<div class = 'images'><img src = 'upload/"+ data.image_one.replace("\'", "&#39;") +"'alt = 'first_image'><img src = 'upload/" + data.image_two.replace("\'", "&#39;") + "'alt = 'second_image'><img src = 'upload/" + data.image_three.replace("\'", "&#39;") + "'alt = 'third_image'><button type = 'button' class = 'delete' value ="+data.id+" >Delete</button></div><div class ='activeSelect'><p class='title'>"+type+"</p><input type = 'radio' name = 'chooseActive' value = '2'>Activate this schedule<input type ='radio' name = 'chooseActive' value = '1'>Make this schedule next<input type = 'submit' id = 'submit' value = 'Submit'></div>");
            }
            else if(data.image_two != ''){
                $("#options"+$id).append("<div class = 'images'><img src = 'upload/"+ data.image_one.replace("\'", "&#39;") +"'alt = 'first_image'><img src = 'upload/" + data.image_two.replace("\'", "&#39;") + "'alt = 'second_image'><button type = 'button' class = 'delete' value ="+data.id+" >Delete</button></div><div class ='activeSelect'><p class='title'>"+type+"</p><input type = 'radio' name = 'chooseActive' value = '2'>Activate this schedule<input type ='radio' name = 'chooseActive' value = '1'>Make this schedule next<input type = 'submit' id = 'submit' value = 'Submit'></div>");
            }
            else if(data.image_one != ''){
                $("#options"+$id).append("<div class = 'images'><img src = 'upload/"+ data.image_one.replace("\'", "&#39;") +"'alt = 'first_image'><button type = 'button' class = 'delete' value ="+data.id+" >Delete</button></div><div class ='activeSelect'><p class='title'>"+type+"</p><input type = 'radio' name = 'chooseActive' value = '2'>Activate this schedule<input type ='radio' name = 'chooseActive' value = '1'>Make this schedule next<input type = 'submit' id = 'submit' value = 'Submit'></div>");
            }
        });
    });
    /*This allows the added delete handler to actually remove schedules*/
    $(document).on('click', '.delete', function(data){
        $.get("Schedules.php/"+$(this).val(), {delete: true}, function(){
            if($('.scheduleEncloser').length==1 && <?php echo intval($_GET['page']) ?> > 1)
                window.location.replace("AdminManageSchedule.php?tags="+encodeURIComponent($('#search').val().trim())+"&user="+encodeURIComponent($("select[name='user']").val())+"&page=<?php echo intval($_GET['page'])-1 ?>");
            else
                location.reload();
        });
    });
    /*This allows the radio buttons to actually make one the selected schedule active or next*/
    $(document).on('click', '#submit', function(data){
        var x = $('input:radio[name=chooseActive]:checked').val()
        if(x==2){
            alert("This Schedule is now active");}
        if(x==1){
            alert("This Schedule is now next");}
        if(x===undefined)
        	alert("Must choose option");

        else
	        $.post("Schedules.php/"+$id, {active: $('input:radio[name=chooseActive]:checked').val()}, function()
	        {
	        	location.reload();
	        });
    });
    $(document).on('click', '.logout', function(){
	$.get("asynchronousLogout.php", function(data){
		$("#welcomePlaceHolder").empty();
        	$("#logoutPlaceHolder").empty();
	});
    });

    function fillSchedulesUsingTags(){
        $.ajax({
            url:"Schedules.php",
            data: {tags: "<?php echo $_GET['tags'] ?>", user: "<?php echo $_GET['user'] ?>", lowerBound: <?php echo (isset($_GET['page']) ? ($_GET['page']-1)*10 : 0) ?>},
            success: function(data){
                $i = 0;
                $("#mostActive").empty();
                $("#schedule_spot").empty();
                $("#activeInfo").empty();
                $.each(data, function(){
                    if(data[$i].active==2){
                        $("#activeInfo").append("<div class='scheduleEncloser'><div id = '"+data[$i].id+"'class = 'schedules'><p class='title'>This Schedule is currently active: " + data[$i].tags + "</p></div><div class = 'options' id = 'options"+data[$i].id+"'></div></div>");
                    }
                    else if(data[$i].active==1){
                        $("#mostActive").append("<div class='scheduleEncloser'><div id = '"+data[$i].id+"'class = 'schedules'><p class='title'>This Schedule is next: " + data[$i].tags + "</p></div><div class = 'options' id = 'options"+data[$i].id+"'></div></div>");
                    }
                    else{
                        $("#schedule_spot").append("<div class='scheduleEncloser'><div id = '"+data[$i].id+"'class = 'schedules'><p class='title'>" + data[$i].tags + "</p></div><div class = 'options' id = 'options"+data[$i].id+"'></div></div>");
                    }
                    $i++;
                });

                if(data.length>0)
                {
                    var tags="<?php echo $_GET['tags'] ?>";
                    var user="<?php echo $_GET['user'] ?>";
                    var page=<?php echo intval($_GET['page']) ?>;
                    if(page>1){
                        $("#link_spot").append("<div class='leftLink'></div>");
                        $(".leftLink").append("<a href='AdminManageSchedule.php?tags="+tags+"&user="+user+"&page="+(page-1)+"'>Previous Schedules</a>");
			$(".leftLink>a").addClass("pageChanger");
                    }
                    if(data.length>=10){
                        //See if there are any more schedules. If so, add a link to the next page of schedules
                        $.get("Schedules.php", {tags: "<?php echo $_GET['tags'] ?>", user: "<?php echo $_GET['user'] ?>", lowerBound: <?php echo (isset($_GET['page']) ? ($_GET['page'])*10 : 0) ?>}, function(data){
                            if(data.length>0){
                                $("#link_spot").append("<div class='rightLink'></div>");
                                $(".rightLink").append("<a href='AdminManageSchedule.php?tags="+tags+"&user="+user+"&page="+(page+1)+"'>More Schedules</a>");
				$(".rightLink>a").addClass("pageChanger");
                            }
                        }, "json");
                    }
                }
            },
            error: function(data){
                alert("Search returned no results try again");
                //Don't empty slot of currently active schedule
                $("#mostActive").empty();
                $("#schedule_spot").empty();
                $("#activeInfo").empty();
            },
            dataType: "json"});
    }

    function fillSchedulesUsingUser(){
        $.get("Schedules.php", {user: "<?php echo $_GET['user'] ?>", lowerBound: <?php echo (isset($_GET['page']) ? ($_GET['page']-1)*10 : 0) ?>}, function(data){
            $i = 0;
            $("#mostActive").empty();
            $("#schedule_spot").empty();
            $("#activeInfo").empty();

            if(data.length==0){
                alert("Search returned no results try again");
            }
            else
            {
                var user="<?php echo $_GET['user'] ?>";
                var page=<?php echo intval($_GET['page']) ?>;
                if(page>1){
                    $("#link_spot").append("<div class='leftLink'></div>");
                    $(".leftLink").append("<a href='AdminManageSchedule.php?user="+user+"&page="+(page-1)+"'>Previous Schedules</a>");
		    $(".leftLink>a").addClass("pageChanger");
                }
                if(data.length>=10){
                    //See if there are any more schedules. If so, add a link to the next page of schedules
                    $.get("Schedules.php", {user: "<?php echo $_GET['user'] ?>", lowerBound: <?php echo (isset($_GET['page']) ? ($_GET['page'])*10 : 0) ?>}, function(data){
                        if(data.length>0){
                            $("#link_spot").append("<div class='rightLink'></div>");
                            $(".rightLink").append("<a href='AdminManageSchedule.php?user="+user+"&page="+(page+1)+"'>More Schedules</a>");
			    $(".rightLink>a").addClass("pageChanger");
                        }
                    }, "json");
                }
            }

            $.each(data, function(){
                if(data[$i].active==2){
                    $("#activeInfo").append("<div class='scheduleEncloser'><div id = '"+data[$i].id+"'class = 'schedules'><p class='title'>This Schedule is currently active: " + data[$i].tags + "</p></div><div class = 'options' id = 'options"+data[$i].id+"'></div></div>");
                }
                else if(data[$i].active==1){
                    $("#mostActive").append("<div class='scheduleEncloser'><div id = '"+data[$i].id+"'class = 'schedules'><p class='title'>This Schedule is next: " + data[$i].tags + "</p></div><div class = 'options' id = 'options"+data[$i].id+"'></div></div>");
                }
                else{
                    $("#schedule_spot").append("<div class='scheduleEncloser'><div id = '"+data[$i].id+"'class = 'schedules'><p class='title'>" + data[$i].tags + "</p></div><div class = 'options' id = 'options"+data[$i].id+"'></div></div>");
                }
                $i++;
            });
        }, "json");
    }
 	</script>
</head>
<body>
    <!--html code filled in by JS-->
    <div class="page" id = "broken" data-theme="b"><div class = "content">
    <div id="welcomePlaceHolder"></div>
    <div id="logoutPlaceHolder"></div>
    <div class = "header"><h1>Manage Schedules</h1></div>
    <div>
	   <label> Select a User:</label>
	   <select name = "user">
	   </select>
	   <input type = "submit" value = "Submit User" id = "selectUser"></input>
    </div>
    <div>
        <label>Search By Title:</label>
        <input type = "text" id = 'search' placeholder="List all schedules" value="<?php echo $_GET['tags'] ?>"></input>
        <input type = "submit" value = "Search" id = 'submitSearch'></input>
    </div>
    <div id = "activeInfo"></div>
    <div id = "mostActive">
    </div>
	<div id = "schedule_spot"></div>
    <div id = "link_spot"></div>
    <div><br><br><a href = "index.html" class="button">Back Home</a></div>
    </div>
</div>
</body>
</html>
