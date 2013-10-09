package processing;

import exceptions.*;
import simulation.*;
import tokens.*;

/**
 * Parses commands entered into a line in order to work the Halloween simulation
 * @author Robert Dallara
 *
 */
public class CommandParser implements Parser {

	private ProcessorIterator<Token> iterator;
	private Simulation halloweenSimulation;
	
	private String listOfErrors;
	
	/**
	 * Creates a new command parser
	 * @param halloweenSimulation the Halloween simulation to invoke the commands on
	 */
	public CommandParser(Simulation halloweenSimulation)
	{
		this.halloweenSimulation=halloweenSimulation;
		this.setSentence("");
	}
	
	/**
	 * Gets the sentence of this parser (which is also the sentence of the iterator)
	 */
	@util.annotations.ComponentWidth(800)
	
	public String getSentence()
	{
		return ((AStatefulWordAndNumberListProcessor) iterator).getSentence();
	}
	
	/**
	 * Sets the scanner's sentence to the command list in the Halloween simulation and execute the commands
	 * @param sentence the sentence to set the scanner's sentence to
	 */
	public void setSentence(String sentence)
	{
		this.listOfErrors="";
		
		iterator=new AnotherStatefulWordAndNumberListProcessor(sentence);
		
		//Execute commands in sentence after it is changed
		while(iterator.hasNext())
		{
			try
			{
				parseToken(iterator.next());
			}
			
			catch(InvalidInputException e)
			{
				listOfErrors+=e.getMessage()+" ";
			}
		}
	}
	
	/**
	 * Returns the entire list of errors encountered during the parsing
	 * @return the list of errors encountered
	 */
	@util.annotations.ComponentWidth(800)
	
	public String getListOfErrors()
	{
		return this.listOfErrors;
	}
	
	/**
	 * Parses the next token in the command list
	 * @param currentToken the token we are at in the command list
	 */
	private void parseToken(Token currentToken) throws ParserException, ScannerException
	{
		//Are we trying to move the avatar? If so, figure out movement amounts on screen
		if(currentToken instanceof MoveToken)
			parseMoveToken();

		//Are we trying to add or remove a house?
		else if(currentToken instanceof AddHouseToken)
			parseAddToken();

		else if(currentToken instanceof RemoveHouseToken)
			parseRemoveToken();
		
		//Are we trying to give or take candy?
		else if(currentToken instanceof GiveToken)
			parseGiveToken();
		
		else if(currentToken instanceof TakeToken)
			parseTakeToken();
		
		//Are we trying to switch the animation status of the simulation?
		else if(currentToken instanceof AnimateToken)
			parseAnimateToken();
		
		//Are we trying to undo or redo a command?
		else if(currentToken instanceof UndoToken)
			parseUndoToken();
		
		else if(currentToken instanceof RedoToken)
			parseRedoToken();
		
		//Are we at the end of the sentence?
		else if(currentToken.toString().length()==0)
			return;
		
		/**Any command that isn't recognized by the simulation (in a
		 * valid format or not) must be illegal
		 */
		else if(currentToken instanceof InvalidToken)
			throw new ScannerException("Invalid input: "+currentToken);
		
		else
			throw new ParserException("Illegal command: "+currentToken);
	}
	
	/**
	 * Parses the two expressions after a move token to figure out how to move the
	 * avatar in the Halloween simulation.
	 */
	private void parseMoveToken() throws ParserException
	{
		Token firstNumberToken=null;
		Token secondNumberToken=null;
		
		//Checks to see if the two expressions after the move command are numbers
		if(iterator.hasNext())
		{
			firstNumberToken=iterator.next();
			
			if(!(firstNumberToken instanceof NumberToken))
				throw new ParserException("Illegal move command format!");
		}
		
		if(iterator.hasNext())
		{
			secondNumberToken=iterator.next();
			
			if(!(secondNumberToken instanceof NumberToken))
				throw new ParserException("Illegal move command format!");
		}
		
		//Move the avatar if the move command is in the correct format (two numbers come after the command)
		if(firstNumberToken instanceof NumberToken && secondNumberToken instanceof NumberToken)
			halloweenSimulation.moveAvatar(this.getIntegerValue(firstNumberToken.toString()), this.getIntegerValue(secondNumberToken.toString()));
		
		else if(firstNumberToken==null || secondNumberToken==null)
			throw new ParserException("Illegal move command format!");
	}
	
	/**
	 * Parses the expression after a give token to figure out how to make the avatar
	 * give candy in the simulation.
	 */
	private void parseGiveToken() throws ParserException
	{
		Token numberToken=null;
		
		//Checks to see if the expression after the give command is a number
		if(iterator.hasNext())
		{
			numberToken=iterator.next();
			
			if(!(numberToken instanceof NumberToken))
				throw new ParserException("Illegal give command format!");
		}
		
		//Make the avatar give candies if the give command is in the correct format (one number comes after the command)
		if(numberToken instanceof NumberToken)
			halloweenSimulation.giveCandies(this.getIntegerValue(numberToken.toString()));
		
		else if(numberToken==null)
			throw new ParserException("Illegal give command format!");
	}
	
	/**
	 * Parses the expression after a take token to figure out how to make the avatar
	 * take candy in the simulation.
	 */
	private void parseTakeToken() throws ParserException
	{
		Token numberToken=null;
		
		//Checks to see if the expression after the take command is a number
		if(iterator.hasNext())
		{
			numberToken=iterator.next();
			
			if(!(numberToken instanceof NumberToken))
				throw new ParserException("Illegal take command format!");
		}
		
		//Make the avatar take candies if the take command is in the correct format (one number comes after the command)
		if(numberToken instanceof NumberToken)
			halloweenSimulation.takeCandies(this.getIntegerValue(numberToken.toString()));
		
		else if(numberToken==null)
			throw new ParserException("Illegal take command format!");
	}
	
	/**
	 * After seeing an add token, add a house to the simulation
	 */
	private void parseAddToken()
	{
		halloweenSimulation.addHouse();
	}
	
	/**
	 * After seeing a remove token, remove the last added house from the simulation
	 */
	private void parseRemoveToken()
	{
		halloweenSimulation.removeHouse();
	}
	
	/**
	 * After seeing an animate token, switch the animation status of the simulation
	 */
	private void parseAnimateToken()
	{
		halloweenSimulation.switchAnimationStatus();
	}
	
	/**
	 * After seeing an undo token, undo the last command in the simulation
	 */
	private void parseUndoToken()
	{
		halloweenSimulation.undo();
	}
	
	/**
	 * After seeing a redo token, redo the last command in the simulation
	 */
	private void parseRedoToken()
	{
		halloweenSimulation.redo();
	}
	
	/**
	 * Figures out the integer value of an integer string
	 * @param integerString the string to find the value of
	 * @return the integer value of the string
	 */
	private int getIntegerValue(String integerString)
	{
		//The parseInt method of the Integer class won't allow for '+' signs, so must take that into account
		if(integerString.charAt(0)=='+')
			return Integer.parseInt(integerString.substring(1));
		
		return Integer.parseInt(integerString);
	}
}
