package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.*;

public class ClassRef extends Reference {

	public ClassRef(Identifier classIdentifier, IdentifierList qualifierList, SourcePosition posn)
	{
		super(posn);
		
		this.classIdentifier=classIdentifier;
		this.qualifierList=qualifierList; //typeName is assumed to be disjoint from list
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitClassRef(this, o);
	}
	
	public Identifier classIdentifier;
	public IdentifierList qualifierList;
}
