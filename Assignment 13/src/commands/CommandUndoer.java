package commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a new history that tracks all of the commands in this simulation
 * and allows them to be executed, undone, and redone
 * @author Robert Dallara
 *
 */
public class CommandUndoer implements Undoer {

	private int commandIndex=0;
	private List<Command> commandList=new ArrayList<Command>();
	
	/**
	 * Checks to see if the undo command can be executed
	 * @return whether or not the undo command can be executed
	 */
	public boolean preUndo()
	{
		return commandIndex>0;
	}
	
	/**
	 * Checks to see if the redo command can be executed
	 * @return whether or not the redo command can be executed
	 */
	public boolean preRedo()
	{
		return commandIndex<commandList.size();
	}
	
	/**
	 * Undoes the last command (if there is a command to be undone)
	 */
	public void undo()
	{
		assert preUndo();
		
		commandIndex--;
		commandList.get(commandIndex).undo();
	}

	/**
	 * Redo the last command (if any previous commands were undone)
	 */
	public void redo()
	{
		assert preRedo();
		
		commandList.get(commandIndex).execute();
		commandIndex++;
	}
	
	/**
	 * Executes a certain command
	 * @param command the command to execute
	 */
	public void execute(Command command)
	{
		//Reset command list if any commands previously undone
		if(commandIndex<commandList.size())
		{
			commandIndex=0;
			commandList.clear();
		}
		
		//Execute the command and add it to the list of commands
		command.execute();
		commandList.add(command);
		
		commandIndex++;
	}
}
