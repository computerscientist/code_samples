package tokens;

/**
 * Creates a token object that stores something in it (such as a number or word)
 * @author Robert Dallara
 */
public abstract class AToken implements Token {
	
	protected final String stringValue;
	protected final String description;

	/**
	 * Creates a new token object
	 * @param stringValue the value stored in the token object
	 */
	public AToken(String stringValue, String description)
	{
		this.stringValue=stringValue;
		this.description=description;
	}
	
	/**
	 * Gets the description of the token
	 * @return the description of the token
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Returns a string representation of the token, which is its string value
	 * @return the string representation of the token
	 */
	public String toString()
	{
		return this.stringValue;
	}
}
