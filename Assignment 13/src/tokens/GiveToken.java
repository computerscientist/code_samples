package tokens;

/**
 * A token object representing a "give" command in this simulation
 * @author Robert Dallara
 *
 */
public class GiveToken extends WordToken {

	/**
	 * Creates a new "give" token object
	 * @param give the string input that conveys the giving of candy
	 */
	public GiveToken(String give)
	{
		super(give, "Give");
	}
}