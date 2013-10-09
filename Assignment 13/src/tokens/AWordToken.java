package tokens;

/**
 * Creates a word object. Designed for non-command use of words
 * @author Robert Dallara
 *
 */
public class AWordToken extends WordToken {

	/**
	 * Creates a new word object for non-commands
	 * @param word the word stored in this object
	 */
	public AWordToken(String word)
	{
		super(word);
	}
}
