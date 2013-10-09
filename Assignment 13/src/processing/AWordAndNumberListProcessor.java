package processing;

import tokens.*;
import collections.*;

/**
 * Takes an input string and then analyzes it in order (and points out words and numbers)
 * @author Robert Dallara
 *
 */
public class AWordAndNumberListProcessor implements ListProcessor {

	/**
	 * Analyzes a sentence that is input into the method in order, pointing out words and numbers.
	 * @param sentence the sentence to analyze in order
	 * @return the analyzed sentence, which points out its parts
	 */
	public String analyzeSentenceParts(String sentence)
	{
		boolean validExpressionEntered=false;
		int index=0;
		
		TokenHistory sentenceParts=new ATokenHistory();
		
		while(true)
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
			
			//Is there another expression to look at?
			if(next.equals(""))
				break;
			
			//Analyzes the next expression to see if it is a number or word
			boolean isNumber=true;
			boolean isWord=true;
			
			//Checks to see if the next expression is a number
			for(int i=0;i<next.length();i++)
			{
				if(!(Character.isDigit(next.charAt(i)) || (next.charAt(i)=='+' && i==0) || (next.charAt(i)=='-' && i==0)))
					isNumber=false;
			}
			
			//Checks to see if the next expression is a word
			for(int i=0;i<next.length();i++)
			{
				//Any character in a word that is not a letter (including numbers) makes it invalid
				if(!Character.isLetter(next.charAt(i)))
					isWord=false;
			}
			
			//Add the next expression token to the history for token objects
			if(isNumber)
			{
				sentenceParts.addElement(new ANumberToken(next));
				validExpressionEntered=true;
			}
			
			else if(isWord)
			{
				sentenceParts.addElement(new AWordToken(next));
				validExpressionEntered=true;
			}
			
			//If the next expression is not a word or number, it must be invalid
			else
				sentenceParts.addElement(new AnInvalidToken(next));
		}
		
		if(!validExpressionEntered)
			sentenceParts.addElement(new AWordToken("No valid number or word in input!"));
		
		//Creates a string to be returned which displays the words of the sentence and their types in order
		String theSentenceParts="";
		
		for(int i=0;i<sentenceParts.size();i++)
		{
			if(i==sentenceParts.size()-1 && !validExpressionEntered)
				theSentenceParts+=" "+sentenceParts.elementAt(i).toString();
		
			else
				theSentenceParts+=sentenceParts.elementAt(i).getDescription()+": "+sentenceParts.elementAt(i).toString()+(i<sentenceParts.size()-1 ? " ": "");
		}
		
		return theSentenceParts;
	}
}
