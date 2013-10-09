package commands;

/**
 * An interface used as a type for all command objects
 * @author Robert Dallara
 *
 */
public interface Command {

	void execute();
	void undo();
}
