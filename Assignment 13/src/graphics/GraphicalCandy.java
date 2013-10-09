package graphics;

import basicgeometry.*;

/**
 * Creates a candy object that can be displayed in Object Editor
 * @author Robert Dallara
 *
 */
public class GraphicalCandy implements Candy {

	private Oval candyOval;
	
	/**
	 * Constructs a new displayable candy object
	 * @param location the location of the candy
	 */
	public GraphicalCandy(Point location)
	{
		this.candyOval=new GraphicalOval(location, Candy.WIDTH, Candy.HEIGHT);
	}
	
	/**
	 * Returns the oval which is the candy's shape
	 * @return the oval that is the shape of the candy
	 */
	public Oval getCandyOval()
	{
		return this.candyOval;
	}
	
	/**
	 * Moves the candy oval to a new location
	 * @param newLocation the location to move the candy oval to.
	 */
	public void setLocation(Point newLocation)
	{
		this.candyOval.setLocation(newLocation);
	}
}
