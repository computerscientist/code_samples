package graphics;

import java.util.List;
import java.util.ArrayList;

import basicgeometry.*;

/**
 * Creates a new trick or treat avatar that can visit houses (that can also be displayed in Object Editor)
 * @author Robert Dallara
 *
 */
public class GraphicalVisitingTrickOrTreatAvatar extends GraphicalTrickOrTreatAvatar implements VisitingTrickOrTreatAvatar {

	private FullProperty currentHouse;
	private List<FullProperty> observers;
	
	/**
	 * Creates a new visiting trick or treat avatar
	 * @param location where to place the avatar
	 * @param ovalWidth the width of the avatar's head
	 * @param ovalHeight the height of the avatar's head
	 * @param stickLength the length of the avatar's "stick"
	 */
	public GraphicalVisitingTrickOrTreatAvatar(Point location, int ovalWidth, int ovalHeight, int stickLength)
	{
		super(location, ovalWidth, ovalHeight, stickLength);
		
		this.observers=new ArrayList<FullProperty>();
	}
	
	/**
	 * Adds a new property observer to this avatar's list of observers
	 * @param observerProperty the property that will be observing this avatar
	 */
	public void addObserver(FullProperty observerProperty)
	{
		this.observers.add(observerProperty);
		
		//If the avatar is already connected to a house, no need to check if other houses do
		if(this.currentHouse==null)
			observerProperty.update(this);
	}
	
	/**
	 * Removes a property observer from this avatar's list of observers
	 * @param observerProperty the property that is no longer observing the avatar
	 */
	public void removeObserver(FullProperty observerProperty)
	{
		this.observers.remove(observerProperty);
		
		if(observerProperty!=null && observerProperty.equals(currentHouse))
			this.disconnectFromHouse();
	}
	
	/**
	 * Notifies all of the avatar's observers (which are house properties) about
	 * the avatar's current status
	 */
	private void notifyObservers()
	{
		for(int i=0;i<observers.size();i++)
		{
			observers.get(i).update(this);
			
			//If we've been connected to a house, don't let the other houses update the avatar
			if(this.currentHouse!=null)
				break;
		}
	}
	
	/**
	 * Connects the avatar to a house (if it falls within its path)
	 * @param property the property that the avatar is trying to connect to
	 */
	public void connectToHouse(FullProperty property)
	{
		if(property.hasVisitor(this))
			this.currentHouse=property;
	}

	/**
	 * Disconnects the avatar from a house (if it is visiting it)
	 */
	public void disconnectFromHouse()
	{
		this.currentHouse=null;
	}
	
	/**
	 * Moves the avatar to a new location and updates all of its
	 * observers about it
	 * @param newLocation the location to move the avatar to
	 */
	public void setLocation(Point newLocation)
	{
		super.setLocation(newLocation);
		this.notifyObservers();
	}
	
	/**
	 * Makes the avatar give a certain number of candies to the house property it is connected to
	 * (if it is connected to one). 
	 * 
	 * @param numberOfCandies the number of candies the avatar is to give to the house property
	 * it is connected to.
	 */
	public void giveCandies(int numberOfCandies)
	{
		if(this.currentHouse!=null)
		{
			for(int i=0;i<numberOfCandies;i++)
			{
				this.getCandyBag().removeCandy();
				this.currentHouse.getCandyBag().addCandy();
			}
		}
	}
	
	/**
	 * Makes the avatar take a certain number of candies from the house property it is connected to
	 * (if it is connected to one).
	 * 
	 * @param numberOfCandies the number of candies the avatar is to take from the house property
	 * it is connected to.
	 */
	public void takeCandies(int numberOfCandies)
	{
		if(this.currentHouse!=null)
		{
			for(int i=0;i<numberOfCandies;i++)
			{
				this.currentHouse.getCandyBag().removeCandy();
				this.getCandyBag().addCandy();
			}
		}
	}
	
	/**
	 * Checks to see if the avatar's current house (if it has one) has enough room
	 * in its candy bag for the avatar to add a certain number of additional candies
	 * @param numberOfAdditionalCandies the number of candies that the avatar wants to add
	 * @return whether or not the house's candy bag has room for the additional candies
	 */
	public boolean currentHouseHasRoomForCandies(int numberOfAdditionalCandies)
	{
		if(this.currentHouse!=null)
		{
			if(this.currentHouse.getCandyBag().getCandyCollection().size()+numberOfAdditionalCandies>this.currentHouse.getCandyBag().getCandyCollection().maxSize())
				return false;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks to see if the avatar's current house (if it has one) has enough candies
	 * in order for it to take a certain number of candies
	 * @param numberOfCandiesToTake the number of candies that the avatar wants to take
	 * @return whether or not the house's candy bag has enough candies for the avatar to take
	 */
	public boolean currentHouseHasEnoughCandies(int numberOfCandiesToTake)
	{
		if(this.currentHouse!=null)
		{
			if(this.currentHouse.getCandyBag().getCandyCollection().size()<numberOfCandiesToTake)
				return false;
			
			return true;
		}
		
		return false;
	}
}