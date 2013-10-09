package tokens;

/**
 * A token object representing a "move" command in this simulation
 * @author Robert Dallara
 *
 */
public class MoveToken extends WordToken {

	/**
	 * Creates a new "move" token object
	 * @param move the string input that conveys movement
	 */
	public MoveToken(String move)
	{
		super(move, "Move");
	}
}
