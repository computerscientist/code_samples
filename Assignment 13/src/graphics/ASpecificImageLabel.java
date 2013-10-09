package graphics;

import basicgeometry.Point;

/**
 * Creates an object that represents a label with only an image on it
 * @author Robert Dallara
 *
 */
public class ASpecificImageLabel implements SpecificImageLabel {

	protected final String IMAGE;
	
	protected final int WIDTH;
	protected final int HEIGHT;
	
	protected Point location;
	
	/**
	 * Creates a new specific image label
	 * @param location where to place the label
	 * @param imageName the image to use
	 * @param width the width of the image
	 * @param height the height of the image
	 */
	public ASpecificImageLabel(Point location, String imageName, int width, int height)
	{
		this.location=location;
		IMAGE=imageName;
		
		WIDTH=width;
		HEIGHT=height;
	}
	
	/**
	 * Returns the location of the house
	 * @return the house location
	 */
	public Point getLocation()
	{
		return this.location;
	}
	
	/**
	 * Sets the location of the house to a new location
	 * @param newLocation the new location of the house
	 */
	public void setLocation(Point newLocation)
	{
		this.location=newLocation;
	}
	
	/**
	 * Returns the width of the house label
	 * @return the house label width 
	 */
	public int getWidth()
	{
		return WIDTH;
	}
	
	/**
	 * Returns the height of the house label
	 * @return the house label height
	 */
	public int getHeight()
	{
		return HEIGHT;
	}
	
	/**
	 * Returns the name of the file which contains the picture of the house
	 * @return the house image file name
	 */
	public String getImageFileName()
	{
		return IMAGE;
	}
}
