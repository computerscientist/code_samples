package graphics;

import basicgeometry.*;

/**
 * Creates a new object that contains a leg
 * @author Robert Dallara
 *
 */
public abstract class GraphicalLeggedObject implements LeggedObject {

	protected Line leg;
	protected Shape top;
	
	/**
	 * Returns the line representing the graphical object's leg
	 * @return the leg of the object
	 */
	public abstract Line getLeg();
	
	/**
	 * Returns the object's top
	 * @return the top of the object
	 */
	public abstract Shape getTop();
}
