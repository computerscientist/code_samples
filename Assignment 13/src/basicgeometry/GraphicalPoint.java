package basicgeometry;

/**
 * Creates a point that can be displayed in object editor
 * @author Robert Dallara
 *
 */
public class GraphicalPoint implements Point {

	private int x;
	private int y;
	
	/**
	 * Creates a graphical point object
	 * @param x the point's x-coordinate
	 * @param y the point's y-coordinate
	 */
	public GraphicalPoint(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	/**
	 * Returns the x-coordinate of the point
	 * @return the x-coordinate of the point
	 */
	public int getX()
	{
		return this.x;
	}
	
	/**
	 * Returns the y-coordinate of the point
	 * @return the y-coordinate of the point
	 */
	public int getY()
	{
		return this.y;
	}
	
	/**
	 * Sets the x-coordinate of the point to a new value
	 * @param newX the new x-coordinate of the point
	 */
	public void setX(int newX)
	{
		this.x=newX;
	}
	
	/**
	 * Sets the y-coordinate of the point to a new value
	 * @param newY the new y-coordinate of the point
	 */
	public void setY(int newY)
	{
		this.y=newY;
	}
}
