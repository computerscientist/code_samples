package tokens;

/**
 * Creates a token that stores a word in it
 * @author Robert Dallara
 *
 */
public abstract class WordToken extends AToken {
	
	/**
	 * Creates a token that stores a word (usually a command)
	 * @param word the word to store in the token
	 * @param wordType the type of word being dealt with
	 */
	public WordToken(String word, String wordType)
	{
		super(word, wordType);
	}
	
	/**
	 * Creates a token that stores a word (not a command)
	 * @param word the word to store in the token
	 */
	public WordToken(String word)
	{
		super(word, "Word");
	}
}
