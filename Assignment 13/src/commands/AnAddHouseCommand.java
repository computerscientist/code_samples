package commands;

import graphics.FullHouseProperty;
import graphics.FullProperty;
import basicgeometry.GraphicalPoint;
import simulation.*;

/**
 * A class used for objects representing commands telling the simulation to
 * add a house to itself
 * @author Robert Dallara
 *
 */
public class AnAddHouseCommand implements Command {

	private Simulation halloweenSimulation;
	
	/**
	 * Constructs a new "add house" command
	 * @param simulation the simulation that needs to add a house to itself
	 */
	public AnAddHouseCommand(Simulation simulation)
	{
		this.halloweenSimulation=simulation;
	}
	
	/**
	 * Adds a new house to the simulation and makes it observe the avatar's movement
	 */
	public void execute()
	{
		FullProperty newHouse=new FullHouseProperty(new GraphicalPoint(270*halloweenSimulation.getHouseCollection().size()%1080, 250*(halloweenSimulation.getHouseCollection().size()/4)), Simulation.MAILBOX_WIDTH, Simulation.MAILBOX_HEIGHT, Simulation.MAILBOX_SUPPORT_LENGTH);
		halloweenSimulation.getHouseCollection().addElement(newHouse);
		
		halloweenSimulation.getVisitingTrickOrTreatAvatar().addObserver(newHouse);
	}
	
	/**
	 * Undoes this command
	 */
	public void undo()
	{
		halloweenSimulation.getVisitingTrickOrTreatAvatar().removeObserver(halloweenSimulation.getHouseCollection().elementAt(halloweenSimulation.getHouseCollection().size()-1));
		halloweenSimulation.getHouseCollection().removeElement();
	}
}
