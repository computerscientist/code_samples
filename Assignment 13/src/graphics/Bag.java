package graphics;

import basicgeometry.*;
import collections.*;

/**
 * An interface used as a type for candy bag objects
 * @author Robert Dallara
 *
 */
public interface Bag {

	CandyCollection getCandyCollection();
	Container getCandyContainer();
	
	void addCandy();
	void removeCandy();
	
	void setLocation(Point newLocation);
}
