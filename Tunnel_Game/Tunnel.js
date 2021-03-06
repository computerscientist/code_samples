dojo.provide('Tunnel');

dojo.declare('Tunnel', null, 
{
	FPS: 50,
	GAP_DECREASE_FACTOR: 0.985,
	
	audio: null,
	canvas: null,
	context2D: null,

	bufferLoader: null,
	webkitAudioContext: null,
	
	leftGainNode: null,
	leftTunnelSource: null,
	rightGainNode: null,
	rightTunnelSource: null,
	
	currentX: 300,
	initialGap: 400,

	shipXCoordinate: 300,

	iterationsBetweenXChanges: 20,
	iterationsSinceLastXChange: 19,
	xChange: 0,

	health: 10,
	isGameRunning: true,
	keyPressed: 0,

	timeToNextTreasure: null,
	treasureXCoordinate: -50,
	treasureYCoordinate: -50,
	treasureRadius: 15,
	treasureFalling: false,

	score: 0,
	
	iterationsBetweenCoinRings: 40,
	iterationsSinceLastCoinRing: 39,
	
	startCollisionIndex: 56, //Used to help with ship collision detection
	currentTopBlockIndex: 0, //Used to help with treasure placement

	sources: null,

	//Used to keep track of where the tunnel blocks are as well as the gap between the two tunnel walls
	tunnelArrayX: null,
	tunnelArrayY: null,
	tunnelArrayGap: null,

	background_image: new Image(),
	
	//Initializes the tunnel game
	constructor: function()
	{
		this.sources=new Array(8);
		this.tunnelArrayX=new Array(60);
		this.tunnelArrayY=new Array(60);
		this.tunnelArrayGap=new Array(60);
		
		this.webkitAudioContext=new webkitAudioContext();
		
		this.leftGainNode=this.webkitAudioContext.createGainNode();
		this.rightGainNode=this.webkitAudioContext.createGainNode();
		
		this.leftTunnelSource=this.webkitAudioContext.createBufferSource();
		this.rightTunnelSource=this.webkitAudioContext.createBufferSource();
		
		this.bufferLoader = new BufferLoader(
			this.webkitAudioContext,
			['coin-0.mp3', 'coin-90.mp3', 'coin-180.mp3', 'crash_left.mp3', 'crash_right.mp3', 'treasure.mp3', 'wall_left.mp3', 'wall_right.mp3'],
			dojo.hitch(this, this.initializeGame)
		);

		this.bufferLoader.load();
    },
	
	//Initializes the game variables
	initializeGame: function(bufferList)
	{
		//Initializes the buffer sources that will be used for game sounds
		for(var i=0;i<8;i++)
		{
			this.sources[i]=this.webkitAudioContext.createBufferSource();
			this.sources[i].buffer=bufferList[i];
			this.sources[i].connect(this.webkitAudioContext.destination);
		}
		
		this.canvas = document.getElementById('canvas');
		this.context2D = canvas.getContext('2d');
		
		//Initializes the tunnel block locations
		for(var i=0;i<60;i++)
		{
			this.tunnelArrayX[i]=this.currentX;
			this.tunnelArrayY[i]=-10+10*i;
			this.tunnelArrayGap[i]=this.initialGap;
		}
	
		this.background_image.src="Images/Galaxy_Background.png";
		this.timeToNextTreasure=Math.floor(Math.random()*8*this.FPS)+2*this.FPS;
		
		//Used for speech...
		myAudio.initialize();
		
		//Starts playing the sounds for each tunnel wall
		this.playTunnelSound(this.sources[6], this.leftTunnelSource, this.leftGainNode, Math.max(0, 1-(2.0*(this.shipXCoordinate-30-(this.tunnelArrayX[(this.startCollisionIndex+2)%60]-this.tunnelArrayGap[(this.startCollisionIndex+2)%60]/2)))/(this.tunnelArrayGap[(this.startCollisionIndex+2)%60])));
		this.playTunnelSound(this.sources[7], this.rightTunnelSource, this.rightGainNode, Math.max(0, 1-(2.0*(this.tunnelArrayX[(this.startCollisionIndex+2)%60]+this.tunnelArrayGap[(this.startCollisionIndex+2)%60]/2-(this.shipXCoordinate+30)))/(this.tunnelArrayGap[(this.startCollisionIndex+2)%60])));
			
		document.onkeydown=dojo.hitch(this, this.onKeyDown);
		document.onkeyup=dojo.hitch(this, this.onKeyUp);
		
		setInterval(dojo.hitch(this, this.draw), 1000/this.FPS);
	},
	
	//Plays any sound other than a tunnel wall sound
	playSound: function(buffer)
	{
		var gainNode=this.webkitAudioContext.createGainNode();

		var source=this.webkitAudioContext.createBufferSource();
		source.buffer=buffer.buffer;
		source.connect(gainNode);
		
		gainNode.connect(this.webkitAudioContext.destination);
		gainNode.gain.value=0.5;
		source.noteOn(0);
	},
	
	//Plays a sound from either tunnel wall according to how close your ship is to it
	playTunnelSound: function(buffer, source, gainNode, volume)
	{
		source = this.webkitAudioContext.createBufferSource();
		source.buffer=buffer.buffer;
		source.connect(gainNode);
		
		gainNode.connect(this.webkitAudioContext.destination);
		gainNode.gain.value=volume;
		
		source.loop=true;
		source.noteOn(0);
	},
	
	//Draws onto the screen repeatedly as the game plays
	draw: function()
	{
		//Clear screen
		this.context2D.fillStyle="rgb(255, 255, 255)";
		this.context2D.beginPath();
		this.context2D.rect(0, 0, this.canvas.width, this.canvas.height);
		this.context2D.closePath();
		this.context2D.fill();
			
		//Show final score if game is over
		if(!this.isGameRunning)
		{
			this.context2D.fillStyle='red';
			this.context2D.font='48px courier';
			this.context2D.fillText("Score: "+this.score, this.canvas.width/2-175, this.canvas.height/2-10);
			
			return;
		}
		
		else
		{
			this.leftGainNode.gain.value=Math.max(0, 1-(2.0*(this.shipXCoordinate-30-(this.tunnelArrayX[(this.startCollisionIndex+2)%60]-this.tunnelArrayGap[(this.startCollisionIndex+2)%60]/2)))/(this.tunnelArrayGap[(this.startCollisionIndex+2)%60]));
			this.rightGainNode.gain.value=Math.max(0, 1-(2.0*(this.tunnelArrayX[(this.startCollisionIndex+2)%60]+this.tunnelArrayGap[(this.startCollisionIndex+2)%60]/2-(this.shipXCoordinate+30)))/(this.tunnelArrayGap[(this.startCollisionIndex+2)%60]));
			
			this.score++;
			
			//Is it time for a new treasure to scroll down with the tunnel?
			if(this.timeToNextTreasure<=0 && !this.treasureFalling)
			{
				this.treasureFalling=true;
				this.treasureXCoordinate=this.tunnelArrayX[this.currentTopBlockIndex]+this.random(-this.tunnelArrayGap[this.currentTopBlockIndex]*0.3, this.tunnelArrayGap[this.currentTopBlockIndex]*0.3);
				this.treasureYCoordinate=-this.treasureRadius;
			}
			
			else
				this.timeToNextTreasure--;
		}
		
		this.context2D.drawImage(this.background_image, 0, 0, this.canvas.width, this.canvas.height);
		
		//Is there a treasure on the screen?
		if(this.treasureFalling)
		{
			this.context2D.beginPath();
			this.context2D.arc(this.treasureXCoordinate, this.treasureYCoordinate, this.treasureRadius, 0, 2*Math.PI, false);
			this.context2D.fillStyle='orange';
			this.context2D.closePath();
			this.context2D.fill();
			this.context2D.lineWidth=1;
			this.context2D.strokeStyle='black';
			this.context2D.stroke();
			
			this.iterationsSinceLastCoinRing++;
			
			//Is it time to play a "coin ring" sound for the falling treasure?
			if(this.iterationsSinceLastCoinRing>=this.iterationsBetweenCoinRings)
			{
				this.iterationsSinceLastCoinRing=0;
				
				//Play coin ring sound according to where coin is relative to ship
				if(this.treasureXCoordinate<this.shipXCoordinate-15)
					this.playSound(this.sources[2]);
				
				else if(this.treasureXCoordinate>=this.shipXCoordinate-15 && this.treasureXCoordinate<=this.shipXCoordinate+15)	
					this.playSound(this.sources[1]);
					
				else
					this.playSound(this.sources[0]);
			}	
		}
		
		//Draw the ship
		this.context2D.beginPath();
		this.context2D.moveTo(this.shipXCoordinate, 540);
		this.context2D.fillStyle='gray';
		this.context2D.lineTo(this.shipXCoordinate+10, 575);
		this.context2D.lineTo(this.shipXCoordinate+30, 590);
		this.context2D.lineTo(this.shipXCoordinate, 582);
		this.context2D.lineTo(this.shipXCoordinate-30, 590);
		this.context2D.lineTo(this.shipXCoordinate-10, 575);
		this.context2D.lineTo(this.shipXCoordinate, 540);
		this.context2D.closePath();
		this.context2D.fill();
		this.context2D.lineWidth=2;
		this.context2D.strokeStyle='black';
		this.context2D.stroke();
		
		//Draw each block of the tunnel walls
		for(var i=0;i<this.tunnelArrayY.length;i++)
		{
			this.context2D.beginPath();
			this.context2D.rect(this.tunnelArrayX[i]-this.tunnelArrayGap[i]/2, this.tunnelArrayY[i], 10, 10);
			this.context2D.fillStyle='green';
			this.context2D.closePath();
			this.context2D.fill();
			this.context2D.lineWidth=1;
			this.context2D.strokeStyle='black';
			this.context2D.stroke();
			
			this.context2D.beginPath();
			this.context2D.rect(this.tunnelArrayX[i]+this.tunnelArrayGap[i]/2, this.tunnelArrayY[i], 10, 10);
			this.context2D.fillStyle='green';
			this.context2D.closePath();
			this.context2D.fill();
			this.context2D.lineWidth=1;
			this.context2D.strokeStyle='black';
			this.context2D.stroke();
			
			this.context2D.fillStyle='green';
			this.tunnelArrayY[i]=this.tunnelArrayY[i]+5;
			
			//Make tunnel scroll down
			if(this.tunnelArrayY[i]>=this.canvas.height)
			{
				this.tunnelArrayY[i]=0;
				this.iterationsSinceLastXChange++;
				
				this.startCollisionIndex--;
				this.currentTopBlockIndex--;
				
				if(this.startCollisionIndex<0)
					this.startCollisionIndex+=this.tunnelArrayY.length;
				
				if(this.currentTopBlockIndex<0)
					this.currentTopBlockIndex+=this.tunnelArrayY.length;
				
				//Make tunnel drift from left to right at different times
				if(this.iterationsSinceLastXChange>=this.iterationsBetweenXChanges)
				{
					if(this.xChange>0)
						this.xChange=(Math.random()<0.5) ? 0 : -2;
						
					else if(this.xChange==0)
						this.xChange=(Math.random()<0.5) ? 2 : -2;
					
					else
						this.xChange=(Math.random()<0.5) ? 0 : 2;
						
					this.currentX+=this.xChange;
					
					this.iterationsBetweenXChanges=Math.floor(Math.random()*(this.xChange==0 ? 5 : 20)+(this.xChange==0 ? 0 : 10));
					this.iterationsSinceLastXChange=0;
				}
				
				else
					this.currentX+=this.xChange;
				
				if(this.currentX+this.tunnelArrayGap[i]/2>this.canvas.width-10)
					this.currentX=this.canvas.width-(this.tunnelArrayGap[i]/2+10);
				
				else if(this.currentX-this.tunnelArrayGap[i]/2<0)
					this.currentX=this.tunnelArrayGap[i]/2;
				
				this.tunnelArrayX[i]=this.currentX;

				//Make the tunnel narrower over time
				this.tunnelArrayGap[i]=Math.max(20, this.tunnelArrayGap[i]*this.GAP_DECREASE_FACTOR);
			}
		}
		
		if(this.treasureFalling)
		{
			//See if we got treasure!
			if(this.treasureYCoordinate==585 && this.treasureXCoordinate>=this.shipXCoordinate-32 && this.treasureXCoordinate<=this.shipXCoordinate+32)
			{
				this.score+=100;
				this.playSound(this.sources[5]);
				
				this.treasureXCoordinate=-50;
				this.treasureYCoordinate=-50;
				
				this.treasureFalling=false;
				this.iterationsSinceLastCoinRing=39;
				
				this.timeToNextTreasure=Math.floor(Math.random()*250)+100;
			}	
			
			//Otherwise, the treasure just keeps scrolling down with tunnel
			else
			{
				this.treasureYCoordinate+=5;
			
				//Has the treasure reached the bottom of the screen? If so, decide length of time to next treasure
				if(this.treasureYCoordinate>600)
				{
					this.treasureFalling=false;
					this.timeToNextTreasure=Math.floor(Math.random()*250)+100;
				}
			}
		}
			
		var j;
		
		//Check bottom four tunnel blocks on the screen for collisions
		for(j=0;j<4;j++)
		{
			//Collision on left side...
			if(this.shipXCoordinate<=this.tunnelArrayX[(this.startCollisionIndex+j)%this.tunnelArrayY.length]-this.tunnelArrayGap[(this.startCollisionIndex+j)%this.tunnelArrayY.length]/2+32)
			{
				this.playSound(this.sources[3]);
				
				this.health--;
				this.shipXCoordinate+=60; //"Bounce" off wall after collision
				
				//Have we run out of health?
				if(this.health<1)
				{
					this.isGameRunning=false;
					
					this.leftGainNode.gain.value=0;
					this.rightGainNode.gain.value=0;
					
					this.leftTunnelSource.noteOff(0);
					this.rightTunnelSource.noteOff(0);
					
					myAudio.say('child', 'en', 'You were killed by the space tunnel! Your final score is: '+this.score);
				}
			}
			
			//Collision on right side...
			else if(this.shipXCoordinate>=this.tunnelArrayX[(this.startCollisionIndex+j)%this.tunnelArrayY.length]+this.tunnelArrayGap[(this.startCollisionIndex+j)%this.tunnelArrayY.length]/2-32)
			{
				this.playSound(this.sources[4]);
				
				this.health--;
				this.shipXCoordinate-=60; //"Bounce" off wall after collision
				
				//Have we run out of health?
				if(this.health<1)
				{	
					this.isGameRunning=false;
					
					this.leftGainNode.gain.value=0;
					this.rightGainNode.gain.value=0;
					
					this.leftTunnelSource.noteOff(0);
					this.rightTunnelSource.noteOff(0);
					
					myAudio.say('child', 'en', 'You were killed by the space tunnel! Your final score is: '+this.score);
				}
			}
		}
	},
	
	//Detects when a key is pressed and responds accordingly
	onKeyDown: function(evt)
	{
		if(this.isGameRunning)
		{
			//Did we push the left arrow key?
			if (evt.keyCode == 37 && !evt.shiftKey)
			{
				this.shipXCoordinate-=5;
				this.keyPressed = 1;
			}
			
			//Did we push the right arrow key?
			else if (evt.keyCode == 39 && !evt.shiftKey)
			{
				this.shipXCoordinate+=5;
				this.keyPressed = 3;
			}
		}
	},

	//Detects when a key is released
	onKeyUp: function(evt)
	{
		if(this.isGameRunning)
			keyPressed=0;
	},
	
	//Returns a random value between the lower and upper bounds (inclusive)
	random: function(lower, upper)
	{
		return lower+Math.random()*(upper-lower);
	}
});

dojo.ready(function()
{
    new Tunnel();
});
