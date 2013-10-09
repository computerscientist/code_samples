package main;

import processing.*;
import simulation.*;

import bus.uigen.ObjectEditor;

/**
 * Runs a Halloween simulation as well as a parser that goes with it
 * @author Robert Dallara
 *
 */
public class Assignment13 {

	/**
	 * Starts the program
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		Simulation halloweenSimulation=new HalloweenSimulation();
		Parser commandParser=new CommandParser(halloweenSimulation);
		
		ObjectEditor.edit(halloweenSimulation).hideMainPanel();
		ObjectEditor.edit(commandParser);
	}
}