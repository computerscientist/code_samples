package miniJava.ContextualAnalyzer;

import java.util.*;

import mJAM.Machine;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

import miniJava.CodeGenerator.*;

/**
 * Performs contextual analysis on an AST
 * @author Robert Dallara
 *
 */
public class TypeChecker implements Visitor<String, Object> {

	boolean mainMethodFound=false;
	boolean inStaticMethod=false;
	boolean dealingWithCallExpression=false;
	boolean staticMemberJustFound=false;
	boolean localVariableAccessed=false;
	boolean classRefAccessed=false;
	boolean visitingLeftSide=false; //Are we visiting left side of assign statement?
	
	ClassDecl currentClassDecl;
	ClassDeclList classDeclList;
	ClassDeclList stdClassDeclList; //For std environment

	IdentificationTable idTable;
	
	static boolean duplicateFound=false;
	static boolean errorFound=false;
	
	public TypeChecker()
	{
		idTable=new IdentificationTable();
		setupStandardEnvironment();
	}
	
	public void checkTree(AST ast)
	{
        ast.visit(this, "");
        
        if(errorFound)
        	System.exit(4);
    }   
	
	public void setupStandardEnvironment()
	{
		//Set up standard environment
		ParameterDeclList printlnParameterList=new ParameterDeclList();
		printlnParameterList.add(new ParameterDecl(new BaseType(TypeKind.INT, null), "n", null));

		MemberDecl printlnMemberDeclaration=new FieldDecl(false, false, new BaseType(TypeKind.VOID, null), "println", null);
		MethodDecl printlnDeclaration=new MethodDecl(printlnMemberDeclaration, printlnParameterList, null, null, null);
		printlnDeclaration.runtimeEntity=new UnknownAddress(Machine.addressSize, -1);
		
		MethodDeclList _PrintStreamMethodDeclarationList=new MethodDeclList();
		_PrintStreamMethodDeclarationList.add(printlnDeclaration);

		ClassDecl _PrintStreamDeclaration=new ClassDecl("_PrintStream", null, _PrintStreamMethodDeclarationList, null);

		FieldDeclList SystemDeclarationList=new FieldDeclList();
		FieldDecl outDeclaration=new FieldDecl(false, true, new ClassType("_PrintStream", null), "out", null);
		
		outDeclaration.runtimeEntity=new UnknownAddress(Machine.addressSize, -1);
		SystemDeclarationList.add(outDeclaration);

		ClassDecl SystemDeclaration=new ClassDecl("System", SystemDeclarationList, null, null);
		SystemDeclaration.type=new BaseType(TypeKind.CLASS, null);
		SystemDeclaration.runtimeEntity=new UnknownAddress(Machine.addressSize, -1);
		
		ClassDecl StringDeclaration=new ClassDecl("String", null, null, null);
		StringDeclaration.type=new BaseType(TypeKind.UNSUPPORTED, null);

		idTable.enter("_PrintStream", _PrintStreamDeclaration, false);
		idTable.enter("System", SystemDeclaration, false);
		idTable.enter("String", StringDeclaration, false);
		
		stdClassDeclList=new ClassDeclList();
		
		stdClassDeclList.add(_PrintStreamDeclaration);
		stdClassDeclList.add(SystemDeclaration);
		stdClassDeclList.add(StringDeclaration);
	}
	
	public Object visitPackage(Package prog, String arg)
	{
		classDeclList=prog.classDeclList;
		
		//Put all classes into identification table before doing anything
		for(ClassDecl c: classDeclList)
			idTable.enter(c.name, c, false);
		
		for(ClassDecl c: prog.classDeclList)
		{
			idTable.openScope();
			
			currentClassDecl=new ClassDecl(c.name, c.fieldDeclList, c.methodDeclList, c.posn);
			currentClassDecl.type=new ClassType(c.name, c.posn);
			
			c.visit(this, arg);
			
			idTable.closeScope();
		}
		
		if(!mainMethodFound)
		{
			errorFound=true;
			System.out.println("***Error: no main method found!");
		}
		
		return null;
	}
	
	public Object visitClassDecl(ClassDecl cd, String arg)
	{
		//Visit global variable field declarations first since global variables can be used before they are declared
		for(FieldDecl fd: cd.fieldDeclList)
			fd.visit(this, arg);
		
		//Put all methods of current class into identification table before visiting each of them
		for(MethodDecl md: cd.methodDeclList)
	    	idTable.enter(md.name, md, false);
		
		for(MethodDecl md: cd.methodDeclList)
		{
			md.visit(this, arg);
			
			//See if we have found the main method
			if(!md.isPrivate && md.isStatic && md.type.typeKind==TypeKind.VOID && md.name.equals("main") && md.parameterDeclList.size()==1 && md.parameterDeclList.get(0).type.typeKind==TypeKind.ARRAY && ((ArrayType)(md.parameterDeclList.get(0).type)).eltType.typeKind==TypeKind.UNSUPPORTED)
			{
				if(!mainMethodFound)
					mainMethodFound=true;
				
				else
				{
					errorFound=true;
					System.out.println("***Error: duplicate main method!");
				}
			}
		}
		
		return null;
	}
	
    public Object visitFieldDecl(FieldDecl fd, String arg)
    {
    	Type type=(Type) fd.type.visit(this, arg);
    	
    	if(type.typeKind==TypeKind.ERROR)
    	{
    		errorFound=true;
    		System.out.println("***Error: Bad field declaration type!");
    		return type;
    	}
    	
    	else if(type.typeKind==TypeKind.VOID)
    	{
    		errorFound=true;
    		System.out.println("***Error: can't have void field declarations!");
    		return new BaseType(TypeKind.ERROR, fd.type.posn);
    	}
    	
    	String id=fd.name;
    	idTable.enter(id, fd, false);
    	
    	return type;
    }
    
    public Object visitMethodDecl(MethodDecl md, String arg)
    {
    	if(md.isStatic)
    		inStaticMethod=true;
    	
    	else
    		inStaticMethod=false;
    	
    	Type returnType=md.type;
    	
    	if((returnType instanceof ClassType) && ((ClassType) returnType).className.equals("String"))
    		returnType.typeKind=TypeKind.UNSUPPORTED;
    	
    	if(returnType.typeKind==TypeKind.ERROR)
    	{
    		errorFound=true;
    		System.out.println("***Error: Bad return type for method "+md.name);
    		return new BaseType(TypeKind.ERROR, md.posn);
    	}
    	
    	idTable.openScope();
    	
    	for(ParameterDecl pd: md.parameterDeclList)
    		pd.visit(this, arg);
    	
    	idTable.openScope();
    	
    	for(Statement s: md.statementList)
    		s.visit(this, arg);
    	
    	if(md.returnExp!=null)
    	{
    		if(returnType.typeKind==TypeKind.VOID)
    		{
    			errorFound=true;
    			System.out.println("***Error: Wrong return type for "+md.name+" method!");
    		}
    		
    		else
    		{
	    		Type returnedType=(Type) md.returnExp.visit(this, arg);
	    		
	    		if(!typesEqual(returnedType, returnType) || returnType.typeKind==TypeKind.ERROR)
	    		{
	    			errorFound=true;
	    			System.out.println("***Error: Wrong return type for "+md.name+" method!");
	    		}
    		}
    	}
    	
    	else if(md.returnExp==null && returnType.typeKind!=TypeKind.VOID)
    	{
    		errorFound=true;
    		System.out.println("***Error: Wrong return type for "+md.name+" method!");
    	}
    	
    	idTable.closeScope();
    	idTable.closeScope();
    	
    	return null;
    }
    
    public Object visitParameterDecl(ParameterDecl pd, String arg)
    {
    	if(pd.type.typeKind==TypeKind.ERROR || pd.type.typeKind==TypeKind.VOID)
    	{
    		errorFound=true;
    		System.out.println("***Error: Bad parameter type!");
    		return new BaseType(TypeKind.ERROR, pd.posn);
    	}
    	
    	idTable.enter(pd.name, pd, false);
    	pd.type.visit(this, arg);

    	return null;
    }
    
    public Object visitVarDecl(VarDecl decl, String arg)
    {
    	duplicateFound=false;
    	
    	idTable.enter(decl.name, decl, true);
    	return decl.type.visit(this, arg);
    }

    public Object visitBaseType(BaseType type, String arg)
    {
    	return type;
    }
    
    public Object visitClassType(ClassType type, String arg)
    {
    	Object classDeclObject=idTable.retrieveClass(type.className);
    	
    	if(classDeclObject==null)
    	{
    		System.out.println("***Error: class "+type.className+" not declared!");
    		System.exit(4);
    	}
    	
    	type.classDecl=(ClassDecl) classDeclObject;
    	
    	//Special case for String class as well as errors
    	if(type.classDecl.type!=null && (type.classDecl.type.typeKind==TypeKind.UNSUPPORTED || type.classDecl.type.typeKind==TypeKind.ERROR))
    		type.typeKind=type.classDecl.type.typeKind;
    	
    	return type;
    }
    
    public Object visitArrayType(ArrayType arrayType, String arg)
    {
    	arrayType.eltType.visit(this, arg);
    	return arrayType;
    }
    
    public Object visitBlockStmt(BlockStmt stmt, String arg)
    {
    	idTable.openScope();
    	
    	for(Statement s: stmt.sl)
    		s.visit(this, arg);
    	
    	idTable.closeScope();
    	
    	return null;
    }
    
    public Object visitVardeclStmt(VarDeclStmt stmt, String arg)
    {
    	Type varDeclKind=(Type) stmt.varDecl.visit(this, arg);
    	
    	//If duplicate of variable being declared existed, return
    	if(duplicateFound)
    		return null;
    	
    	staticMemberJustFound=false;
    	Type exprKind=(Type) stmt.initExp.visit(this, arg);
    	
    	//Now that variable has been declared (& the right side has been evaluated), it should be made usable
    	idTable.lastEntry.isNull=false;
    	
    	if(!typesEqual(varDeclKind, exprKind))
    	{
    		errorFound=true;
    		System.out.println("***Error: type mismatch in variable declaration!");
    	}
    	
    	else if(exprKind.typeKind==TypeKind.VOID)
    	{
    		errorFound=true;
    		System.out.println("***Error: cannot declare void variables!");
    	}
    	
    	return null;
    }
    
    public Object visitAssignStmt(AssignStmt stmt, String arg)
    {
    	staticMemberJustFound=false;
    	
    	visitingLeftSide=true;
    	Object stmtRefObj=stmt.ref.visit(this, arg);
    	visitingLeftSide=false;
    	
    	if((stmtRefObj instanceof Type) && ((Type) stmtRefObj).typeKind==TypeKind.ERROR)
    		return stmtRefObj;
    	
    	Type refType=null;
    	
    	if(stmtRefObj instanceof DeRef)
    	{
	    	DeRef stmtRef=(DeRef) stmtRefObj;
	    	
	    	if(stmtRef.ref!=null && (stmtRef.ref instanceof ThisRef) && stmtRef.id==null)
	    	{
	    		errorFound=true;
	    		System.out.println("***Error: Can't assign anything to \"this\" construct!");
	    		return new BaseType(TypeKind.ERROR, stmt.ref.posn);
	    	}
	    	
	    	visitingLeftSide=true;
	    	refType=(Type) stmtRef.visit(this, arg);
	    	visitingLeftSide=false;
	    	
	    	if(staticMemberJustFound)
	    	{
		    	errorFound=true;
	    		System.out.println("***Error: PA3 no static access");
	    		return new BaseType(TypeKind.ERROR, stmt.ref.posn);
	    	}
	    	
	    	else if(!staticMemberJustFound && inStaticMethod && !localVariableAccessed && !classRefAccessed)
	    	{
	    		errorFound=true;
	    		System.out.println("***Error: Cannot access non-static field in static method!");
	    		return new BaseType(TypeKind.ERROR, stmt.ref.posn);
	    	}
	    	
	    	stmt.ref=stmtRef; //Replace reference subtree with DeRef AST
    	}
    	
    	else
    		refType=(Type) stmtRefObj;
    	
    	staticMemberJustFound=false;
    	Type assignType=(Type) stmt.val.visit(this, arg);
    	
    	if(!typesEqual(refType, assignType))
    	{
    		errorFound=true;
	    	System.out.println("***Error: type mismatch in assign statement!");
    	}
    	
    	return null;
    }
    
    public Object visitCallStmt(CallStmt stmt, String arg)
    {
    	staticMemberJustFound=false;
    	dealingWithCallExpression=true;
    	
    	Object methodRefObj=stmt.methodRef.visit(this, arg);
    	
    	if(!(methodRefObj instanceof DeRef))
    		return methodRefObj; //Error case
    	
    	DeRef methodRef=(DeRef) methodRefObj;
    	Type returnResult=(Type) methodRef.visit(this, arg);
    	
    	MethodDecl callStmtMethodDecl=null;
    	
    	if(returnResult.typeKind==TypeKind.ERROR)
    		return returnResult;
    	
    	else if(staticMemberJustFound)
    	{
    		errorFound=true;
    		System.out.println("***Error: PA3 no static access");
    		return new BaseType(TypeKind.ERROR, methodRef.posn);
    	}
    	
    	else if(!staticMemberJustFound && inStaticMethod && !localVariableAccessed && !classRefAccessed)
    	{
    		errorFound=true;
    		System.out.println("***Error: Cannot access non-static field in static method!");
    		return new BaseType(TypeKind.ERROR, methodRef.posn);
    	}
    	
    	else if(!(methodRef.ref instanceof DeRef) && methodRef.id==null)
    	{
    		//Special case for "this()" case, which is an error
    		if(methodRef.ref instanceof ThisRef)
    		{
    			errorFound=true;
    			System.out.println("***Error: Cannot invoke this()");
    			return new BaseType(TypeKind.ERROR, methodRef.posn);
    		}
    		
    		callStmtMethodDecl=((MethodDecl)((MemberRef) methodRef.ref).classIdentifier.decl);
    	}
    	
    	else
    	{
    		if(methodRef.id!=null && methodRef.id.decl!=null && (methodRef.id.decl instanceof FieldDecl))
    		{
    			errorFound=true;
    			System.out.println("***Error: Dealing with call expression, but field declaration found!");
				return new BaseType(TypeKind.ERROR, methodRef.id.decl.posn);
    		}
    		
    		callStmtMethodDecl=(MethodDecl) methodRef.id.decl;
    	}
    	
    	//Replace methodRef AST of call statement with that of the DeRef
    	stmt.methodRef=methodRef;
    	
    	//No longer dealing with actual call, now dealing with parameters
    	dealingWithCallExpression=false;
    	
    	if(stmt.argList.size()!=callStmtMethodDecl.parameterDeclList.size())
    	{
    		errorFound=true;
    		System.out.println("***Error: Wrong number of parameters in call statement!");
    		return new BaseType(TypeKind.ERROR, stmt.posn);
    	}
    	
    	for(int i=0;i<stmt.argList.size();i++)
    	{
    		Type currentParameterType=(Type) stmt.argList.get(i).visit(this, arg);
    		
    		if(!typesEqual(currentParameterType, callStmtMethodDecl.parameterDeclList.get(i).type))
    		{
    			errorFound=true;
    			System.out.println("***Error: Parameter type mismatch in call statement!");
    			return new BaseType(TypeKind.ERROR, stmt.argList.get(i).posn);
    		}
    	}
    	
    	return returnResult;
    }
    
    public Object visitIfStmt(IfStmt stmt, String arg)
    {
    	Type conditionalExpressionType=(Type) stmt.cond.visit(this, arg);
    	
    	if(conditionalExpressionType.typeKind!=TypeKind.BOOLEAN)
    	{
    		errorFound=true;
    		System.out.println("***Error: boolean type expected of if statement condition expression!");
    	}
    	
    	if(stmt.thenStmt instanceof VarDeclStmt)
    	{
    		errorFound=true;
    		System.out.println("***Error: Variable declaration statement cannot be solitary statement in branch of conditional statement!");
    		return new BaseType(TypeKind.ERROR, stmt.thenStmt.posn);
    	}
    	
    	if(!(stmt.thenStmt instanceof BlockStmt))
    		idTable.openScope();
    	
    	stmt.thenStmt.visit(this, arg);
    	
    	if(!(stmt.thenStmt instanceof BlockStmt))
    		idTable.closeScope();
    	
    	if(stmt.elseStmt!=null)
    	{
    		if(stmt.elseStmt instanceof VarDeclStmt)
        	{
    			errorFound=true;
        		System.out.println("***Error: Variable declaration statement cannot be solitary statement in branch of conditional statement!");
        		return new BaseType(TypeKind.ERROR, stmt.elseStmt.posn);
        	}
    		
    		if(!(stmt.elseStmt instanceof BlockStmt))
    			idTable.openScope();
    		
    		stmt.elseStmt.visit(this, arg);
    		
    		if(!(stmt.elseStmt instanceof BlockStmt))
    			idTable.closeScope();
    	}
    	
    	return null;
    }
    
    public Object visitWhileStmt(WhileStmt stmt, String arg)
    {
    	Type conditionalExpressionType=(Type) stmt.cond.visit(this, arg);
    	
    	if(conditionalExpressionType.typeKind!=TypeKind.BOOLEAN)
    	{
    		errorFound=true;
    		System.out.println("***Error: boolean type expected of while statement condition expression!");
    	}
    	
    	if(stmt.body instanceof VarDeclStmt)
    	{
    		errorFound=true;
    		System.out.println("***Error: Variable declaration statement cannot be solitary statement in while loop!");
    		return new BaseType(TypeKind.ERROR, stmt.body.posn);
    	}
    	
    	if(!(stmt.body instanceof BlockStmt))
    		idTable.openScope();
    	
    	stmt.body.visit(this, arg);
    	
    	if(!(stmt.body instanceof BlockStmt))
    		idTable.closeScope();
    	
    	return null;
    }
    
    public Object visitUnaryExpr(UnaryExpr unaryExpr, String arg)
    {
    	Type expressionKind=(Type) unaryExpr.expr.visit(this, arg);
    	String operatorSpelling=(String) unaryExpr.operator.visit(this, arg);

    	if(operatorSpelling.equals("-") && expressionKind.typeKind==TypeKind.INT)
    		return new BaseType(TypeKind.INT, unaryExpr.posn);
    	
    	else if(operatorSpelling.equals("!") && expressionKind.typeKind==TypeKind.BOOLEAN)
    		return new BaseType(TypeKind.BOOLEAN, unaryExpr.posn);

    	//Default case, if none of tests pass
    	errorFound=true;
    	System.out.println("***Error: Invalid unary expression!");
    	return new BaseType(TypeKind.ERROR, unaryExpr.posn);
    }
    
    public Object visitBinaryExpr(BinaryExpr expr, String arg)
    {
    	Type leftSideKind=(Type) expr.left.visit(this, arg);
    	Type rightSideKind=(Type) expr.right.visit(this, arg);
    	
    	String operatorSpelling=(String) expr.operator.visit(this, arg);
    	
    	if(operatorSpelling.equals("||") || operatorSpelling.equals("&&"))
    	{
    		if(typesEqual(leftSideKind, rightSideKind) && leftSideKind.typeKind==TypeKind.BOOLEAN)
    			return new BaseType(TypeKind.BOOLEAN, expr.posn);
    	}
    	
    	else if(operatorSpelling.equals("==") || operatorSpelling.equals("!="))
    	{
    		if(typesEqual(leftSideKind, rightSideKind) && (leftSideKind.typeKind==TypeKind.BOOLEAN || leftSideKind.typeKind==TypeKind.INT))
    			return new BaseType(TypeKind.BOOLEAN, expr.posn);
    	}
    	
    	else if(operatorSpelling.equals("<=") || operatorSpelling.equals("<") || operatorSpelling.equals(">") || operatorSpelling.equals(">="))
    	{
    		if(typesEqual(leftSideKind, rightSideKind) && leftSideKind.typeKind==TypeKind.INT)
    			return new BaseType(TypeKind.BOOLEAN, expr.posn);
    	}
    	
    	else if(operatorSpelling.equals("+") || operatorSpelling.equals("-") || operatorSpelling.equals("*") || operatorSpelling.equals("/"))
    	{
    		if(typesEqual(leftSideKind, rightSideKind) && leftSideKind.typeKind==TypeKind.INT)
    			return new BaseType(TypeKind.INT, expr.posn);
    	}
    	
    	//Default case, if none of the test cases pass
    	errorFound=true;
    	System.out.println("***Error: Invalid binary expression!");
    	return new BaseType(TypeKind.ERROR, expr.posn);
    }
    
    public Object visitRefExpr(RefExpr expr, String arg)
    {
    	staticMemberJustFound=false;
    	Object deRefObj=expr.ref.visit(this, arg);
    	
    	if(deRefObj instanceof Type)
    		return deRefObj;
    	
    	DeRef deRef=(DeRef) deRefObj;
    	Type resultingType=(Type) deRef.visit(this, arg);
    	
    	//Are we using a static member?
    	if(staticMemberJustFound)
    	{
    		errorFound=true;
    		System.out.println("***Error: PA3 no static access");
    		return new BaseType(TypeKind.ERROR, expr.posn);
    	}
    	
    	else if(deRef.id!=null && deRef.id.decl!=null && (deRef.id.decl instanceof MethodDecl) && !dealingWithCallExpression)
		{
    		errorFound=true;
			System.out.println("***Error: Not dealing with call expression, but method declaration found!");
			return new BaseType(TypeKind.ERROR, deRef.id.decl.posn);
		}
    	
    	//Change AST of expression's reference to be that of the DeRef
    	expr.ref=deRef;
    	
    	return resultingType;
    }
    
    public Object visitCallExpr(CallExpr expr, String arg)
    {
    	dealingWithCallExpression=true;
    	Object methodRefObj=expr.functionRef.visit(this, arg);
    	
    	if(!(methodRefObj instanceof DeRef))
    		return methodRefObj; //Error case
    	
    	DeRef methodRef=(DeRef) methodRefObj;
    	Type returnResult=(Type) methodRef.visit(this, arg);
    	
    	dealingWithCallExpression=false;
    	
    	MethodDecl callStmtMethodDecl=null;
    	
    	//Method invocation by itself, without any other qualifiers before it
    	if(returnResult.typeKind==TypeKind.ERROR)
    		return returnResult;
    	
    	else if(!(methodRef.ref instanceof DeRef) && methodRef.id==null)
    	{
    		//Special case for "this()" case, which is an error
    		if(methodRef.ref instanceof ThisRef)
    		{
    			errorFound=true;
    			System.out.println("***Error: Cannot invoke this()");
    			return new BaseType(TypeKind.ERROR, expr.functionRef.posn);
    		}
    		
    		callStmtMethodDecl=((MethodDecl)((MemberRef) methodRef.ref).classIdentifier.decl);
    	}
    	
    	else
    	{
    		if(methodRef.id!=null && methodRef.id.decl!=null && (methodRef.id.decl instanceof FieldDecl))
    		{
    			errorFound=true;
    			System.out.println("***Error: Dealing with call expression, but field declaration found!");
				return new BaseType(TypeKind.ERROR, methodRef.id.decl.posn);
    		}
    		
    		callStmtMethodDecl=(MethodDecl) methodRef.id.decl;
    	}
    	
    	if(callStmtMethodDecl.isStatic)
    	{
    		errorFound=true;
    		System.out.println("***Error: PA3 no static access");
    		return new BaseType(TypeKind.ERROR, expr.posn);
    	}
    	
    	else if(!callStmtMethodDecl.isStatic && inStaticMethod && !localVariableAccessed && !classRefAccessed)
    	{
    		errorFound=true;
    		System.out.println("***Error: Cannot access non-static field in static method!");
    		return new BaseType(TypeKind.ERROR, expr.posn);
    	}
    	
    	else if(expr.argList.size()!=callStmtMethodDecl.parameterDeclList.size())
    	{
    		errorFound=true;
    		System.out.println("***Error: Wrong number of parameters in call statement!");
    		return new BaseType(TypeKind.ERROR, expr.posn);
    	}
    	
    	for(int i=0;i<expr.argList.size();i++)
    	{
    		Type currentParameterType=(Type) expr.argList.get(i).visit(this, arg);
    		
    		if(!typesEqual(currentParameterType, callStmtMethodDecl.parameterDeclList.get(i).type))
    		{
    			errorFound=true;
    			System.out.println("***Error: Parameter type mismatch in call statement!");
    			return new BaseType(TypeKind.ERROR, expr.argList.get(i).posn);
    		}
    	}
    	
    	//Change AST of call expression's function reference to be that of the DeRef
    	expr.functionRef=methodRef;
    	
    	return returnResult;
    }
    
    public Object visitLiteralExpr(LiteralExpr expr, String arg)
    {
    	return expr.literal.visit(this, arg);
    }
    
    public Object visitNewObjectExpr(NewObjectExpr expr, String arg)
    {
    	return expr.classtype.visit(this, arg);
    }
    
    public Object visitNewArrayExpr(NewArrayExpr expr, String arg)
    {
    	Type elementType=(Type) expr.eltType.visit(this, arg);
    	Type sizeExpressionType=(Type) expr.sizeExpr.visit(this, arg);
    	
    	if(sizeExpressionType.typeKind!=TypeKind.INT)
    	{
    		errorFound=true;
    		System.out.println("***Error: Array size expression must be integer!");
    		return new BaseType(TypeKind.ERROR, expr.sizeExpr.posn);
    	}
    	
    	else if(elementType.typeKind!=TypeKind.INT && elementType.typeKind!=TypeKind.CLASS)
    	{
    		errorFound=true;
    		System.out.println("***Error: Array element type must be INT or CLASS!");
    		return new BaseType(TypeKind.ERROR, expr.eltType.posn);
    	}
    	
    	else
    		return new ArrayType(elementType, expr.posn);
    }
    
    public Object visitThisRef(ThisRef thisRef, String arg)
    {
    	if(thisRef.qualifierList.size()==0)
    		return new DeRef(thisRef, null, thisRef.posn);
    	
    	else
    	{
    		Iterator<Identifier> qlIterator=thisRef.qualifierList.iterator();
    		Identifier nextIdentifier=qlIterator.next();
    		
    		DeRef currentDeRef=new DeRef(thisRef, nextIdentifier, nextIdentifier.posn);
    		
    		while(qlIterator.hasNext())
    		{
    			nextIdentifier=qlIterator.next();
    			currentDeRef=new DeRef(currentDeRef, nextIdentifier, nextIdentifier.posn);
    		}
    		
    		return currentDeRef;
    	}
    }
    
    public Object visitMemberRef(MemberRef memberRef, String arg)
    {
    	Iterator<Identifier> qlIterator=memberRef.qualifierList.iterator();
    	DeRef currentDeRef=null;
    	
    	if(!qlIterator.hasNext())
    		currentDeRef=new DeRef(memberRef, null, memberRef.posn);

    	else
    	{
    		Identifier nextIdentifier=qlIterator.next();
    		currentDeRef=new DeRef(memberRef, nextIdentifier, nextIdentifier.posn);

    		while(qlIterator.hasNext())
    		{
    			nextIdentifier=qlIterator.next();
    			currentDeRef=new DeRef(currentDeRef, nextIdentifier, nextIdentifier.posn);
    		}
    	}
    	
    	return currentDeRef;
    }
    
    public Object visitLocalRef(LocalRef localRef, String arg)
    {
    	Iterator<Identifier> qlIterator=localRef.qualifierList.iterator();
    	DeRef currentDeRef=null;
    	
    	if(!qlIterator.hasNext())
    		currentDeRef=new DeRef(localRef, null, localRef.posn);

    	else
    	{
    		Identifier nextIdentifier=qlIterator.next();
    		currentDeRef=new DeRef(localRef, nextIdentifier, nextIdentifier.posn);

    		while(qlIterator.hasNext())
    		{
    			nextIdentifier=qlIterator.next();
    			currentDeRef=new DeRef(currentDeRef, nextIdentifier, nextIdentifier.posn);
    		}
    	}
    	
    	return currentDeRef;
    }
    
    public Object visitClassRef(ClassRef classRef, String arg)
    {
    	//Can't have class name by itself
    	if(classRef.qualifierList.size()==0)
    	{
    		errorFound=true;
    		return new BaseType(TypeKind.ERROR, classRef.posn);
    	}
    	
    	Iterator<Identifier> qlIterator=classRef.qualifierList.iterator();
    	Identifier nextIdentifier=qlIterator.next();
    	
    	DeRef currentDeRef=new DeRef(classRef, nextIdentifier, nextIdentifier.posn);

    	while(qlIterator.hasNext())
    	{
    		nextIdentifier=qlIterator.next();
    		currentDeRef=new DeRef(currentDeRef, nextIdentifier, nextIdentifier.posn);
    	}
    	
    	return currentDeRef;
    }
    
    public Object visitDeRef(DeRef deRef, String arg)
    {
    	localVariableAccessed=false;
    	classRefAccessed=false;
    	
    	if(!(deRef.ref instanceof DeRef))
    	{
    		Declaration lowerRefDecl=null;
    		
    		if(deRef.ref instanceof ClassRef)
    		{
    			lowerRefDecl=((ClassRef) deRef.ref).classIdentifier.decl;
    			classRefAccessed=true;
    			
    			if(deRef.id==null)
    			{
    				errorFound=true;
    				staticMemberJustFound=false;
    				
    				System.out.println("***Error: ClassRef cannot stand alone!");
    				return new BaseType(TypeKind.ERROR, lowerRefDecl.posn);
    			}
    			
    			Declaration retrievedMemberDeclaration=retrieveMember((ClassDecl) lowerRefDecl, deRef.id.spelling);
    			Declaration retrievedMember=null;
    			
    			if(retrievedMemberDeclaration instanceof FieldDecl)
    				retrievedMember=(FieldDecl) retrievedMemberDeclaration;
    			
    			else
    				retrievedMember=(MethodDecl) retrievedMemberDeclaration;
    			
    			if(retrievedMember==null)
    			{
    				System.out.println("***Error: Member being retrieved doesn't exist!");
        			System.exit(4);
    			}
    			
    			else if(!((MemberDecl) retrievedMember).isStatic || ((MemberDecl) retrievedMember).isPrivate && !((ClassDecl) lowerRefDecl).name.equals(currentClassDecl.name))
    			{
    				errorFound=true;
    				staticMemberJustFound=false;
    				
    				System.out.println("***Error: "+(((MemberDecl) retrievedMember).isPrivate ? "Member being retrieved is private!" : "Member being retrieved is not static!"));
    				return new BaseType(TypeKind.ERROR, retrievedMember.posn);
    			}
    			
    			staticMemberJustFound=true; //Otherwise, error case above would have been reached
    			deRef.id.decl=retrievedMember;
    			
    			//This will make all ClassRefs (other than for System.out.println) errors, but this is necessary for PA4
    			if(staticMemberJustFound && !(retrievedMember.runtimeEntity instanceof UnknownAddress))
    			{
    				errorFound=true;
    	    		System.out.println("***Error: PA3 no static access");
    	    		return new BaseType(TypeKind.ERROR, deRef.ref.posn);
    			}
    			
    			return retrievedMember.type;
    		}
    		
    		else if(deRef.ref instanceof ThisRef)
    		{
    			if(inStaticMethod)
    			{
    				errorFound=true;
    				staticMemberJustFound=false;
    				
    				System.out.println("***Error: Cannot use \"this\" in static context!");
    				return new BaseType(TypeKind.ERROR, deRef.ref.posn);
    			}
    			
    			lowerRefDecl=((ThisRef) deRef.ref).classIdentifier.decl;
    			
    			if(deRef.id!=null)
    			{
	    			Declaration retrievedMemberDeclaration=retrieveMember(currentClassDecl, deRef.id.spelling);
	    			Declaration retrievedMember=null;
	    			
    				if(retrievedMemberDeclaration instanceof FieldDecl)
	    				retrievedMember=(FieldDecl) retrievedMemberDeclaration;
	    			
	    			else
	    				retrievedMember=(MethodDecl) retrievedMemberDeclaration;
    				
	    			//Does referred member even exist?
	    			if(retrievedMember==null)
	    			{
	    				System.out.println("***Error: Member being retrieved doesn't exist!");
	    				System.exit(4);
	    			}
	    			
	    			staticMemberJustFound=((MemberDecl) retrievedMember).isStatic;
	    			deRef.id.decl=retrievedMember;
	    			
	    			if(staticMemberJustFound)
	    			{
	    				errorFound=true;
	    	    		System.out.println("***Error: PA3 no static access");
	    	    		return new BaseType(TypeKind.ERROR, deRef.ref.posn);
	    			}
	    			
	        		return retrievedMember.type;
    			}
    			
    			else
    			{
    				staticMemberJustFound=false;
    				return new ClassType(lowerRefDecl.name, lowerRefDecl.posn);
    			}
    		}
    		
    		else if(deRef.ref instanceof MemberRef)
    		{
    			lowerRefDecl=((MemberRef) deRef.ref).classIdentifier.decl;
    			
    			if(!((MemberDecl) lowerRefDecl).isStatic && inStaticMethod)
    			{
    				errorFound=true;
    				staticMemberJustFound=false;
    				
    	    		System.out.println("***Error: Cannot access non-static field in static method!");
    	    		return new BaseType(TypeKind.ERROR, null);
    			}
    			
    			if(deRef.id==null)
    			{
    				staticMemberJustFound=((MemberDecl) lowerRefDecl).isStatic;
    				
    				if(staticMemberJustFound)
    				{
    					errorFound=true;
    		    		System.out.println("***Error: PA3 no static access");
    		    		return new BaseType(TypeKind.ERROR, deRef.ref.posn);
    				}
    				
    				return lowerRefDecl.type;
    			}
    			
    			if(lowerRefDecl.type instanceof ClassType)
    			{
	    			ClassDecl cd=retrieveClassDecl(((ClassType) lowerRefDecl.type).className);
	    			
	    			Declaration retrievedMemberDeclaration=retrieveMember(cd, deRef.id.spelling);
	    			Declaration retrievedMember=null;
	    			
	    			if(retrievedMemberDeclaration instanceof FieldDecl)
	    				retrievedMember=(FieldDecl) retrievedMemberDeclaration;
	    			
	    			else
	    				retrievedMember=(MethodDecl) retrievedMemberDeclaration;
	    			
	    			if(retrievedMember==null)
	    			{
	    				System.out.println("***Error: Member being retrieved in memberRef doesn't exist!");
	    				System.exit(4);
	    			}
	    			
	    			else if(((MemberDecl) retrievedMember).isPrivate && !typesEqual(((MemberDecl) retrievedMember).type, currentClassDecl.type))
	    			{
	    				errorFound=true;
	    				staticMemberJustFound=false;
	    				
	    				System.out.println("***Error: "+"Member being retrieved is private!");
	    				return new BaseType(TypeKind.ERROR, retrievedMember.posn);
	    			}
	    			
	    			else if(!((MemberDecl) retrievedMember).isStatic && inStaticMethod)
	    			{
	    				errorFound=true;
	    				staticMemberJustFound=false;
	    				
	    	    		System.out.println("***Error: Cannot access non-static field in static method!");
	    	    		return new BaseType(TypeKind.ERROR, retrievedMember.posn);
	    			}
	    			
	    			staticMemberJustFound=((MemberDecl) retrievedMember).isStatic;
	    			deRef.id.decl=retrievedMember;
	    			
	    			if(staticMemberJustFound)
	    			{
	    				errorFound=true;
    		    		System.out.println("***Error: PA3 no static access");
    		    		return new BaseType(TypeKind.ERROR, deRef.ref.posn);
	    			}
	    			
	        		return retrievedMember.type;
    			}
    			
    			else
    			{
    				boolean dealingWithLengthAttribute=false;
    				
    				if(lowerRefDecl.type.typeKind!=TypeKind.CLASS && deRef.id!=null)
    				{
    					//Special case for "length" feature of arrays
    					if(lowerRefDecl.type.typeKind==TypeKind.ARRAY && deRef.id.spelling.equals("length") && !visitingLeftSide)
    						dealingWithLengthAttribute=true;
    					
    					else
    					{
	    					errorFound=true;
	    					staticMemberJustFound=false;
	    					
	    					//Trying to assign to "length" attribute of arrays?
	    					if(lowerRefDecl.type.typeKind==TypeKind.ARRAY && deRef.id.spelling.equals("length"))
	    						System.out.println("***Error: Can't assign to length attribute of arrays!");
	    					
	    					else
	    						System.out.println("***Error: Primitives can't have .'s after them!");
	    					
	    	    			return new BaseType(TypeKind.ERROR, lowerRefDecl.posn);
    					}
    				}
    				
    				else if(!((MemberDecl) lowerRefDecl).isStatic && inStaticMethod)
    				{
    					errorFound=true;
    					staticMemberJustFound=false;
    					
	    	    		System.out.println("***Error: Cannot access non-static field in static method!");
	    	    		return new BaseType(TypeKind.ERROR, lowerRefDecl.posn);
    				}
    				
    				staticMemberJustFound=((MemberDecl) lowerRefDecl).isStatic;
    				
    				if(staticMemberJustFound)
    				{
    					errorFound=true;
    		    		System.out.println("***Error: PA3 no static access");
    		    		return new BaseType(TypeKind.ERROR, deRef.ref.posn);
    				}
    				
    				return dealingWithLengthAttribute ? new BaseType(TypeKind.INT, lowerRefDecl.posn) : lowerRefDecl.type;
    			}
    		}
    		
    		else if(deRef.ref instanceof LocalRef)
    		{
    			localVariableAccessed=true;
    			
    			Declaration localDeclaration=(Declaration) idTable.retrieveVariable(((LocalRef) deRef.ref).id.spelling);
    			Declaration localDecl=null;
    			
    			if(localDeclaration instanceof ParameterDecl)
    				localDecl=(ParameterDecl) localDeclaration;

    			else
    				localDecl=(VarDecl) localDeclaration;
    			
    			if(deRef.id==null)
    			{
    				staticMemberJustFound=false;
    				return localDecl.type;
    			}
    			
    			if(localDecl.type instanceof ClassType)
    			{
	    			ClassDecl cd=retrieveClassDecl(((ClassType) localDecl.type).className);
	    			
	    			Declaration retrievedMemberDeclaration=retrieveMember(cd, deRef.id.spelling);
	    			Declaration retrievedMember=null;
	    			
	    			if(retrievedMemberDeclaration instanceof FieldDecl)
	    				retrievedMember=(FieldDecl) retrievedMemberDeclaration;
	    			
	    			else
	    				retrievedMember=(MethodDecl) retrievedMemberDeclaration;
	    			
	    			if(retrievedMember==null)
	    			{
	    				System.out.println("***Error: Member being retrieved doesn't exist!");
	        			System.exit(4);
	    			}
	    			
	    			else if(((MemberDecl) retrievedMember).isPrivate && !typesEqual(((MemberDecl) retrievedMember).type, currentClassDecl.type))
	    			{
	    				errorFound=true;
	    				staticMemberJustFound=false;
	    				
	    				System.out.println("***Error: "+(((MemberDecl) retrievedMember).isPrivate ? "Member being retrieved is private!" : "Member being retrieved is not static!"));
	    				return new BaseType(TypeKind.ERROR, retrievedMember.posn);
	    			}
	    			
	    			staticMemberJustFound=((MemberDecl) retrievedMember).isStatic;
	    			deRef.id.decl=retrievedMember;
	    			
	    			if(staticMemberJustFound)
	    			{
	    				errorFound=true;
    		    		System.out.println("***Error: PA3 no static access");
    		    		return new BaseType(TypeKind.ERROR, deRef.ref.posn);
	    			}
	    			
	        		return retrievedMember.type;
    			}
    			
    			else
    			{
    				boolean dealingWithLengthAttribute=false;
    				
    				if(localDecl.type.typeKind!=TypeKind.CLASS && deRef.id!=null)
    				{
    					//Special case for "length" feature of arrays
    					if(localDecl.type.typeKind==TypeKind.ARRAY && deRef.id.spelling.equals("length") && !visitingLeftSide)
    						dealingWithLengthAttribute=true;
    					
    					else
    					{
	    					errorFound=true;
	    					staticMemberJustFound=false;
	    					
	    					if(localDecl.type.typeKind==TypeKind.ARRAY && deRef.id.spelling.equals("length"))
	    						System.out.println("***Error: Can't assign to length attribute of arrays!");
	    					
	    					else
	    						System.out.println("***Error: Primitives can't have .'s after them!");
	    					
	    	    			return new BaseType(TypeKind.ERROR, localDecl.posn);
    					}
    				}
    				
    				staticMemberJustFound=false;
    				return dealingWithLengthAttribute ? new BaseType(TypeKind.INT, localDecl.posn) : localDecl.type;
    			}
    		}
    		
    		else
    		{
    			errorFound=true;
    			staticMemberJustFound=false;
    			
    			return new BaseType(TypeKind.ERROR, deRef.posn);
    		}
    	}
    	
    	else
    	{
    		Type incomingType=(Type) deRef.ref.visit(this, arg);
    		
    		if(incomingType.typeKind==TypeKind.ERROR || incomingType.typeKind==TypeKind.UNSUPPORTED)
    			return incomingType;
    		
    		if(((DeRef) deRef.ref).id!=null && ((DeRef) deRef.ref).id.decl!=null && ((DeRef) deRef.ref).id.decl instanceof MethodDecl)
    		{
    			errorFound=true;
    			staticMemberJustFound=false;
    			
    			System.out.println("***Error: Invalid use of method in reference!");
    			return new BaseType(TypeKind.ERROR, ((DeRef) deRef.ref).id.decl.posn);	
    		}
    		
    		//If currently returning primitive, must be more "."'s in front of it
    		if(incomingType.typeKind!=TypeKind.CLASS)
    		{
    			//Special case for "length" feature of arrays
    			if(incomingType.typeKind==TypeKind.ARRAY && deRef.id.spelling.equals("length") && !visitingLeftSide)
    			{
    				staticMemberJustFound=false;
					return new BaseType(TypeKind.INT, incomingType.posn);
    			}
    			
    			errorFound=true;
    			staticMemberJustFound=false;
    			
    			System.out.println("***Error: Primitives can't have .'s after them!");
    			return new BaseType(TypeKind.ERROR, incomingType.posn);
    		}
    		
    		ClassDecl incomingClassDeclaration=retrieveClassDecl(((ClassType) incomingType).className);
    		Declaration idDecl=retrieveMember(incomingClassDeclaration, deRef.id.spelling);
    		
    		deRef.id.decl=idDecl;
    		
    		if(idDecl==null)
    		{
    			System.out.println("***Error: Member being retrieved doesn't exist!");
    			System.exit(4);
    		}
    		
    		else if(((MemberDecl) idDecl).isPrivate && !typesEqual(((DeRef) deRef.ref).id.decl.type, currentClassDecl.type))
    		{
    			errorFound=true;
    			staticMemberJustFound=false;
    			
    			System.out.println("***Error: Member being retrieved is private!");
				return new BaseType(TypeKind.ERROR, idDecl.posn);
    		}
    		
    		else if(!((MemberDecl) idDecl).isStatic && inStaticMethod && !localVariableAccessed && !classRefAccessed)
    		{
    			errorFound=true;
    			staticMemberJustFound=false;
    			
	    		System.out.println("***Error: Cannot access non-static field in static method!");
	    		return new BaseType(TypeKind.ERROR, idDecl.posn);
    		}
    		
    		staticMemberJustFound=((MemberDecl) idDecl).isStatic;
    		
    		if(staticMemberJustFound)
    		{
    			errorFound=true;
	    		System.out.println("***Error: PA3 no static access");
	    		return new BaseType(TypeKind.ERROR, deRef.ref.posn);
    		}
    		
    		return idDecl.type;
    	}
    }
    
    public Object visitQualifiedRef(QualifiedRef qr, String arg)
    {
    	Reference qualifiedReferenceAST=null;
    	
    	if(qr.thisRelative)
    	{
    		Declaration currentClassDeclaration=idTable.retrieveClass(currentClassDecl.name);
    		
    		Identifier currentClassIdentifier=new Identifier(currentClassDecl.name, currentClassDecl.posn);
    		currentClassIdentifier.decl=currentClassDeclaration;
    		
    		ThisRef thisRef=new ThisRef(currentClassIdentifier, qr.qualifierList, qr.posn);
        	qualifiedReferenceAST=(Reference) thisRef.visit(this, arg);
    	}
    	
    	else
    	{
    		Declaration qrDeclaration=null;
    		int scopeLevel=0;
    		
    		if(!dealingWithCallExpression)
    		{
    			qrDeclaration=idTable.retrieveVariable(qr.qualifierList.get(0).spelling);
    			
    			//Case for id reference in initializing expression
    			if(errorFound)
    				return new BaseType(TypeKind.ERROR, qr.qualifierList.get(0).posn);
    				
    			//If variable not found, see if it is a class name (for a class reference)
				if(qrDeclaration==null)
					qrDeclaration=idTable.retrieveClass(qr.qualifierList.get(0).spelling);
				
    			scopeLevel=idTable.lastScopeLevelRetrieved;
    		}
    		
    		else
    		{
    			//All preceding qualifiers in call expression are not methods, but variables
    			if(qr.qualifierList.size()==1)
    				qrDeclaration=idTable.retrieveMethod(qr.qualifierList.get(0).spelling);
    			
    			else
    			{
    				qrDeclaration=idTable.retrieveVariable(qr.qualifierList.get(0).spelling);
    				
    				//If variable not found, see if it is a class name (for a class reference)
    				if(qrDeclaration==null)
    					qrDeclaration=idTable.retrieveClass(qr.qualifierList.get(0).spelling);
    			}
    			
    			scopeLevel=idTable.lastScopeLevelRetrieved;
    		}
    		
    		if(qrDeclaration!=null)
    		{
    			if(qrDeclaration instanceof ClassDecl)
    			{
    				IdentifierList qualifierList=new IdentifierList();
    				Iterator<Identifier> iterator=qr.qualifierList.iterator();
    				
    				//Skip over class name itself
    				iterator.next();
    				
    				while(iterator.hasNext())
    					qualifierList.add(iterator.next());
    				
    				String className=qr.qualifierList.get(0).spelling;
    				//Declaration classDeclaration=retrieveClassDecl(className);
    				
    				Identifier currentClassIdentifier=new Identifier(className, qrDeclaration.posn);
    	    		currentClassIdentifier.decl=qrDeclaration;
    	    		
    				ClassRef classRef=new ClassRef(currentClassIdentifier, qualifierList, qr.posn);
    				Object qualifiedReferenceASTObject=classRef.visit(this, arg);
    				
    				//Error case
    				if(qualifiedReferenceASTObject instanceof Type)
    				{
    					errorFound=true;
    					System.out.println("***Error: Cannot reference class name alone!");
    					
    					return qualifiedReferenceASTObject;
    				}
    				
    				qualifiedReferenceAST=(Reference) classRef.visit(this, arg);
    			}
    			
    			else if(scopeLevel==1)
    			{
    				IdentifierList qualifierList=new IdentifierList();
    				Iterator<Identifier> iterator=qr.qualifierList.iterator();
    				
    				//Skip over member reference itself
    				if(!dealingWithCallExpression)
    					iterator.next().decl=idTable.retrieveVariable(qr.qualifierList.get(0).spelling);
    				
    				else
    				{
    					//All preceding qualifiers in call expression are not methods, but variables
    	    			if(qr.qualifierList.size()==1)
    	    				iterator.next().decl=idTable.retrieveMethod(qr.qualifierList.get(0).spelling);
    	    			
    	    			else
    	    				iterator.next().decl=idTable.retrieveVariable(qr.qualifierList.get(0).spelling);
    				}
    				
    				while(iterator.hasNext())
    					qualifierList.add(iterator.next());
    	    		
    				MemberRef memberRef=new MemberRef(qr.qualifierList.get(0), qualifierList, qr.posn);
    				qualifiedReferenceAST=(Reference) memberRef.visit(this, arg);
    			}
    			
    			else if(scopeLevel>1)
    			{
    				IdentifierList qualifierList=new IdentifierList();
    				Iterator<Identifier> iterator=qr.qualifierList.iterator();
    				
    				if(!dealingWithCallExpression)
    					iterator.next().decl=idTable.retrieveVariable(qr.qualifierList.get(0).spelling);
    				
    				else
    				{
    					//All preceding qualifiers in call expression are not methods, but variables
    	    			if(qr.qualifierList.size()==1)
    	    				iterator.next().decl=idTable.retrieveMethod(qr.qualifierList.get(0).spelling);
    	    			
    	    			else
    	    			{
    	    				iterator.next().decl=idTable.retrieveVariable(qr.qualifierList.get(0).spelling);
    	    			}
    				}
    				
    				while(iterator.hasNext())
    					qualifierList.add(iterator.next());
    				
    				LocalRef localRef=new LocalRef(qr.qualifierList.get(0), qualifierList, qr.posn);
    				qualifiedReferenceAST=(Reference) localRef.visit(this, arg);
    			}
    			
    			else
    			{
    				errorFound=true;
    				System.out.println("***Error: Invalid qualified reference!");
    				return new BaseType(TypeKind.ERROR, qr.posn);
    			}
    		}
    		
    		else
    		{
    			System.out.println("***Error: identifier "+qr.qualifierList.get(0).spelling+" not found!");
    			System.exit(4);
    		}
    	}
    	
    	return qualifiedReferenceAST;
    }

    public Object visitIndexedRef(IndexedRef ir, String arg)
    {
    	DeRef arrayDeRef=(DeRef) ir.ref.visit(this, arg);
    	Object arrayTypeObject=arrayDeRef.visit(this, arg);
    	
    	visitingLeftSide=false;
    	
    	//Error case
    	if(!(arrayTypeObject instanceof ArrayType))
    		return arrayTypeObject;
    	
    	Type arrayType=((ArrayType) arrayDeRef.visit(this, arg)).eltType;
    	Type indexType=(Type) ir.indexExpr.visit(this, arg);
    		
    	if(indexType.typeKind!=TypeKind.INT)
    	{
    		errorFound=true;
    		return new BaseType(TypeKind.ERROR, ir.indexExpr.posn);
    	}
    	
    	else
    	{
    		ir.ref=arrayDeRef; //Change AST of indexedRef reference to be that of the DeRef
    		return arrayType;
    	}
    }

    public Object visitIdentifier(Identifier id, String arg)
    {
    	Declaration idBinding=idTable.retrieve(id.spelling);
    	
    	if(idBinding!=null)
    		id.decl=idBinding;
    	
    	return idBinding;
    }
    
    public Object visitOperator(Operator op, String arg)
    {
    	return op.spelling;
    }
    
    public Object visitIntLiteral(IntLiteral num, String arg)
    {
    	return new BaseType(TypeKind.INT, num.posn);
    }
    
    public Object visitBooleanLiteral(BooleanLiteral bool, String arg)
    {
    	return new BaseType(TypeKind.BOOLEAN, bool.posn);
    }
    
    public ClassDecl getClassDecl(String cn)
    {
    	for(ClassDecl cd: classDeclList)
    	{
    		if(cd.name.equals(cn))
    			return cd;
    	}
    	
    	return null;
    }
    
    public ClassDecl retrieveClassDecl(String cn)
    {
    	for(ClassDecl cd: classDeclList)
    		if(cd.name.equals(cn))
    			return cd;
    	
    	//For std environment classes
    	for(ClassDecl cd: stdClassDeclList)
    		if(cd.name.equals(cn))
    			return cd;
    	
    	return null;
    }
    
    public Declaration retrieveMember(ClassDecl cd, String id)
    {
    	if(cd.fieldDeclList!=null)
    		for(FieldDecl fd: cd.fieldDeclList)
    			if(fd.name.equals(id))
    				return fd;
    	
    	if(cd.methodDeclList!=null)
    		for(MethodDecl md: cd.methodDeclList)
    			if(md.name.equals(id))
    				return md;
    	
    	return null;
    }
    
    public boolean typesEqual(Type type1, Type type2)
    {
    	if(type1.typeKind==type2.typeKind)
    	{
    		if(type1.typeKind==TypeKind.CLASS)
    		{
    			if(((ClassType) type1).className.equals(((ClassType) type2).className))
    				return true;
    			
    			else
    				return false;
    		}
    		
    		else if(type1.typeKind==TypeKind.ARRAY)
    		{
    			if(typesEqual(((ArrayType) type1).eltType, ((ArrayType) type2).eltType))
    				return true;
    			
    			else
    				return false;
    		}
    		
    		else if(type1.typeKind==TypeKind.VOID || type1.typeKind==TypeKind.INT || type1.typeKind==TypeKind.BOOLEAN)
    			return true; //For primitive types
    		
    		else
    			return false; //For UNSUPPORTED or ERROR types
    	}
    	
    	return false;
    }
}
