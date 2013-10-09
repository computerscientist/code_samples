package graphics;

import basicgeometry.*;

/**
 * An interface used as a type for graphical candy container objects
 * @author Robert Dallara
 *
 */
public interface Container {

	int WIDTH=Candy.WIDTH+2;
	int HEIGHT=52;
	
	Line getLeftSide();
	Line getBottomSide();
	Line getRightSide();
	
	void setLocation(Point newLocation);
}
