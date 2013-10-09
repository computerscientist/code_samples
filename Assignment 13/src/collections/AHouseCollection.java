package collections;

import graphics.*;

/**
 * Creates a collection that can store up to 12 houses (along with
 * their full properties) and display them in Object Editor.
 * @author Robert Dallara
 *
 */
public class AHouseCollection extends ACollection<FullProperty> implements HouseCollection {
	
	/**
	 * Creates an initializes a new house collection
	 */
	public AHouseCollection()
	{
		super(12);
	}
}
