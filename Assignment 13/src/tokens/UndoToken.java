package tokens;

/**
 * A token object representing an "undo" command in this simulation
 * @author Robert Dallara
 *
 */
public class UndoToken extends WordToken {

	/**
	 * Creates a new "undo" token object
	 * @param undo the string input that conveys the undoing of the previous operation
	 */
	public UndoToken(String undo)
	{
		super(undo, "Undo");
	}
}
