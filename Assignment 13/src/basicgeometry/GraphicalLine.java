package basicgeometry;


/**
 * Creates a line that can be displayed in object editor
 * @author Robert Dallara
 *
 */
public class GraphicalLine extends GraphicalShape implements Line {

	/**
	 * Constructs a new line that can be displayed graphically in object editor
	 * @param location the starting location of the line
	 * @param width the width of the line's bounding box
	 * @param height the height of the line's bounding box
	 */
	public GraphicalLine(Point location, int width, int height)
	{
		this.location=location;
		this.width=width;
		this.height=height;
	}
}
