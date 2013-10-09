package commands;

import graphics.*;

/**
 * A class used for objects representing commands telling the avatar to give
 * candy to a house
 * @author Robert Dallara
 *
 */
public class AGiveCandyCommand implements Command {

	private VisitingTrickOrTreatAvatar avatar;
	private int numberOfCandies;
	
	/**
	 * Constructs a new "give candy" command object
	 * @param avatar the avatar that will be giving the candy
	 * @param numberOfCandies how many candies to give
	 */
	public AGiveCandyCommand(VisitingTrickOrTreatAvatar avatar, int numberOfCandies)
	{
		this.avatar=avatar;
		this.numberOfCandies=numberOfCandies;
	}
	
	/**
	 * Executes this command
	 */
	public void execute()
	{
		avatar.giveCandies(numberOfCandies);
	}
	
	/**
	 * Undoes this command
	 */
	public void undo()
	{
		avatar.takeCandies(numberOfCandies);
	}
}
