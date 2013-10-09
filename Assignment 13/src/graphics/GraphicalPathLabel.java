package graphics;

import basicgeometry.*;

/**
 * Creates a label with an image of a path on it
 * (which can lead up to a house)
 * @author Robert Dallara
 *
 */
public class GraphicalPathLabel extends ASpecificImageLabel {
	
	/**
	 * Creates a new path label object
	 * @param initialX the initial x-coordinate of the path label
	 * @param initialY the initial y-coordinate of the path label
	 */
	public GraphicalPathLabel(int initialX, int initialY)
	{
		super(new GraphicalPoint(initialX, initialY), "Path.png", 17, 80);
	}
}
