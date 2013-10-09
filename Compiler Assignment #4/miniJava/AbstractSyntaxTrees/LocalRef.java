package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.*;

public class LocalRef extends Reference {

	public LocalRef(Identifier id, IdentifierList ql, SourcePosition posn)
	{
		super(posn);
		
		this.id=id;
		this.qualifierList=ql;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitLocalRef(this, o);
	}

	public Identifier id;
	public IdentifierList qualifierList;
}
