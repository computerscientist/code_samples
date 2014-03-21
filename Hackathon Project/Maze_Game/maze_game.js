dojo.provide('maze_game');

dojo.declare('maze_game', null,
{
	canvas: null,
	context2D: null,
	FPS: 5,
	keyPressed: false,
	maze: null,
	isGameRunning: true,
	hasWon: false,
	backgroundImage: new Image(),
	finishImage: new Image(),
	playerImage: new Image(),
	wallImage: new Image(),
	tileWidth: 50,
	sideLength:13,
	//playerRelativeSize: 0.33,
	current_row: 51,
	current_column: 51,

	constructor: function()
	{
		this.canvas=document.getElementById("canvas");
		this.context2D=canvas.getContext('2d');

		this.backgroundImage.src="white.png";
		this.finishImage.src="yellow.png";
		this.playerImage.src="blue.png";
		this.wallImage.src="red.png";

		document.onkeydown=dojo.hitch(this, this.onKeyDown);
		document.onkeyup=dojo.hitch(this, this.onKeyUp);

		this.initializeGame();
	},

	initializeGame: function()
	{
		this.readMazeFile();
	},

	readMazeFile: function()
	{
		var mazeFile=new XMLHttpRequest();
		mazeFile.onreadystatechange=dojo.hitch(this, function()
		{
			if(mazeFile.readyState===4)
			{
				if(mazeFile.status===200 || mazeFile.status===0)
				{
					var fileText=mazeFile.responseText;
					this.generateGameMaze(fileText);
					setInterval(dojo.hitch(this, this.draw), 1000/this.FPS);
				}

				else
					alert("Error loading game!");
			}
		});

		mazeFile.open("GET", "maze_file.txt", true);
		mazeFile.send(null);
	},

	generateGameMaze: function(fileText)
	{
		this.maze=new Array();
		var currentMazeRow=new Array();
		var i;

		for(i=0;i<fileText.length;i++)
		{
			if(fileText[i]!='\n')
				currentMazeRow.push(fileText[i])

			else
			{
				this.maze.push(currentMazeRow);
				currentMazeRow=new Array();
			}
		}
	},

	evaluateJSON: function()
	{
		var xhttp =new XMLHttpRequest();
        xhttp.onreadystatechange=dojo.hitch(this, function()
        {
            if (xhttp.readyState===4 && xhttp.status===200)
            {
                    var obj = JSON.parse(xhttp.responseText);
                    
                    //only read if non-empty
                    if(!obj.hasOwnProperty('blank')){
                    	
                          //  alert(obj.player);

                   	//Set player color
							if(obj.player.red)
								this.setPlayerImage('../assets/player/red.png');
							else if(obj.player.blue)
								this.setPlayerImage('../assets/player/blue.png');
							else if(obj.player.green)
								this.setPlayerImage('../assets/player/green.png');
							else if(obj.player.black)
								this.setPlayerImage('../assets/player/black.png');
							else if(obj.player.yellow)
								this.setPlayerImage('../assets/player/yellow.png');
							else if(obj.player.white)
								this.setPlayerImage('../assets/player/white.png');
							else if(obj.player.purple)
								this.setPlayerImage('../assets/player/purple.png');
							else if(obj.player.orange)
								this.setPlayerImage('../assets/player/orange.png');
							else if(obj.player.normal)
								this.setPlayerImage('../assets/player/normal.png');
							else if(obj.player.zelda)
								this.setPlayerImage('../assets/player/zelda.png');
							else if(obj.player.pokemon)
								this.setPlayerImage('../assets/player/pokemon.png');
							else if(obj.player.hacknc)
								this.setPlayerImage('../assets/player/pokemon.png');
							else if(obj.player.doge)
								this.setPlayerImage('../assets/player/pokemon.png');
							
							if(obj.wall.red)
								this.setWallImage('../assets/wall/red.png');
							else if(obj.wall.blue)
								this.setWallImage('../assets/wall/blue.png');
							else if(obj.wall.green)
								this.setWallImage('../assets/wall/green.png');
							else if(obj.wall.black)
								this.setWallImage('../assets/wall/black.png');
							else if(obj.wall.yellow)
								this.setWallImage('../assets/wall/yellow.png');
							else if(obj.wall.white)
								this.setWallImage('../assets/wall/white.png');
							else if(obj.wall.purple)
								this.setWallImage('../assets/wall/purple.png');
							else if(obj.wall.orange)
								this.setWallImage('../assets/wall/orange.png');
							else if(obj.wall.normal)
								this.setWallImage('../assets/wall/normal.png');
							else if(obj.wall.zelda)
								this.setWallImage('../assets/wall/zelda.png');
							else if(obj.wall.pokemon)
								this.setWallImage('../assets/wall/pokemon.png');
							else if(obj.wall.hacknc)
								this.setWallImage('../assets/wall/pokemon.png');
							else if(obj.wall.doge)
								this.setWallImage('../assets/wall/pokemon.png');
							
							
							if(obj.wall.red)
								this.setWallImage('../assets/wall/red.png');
							else if(obj.wall.blue)
								this.setWallImage('../assets/wall/blue.png');
							else if(obj.wall.green)
								this.setWallImage('../assets/wall/green.png');
							else if(obj.wall.black)
								this.setWallImage('../assets/wall/black.png');
							else if(obj.wall.yellow)
								this.setWallImage('../assets/wall/yellow.png');
							else if(obj.wall.white)
								this.setWallImage('../assets/wall/white.png');
							else if(obj.wall.purple)
								this.setWallImage('../assets/wall/purple.png');
							else if(obj.wall.orange)
								this.setWallImage('../assets/wall/orange.png');
							else if(obj.wall.normal)
								this.setWallImage('../assets/wall/normal.png');
							else if(obj.wall.zelda)
								this.setWallImage('../assets/wall/zelda.png');
							else if(obj.wall.pokemon)
								this.setWallImage('../assets/wall/pokemon.png');
							else if(obj.wall.hacknc)
								this.setWallImage('../assets/wall/pokemon.png');
							else if(obj.wall.doge)
								this.setWallImage('../assets/wall/pokemon.png');
							
							
							if(obj.ground.red)
								this.setBackgroundImage('../assets/ground/red.png');
							else if(obj.ground.blue)
								this.setBackgroundImage('../assets/ground/blue.png');
							else if(obj.ground.green)
								this.setBackgroundImage('../assets/ground/green.png');
							else if(obj.ground.black)
								this.setBackgroundImage('../assets/ground/black.png');
							else if(obj.ground.yellow)
								this.setBackgroundImage('../assets/ground/yellow.png');
							else if(obj.ground.white)
								this.setBackgroundImage('../assets/ground/white.png');
							else if(obj.ground.purple)
								this.setBackgroundImage('../assets/ground/purple.png');
							else if(obj.ground.orange)
								this.setBackgroundImage('../assets/ground/orange.png');
							else if(obj.ground.normal)
								this.setBackgroundImage('../assets/ground/normal.png');
							else if(obj.ground.zelda)
								this.setBackgroundImage('../assets/ground/zelda.png');
							else if(obj.ground.pokemon)
								this.setBackgroundImage('../assets/ground/pokemon.png');
							else if(obj.ground.hacknc)
								this.setBackgroundImage('../assets/ground/hacknc.png');
							else if(obj.ground.doge)
								this.setBackgroundImage('../assets/ground/doge.png');
							
							
							if(obj.normal) {
								this.setPlayerImage('../assets/player/normal.png');
								this.setWallImage('.../assets/wall/normal.png');
								this.setBackgroundImage('.../assets/ground/normal.png');
							} else if(obj.zelda) {
								this.setPlayerImage('../assets/player/zelda.png');
								this.setWallImage('.../assets/wall/zelda.png');
								this.setBackgroundImage('.../assets/ground/zelda.png');
							} else if(obj.pokemon) {
								this.setPlayerImage('../assets/player/pokemon.png');
								this.setWallImage('.../assets/wall/pokemon.png');
								this.setBackgroundImage('.../assets/ground/pokemon.png');
							} else if(obj.hacknc) {
								this.setPlayerImage('../assets/player/hacknc.png');
								this.setWallImage('.../assets/wall/hacknc.png');
								this.setBackgroundImage('.../assets/ground/hacknc.png');
							} else if(obj.doge) {
								this.setPlayerImage('../assets/player/doge.png');
								this.setWallImage('.../assets/wall/doge.png');
								this.setBackgroundImage('.../assets/ground/doge.png');
							}
							
							if(obj.left)
							{
								if(this.current_column>0 && this.maze[this.current_row][this.current_column-1]!="O")
									this.current_column--;
							}	
							else if(obj.right)
							{
								if(this.current_column<this.maze[0].length-1 && this.maze[this.current_row][this.current_column+1]!="O")
									this.current_column++;
							}
							else if(obj.up)
							{
								if(this.current_row>0 && this.maze[this.current_row-1][this.current_column]!="O")
									this.current_row--;
							}
							else if(obj.down)
							{
								if(this.current_row<this.maze.length-1 && this.maze[this.current_row+1][this.current_column]!="O")
									this.current_row++;
							}
							
							//WRITE JSON
							//convert to string, store string in file
							//var json = JSON.stringify(obj);
							//var encoded = btoa(json);
							
							//use ajax for sending 
							var xhr = new XMLHttpRequest();
							xhr.open('POST','savejson.php',true);
							xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
							xhr.send();		
						}

            }
        });
        xhttp.open("GET", "../dataTemp.json", true);
        xhttp.send(null);
        
        
	},

	draw: function()
	{
		//UPDATE VALUES FROM JSON
		this.evaluateJSON();
		
		var row, column;

		//Are we in middle of maze or near its top and/or left edges?
		if(this.current_row+2<this.maze.length && this.current_column+2<this.maze[0].length)
		{
			for(row=0;row<this.sideLength;row++)
			{
				for(column=0;column<this.sideLength;column++)
				{
					if(this.maze[Math.max(this.current_row+row-2, row)][Math.max(this.current_column+column-2, column)]=='O')
						this.draw_image(this.wallImage, column*this.canvas.width/this.sideLength, row*this.canvas.height/this.sideLength, 
							this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else if(this.maze[Math.max(this.current_row+row-2, row)][Math.max(this.current_column+column-2, column)]=='G')
						this.draw_image(this.finishImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else
						this.draw_image(this.backgroundImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
				}
			}

			this.draw_image(this.playerImage, 
				Math.min(2*this.canvas.width/this.sideLength, this.canvas.width/this.sideLength*this.current_column)+this.canvas.width/(this.sideLength*3), 
				Math.min(2*this.canvas.height/this.sideLength, this.canvas.height/this.sideLength*this.current_row)+this.canvas.height/(this.sideLength*3), 
				this.canvas.width/(this.sideLength*3), 
				this.canvas.height/(this.sideLength*3));
		}

		//Are we in the lower right corner next to the goal?
		else if(this.current_row+2==this.maze.length && this.current_column+2==this.maze[0].length)
		{
			for(row=0;row<this.sideLength;row++)
			{
				for(column=0;column<this.sideLength;column++)
				{
					if(this.maze[this.maze.length-5+row][this.maze[0].length-5+column]=='O')
						this.draw_image(this.wallImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else if(this.maze[this.maze.length-5+row][this.maze[0].length-5+column]=='G')
						this.draw_image(this.finishImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else
						this.draw_image(this.backgroundImage, 
							column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, 
							this.canvas.width/this.sideLength, 
							this.canvas.height/this.sideLength);
				}
			}

			this.draw_image(this.playerImage, 
				3*this.canvas.width/this.sideLength+this.canvas.width/(this.sideLength*3), 
				3*this.canvas.height/this.sideLength+this.canvas.height/(this.sideLength*3), 
				this.canvas.width/(this.sideLength*3), 
				this.canvas.height/(this.sideLength*3));
		}

		//Are we at the bottom of the maze?
		else if(this.current_row+2==this.maze.length)
		{
			for(row=0;row<this.sideLength;row++)
			{
				for(column=0;column<this.sideLength;column++)
				{
					if(this.maze[this.maze.length-5+row][Math.max(this.current_column+column-2, column)]=='O')
						this.draw_image(this.wallImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else if(this.maze[this.maze.length-5+row][Math.max(this.current_column+column-2, column)]=='G')
						this.draw_image(this.finishImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else
						this.draw_image(this.backgroundImage, 
						column*this.canvas.width/this.sideLength, 
						row*this.canvas.height/this.sideLength, 
						this.canvas.width/this.sideLength, 
						this.canvas.height/this.sideLength);
				}
			}

			this.draw_image(this.playerImage, 
				Math.min(2*this.canvas.width/this.sideLength, 
					 this.canvas.width/this.sideLength*this.current_column)+this.canvas.height/(this.sideLength*3),
					 3*this.canvas.height/this.sideLength+this.canvas.height/(this.sideLength*3), 
					 this.canvas.width/(this.sideLength*3), 
					 this.canvas.height/(this.sideLength*3));
		}

		//Are we on the right side of the maze?
		else
		{
			for(row=0;row<this.sideLength;row++)
			{
				for(column=0;column<this.sideLength;column++)
				{
					if(!this.isGameRunning)
						return;

					if(this.maze[Math.max(this.current_row+row-2, row)][this.maze[0].length-5+column]=='O')
						this.draw_image(this.wallImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else if(this.maze[this.maze.length-5+row][Math.max(this.current_column+column-2, column)]=='G')
						this.draw_image(this.finishImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
					else
						this.draw_image(this.backgroundImage, column*this.canvas.width/this.sideLength, 
							row*this.canvas.height/this.sideLength, this.canvas.width/this.sideLength, this.canvas.height/this.sideLength);
				}
			}

			this.draw_image(this.playerImage, 
				3*this.canvas.width/this.sideLength+this.canvas.width/(this.sideLength*3), 
				Math.min(2*this.canvas.height/this.sideLength, this.canvas.height/this.sideLength*this.current_row)+this.canvas.height/(this.sideLength*3), 
				this.canvas.width/(this.sideLength*3), 
				this.canvas.height/(this.sideLength*3));
		}
	},

	draw_image: function(img, x, y, width, height)
	{
		this.context2D.drawImage(img, x, y, width, height);
	},

	onKeyDown: function(evt)
	{
		if(this.isGameRunning)
		{
			//Did we push the left arrow key?
			if(evt.keyCode==37)
			{
				this.keyPressed=true;

				if(this.current_column>0 && this.maze[this.current_row][this.current_column-1]!="O")
					this.current_column--;
			}

			//Did we push the up arrow key?
			else if(evt.keyCode==38)
			{
				this.keyPressed=true;

				if(this.current_row>0 && this.maze[this.current_row-1][this.current_column]!="O")
					this.current_row--;
			}

			//Did we push the right arrow key?
			else if(evt.keyCode==39)
			{
				this.keyPressed=true;

				if(this.current_column<this.maze[0].length-1 && this.maze[this.current_row][this.current_column+1]!="O")
					this.current_column++;
			}

			//Did we push the down arrow key?
			else if(evt.keyCode==40)
			{
				this.keyPressed=true;

				if(this.current_row<this.maze.length-1 && this.maze[this.current_row+1][this.current_column]!="O")
					this.current_row++;
			}

			if(this.maze[this.current_row][this.current_column]=='G')
			{
				this.isGameRunning=false;
				this.hasWon=true;
			}
		}

		else if(this.hasWon)
		{
			//Did we push down the spacebar?
			if(evt.keyCode==32)
			{
				this.isGameRunning=true;
				this.hasWon=false;

				this.current_row=51;
				this.current_column=51;
			}
		}
	},

	onKeyUp: function(evt)
	{
		if(this.isGameRunning)
			this.keyPressed=this.false;
	},

	setPlayerImage: function(img)
	{
		
		this.playerImage=this.resize(img);
	},

	setWallImage: function(img)
	{
		this.wallImage=this.resize(img);
	},

	setFinishImage: function(img)
	{
		this.finishImage=img;
	},

	setBackgroundImage: function(img)
	{
		this.backgroundImage=this.resize(img);
	},
	resize: function(img)
	{
		var image = new Image();
		image.src=img;
		return image;
	},

	/**setRelativePlayerSize: function(multiplier)
	{
		this.playerRelativeSize=multiplier;
	},*/

	restart: function()
	{
		this.current_row=51;
		this.current_column=51;
	}
});

dojo.ready(function()
{
	new maze_game();
});

