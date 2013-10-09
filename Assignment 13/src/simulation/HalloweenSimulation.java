package simulation;

import basicgeometry.*;
import collections.*;
import commands.*;
import graphics.*;

/**
 * An object used as a simulation of trick-or-treating with an avatar and houses
 * @author Robert Dallara
 *
 */
public class HalloweenSimulation implements Simulation {

	private HouseCollection houseCollection;
	private VisitingTrickOrTreatAvatar trickOrTreatAvatar;
	
	/**
	 * Creates a new simulation object (with an avatar with no candies and two houses)
	 */
	public HalloweenSimulation()
	{
		trickOrTreatAvatar=new GraphicalVisitingTrickOrTreatAvatar(new GraphicalPoint(50, 220), 13, 13, 13);
		houseCollection=new AHouseCollection();

		SimulationState.initialize();
		
		//Adds the first two houses to the simulation
		for(int i=0;i<2;i++)
		{
			FullProperty newHouse=new FullHouseProperty(new GraphicalPoint(270*this.getHouseCollection().size()%1080, 250*(this.getHouseCollection().size()/4)), MAILBOX_WIDTH, MAILBOX_HEIGHT, MAILBOX_SUPPORT_LENGTH);
			this.getHouseCollection().addElement(newHouse);
			
			this.getVisitingTrickOrTreatAvatar().addObserver(newHouse);
		}
	}
	
	/**
	 * Checks to see if the avatar can be moved
	 */
	public boolean preMoveAvatar(int xChange, int yChange)
	{
		return true;
	}
	
	/**
	 * Moves the avatar on the screen
	 * @param xChange the change in the avatar's x-coordinate during the move
	 * @param yChange the change in the avatar's y-coordinate during the move
	 */
	public void moveAvatar(int xChange, int yChange)
	{
		assert preMoveAvatar(xChange, yChange);
		Point currentAvatarLocation=trickOrTreatAvatar.getAvatar().getTop().getLocation();
		
		if(SimulationState.getAnimationStatus()==true)
			trickOrTreatAvatar.animateSetLocation(new GraphicalPoint(currentAvatarLocation.getX()+xChange, currentAvatarLocation.getY()+yChange));
		
		SimulationState.getUndoer().execute(new AMoveAvatarCommand(this.trickOrTreatAvatar, currentAvatarLocation, new GraphicalPoint(currentAvatarLocation.getX()+xChange, currentAvatarLocation.getY()+yChange)));
	}
	
	/**
	 * Checks to see if we can add a house to the simulation
	 * @boolean whether or not we can add another house
	 */
	public boolean preAddHouse()
	{
		return this.houseCollection.size()<houseCollection.maxSize();
	}
	
	/**
	 * Adds another house to the simulation (into a grid-like pattern with
	 * three rows and four columns)
	 */
	public void addHouse()
	{
		assert preAddHouse();
		SimulationState.getUndoer().execute(new AnAddHouseCommand(this));
	}
	
	/**
	 * Checks to see if we can remove a house from the simulation
	 * @boolean whether or not there is a previously added house that can be removed
	 */
	public boolean preRemoveHouse()
	{
		return this.houseCollection.size()>0;
	}
	
	/**
	 * Removes the last house added to the simulation
	 */
	public void removeHouse()
	{
		assert preRemoveHouse();
		SimulationState.getUndoer().execute(new ARemoveHouseCommand(this));
	}
	
	/**
	 * Checks to see if the avatar can give a certain number of candies
	 * @param numberOfCandies the number of candies that the avatar wants to give
	 * 
	 * @return whether or not the avatar can give the number of candies it wants to give
	 */
	public boolean preGiveCandies(int numberOfCandies)
	{
		return this.trickOrTreatAvatar.getCandyBag().getCandyCollection().size()>=numberOfCandies && this.trickOrTreatAvatar.currentHouseHasRoomForCandies(numberOfCandies);
	}
	
	/**
	 * Makes the avatar give a certain number of candies to the house property it is connected to
	 * (if it is connected to one). Nothing will happen if the avatar doesn't have enough candies
	 * to give or if the house will have too many candies after the giving is complete.
	 * @param numberOfCandies the number of candies the avatar is to give to the house property
	 * it is connected to.
	 */
	public void giveCandies(int numberOfCandies)
	{
		assert preGiveCandies(numberOfCandies);
		SimulationState.getUndoer().execute(new AGiveCandyCommand(this.trickOrTreatAvatar, numberOfCandies));
	}
	
	/**
	 * Checks to see if the avatar can take a certain number of candies
	 * @param numberOfCandies the number of candies that the avatar wants to take
	 * 
	 * @return whether or not the avatar can take the number of candies it wants to take
	 */
	public boolean preTakeCandies(int numberOfCandies)
	{
		return this.trickOrTreatAvatar.getCandyBag().getCandyCollection().size()+numberOfCandies<=this.trickOrTreatAvatar.getCandyBag().getCandyCollection().maxSize() && this.trickOrTreatAvatar.currentHouseHasEnoughCandies(numberOfCandies);
	}
	
	/**
	 * Makes the avatar take a certain number of candies from the house property it is connected to
	 * (if it is connected to one). Nothing will happen if the house doesn't have enough candies
	 * to give or if the avatar will have too many candies after the taking is complete
	 * @param numberOfCandies the number of candies the avatar is to take from the house property
	 * it is connected to.
	 */
	public void takeCandies(int numberOfCandies)
	{
		assert preTakeCandies(numberOfCandies);
		SimulationState.getUndoer().execute(new ATakeCandyCommand(this.trickOrTreatAvatar, numberOfCandies));
	}
	
	/**
	 * Returns the trick-or-treating avatar that is part of the simulation
	 * @return the avatar in the simulation
	 */
	public VisitingTrickOrTreatAvatar getVisitingTrickOrTreatAvatar()
	{
		return this.trickOrTreatAvatar;
	}
	
	/**
	 * Returns the house collection in this simulation
	 * @return the collection of houses for this simulation
	 */
	public HouseCollection getHouseCollection()
	{
		return this.houseCollection;
	}
	
	/**
	 * Figures out if the avatar is visiting a house
	 * @return whether or not the avatar is visiting a house
	 */
	public boolean getAvatarVisitorStatus()
	{
		for(int i=0;i<houseCollection.size();i++)
		{
			if(((FullProperty) houseCollection.elementAt(i)).hasVisitor(this.trickOrTreatAvatar))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Checks to see if the animation status can be switched
	 */
	public boolean preSwitchAnimationStatus()
	{
		return true;
	}
	
	/**
	 * Switches the animation status of this simulation (i.e. whether or not the
	 * simulation will animate the movement of the avatar or not).
	 */
	public void switchAnimationStatus()
	{
		assert preSwitchAnimationStatus();
		SimulationState.getUndoer().execute(new AnAnimateCommand(this));
	}
	
	/**
	 * Checks to see if there is a command to be undone
	 */
	public boolean preUndo()
	{
		return SimulationState.getUndoer().preUndo();
	}
	
	/**
	 * Undoes the last executed command
	 */
	public void undo()
	{
		assert preUndo();
		SimulationState.getUndoer().undo();
	}
	
	/**
	 * Checks to see if there is a command to be redone
	 */
	public boolean preRedo()
	{
		return SimulationState.getUndoer().preRedo();
	}
	
	/**
	 * Redo the last executed command
	 */
	public void redo()
	{
		assert preRedo();
		SimulationState.getUndoer().redo();
	}
}
