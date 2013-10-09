package sprites;

import javax.swing.ImageIcon;
import java.util.ArrayList;

public class GraphicalImage extends ImageIcon implements Image {

	private String imageFileName="";
	
	public GraphicalImage(String imageFileName)
	{
		this.imageFileName=imageFileName;
	}
	
	public String getImageFileName()
	{
		return this.imageFileName;
	}
	
	public boolean equals(Image anotherImage)
	{
		return (this.getImageFileName()).equals(anotherImage.getImageFileName());
	}
	
	public ArrayList<Sprite> getComponents()
	{
		ArrayList<Sprite> imageComponents=new ArrayList<Sprite>();
		imageComponents.add(this);
		
		return imageComponents;
	}
}
