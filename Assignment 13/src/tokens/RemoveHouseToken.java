package tokens;

/**
 * A token object representing a "remove house" command in this simulation
 * @author Robert Dallara
 *
 */
public class RemoveHouseToken extends WordToken {

	/**
	 * Creates a new "remove house" token object
	 * @param removeHouse the string input that conveys the removing of a house
	 */
	public RemoveHouseToken(String removeHouse)
	{
		super(removeHouse, "Remove");
	}
}