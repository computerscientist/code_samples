package graphics;

import basicgeometry.*;

/**
 * Creates an object that represents the entire "property" of the house
 * (which includes the house, its mailbox nearby, its candy bag, and the path leading to it)
 * @author Robert Dallara
 *
 */
public class FullHouseProperty extends HouseProperty implements FullProperty {

	private SpecificImageLabel pathLabel;
	private Bag candyBag;
	
	/**
	 * Creates a new house property object
	 * @param location where to place the full house property
	 * @param mailBoxWidth the width of the house's mailbox
	 * @param mailBoxHeight the height of the house's mailbox
	 * @param mailBoxSupportLength the length of the support of the house's mailbox
	 */
	public FullHouseProperty(Point location, int mailBoxWidth, int mailBoxHeight, int mailBoxSupportLength)
	{
		super(location, mailBoxWidth, mailBoxHeight, mailBoxSupportLength);
		
		//Creates the path and sets its location so that it is near the door of the house
		this.pathLabel=new GraphicalPathLabel(houseLabel.getLocation().getX()+houseLabel.getWidth()*47/100, houseLabel.getLocation().getY()+houseLabel.getHeight());
		this.candyBag=new CandyBag(new GraphicalPoint(location.getX()+85, location.getY()+103));
		
		for(int i=0;i<FullProperty.DEFAULT_NUMBER_OF_CANDIES;i++)
			this.candyBag.addCandy();
	}
	
	/**
	 * Creates a new house property object with a mailbox of default dimensions
	 * @param location where to place the full house property
	 */
	public FullHouseProperty(Point location)
	{
		super(location);
		
		//Creates the path and sets its location so that it is near the door of the house
		this.pathLabel=new GraphicalPathLabel(houseLabel.getLocation().getX()+houseLabel.getWidth()*47/100, houseLabel.getLocation().getY()+houseLabel.getHeight());
	}
	
	/**
	 * Returns the path label object of this house property
	 * @return the path on the house property
	 */
	public SpecificImageLabel getPathLabel()
	{
		return this.pathLabel;
	}
	
	/**
	 * Returns the candy bag object of this house property
	 * @return the house property's candy bag
	 */
	public Bag getCandyBag()
	{
		return this.candyBag;
	}
	
	/**
	 * Returns whether or not a certain avatar is visiting on the path of the house
	 * @param avatar the avatar whose visitor status is in question
	 * @return whether or not the avatar is visiting the house (on its path)
	 */
	public boolean hasVisitor(TrickOrTreatAvatar avatar)
	{
		Point avatarFootLocation=new GraphicalPoint(avatar.getAvatar().getLeg().getLocation().getX()+avatar.getAvatar().getLeg().getWidth(), avatar.getAvatar().getLeg().getLocation().getY()+avatar.getAvatar().getLeg().getHeight());
		
		if((avatarFootLocation.getX()>=pathLabel.getLocation().getX() && avatarFootLocation.getX()<=pathLabel.getLocation().getX()+pathLabel.getWidth()) &&
		   (avatarFootLocation.getY()>=pathLabel.getLocation().getY() && avatarFootLocation.getY()<=pathLabel.getLocation().getY()+pathLabel.getHeight()))
			
			return true;
		
		return false;
	}
	
	/**
	 * Updates the avatar and connects or disconnects it to a house depending
	 * on whether or not the avatar is visiting it
	 * @param visitingAvatar the avatar to update and check
	 */
	public void update(VisitingTrickOrTreatAvatar visitingAvatar)
	{
		if(this.hasVisitor(visitingAvatar))
			visitingAvatar.connectToHouse(this);
		
		else
			visitingAvatar.disconnectFromHouse();
	}
}
