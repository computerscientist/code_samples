package collections;

import util.models.VectorChangeEvent;
import util.models.VectorListener;

import java.util.List;
import java.util.ArrayList;

/**
 * Creates a collection that can store different types of objects (such as houses and candies)
 * @author Robert Dallara
 *
 * @param <T> the object type that the collection is storing
 */
public abstract class ACollection<T> implements Collection<T> {

	protected final int MAX_SIZE;
	protected int size;
	
	protected Object element[];
	protected List<VectorListener> listeners;
	
	/**
	 * Creates a new collection
	 * @param MAX_SIZE the maximum size of the collection
	 */
	public ACollection(int MAX_SIZE)
	{
		this.size=0;
		this.MAX_SIZE=MAX_SIZE;
		
		this.element=new Object[this.MAX_SIZE];
		this.listeners=new ArrayList<VectorListener>();
	}
	
	/**
	 * Adds a new vector listener to this collection
	 * @param listener the vector listener that listens in on this collection
	 */
	public void addVectorListener(VectorListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Updates all of the listeners listening to this collection of an event
	 * @param event the event to notify the listeners about
	 */
	private void notifyListeners(VectorChangeEvent event)
	{
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).updateVector(event);
	}
	
	/**
	 * Adds a new element to the object collection
	 * @param element the new object to add to the collection
	 */
	public void addElement(T element)
	{
		if(this.isFull())
			return;
		
		this.element[size]=element;
		size++;
		
		this.notifyListeners(new VectorChangeEvent(this, VectorChangeEvent.AddComponentEvent, size-1, null, element, size));
	}
	
	/**
	 * Removes the last element from the object collection
	 */
	public void removeElement()
	{
		if(size>0)
		{
			size--;
			this.notifyListeners(new VectorChangeEvent(this, VectorChangeEvent.DeleteComponentEvent, size, null, element, size-1));
		}
	}
	
	/**
	 * Returns the size of the object collection
	 * @return the collection size
	 */
	public int size()
	{
		return this.size;
	}
	
	/**
	 * Returns the max size of the object collection
	 * @return the max collection size
	 */
	public int maxSize()
	{
		return this.MAX_SIZE;
	}
	
	/**
	 * Returns the object stored at a given index in the object collection
	 * @param index the index of the object to obtain
	 * @return the object at the given index
	 */
	
	@SuppressWarnings("unchecked")
	public T elementAt(int index)
	{
		//Are we trying to return an element outside the bounds of the collection?
		if(index>=MAX_SIZE || index<0)
			return null;
		
		return (T) this.element[index];
	}
	
	/**
	 * Returns whether or not this collection is full
	 * @return whether of not the collection is full
	 */
	protected boolean isFull()
	{
		return size==this.MAX_SIZE;
	}
}
