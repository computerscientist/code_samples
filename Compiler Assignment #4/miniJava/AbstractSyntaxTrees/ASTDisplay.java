/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

/**
 * Display AST in text form
 * @author prins
 * @version COMP 520 V2.2
 * 
 * visitXXX( AST Node type XXX, prefix string for display of each line)
 */
public class ASTDisplay implements Visitor<String,Object> {
	
	public static boolean showPosition = false;
    
    public void showTree(AST ast){
        System.out.println("======= AST Display =========================");
        ast.visit(this, "");
        System.out.println("=============================================");
    }   
    
    // procedures to format output
    private void show(String arg, String text) {
        System.out.println(arg + text);
    }
    
    private void show(String arg, AST node) {
    	System.out.println(arg + node.toString());
    }
    	
    private String indent(String arg) {
        return arg + "  ";
    }
    
    public void showQuoted(String arg, String text) {
    	System.out.println(arg + "\"" + text + "\"");
    }
    
    // Package
    public Object visitPackage(Package prog, String arg){
        show(arg, prog);
        ClassDeclList cl = prog.classDeclList;
        show(arg,"  ClassDeclList [" + cl.size() + "]");
        String pfx = arg + "  . "; 
        for (ClassDecl c: prog.classDeclList){
            c.visit(this, pfx);
        }
        return null;
    }
    
    
  // Declarations
    public Object visitClassDecl(ClassDecl clas, String arg){
        show(arg, clas);
        showQuoted(indent(arg), clas.name);
        show(arg,"  FieldDeclList [" + clas.fieldDeclList.size() + "]");
        String pfx = arg + "  . "; 
        for (FieldDecl f: clas.fieldDeclList)
        	f.visit(this, pfx);
        show(arg,"  MethodDeclList [" + clas.methodDeclList.size() + "]");
        for (MethodDecl m: clas.methodDeclList)
        	m.visit(this, pfx);
        return null;
    }
    
    public Object visitFieldDecl(FieldDecl f, String arg){
       	show(arg, "(" + (f.isPrivate ? "private": "public") 
    			+ (f.isStatic ? " static) " :") ") + f.toString());
    	f.type.visit(this, indent(arg));
    	showQuoted(indent(arg), f.name);
        return null;
    }
    
    public Object visitMethodDecl(MethodDecl m, String arg){
       	show(arg, "(" + (m.isPrivate ? "private": "public") 
    			+ (m.isStatic ? " static) " :") ") + m.toString());
    	m.type.visit(this, indent(arg));
    	showQuoted(indent(arg), m.name);
        ParameterDeclList pdl = m.parameterDeclList;
        show(arg, "  ParameterDeclList [" + pdl.size() + "]");
        String pfx = ((String) arg) + "  . ";
        for (ParameterDecl pd: pdl) {
            pd.visit(this, pfx);
        }
        StatementList sl = m.statementList;
        show(arg, "  StmtList [" + sl.size() + "]");
        for (Statement s: sl) {
            s.visit(this, pfx);
        }
        if (m.returnExp != null) {
            m.returnExp.visit(this, indent(arg));
        }
        return null;
    }
    
    public Object visitParameterDecl(ParameterDecl pd, String arg){
        show(arg, pd);
        pd.type.visit(this, indent(arg));
        showQuoted(indent(arg), pd.name);
        return null;
    } 
    
    public Object visitVarDecl(VarDecl vd, String arg){
        show(arg, vd);
        vd.type.visit(this, indent(arg));
        showQuoted(indent(arg), vd.name);
        return null;
    }
 
    
  // Types
    public Object visitBaseType(BaseType type, String arg){
        show(arg, type.typeKind + " " + type.toString());
        return null;
    }
    
    public Object visitClassType(ClassType type, String arg){
        show(arg, type);
        showQuoted(indent(arg), type.className);
        return null;
    }
    
    public Object visitArrayType(ArrayType type, String arg){
        show(arg, type);
        type.eltType.visit(this, indent(arg));
        return null;
    }
    
    
    // Statements
    public Object visitBlockStmt(BlockStmt stmt, String arg){
        show(arg, stmt);
        StatementList sl = stmt.sl;
        show(arg,"  StatementList [" + sl.size() + "]");
        String pfx = arg + "  . ";
        for (Statement s: sl) {
        	s.visit(this, pfx);
        }
        return null;
    }
    
    public Object visitVardeclStmt(VarDeclStmt stmt, String arg){
        show(arg, stmt);
        stmt.varDecl.visit(this, indent(arg));	
        if (stmt.initExp != null)
            stmt.initExp.visit(this, indent(arg));
        return null;
    }
    
    public Object visitAssignStmt(AssignStmt stmt, String arg){
        show(arg,stmt);
        stmt.ref.visit(this, indent(arg));
        stmt.val.visit(this, indent(arg));
        return null;
    }
    
    public Object visitCallStmt(CallStmt stmt, String arg){
        show(arg,stmt);
        stmt.methodRef.visit(this, indent(arg));
        ExprList al = stmt.argList;
        show(arg,"  ExprList [" + al.size() + "]");
        String pfx = arg + "  . ";
        for (Expression e: al) {
            e.visit(this, pfx);
        }
        return null;
    }
    
    public Object visitIfStmt(IfStmt stmt, String arg){
        show(arg,stmt);
        stmt.cond.visit(this, indent(arg));
        stmt.thenStmt.visit(this, indent(arg));
        if (stmt.elseStmt != null)
            stmt.elseStmt.visit(this, indent(arg));
        return null;
    }
    
    public Object visitWhileStmt(WhileStmt stmt, String arg){
        show(arg, stmt);
        stmt.cond.visit(this, indent(arg));
        stmt.body.visit(this, indent(arg));
        return null;
    }
    
    
  // Expressions
    public Object visitUnaryExpr(UnaryExpr expr, String arg){
        show(arg, expr);
        expr.operator.visit(this, indent(arg));
        expr.expr.visit(this, indent(indent(arg)));
        return null;
    }
    
    public Object visitBinaryExpr(BinaryExpr expr, String arg){
        show(arg, expr);
        expr.operator.visit(this, indent(arg));
        expr.left.visit(this, indent(indent(arg)));
        expr.right.visit(this, indent(indent(arg)));
        return null;
    }
    
    public Object visitRefExpr(RefExpr expr, String arg){
        show(arg, expr);
        expr.ref.visit(this, indent(arg));
        return null;
    }
    
    public Object visitCallExpr(CallExpr expr, String arg){
        show(arg, expr);
        expr.functionRef.visit(this, indent(arg));
        ExprList al = expr.argList;
        show(arg,"  ExprList + [" + al.size() + "]");
        String pfx = arg + "  . ";
        for (Expression e: al) {
            e.visit(this, pfx);
        }
        return null;
    }
    
    public Object visitLiteralExpr(LiteralExpr expr, String arg){
        show(arg, expr);
        expr.literal.visit(this, indent(arg));
        return null;
    }
 
    public Object visitNewArrayExpr(NewArrayExpr expr, String arg){
        show(arg, expr);
        expr.eltType.visit(this, indent(arg));
        expr.sizeExpr.visit(this, indent(arg));
        return null;
    }
    
    public Object visitNewObjectExpr(NewObjectExpr expr, String arg){
        show(arg, expr);
        expr.classtype.visit(this, indent(arg));
        return null;
    }
    
   
  // References
    
    public Object visitQualifiedRef(QualifiedRef qr, String arg) {
    	show(arg, qr);
     	if (qr.thisRelative)
    			show(arg, "  this");
	    IdentifierList ql = qr.qualifierList;
	    if (ql.size() > 0)
	    	ql.get(0).visit(this, indent(arg));
	    String pfx = ((String) indent(arg)) + ".";
	    for (int i = 1; i < ql.size(); i++) {
	    	ql.get(i).visit(this, pfx);
	    }
	    return null;
    }
    
    public Object visitIndexedRef(IndexedRef ir, String arg) {
    	show(arg, ir);
    	ir.ref.visit(this, indent(arg));
    	ir.indexExpr.visit(this, indent(arg));
    	return null;
    }
    
    
  // Terminals
    public Object visitIdentifier(Identifier id, String arg){
        show(arg, "\"" + id.spelling + "\" " + id.toString());
        return null;
    }
    
    public Object visitOperator(Operator op, String arg){
        show(arg, "\"" + op.spelling + "\" " + op.toString());
        return null;
    }
    
    public Object visitIntLiteral(IntLiteral num, String arg){
        show(arg, "\"" + num.spelling + "\" " + num.toString());
        return null;
    }
    
    public Object visitBooleanLiteral(BooleanLiteral bool, String arg){
        show(arg, "\"" + bool.spelling + "\" " + bool.toString());
        return null;
    }
    
    //Last five methods aren't used, but must be implemented from Visitor interface
    public Object visitThisRef(ThisRef ref, String arg) { return null; }
    public Object visitMemberRef(MemberRef ref, String arg) { return null; }
    public Object visitLocalRef(LocalRef ref, String arg) { return null; }
    public Object visitDeRef(DeRef ref, String arg) { return null; }
    public Object visitClassRef(ClassRef ref, String arg) { return null; }
}
