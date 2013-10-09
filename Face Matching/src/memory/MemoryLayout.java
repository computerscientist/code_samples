package memory;

/**
 * Stores the types of emotions and pictures in a grid corresponding to
 * the grid of cards on the actual frame
 * 
 * @author Robert Dallara
 *
 */
public class MemoryLayout {

	private EmotionType[][] emotionTypeLayout;
	private PictureType[][] pictureTypeLayout;
	
	/**
	 * Constructs a new memory storage object for the grid of cards
	 * @param rows how many rows of cards there are in the grid
	 * @param columns how many columns of cards there are in the grid
	 */
	public MemoryLayout(int rows, int columns)
	{
		this.emotionTypeLayout=new EmotionType[rows][columns];
		this.pictureTypeLayout=new PictureType[rows][columns];
	}
	
	/**
	 * Returns the emotion of a card at a certain location
	 * @param row the row of the card
	 * @param column the column of the card
	 * @return the emotion of the card at this particular location
	 */
	public EmotionType getEmotionType(int row, int column)
	{
		return emotionTypeLayout[row][column];
	}
	
	/**
	 * Sets the emotion of a card at a certain location (run at startup)
	 * @param row the row of the card
	 * @param column the column of the card
	 * @param type the emotion of the card that this memory object needs to remember
	 */
	public void setEmotionType(int row, int column, EmotionType type)
	{
		this.emotionTypeLayout[row][column]=type;
	}
	
	/**
	 * Returns the picture type of a card at a certain location
	 * @param row the row of the card
	 * @param column the column of the card
	 * @return the type of picture that the card at this particular location has (i.e face/word)
	 */
	public PictureType getPictureType(int row, int column)
	{
		return pictureTypeLayout[row][column];
	}
	
	/**
	 * Sets the picture type of a card at a certain location (run at startup)
	 * @param row the row of the card
	 * @param column the column of the card
	 * @param type the picture classification of the card that this memory object needs to remember
	 */
	public void setPictureType(int row, int column, PictureType type)
	{
		this.pictureTypeLayout[row][column]=type;
	}
}
