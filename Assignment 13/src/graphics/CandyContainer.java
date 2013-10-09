package graphics;

import basicgeometry.*;

/**
 * A candy container that can be displayed in object editor
 * @author Robert Dallara
 *
 */
public class CandyContainer implements Container {

	private Line leftSide;
	private Line bottomSide;
	private Line rightSide;
	
	private Point location;
	
	/**
	 * Creates a new displayable candy container object
	 * @param location the location to display the container
	 */
	public CandyContainer(Point location)
	{
		this.location=location;
		
		this.leftSide=new GraphicalLine(this.location, 0, HEIGHT);
		this.bottomSide=new GraphicalLine(new GraphicalPoint(this.location.getX(), this.location.getY()+HEIGHT), WIDTH, 0);
		this.rightSide=new GraphicalLine(new GraphicalPoint(this.location.getX()+WIDTH, this.location.getY()+HEIGHT), 0, -HEIGHT);
	}
	
	/**
	 * Returns the left side of the candy container
	 * @return the candy container's left side
	 */
	public Line getLeftSide()
	{
		return this.leftSide;
	}
	
	/**
	 * Returns the bottom side of the candy container
	 * @return the candy container's bottom side
	 */
	public Line getBottomSide()
	{
		return this.bottomSide;
	}
	
	/**
	 * Returns the right side of the candy container
	 * @return the candy container's right side
	 */
	public Line getRightSide()
	{
		return this.rightSide;
	}
	
	/**
	 * Moves the container to a new location
	 * @param newLocation the new location to move the container to
	 */
	public void setLocation(Point newLocation)
	{
		this.leftSide.setLocation(newLocation);
		this.bottomSide.setLocation(new GraphicalPoint(newLocation.getX(), newLocation.getY()+HEIGHT));
		this.rightSide.setLocation(new GraphicalPoint(newLocation.getX()+WIDTH, newLocation.getY()+HEIGHT));
	}
}
