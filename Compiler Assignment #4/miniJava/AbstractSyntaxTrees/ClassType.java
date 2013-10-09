/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassType extends Type
{
    public ClassType(String cn, SourcePosition posn){
        super(TypeKind.CLASS, posn);
        className = cn;
        classDecl = null;
    }
        
    public <A,R> R visit(Visitor<A,R> v, A o) {
        return v.visitClassType(this, o);
    }

    public String className;
    public ClassDecl classDecl;  // resolved in contextual analysis
}
