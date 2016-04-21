dojo.provide('maze_game');

dojo.declare('maze_game', null,
{
	canvas: null,
	context2D: null,
	FPS: 50,
	keyPressed: false,
	maze: null,
	isGameRunning: true,
	hasWon: false,
	backgroundColor: "white",
	finishColor: "yellow",
	playerColor: "blue",
	wallColor: "red",
	playerRelativeSize: 0.33,
	current_row: 51,
	current_column: 51,

	constructor: function()
	{
		this.canvas=document.getElementById("canvas");
		this.context2D=canvas.getContext('2d');

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

	evaluateJSON: function(fileText)
	{
		var obj=JSON.parse("data.json");

		//Set player color
		if(obj.player.red)
			this.setPlayerColor('red');
		else if(obj.player.blue)
			this.setPlayerColor('blue');
		else
			this.setPlayerColor('green');

		if(obj.left)
		{
			if(this.current_column>0 && this.maze[this.current_row][this.current_column-1]!="O")
				this.current_column--;
			obj.left=false;
		}	
		else if(obj.right)
		{
			if(this.current_column<this.maze[0].length-1 && this.maze[this.current_row][this.current_column+1]!="O")
				this.current_column++;
			obj.right=false;
		}
		else if(obj.up)
		{
			if(this.current_row>0 && this.maze[this.current_row-1][this.current_column]!="O")
				this.current_row--;
			obj.up=false;
		}
		else if(obj.down)
		{
			if(this.current_row<this.maze.length-1 && this.maze[this.current_row+1][this.current_column]!="O")
				this.current_row++;
			obj.down=false;
		}

	},

	draw: function()
	{
		if(!this.isGameRunning)
		{
			if(!this.hasWon)
				return;

			else
			{
				this.draw_filled_rectangle(this.backgroundColor, 0, 0, this.canvas.width, this.canvas.height);

				this.context2D.fillStyle='orange';
				this.context2D.font='72px courier';
				this.context2D.fillText("You Win!", this.canvas.width/2-160, this.canvas.height/2-10);

				return;
			}
		}

		//this.evaluateJSON();

		//Clear screen
		this.draw_filled_rectangle(this.backgroundColor, 0, 0, this.canvas.width, this.canvas.height);
		
		var row, column;
		if(this.current_row+2<this.maze.length && this.current_column+2<this.maze[0].length)
		{
			for(row=0;row<5;row++)
			{
				for(column=0;column<5;column++)
				{
					if(this.maze[Math.max(this.current_row+row-2, row)][Math.max(this.current_column+column-2, column)]=='O')
						this.draw_filled_rectangle(this.wallColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else if(this.maze[Math.max(this.current_row+row-2, row)][Math.max(this.current_column+column-2, column)]=='G')
						this.draw_filled_rectangle(this.finishColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else
						this.draw_filled_rectangle(this.backgroundColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
				}
			}

			this.draw_filled_rectangle(this.playerColor, Math.min(2*this.canvas.width/5, this.canvas.width/5*this.current_column)+(1-this.playerRelativeSize)/2*this.canvas.width/5, Math.min(2*this.canvas.height/5, this.canvas.height/5*this.current_row)+(1-this.playerRelativeSize)/2*this.canvas.height/5, this.canvas.width/5*this.playerRelativeSize, this.canvas.height/5*this.playerRelativeSize);
		}

		else if(this.current_row+2==this.maze.length && this.current_column+2==this.maze[0].length)
		{
			for(row=0;row<5;row++)
			{
				for(column=0;column<5;column++)
				{
					if(this.maze[this.maze.length-5+row][this.maze[0].length-5+column]=='O')
						this.draw_filled_rectangle(this.wallColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else if(this.maze[this.maze.length-5+row][this.maze[0].length-5+column]=='G')
						this.draw_filled_rectangle(this.finishColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else
						this.draw_filled_rectangle(this.backgroundColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
				}
			}

			this.draw_filled_rectangle(this.playerColor, 3*this.canvas.width/5+(1-this.playerRelativeSize)/2*this.canvas.width/5, 3*this.canvas.height/5+(1-this.playerRelativeSize)/2*this.canvas.height/5, this.canvas.width/5*this.playerRelativeSize, this.canvas.height/5*this.playerRelativeSize);
		}

		else if(this.current_row+2==this.maze.length)
		{
			for(row=0;row<5;row++)
			{
				for(column=0;column<5;column++)
				{
					if(this.maze[this.maze.length-5+row][Math.max(this.current_column+column-2, column)]=='O')
						this.draw_filled_rectangle(this.wallColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else if(this.maze[this.maze.length-5+row][Math.max(this.current_column+column-2, column)]=='G')
						this.draw_filled_rectangle(this.finishColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else
						this.draw_filled_rectangle(this.backgroundColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
				}
			}

			this.draw_filled_rectangle(this.playerColor, Math.min(2*this.canvas.width/5, this.canvas.width/5*this.current_column)+(1-this.playerRelativeSize)/2*this.canvas.width/5, 3*this.canvas.height/5+(1-this.playerRelativeSize)/2*this.canvas.height/5, this.canvas.width/5*this.playerRelativeSize, this.canvas.height/5*this.playerRelativeSize);
		}

		else //this.current_column+2==this.maze[0].length
		{
			for(row=0;row<5;row++)
			{
				for(column=0;column<5;column++)
				{
					if(!this.isGameRunning)
						return;

					if(this.maze[Math.max(this.current_row+row-2, row)][this.maze[0].length-5+column]=='O')
						this.draw_filled_rectangle(this.wallColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else if(this.maze[Math.max(this.current_row+row-2, row)][this.maze[0].length-5+column]=='G')
						this.draw_filled_rectangle(this.finishColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
					else
						this.draw_filled_rectangle(this.backgroundColor, column*this.canvas.width/5, row*this.canvas.height/5, this.canvas.width/5, this.canvas.height/5);
				}
			}

			this.draw_filled_rectangle(this.playerColor, 3*this.canvas.width/5+(1-this.playerRelativeSize)/2*this.canvas.width/5, Math.min(2*this.canvas.height/5, this.canvas.height/5*this.current_row)+(1-this.playerRelativeSize)/2*this.canvas.height/5, this.canvas.width/5*this.playerRelativeSize, this.canvas.height/5*this.playerRelativeSize);
		}
	},

	draw_filled_rectangle: function(color, x, y, width, height)
	{
		this.context2D.fillStyle=color;
		this.context2D.beginPath();
		this.context2D.rect(x, y, width, height);
		this.context2D.closePath();
		this.context2D.fill();
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

	setPlayerColor: function(color)
	{
		this.playerColor=color;
	},

	setWallColor: function(color)
	{
		this.wallColor=color;
	},

	setFinishColor: function(color)
	{
		this.finishColor=color;
	},

	setBackgroundColor: function(color)
	{
		this.backgroundColor=color;
	},

	setRelativePlayerSize: function(multiplier)
	{
		this.playerRelativeSize=multiplier;
	},

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
