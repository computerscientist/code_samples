package graphics;

import basicgeometry.*;

/**
 * An interface used as a type for all objects with legs (such as mailboxes and avatars)
 * @author Robert Dallara
 *
 */
public interface LeggedObject {

	Line getLeg();
	Shape getTop();
}
