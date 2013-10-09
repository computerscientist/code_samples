package commands;

import basicgeometry.*;
import graphics.*;

/**
 * Creates an object that represents a move command (that will include animations)
 * for the trick-or-treating avatar (which can visit houses as well as give and
 * take candies).
 * @author Robert Dallara
 *
 */
public class AnAnimateMoveAvatarCommand implements Runnable {

	private TrickOrTreatAvatar avatar;
	private Point location;
	
	private int steps;
	private int pauseTime;
	
	/**
	 * Constructs the new "move" command
	 * @param avatar the avatar to move
	 * @param newLocation the position of the avatar after this command has been executed
	 * @param steps the number of animation steps
	 * @param pauseTime the time in between individual animation steps
	 */
	public AnAnimateMoveAvatarCommand(TrickOrTreatAvatar avatar, Point newLocation, int steps, int pauseTime)
	{
		this.avatar=avatar;
		this.location=newLocation;
		
		this.steps=steps;
		this.pauseTime=pauseTime;
	}
	
	/**
	 * Runs this command
	 */
	public void run()
	{
		avatar.animateSetLocation(location, steps, pauseTime);
	}
}
