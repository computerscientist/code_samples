package simulation;

import collections.*;
import graphics.*;

/**
 * An interface used as a type for Halloween simulation objects
 * @author Robert Dallara
 *
 */
public interface Simulation {

	final int MAILBOX_WIDTH=13;
	final int MAILBOX_HEIGHT=13;
	final int MAILBOX_SUPPORT_LENGTH=13;
	
	boolean preMoveAvatar(int xChange, int yChange);
	void moveAvatar(int xChange, int yChange);
	
	boolean preAddHouse();
	void addHouse();
	
	boolean preRemoveHouse();
	void removeHouse();
	
	boolean preGiveCandies(int numberOfCandies);
	void giveCandies(int numberOfCandies);
	
	boolean preTakeCandies(int numberOfCandies);
	void takeCandies(int numberOfCandies);
	
	boolean preSwitchAnimationStatus();
	void switchAnimationStatus();
	
	boolean preUndo();
	void undo();
	
	boolean preRedo();
	void redo();
	
	boolean getAvatarVisitorStatus();
	
	VisitingTrickOrTreatAvatar getVisitingTrickOrTreatAvatar();
	HouseCollection getHouseCollection();
}