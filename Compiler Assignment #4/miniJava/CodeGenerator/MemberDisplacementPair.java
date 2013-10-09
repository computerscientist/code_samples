package miniJava.CodeGenerator;

import miniJava.AbstractSyntaxTrees.*;

/**
 * Keeps track of the member declaration and address of an instruction which
 * references a certain member of a certain class (in case that particular member
 * hasn't been visited yet when that instruction is encountered)
 *  
 * @author Robert Dallara
 *
 */
public class MemberDisplacementPair {

	private MemberDecl md;
	private int instructionAddress;
	
	public MemberDisplacementPair(int instructionAddress, MemberDecl md)
	{
		this.md=md;
		this.instructionAddress=instructionAddress;
	}
	
	public MemberDecl getMemberDecl()
	{
		return this.md;
	}
	
	public int getMemberAddress()
	{
		if(this.md.runtimeEntity==null)
			return -1;
		
		else if(this.md.runtimeEntity instanceof KnownAddress)
			return ((KnownAddress) this.md.runtimeEntity).address;
		
		else
			return ((UnknownValue) this.md.runtimeEntity).index;
	}
	
	public int getInstructionAddress()
	{
		return this.instructionAddress;
	}
}
