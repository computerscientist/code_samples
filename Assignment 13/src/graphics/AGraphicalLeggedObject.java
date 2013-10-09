package graphics;

import basicgeometry.*;

/**
 * Creates a legged object (but with its methods implemented)
 * @author Robert Dallara
 *
 */
public abstract class AGraphicalLeggedObject extends GraphicalLeggedObject {

	/**
	 * Returns the line representing the legged object's support
	 * @return the support of the legged object
	 */
	public Line getLeg()
	{
		return this.leg;
	}
	
	/**
	 * Returns the rectangular top of the legged object
	 * @return the top of the legged object
	 */
	public Shape getTop()
	{
		return this.top;
	}
}
