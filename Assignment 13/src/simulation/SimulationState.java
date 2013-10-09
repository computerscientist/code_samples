package simulation;

import commands.*;

/**
 * An object used to keep track of whether or not the simulation is animating
 * @author Robert Dallara
 *
 */
public class SimulationState {

	private static boolean animationStatus;
	private static boolean inProcessOfAnimating;
	
	private static Undoer commandUndoer;
	
	/**
	 * Initializes the simulation state (which keeps track of its animations and commands done)
	 */
	public static void initialize()
	{
		animationStatus=false;
		inProcessOfAnimating=false;
		
		commandUndoer=new CommandUndoer();
	}
	
	/**
	 * Returns whether or not the halloween simulation can do an animation
	 * @return whether or not the simulation can animate
	 */
	public static boolean getAnimationStatus()
	{
		return animationStatus;
	}
	
	/**
	 * Updates whether or not the simulation can do an animation
	 */
	public static void switchAnimationStatus()
	{
		animationStatus=!animationStatus;
	}
	
	/**
	 * Returns whether or not the simulation is currently processing an animation
	 * @return whether or not the simulation is doing an animation
	 */
	public static boolean isAnimating()
	{
		return inProcessOfAnimating;
	}
	
	/**
	 * Switches the simulation's status on whether it is currently animating or not
	 */
	public static void switchAnimationProcessState()
	{
		inProcessOfAnimating=!inProcessOfAnimating;
	}
	
	/**
	 * Returns the command undoer that the simulation relies on
	 * @return the command undoer for the simulation
	 */
	public static Undoer getUndoer()
	{
		return commandUndoer;
	}
}
