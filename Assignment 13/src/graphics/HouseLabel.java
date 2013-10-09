package graphics;

import basicgeometry.*;

/**
 * Creates a label with an image of a house on it
 * @author Robert Dallara
 *
 */
public class HouseLabel extends ASpecificImageLabel {
	
	/**
	 * Creates a new house label object
	 * @param initialX the initial x-coordinate of the house label
	 * @param initialY the initial y-coordinate of the house label
	 */
	public HouseLabel(int initialX, int initialY)
	{
		super(new GraphicalPoint(initialX, initialY), "House.png", 253, 157);
	}
}
