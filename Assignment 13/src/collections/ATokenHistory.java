package collections;

import tokens.*;

/**
 * Creates a collection that can store up to 100 tokens
 * @author Robert Dallara
 *
 */
public class ATokenHistory implements TokenHistory {

	private final int MAX_SIZE=100;
	private int size;
	
	private Token token[];
	
	/**
	 * Creates an initializes a new token collection
	 */
	public ATokenHistory()
	{
		this.size=0;
		this.token=new Token[this.MAX_SIZE];
	}
	
	/**
	 * Adds a new element to the token collection
	 * @param token the new token to add to the collection
	 */
	public void addElement(Token token)
	{
		if(this.isFull())
			return;
		
		this.token[size]=token;
		size++;
	}
	
	/**
	 * Returns the size of the token collection
	 * @return the collection size
	 */
	public int size()
	{
		return this.size;
	}
	
	/**
	 * Returns the max size of the token collection
	 * @return the max collection size
	 */
	public int maxSize()
	{
		return this.MAX_SIZE;
	}
	
	/**
	 * Returns the token stored at a given index in the token collection
	 * @param index the index of the token to obtain
	 * @return the token at the given index
	 */
	public Token elementAt(int index)
	{
		//Are we trying to return an element outside the bounds of the collection?
		if(index>=MAX_SIZE || index<0)
			return null;
		
		return this.token[index];
	}
	
	/**
	 * Returns whether or not this collection is full
	 * @return whether of not the collection is full
	 */
	private boolean isFull()
	{
		return size==this.MAX_SIZE;
	}
}
