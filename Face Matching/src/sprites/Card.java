package sprites;

import javax.swing.*;

/**
 * An object representing a card on the grid in this game
 * @author Robert Dallara
 *
 */
public class Card {

	private boolean isFlippable;
	
	private Side frontSide;
	private Side backSide;
	
	private Side visibleSide;
	
	/**
	 * Creates a new card object
	 * @param icon the card's main icon (which is flipped over at first)
	 */
	public Card(ImageIcon icon)
	{
		this.isFlippable=true;
		
		//The card's back side is invisible at first
		backSide=new Side(new AImage(icon));
		frontSide=new Side(new AImage(new ImageIcon(Card.class.getResource("Front_Side.png"))));
		
		visibleSide=frontSide;
	}
	
	/**
	 * Returns whether or not this card is face down
	 * @return whether or not this card is face down
	 */
	public boolean isFaceDown()
	{
		return visibleSide.equals(frontSide);
	}
	
	/**
	 * Flips this card over (if it can be flipped)
	 */
	public void flip()
	{
		if(isFlippable)
		{
			if(visibleSide.equals(frontSide))
				visibleSide=backSide;
			
			else
				visibleSide=frontSide;
		}
	}
	
	/**
	 * Returns the image on the card that is currently face-up and visible to the user
	 * @return
	 */
	public ImageIcon getVisibleSideIcon()
	{
		return visibleSide.getImage().getIcon();
	}
	
	/**
	 * Returns whether or not this card can be flipped
	 * @return whether or not this card is flippable
	 */
	public boolean isFlippable()
	{
		return this.isFlippable;
	}
	
	/**
	 * Make the card impossible to flip if it can be flipped, and vice versa
	 */
	public void reverseFlippability()
	{
		this.isFlippable=!this.isFlippable;
	}
}
