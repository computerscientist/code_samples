package emotionimages;

import memory.*;
import java.net.URL;

/**
 * Used to find the locations of the different emotion labels, which are displayed
 * beneath the cards (not the emotion word pictures). This class is not meant to be
 * instantiated.
 * 
 * @author Robert Dallara
 *
 */
public class LabelGrabber {

	private static URL labelsFolderLocation=LabelGrabber.class.getResource("Emotion Labels");
	
	/**
	 * Finds the location of the label for a particular emotion
	 * @param emotionType the emotion to find the label for
	 * @return
	 */
	public static String getLabel(EmotionType emotionType)
	{
		if(emotionType.equals(EmotionType.HAPPY))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Happy_Label.png";
		
		else if(emotionType.equals(EmotionType.SAD))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Sad_Label.png";
		
		else if(emotionType.equals(EmotionType.CONFUSED))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Confused_Label.png";
		
		else if(emotionType.equals(EmotionType.SCARED))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Scared_Label.png";
		
		else if(emotionType.equals(EmotionType.ANGRY))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Angry_Label.png";
		
		else if(emotionType.equals(EmotionType.SLEEPY))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Sleepy_Label.png";
		
		else if(emotionType.equals(EmotionType.SURPRISED))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Surprised_Label.png";
		
		else if(emotionType.equals(EmotionType.DISAPPOINTED))
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Disappointed_Label.png";
		
		else
			return labelsFolderLocation.getPath().replace("%20", " ")+"/Nervous_Label.png";
	}
	
	/**
	 * Finds the location of the "question mark" label (which is displayed beneath
	 * a card until it is found as part of a match)
	 * 
	 * @return the location of the "question mark" label
	 */
	public static String getUncertainLabel()
	{
		return labelsFolderLocation.getPath().replace("%20", " ")+"/Uncertain_Label.png";
	}
}
