package graphics;

/**
 * An interface used to specify extra methods needed in full house property objects
 * in addition to their additional required non-full house property methods.
 * @author Robert Dallara
 *
 */
public interface FullProperty extends Property {

	final int DEFAULT_NUMBER_OF_CANDIES=3;
	
	boolean hasVisitor(TrickOrTreatAvatar avatar);
	void update(VisitingTrickOrTreatAvatar visitingAvatar);
	
	SpecificImageLabel getPathLabel();
	Bag getCandyBag();
}
