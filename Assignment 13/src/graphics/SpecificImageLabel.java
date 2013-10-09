package graphics;

import basicgeometry.*;

/**
 * An interface used as a type for labels that are solely images
 * @author Robert Dallara
 *
 */
public interface SpecificImageLabel {

	public Point getLocation();
	public void setLocation(Point newLocation);
	
	public int getWidth();
	public int getHeight();
	
	public String getImageFileName();
}
