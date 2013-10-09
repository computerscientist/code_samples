package commands;

/**
 * An interface used as a type for all undoer objects
 * @author Robert Dallara
 *
 */
public interface Undoer {

	boolean preUndo();
	boolean preRedo();
	
	void undo();
	void redo();
	
	void execute(Command command);
}
