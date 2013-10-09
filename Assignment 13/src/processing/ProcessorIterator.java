package processing;

/**
 * An interface used as a type for all iterator objects (without remove methods)
 * @author Robert Dallara
 *
 * @param <T> the type to be returned during iterations
 */
public interface ProcessorIterator<T> {

	boolean hasNext();
	T next();
}
