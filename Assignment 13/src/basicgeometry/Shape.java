package basicgeometry;

import java.beans.PropertyChangeListener;

/**
 * An interface used as a type for shapes
 * @author Robert Dallara
 *
 */
public interface Shape {

	Point getLocation();
	
	void setLocation(Point newLocation);
	void addPropertyChangeListener(PropertyChangeListener listener);
	
	int getWidth();
	int getHeight();
}
