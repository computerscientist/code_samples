package processing;

import tokens.*;

/**
 * Takes an input string and then analyzes it in order (and points out words and numbers)
 * @author Robert Dallara
 *
 */
public class AStatefulWordAndNumberListProcessor implements ProcessorIterator<Token> {

	protected int index;
	protected String sentence="";
	
	/**
	 * Creates a list processor that analyzes an input string
	 * @param sentence the sentence to analyze
	 */
	public AStatefulWordAndNumberListProcessor(String sentence)
	{
		this.sentence=sentence;
		this.index=0;
	}
	
	/**
	 * Returns whether or not there is another expression in the sentence to scan
	 * @return if there is another expression to scan
	 */
	public boolean hasNext()
	{
		return this.index<sentence.length();
	}
	
	/**
	 * Returns the properly-typed token of the next expression in the sentence
	 * @return the next token for the next expression in the sentence
	 */
	public Token next()
	{	
		//Find the next expression in the sentence
		String next="";

		//Is there still more of the sentence to be scanned?
		if(index<sentence.length())
		{
			//Keep scanning until we find the beginning of an expression
			while(sentence.charAt(index)==' ')
			{
				index++;

				if(index>=sentence.length())
					break;
			}

			//Look at the next expression, unless we are at the end of the sentence
			if(index<sentence.length())
			{
				while(sentence.charAt(index)!=' ')
				{
					next+=sentence.charAt(index);

					index++;

					if(index>=sentence.length())
						break;
				}
			}
		}

		//Store the next string in the appropriate type of Token object
		Token nextToken=getTokenType(next);

		return nextToken;
	}
	
	/**
	 * Returns the sentence that this iterator is scanning
	 * @return
	 */
	protected String getSentence()
	{
		return this.sentence;
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
		
		else if(isWord(tokenValue))
			return new AWordToken(tokenValue);
		
		else
			return new AnInvalidToken(tokenValue);
	}
	
	/**
	 * Checks to see if an expression is a number
	 * @param expression the expression to check
	 * @return whether or not the expression is a number
	 */
	protected boolean isNumber(String expression)
	{
		//An empty expression is not a number
		if(expression.length()<1)
			return false;
		
		for(int i=0;i<expression.length();i++)
		{
			if(!(Character.isDigit(expression.charAt(i)) || (expression.charAt(i)=='+' && i==0) || (expression.charAt(i)=='-' && i==0)))
				return false;
		}
		
		//Do we have a lone '+' or '-' sign?
		if(expression.length()>0)
		{
			if(!Character.isDigit(expression.charAt(0)) && expression.length()==1)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Checks to see if an expression is a word
	 * @param expression the expression to check
	 * @return whether or not the expression is a word
	 */
	protected boolean isWord(String expression)
	{
		for(int i=0;i<expression.length();i++)
		{
			//Any character in a word that is not a letter (including numbers) makes it invalid
			if(!Character.isLetter(expression.charAt(i)))
				return false;
		}
		
		return true;
	}
}
