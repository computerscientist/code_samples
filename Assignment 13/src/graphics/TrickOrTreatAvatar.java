package graphics;

import basicgeometry.*;
/**
 * An interface used as a type for all avatars that carry around candy
 * bags while trick-or-treating.
 * @author Robert Dallara
 *
 */
public interface TrickOrTreatAvatar {

	int NUMBER_OF_STEPS=20;
	int PAUSE_TIME=50;
	
	void setLocation(Point newLocation);
	
	void animateSetLocation(Point newLocation);
	void animateSetLocation(Point newLocation, int steps, int pauseTime);
	
	void addCandy();
	void removeCandy();
	
	Avatar getAvatar();
	Bag getCandyBag();
}
