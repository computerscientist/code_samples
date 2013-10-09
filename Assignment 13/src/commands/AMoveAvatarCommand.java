package commands;

import basicgeometry.*;
import graphics.*;
import simulation.*;

/**
 * Creates an object representing a move command for the trick-or-treating avatar (that
 * can visit houses as well as give and take candies)
 * 
 * @author Robert Dallara
 *
 */
public class AMoveAvatarCommand implements Command {

	private TrickOrTreatAvatar avatar;
	
	private Point oldLocation;
	private Point location;
	
	/**
	 * Constructs the new move command
	 * @param avatar the avatar to move
	 * @param oldLocation the location that the avatar starts at
	 * @param newLocation the location to move the avatar to
	 */
	public AMoveAvatarCommand(TrickOrTreatAvatar avatar, Point oldLocation, Point newLocation)
	{
		this.avatar=avatar;
		
		this.oldLocation=oldLocation;
		this.location=newLocation;
	}
	
	/**
	 * Runs this command on the avatar
	 */
	public void execute()
	{
		if(SimulationState.getAnimationStatus()==true)
			new Thread(new AnAnimateMoveAvatarCommand(this.avatar, location, TrickOrTreatAvatar.NUMBER_OF_STEPS, TrickOrTreatAvatar.PAUSE_TIME)).start();
			
		else
			this.avatar.setLocation(location);
	}
	
	/**
	 * Undoes this command on the avatar
	 */
	public void undo()
	{
		if(SimulationState.getAnimationStatus()==true)
			new Thread(new AnAnimateMoveAvatarCommand(this.avatar, oldLocation, TrickOrTreatAvatar.NUMBER_OF_STEPS, TrickOrTreatAvatar.PAUSE_TIME)).start();
		
		else
			this.avatar.setLocation(oldLocation);
	}
}
