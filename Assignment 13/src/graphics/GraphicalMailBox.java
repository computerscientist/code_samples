package graphics;

import basicgeometry.*;

/**
 * Creates a mail box that can be displayed in Object Editor
 * @author Robert Dallara
 *
 */
public class GraphicalMailBox extends AGraphicalLeggedObject implements MailBox {

	//Default dimensions of the mail box
	private int width=13;
	private int height=13;
	private int supportLength=13;
	
	private Point location;
	
	/**
	 * Creates a new mail box object that can be displayed
	 * @param location the location of the mail box
	 * @param width the width of the mail box
	 * @param height the height of the mail box (not including its support on the bottom)
	 * @param supportLength the length of the support of the mail box
	 */
	public GraphicalMailBox(Point location, int width, int height, int supportLength)
	{
		this.top=new GraphicalRectangle(location, width, height);
		leg=new GraphicalLine(new GraphicalPoint(location.getX()+width/2, location.getY()+height), 0, supportLength);
	}
	
	/**
	 * Creates a new mail box object with its default dimensions
	 * @param location the location of the mail box
	 */
	public GraphicalMailBox(Point location)
	{
		this.setLocation(location);
	}
	
	/**
	 * Sets the location of the mail box
	 * @param newLocation the location to move the mail box to
	 */
	public void setLocation(Point newLocation)
	{
		this.location=newLocation;
		
		//Figures out where each of the components in the mail box should be based on its location
		top.setLocation(newLocation);
		leg.setLocation(new GraphicalPoint(location.getX()+width/2, location.getY()+height));
	}
}
