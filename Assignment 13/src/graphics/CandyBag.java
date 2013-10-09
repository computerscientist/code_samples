package graphics;

import collections.*;
import basicgeometry.*;

/**
 * A candy bag object that holds candies in it
 * @author Robert Dallara
 *
 */
public class CandyBag implements Bag {
	
	private CandyCollection collection;
	private Container candyContainer;
	
	/**
	 * Construct a new candy bag object
	 * @param location the location of the bag
	 */
	public CandyBag(Point location)
	{
		this.collection=new ACandyCollection();
		this.candyContainer=new CandyContainer(location);
	}
	
	/**
	 * Returns the candy collection associated with the candy bag
	 * @return the collection for the candy bag
	 */
	public CandyCollection getCandyCollection()
	{
		return this.collection;
	}
	
	/**
	 * Returns the container associated with the candy bag
	 * @return the container of the candy bag
	 */
	public Container getCandyContainer()
	{
		return this.candyContainer;
	}
	
	/**
	 * Adds a new candy to the candy container
	 */
	public void addCandy()
	{
		//Puts the candy within the confines of the candy container
		Candy candy=new GraphicalCandy(new GraphicalPoint(candyContainer.getLeftSide().getLocation().getX()+(Container.WIDTH-Candy.WIDTH)/2, candyContainer.getLeftSide().getLocation().getY()+Container.HEIGHT-Candy.HEIGHT-((Container.HEIGHT*collection.size())/collection.maxSize())));
		this.collection.addElement(candy);
	}
	
	/**
	 * Removes the candy last inserted into the candy container
	 */
	public void removeCandy()
	{
		this.collection.removeElement();
	}
	
	/**
	 * Moves the candy bag to a new location
	 * @param newLocation the new location to move the candy bag to.
	 */
	public void setLocation(Point newLocation)
	{
		int xChange=newLocation.getX()-this.candyContainer.getLeftSide().getLocation().getX();
		int yChange=newLocation.getY()-this.candyContainer.getLeftSide().getLocation().getY();
		
		this.candyContainer.setLocation(newLocation);
		
		for(int i=0;i<this.collection.size();i++)
			this.collection.elementAt(i).setLocation(new GraphicalPoint(this.collection.elementAt(i).getCandyOval().getLocation().getX()+xChange, this.collection.elementAt(i).getCandyOval().getLocation().getY()+yChange));
	}
}
