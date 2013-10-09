package tokens;

/**
 * Creates a sample token that stores an invalid expression in it
 * @author Robert Dallara
 *
 */
public class AnInvalidToken extends InvalidToken {

	/**
	 * Creates a sample token that stores an invalid expression
	 * @param invalidExpression the invalid expression to store in the token
	 */
	public AnInvalidToken(String invalidExpression)
	{
		super(invalidExpression);
	}
}
