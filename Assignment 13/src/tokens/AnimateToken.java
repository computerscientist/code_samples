package tokens;

/**
 * A token object representing an "animate" command in this simulation
 * @author Robert Dallara
 *
 */
public class AnimateToken extends WordToken {

	/**
	 * Creates a new "animate" token object
	 * @param animate the string input that conveys animation
	 */
	public AnimateToken(String animate)
	{
		super(animate, "Animate");
	}
}