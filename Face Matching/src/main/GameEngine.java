package main;

import startup.*;

/**
 * Starts up the face matching game
 * @author Robert Dallara
 *
 */
public class GameEngine {
	
	/**
	 * Starts the program
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		startGame();
	}
	
	/**
	 * Starts the game by prompting the user what mode they want to play in (using a frame)
	 */
	public static void startGame()
	{
		new ModeFrame();
	}
}
