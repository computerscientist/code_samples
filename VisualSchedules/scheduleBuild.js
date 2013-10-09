/*Function to build a schedule from inputs, 
Ryan Berger 11/9 modified through 12/6
Willis added some checking and function calls to keep the schedules going
through 12/6*/
function buildSchedule(count, sched_id, src1, alt1, src2, alt2, src3, alt3, src4, alt4) {
	passedArray = new Array(src1, alt1, src2, alt2, src3, alt3, src4, alt4);
	$('body > div').delay(800).fadeOut("slow");
	$(this).data("timeout", setTimeout(function() {
		$('.content').remove();
		$('#welcomePlaceHolder').remove();
		$('body').append('<div id="welcomePlaceHolder"></div>');
		$.get("currentUser.php", function(data){
    			if(data.length>0)
    				$("#welcomePlaceHolder").html("<p class=\"welcome\">Welcome, "+data+"</p>");
    			else
    				$("#welcomePlaceHolder").empty();
    		});

		$('body').append('<div class="scheduleContent"></div>');
		$('.scheduleContent').append('<div class="floatleft"><h1> To-Do </h1></div>');
		$('.scheduleContent').append('<div class="floatright"><h1> Finished </h1></div>');
		$('body').append('<div class="between"></div>');
		for (var i = 1; i <= Math.ceil(sections/2.0); i++){
			dragNum = "draggable"+i;
			dropNum = "droppable"+i;
			source = (i-1)*2;
			alt = source + 1;

			//fix alts to altArray
			$('div.floatleft').append('<div class="longbox_left"><img src="'+passedArray[source]+'" class = "'+dragNum+' imgresizeSched" id="'+dragNum+'" alt="'+passedArray[alt]+'"></div>');
			$('div.floatright').append('<div class="longbox_sched"><div class = "dropbox" id="'+dropNum+'"></div></div>');
		}

		$('body').append('<div class="content"><div><a class="button" data-theme="c" href="index.html">Back Home</a></div></div>');

		//dragging section
		$(".draggable1").draggable({
			axis: "x",
			revert: 'invalid',
			revertDuration: 300,
			snap: "#droppable1",
			snapMode: "inner",
			snapTolerance: 100
		});
		$(".draggable2").draggable({
			axis: "x",
			revert: 'invalid',
			revertDuration: 300,
			snap: "#droppable2",
			snapMode: "inner",
			snapTolerance: 100,
			disabled: true
		});
		/**$(".draggable3").draggable({
			axis: "x",
			revert: 'invalid',
			revertDuration: 300,
			snap: "#droppable3",
			snapMode: "inner",
			snapTolerance: 100,
			disabled: true
		});
		$(".draggable4").draggable({
			axis: "x",
			revert: 'invalid',
			revertDuration: 300,
			snap: "#droppable4",
			snapMode: "inner",
			snapTolerance: 100,
			disabled: true
		});*/

		//dropping section
		var k;
		$("#droppable1").droppable({
			accept: "#draggable1",
			drop : function(){
				k = 1;
				if(k==count){
					$(document).trigger('next', sched_id);}
				$("#draggable1").draggable({disabled: true});
				$("#draggable2").draggable({disabled: false});
				$('img.draggable1').remove();
				$("#droppable1").append('<img src="Checkmark.png" class = "imgresizeSched" alt="check" style="position: absolute;">');
				$('#droppable1').append('<img src="'+passedArray[0]+'" class = "imgresizeSched" id="draggable1" alt="'+passedArray[1]+'">');
			}
		});
		$("#droppable2").droppable({
			accept: "#draggable2",
			drop : function(){
				k = 2;
				if(k==count){
					$(document).trigger('next', sched_id);}
				$("#draggable2").draggable({disabled: true});
				//$("#draggable3").draggable({disabled: false});
				$('img.draggable2').remove();
				$("#droppable2").append('<img src="Checkmark.png" class = "imgresizeSched" alt="check" style="position: absolute;">');
				$('#droppable2').append('<img src="'+passedArray[2]+'" class = "imgresizeSched" id="draggable1" alt="'+passedArray[3]+'">');
			}
		});
		/**$("#droppable3").droppable({
			accept: "#draggable3",
			drop : function(){
				k = 3;
				if(k==count){
					$(document).trigger('next', sched_id);}
				$("#draggable3").draggable({disabled: true});
				$("#draggable4").draggable({disabled: false});
				$('img.draggable3').remove();
				$("#droppable3").append('<img src="Checkmark.png" class = "imgresizeSched" alt="check" style="position: absolute;">');
				$('#droppable3').append('<img src="'+passedArray[4]+'" class = "imgresizeSched" id="draggable1" alt="'+passedArray[5]+'">');
			}
		});
		$("#droppable4").droppable({
			accept: "#draggable4",
			drop : function(){
				k = 4;
				if(k==count){
					$(document).trigger('next', sched_id);}
				$("#draggable4").draggable({disabled: true});
				$('img.draggable4').remove();
				$("#droppable4").append('<img src="Checkmark.png" class = "imgresizeSched" alt="check" style="position: absolute;">');
				$('#droppable4').append('<img src="'+passedArray[6]+'" class = "imgresizeSched" id="draggable1" alt="'+passedArray[7]+'">');
			}
		});*/
	}, 1500));
}
