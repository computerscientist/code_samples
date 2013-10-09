<?php
#starts session for user info
session_start();
?>

<html lang=en>

<head>
<meta charset=utf-8 />
<title>Schedule</title>
<link rel = stylesheet href = "scheduleStyle.css" type = "text/css" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
<script src = "jquery.ui.touch-punch.min.js" type= "text/javascript"></script>
<script type="text/javascript">
<?php echo 'var user = '.json_encode($_SESSION['user']).';';?>
$(document).ready(function(){
	$(document).trigger('load');
});
/*The load function creates the elements that a user can drag*/
$(document).on('load', function(){
	$.get("currentUser.php", function(data){
    		if(data.length>0)
    			$("#welcomePlaceHolder").html("<p class=\"welcome\">Welcome, "+data+"</p>");
    		else
    			$("#welcomePlaceHolder").empty();
    	});
	var jqxhr = $.get("Schedules.php", {user: user, active: 2}, function(data){
		$('#leftCol').empty();
		$('#rightCol').empty();
		var sched_id = data.id;console.log(data.active);
		var numberList = new Array();
		numberList[0] = data.image_one;
		numberList[1] = data.image_two;
		numberList[2] = data.image_three;
		numberList[3] = data.image_four;
		var count = 0;
		for(var i = 0; i<numberList.length; i++){
			if(numberList[i]!=''){
				count = i;
			}
			else{
				break;
			}
		}
		for(var i = 0; i<=count; i++){
			var m = i+1;
			dragNum = "draggable"+m;
			dropNum = "droppable"+m;
			$('#leftCol').append('<div class="longbox_left"><img src="upload/'+numberList[i]+'" class = "'+dragNum+' imgresize" id="'+dragNum+'" alt="QWERTY"></div>');
			$('#rightCol').append('<div class="longbox"><div class = "dropbox" id="'+dropNum+'"></div></div>');
		}
		$(document).trigger("dragging", [count, sched_id]);
	});
	jqxhr.error(function(){
		$('#leftCol').empty();
		$('#rightCol').empty();
		alert("There is no schedule currently ready");
	});
});
/*The dragging function makes the elements created draggable and droppable*/
$(document).on('dragging', function(event, count, sched_id){
	//dragging section
	for(var i = 0; i<=count; i++){
	var m = i+1;
	if(i==0){
	$(".draggable"+m).draggable({
		axis: "x",
		revert: 'invalid',
		revertDuration: 300,
		snap: "#droppable"+m,
		snapMode: "inner",
	});
	}
	else{
	$(".draggable"+m).draggable({
		axis: "x",
		revert: 'invalid',
		revertDuration: 300,
		snap: "#droppable"+m,
		snapMode: "inner",
		disabled: true,
	});
	}
	}
	/*for(var p = 0; p<=count; p++){
		var m = p+1;
		var n = m+1;
		if(i==count){
			$("#droppable"+m).droppable({
				accept: "#draggable"+m,
				drop : function(){
					$("#draggable"+n).draggable({disabled: true});
					$("#droppable"+m).append('<img src="Checkmark.png" class = "imgresize" alt="check" style="position: absolute;">');
				}
			});
		}
		else{
			$("#droppable"+m).droppable({
				accept: "#draggable"+m,
				drop : function(){
					$("#draggable"+m).draggable({disabled: true});
					$("#draggable"+n).draggable({disabled: false});
					$("#droppable"+m).append('<img src="Checkmark.png" class = "imgresize" alt="check" style="position: absolute;">');
				}
			});
		}
	}*/
	var k;
	$("#droppable1").droppable({
		accept: "#draggable1",
		drop : function(){
			k=0;
			if(k==count){
				$(document).trigger('next', sched_id);}
			$("#draggable1").draggable({disabled: true});
			$("#draggable2").draggable({disabled: false});
			$("#droppable1").append('<img src="Checkmark.png" class = "imgresize" alt="check" style="position: absolute;">');
		}
	});
	$("#droppable2").droppable({
			accept: "#draggable2",
			drop : function(){
				k=1;
				if(k==count){
					$(document).trigger('next', sched_id);}			
				$("#draggable2").draggable({disabled: true});
				$("#draggable3").draggable({disabled: false});
				$("#droppable2").append('<img src="Checkmark.png" class = "imgresize" alt="check" style="position: absolute;">');
			}
	});
	$("#droppable3").droppable({
		accept: "#draggable3",
		drop : function(){
			k=2;
			if(k==count){
				$(document).trigger('next', sched_id);}
			$("#draggable3").draggable({disabled: true});
			$("#draggable4").draggable({disabled: false});
			$("#droppable3").append('<img src="Checkmark.png" class = "imgresize" alt="check" style="position: absolute;">');
		}
	});
	$("#droppable4").droppable({
		accept: "#draggable4",
		drop : function(){
			k=3;
			if(k==count){
				$(document).trigger('next', sched_id);}
			$("#draggable4").draggable({disabled: true});
			$("#droppable4").append('<img src="Checkmark.png" class = "imgresize" alt="check" style="position: absolute;">');
		}
	});
});
/*The next function performs operations that make the schedule just used not active and get
the schedule next in line to be active*/
$(document).on('next', function(event, sched_id){
	$.post("Schedules.php/"+sched_id, {active: 0}, function(){
		var next_sched_id;
		var jqxhr = $.get("Schedules.php", {user: user, active: 1}, function(data){
			next_sched_id = data.id;
			$(document).trigger("newActive", next_sched_id);
		});
		jqxhr.error(function(){
			location.reload();
		});
	});
});
/*newActive sets the schedule found above to the most active schedule and redirects either back to
load to reconstruct the new schedule or to choice if the new schedule is a task selection 
schedule*/
$(document).on('newActive', function(event, next_sched_id){
	$.post("Schedules.php/"+next_sched_id, {active: 2}, function(data){
		if(data.type==0){
			$(document).trigger("load");
		}
		else if(data.type==1){
			window.location.replace("Choice.php");
		}
});
});
</script>

</head>

<body>
<div id="welcomePlaceHolder"></div>
<div class="floatleft">
	<h1> To-Do </h1>
	<div id="leftCol"></div>
</div>
<div class="floatright">
	<h1> Finished </h1>
	<div id="rightCol"></div>
</div>
<div class="between"></div>
<div class="content">
	<div>
		<br><br>
		<a href = "index.html" class="button"  data-theme="c">Back Home</a>
	</div>
</div>
</body>
</html>
