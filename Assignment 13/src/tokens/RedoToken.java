package tokens;

/**
 * A token object representing a "redo" command in this simulation
 * @author Robert Dallara
 *
 */
public class RedoToken extends WordToken {

	/**
	 * Creates a new "redo" token object
	 * @param redo the string input that conveys the redoing of an operation that was undone
	 */
	public RedoToken(String redo)
	{
		super(redo, "Redo");
	}
}