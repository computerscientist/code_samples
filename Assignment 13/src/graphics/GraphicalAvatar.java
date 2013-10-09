package graphics;

import basicgeometry.*;

/**
 * Creates an avatar that can be displayed in the Object Editor window
 * @author Robert Dallara
 *
 */
public class GraphicalAvatar extends AGraphicalLeggedObject implements Avatar {

	private int ovalWidth;
	private int ovalHeight;
	private int stickLength;
	
	private Point location;
	
	/**
	 * Creates a new graphical avatar object
	 * @param location where the avatar is located
	 * @param ovalWidth the width of the bounding box of the avatar's oval
	 * @param ovalHeight the height of the bounding box of the avatar's oval
	 * @param stickLength the length of the avatar's "stick" extending down from its head
	 */
	public GraphicalAvatar(Point location, int ovalWidth, int ovalHeight, int stickLength)
	{
		this.location=location;
		
		this.ovalWidth=ovalWidth;
		this.ovalHeight=ovalHeight;
		this.stickLength=stickLength;
		
		this.leg=new GraphicalLine(new GraphicalPoint(location.getX()+this.ovalWidth/2, location.getY()+this.ovalHeight), 0, stickLength);
		this.top=new GraphicalOval(location, ovalWidth, ovalHeight);
	}
	
	/**
	 * Moves the avatar by a certain amount in the x-direction and a certain amount in the y-direction
	 * @param xChange the amount to change the avatar's x-coordinate by.
	 * @param yChange the amount to change the avatar's y-coordinate by.
	 */
	public void setLocation(Point newLocation)
	{
		this.location=newLocation;
		
		this.top.setLocation(newLocation);
		this.leg.setLocation(new GraphicalPoint(newLocation.getX()+this.ovalWidth/2, newLocation.getY()+this.ovalHeight));
	}
}
