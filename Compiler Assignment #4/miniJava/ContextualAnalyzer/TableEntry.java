package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.*;

public class TableEntry {

	String id;
	TableEntry previousEntry; //Used to implement identification table as linked list
	Declaration declaration;
	int level;
	boolean isNull; //Whether or not associated entry is usable
	
	public TableEntry(String id, Declaration declaration, int level, boolean isNull, TableEntry previousEntry)
	{
		this.id=id;
		this.declaration=declaration;
		this.level=level;
		this.isNull=isNull;
		this.previousEntry=previousEntry;
	}
}
