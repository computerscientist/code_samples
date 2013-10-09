package commands;

import simulation.*;

/**
 * A class used for objects representing commands telling the simulation to
 * switch its animation status (i.e. whether or not it is to animate movements of the avatar)
 * @author Robert Dallara
 *
 */
public class AnAnimateCommand implements Command {

	private Simulation halloweenSimulation;
	
	/**
	 * Constructs a new "animate" command object
	 * @param simulation the simulation to invoke this command on
	 */
	public AnAnimateCommand(Simulation simulation)
	{
		this.halloweenSimulation=simulation;
	}
	
	/**
	 * Executes this command
	 */
	public void execute()
	{
		SimulationState.switchAnimationStatus();
	}
	
	/**
	 * Undoes this command
	 */
	public void undo()
	{
		SimulationState.switchAnimationStatus();
	}
}
