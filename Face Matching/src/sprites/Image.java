package sprites;

import javax.swing.Icon;

public interface Image extends Sprite, Icon {

	String getImageFileName();
	boolean equals(Image anotherImage);
}
