package graphics;

import basicgeometry.GraphicalPoint;
import basicgeometry.Point;

/**
 * Creates an object that represents the "property" of the house
 * (which includes both the house and mailbox nearby it, but no path)
 * @author Robert Dallara
 *
 */
public class HouseProperty implements Property {

	protected SpecificImageLabel houseLabel;
	protected MailBox mailBox;
	
	/**
	 * Creates a new house property object
	 * @param location where to place the house property
	 * @param mailBoxWidth the width of the house's mailbox
	 * @param mailBoxHeight the height of the house's mailbox
	 * @param mailBoxSupportLength the length of the support of the house's mailbox
	 */
	public HouseProperty(Point location, int mailBoxWidth, int mailBoxHeight, int mailBoxSupportLength)
	{
		this.houseLabel=new HouseLabel(location.getX(), location.getY());
		
		//Place the mailbox close to the house and to the bottom-left of it
		int houseHeight=houseLabel.getHeight();
		int mailBoxVerticalOffset=(int)(0.4*houseHeight);
		
		Point mailBoxLocation=new GraphicalPoint(houseLabel.getLocation().getX(), houseLabel.getLocation().getY()+houseLabel.getHeight()+mailBoxVerticalOffset);
		this.mailBox=new GraphicalMailBox(mailBoxLocation, mailBoxWidth, mailBoxHeight, mailBoxSupportLength);
	}
	
	/**
	 * Creates a new house property object with a mailbox of default dimensions
	 * @param location where to place the house property
	 */
	public HouseProperty(Point location)
	{
		this.houseLabel=new HouseLabel(location.getX(), location.getY());
		
		//Place the mailbox close to the house and to the bottom-left of it
		int houseHeight=houseLabel.getHeight();
		int mailBoxVerticalOffset=(int)(0.4*houseHeight);
		
		Point mailBoxLocation=new GraphicalPoint(houseLabel.getLocation().getX(), houseLabel.getLocation().getY()+houseLabel.getHeight()+mailBoxVerticalOffset);
		this.mailBox=new GraphicalMailBox(mailBoxLocation);
	}
	
	/**
	 * Returns the house label object of this house property
	 * @return the house on the house property
	 */
	public SpecificImageLabel getHouseLabel()
	{
		return this.houseLabel;
	}
	
	/**
	 * Returns the mail box object of this house property
	 * @return the mail box on the house property
	 */
	public MailBox getMailBox()
	{
		return this.mailBox;
	}
}
