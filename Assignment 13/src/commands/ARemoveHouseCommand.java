package commands;

import basicgeometry.*;
import graphics.*;
import simulation.*;

/**
 * A class used for objects representing commands telling the simulation to remove
 * a house from itself
 * @author Robert Dallara
 *
 */
public class ARemoveHouseCommand implements Command {

	private Simulation halloweenSimulation;
	
	/**
	 * Creates a new "remove house" command object
	 * @param simulation
	 */
	public ARemoveHouseCommand(Simulation simulation)
	{
		this.halloweenSimulation=simulation;
	}
	
	/**
	 * Removes a house from the simulation and removes that house (as an observer)
	 * from the avatar's list of observers
	 */
	public void execute()
	{
		halloweenSimulation.getVisitingTrickOrTreatAvatar().removeObserver(halloweenSimulation.getHouseCollection().elementAt(halloweenSimulation.getHouseCollection().size()-1));
		halloweenSimulation.getHouseCollection().removeElement();
	}
	
	/**
	 * Undoes this command
	 */
	public void undo()
	{
		FullProperty newHouse=new FullHouseProperty(new GraphicalPoint(270*halloweenSimulation.getHouseCollection().size()%1080, 250*(halloweenSimulation.getHouseCollection().size()/4)), Simulation.MAILBOX_WIDTH, Simulation.MAILBOX_HEIGHT, Simulation.MAILBOX_SUPPORT_LENGTH);
		halloweenSimulation.getHouseCollection().addElement(newHouse);
		
		halloweenSimulation.getVisitingTrickOrTreatAvatar().addObserver(newHouse);
	}
}
