package exceptions;

/**
 * A class used as a type for all errors that occur in the parser
 * @author Robert Dallara
 *
 */
public class ParserException extends InvalidInputException {

	/**
	 * Creates a new instance of a parser exception (but with a message to the user)
	 * @param message tells the user about the parsing error
	 */
	public ParserException(String message)
	{
		super(message);
	}
}
