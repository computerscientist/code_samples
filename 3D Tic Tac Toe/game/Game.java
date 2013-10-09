package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * A game of Tic-Tac-Toe which takes place in a 4x4x4 3D space.
 * The object of the game is to get 4 in a row in any 3D-direction.
 * A human player plays against the computer, which uses AI based
 * on a minimax algorithm with alpha-beta pruning
 * 
 * @author Robert Dallara
 *
 */
public class Game {
	
	boolean firstMove=true;
	
	Node node;
	
	Random random=new Random();
	
	Scanner in=new Scanner(System.in);
	
	int depth=3;
	
	int row;
	int column;
	int layer;
	
	String computerChar;
	String yourChar;
	String turnChar;
	
	public Game()
	{
		node=new Node();
		
		boolean done=false;
		
		//Choose X or O as the letter you will use
		while(done==false)
		{
			System.out.print("Select X or O: ");
			yourChar=in.next();
			
			done=true;
			
			if(!(yourChar.equalsIgnoreCase("X") || yourChar.equalsIgnoreCase("O")))
				done=false;
		}
		
		System.out.println();
		
		if(yourChar.equalsIgnoreCase("X"))
			computerChar="O";
		
		if(yourChar.equalsIgnoreCase("O"))
			computerChar="X";
		
		playGame();
	}
	
	/**
	 * Actually plays the game
	 */
	public void playGame()
	{
		turnChar="X";
		
		if(computerChar.equalsIgnoreCase("O"))
			node.display();
		
		while(true)
		{
			//If it is the player's turn, let him/her make their next move
			if(turnChar.equalsIgnoreCase(yourChar))
			{
				boolean completelyDone=false;
				boolean done=false;
				
				while(completelyDone==false)
				{
					while(done==false)
					{
						System.out.print("Row: ");
						row=in.nextInt();
						
						done=true;
						
						if(row<0 || row>3)
							done=false;
					}
					
					done=false;
					
					while(done==false)
					{
						System.out.print("Column: ");
						column=in.nextInt();
						
						done=true;
						
						if(column<0 || column>3)
							done=false;
					}
					
					done=false;
					
					while(done==false)
					{
						System.out.print("Layer: ");
						layer=in.nextInt();
						
						done=true;
						
						if(layer<0 || layer>3)
							done=false;
					}
					
					completelyDone=true;
					
					//If the space the player chooses is already occupied, make him/her choose again
					if(!node.getBoard(layer, row, column).equals(" "))
					{
						System.out.println("Space already occupied!");
						completelyDone=false;
						done=false;
					}
				}
				
				System.out.println();
				
				node.setBoard(row, column, layer, turnChar);
				node.display();
				
				done=false;
				
				//Has an end point for the game been reached?
				if(node.isWin()!=0)
				{
					if(node.isWin()==-1)
						System.out.println("O wins!");
					
					if(node.isWin()==1)
						System.out.println("X wins!");
					
					break;
				}
				
				if(node.isFull() && node.isWin()==0)
				{
					System.out.println("Draw!");
					break;
				}
				
				if(turnChar.equalsIgnoreCase("X"))
				{
					turnChar="O";
					done=true;
				}
				
				if(turnChar.equalsIgnoreCase("O") && done==false)
					turnChar="X";
			}
			
			//Otherwise, the computer must decide where to move
			else if(turnChar.equalsIgnoreCase(computerChar))
			{
				int numberOfSquaresLeft=node.getNumberOfSquaresLeft();
				
				depth=0;
				
				int number=1;
				
				//Try to search deep enough to make a good move, but not so deep that the game runs slowly
				while(number<500000)
				{
					number*=numberOfSquaresLeft;
					numberOfSquaresLeft--;
					
					if(number<500000)
						depth++;
					
					if(numberOfSquaresLeft<1)
						break;
				}
				
				boolean winFound=false;
				boolean lossFound=false;
				
				double move[]=new double[5];
				
				ArrayList<Node> children=node.getChildren(turnChar);
				ArrayList<int[]> moves=node.getChildrenMoves();
				
				//If there is an immediate win for the computer, go for it and don't recursively look ahead!
				for(int i=0;i<children.size();i++)
				{
					if((children.get(i).isWin()==1 && turnChar.equals("X")) ||
					   (children.get(i).isWin()==-1 && turnChar.equals("O")))
					   
				    {
						winFound=true;
						
						move[0]=(double)moves.get(i)[0];
						move[1]=(double)moves.get(i)[1];
						move[2]=(double)moves.get(i)[2];
						
						break;
					}
				}
				
				if(winFound==false)
				{
					String oppositeTurnChar="";
					
					if(turnChar.equals("X"))
						oppositeTurnChar="O";
					
					else
						oppositeTurnChar="X";
					
					children=node.getChildren(oppositeTurnChar);
					
					//Respond to "immediate" threats without recursion to speed things up...
					for(int i=0;i<children.size();i++)
					{
						if((children.get(i).isWin()==1 && turnChar.equals("O")) ||
						   (children.get(i).isWin()==-1 && turnChar.equals("X")))
						{
							lossFound=true;
							
							move[0]=(double)moves.get(i)[0];
							move[1]=(double)moves.get(i)[1];
							move[2]=(double)moves.get(i)[2];
							
							break;
						}
					}
				}
				
				System.out.println("Computer thinking...");
				
				if(winFound==true || lossFound==true || firstMove==true)
					System.out.println();
				
				if(winFound==false && lossFound==false && firstMove==false)
					System.out.println("Search depth: "+depth+"\n");
				
				double gamma=0;
				
				//Use 
				if(turnChar.equalsIgnoreCase("X"))
					gamma=Double.POSITIVE_INFINITY;
				
				else if(turnChar.equalsIgnoreCase("O"))
					gamma=Double.NEGATIVE_INFINITY;
				
				//Use minimax strategy with alpha-beta pruning to estimate best move for computer
				if(winFound==false && lossFound==false && firstMove==false)
					move=minimax(node, turnChar, depth, gamma);
					
				//Start at random spot for first move as computer to speed things up (and make the game interesting...)
				if(firstMove==true)
				{
					move[0]=random.nextInt(2)+1;
					move[1]=random.nextInt(2)+1;
					move[2]=random.nextInt(2)+1;
					
					while(!node.getBoard((int)move[2], (int)move[0], (int)move[1]).equals(" "))
					{
						move[0]=random.nextInt(2)+1;
						move[1]=random.nextInt(2)+1;
						move[2]=random.nextInt(2)+1;
					}
					
					firstMove=false;
				}
				
				node.setBoard((int)move[0], (int)move[1], (int)move[2], turnChar);
				node.display();
				
				System.out.println("                    R C L");
				System.out.println("Computer moves to: ("+(int)move[0]+","+(int)move[1]+","+(int)move[2]+")");
				
				if(winFound==false && lossFound==false)
					System.out.println("Beta: "+move[3]+"\n");
				
				if(firstMove==true)
					firstMove=false;
				
				//Is the game over?
				if(node.isWin()!=0)
				{
					if(node.isWin()==-1)
						System.out.println("O wins!");
					
					if(node.isWin()==1)
						System.out.println("X wins!");
					
					break;
				}
				
				if(node.isFull() && node.isWin()==0)
				{
					System.out.println("Draw!");
					break;
				}
				
				boolean done=false;
				
				//Switch whose turn it is
				if(turnChar.equalsIgnoreCase("X"))
				{
					turnChar="O";
					done=true;
				}
				
				if(turnChar.equalsIgnoreCase("O") && done==false)
					turnChar="X";
			}
		}
	}
	
	/**
	 * 
	 * @param node the node we are starting our search from
	 * @param turnChar whose turn (X or O) it is
	 * @param depth how much deeper to search
	 * @param gamma used in order to allow "pruning" of subtrees that don't need searching
	 * @return a list containing the best move along with its info
	 */
	public double[] minimax(Node node, String turnChar, int depth, double gamma)
	{
		double[] list=new double[5];
		
		double alpha=0;
		double beta=0;
		
		int win=node.isWin();
		
		//Have we reached a terminal node?
		if(win!=0 || node.isFull())
		{
			list[0]=4;
			list[1]=4;
			list[2]=4;
			
			if(win!=0)
				list[3]=win*Double.POSITIVE_INFINITY;
			
			else
				list[3]=0;
			
			list[4]=depth;
			
			return list;
		}
		
		//If we reach depth 0, return the node's heuristic value
		if(depth==0)
		{
			list[0]=4;
			list[1]=4;
			list[2]=4;
			list[3]=node.getValue();
			list[4]=Double.POSITIVE_INFINITY;
			
			return list;
		}
		
		else
		{
			list[0]=4;
			list[1]=4;
			list[2]=4;
			
			String nextTurnChar="";
			
			if(turnChar.equalsIgnoreCase("X"))
			{
				alpha=Double.NEGATIVE_INFINITY;
				nextTurnChar="O";
			}
			
			if(turnChar.equalsIgnoreCase("O"))
			{
				alpha=Double.POSITIVE_INFINITY;
				nextTurnChar="X";
			}
			
			//Look at possible moves right up front
			ArrayList<Node> children=node.getChildren(turnChar);
			ArrayList<int[]> moves=node.getChildrenMoves();
			
			double winFound=Double.POSITIVE_INFINITY;
			
			//For each possibility, look at best counter-responses by opponent
			for(int i=0;i<children.size();i++)
			{
				double list2[]=minimax(children.get(i), nextTurnChar, depth-1, alpha);
				beta=list2[3];
				
				if((beta==Double.POSITIVE_INFINITY && turnChar.equalsIgnoreCase("X")) || (beta==Double.NEGATIVE_INFINITY && turnChar.equalsIgnoreCase("O")) && winFound>list2[4])
				{
					if(winFound==depth-1)
					{
						list[0]=(double)moves.get(i)[0];
						list[1]=(double)moves.get(i)[1];
						list[2]=(double)moves.get(i)[2];
						list[3]=beta;
						list[4]=list2[4];
						
						return list;
					}
					
					winFound=list2[4];
					
					list[0]=(double)moves.get(i)[0];
					list[1]=(double)moves.get(i)[1];
					list[2]=(double)moves.get(i)[2];
					
					alpha=beta;
				}
				
				else if(list[0]==4 && list[1]==4 && list[2]==4)
				{
					alpha=beta;
					
					list[0]=(double)moves.get(i)[0];
					list[1]=(double)moves.get(i)[1];
					list[2]=(double)moves.get(i)[2];
				}
				
				//Have we found a better move than before?
				else if(beta>alpha && turnChar.equalsIgnoreCase("X"))
				{
					alpha=beta;
					
					list[0]=(double)moves.get(i)[0];
					list[1]=(double)moves.get(i)[1];
					list[2]=(double)moves.get(i)[2];
				}
				
				else if(beta<alpha && turnChar.equalsIgnoreCase("O"))
				{
					alpha=beta;
					
					list[0]=(double)moves.get(i)[0];
					list[1]=(double)moves.get(i)[1];
					list[2]=(double)moves.get(i)[2];
				}
				
				/**If this occurs, further searches for X's replies to O's moves cannot influence
				 * what O's "value" (and thus move choice) is.
				 */
				if(alpha>=gamma && turnChar.equalsIgnoreCase("X"))
					break;
				
				//And vice versa...
				else if(alpha<=gamma && turnChar.equalsIgnoreCase("O"))
					break;
			}
			
			list[3]=alpha;
			list[4]=winFound;
			
			return list;
		}
	}
	
	/**
	 * Starts the program
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		new Game();
	}
}