// target frames per second
const FPS = 50;
const SOUND_DECREASE_FACTOR=0.25;
const RELATIVE_FOOTSTEP_VOLUME=0.3;
const RELATIVE_VOLUMES=[0, 0.5, 1];

var audio=null;

var masterVolume=0;
var speechVolume=0;
var soundVolume=0;

var canvas = null;
var context2D = null;

var levels=[];
var levelNumber=1;

var levelColors=["rgb(255, 0, 0)", "rgb(0, 255, 0)", "rgb(0, 0, 255)", "rgb(255, 165, 0)", "rgb(0, 0, 128)", 
				 "rgb(128, 0, 0)", "rgb(105, 105, 105)", "rgb(255, 160, 122)", "rgb(0, 100, 0)", "rgb(147, 112, 219)"];

var xLocation=null;
var yLocation=null;

var keyDown=0;
var keyPressed=0;

var arrowRotation=90;

var score=0;
var lives=5;

var isGameRunning=true;
var isGameOver=false;
var isFalling=false;
var toDrawArrow=true;
var playingLivesLeft=false;
var playingScoreSound=false;
var playingFinishedSound=false;
var introducing=true;
var givingHint=false;

var windAheadSound='wind_ahead';
var leftWindSound='wind_left';
var rightWindSound='wind_right';

var trap_image=new Image();
trap_image.src="Images/Trap.png";

var coin_image=new Image();
coin_image.src="Images/Coin.png";

dojo.ready(init);
dojo.subscribe('/org/hark/pause', pauseCallBack);
dojo.subscribe('/org/hark/prefs/response', prefsCallback);

document.onkeydown=onKeyDown;
document.onkeyup=onKeyUp;

//Initializes and prepares to introduce the game, and initializes the uow audio element
function init()
{
	canvas = document.getElementById('canvas');
    context2D = canvas.getContext('2d');
	
	uow.getAudio().then(function(a)
	{
		audio=a;
		dojo.publish('/org/hark/prefs/request');
		introduceGame();
	});
}

//Introduces the game
function introduceGame()
{
	//Introduce game
	var i;
	
	audio.setProperty({name : 'voice', channel : 'default', value : 'en/en+f1'});
	speak('Press the space bar at any time to skip past the instructions.', 'default', true, none);
	audio.setProperty({name : 'voice', channel : 'default', value : 'default'});
	
	speak('Before we get started, I am going to play some bumping sounds in each of your ears to make sure your headphones are on correctly. Here are some bumping sounds in your left ear: ', 'default', false, none);
	playSound('Other_Sounds/bump_left_only', 'default', 1, false, none);
	
	speak('Here are some bumping sounds in your right ear: ', 'default', false, none);
	playSound('Other_Sounds/bump_right_only', 'default', 1, false, none);
	
	speak('Welcome to Cave Escape! You are lost in a cave and are trying to get out. Rotate left and right using the left and right arrow keys, and use the up arrow key to go forward. Note, however that you cannot move backwards. Navigate through the cave by listening to the wind sounds in both of your ears. Once you escape, you move on to another cave, which is even harder than the one before it. Score points by collecting coins, but be sure to avoid the traps! If you fall into a trap, you die. If you lose all of your lives, the game is over. You start out with five lives, and there are ten levels to navigate through.', 'default', false, none);
	speak('Here are some hints for doing well in this game: Make sure that your head phones are on correctly. If your head phones are on backwards, you will not hear the sounds correctly and get lost. When there is an opening to your left or right, you will hear more wind on that side. If you hear no wind at all, you are facing a dead end. Just turn and you will hear wind again. If you get totally lost, press the enter key for hints about your current location. A coin nearby makes this sound: ', 'default', false, none);
	playSound('Treasure_Sounds/treasure', 'default', 1, false, none);
	speak('A trap nearby makes this sound: ', 'default', false, none);
	playSound('Trap_Sounds/trap', 'default', 1, false, none);
	speak('When there is a coin or trap nearby, you will hear it in the ear which is in the same direction as the object. The most important hint, however, is to have fun. Good luck!', 'default', false, initializeGame);
}

//Actually initializes the game
function initializeGame()
{
	introducing=false;
	
	initializeLevels();
	initializePlayer();
	
	audio.setProperty({name : 'loop', value : true, channel : 'custom', immediate : true});
	audio.setProperty({name : 'loop', value : true, channel : 'secondary', immediate : true});
	audio.setProperty({name : 'loop', value : true, channel : 'tertiary', immediate : true});
	
	setInterval(draw, 1000/FPS);
}

//Starts up the wind sounds
function startWindSounds()
{
	playSound('Wind_Sounds/'+windAheadSound, 'custom', RELATIVE_VOLUMES[1], true, none);
	playSound('Wind_Sounds/'+leftWindSound, 'secondary', RELATIVE_VOLUMES[1], true, none);
	playSound('Wind_Sounds/'+rightWindSound, 'tertiary', RELATIVE_VOLUMES[1], true, none);
}

//Initializes the level maps. 0's are blank spots, 1's are walls, 2's are traps, and 3's are coins
function initializeLevels()
{
	levels=[[[0, 0, 0, 0, 0], [0, 1, 0, 1, 0], [0, 1, 3, 1, 0], [0, 1, 0, 1, 0], [0, 1, 0, 0, 2]], 
			[[0, 1, 0, 0, 0, 0, 0], [0, 1, 0, 1, 1, 1, 1], [0, 1, 0, 1, 0, 0, 0], [0, 1, 0, 1, 1, 1, 0], [0, 0, 0, 0, 0, 0, 3], [0, 1, 1, 1, 1, 1, 0], [0, 0, 0, 0, 2, 0, 0]],
			[[0, 0, 0, 3, 0, 1, 0], [1, 1, 1, 1, 0, 1, 0], [0, 0, 0, 0, 0, 1, 0], [0, 1, 1, 1, 0, 1, 0], [0, 1, 0, 1, 0, 1, 0], [0, 1, 0, 1, 0, 1, 0], [0, 1, 2, 0, 0, 0, 3]],
			[[0, 1, 3, 1, 0, 0, 3, 0, 0], [0, 1, 0, 1, 1, 1, 1, 1, 0], [0, 1, 0, 1, 0, 0, 0, 0, 0], [0, 1, 0, 1, 0, 1, 0, 1, 0], [0, 0, 0, 0, 0, 1, 0, 1, 0], [0, 1, 1, 1, 1, 1, 0, 1, 0], [0, 0, 0, 0, 0, 1, 2, 1, 0], [1, 1, 1, 1, 0, 1, 0, 1, 0], [0, 0, 0, 0, 0, 1, 0, 1, 0]],
			[[0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0], [0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1], [0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0], 
				[0, 0, 3, 1, 0, 0, 0, 0, 0, 1, 0], [0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0], [0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 3], [0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0],
				[0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0], [0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0]],
			[[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1], [0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0], [1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0], 
				[0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 2], [0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0], [3, 0, 0, 1, 0, 0, 2, 0, 0, 0, 3, 1, 0], [1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0], 
				[0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0], [0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0], [0, 1, 0, 1, 0, 1, 3, 1, 0, 1, 0, 1, 0], [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0], [0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0]],
			[[0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1], [3, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0], [1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0], 
				[0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 3, 0, 0], [1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0], [0, 0, 0, 1, 0, 0, 2, 0, 0, 1, 0, 0, 0], [1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0], 
				[0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0], [0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0], [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 2, 1, 0], [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1], [0, 0, 0, 1, 0, 0, 0, 0, 3, 1, 0, 0, 0]], 
			[[0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0], [0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0], [0, 0, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0], [1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0],
				[0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0], [0, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0, 1, 0, 1, 1], [0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 3], [0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0], 
				[0, 0, 3, 0, 0, 1, 0, 1, 0, 0, 2, 0, 0, 1, 0], [0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0], [0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0], [0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0], 
				[0, 1, 0, 1, 0, 1, 0, 0, 3, 0, 0, 1, 0, 0, 0], [0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0]],
			[[0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 2, 1, 0, 0, 0], [0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0], [0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 1, 3, 1, 0], [1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0],
				[0, 0, 0, 0, 3, 1, 0, 1, 0, 1, 0, 1, 2, 1, 0], [0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0], [0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0], [0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0], 
				[0, 0, 2, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0], [0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0], [0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0], [0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0], 
				[0, 0, 0, 1, 0, 1, 0, 0, 3, 0, 0, 1, 0, 0, 0], [0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0], [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0]],
			[[0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0], [0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0], [0, 0, 2, 0, 0, 0, 0, 1, 3, 1, 0, 1, 0, 0, 0, 1, 3], [0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1],
				[0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 2, 1, 0, 0, 0], [0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1], [0, 0, 0, 0, 3, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0], [1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 3, 1, 1, 1, 1],
				[0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0], [1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 3, 1, 0], 
				[0, 0, 0, 1, 0, 0, 2, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0], [0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1], [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0], [0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1], [0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0]]];
}

//Initializes the player's orientation and location, and starts the game sounds accordingly
function initializePlayer()
{
	xLocation=0;
	arrowRotation=90;
	
	yLocation=levels[levelNumber-1].length-1;
	
	startWindSounds();
	playWindSound('wind', arrowRotation);
	
	checkForObjects('treasure', true);
	checkForObjects('trap', false);
}

//Draws the level we are on as well as where we are
function draw()
{
	//If we are not falling and the game isn't paused for some other reason, refresh the display
	if(isGameRunning && !isGameOver && !isFalling && !playingLivesLeft && !playingScoreSound && !playingFinishedSound)
	{
		var i;
		var j;
		
		context2D.fillStyle="rgb(255, 255, 255)";
		context2D.beginPath();
		context2D.rect(0, 0, canvas.width, canvas.height);
		context2D.closePath();
		context2D.fill();
			
		for(i=0;i<levels[levelNumber-1][0].length+2;i++)
		{
			context2D.fillStyle=levelColors[levelNumber-1];
			context2D.beginPath();
			context2D.rect((canvas.width/(levels[levelNumber-1][0].length+2))*i, 0, (canvas.width/(levels[levelNumber-1][0].length+2)), (canvas.height/(levels[levelNumber-1].length+2)));
			context2D.closePath();
			context2D.fill();
		}
		
		for(i=0;i<levels[levelNumber-1].length;i++)
		{
			context2D.fillStyle=levelColors[levelNumber-1];
			context2D.beginPath();
			context2D.rect(0, (canvas.height/(levels[levelNumber-1].length+2))*(i+1), (canvas.width/(levels[levelNumber-1][0].length+2)), (canvas.height/(levels[levelNumber-1].length+2)));
			context2D.closePath();
			context2D.fill();
			
			for(j=0;j<levels[levelNumber-1][i].length;j++)
			{
				if(levels[levelNumber-1][i][j]<2)
				{
					if(levels[levelNumber-1][i][j]==0)
						context2D.fillStyle="rgb(255, 255, 255)";
						
					else if(levels[levelNumber-1][i][j]==1)
						context2D.fillStyle=levelColors[levelNumber-1];
					
					context2D.beginPath();
					context2D.rect((canvas.width/(levels[levelNumber-1][i].length+2))*(j+1), (canvas.height/(levels[levelNumber-1].length+2))*(i+1), (canvas.width/(levels[levelNumber-1][i].length+2)), (canvas.height/(levels[levelNumber-1].length+2)));
					context2D.closePath();
					context2D.fill();
				}
				
				//Draw trap?
				else if(levels[levelNumber-1][i][j]==2)
					context2D.drawImage(trap_image, (canvas.width/(levels[levelNumber-1][i].length+2))*(j+1), (canvas.height/(levels[levelNumber-1].length+2))*(i+1), (canvas.width/(levels[levelNumber-1][0].length+2)), (canvas.height/(levels[levelNumber-1].length+2)));
				
				//Draw coin treasure?
				else
					context2D.drawImage(coin_image, (canvas.width/(levels[levelNumber-1][i].length+2))*(j+1), (canvas.height/(levels[levelNumber-1].length+2))*(i+1), (canvas.width/(levels[levelNumber-1][0].length+2)), (canvas.height/(levels[levelNumber-1].length+2)));
			}
			
			if(i!=0)
			{
				context2D.fillStyle=levelColors[levelNumber-1];
				context2D.beginPath();
				context2D.rect((canvas.width/(levels[levelNumber-1][0].length+2))*(j+1), (canvas.height/(levels[levelNumber-1].length+2))*(i+1), (canvas.width/(levels[levelNumber-1][0].length+2)), (canvas.height/(levels[levelNumber-1].length+2)));
				context2D.closePath();
				context2D.fill();
			}
		}
		
		for(i=0;i<levels[levelNumber-1][0].length+2;i++)
		{
			context2D.fillStyle=levelColors[levelNumber-1];
			context2D.beginPath();
			context2D.rect((canvas.width/(levels[levelNumber-1][0].length+2))*i, (canvas.height/(levels[levelNumber-1].length+2))*(levels[levelNumber-1].length+1), (canvas.width/(levels[levelNumber-1][0].length+2)), (canvas.height/(levels[levelNumber-1].length+2)));
			context2D.closePath();
			context2D.fill();
		}
		
		drawArrow(arrowRotation);
	}
}

//Draw arrow
function drawArrow(angle)
{
	//Only draw arrow if it hasn't gone over trap
	if(toDrawArrow)
	{
		context2D.fillStyle="rgb(0, 0, 0)";
		context2D.beginPath();
		
		var currentX=(xLocation+1)*(canvas.width/(levels[levelNumber-1][0].length+2));
		var currentY=(yLocation+1)*(canvas.height/(levels[levelNumber-1].length+2));
		
		switch(angle)
		{
			//Point arrow to right (zero degrees)
			case 0:
			{
				context2D.moveTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY);
				context2D.lineTo(currentX+(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.6*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX, currentY+0.6*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX, currentY+0.4*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.4*(canvas.height/(levels[levelNumber-1].length+2)));
				
				break;
			}
			
			//Point arrow up (90 degrees)
			case 90:
			{
				context2D.moveTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY);
				context2D.lineTo(currentX+(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.6*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.6*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.4*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.4*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX, currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				
				break;
			}
			
			//Point arrow left (180 degrees)
			case 180:
			{
				context2D.moveTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY);
				context2D.lineTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.4*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.4*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.6*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.6*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX, currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				
				break;
			}
			
			//If none of other cases hold, point arrow down (270 degrees)
			default:
			{
				context2D.moveTo(currentX+0.4*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY);
				context2D.lineTo(currentX+0.4*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX, currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.5*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.6*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY+0.5*(canvas.height/(levels[levelNumber-1].length+2)));
				context2D.lineTo(currentX+0.6*(canvas.width/(levels[levelNumber-1][0].length+2)), currentY);
			}
		}
		
		context2D.closePath();
		context2D.fill();
	}
}

//Tells us about our current orientation, where we are relative to the exit, and about any traps nearby
function playHints()
{
	givingHint=true;
	
	var relativeDirections=['ahead of', 'to the right of', 'behind', 'to the left of'];
	var i;
	
	var firstNumberToSpeak=arrowRotation%180!=0 ? yLocation : (levels[levelNumber-1][0].length-xLocation+1);
	var secondNumberToSpeak=arrowRotation%180!=0 ? (levels[levelNumber-1][0].length-xLocation+1) : yLocation;
	
	audio.stop({channel : 'eighth'});
	
	//Check for nearby traps, and tell about them if they exist
	for(i=xLocation+1;i<levels[levelNumber-1][yLocation].length && levels[levelNumber-1][yLocation][i]!=1;i++)
		if(levels[levelNumber-1][yLocation][i]==2)
			speak('I think I see a trap '+(i-xLocation)+((i-xLocation)>1 ? 'steps' : 'step')+relativeDirections[arrowRotation/90]+'your current location.', 'eighth', true, none);
			
	for(i=yLocation-1;i>=0 && levels[levelNumber-1][i][xLocation]!=1;i--)
		if(levels[levelNumber-1][i][xLocation]==2)
			speak('I think I see a trap '+(yLocation-i)+((yLocation-i)>1 ? 'steps' : 'step')+relativeDirections[(arrowRotation/90+3)%relativeDirections.length]+'your current location.', 'eighth', true, none);
			
	for(i=xLocation-1;i>=0 && levels[levelNumber-1][yLocation][i]!=1;i--)
		if(levels[levelNumber-1][yLocation][i]==2)
			speak('I think I see a trap '+(xLocation-i)+((xLocation-i)>1 ? 'steps' : 'step')+relativeDirections[(arrowRotation/90+2)%relativeDirections.length]+'your current location.', 'eighth', true, none);
			
	for(i=yLocation+1;i<levels[levelNumber-1].length && levels[levelNumber-1][i][xLocation]!=1;i++)
		if(levels[levelNumber-1][i][xLocation]==2)
			speak('I think I see a trap '+(i-yLocation)+((i-yLocation)>1 ? 'steps' : 'step')+relativeDirections[(arrowRotation/90+1)%relativeDirections.length]+'your current location.', 'eighth', true, none);
			
	speak('I think I see the exit '+(firstNumberToSpeak==0 ? "" : firstNumberToSpeak+(firstNumberToSpeak!=1 ? ' steps ' : ' step ')+relativeDirections[(arrowRotation%180!=0 ? (arrowRotation/90+3)%relativeDirections.length : arrowRotation/90)]+(secondNumberToSpeak>0 ? ' you and ' : ' your current location.'))+(secondNumberToSpeak==0 ? "" : secondNumberToSpeak+(secondNumberToSpeak!=1 ? ' steps ' : ' step ')+relativeDirections[(arrowRotation%180!=0 ? arrowRotation/90 : (arrowRotation/90+3)%relativeDirections.length)]+' your current location.'), 'eighth', false, function()
	{
		givingHint=false;
	});
}

//When we press a key and the game is not paused, respond appropriately
function onKeyDown(evt) 
{
	if(isGameRunning && !isGameOver && !isFalling && !playingLivesLeft && !playingScoreSound && !playingFinishedSound && !introducing)
	{
		//Did we push the left arrow key?
		if (evt.keyCode == 37 && !evt.shiftKey)
			keyPressed = 1;
	  
		//Did we push the up arrow key?
		else if (evt.keyCode == 38 && !evt.shiftKey) 
			keyPressed = 2;
			
		//Did we push the right arrow key?
		else if (evt.keyCode == 39 && !evt.shiftKey)
			keyPressed = 3;
		
		//Did we push the Enter key?
		else if (evt.keyCode == 13)
			playHints();
		
		//If we pressed one of the arrow keys without pressing shift, move if we can
		if(evt.keyCode!=13 && !evt.shiftKey)
			movePlayer();
	}
	
	//Have we pressed the spacebar in order to skip the introduction?
	if(introducing && evt.keyCode==32)
	{
		audio.stop({channel : 'default'});
		
		//Set voice to default in case spacebar is pressed when female voice was speaking
		audio.setProperty({name : 'voice', channel : 'default', value : 'default'});
		
		initializeGame();
	}
}

//When we release a key, take note of it
function onKeyUp(evt)
{
	if(isGameRunning && !isGameOver && !isFalling && !playingLivesLeft && !playingScoreSound  && !playingFinishedSound && !introducing)
	{
		keyDown=0;
		keyPressed=0;
	}
}

//Moves or rotates the player depending on what key they pressed
function movePlayer()
{	
	if(!keyDown)
	{
		//Rotate left?
		if(keyPressed == 1)
		{
			arrowRotation=(arrowRotation+90)%360;
			playWindSound('wind', arrowRotation);
			
			checkForObjects('treasure', true);
			checkForObjects('trap', false);
		}
		
		//Move forward? If so, make sure sounds are updated to reflect new location
		else if(keyPressed == 2)
		{
			var originalXLocation=xLocation;
			var originalYLocation=yLocation;
			
			if(arrowRotation==0)
			{
				if(!(xLocation>=levels[levelNumber-1][yLocation].length-1 && yLocation>0) && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				{
					xLocation++;
					
					if(xLocation>=levels[levelNumber-1][yLocation].length+1)
					{
						//Update screen to have no arrow on it before actually saying that you have completed level
						toDrawArrow=false;
						draw();
						
						advanceToNextLevel();
					}
					
					else
						playWindSound('wind', arrowRotation);
				}
				
				else
					playBumpSound();
			}
			
			else if(arrowRotation==90)
			{
				if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1)
				{
					yLocation--;
					playWindSound('wind', arrowRotation);
				}
				
				else
					playBumpSound();
			}
			
			else if(arrowRotation==180)
			{
				if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1)
				{
					xLocation--;
					playWindSound('wind', arrowRotation);
				}
				
				else
					playBumpSound();
			}
			
			else if(arrowRotation==270)
			{
				if(yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1 && !(yLocation==0 && xLocation>=levels[levelNumber-1][yLocation].length))
				{
					yLocation++;
					playWindSound('wind', arrowRotation);
				}
				
				else
					playBumpSound();
			}
			
			//Have we fallen into a trap?
			if(levels[levelNumber-1][yLocation][xLocation]==2)
			{
				toDrawArrow=false;
				draw();
				
				lives--;
				playFallingSound();
			}
			
			//Do we have treasure?
			else if(levels[levelNumber-1][yLocation][xLocation]==3)
			{
				levels[levelNumber-1][yLocation][xLocation]=0;
				score++;
				
				playSound('Treasure_Sounds/treasure_obtained', 'default', 1, true, none);
			}
			
			checkForObjects('treasure', true);
			checkForObjects('trap', false);
			
			//If we have moved, play walking sound
			if(originalXLocation!=xLocation || originalYLocation!=yLocation)
				playSound('Other_Sounds/walk', 'seventh', RELATIVE_FOOTSTEP_VOLUME, true, none);
		}
		
		//Rotate right?
		else if(keyPressed == 3)
		{
			arrowRotation-=90;
			arrowRotation=(arrowRotation<0) ? arrowRotation+360 : arrowRotation;
			
			playWindSound('wind', arrowRotation);
			
			checkForObjects('treasure', true);
			checkForObjects('trap', false);
		}
	}
	
	keyDown=1;
}

//After we finish a level, advance to the next level
function advanceToNextLevel()
{	
	audio.stop({channel : 'custom'});
	audio.stop({channel : 'secondary'});
	audio.stop({channel : 'tertiary'});
	
	playingFinishedSound=true;
	toDrawArrow=true;
	
	playFinishedSound();
}

//Check to see if there is an object (a coin or trap) ahead of us, to the left of us, or to the right of us
function checkForObjects(sound_type, checking_for_coin)
{
	var i;
	
	switch(arrowRotation)
	{
		case 0:
		{
			//Check ahead of us
			for(i=xLocation+1;i<levels[levelNumber-1][yLocation].length;i++)
			{
				if(levels[levelNumber-1][yLocation][i]==1)
					break;
					
				else if((levels[levelNumber-1][yLocation][i]==2 && !checking_for_coin) || (levels[levelNumber-1][yLocation][i]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type, checking_for_coin, i-xLocation);
					break;
				}
			}
			
			//Check to our left
			for(i=yLocation-1;i>=0;i--)
			{
				if(levels[levelNumber-1][i][xLocation]==1)
					break;
					
				else if((levels[levelNumber-1][i][xLocation]==2 && !checking_for_coin) || (levels[levelNumber-1][i][xLocation]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_left_only', checking_for_coin, yLocation-i);
					break;
				}
			}
			
			//Check to our right
			for(i=yLocation+1;i<levels[levelNumber-1].length;i++)
			{
				if(levels[levelNumber-1][i][xLocation]==1)
					break;
					
				else if((levels[levelNumber-1][i][xLocation]==2 && !checking_for_coin) || (levels[levelNumber-1][i][xLocation]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_right_only', checking_for_coin, i-yLocation);
					break;
				}
			}
			
			break;
		}
		
		case 90:
		{
			//Check ahead of us
			for(i=yLocation-1;i>=0;i--)
			{
				if(levels[levelNumber-1][i][xLocation]==1)
					break;
					
				else if((levels[levelNumber-1][i][xLocation]==2 && !checking_for_coin) || (levels[levelNumber-1][i][xLocation]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type, checking_for_coin, yLocation-i);
					break;
				}
			}
			
			//Check to our left
			for(i=xLocation-1;i>=0;i--)
			{
				if(levels[levelNumber-1][yLocation][i]==1)
					break;
					
				else if((levels[levelNumber-1][yLocation][i]==2 && !checking_for_coin) || (levels[levelNumber-1][yLocation][i]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_left_only', checking_for_coin, xLocation-i);
					break;
				}
			}
			
			//Check to our right
			for(i=xLocation+1;i<levels[levelNumber-1][yLocation].length;i++)
			{
				if(levels[levelNumber-1][yLocation][i]==1)
					break;
					
				else if((levels[levelNumber-1][yLocation][i]==2 && !checking_for_coin) || (levels[levelNumber-1][yLocation][i]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_right_only', checking_for_coin, i-xLocation);
					break;
				}
			}
			
			break;
		}
		
		case 180:
		{
			//Check ahead of us
			for(i=xLocation-1;i>=0;i--)
			{
				if(levels[levelNumber-1][yLocation][i]==1)
					break;
					
				else if((levels[levelNumber-1][yLocation][i]==2 && !checking_for_coin) || (levels[levelNumber-1][yLocation][i]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type, checking_for_coin, xLocation-i);
					break;
				}
			}
			
			//Check to our left
			for(i=yLocation+1;i<levels[levelNumber-1].length;i++)
			{
				if(levels[levelNumber-1][i][xLocation]==1)
					break;
					
				else if((levels[levelNumber-1][i][xLocation]==2 && !checking_for_coin) || (levels[levelNumber-1][i][xLocation]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_left_only', checking_for_coin, i-yLocation);
					break;
				}
			}
			
			//Check to our right
			for(i=yLocation-1;i>=0;i--)
			{
				if(levels[levelNumber-1][i][xLocation]==1)
					break;
					
				else if((levels[levelNumber-1][i][xLocation]==2 && !checking_for_coin) || (levels[levelNumber-1][i][xLocation]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_right_only', checking_for_coin, yLocation-i);
					break;
				}
			}
			
			break;
		}
		
		default:
		{
			//Check ahead of us
			for(i=yLocation+1;i<levels[levelNumber-1].length;i++)
			{
				if(levels[levelNumber-1][i][xLocation]==1)
					break;
					
				else if((levels[levelNumber-1][i][xLocation]==2 && !checking_for_coin) || (levels[levelNumber-1][i][xLocation]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type, checking_for_coin, i-yLocation);
					break;
				}
			}
			
			//Check to our left
			for(i=xLocation+1;i<levels[levelNumber-1][yLocation].length;i++)
			{
				if(levels[levelNumber-1][yLocation][i]==1)
					break;
					
				else if((levels[levelNumber-1][yLocation][i]==2 && !checking_for_coin) || (levels[levelNumber-1][yLocation][i]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_left_only', checking_for_coin, i-xLocation);
					break;
				}
			}
			
			//Check to our right
			for(i=xLocation-1;i>=0;i--)
			{
				if(levels[levelNumber-1][yLocation][i]==1)
					break;
					
				else if((levels[levelNumber-1][yLocation][i]==2 && !checking_for_coin) || (levels[levelNumber-1][yLocation][i]==3 && checking_for_coin==true))
				{
					EvalObjectNearbySound(sound_type+'_right_only', checking_for_coin, xLocation-i);
					break;
				}
			}
			
			break;
		}
	}
}

//Play wind sounds into our ears depending on where we are in the maze as well as our rotation
function playWindSound(sound, rotation)
{
	//Consider special case where we are just to right of upper-right cell (which is the exit)
	if(xLocation>levels[levelNumber-1][yLocation].length-1 && yLocation==0)
	{
		if(rotation==0 || rotation==180)
			EvalWindSound(1, 0, 0);
			
		else
			EvalWindSound(0, 1, 1);
	}
	
	//Consider special case where we are just to left of exit
	else if(xLocation==levels[levelNumber-1][yLocation].length-1 && yLocation==0 && rotation!=180)
	{
		if(rotation==0)
		{
			if(levels[levelNumber-1][yLocation+1][xLocation]==0)
				EvalWindSound(1, 0, 2);
				
			else
				EvalWindSound(1, 0, 0);
		}
		
		else if(rotation==90 || rotation==270)
		{
			if(levels[levelNumber-1][yLocation][xLocation-1]==0)
			{
				if(rotation==90)
					EvalWindSound(0, 1, 1);
					
				else if(rotation==270)
				{
					if(levels[levelNumber-1][yLocation+1][xLocation]==0)
						EvalWindSound(1, 2, 2);
						
					else
						EvalWindSound(0, 1, 1);
				}
			}
			
			else
			{
				if(rotation==90)
					EvalWindSound(0, 0, 1);
					
				else
				{
					if(levels[levelNumber-1][yLocation+1][xLocation]==0)
						EvalWindSound(1, 2, 0);
						
					else
						EvalWindSound(0, 1, 0);
				}
			}
		}
	}
		
	else if(rotation==0)
	{
		//Is there a path ahead of us?
		if(xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
		{
			//Look at openings around us
			if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1 && yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(1, 2, 2);
			
			else if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1)
				EvalWindSound(1, 2, 0);
				
			else if(yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(1, 0, 2);
				
			else
				EvalWindSound(1, 0, 0);
		}
		
		else
		{
			//Look at openings around us
			if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1 && yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(0, 1, 1);
			
			else if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1)
				EvalWindSound(0, 1, 0);
				
			else if(yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(0, 0, 1);
				
			else
				EvalWindSound(0, 0, 0);
		}
	}
	
	else if(rotation==90)
	{
		//Is there a path ahead of us?
		if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1)
		{
			//Look at openings around us
			if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1 && xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(1, 2, 2);
			
			else if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1)
				EvalWindSound(1, 2, 0);
				
			else if(xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(1, 0, 2);
				
			else
				EvalWindSound(1, 0, 0);
		}
		
		else
		{
			//Look at openings around us
			if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1 && xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(0, 1, 1);
			
			else if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1)
				EvalWindSound(0, 1, 0);
				
			else if(xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(0, 0, 1);
				
			else
				EvalWindSound(0, 0, 0);
		}
	}
	
	else if(rotation==180)
	{
		//Is there a path ahead of us?
		if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1)
		{
			//Look at openings around us
			if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1 && yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(1, 2, 2);
			
			else if(yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(1, 2, 0);
				
			else if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1)
				EvalWindSound(1, 0, 2);
				
			else
				EvalWindSound(1, 0, 0);
		}
		
		else
		{
			//Look at openings around us
			if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1 && yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(0, 1, 1);
			
			else if(yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
				EvalWindSound(0, 1, 0);
				
			else if(yLocation>0 && levels[levelNumber-1][yLocation-1][xLocation]!=1)
				EvalWindSound(0, 0, 1);
				
			else
				EvalWindSound(0, 0, 0);
		}
	}
	
	else if(rotation==270)
	{
		//Is there a path ahead of us?
		if(yLocation<levels[levelNumber-1].length-1 && levels[levelNumber-1][yLocation+1][xLocation]!=1)
		{
			//Look at openings around us
			if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1 && xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(1, 2, 2);
			
			else if(xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(1, 2, 0);
				
			else if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1)
				EvalWindSound(1, 0, 2);
			
			else
				EvalWindSound(1, 0, 0);
		}
		
		else
		{
			//Look at openings around us
			if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1 && xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(0, 1, 1);
			
			else if(xLocation<levels[levelNumber-1][yLocation].length-1 && levels[levelNumber-1][yLocation][xLocation+1]!=1)
				EvalWindSound(0, 1, 0);
				
			else if(xLocation>0 && levels[levelNumber-1][yLocation][xLocation-1]!=1)
				EvalWindSound(0, 0, 1);
				
			else
				EvalWindSound(0, 0, 0);
		}
	}
}

//Checks to see if the game is paused or not
function pauseCallBack(paused)
{
	isGameRunning=!paused;
}

//Updates properties of the game such as volume and speech rate when they are updated on the Hark site
function prefsCallback(prefs, which)
{
	setSpeechRate(prefs.speechRate);
	
	if(!introducing)
		playWindSound('wind', arrowRotation); //Adjust wind sound volumes, since wind sounds last so long (unless we are introducing the game)
	
	masterVolume=prefs.volume;
	speechVolume=prefs.speechVolume;
	soundVolume=prefs.soundVolume;
	
	//Volume adjustment during long speeches and sounds
	if(introducing || givingHint || isFalling || playingFinishedSound || playingScoreSound || playingLivesLeft || isGameOver)
		audio.setProperty({name : 'volume', value : masterVolume*speechVolume, channel : (givingHint ? 'eighth' : 'default'), immediate : true});
}

//Sets the speech rate of all the audio channels
function setSpeechRate(rate)
{
	audio.setProperty({name : 'rate', channel : 'default', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'custom', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'secondary', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'tertiary', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'fourth', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'fifth', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'sixth', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'seventh', value : rate, immediate : true});
	audio.setProperty({name : 'rate', channel : 'eighth', value : rate, immediate : true});
}

//Speaks an utterance over a certain audio channel
function speak(string, audioChannel, shouldStop, afterFunction)
{
	if(shouldStop)
		audio.stop({channel : audioChannel});
		
	audio.setProperty({name : 'volume', channel : audioChannel, value : masterVolume*speechVolume, immediate : true});
	audio.say({text : string, channel : audioChannel}).callAfter(afterFunction);
}

//Plays a certain sound over a certain audio channel
function playSound(urlString, audioChannel, relativeVolume, shouldStop, afterFunction)
{
	if(shouldStop)
		audio.stop({channel : audioChannel});
		
	audio.setProperty({name : 'volume', channel : audioChannel, value : masterVolume*soundVolume*relativeVolume, immediate : true});
	audio.play({url : urlString, channel : audioChannel}).callAfter(afterFunction);
}

//A "do nothing" function for when we don't want to do anything after a sound finishes
function none()
{

}

//Plays a bumping sound when we hit a wall
function playBumpSound()
{
	playSound('Other_Sounds/bump', 'fourth', 1, true, none);
}

//Play a falling sound when we run over a trap
function playFallingSound()
{
	//Make sure any speech about our current location stops when we start falling
	audio.stop({channel : 'eighth'});
	
	playSound('Other_Sounds/falling', 'default', 1, true, function()
	{
		isFalling=false;
		toDrawArrow=true;
			
		if(lives>0)
		{
			playingLivesLeft=true;
			
			speak('You have fallen into a trap and died. '+(lives==1 ? 'You have '+lives+ 'life left.' : 'You have '+lives+ 'lives left.'), 'default', false, function()
			{
				playingLivesLeft=false;
				initializePlayer();
			});
		}
		
		else
			endGame();
	});
	
	audio.stop({channel : 'custom'});
	audio.stop({channel : 'secondary'});
	audio.stop({channel : 'tertiary'});
	
	isFalling=true;
}

//Says when we finish a level
function playFinishedSound()
{
	//Make sure any speech about our current location stops when we finish the level
	audio.stop({channel : 'eighth'});
	
	speak('Congratulations! You have finished level '+levelNumber, 'default', true, function()
	{
		if(levelNumber==10)
		{
			playSound('Other_Sounds/cheer', 'default', 1, false, none);
			speak('You have completed all of the levels! If you would like to continue, you will start again at level one. If you do not want to continue, close the browser window.', 'default', false, none);
			initializeLevels();
		}
		
		playingFinishedSound=false;
		
		levelNumber++;
		
		if(levelNumber>10)
			levelNumber=1;
		
		playingScoreSound=true;
	
		speak('Your current score is: '+score+' You have '+lives+(lives==1 ? ' life left.' : 'lives left'), 'default', false, function()
		{
			playingScoreSound=false;
			arrowRotation=90;
			
			initializePlayer();
		});
	});
}

//Play a certain wind sound
function EvalWindSound(ahead_volume_level, left_volume_level, right_volume_level) 
{
	audio.setProperty({name : 'volume', value : masterVolume*soundVolume*RELATIVE_VOLUMES[ahead_volume_level], channel : 'custom', immediate : true});
	audio.setProperty({name : 'volume', value : masterVolume*soundVolume*RELATIVE_VOLUMES[left_volume_level], channel : 'secondary', immediate : true});
	audio.setProperty({name : 'volume', value : masterVolume*soundVolume*RELATIVE_VOLUMES[right_volume_level], channel : 'tertiary', immediate : true});
}

//Play a certain "object nearby" sound (whether the object is a coin or trap)
function EvalObjectNearbySound(sound, checking_for_coin, distance)
{
	if(checking_for_coin==false)
		playSound('Trap_Sounds/'+sound, 'fifth', Math.pow(1-SOUND_DECREASE_FACTOR, distance-1), true, none);
	
	else
		playSound('Treasure_Sounds/'+sound, 'sixth', Math.pow(1-SOUND_DECREASE_FACTOR, distance-1), true, none);
}

//Ends the game if we run out of lives
function endGame()
{
	isGameOver=true;
	
	audio.stop({channel : 'custom'});
	audio.stop({channel : 'secondary'});
	audio.stop({channel : 'tertiary'});
	
	//Clear the screen
	context2D.fillStyle="rgb(255, 255, 255)";
	context2D.beginPath();
	context2D.rect(0, 0, canvas.width, canvas.height);
	context2D.closePath();
	context2D.fill();
	
	playSound('Other_Sounds/game_over', 'default', 1, false, none);
	speak('You have fallen into a trap and died. Game over, Your final score is: '+score, 'default', false);
}