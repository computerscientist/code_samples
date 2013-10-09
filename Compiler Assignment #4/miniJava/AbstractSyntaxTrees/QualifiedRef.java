/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.*;

public class QualifiedRef extends Reference {
	
	public QualifiedRef(boolean thisRelative, IdentifierList ql, SourcePosition posn){
		super(posn);
		this.thisRelative = thisRelative;
		qualifierList = ql;
	}
	
	public QualifiedRef(Identifier id) {
		super(id.posn);
		thisRelative = false;
		qualifierList = new IdentifierList();
		qualifierList.add(id);
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitQualifiedRef(this, o);
	}

	public boolean thisRelative;
	public IdentifierList qualifierList;
}
