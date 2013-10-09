package collections;

import graphics.*;

/**
 * Creates a collection that can store up to 7 candies and display them in Object Editor
 * @author Robert Dallara
 *
 */
public class ACandyCollection extends ACollection<Candy> implements CandyCollection {
	
	/**
	 * Creates an initializes a new candy collection
	 */
	public ACandyCollection()
	{
		super(7);
	}
}

