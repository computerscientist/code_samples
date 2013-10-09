package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.*;

public class ThisRef extends Reference {

	public ThisRef(Identifier classIdentifier, IdentifierList ql, SourcePosition posn)
	{
		super(posn);
		
		this.classIdentifier=classIdentifier;
		this.qualifierList = ql;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitThisRef(this, o);
	}

	public Identifier classIdentifier; //The class associated with the "this"
	public IdentifierList qualifierList;
}
