package buttonpanel;

import javax.swing.*;


import emotionimages.*;
import memory.*;

/**
 * Creates a panel with both a button (which represents a card) and a label describing
 * that card's emotion (meant to be used with "face picture" cards)
 * 
 * @author Robert Dallara
 *
 */
public class LabeledButtonPanel extends ButtonPanel {
	
	private boolean completed;
	
	private EmotionType backgroundEmotion;
	private ImageIcon currentEmotionLabel;
	
	private JLabel emotionLabel;
	
	/**
	 * Constructs a new button panel with a label
	 * @param button the button on the panel that represents a card
	 * @param emotion the card's emotion
	 */
	public LabeledButtonPanel(JButton button, EmotionType emotion)
	{
		super(button);
		
		backgroundEmotion=emotion;
		completed=false;
		
		/**The super constructor automatically tries to construct the panel in a different
		 * way, so disregard it and construct it using the methods of this object
		 */
		this.remove(box);
		this.organizeBox();
	}
	
	/**
	 * Changes the background color of this panel when the card (which is represented
	 * by the button) is flipped over
	 */
	public void switchBackgroundColor()
	{
		if(backgroundColor.equals(notSelectedBackgroundColor))
			backgroundColor=selectedBackgroundColor;
		
		else if(backgroundColor.equals(selectedBackgroundColor))
			backgroundColor=notSelectedBackgroundColor;
		
		this.setBackground(backgroundColor);
	}
	
	/**
	 * When the button's card has been identified as part of a match, change its layout to reflect that
	 */
	public void setBackgroundToCompleted()
	{
		this.remove(box);
		completed=true;
		
		this.organizeBox();
	}
	
	/**
	 * Organizes the look and style of this panel depending on the circumstances
	 */
	protected void organizeBox()
	{
		box=Box.createVerticalBox();
		
		/**
		 * If the button's card has been found as part of a match, change its background
		 * to red and reveal the card's emotion
		 */
		if(completed)
		{
			backgroundColor=completedBackgroundColor;
			currentEmotionLabel=new ImageIcon(LabelGrabber.getLabel(backgroundEmotion));
		}
		
		/**
		 * If the card hasn't been found in a match yet (and its not selected),
		 * keep the panel's background the same as that of the frame and hide
		 * the card's emotion
		 */
		else
		{
			backgroundColor=notSelectedBackgroundColor;
			currentEmotionLabel=new ImageIcon(LabelGrabber.getUncertainLabel());
		}
		
		emotionLabel=new JLabel(currentEmotionLabel);
		this.setBackground(backgroundColor);
		
		box.add(cardButton);
		box.add(emotionLabel);
		
		this.add(box);
	}
}
