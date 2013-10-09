package testing;

import basicgeometry.*;
import graphics.*;

/**
 * Tests an avatar object to make sure that all of its components "stay together" when the avatar moves
 * @author Robert Dallara
 *
 */
public class AvatarTester implements Tester {

	/**
	 * Creates and moves an avatar object in order to test whether or not all of the relative positions
	 * of the avatar parts stay the same before and after the avatar movement
	 */
	public void test(Avatar avatar, int xChange, int yChange)
	{
		//Get and display original avatar data
		int originalXCoordinate=avatar.getTop().getLocation().getX();
		int originalYCoordinate=avatar.getTop().getLocation().getY();
		
		System.out.println("Original avatar location: ("+originalXCoordinate+", "+originalYCoordinate+")");
		System.out.println("Avatar will move by: ("+xChange+", "+yChange+")\n");
		
		Point originalAvatarStickLocation=avatar.getLeg().getLocation();
		
		//Move the avatar
		Point currentAvatarLocation=avatar.getTop().getLocation();
		avatar.setLocation(new GraphicalPoint(currentAvatarLocation.getX()+xChange, currentAvatarLocation.getY()+yChange));
		
		//Where do the avatar parts end up?
		Point finalAvatarLocation=new GraphicalPoint(avatar.getTop().getLocation().getX(), avatar.getTop().getLocation().getY());
		Point finalAvatarStickLocation=new GraphicalPoint(avatar.getLeg().getLocation().getX(), avatar.getLeg().getLocation().getY());
		
		//Prints out the expected and actual locations of the avatar parts after they have moved with the avatar
		System.out.println("Expected new avatar head location: ("+(originalXCoordinate+xChange)+", "+(originalYCoordinate+yChange)+")");
		System.out.println("Actual new avatar head location: ("+finalAvatarLocation.getX()+", "+finalAvatarLocation.getY()+")");
		System.out.println("Avatar x-coordinate error: "+(finalAvatarLocation.getX()-(originalXCoordinate+xChange)));
		System.out.println("Avatar y-coordinate error: "+(finalAvatarLocation.getY()-(originalYCoordinate+yChange))+"\n");
		
		System.out.println("Expected new top coordinate of avatar \"stick\": ("+(originalAvatarStickLocation.getX()+xChange)+", "+(originalAvatarStickLocation.getY()+yChange)+")");
		System.out.println("Actual new top coordinate of avatar \"stick\": ("+finalAvatarStickLocation.getX()+", "+finalAvatarStickLocation.getY()+")");
		System.out.println("Avatar \"stick\" x-coordinate error: "+(finalAvatarStickLocation.getX()-(originalAvatarStickLocation.getX()+xChange)));
		System.out.println("Avatar \"stick\" y-coordinate error: "+(finalAvatarStickLocation.getY()-(originalAvatarStickLocation.getY()+yChange)));
	}
}
