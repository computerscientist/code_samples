package graphics;

import basicgeometry.*;

/**
 * An interface used as a type for candy objects
 * @author Robert Dallara
 *
 */
public interface Candy {

	int WIDTH=7;
	int HEIGHT=7;
	
	int NUMBER_OF_STEPS=20;
	int PAUSE_TIME=50;
	
	Oval getCandyOval();
	void setLocation(Point newLocation);
}
