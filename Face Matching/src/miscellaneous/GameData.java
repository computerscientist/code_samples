package miscellaneous;

import startup.*;

/**
 * Used to store data about the game (such as the grid size and what mode the user
 * is playing in). This class should only be used to store game data; it should not
 * be instantiated
 * 
 * @author Robert Dallara
 *
 */
public class GameData {

	private static int gridRows;
	private static int gridColumns;
	
	private static Mode gameMode;
	
	/**
	 * Returns the number of rows in the current grid
	 * @return how many rows are in the grid of cards
	 */
	public static int getGridRows()
	{
		return gridRows;
	}
	
	/**
	 * Stores how many rows are in the current grid of cards
	 * @param numberOfRows the number of rows in the grid of cards
	 */
	public static void setGridRows(int numberOfRows)
	{
		gridRows=numberOfRows;
	}
	
	/**
	 * Returns the number of columns in the current grid
	 * @return how many columns are in the grid of cards
	 */
	public static int getGridColumns()
	{
		return gridColumns;
	}
	
	/**
	 * Stores how many columns are in the current grid of cards
	 * @param numberOfColumns the number of columns in the grid of cards
	 */
	public static void setGridColumns(int numberOfColumns)
	{
		gridColumns=numberOfColumns;
	}
	
	/**
	 * Returns the game mode that the user is playing in
	 * @return the current game mode
	 */
	public static Mode getGameMode()
	{
		return gameMode;
	}
	
	/**
	 * Stores the game mode that the user is playing in
	 * @param mode the current game mode
	 */
	public static void setGameMode(Mode mode)
	{
		gameMode=mode;
	}
}
