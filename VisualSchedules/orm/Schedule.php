<?php
/*A class for individual schedules this makes schedules easy to fetch and run functions on.*/
class Schedule{
	private $id;
	private $active;
	private $user;
	private $image_one;
	private $image_two;
	private $image_three;
	private $image_four;
	private $tags;
	private $type;

	#public static function create($user, $)
	private function __construct($id, $active, $user, $image_one, $image_two, 
		$image_three, $image_four, $tags, $type){
		$this->id = $id;
		$this->active = $active;
		$this->user = $user;
		$this->image_one = $image_one;
		$this->image_two = $image_two;
		$this->image_three = $image_three;
		$this->image_four = $image_four;
		$this->tags = $tags;
		$this->type = $type;
	}

	public static function findByID($id){
		$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
		$findStmt=$mysqli->prepare("SELECT * FROM tbl_sched WHERE sched_id=?");
                $findStmt->bind_param('s', $id);
                $findStmt->bind_result($image_one, $image_two, $image_three, $image_four, $user, $tags, $type, $active, $sched_id);
                $findStmt->execute();
                $outcome=$findStmt->fetch();

		if($outcome !== false){
		if($outcome === null){
			return null;
		}
		//print('2');
		/*print($schedule_info['user']);
		print($schedule_info['image_four']);
		print($schedule_info['image_one']);
		print($schedule_info['image_two']);
		print($schedule_info['image_three']);
		print($schedule_info['tags']);*/
		$findStmt->close();
                $mysqli->close();

		return new Schedule(intval($sched_id), intval($active),
		 $user, $image_one, $image_two, $image_three, $image_four,
		 $tags, $type);
		}
		return null;
	}

	public static function getFromUser($user, $lowBound){
		$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
                $findStmt=$mysqli->prepare("SELECT sched_id FROM tbl_sched WHERE user=? ORDER BY active DESC, sched_id DESC LIMIT ?, 10");
                $findStmt->bind_param('sd', $user, intval($lowBound));
                $findStmt->bind_result($retrieved_id);
                $findStmt->execute();

		$schedules = array();
		while($findStmt->fetch()){
			$schedules[] = Schedule::findByID($retrieved_id);
		}
		$findStmt->close();
		$mysqli->close();

		return $schedules;
	}

	public static function getMostActive($user, $active){
		$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
		$findStmt=$mysqli->prepare("SELECT sched_id FROM tbl_sched WHERE active=? AND user=? ORDER BY sched_id DESC");
		$findStmt->bind_param('ds', $active, $user);
		$findStmt->bind_result($retrieved_id);
		$findStmt->execute();
		$outcome=$findStmt->fetch();

		if($outcome !== false && $outcome !== null)
			$schedule = Schedule::findByID($retrieved_id);

		//See if there is a 'next' schedule we can use if trying to look for currently active schedule and can't find one...
		else if($outcome === null && $active==2)
		{
			$findLessActiveStmt=$mysqli->prepare("SELECT sched_id FROM tbl_sched WHERE active=1 AND user=? ORDER BY sched_id DESC");
			$findLessActiveStmt->bind_param('s', $user);
			$findLessActiveStmt->bind_result($retrieved_id);
			$findLessActiveStmt->execute();
			$outcome=$findLessActiveStmt->fetch();

			if($outcome !== false && $outcome !== null)
			{
                        	$schedule = Schedule::findByID($retrieved_id);
				$schedule->setActive(2);
			}

			$findLessActiveStmt->close();
		}

		$findStmt->close();
		$mysqli->close();

		return $schedule;
	}

	public function setActive($active){
		$this->active = $active;
		$this->update();
	}

	public function update(){
		$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
		$queryOneStmt = $mysqli->prepare("UPDATE tbl_sched SET active = 0 WHERE user = ? AND active = ?"); //Make sure only one schedule "active" and one schedule "next" at a time
		$queryOneStmt->bind_param('sd', $this->user, $this->active);
		$queryOneStmt->execute();
		$queryOneStmt->close();
		$queryTwoStmt = $mysqli->prepare("UPDATE tbl_sched SET active = ? WHERE sched_id = ?");
                $queryTwoStmt->bind_param('dd', $this->active, $this->id);
                $queryTwoStmt->execute();
                $queryTwoStmt->close();
		$mysqli->close();
	}

	public function delete(){
		$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
		$deleteStmt = $mysqli->prepare("DELETE FROM tbl_sched WHERE sched_id=?");
		$deleteStmt->bind_param('d', $this->id);
		$deleteStmt->execute();
		$deleteStmt->close();
		$mysqli->close();
	}

	public function getJSON(){
		$json_rep = array();
		$json_rep['id'] = $this->id;
		$json_rep['active'] = $this->active;
		$json_rep['user'] = $this->user;
		$json_rep['image_one'] = str_replace('"', "&quot;", $this->image_one);
                $json_rep['image_two'] = str_replace('"', "&quot;", $this->image_two);
                $json_rep['image_three'] = str_replace('"', "&quot;", $this->image_three);
                $json_rep['image_four'] = str_replace('"', "&quot;", $this->image_four);
		$json_rep['tags'] = $this->tags;
		$json_rep['type'] = $this->type;
		return $json_rep;
	}

	public static function getFromTag($tags, $user, $lowBound){
		$tags="%" . $tags . "%";
		$mysqli = new mysqli("localhost", "root", "", "comp523p1db");
		$searchStmt=$mysqli->prepare("SELECT sched_id FROM tbl_sched WHERE tags LIKE ? AND user = ? ORDER BY active DESC, sched_id DESC LIMIT ?, 10");
    		$searchStmt->bind_param('ssd', $tags, $user, intval($lowBound));
    		$searchStmt->bind_result($retrieved_id);
    		$searchStmt->execute();

		$schedules = array();
		while($searchStmt->fetch()){
                        $schedules[] = Schedule::findByID($retrieved_id);
                }
		$searchStmt->close();
		$mysqli->close();

		if(count($schedules)==0)
			return null;

		return $schedules;
	}
}
