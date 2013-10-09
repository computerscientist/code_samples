package tokens;

/**
 * Creates a token that stores an invalid expression in it
 * @author Robert Dallara
 *
 */
public abstract class InvalidToken extends AToken {

	/**
	 * Creates a token that stores an invalid expression
	 * @param invalidExpression the invalid expression to store in the token
	 */
	public InvalidToken(String invalidExpression)
	{
		super(invalidExpression, "Invalid");
	}
}
