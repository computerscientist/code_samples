package basicgeometry;

/**
 * Creates a rectangle object that can be displayed with Object Editor
 * @author Robert Dallara
 *
 */
public class GraphicalRectangle extends GraphicalShape implements Rectangle {

	/**
	 * Creates a new instance of a rectangle
	 * @param location the location of the rectangle
	 * @param width the rectangle's width
	 * @param height the rectangle's height
	 */
	public GraphicalRectangle(Point location, int width, int height)
	{
		this.location=location;
		
		this.width=width;
		this.height=height;
	}
}
