/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class IndexedRef extends Reference {
	
	public IndexedRef(Reference ref, Expression e, SourcePosition posn){
		super(posn);
		this.ref = ref;
		this.indexExpr = e;
	}

	public <A,R> R visit(Visitor<A,R> v, A o){
		return v.visitIndexedRef(this, o);
	}
	
	public Reference ref;
	public Expression indexExpr;
}
