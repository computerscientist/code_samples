package commands;

import graphics.*;

/**
 * A class used for objects representing commands telling the avatar to take
 * candy from a house
 * 
 * @author Robert Dallara
 * 
 */
public class ATakeCandyCommand implements Command {

	private VisitingTrickOrTreatAvatar avatar;

	private int numberOfCandies;

	/**
	 * Constructs a new "take candy" command object
	 * 
	 * @param avatar
	 *            the avatar that will be taking the candy
	 * @param numberOfCandies
	 *            how many candies to take
	 */
	public ATakeCandyCommand(VisitingTrickOrTreatAvatar avatar, int numberOfCandies) 
	{
		this.avatar = avatar;
		this.numberOfCandies = numberOfCandies;
	}

	/**
	 * Executes this command
	 */
	public void execute() {
		avatar.takeCandies(numberOfCandies);
	}

	/**
	 * Undoes this command
	 */
	public void undo() {
		avatar.giveCandies(numberOfCandies);
	}
}
