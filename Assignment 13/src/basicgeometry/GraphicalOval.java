package basicgeometry;


/**
 * Creates an oval that can be displayed in Object Editor
 * @author Robert Dallara
 *
 */
public class GraphicalOval extends GraphicalShape implements Oval {
	
	/**
	 * Creates a new oval object
	 * @param location the location of the oval in Object Editor
	 * @param width the width of the oval's bounding box
	 * @param height the height of the oval's bounding box
	 */
	public GraphicalOval(Point location, int width, int height)
	{
		this.width=width;
		this.height=height;
		
		this.location=location;
	}
}
