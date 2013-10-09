package exceptions;

/**
 * A class used as a type for all "invalid input" exceptions in the simulation
 * @author Robert Dallara
 *
 */
public class InvalidInputException extends Exception {

	/**
	 * Creates a new "invalid input" exception object
	 */
	public InvalidInputException()
	{
		
	}
	
	/**
	 * Creates a new instance of an "invalid input" exception, but with a specific message
	 * @param message the message to tell the user about the invalid input
	 */
	public InvalidInputException(String message)
	{
		super(message);
	}
}
