package collections;

import util.models.VectorListener;

/**
 * An interface used as a type for all collection objects
 * @author Robert Dallara
 *
 */
public interface Collection<T> {

	T elementAt(int index);
	
	void addElement(T property);
	void removeElement();
	
	void addVectorListener(VectorListener listener);
	
	int size();
	int maxSize();
}
