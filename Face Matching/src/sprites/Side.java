package sprites;

import javax.swing.*;

/**
 * An object representing the side of a card in this game
 * @author Robert Dallara
 *
 */
public class Side {

	private boolean hasImage;
	private AImage image;
	
	/**
	 * Creates a side object
	 * @param image the image on this side
	 */
	public Side(AImage image)
	{
		this.image=image;
		this.hasImage=true;
	}
	
	/**
	 * Creates a side object that has no main image (face picture) on it
	 */
	public Side()
	{
		this.hasImage=false;
		this.image=new AImage(new ImageIcon(Card.class.getResource("Front_Side.png")));
	}
	
	/**
	 * Returns whether or not this side has a face picture on it
	 * @return whether or not this side has a main (face) picture
	 */
	public boolean hasIcon()
	{
		return this.hasImage;
	}
	
	/**
	 * Gets the image displayed on this side
	 * @return this side's image
	 */
	public AImage getImage()
	{
		return this.image;
	}
	
	/**
	 * Checks to see if this side is equal to another side
	 * @param anotherSide the other side to compare this side to
	 * @return whether or not the image on this side equals the image on the other side
	 */
	public boolean equals(Side anotherSide)
	{
		if((this.getImage()).equals(anotherSide.getImage()))
			return true;
		
		return false;
	}
}
