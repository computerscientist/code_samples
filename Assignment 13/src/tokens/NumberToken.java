package tokens;

/**
 * Creates a new number token
 * @author Robert Dallara
 *
 */
public abstract class NumberToken extends AToken implements Number {

	/**
	 * Creates a new number token object
	 * @param value the value of the token's number
	 */
	public NumberToken(String value)
	{
		super(value, "Number");
	}
	
	/**
	 * Returns the integer value of the number
	 * @return the number's integer value
	 */
	public int getValue()
	{
		return Integer.parseInt(this.stringValue);
	}
}
