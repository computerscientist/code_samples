<?php
/*The php file that runs get and posts for schedules. It allows updates to several different
schedule elements and makes throwing functions to the schedule greatly simplified. 
Willis Kennedy created 11/29 modified through 12/7*/
require_once('orm/Schedule.php');
if($_SERVER['REQUEST_METHOD'] == 'GET'){
	if(!isset($_SERVER['PATH_INFO'])){
		if(isset($_GET['user'])&&isset($_GET['tags'])){
			$schedules = Schedule::getFromTag($_GET['tags'], $_GET['user'], $_GET['lowerBound']);
			if(is_null($schedules)){
				header("HTTP/1.1 400 Bad Request");
				print("Error in title");
				exit();
			}
			$schedules_ids = array();
			foreach($schedules as $t){ //Iterate through each schedule (not keys)
				$schedule_ids[] = $t->getJSON();
			}
			header("Content-type: application/json");
			print(json_encode($schedule_ids));
			exit();
		}
		if(isset($_GET['user'])&&isset($_GET['active'])){
			$active = intval($_GET['active']);
			$schedules = Schedule::getMostActive($_GET['user'], $active);
			if(is_null($schedules)){
				header("HTTP/1.1 400 Bad Request");
				print("Most active somehow not found");
				exit();
			}
			$sched = $schedules->getJSON();
			header("Content-type: application/json");
			print(json_encode($sched));
			exit();
		}
		if(isset($_GET['user'])){
			$schedules = Schedule::getFromUser($_GET['user'], $_GET['lowerBound']);
			if(is_null($schedules)){
				header("HTTP/1.1 400 Bad Request");
				print("Error in user, probably didn't exist");
				exit();
			}
			$schedule_ids = array();
			foreach($schedules as $t){
				$schedule_ids[] = $t->getJSON();
			}
			header("Content-type: application/json");
			print(json_encode($schedule_ids));
			exit();
		}
	} else {
		$schedule_id = intval(substr($_SERVER['PATH_INFO'], 1));
		$schedule = Schedule::findByID($schedule_id);
		if(is_null($schedule)){
			header("HTTP/1.1 404 Not Found");
			print("Schedule id not found or illegal");
			exit();
		}
		if(!isset($_GET['delete'])){
			header("Content-type: application/json");
			print(json_encode($schedule->getJSON()));
			exit();
		}
		else{
			$schedule->delete();
			header("Content-type: application/json");
			print(json_encode(true));
			exit();
		}
	}
} else if($_SERVER['REQUEST_METHOD'] == 'POST') {
	if(!isset($_SERVER['PATH_INFO'])){
		/*This would be create schedule, but I already have made that. 
		I'd like to merge them eventually*/
	} else {
		$schedule_id = intval(substr($_SERVER['PATH_INFO'], 1));
		$schedule = Schedule::findByID($schedule_id);
		if(is_null($schedule)){
			header("HTTP/1.1 404 Not Found");
			print("Schedule id not found or illegal");
			exit();
		}
		if(isset($_POST['active'])){
			$schedule->setActive(intval($_POST['active']));
			header("Content-type: application/json");
			print(json_encode($schedule->getJSON()));
			exit();
		}
	}
}
?>
