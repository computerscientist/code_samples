package miniJava.CodeGenerator;

import miniJava.AbstractSyntaxTrees.*;

/**
 * Keeps track of the class declaration and address of an instruction which
 * creates a new object of a certain class type (in case that particular class
 * hasn't been visited yet when that instruction is encountered)
 *  
 * @author Robert Dallara
 *
 */
public class ClassDisplacementPair {

	private ClassDecl cd;
	private int instructionAddress;
	
	public ClassDisplacementPair(int instructionAddress, ClassDecl cd)
	{
		this.cd=cd;
		this.instructionAddress=instructionAddress;
	}
	
	public ClassDecl getClassDecl()
	{
		return this.cd;
	}
	
	public int getClassAddress()
	{
		if(this.cd.runtimeEntity==null || !(this.cd.runtimeEntity instanceof KnownAddress))
			return -1;
		
		return ((KnownAddress) this.cd.runtimeEntity).address;
	}
	
	public int getInstructionAddress()
	{
		return this.instructionAddress;
	}
}
