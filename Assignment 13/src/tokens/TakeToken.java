package tokens;

/**
 * A token object representing a "take" command in this simulation
 * @author Robert Dallara
 *
 */
public class TakeToken extends WordToken {

	/**
	 * Creates a new "take" token object
	 * @param take the string input that conveys the taking of candy
	 */
	public TakeToken(String take)
	{
		super(take, "Take");
	}
}
