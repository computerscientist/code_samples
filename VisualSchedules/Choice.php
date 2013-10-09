<?php
session_start();
?>
<!--Creates task selection pages after loading them from the database
Ryan Berger most javascript and html functionality Created 11/9 modified through 12/3
Willis Kennedy added server connections and ajax functionality modified through 12/8-->
<html lang=en>

<head>
<meta charset=utf-8>
<title>Choice Page</title>
<link rel = stylesheet href = "choiceStyle.css" type = "text/css">
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
<script src = "jquery.ui.touch-punch.min.js"></script>
<!--scheduleBuild builds the schedule with the choices the user made-->
<script src = "scheduleBuild.js" type= "text/javascript"></script>

<script type="text/javascript">
<?php echo 'var user = '.json_encode($_SESSION['user']).';';?>
$(document).ready(function(){
	$(document).trigger('load');
});
var sections = 0,
	imageArray = new Array(sections),
	altArray = new Array(sections),
	$clickNum = 0,
	$imgSrc1 = $imgSrc2 = $imgSrc3 = $imgSrc4 = null,
	$imgAlt1 = $imgAlt2 = $imgAlt3 = $imgAlt4 = null,
	dragNum,
	dropNum;
/*load once again creates the elements that will be used*/
$(document).on('load', function(){
	$.get("currentUser.php", function(data){
    		if(data.length>0)
    			$("#welcomePlaceHolder").html("<p class=\"welcome\">Welcome, "+data+"</p>");
    		else
    			$("#welcomePlaceHolder").empty();
    	});
	var jqxhr = $.get("Schedules.php", {user: user, active: 2}, function (data){
		var sched_id = data.id;
		var numberList = new Array();
		numberList[0] = data.image_one;
		numberList[1] = data.image_two;
		numberList[2] = data.image_three;
		numberList[3] = data.image_four;
		for(var i = 0; i<numberList.length; i++){
			if(numberList[i]!=''){
				sections = i+1;
			}
			else{
				break;
			}
		}

	for (var i = 0; i < sections; i+=2){
		var m = i+1;
		imgNum = "img"+m;

		var leftClass=numberList[m]!='' ? "left" : "single";
		leftImg = '<div class="'+leftClass+'"><img class="'+imgNum+' imgresize" src="upload/'+numberList[i]+'" alt="lighthouse"></div>';

		if(numberList[m]!='')
		{
			imgCenter = '<div class="center"><span class="center">OR</span></div>';
			imgRight = '<div class="right"><img class="'+imgNum+' imgresize" src="upload/'+numberList[m]+'" alt="desert"></div>';
			$('body').append('<div class="longbox">'+leftImg+imgCenter+imgRight+'</div>');
		}

		else
			$('body').append('<div class="longbox">'+leftImg+'</div>');
	}
	var count = Math.round(sections/2);
	$('body').append('<div class="content"><div><a class="button" data-theme="c" href="index.html">Back Home</a></div></div>');
	$(document).trigger("clicking", [count, sched_id]);
	});
	jqxhr.error(function(){
		$('body').append('<div class="content"><div><a class="button" data-theme="c" href="index.html">Back Home</a></div></div>');
		alert("There is no schedule currently ready");
	});
});
/*clicking adds a click event to each of the image elements created by load and then
when all of the clicking is done the information is passed on to scheduleBuild*/
$(document).on("clicking", function(event, count, sched_id){
	//had to nest clicks to enable and disable each click after the previous choice
	$(".img1").click(function(oneClick) { //click 1
		$(".img1").unbind('click');
		$clickNum = 1;
		$imgSrc1 = $(this).attr('src').replace("\"", "&quot;");
		$imgAlt1 = $(this).attr('alt').replace("\"", "&quot;");
		//append image
		$('.topbox').append("<img class='imgresizeTop' src="+$imgSrc1 +" alt="+ $imgAlt1 +" />");

		if ($clickNum == count) {
			buildSchedule(count, sched_id, $imgSrc1, $imgAlt1, $imgSrc2, $imgAlt2, $imgSrc3, $imgAlt3, $imgSrc4, $imgAlt4);
		}

		$(".img3").click(function() { //click 2
			$(".img3").unbind('click');
			$clickNum = 2;
			$imgSrc2 = $(this).attr('src').replace("\"", "&quot;");
			$imgAlt2 = $(this).attr('alt').replace("\"", "&quot;");
			//append image
			$('.topbox').append("<span class='top'> &gt; </span>");
			$('.topbox').append("<img class='imgresizeTop' src="+ $imgSrc2 +" alt="+ $imgAlt2 +" />");
			if ($clickNum == count) {
				buildSchedule(count, sched_id, $imgSrc1, $imgAlt1, $imgSrc2, $imgAlt2, $imgSrc3, $imgAlt3, $imgSrc4, $imgAlt4);
			}

			/**$(".img5").click(function() { //click 3
				$(".img5").unbind('click');
				$clickNum = 3;
				$imgSrc3 = $(this).attr('src');
				$imgAlt3 = $(this).attr('alt');
				//append image
				$('.topbox').append("<span class='top'> &gt; </span>");
				$('.topbox').append("<img class='imgresizeTop' src="+ $imgSrc3 +" alt="+ $imgAlt3 +" />");
				if ($clickNum == count) {
					buildSchedule(count, sched_id, $imgSrc1, $imgAlt1, $imgSrc2, $imgAlt2, $imgSrc3, $imgAlt3, $imgSrc4, $imgAlt4);
				}

				$(".img7").click(function() { //click 2
					$(".img7").unbind('click');
					$clickNum = 4;
					$imgSrc4 = $(this).attr('src');
					$imgAlt4 = $(this).attr('alt');
					//append image
					$('.topbox').append("<span class='top'> &gt; </span>");
					$('.topbox').append("<img class='imgresizeTop' src="+ $imgSrc4 +" alt="+ $imgAlt4 +" />");

					if ($clickNum == count) {
						buildSchedule(count, sched_id, $imgSrc1, $imgAlt1, $imgSrc2, $imgAlt2, $imgSrc3, $imgAlt3, $imgSrc4, $imgAlt4);
					}
				});
			});*/
		});
	});
});
/*These next two functions perform the same as on schedule display. They make the 
current schedule no longer current, get the next schedule, make the next schedule
current, and then redirect to wherever the next schedule should be*/
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
$(document).on('newActive', function(event, next_sched_id){
	$.post("Schedules.php/"+next_sched_id, {active: 2}, function(data){
		if(data.type==0){
			window.location.replace("ScheduleDisplay.php");
		}
		else if(data.type==1){
			location.reload();
		}
	});
});
</script>

</head>

<body>
	<div id="welcomePlaceHolder"></div>
	<div class="topbox"></div>
</html>
