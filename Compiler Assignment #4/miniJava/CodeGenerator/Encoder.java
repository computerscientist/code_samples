package miniJava.CodeGenerator;

import java.util.ArrayList;

import mJAM.*;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

/**
 * Compiles an AST into mJAM assembly instructions
 * @author Robert Dallara
 * 
 */
public class Encoder implements Visitor<Integer, Object>
{
	public static final int inVarDecl=-1;
	public static final int atTopLevel=1;
	
	private boolean visitingUnaryExpression;
	private boolean visitingFunctionReference;
	private boolean visitingLeftSide;
	
	private ArrayList<ClassDisplacementPair> cdp;
	private ArrayList<MemberDisplacementPair> mdp;
	private ArrayList<MemberDisplacementPair> danglingMemberRefs;
	
	private int startAddress;
	private int currentVarDeclIndex;
	
	public Encoder()
	{
		visitingUnaryExpression=false;
		visitingFunctionReference=false;
		visitingLeftSide=false;
		
		cdp=new ArrayList<ClassDisplacementPair>();
		mdp=new ArrayList<MemberDisplacementPair>();
		danglingMemberRefs=new ArrayList<MemberDisplacementPair>();
	}
	
	public void compileAST(AST ast, String fileName)
	{
		ast.visit(this, 0);
		
		//Make sure all new objects being created know where their corresponding class object actually is
		for(int i=0;i<cdp.size();i++)
			Machine.patch(cdp.get(i).getInstructionAddress(), cdp.get(i).getClassAddress());
		
		//Make sure all references to members in another class know the address of that member
		for(int i=0;i<mdp.size();i++)
			Machine.patch(mdp.get(i).getInstructionAddress(), mdp.get(i).getMemberAddress());
		
		for(int i=0;i<danglingMemberRefs.size();i++)
		{
			Machine.patch(danglingMemberRefs.get(i).getInstructionAddress(), danglingMemberRefs.get(i).getMemberAddress());
			Machine.code[danglingMemberRefs.get(i).getInstructionAddress()].n = danglingMemberRefs.get(i).getMemberDecl().runtimeEntity.size;
		}

		ObjectFile objectFile=new ObjectFile(fileName);
		objectFile.write();
	}
	
	public Object visitPackage(Package prog, Integer arg)
	{
		Machine.initCodeGen();
		
		//Ensure that local variables in main method start at LB[3]
		Machine.emit(Machine.Op.PUSH, 3);
		
		int firstAddress=Machine.nextInstrAddr();
		Machine.emit(Machine.Op.JUMP, Machine.Reg.CB, 0);
		
		//int currentAddress=firstAddress+1; //Take into account call to main method as well as "println" method storage
		
		for(ClassDecl cd: prog.classDeclList)
	        cd.visit(this, arg);

		Machine.patch(firstAddress, startAddress);
        return null;
    }
    
    public Object visitClassDecl(ClassDecl clas, Integer arg)
    {
    	int currentFieldDeclIndex=0;
    	clas.runtimeEntity=new KnownAddress(Machine.addressSize, Machine.nextInstrAddr());
    	
        for(FieldDecl f: clas.fieldDeclList)
        {
        	f.visit(this, currentFieldDeclIndex);
        	currentFieldDeclIndex++;
        }
        
        for(MethodDecl m: clas.methodDeclList)
        	m.visit(this, arg);
        
        return null;
    }
    
    public Object visitFieldDecl(FieldDecl f, Integer arg)
    {
    	int size=(Integer) f.type.visit(this, arg);
    	
    	f.runtimeEntity=new UnknownValue(size, arg);
        return size;
    }
    
    public Object visitMethodDecl(MethodDecl m, Integer arg)
    {
    	boolean inMainMethod=false;
    	
    	currentVarDeclIndex=0;
    	
    	if(!m.isPrivate && m.isStatic && m.type.typeKind==TypeKind.VOID && m.name.equals("main") && m.parameterDeclList.size()==1 && m.parameterDeclList.get(0).type.typeKind==TypeKind.ARRAY && ((ArrayType)(m.parameterDeclList.get(0).type)).eltType.typeKind==TypeKind.UNSUPPORTED)
    	{
    		inMainMethod=true;
    		startAddress=Machine.nextInstrAddr();
    	}

    	m.runtimeEntity=new KnownAddress(Machine.addressSize, Machine.nextInstrAddr());
    	int returnSize=(Integer) m.type.visit(this, arg);
    	
        ParameterDeclList pdl=m.parameterDeclList;
        
        for(int i=0;i<pdl.size();i++)
            pdl.get(i).visit(this, -pdl.size()+i);
        
        StatementList sl=m.statementList;
        
        for(Statement s: sl)
        	s.visit(this, arg);
        
        if(m.returnExp!=null)
            m.returnExp.visit(this, arg);
        
        if(!inMainMethod)
        	Machine.emit(Machine.Op.RETURN, returnSize, 0, pdl.size());
        
        else
        	Machine.emit(Machine.Op.HALT, 0, 0, 0);
        
        return null;
    }
    
    public Object visitParameterDecl(ParameterDecl pd, Integer arg)
    {
    	int parameterSize=(Integer) pd.type.visit(this, arg);
    	pd.runtimeEntity=new UnknownValue(parameterSize, arg);
    	
        return null;
    } 
    
    public Object visitVarDecl(VarDecl vd, Integer arg)
    {
    	return vd.type.visit(this, arg);
    }
    
    public Object visitBaseType(BaseType type, Integer arg)
    {
    	int size=1;
    	
    	switch(type.typeKind)
    	{
    		case BOOLEAN:
    		{
    			size=Machine.booleanSize;
    			break;
    		}
    		
    		case INT:
    		{
    			size=Machine.integerSize;
    			break;
    		}
    		
    		case VOID:
    		{
    			size=0;
    			break;
    		}
    		
    		default:
    		{
    			System.out.println("Error: Invalid Base Type!");
    			System.exit(4);
    		}
    	}
    	
        return size;
    }
    
    public Object visitClassType(ClassType type, Integer arg)
    {
        return Machine.addressSize;
    }
    
    public Object visitArrayType(ArrayType type, Integer arg)
    {
        return type.eltType.visit(this, arg);
    }
    
    public Object visitBlockStmt(BlockStmt stmt, Integer arg)
    {
        StatementList sl = stmt.sl;
        int startArg=arg;
        
        for(Statement s: sl)
        {
        	if(s instanceof VarDeclStmt)
        		stmt.numberOfVarDecls++;
        	
        	s.visit(this, arg);
        }
        
        //If any variables allocated onto stack in block statement, they must be deallocated
        if(stmt.numberOfVarDecls>0)
        {
        	Machine.emit(Machine.Op.POP, 0, Machine.Reg.ST, stmt.numberOfVarDecls);
        	this.currentVarDeclIndex--;
        }
        
        return arg-startArg;
    }
    
    public Object visitVardeclStmt(VarDeclStmt stmt, Integer arg)
    {
        int varSize=(Integer) stmt.varDecl.visit(this, arg);
        stmt.initExp.visit(this, stmt.initExp instanceof NewObjectExpr ? Encoder.inVarDecl : arg);
        
        stmt.varDecl.runtimeEntity=new UnknownValue(varSize, currentVarDeclIndex);
        
        currentVarDeclIndex++;
        return null;
    }
    
    public Object visitAssignStmt(AssignStmt stmt, Integer arg)
    {
        if(stmt.ref instanceof IndexedRef)
        {
        	((IndexedRef) stmt.ref).ref.visit(this, Encoder.atTopLevel);
        	
        	((IndexedRef) stmt.ref).indexExpr.visit(this, arg);
        	stmt.val.visit(this, arg);
            
        	Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.arrayupd);
        }
        
        else
        {
        	if(((DeRef) stmt.ref).id==null)
        	{
        		if(((DeRef) stmt.ref).ref instanceof LocalRef)
        		{
        			stmt.val.visit(this, arg);	
        			
        			int index=((UnknownValue)((LocalRef)((DeRef) stmt.ref).ref).id.decl.runtimeEntity).index;
        			Machine.emit(Machine.Op.STORE, ((LocalRef)((DeRef) stmt.ref).ref).id.decl.runtimeEntity.size, Machine.Reg.LB, index+(index>=0 ? Machine.linkDataSize : 0));
        		}
        		
        		else
        		{
        			Identifier lowerId=((DeRef) stmt.ref).ref instanceof ClassRef ? ((ClassRef)(((DeRef) stmt.ref).ref)).classIdentifier :
        							   ((DeRef) stmt.ref).ref instanceof MemberRef ? ((MemberRef)(((DeRef) stmt.ref).ref)).classIdentifier :
        							   ((ThisRef)(((DeRef) stmt.ref).ref)).classIdentifier;
        			
        			stmt.val.visit(this, ((UnknownValue) lowerId.decl.runtimeEntity).index);
        			Machine.emit(Machine.Op.STORE, lowerId.decl.runtimeEntity.size, Machine.Reg.OB, ((UnknownValue) lowerId.decl.runtimeEntity).index);
        		}
        	}

        	else
        	{
        		visitingLeftSide=true;
        		boolean visitedLocal=(Boolean) stmt.ref.visit(this, Encoder.atTopLevel);
        		visitingLeftSide=false;
        		
        		/**int nextInstruction=Machine.nextInstrAddr();
        		Machine.emit(Machine.Op.LOADL, 0);

        		mdp.add(new MemberDisplacementPair(nextInstruction, (MemberDecl)((DeRef) stmt.ref).id.decl));*/
        		stmt.val.visit(this, visitedLocal ? 1 : 0);
        		
        		if(((DeRef) stmt.ref).ref instanceof ThisRef)
        			Machine.emit(Machine.Op.STORE, ((DeRef) stmt.ref).id.decl.runtimeEntity.size, Machine.Reg.OB, ((UnknownValue)((DeRef) stmt.ref).id.decl.runtimeEntity).index);
        			
        		else
        			Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.fieldupd);
        	}
        }
        
        return null;
    }
    
    public Object visitCallStmt(CallStmt stmt, Integer arg)
    {
    	RuntimeEntity runtimeEntity=null;
    	
    	for(Expression e: stmt.argList)
    		e.visit(this, arg);
    	
    	if(((DeRef) stmt.methodRef).id!=null)
    		runtimeEntity=((DeRef) stmt.methodRef).id.decl.runtimeEntity;
    	
    	else
    		runtimeEntity=((MemberRef)((DeRef) stmt.methodRef).ref).classIdentifier.decl.runtimeEntity;
    	
    	int address=0;
    	
    	//Special case for System.out.println()
    	if(runtimeEntity instanceof UnknownAddress)
    	{
    		Machine.emit(Machine.Prim.putint);
    		return 1;
    	}
    	
    	else if(runtimeEntity!=null)
    	{
    		if(((DeRef) stmt.methodRef).id!=null)
    			address=((KnownAddress)((DeRef) stmt.methodRef).id.decl.runtimeEntity).address;
    		
    		else
    			address=((KnownAddress)((MemberRef)((DeRef) stmt.methodRef).ref).classIdentifier.decl.runtimeEntity).address;
    	}
    	
    	visitingFunctionReference=true;
    	stmt.methodRef.visit(this, Encoder.atTopLevel);
    	visitingFunctionReference=false;
    	
        if(runtimeEntity==null)
    	{
    		int currentInstructionAddr=Machine.nextInstrAddr();
    		
    		MethodDecl methodDecl=((DeRef) stmt.methodRef).id!=null ? (MethodDecl)(((DeRef) stmt.methodRef).id.decl) : (MethodDecl)(((MemberRef)((DeRef) stmt.methodRef).ref).classIdentifier.decl);
    		mdp.add(new MemberDisplacementPair(currentInstructionAddr, methodDecl));
    	}
        
        Machine.emit(Machine.Op.CALL, Machine.Reg.CB, address);
        
        if((((DeRef) stmt.methodRef).id!=null && ((DeRef) stmt.methodRef).id.decl.type.typeKind!=TypeKind.VOID) || (((DeRef) stmt.methodRef).id==null && ((MethodDecl)((MemberRef)((DeRef) stmt.methodRef).ref).classIdentifier.decl).type.typeKind!=TypeKind.VOID))
        	Machine.emit(Machine.Op.POP, 0, Machine.Reg.ST, 1);
        
        return null;
    }
    
    public Object visitIfStmt(IfStmt stmt, Integer arg)
    {
    	stmt.cond.visit(this, arg);
        
        int i=Machine.nextInstrAddr();
        Machine.emit(Machine.Op.JUMPIF, 0, Machine.Reg.CB, 0);
        
        stmt.thenStmt.visit(this, arg);
        
        if(stmt.elseStmt!=null)
        {
        	//Create jump instruction at end of then statement...
        	int j=Machine.nextInstrAddr();
            Machine.emit(Machine.Op.JUMP, 0, Machine.Reg.CB, 0);
            
        	int g=Machine.nextInstrAddr();
        	Machine.patch(i, g);
        	
            stmt.elseStmt.visit(this, arg);
            
            int h=Machine.nextInstrAddr();
            Machine.patch(j, h);
        }
        
        else
        {
        	int h=Machine.nextInstrAddr();
        	Machine.patch(i, h);
        }
        
        return null;
    }
    
    public Object visitWhileStmt(WhileStmt stmt, Integer arg)
    {
    	int j=Machine.nextInstrAddr();
    	Machine.emit(Machine.Op.JUMP, 0, Machine.Reg.CB, 0);
    	
    	int g=Machine.nextInstrAddr();
    	stmt.body.visit(this, arg);
    	
    	int h=Machine.nextInstrAddr();
    	Machine.patch(j, h);
    	
    	stmt.cond.visit(this, arg);
    	
    	Machine.emit(Machine.Op.JUMPIF, 1, Machine.Reg.CB, g);
    	return null;
    }
    
    public Object visitUnaryExpr(UnaryExpr expr, Integer arg)
    {
    	visitingUnaryExpression=true;
    	expr.expr.visit(this, arg);
    	
    	Machine.Prim p=(Machine.Prim) expr.operator.visit(this, arg);
        
        Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, p);
        visitingUnaryExpression=false;
        
        return null;
    }
    
    public Object visitBinaryExpr(BinaryExpr expr, Integer arg)
    {
    	int jumpIfAddr=-1;
    	int jumpAddr=-1;
    	
    	//Visit left side
    	expr.left.visit(this, arg);
    	
    	//Handle short-circuit logic for "&&" and "||" operators
    	if(expr.operator.spelling.equals("&&"))
    	{
    		jumpIfAddr=Machine.nextInstrAddr();
    		Machine.emit(Machine.Op.JUMPIF, 0, Machine.Reg.CB, 0);
    	}
    	
    	else if(expr.operator.spelling.equals("||"))
    	{
    		jumpIfAddr=Machine.nextInstrAddr();
    		Machine.emit(Machine.Op.JUMPIF, 1, Machine.Reg.CB, 0);
    	}
    	
    	//Visit right side
        expr.right.visit(this, arg);
        
        //Take care of last instructions that deal with short-circuit logic
        if(expr.operator.spelling.equals("&&") || expr.operator.spelling.equals("||"))
        {
        	//Jump to "absolute end" of everything after handling short circuit logic...
        	jumpAddr=Machine.nextInstrAddr();
        	Machine.emit(Machine.Op.JUMP, Machine.Reg.CB, 0);
        	
        	//Instruction at "end" label
        	int endAddress=Machine.nextInstrAddr();
            Machine.emit(Machine.Op.LOADL, expr.operator.spelling.equals("&&") ? 0 : 1);
            
            Machine.patch(jumpIfAddr, endAddress);
            
            //We are now at "absolute end" label
            int absoluteEndAddress=Machine.nextInstrAddr();
            Machine.patch(jumpAddr, absoluteEndAddress);
        }
        
        else
        {
        	Machine.Prim p=(Machine.Prim) expr.operator.visit(this, arg);
        	Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, p);
        }
        
        return null;
    }
    
    public Object visitRefExpr(RefExpr expr, Integer arg)
    {
        return expr.ref.visit(this, Encoder.atTopLevel);
    }
    
    public Object visitCallExpr(CallExpr expr, Integer arg)
    {
    	RuntimeEntity runtimeEntity=null;
    	
    	for(Expression e: expr.argList)
        	e.visit(this, arg);
    	
    	if(((DeRef) expr.functionRef).id!=null)
    		runtimeEntity=((DeRef) expr.functionRef).id.decl.runtimeEntity;
    	
    	else
    		runtimeEntity=((MemberRef)((DeRef) expr.functionRef).ref).classIdentifier.decl.runtimeEntity;
    	
    	int address=0;
    	
    	
    	if(runtimeEntity!=null)
    	{
    		if(((DeRef) expr.functionRef).id!=null)
    			address=((KnownAddress)((DeRef) expr.functionRef).id.decl.runtimeEntity).address;
    		
    		else
    			address=((KnownAddress)((MemberRef)((DeRef) expr.functionRef).ref).classIdentifier.decl.runtimeEntity).address;
    	}
    	
    	visitingFunctionReference=true;
    	expr.functionRef.visit(this, Encoder.atTopLevel);
        visitingFunctionReference=false;
    	
        if(runtimeEntity==null)
    	{
    		int currentInstructionAddr=Machine.nextInstrAddr();
    		
    		MethodDecl methodDecl=((DeRef) expr.functionRef).id!=null ? (MethodDecl)(((DeRef) expr.functionRef).id.decl) : (MethodDecl)(((MemberRef)((DeRef) expr.functionRef).ref).classIdentifier.decl);
    		mdp.add(new MemberDisplacementPair(currentInstructionAddr, methodDecl));
    	}
        
        Machine.emit(Machine.Op.CALL, Machine.Reg.CB, address); 
        return null;
    }
    
    public Object visitLiteralExpr(LiteralExpr expr, Integer arg)
    {
        return expr.literal.visit(this, arg);
    }

    public Object visitNewArrayExpr(NewArrayExpr expr, Integer arg)
    {
        expr.sizeExpr.visit(this, arg);
        Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.newarr);
        
        return null;
    }
    
    public Object visitNewObjectExpr(NewObjectExpr expr, Integer arg)
    {
    	//Local variable case
    	if(arg==Encoder.inVarDecl)
    		Machine.emit(Machine.Op.LOADA, Machine.Reg.LB, currentVarDeclIndex+Machine.linkDataSize);
        
    	else
    		Machine.emit(Machine.Op.LOADA, Machine.Reg.OB, arg);
    	
        Machine.emit(Machine.Op.LOADL, expr.classtype.classDecl.fieldDeclList.size());
        Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.newobj);
        
        return null;
    }
    
    public Object visitQualifiedRef(QualifiedRef qr, Integer arg)
    {
	    return null;
    }
    
    public Object visitIndexedRef(IndexedRef ir, Integer arg)
    {
    	ir.ref.visit(this, Encoder.atTopLevel);
    	ir.indexExpr.visit(this, arg);
    	
    	Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.arrayref);
    	return null;
    }
    
    public Object visitIdentifier(Identifier id, Integer arg)
    {
        return null;
    }
    
    public Object visitOperator(Operator op, Integer arg)
    {
    	return getPrimitiveRoutineAddress(op);
    }
    
    public Object visitIntLiteral(IntLiteral num, Integer arg)
    {
    	int v=valuation(num.spelling);
        Machine.emit(Machine.Op.LOADL, v);
        
        return null;
    }
    
    public Object visitBooleanLiteral(BooleanLiteral bool, Integer arg)
    {
    	int v=booleanValuation(bool.spelling);
        Machine.emit(Machine.Op.LOADL, v);
        
        return null;
    }
    
    public Object visitThisRef(ThisRef ref, Integer arg)
    {
    	return false;
    }
    
    public Object visitMemberRef(MemberRef ref, Integer arg)
    {
    	//If calling method inside current object, just load OB
    	if(ref.classIdentifier.decl instanceof MethodDecl)
    	{
    		Machine.emit(Machine.Op.LOADA, Machine.Reg.OB, 0);
    		return false;
    	}
    	
    	if(ref.classIdentifier.decl.runtimeEntity!=null)
    	{
    		if(ref.classIdentifier.decl.runtimeEntity instanceof UnknownValue)
    			Machine.emit(Machine.Op.LOAD, ref.classIdentifier.decl.runtimeEntity.size, Machine.Reg.OB, ((UnknownValue) ref.classIdentifier.decl.runtimeEntity).index);
    	}
    	
    	else
    	{
    		int currentInstructionAddr=Machine.nextInstrAddr();
    		danglingMemberRefs.add(new MemberDisplacementPair(currentInstructionAddr, (MethodDecl) ref.classIdentifier.decl));
    		
    		Machine.emit(Machine.Op.LOAD, 0, Machine.Reg.OB, 0);
    	}
    	
    	return false;
    }
    
    public Object visitLocalRef(LocalRef ref, Integer arg)
    {
    	int index=((UnknownValue) ref.id.decl.runtimeEntity).index;
    	
    	Machine.emit(Machine.Op.LOAD, ref.id.decl.runtimeEntity.size, Machine.Reg.LB, index+(index>=0 ? Machine.linkDataSize : 0));
    	return true;
    }
    
    public Object visitDeRef(DeRef ref, Integer arg)
    {
    	boolean visitedLocal=(Boolean) ref.ref.visit(this, 0);
    	
    	//For "length field of arrays case
    	if(ref.id!=null && (ref.id.decl==null))
    	{
    		Machine.emit(Machine.Op.LOADL, -1);
        	Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.add);
        	
    		Machine.emit(Machine.Op.LOADI);
    	}
    	
    	else if(ref.id!=null && !(ref.id.decl.runtimeEntity instanceof UnknownAddress) && !(ref.ref instanceof ThisRef))
    	{
	    	RuntimeEntity runtimeEntity=ref.id.decl.runtimeEntity;
	    	
	    	if(runtimeEntity instanceof UnknownValue)
	    	{
		    	Machine.emit(Machine.Op.LOADL, ((UnknownValue) runtimeEntity).index);
		    	
		    	if(arg!=Encoder.atTopLevel || !visitingLeftSide)
		    		Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.fieldref);
	    	}
	    	
	    	else if(runtimeEntity==null)
	    	{
	    		if(!(arg==Encoder.atTopLevel && visitingFunctionReference))
	    		{
		    		int currentInstructionAddr=Machine.nextInstrAddr();
		    		Machine.emit(Machine.Op.LOADL, 0);
		    		
		    		mdp.add(new MemberDisplacementPair(currentInstructionAddr, (MemberDecl) ref.id.decl));
	    		}
	    		
	    		if(arg!=Encoder.atTopLevel || (!visitingLeftSide && !visitingFunctionReference))
	    			Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.fieldref);
	    	}
    	}
    	
    	else if(ref.ref instanceof ThisRef)
    	{
    		//If calling method inside current object or just referring to "this" by itself or dealing with left side of assignment, just load OB
        	if(ref.id==null || ref.id.decl instanceof MethodDecl || visitingLeftSide)
        	{
        		Machine.emit(Machine.Op.LOADA, Machine.Reg.OB, 0);
        		
        		if(ref.id!=null && !(ref.id.decl instanceof MethodDecl))
        		{
        			RuntimeEntity runtimeEntity=ref.id.decl.runtimeEntity;
        			
        			if(runtimeEntity instanceof UnknownValue)
        	    	{
        		    	Machine.emit(Machine.Op.LOADL, ((UnknownValue) runtimeEntity).index);
        		    	
        		    	if(arg!=Encoder.atTopLevel || !visitingLeftSide)
        		    		Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.fieldref);
        	    	}
        			
        			else if(runtimeEntity==null)
        	    	{
        	    		if(!(arg==Encoder.atTopLevel && visitingFunctionReference))
        	    		{
        		    		int currentInstructionAddr=Machine.nextInstrAddr();
        		    		Machine.emit(Machine.Op.LOADL, 0);
        		    		
        		    		mdp.add(new MemberDisplacementPair(currentInstructionAddr, (MemberDecl) ref.id.decl));
        	    		}
        	    		
        	    		if(arg!=Encoder.atTopLevel || (!visitingLeftSide && !visitingFunctionReference))
        	    			Machine.emit(Machine.Op.CALL, 0, Machine.Reg.PB, Machine.Prim.fieldref);
        	    	}
        		}
        		
        		return visitedLocal;
        	}
        	
        	if(ref.id.decl.runtimeEntity!=null && !(arg==Encoder.atTopLevel && visitingLeftSide))
        	{
        		if(ref.id.decl.runtimeEntity instanceof UnknownValue)
        			Machine.emit(Machine.Op.LOAD, ref.id.decl.runtimeEntity.size, Machine.Reg.OB, ((UnknownValue) ref.id.decl.runtimeEntity).index);
        	}

        	if(ref.id.decl.runtimeEntity==null && arg!=Encoder.atTopLevel)
        	{
        		int currentInstructionAddr=Machine.nextInstrAddr();
        		danglingMemberRefs.add(new MemberDisplacementPair(currentInstructionAddr, (MethodDecl) ref.id.decl));

        		Machine.emit(Machine.Op.LOAD, 0, Machine.Reg.OB, 0);
        	}
    	}
    	
    	return visitedLocal;
    }
    
    public Object visitClassRef(ClassRef ref, Integer arg)
    {
    	//Special case with System.out.println case
    	if(ref.classIdentifier.decl.runtimeEntity instanceof UnknownAddress)
    		return false;
    	
    	Machine.emit(Machine.Op.LOAD, ref.classIdentifier.decl.runtimeEntity.size, Machine.Reg.OB, ((KnownAddress) ref.classIdentifier.decl.runtimeEntity).address);
    	return false;
    }
    
    private Machine.Prim getPrimitiveRoutineAddress(Operator o)
    {
    	if(o.spelling.equals("!"))
    		return Machine.Prim.not;
    	
    	else if(o.spelling.equals("&&"))
    		return Machine.Prim.and;
    	
    	else if(o.spelling.equals("||"))
    		return Machine.Prim.or;
    	
    	else if(o.spelling.equals("+"))
    		return Machine.Prim.add;
    	
    	else if(o.spelling.equals("-"))
    		return visitingUnaryExpression ? Machine.Prim.neg: Machine.Prim.sub;
    	
    	else if(o.spelling.equals("*"))
    		return Machine.Prim.mult;
    	
    	else if(o.spelling.equals("/"))
    		return Machine.Prim.div;
    	
    	else if(o.spelling.equals("<"))
    		return Machine.Prim.lt;
    	
    	else if(o.spelling.equals("<="))
    		return Machine.Prim.le;
    	
    	else if(o.spelling.equals(">="))
    		return Machine.Prim.ge;
    	
    	else if(o.spelling.equals(">"))
    		return Machine.Prim.gt;
    	
    	else if(o.spelling.equals("=="))
    		return Machine.Prim.eq;
    	
    	else if(o.spelling.equals("!="))
    		return Machine.Prim.ne;
    	
    	else
    	{
    		System.out.println("Error: Invalid operator!");
    		System.exit(4);
    	}
    	
    	//Shouldn't get down to here...
    	return null;
    }
    
    //PRE: spelling must be a boolean literal (i.e: "true" or "false")
    private int booleanValuation(String spelling)
    {
    	return (Boolean.parseBoolean(spelling)==true) ? Machine.trueRep : Machine.falseRep;
    }
    
    //PRE: spelling must be an integer
    private int valuation(String spelling)
    {
    	int valuation=0;
    	
    	try
    	{
    		valuation=Integer.parseInt(spelling);
    	}
    	
    	catch(NumberFormatException e)
    	{
    		System.out.println("Integer "+spelling+" out of range!");
    		System.exit(4);
    	}
    	
    	return valuation;
    }
}
