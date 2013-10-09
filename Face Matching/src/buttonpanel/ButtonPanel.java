package buttonpanel;

import java.awt.*;

import javax.swing.*;

import emotionimages.LabelGrabber;

/**
 * Creates a panel with a button on it (meant to be used with "word picture" cards)
 * @author Robert Dallara
 *
 */
public class ButtonPanel extends JPanel {

	public static final Color frameDefaultBackgroundColor=new Color(238, 238, 238);
	public static final Color notSelectedBackgroundColor=frameDefaultBackgroundColor;
	public static final Color selectedBackgroundColor=Color.YELLOW;
	public static final Color completedBackgroundColor=Color.RED;
	
	protected Box box;
	protected Color backgroundColor;
	
	protected JButton cardButton;
	
	private boolean uncertaintyLabelShowing;
	
	/**
	 * Constructs a new button panel
	 * @param button the button to add to the panel
	 */
	public ButtonPanel(JButton button)
	{
		cardButton=button;
		
		this.organizeBox();
	}
	
	/**
	 * Returns the button that this panel has
	 * @return
	 */
	public JButton getJButton()
	{
		return cardButton;
	}
	
	/**
	 * Changes the background color behind the button (which represents a card)
	 * whenever the card is flipped
	 */
	public void switchBackgroundColor()
	{
		if(backgroundColor.equals(notSelectedBackgroundColor))
			backgroundColor=selectedBackgroundColor;
		
		else if(backgroundColor.equals(selectedBackgroundColor))
			backgroundColor=notSelectedBackgroundColor;
		
		this.setBackground(backgroundColor);
		
		//Get rid of question mark below button when back side of card flipped to front
		if(uncertaintyLabelShowing)
			this.getRidOfUncertaintyLabel();
		
		//If card being reset, put button panel back to original state
		else
		{
			this.remove(box);
			this.organizeBox();
		}
	}
	
	/**
	 * When card on button has been found as part of match, change its background
	 * to red and make sure that the button takes up the entire panel (no question
	 * mark below the card anymore)
	 */
	public void setBackgroundToCompleted()
	{
		this.setBackground(completedBackgroundColor);
		this.getRidOfUncertaintyLabel();
	}
	
	/**
	 * Gets rid of the question mark below the card (such as when its hidden
	 * back side is flipped over to its front)
	 */
	private void getRidOfUncertaintyLabel()
	{
		uncertaintyLabelShowing=false;
		
		this.remove(box);
		
		box=Box.createVerticalBox();
		box.add(cardButton);
		
		this.add(box);
	}
	
	/**
	 * Structures the button panel to have both a button (which represents a card)
	 * as well as a question mark beneath it (for when the card's visible side 
	 * is hidden on the back)
	 */
	protected void organizeBox()
	{
		uncertaintyLabelShowing=true;
		
		box=Box.createVerticalBox();
		
		box.add(cardButton);
		box.add(new JLabel(new ImageIcon(LabelGrabber.getUncertainLabel())));
		
		backgroundColor=notSelectedBackgroundColor;
		
		this.setBackground(backgroundColor);
		this.add(box);
	}
}
