package emotionimages;

import java.io.File;
import java.net.URL;
import java.util.Random;

import memory.*;

/**
 * Finds the locations of pictures of different emotions. This class is not meant
 * to be instantiated.
 * 
 * @author Robert Dallara
 *
 */
public class ImageGrabber {

	private static URL emotionPicturesFolderLocation=ImageGrabber.class.getResource("Emotion Pictures");
	private static URL emotionWordPicturesFolderLocation=ImageGrabber.class.getResource("Emotion Word Pictures");

	/**
	 * Finds the location of a happy face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the happy face
	 */
	public static String grabHappyFace(PictureType pictureType)
	{	
		//Retrieves a random happy face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String happyFolderLocation=emotionPicturesFolderLocation.getPath()+"/happy";
			File happyFolder=new File(happyFolderLocation.replace("%20", " "));

			File[] happyPictures=happyFolder.listFiles();
			return happyPictures[new Random().nextInt(happyPictures.length)].toString();
		}

		//If looking for the happy "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Happy.png";
	}

	/**
	 * Finds the location of a sad face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the sad face
	 */
	public static String grabSadFace(PictureType pictureType)
	{
		//Retrieves a random sad face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String sadFolderLocation=emotionPicturesFolderLocation.getPath()+"/sad";
			File sadFolder=new File(sadFolderLocation.replace("%20", " "));

			File[] sadPictures=sadFolder.listFiles();

			return sadPictures[new Random().nextInt(sadPictures.length)].toString();
		}

		//If looking for the sad "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Sad.png";
	}

	/**
	 * Finds the location of a confused face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the confused face
	 */
	public static String grabConfusedFace(PictureType pictureType)
	{
		//Retrieves a random confused face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String confusedFolderLocation=emotionPicturesFolderLocation.getPath()+"/confused";
			File confusedFolder=new File(confusedFolderLocation.replace("%20", " "));

			File[] confusedPictures=confusedFolder.listFiles();

			return confusedPictures[new Random().nextInt(confusedPictures.length)].toString();
		}

		//If looking for the confused "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Confused.png";
	}

	/**
	 * Finds the location of a scared face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the scared face
	 */
	public static String grabScaredFace(PictureType pictureType)
	{
		//Retrieves a random scared face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String scaredFolderLocation=emotionPicturesFolderLocation.getPath()+"/scared";
			File scaredFolder=new File(scaredFolderLocation.replace("%20", " "));

			File[] scaredPictures=scaredFolder.listFiles();

			return scaredPictures[new Random().nextInt(scaredPictures.length)].toString();
		}

		//If looking for the scared "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Scared.png";
	}

	/**
	 * Finds the location of an angry face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the angry face
	 */
	public static String grabAngryFace(PictureType pictureType)
	{
		//Retrieves a random angry face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String angryFolderLocation=emotionPicturesFolderLocation.getPath()+"/angry";
			File angryFolder=new File(angryFolderLocation.replace("%20", " "));

			File[] angryPictures=angryFolder.listFiles();

			return angryPictures[new Random().nextInt(angryPictures.length)].toString();
		}

		//If looking for the angry "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Angry.png";
	}

	/**
	 * Finds the location of a sleepy face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the sleepy face
	 */
	public static String grabSleepyFace(PictureType pictureType)
	{
		//Retrieves a random sleepy face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String sleepyFolderLocation=emotionPicturesFolderLocation.getPath()+"/sleepy";
			File sleepyFolder=new File(sleepyFolderLocation.replace("%20", " "));

			File[] sleepyPictures=sleepyFolder.listFiles();

			return sleepyPictures[new Random().nextInt(sleepyPictures.length)].toString();
		}

		//If looking for the sleepy "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Sleepy.png";
	}

	/**
	 * Finds the location of a surprised face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the surprised face
	 */
	public static String grabSurprisedFace(PictureType pictureType)
	{
		//Retrieves a random surprised face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String surprisedFolderLocation=emotionPicturesFolderLocation.getPath()+"/surprised";
			File surprisedFolder=new File(surprisedFolderLocation.replace("%20", " "));

			File[] surprisedPictures=surprisedFolder.listFiles();

			return surprisedPictures[new Random().nextInt(surprisedPictures.length)].toString();
		}

		//If looking for the surprised "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Surprised.png";
	}

	/**
	 * Finds the location of a disappointed face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the disappointed face
	 */
	public static String grabDisappointedFace(PictureType pictureType)
	{
		//Retrieves a random disappointed face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String disappointedFolderLocation=emotionPicturesFolderLocation.getPath()+"/disappointed";
			File disappointedFolder=new File(disappointedFolderLocation.replace("%20", " "));

			File[] disappointedPictures=disappointedFolder.listFiles();

			return disappointedPictures[new Random().nextInt(disappointedPictures.length)].toString();
		}

		//If looking for the disappointed "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Disappointed.png";
	}

	/**
	 * Finds the location of a nervous face
	 * @param pictureType the type of picture to find (i.e face/word picture)
	 * @return the location of the nervous face
	 */
	public static String grabNervousFace(PictureType pictureType)
	{
		//Retrieves a random nervous face
		if(pictureType.equals(PictureType.FACE_PICTURE))
		{
			String nervousFolderLocation=emotionPicturesFolderLocation.getPath()+"/nervous";
			File nervousFolder=new File(nervousFolderLocation.replace("%20", " "));

			File[] nervousPictures=nervousFolder.listFiles();

			return nervousPictures[new Random().nextInt(nervousPictures.length)].toString();
		}

		//If looking for the nervous "word picture", locate, find, and return its location
		return (emotionWordPicturesFolderLocation.getPath()).replace("%20", " ")+"/Nervous.png";
	}
}
