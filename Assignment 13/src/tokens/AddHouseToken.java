package tokens;

/**
 * A token object representing an "add house" command in this simulation
 * @author Robert Dallara
 *
 */
public class AddHouseToken extends WordToken {

	/**
	 * Creates a new "add house" token object
	 * @param addHouse the string input that conveys the adding of a house
	 */
	public AddHouseToken(String addHouse)
	{
		super(addHouse, "Add");
	}
}