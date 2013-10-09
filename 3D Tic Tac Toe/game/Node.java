package game;

import java.util.ArrayList;

/**
 * A class used to create instances of boards ("nodes") for the game
 * @author Robert Dallara
 *
 */
public class Node {
	
	private String[][][] board;
	
	/**
	 * Initializes a new node
	 */
	public Node()
	{
		board=new String[4][4][4];
		
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board[i].length;j++)
			{
				for(int k=0;k<board[i][j].length;k++)
					board[i][j][k]=" ";
			}
		}
	}
	
	/**
	 * Returns what is at a certain spot in the game space
	 * @param layer what 3D layer to go to
	 * @param row what row to go to
	 * @param column what column to go to
	 * @return whatever is at the specified spot (X, O, or a blank space)
	 */
	public String getBoard(int layer, int row, int column)
	{
		return board[row][column][layer];
	}
	
	/**
	 * Sets a certain spot in the game space to 'X' or 'O'
	 * @param row what row to go to
	 * @param column what column to go to
	 * @param layer what layer to go to
	 * @param turnChar 'X' or 'O'
	 */
	public void setBoard(int row, int column, int layer, String turnChar)
	{
		board[row][column][layer]=turnChar;
	}
	
	/**
	 * Copies a node into a new, identical node
	 * @return the copy of this node
	 */
	public Node copy()
	{
		Node nodeCopy=new Node();
		
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board[i].length;j++)
			{
				for(int k=0;k<board[i][j].length;k++)
					nodeCopy.setBoard(i, j, k, board[i][j][k]);
			}
		}
		
		return nodeCopy;
	}
	
	/**
	 * Gets the children of our current position
	 * @param turnChar whose turn ('X' or 'O') it is
	 * @return the list of all possible positions after the current one
	 */
	public ArrayList<Node> getChildren(String turnChar)
	{
		ArrayList<Node> children=new ArrayList<Node>();
		
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board[i].length;j++)
			{
				for(int k=0;k<board[i][j].length;k++)
				{
					if(board[i][j][k]==" ")
					{
						Node child=this.copy();
						child.setBoard(i, j, k, turnChar);
						
						children.add(child);
					}
				}
			}
		}
		
		return children;
	}
	
	/**
	 * Get the corresponding moves associated with children of our current node
	 * @return the list of all possible moves (represented as an ArrayList of int arrays)
	 */
	public ArrayList<int[]> getChildrenMoves()
	{
		ArrayList<int[]> moves=new ArrayList<int[]>();
		
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board[i].length;j++)
			{
				for(int k=0;k<board[i][j].length;k++)
				{
					if(board[i][j][k]==" ")
					{
						int[] newMove=new int[3];
						
						newMove[0]=i;
						newMove[1]=j;
						newMove[2]=k;
						
						moves.add(newMove);
					}
				}
			}
		}
		
		return moves;
	}
	
	/**
	 * Displays the node
	 */
	public void display()
	{
		System.out.println("        0123     0123     0123     0123 ");
		System.out.println("       +----+   +----+   +----+   +----+");

		for(int k=0;k<4;k++)
		{
			System.out.print("      ");
			
			for(int j=0;j<4;j++)
			{
				System.out.print(k+"+");
				
				for(int i=0;i<4;i++)
					System.out.print(board[k][i][j]);
				
				System.out.print("+  ");
			}
			
			System.out.println();
		}
		
		System.out.println("       +----+   +----+   +----+   +----+");
		System.out.println("Layer:   0        1        2        3   \n\n");
	}
	
	/**
	 * Checks to see if our current node is full
	 * @return whether or not our current node is full
	 */
	public boolean isFull()
	{
		int numberOfSquaresFull=0;
		
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				for(int k=0;k<4;k++)
				{
					if(!board[i][j][k].equals(" "))
						numberOfSquaresFull++;
				}
			}
		}
		
		return numberOfSquaresFull==64;
	}
	
	/**
	 * Returns how many blank squares there are in our current node
	 * @return the number of blank squares left
	 */
	public int getNumberOfSquaresLeft()
	{
		int numberLeft=0;
		
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				for(int k=0;k<4;k++)
				{
					if(board[i][j][k].equals(" "))
						numberLeft++;
				}
			}
		}
		
		return numberLeft;
	}
	
	/**
	 * Checks to see if 'X' or 'O' has a winning position in our current node
	 * @return -1 if 'O' has won, 1 if 'X' has won
	 */
	public int isWin()
	{
		for(int k=0;k<4;k++)
		{
			for(int i=0;i<4;i++)
			{
				if(board[k][i][0].equalsIgnoreCase("X") && board[k][i][1].equalsIgnoreCase("X") && board[k][i][2].equalsIgnoreCase("X") && board[k][i][3].equalsIgnoreCase("X"))
					return 1;
				
				if(board[k][i][0].equalsIgnoreCase("O") && board[k][i][1].equalsIgnoreCase("O") && board[k][i][2].equalsIgnoreCase("O") && board[k][i][3].equalsIgnoreCase("O"))
					return -1;
			}
			
			for(int i=0;i<4;i++)
			{
				if(board[k][0][i].equalsIgnoreCase("X") && board[k][1][i].equalsIgnoreCase("X") && board[k][2][i].equalsIgnoreCase("X") && board[k][3][i].equalsIgnoreCase("X"))
					return 1;
				
				if(board[k][0][i].equalsIgnoreCase("O") && board[k][1][i].equalsIgnoreCase("O") && board[k][2][i].equalsIgnoreCase("O") && board[k][3][i].equalsIgnoreCase("O"))
					return -1;
			}
			
			if((board[k][0][0].equalsIgnoreCase("X") && board[k][1][1].equalsIgnoreCase("X") && board[k][2][2].equalsIgnoreCase("X") && board[k][3][3].equalsIgnoreCase("X")) ||
			   (board[k][3][0].equalsIgnoreCase("X") && board[k][2][1].equalsIgnoreCase("X") && board[k][1][2].equalsIgnoreCase("X") && board[k][0][3].equalsIgnoreCase("X")))
				
				return 1;
			
			if((board[k][0][0].equalsIgnoreCase("O") && board[k][1][1].equalsIgnoreCase("O") && board[k][2][2].equalsIgnoreCase("O") && board[k][3][3].equalsIgnoreCase("O")) ||
			   (board[k][3][0].equalsIgnoreCase("O") && board[k][2][1].equalsIgnoreCase("O") && board[k][1][2].equalsIgnoreCase("O") && board[k][0][3].equalsIgnoreCase("O")))
						
				return -1;
		}
		
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				if(board[0][i][j].equalsIgnoreCase("X") && board[1][i][j].equalsIgnoreCase("X") && board[2][i][j].equalsIgnoreCase("X") && board[3][i][j].equalsIgnoreCase("X"))
					return 1;
				
				if(board[0][i][j].equalsIgnoreCase("O") && board[1][i][j].equalsIgnoreCase("O") && board[2][i][j].equalsIgnoreCase("O") && board[3][i][j].equalsIgnoreCase("O"))
					return -1;
			}
		}
		
		for(int i=0;i<4;i++)
		{
			if((board[0][i][0].equalsIgnoreCase("X") && board[1][i][1].equalsIgnoreCase("X") && board[2][i][2].equalsIgnoreCase("X") && board[3][i][3].equalsIgnoreCase("X")) ||
			   (board[3][i][0].equalsIgnoreCase("X") && board[2][i][1].equalsIgnoreCase("X") && board[1][i][2].equalsIgnoreCase("X") && board[0][i][3].equalsIgnoreCase("X")))
				
				return 1;
			
			if((board[0][i][0].equalsIgnoreCase("O") && board[1][i][1].equalsIgnoreCase("O") && board[2][i][2].equalsIgnoreCase("O") && board[3][i][3].equalsIgnoreCase("O")) ||
			   (board[3][i][0].equalsIgnoreCase("O") && board[2][i][1].equalsIgnoreCase("O") && board[1][i][2].equalsIgnoreCase("O") && board[0][i][3].equalsIgnoreCase("O")))
						
				return -1;
		}
		
		for(int i=0;i<4;i++)
		{
			if((board[0][0][i].equalsIgnoreCase("X") && board[1][1][i].equalsIgnoreCase("X") && board[2][2][i].equalsIgnoreCase("X") && board[3][3][i].equalsIgnoreCase("X")) ||
			   (board[3][0][i].equalsIgnoreCase("X") && board[2][1][i].equalsIgnoreCase("X") && board[1][2][i].equalsIgnoreCase("X") && board[0][3][i].equalsIgnoreCase("X")))
				
				return 1;
			
			if((board[0][0][i].equalsIgnoreCase("O") && board[1][1][i].equalsIgnoreCase("O") && board[2][2][i].equalsIgnoreCase("O") && board[3][3][i].equalsIgnoreCase("O")) ||
			   (board[3][0][i].equalsIgnoreCase("O") && board[2][1][i].equalsIgnoreCase("O") && board[1][2][i].equalsIgnoreCase("O") && board[0][3][i].equalsIgnoreCase("O")))
						
				return -1;
		}
		
		if((board[0][0][0].equalsIgnoreCase("X") && board[1][1][1].equalsIgnoreCase("X") && board[2][2][2].equalsIgnoreCase("X") && board[3][3][3].equalsIgnoreCase("X")) ||
		   (board[3][3][0].equalsIgnoreCase("X") && board[2][2][1].equalsIgnoreCase("X") && board[1][1][2].equalsIgnoreCase("X") && board[0][0][3].equalsIgnoreCase("X")) ||
		   (board[3][0][0].equalsIgnoreCase("X") && board[2][1][1].equalsIgnoreCase("X") && board[1][2][2].equalsIgnoreCase("X") && board[0][3][3].equalsIgnoreCase("X")) ||
		   (board[0][3][0].equalsIgnoreCase("X") && board[1][2][1].equalsIgnoreCase("X") && board[2][1][2].equalsIgnoreCase("X") && board[3][0][3].equalsIgnoreCase("X")))
			
			return 1;
		
		if((board[0][0][0].equalsIgnoreCase("O") && board[1][1][1].equalsIgnoreCase("O") && board[2][2][2].equalsIgnoreCase("O") && board[3][3][3].equalsIgnoreCase("O")) ||
		   (board[3][3][0].equalsIgnoreCase("O") && board[2][2][1].equalsIgnoreCase("O") && board[1][1][2].equalsIgnoreCase("O") && board[0][0][3].equalsIgnoreCase("O")) ||
		   (board[3][0][0].equalsIgnoreCase("O") && board[2][1][1].equalsIgnoreCase("O") && board[1][2][2].equalsIgnoreCase("O") && board[0][3][3].equalsIgnoreCase("O")) ||
		   (board[0][3][0].equalsIgnoreCase("O") && board[1][2][1].equalsIgnoreCase("O") && board[2][1][2].equalsIgnoreCase("O") && board[3][0][3].equalsIgnoreCase("O")))
					
			return -1;
		
		return 0;
	}
	
	/**
	 * Get the heuristic value of our current node
	 * The more positive it is, the closer X is to winning.
	 * The more negative it is, the closer O is to winning.
	 * 
	 * @return the heuristic value of this node
	 */
	public double getValue()
	{
		//Use positive infinity as a value when X has won
		if(this.isWin()==1)
			return Double.POSITIVE_INFINITY;
		
		//Use negative infinity as a value when O has won
		else if(this.isWin()==-1)
			return Double.NEGATIVE_INFINITY;
		
		else
		{
			int value=0;
			
			//Determine heuristic value of board based on position information of both players
			for(int i=0;i<board.length;i++)
			{
				for(int j=0;j<board[i].length;j++)
				{
					for(int k=0;k<board[i][j].length;k++)
					{
						if(board[i][j][k].equals("X"))
						{
							if(board[1][1][1].equals("X") || board[1][2][1].equals("X") ||
							   board[2][1][1].equals("X") || board[2][2][1].equals("X") ||
							   board[1][1][2].equals("X") || board[1][2][2].equals("X") ||
							   board[2][1][2].equals("X") || board[2][2][2].equals("X"))
								
								value+=60;
							
							if(board[0][1][1].equals("X") || board[0][1][2].equals("X") ||
							   board[0][2][1].equals("X") || board[0][2][2].equals("X") ||
							   board[3][1][1].equals("X") || board[3][1][2].equals("X") ||
							   board[3][2][1].equals("X") || board[3][2][2].equals("X"))
								
								value+=30;
							
							boolean done=false;
							
							if(i>1 && j>1 && k>1)
							{
								if(board[i-1][j-1][k-1].equals("X") && board[i-2][j-2][k-2].equals("X") && i==j && j==k && sum(board[0][0][0], board[1][1][1], board[2][2][2], board[3][3][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && j>0 && k>0 && done==false)
							{
								if(board[i-1][j-1][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && j>1 && k>1)
							{
								if(board[i+1][j-1][k-1].equals("X") && board[i+2][j-2][k-2].equals("X") && ((i==0 && j==3 && k==3) || (i==1 && j==2 && k==2)) && sum(board[0][3][3], board[1][2][2], board[2][1][1], board[3][0][0])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && j>0 && k>0 && done==false)
							{
								if(board[i+1][j-1][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i>1 && j<2 && k>1)
							{
								if(board[i-1][j+1][k-1].equals("X") && board[i-2][j+2][k-2].equals("X") && ((i==3 && j==0 && k==3) || (i==2 && j==1 && k==2)) && sum(board[3][0][3], board[2][1][2], board[1][2][1], board[0][3][0])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && j<3 && k>0 && done==false)
							{
								if(board[i-1][j+1][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && j<2 && k>1)
							{
								if(board[i+1][j+1][k-1].equals("X") && board[i+2][j+2][k-2].equals("X") && ((i==0 && j==0 && k==3) || (i==1 && j==1 && k==2)) && sum(board[0][0][3], board[1][1][2], board[2][2][1], board[3][3][0])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && j<3 && k>0 && done==false)
							{
								if(board[i+1][j+1][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i>1 && j>1 && k<2)
							{
								if(board[i-1][j-1][k+1].equals("X") && board[i-2][j-2][k+2].equals("X") && ((i==3 && j==3 && k==0) || (i==2 && j==2 && k==1)) && sum(board[3][3][0], board[2][2][1], board[1][1][2], board[0][0][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && j>0 && k<3 && done==false)
							{
								if(board[i-1][j-1][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && j>1 && k<2)
							{
								if(board[i+1][j-1][k+1].equals("X") && board[i+2][j-2][k+2].equals("X") && ((i==0 && j==3 && k==0) || (i==1 && j==2 && k==1)) && sum(board[0][3][0], board[1][2][1], board[2][1][2], board[3][0][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && j>0 && k<3 && done==false)
							{
								if(board[i+1][j-1][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i>1 && j<2 && k<2)
							{
								if(board[i-1][j+1][k+1].equals("X") && board[i-2][j+2][k+2].equals("X") && ((i==3 && j==0 && k==0) || (i==2 && j==1 && k==1)) && sum(board[3][0][0], board[2][1][1], board[1][2][2], board[0][3][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && j<3 && k<3 && done==false)
							{
								if(board[i-1][j+1][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && j<2 && k<2)
							{
								if(board[i+1][j+1][k+1].equals("X") && board[i+2][j+2][k+2].equals("X") && i==j && j==k && sum(board[0][0][0], board[1][1][1], board[2][2][2], board[3][3][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && j<3 && k<3 && done==false)
							{
								if(board[i+1][j+1][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(j>1 && k>1)
							{
								if(board[i][j-1][k-1].equals("X") && board[i][j-2][k-2].equals("X") && j==k && sum(board[i][0][0], board[i][1][1], board[i][2][2], board[i][3][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(j>0 && k>0 && done==false)
							{
								if(board[i][j-1][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(j>1 && k<2)
							{
								if(board[i][j-1][k+1].equals("X") && board[i][j-2][k+2].equals("X") && ((j==3 && k==0) || (j==2 && k==1)) && sum(board[i][3][0], board[i][2][1], board[i][1][2], board[i][0][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(j>0 && k<3 && done==false)
							{
								if(board[i][j-1][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(j<2 && k>1)
							{
								if(board[i][j+1][k-1].equals("X") && board[i][j+2][k-2].equals("X") && ((j==0 && k==3) || (j==1 && k==2)) && sum(board[i][0][3], board[i][1][2], board[i][2][1], board[i][3][0])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(j<3 && k>0 && done==false)
							{
								if(board[i][j+1][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(j<2 && k<2)
							{
								if(board[i][j+1][k+1].equals("X") && board[i][j+2][k+2].equals("X") && j==k && sum(board[i][0][0], board[i][1][1], board[i][2][2], board[i][3][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(j<3 && k<3 && done==false)
							{
								if(board[i][j+1][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i>1 && k>1)
							{
								if(board[i-1][j][k-1].equals("X") && board[i-2][j][k-2].equals("X") && i==k && sum(board[0][j][0], board[1][j][1], board[2][j][2], board[3][j][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && k>0 && done==false)
							{
								if(board[i-1][j][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && k>1)
							{
								if(board[i+1][j][k-1].equals("X") && board[i+2][j][k-2].equals("X") && ((i==0 && k==3) || (i==1 && k==2)) && sum(board[0][j][3], board[1][j][2], board[2][j][1], board[3][j][0])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && k>0 && done==false)
							{
								if(board[i+1][j][k-1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i>1 && k<2)
							{
								if(board[i-1][j][k+1].equals("X") && board[i-2][j][k+2].equals("X") && ((i==3 && k==0) || (i==2 && k==1)) && sum(board[3][j][0], board[2][j][1], board[1][j][2], board[0][j][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && k<3 && done==false)
							{
								if(board[i-1][j][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && k<2)
							{
								if(board[i+1][j][k+1].equals("X") && board[i+2][j][k+2].equals("X") && i==k && sum(board[0][j][0], board[1][j][1], board[2][j][2], board[3][j][3])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && k<3 && done==false)
							{
								if(board[i+1][j][k+1].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i>1 && j>1)
							{
								if(board[i-1][j-1][k].equals("X") && board[i-2][j-2][k].equals("X") && i==j && sum(board[0][0][k], board[1][1][k], board[2][2][k], board[3][3][k])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && j>0 && done==false)
							{
								if(board[i-1][j-1][k].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i>1 && j<2)
							{
								if(board[i-1][j+1][k].equals("X") && board[i-2][j+2][k].equals("X") && ((i==3 && j==0) || (i==2 && j==1)) && sum(board[3][0][k], board[2][1][k], board[1][2][k], board[0][3][k])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i>0 && j<3 && done==false)
							{
								if(board[i-1][j+1][k].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && j>1)
							{
								if(board[i+1][j-1][k].equals("X") && board[i+2][j-2][k].equals("X") && ((i==0 && j==3) || (i==1 && j==2)) && sum(board[0][3][k], board[1][2][k], board[2][1][k], board[3][0][k])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && j>0 && done==false)
							{
								if(board[i+1][j-1][k].equals("X"))
									value+=25;
							}
							
							done=false;
							
							if(i<2 && j<2)
							{
								if(board[i+1][j+1][k].equals("X") && board[i+2][j+2][k].equals("X") && i==j && sum(board[0][0][k], board[1][1][k], board[2][2][k], board[3][3][k])==3)
								{
									value+=125;
									done=true;
								}
							}
							
							if(i<3 && j<3 && done==false)
							{
								if(board[i+1][j+1][k].equals("X"))
									value+=25;
							}
							
							value+=5;
						}
						
						if(board[i][j][k].equals("O"))
						{
							if(board[1][1][1].equals("O") || board[1][2][1].equals("O") ||
							   board[2][1][1].equals("O") || board[2][2][1].equals("O") ||
							   board[1][1][2].equals("O") || board[1][2][2].equals("O") ||
							   board[2][1][2].equals("O") || board[2][2][2].equals("O"))
								
								value-=60;
							
							if(board[0][1][1].equals("O") || board[0][1][2].equals("O") ||
							   board[0][2][1].equals("O") || board[0][2][2].equals("O") ||
							   board[3][1][1].equals("O") || board[3][1][2].equals("O") ||
							   board[3][2][1].equals("O") || board[3][2][2].equals("O"))
								
								value-=30;
							
							boolean done=false;
							
							if(i>1 && j>1 && k>1)
							{
								if(board[i-1][j-1][k-1].equals("O") && board[i-2][j-2][k-2].equals("O") && i==j && j==k && sum(board[0][0][0], board[1][1][1], board[2][2][2], board[3][3][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && j>0 && k>0 && done==false)
							{
								if(board[i-1][j-1][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && j>1 && k>1)
							{
								if(board[i+1][j-1][k-1].equals("O") && board[i+2][j-2][k-2].equals("O") && ((i==0 && j==3 && k==3) || (i==1 && j==2 && k==2)) && sum(board[0][3][3], board[1][2][2], board[2][1][1], board[3][0][0])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && j>0 && k>0 && done==false)
							{
								if(board[i+1][j-1][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i>1 && j<2 && k>1)
							{
								if(board[i-1][j+1][k-1].equals("O") && board[i-2][j+2][k-2].equals("O") && ((i==3 && j==0 && k==3) || (i==2 && j==1 && k==2)) && sum(board[3][0][3], board[2][1][2], board[1][2][1], board[0][3][0])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && j<3 && k>0 && done==false)
							{
								if(board[i-1][j+1][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && j<2 && k>1)
							{
								if(board[i+1][j+1][k-1].equals("O") && board[i+2][j+2][k-2].equals("O") && ((i==0 && j==0 && k==3) || (i==1 && j==1 && k==2)) && sum(board[0][0][3], board[1][1][2], board[2][2][1], board[3][3][0])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && j<3 && k>0 && done==false)
							{
								if(board[i+1][j+1][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i>1 && j>1 && k<2)
							{
								if(board[i-1][j-1][k+1].equals("O") && board[i-2][j-2][k+2].equals("O") && ((i==3 && j==3 && k==0) || (i==2 && j==2 && k==1)) && sum(board[3][3][0], board[2][2][1], board[1][1][2], board[0][0][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && j>0 && k<3 && done==false)
							{
								if(board[i-1][j-1][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && j>1 && k<2)
							{
								if(board[i+1][j-1][k+1].equals("O") && board[i+2][j-2][k+2].equals("O") && ((i==0 && j==3 && k==0) || (i==1 && j==2 && k==1)) && sum(board[0][3][0], board[1][2][1], board[2][1][2], board[3][0][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && j>0 && k<3 && done==false)
							{
								if(board[i+1][j-1][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i>1 && j<2 && k<2)
							{
								if(board[i-1][j+1][k+1].equals("O") && board[i-2][j+2][k+2].equals("O") && ((i==3 && j==0 && k==0) || (i==2 && j==1 && k==1)) && sum(board[3][0][0], board[2][1][1], board[1][2][2], board[0][3][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && j<3 && k<3 && done==false)
							{
								if(board[i-1][j+1][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && j<2 && k<2)
							{
								if(board[i+1][j+1][k+1].equals("O") && board[i+2][j+2][k+2].equals("O") && i==j && j==k && sum(board[0][0][0], board[1][1][1], board[2][2][2], board[3][3][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && j<3 && k<3 && done==false)
							{
								if(board[i+1][j+1][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(j>1 && k>1)
							{
								if(board[i][j-1][k-1].equals("O") && board[i][j-2][k-2].equals("O") && j==k && sum(board[i][0][0], board[i][1][1], board[i][2][2], board[i][3][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(j>0 && k>0 && done==false)
							{
								if(board[i][j-1][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(j>1 && k<2)
							{
								if(board[i][j-1][k+1].equals("O") && board[i][j-2][k+2].equals("O") && ((j==3 && k==0) || (j==2 && k==1)) && sum(board[i][3][0], board[i][2][1], board[i][1][2], board[i][0][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(j>0 && k<3 && done==false)
							{
								if(board[i][j-1][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(j<2 && k>1)
							{
								if(board[i][j+1][k-1].equals("O") && board[i][j+2][k-2].equals("O") && ((j==0 && k==3) || (j==1 && k==2)) && sum(board[i][0][3], board[i][1][2], board[i][2][1], board[i][3][0])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(j<3 && k>0 && done==false)
							{
								if(board[i][j+1][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(j<2 && k<2)
							{
								if(board[i][j+1][k+1].equals("O") && board[i][j+2][k+2].equals("O") && j==k && sum(board[i][0][0], board[i][1][1], board[i][2][2], board[i][3][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(j<3 && k<3 && done==false)
							{
								if(board[i][j+1][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i>1 && k>1)
							{
								if(board[i-1][j][k-1].equals("O") && board[i-2][j][k-2].equals("O") && i==k && sum(board[0][j][0], board[1][j][1], board[2][j][2], board[3][j][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && k>0 && done==false)
							{
								if(board[i-1][j][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && k>1)
							{
								if(board[i+1][j][k-1].equals("O") && board[i+2][j][k-2].equals("O") && ((i==0 && k==3) || (i==1 && k==2)) && sum(board[0][j][3], board[1][j][2], board[2][j][1], board[3][j][0])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && k>0 && done==false)
							{
								if(board[i+1][j][k-1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i>1 && k<2)
							{
								if(board[i-1][j][k+1].equals("O") && board[i-2][j][k+2].equals("O") && ((i==3 && k==0) || (i==2 && k==1)) && sum(board[3][j][0], board[2][j][1], board[1][j][2], board[0][j][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && k<3 && done==false)
							{
								if(board[i-1][j][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && k<2)
							{
								if(board[i+1][j][k+1].equals("O") && board[i+2][j][k+2].equals("O") && i==k && sum(board[0][j][0], board[1][j][1], board[2][j][2], board[3][j][3])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && k<3 && done==false)
							{
								if(board[i+1][j][k+1].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i>1 && j>1)
							{
								if(board[i-1][j-1][k].equals("O") && board[i-2][j-2][k].equals("O") && i==j && sum(board[0][0][k], board[1][1][k], board[2][2][k], board[3][3][k])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && j>0 && done==false)
							{
								if(board[i-1][j-1][k].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i>1 && j<2)
							{
								if(board[i-1][j+1][k].equals("O") && board[i-2][j+2][k].equals("O") && ((i==3 && j==0) || (i==2 && j==1)) && sum(board[3][0][k], board[2][1][k], board[1][2][k], board[0][3][k])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i>0 && j<3 && done==false)
							{
								if(board[i-1][j+1][k].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && j>1)
							{
								if(board[i+1][j-1][k].equals("O") && board[i+2][j-2][k].equals("O") && ((i==0 && j==3) || (i==1 && j==2)) && sum(board[0][3][k], board[1][2][k], board[2][1][k], board[3][0][k])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && j>0 && done==false)
							{
								if(board[i+1][j-1][k].equals("O"))
									value-=25;
							}
							
							done=false;
							
							if(i<2 && j<2)
							{
								if(board[i+1][j+1][k].equals("O") && board[i+2][j+2][k].equals("O") && i==j && sum(board[0][0][k], board[1][1][k], board[2][2][k], board[3][3][k])==-3)
								{
									value-=125;
									done=true;
								}
							}
							
							if(i<3 && j<3 && done==false)
							{
								if(board[i+1][j+1][k].equals("O"))
									value-=25;
							}
							
							value-=5;
						}
					}
				}
			}
			
			return value;
		}
	}
	
	/**
	 * An auxiliary function used to see if 'X' or 'O' has four in a row
	 * @param square1 the value of square one ('X' or 'O')
	 * @param square2 the value of square two
	 * @param square3 the value of square three
	 * @param square4 the value of square four
	 * @return the "sum" of the four squares
	 */
	public int sum(String square1, String square2, String square3, String square4)
	{
		int sum=0;
		
		if(square1.equals("X"))
			sum++;
		
		else if(square1.equals("O"))
			sum--;
		
		if(square2.equals("X"))
			sum++;
		
		else if(square2.equals("O"))
			sum--;
		
		if(square3.equals("X"))
			sum++;
		
		else if(square3.equals("O"))
			sum--;
		
		if(square4.equals("X"))
			sum++;
		
		else if(square4.equals("O"))
			sum--;
		
		return sum;
	}
}
