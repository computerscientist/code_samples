package basicgeometry;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.List;
import java.util.ArrayList;

/**
 * Creates a shape that can be displayed in object editor
 * @author Robert Dallara
 *
 */
public abstract class GraphicalShape implements Shape {

	protected Point location;
	
	protected int width;
	protected int height;
	
	protected List<PropertyChangeListener> listeners;
	
	/**
	 * Creates a new graphical shape
	 */
	public GraphicalShape()
	{
		listeners=new ArrayList<PropertyChangeListener>();
	}
	
	/**
	 * Returns the width of the shape's bounding box
	 * @return the width of the shape's bounding box
	 */
	public int getWidth()
	{
		return this.width;
	}
	
	/**
	 * Returns the height of the shape's bounding box
	 * @return the height of the shape's bounding box
	 */
	public int getHeight()
	{
		return this.height;
	}
	

	/**
	 * Returns the point of the shape's location
	 * @return the shape's location
	 */
	public Point getLocation()
	{
		return this.location;
	}
	
	/**
	 * Moves the shape to a new location
	 * @param newLocation the location to move the shape to.
	 */
	public void setLocation(Point newLocation)
	{
		Point oldLocation=location;
		location=newLocation;
		
		notifyAllListeners(new PropertyChangeEvent(this, "location", oldLocation, newLocation));
	}
	
	/**
	 * Adds a new property change listener to this avatar
	 * @param listener the listener that will be listening in on this avatar's properties
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Notifies all the listeners of this shape that an event has changed the shape's properties
	 * @param event the event to notify the listeners about
	 */
	private void notifyAllListeners(PropertyChangeEvent event)
	{
		for(int i=0;i<listeners.size();i++)
			listeners.get(i).propertyChange(event);
	}
}
