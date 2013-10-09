package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.*;

public class DeRef extends Reference {

	public DeRef(Reference ref, Identifier id, SourcePosition posn)
	{
		super(posn);

		this.ref=ref;
		this.id=id;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitDeRef(this, o);
	}
	
	public Reference ref;
	public Identifier id;
}
