package graphics;

import simulation.SimulationState;
import basicgeometry.*;
import commands.*;

/**
 * Creates an avatar with a candy bag that trick-or-treats
 * @author Robert Dallara
 *
 */
public class GraphicalTrickOrTreatAvatar implements TrickOrTreatAvatar {

	protected Avatar avatar;
	protected Bag candyBag;
	
	/**
	 * Creates a new trick-or-treating avatar
	 * @param location where to place the avatar
	 * @param ovalWidth the width of the avatar's head
	 * @param ovalHeight the height of the avatar's head
	 * @param stickLength the length of the avatar's "stick"
	 */
	public GraphicalTrickOrTreatAvatar(Point location, int ovalWidth, int ovalHeight, int stickLength)
	{
		this.avatar=new GraphicalAvatar(location, ovalWidth, ovalHeight, stickLength);
		this.candyBag=new CandyBag(new GraphicalPoint(avatar.getTop().getLocation().getX()+(avatar.getTop().getWidth()/2-Container.WIDTH/2), avatar.getTop().getLocation().getY()-Container.HEIGHT*101/100));
	}
	
	/**
	 * Moves the trick-or-treat avatar to a new locatoin
	 * @param newLocation the location to move the avatar to.
	 */
	public void setLocation(Point newLocation)
	{
		this.avatar.setLocation(newLocation);
		this.candyBag.setLocation(new GraphicalPoint(newLocation.getX()+(this.avatar.getTop().getWidth()/2-Container.WIDTH/2), newLocation.getY()-Container.HEIGHT*101/100));
	}
	
	/**
	 * Adds a candy to the avatar's candy bag
	 */
	public void addCandy()
	{
		candyBag.addCandy();
	}
	
	/**
	 * Removes the topmost candy from the avatar's candy bag
	 */
	public void removeCandy()
	{
		candyBag.removeCandy();
	}
	
	/**
	 * Returns the avatar itself
	 * @return the avatar
	 */
	public Avatar getAvatar()
	{
		return this.avatar;
	}
	
	/**
	 * Returns the avatar's candy bag
	 * @return the candy bag
	 */
	public Bag getCandyBag()
	{
		return this.candyBag;
	}
	
	/**
	 * Begins the animation of the avatar's movement to a new location
	 * @param newLocation the location that the avatar will move to
	 */
	public void animateSetLocation(Point newLocation)
	{
		new Thread(new AnAnimateMoveAvatarCommand(this, newLocation, NUMBER_OF_STEPS, PAUSE_TIME)).start();
	}
	
	/**
	 * Animates the movement of the avatar from one location to another
	 * @param newLocation the new location that the avatar will end up at
	 * @param steps the number of animation steps in the animation of the avatar's movement
	 * @param pauseTime the time in between animation steps
	 */
	public synchronized void animateSetLocation(Point newLocation, int steps, int pauseTime)
	{
		//Tells the simulation that it is beginning to animate
		SimulationState.switchAnimationProcessState();
		
		Point originalLocation=this.avatar.getTop().getLocation();
		
		for(int i=0;i<steps;i++)
		{
			this.setLocation(new GraphicalPoint((int)(this.avatar.getTop().getLocation().getX()+(newLocation.getX()-originalLocation.getX())/((double) steps)), (int)(this.avatar.getTop().getLocation().getY()+(newLocation.getY()-originalLocation.getY())/((double) steps))));
			this.sleep(pauseTime);
		}
		
		this.setLocation(newLocation);
		
		//Tell the simulation when it is no longer animating
		SimulationState.switchAnimationProcessState();
	}
	
	/**
	 * Pauses the animation for a certain period of time
	 * @param pauseTime the number of milliseconds to pause the animation
	 */
	private void sleep(int pauseTime) 
	{
		try 
		{
			Thread.sleep(pauseTime);
		} 
		
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
