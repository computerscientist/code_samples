package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.*;

public class MemberRef extends Reference {

	public MemberRef(Identifier classIdentifier, IdentifierList qualifierList, SourcePosition posn)
	{
		super(posn);
		
		this.qualifierList=qualifierList;
		this.classIdentifier=classIdentifier; //class reference associated with this reference
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitMemberRef(this, o);
	}
	
	public Identifier classIdentifier;
	public IdentifierList qualifierList;
}
