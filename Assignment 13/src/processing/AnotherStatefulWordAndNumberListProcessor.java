package processing;

import tokens.*;

/**
 * Creates a stateful sentence processor that divides up the words into their proper commands
 * for the simulation
 * @author Robert Dallara
 *
 */
public class AnotherStatefulWordAndNumberListProcessor extends AStatefulWordAndNumberListProcessor {

	/**
	 * Creates a new stateful sentence processor
	 * @param sentence the sentence to scan over
	 */
	public AnotherStatefulWordAndNumberListProcessor(String sentence)
	{
		super(sentence);
	}
	
	/**
	 * Returns a token object containing a token value based on what the token value is
	 * @param tokenValue the value of the token (which will be used to figure out its type)
	 * @return the correct token object with the token value stored in it
	 */
	protected Token getTokenType(String tokenValue)
	{
		if(isNumber(tokenValue))
			return new ANumberToken(tokenValue);
		
		//Is the tokenValue token a command?
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("move")) || tokenValue.equalsIgnoreCase("mv"))
			return new MoveToken(tokenValue);
		
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("take")) || tokenValue.equalsIgnoreCase("tk"))
			return new TakeToken(tokenValue);
		
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("give")) || tokenValue.equalsIgnoreCase("gv"))
			return new GiveToken(tokenValue);
		
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("addHouse")) || tokenValue.equalsIgnoreCase("add"))
			return new AddHouseToken(tokenValue);
		
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("removeHouse")) || tokenValue.equalsIgnoreCase("remove") || tokenValue.equalsIgnoreCase("rm"))
			return new RemoveHouseToken(tokenValue);
		
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("animate")) || tokenValue.equalsIgnoreCase("am"))
			return new AnimateToken(tokenValue);
		
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("undo")) || tokenValue.equalsIgnoreCase("ud"))
			return new UndoToken(tokenValue);
		
		else if(isWord(tokenValue) && (tokenValue.equalsIgnoreCase("redo")) || tokenValue.equalsIgnoreCase("rd"))
			return new RedoToken(tokenValue);
		
		//Is the token simply another word?
		else if(isWord(tokenValue))
			return new AWordToken(tokenValue);
		
		//If the token isn't any of the above, it must be invalid.
		else
			return new AnInvalidToken(tokenValue);
	}
}
