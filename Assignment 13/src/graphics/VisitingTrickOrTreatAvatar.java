package graphics;

/**
 * An interface used as a type for trick or treat avatars that can visit houses
 * @author Robert Dallara
 *
 */
public interface VisitingTrickOrTreatAvatar extends TrickOrTreatAvatar {

	void connectToHouse(FullProperty property);
	void disconnectFromHouse();
	
	void giveCandies(int numberOfCandies);
	void takeCandies(int numberOfCandies);
	
	void addObserver(FullProperty observerProperty);
	void removeObserver(FullProperty observerProperty);
	
	boolean currentHouseHasRoomForCandies(int numberOfAdditionalCandies);
	boolean currentHouseHasEnoughCandies(int numberOfCandiesToTake);
}
