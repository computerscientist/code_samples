package exceptions;

/**
 * A class used as a type for all errors that occur in the scanner
 * @author Robert Dallara
 *
 */
public class ScannerException extends InvalidInputException {

	/**
	 * Creates a new instance of a scanner exception (but with a message to the user)
	 * @param message tells the user about the scanning error
	 */
	public ScannerException(String message)
	{
		super(message);
	}
}
