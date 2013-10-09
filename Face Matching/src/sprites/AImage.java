package sprites;

import javax.swing.*;

/**
 * Creates an object that stores an image
 * @author Robert Dallara
 *
 */
public class AImage {

	private ImageIcon icon;
	
	/**
	 * Creates a new image-storing object
	 * @param icon the icon that this object is storing
	 */
	public AImage(ImageIcon icon)
	{
		this.icon=icon;
	}
	
	/**
	 * Returns the icon that this object is storing
	 * @return this object's icon
	 */
	public ImageIcon getIcon()
	{
		return this.icon;
	}
	
	/**
	 * Checks to see if this object's stored image is equal to another image-storing object's image
	 * @param anotherImage the other image-storing object to compare this one to
	 * @return whether or not this object is equal to the other image-storing object
	 */
	public boolean equals(AImage anotherImage)
	{
		return (this.getIcon()).equals(anotherImage.getIcon());
	}
}
