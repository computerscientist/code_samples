package miniJava.SyntacticAnalyzer;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

public class Parser {

	private Scanner sc;
	private SourcePosition currentSourcePosition;
	
	private Token currentToken;
	
	public Parser(Scanner scanner)
	{
		this.sc=scanner;
	}
	
	public AST parse()
	{
		do
		{
			currentToken=this.sc.scan();
		}
		while(currentToken.getType()!=TokenType.EOT && currentToken.getType()==TokenType.COMMENT);
		
		AST programAST=parseProgram();
		accept(TokenType.EOT);
		
		return programAST;
	}
	
	private Package parseProgram()
	{
		ClassDeclList classDeclarations=new ClassDeclList();
		
		while(currentToken.getType()==TokenType.CLASS)
			classDeclarations.add(parseClassDeclaration());
		
		Package programAST=new Package(classDeclarations, new SourcePosition(1, 0));
		return programAST;
	}
	
	private ClassDecl parseClassDeclaration()
	{
		FieldDeclList fdl=new FieldDeclList();
		MethodDeclList mdl=new MethodDeclList();
		
		SourcePosition classDeclPosn=new SourcePosition(currentToken.getSourcePosition().getLineNumber(), currentToken.getSourcePosition().getColumnNumber());
		accept(TokenType.CLASS);
		
		Identifier classIdentifier=parseIdentifier();
		
		String cn=classIdentifier.spelling;
		
		accept(TokenType.LBRACE);
		
		while(currentToken.getType()!=TokenType.RBRACE)
		{
			Declaration declaration=parseMemberDeclaration();
			
			if(declaration instanceof FieldDecl)
				fdl.add((FieldDecl) declaration);
			
			else
				mdl.add((MethodDecl) declaration);
		}
		
		accept(TokenType.RBRACE);
		
		return new ClassDecl(cn, fdl, mdl, classDeclPosn);
	}
	
	private Declaration parseMemberDeclaration()
	{
		Declaration declaration=null;
		FieldDecl declarators=parseDeclarators();
		
		switch(currentToken.getType())
		{
			case SEMICOLON:
			{
				acceptIt();
				declaration=new FieldDecl(declarators, declarators.posn);
				
				break;
			}
			
			case LPAREN:
			{
				acceptIt();
				ParameterDeclList parameterList=new ParameterDeclList();
				
				if(currentToken.getType()!=TokenType.RPAREN)
					parameterList=parseParameterList();
				
				accept(TokenType.RPAREN);
				accept(TokenType.LBRACE);
				
				StatementList statementList=new StatementList();
				
				while(currentToken.getType()!=TokenType.RETURN && currentToken.getType()!=TokenType.RBRACE)
				{
					Statement s=parseStatement();
					statementList.add(s);
				}
				
				Expression returnExpression=null;
				
				if(currentToken.getType()==TokenType.RETURN)
				{
					acceptIt();
					returnExpression=parseExpression();
					
					accept(TokenType.SEMICOLON);
				}
				
				accept(TokenType.RBRACE);
				
				declaration=new MethodDecl(declarators, parameterList, statementList, returnExpression, declarators.posn);
				break;
			}
			
			default:
				parseError("Error parsing declaration!");
		}
		
		return declaration;
	}
	
	private FieldDecl parseDeclarators()
	{
		Identifier id=null;
		FieldDecl fdecl;
		
		boolean isPrivate=false, isStatic=false;
		SourcePosition declaratorsPosition=null;
		
		if(currentToken.getType()==TokenType.PUBLIC || currentToken.getType()==TokenType.PRIVATE)
		{
			if(currentToken.getType()==TokenType.PRIVATE)
				isPrivate=true;
			
			declaratorsPosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
			acceptIt();
		}
		
		if(currentToken.getType()==TokenType.STATIC)
		{
			if(declaratorsPosition==null)
				declaratorsPosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
			
			acceptIt();
			isStatic=true;
		}
		
		Type type=parseType();
		
		if(declaratorsPosition==null)
			declaratorsPosition=type.posn;
		
		id=parseIdentifier();
		
		fdecl=new FieldDecl(isPrivate, isStatic, type, id.spelling, declaratorsPosition);
		return fdecl;
	}
	
	private Type parseType()
	{
		Type t=null;
		
		switch(currentToken.getType())
		{
			case INT:
			{
				SourcePosition intSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				acceptIt();
				
				if(currentToken.getType()==TokenType.LBRACKET)
				{
					acceptIt();
					accept(TokenType.RBRACKET);
					
					t=new ArrayType(new BaseType(TypeKind.INT, intSourcePosition), intSourcePosition);
				}
				
				else
					t=new BaseType(TypeKind.INT, intSourcePosition);
				
				break;
			}
			
			case BOOLEAN:
			{
				SourcePosition booleanSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				acceptIt();
				t=new BaseType(TypeKind.BOOLEAN, booleanSourcePosition);
				
				break;
			}
			
			case VOID:
			{
				SourcePosition voidSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				acceptIt();
				t=new BaseType(TypeKind.VOID, voidSourcePosition);
				
				break;
			}
			
			case IDENTIFIER:
			{
				String id=currentToken.getSpelling();
				SourcePosition idSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				acceptIt();
				
				if(currentToken.getType()==TokenType.LBRACKET)
				{
					acceptIt();
					accept(TokenType.RBRACKET);
					
					t=new ArrayType(new ClassType(id, idSourcePosition), idSourcePosition);
				}
				
				else
					t=new ClassType(id, idSourcePosition);
				
				break;
			}
			
			default:
				parseError("Improperly formatted type!");
		}
		
		return t;
	}
	
	private ParameterDeclList parseParameterList()
	{
		ParameterDeclList pl=new ParameterDeclList();
		
		Type t=parseType();
		Identifier id=parseIdentifier();
		
		pl.add(new ParameterDecl(t, id.spelling, id.posn));
		
		while(currentToken.getType()==TokenType.COMMA)
		{
			acceptIt();
			
			Type u=parseType();
			Identifier i=parseIdentifier();
			
			pl.add(new ParameterDecl(u, i.spelling, i.posn));
		}
		
		return pl;
	}
	
	/**
	 * Parses an identifier from the source program, ACCEPTS it, and returns it
	 * @return the identifier found
	 */
	private Identifier parseIdentifier()
	{
		Identifier id=new Identifier(currentToken.getSpelling(), currentToken.getSourcePosition());
		accept(TokenType.IDENTIFIER);
		
		return id;
	}
	
	private ExprList parseArgumentList()
	{
		ExprList el=new ExprList();
		el.add(parseExpression());
		
		while(currentToken.getType()==TokenType.COMMA)
		{
			acceptIt();
			el.add(parseExpression());
		}
		
		return el;
	}
	
	private Statement parseStatement()
	{
		Statement statement=null;
		
		switch(currentToken.getType())
		{
			case LBRACE:
			{
				SourcePosition braceSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				acceptIt();
				StatementList statementList=new StatementList();
				
				while(currentToken.getType()!=TokenType.RBRACE)
					statementList.add(parseStatement());
				
				accept(TokenType.RBRACE);
				
				statement=new BlockStmt(statementList, braceSourcePosition);
				break;
			}
			
			case IF:
			{
				SourcePosition ifSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				acceptIt();
				
				accept(TokenType.LPAREN);
				Expression condition=parseExpression();
				accept(TokenType.RPAREN);
				
				Statement thenStatement=parseStatement();
				
				if(currentToken.getType()==TokenType.ELSE)
				{
					acceptIt();
					Statement elseStatement=parseStatement();
					
					statement=new IfStmt(condition, thenStatement, elseStatement, ifSourcePosition);
				}
				
				else
					statement=new IfStmt(condition, thenStatement, ifSourcePosition);
				
				break;
			}
			
			case WHILE:
			{
				SourcePosition whileSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				acceptIt();
				
				accept(TokenType.LPAREN);
				Expression condition=parseExpression();
				accept(TokenType.RPAREN);
				
				Statement body=parseStatement();
				
				statement=new WhileStmt(condition, body, whileSourcePosition);
				break;
			}
			
			case INT:
			{
				SourcePosition intSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				acceptIt();
				boolean isArray=false;
				
				if(currentToken.getType()==TokenType.LBRACKET)
				{
					acceptIt();
					isArray=true;
					
					accept(TokenType.RBRACKET);
				}

				Identifier id=parseIdentifier();
				accept(TokenType.EQUALS);
				
				Expression rightSide=parseExpression();
				accept(TokenType.SEMICOLON);

				VarDecl varDecl=new VarDecl(isArray ? new ArrayType(new BaseType(TypeKind.INT, intSourcePosition), intSourcePosition) : new BaseType(TypeKind.INT, intSourcePosition), id.spelling, intSourcePosition);
				statement=new VarDeclStmt(varDecl, rightSide, varDecl.posn);
				
				break;
			}

			case BOOLEAN: case VOID:
			{
				boolean isBoolean=(currentToken.getType()==TokenType.BOOLEAN);
				SourcePosition booleanVoidSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				acceptIt();
				
				Identifier id=parseIdentifier();
				accept(TokenType.EQUALS);
				
				Expression rightSide=parseExpression();
				accept(TokenType.SEMICOLON);

				VarDecl varDecl=new VarDecl(new BaseType(isBoolean ? TypeKind.BOOLEAN : TypeKind.VOID, booleanVoidSourcePosition), id.spelling, booleanVoidSourcePosition);
				statement=new VarDeclStmt(varDecl, rightSide, booleanVoidSourcePosition);
				
				break;
			}

			case IDENTIFIER:
			{
				Identifier identifier=parseIdentifier(); //Gets and accepts the identifier

				switch(currentToken.getType())
				{
					case LBRACKET:
					{
						acceptIt();

						if(currentToken.getType()!=TokenType.RBRACKET)
						{
							Expression indexExpression=parseExpression();
							
							accept(TokenType.RBRACKET);
							accept(TokenType.EQUALS);
							
							Expression rightSideExpression=parseExpression();
							accept(TokenType.SEMICOLON);
							
							Reference r=new IndexedRef(new QualifiedRef(identifier), indexExpression, identifier.posn);
							statement=new AssignStmt(r, rightSideExpression, r.posn);
						}
	
						else
						{
							accept(TokenType.RBRACKET);
							Identifier name=parseIdentifier();
							
							accept(TokenType.EQUALS);
							Expression rightSideExpression=parseExpression();
							
							accept(TokenType.SEMICOLON);
							
							Type varType=new ArrayType(new ClassType(identifier.spelling, identifier.posn), identifier.posn);
							VarDecl varDecl=new VarDecl(varType, name.spelling, varType.posn);
							
							statement=new VarDeclStmt(varDecl, rightSideExpression, varDecl.posn);
						}
						
						break;
					}

					case IDENTIFIER:
					{
						Identifier name=parseIdentifier(); //Gets and accepts the identifier

						accept(TokenType.EQUALS);
						Expression rightSideExpression=parseExpression();
						accept(TokenType.SEMICOLON);
						
						VarDecl varDecl=new VarDecl(new ClassType(identifier.spelling, identifier.posn), name.spelling, name.posn);
						statement=new VarDeclStmt(varDecl, rightSideExpression, varDecl.posn);
						
						break;
					}

					case LPAREN:
					{
						acceptIt();
						ExprList argList=new ExprList();
						
						if(currentToken.getType()!=TokenType.RPAREN)
							argList=parseArgumentList();
	
						accept(TokenType.RPAREN);
						accept(TokenType.SEMICOLON);
						
						statement=new CallStmt(new QualifiedRef(identifier), argList, identifier.posn);
						break;
					}
	
					case PERIOD:
					{
						IdentifierList identifierList=new IdentifierList();
						identifierList.add(identifier); //Include first identifier in list
						
						do
						{
							acceptIt();
							identifierList.add(parseIdentifier()); //Gets and accepts the identifier
						}
						while(currentToken.getType()==TokenType.PERIOD);
						
						switch(currentToken.getType())
						{
							case LPAREN:
							{
								acceptIt();
								ExprList args=new ExprList();
								
								if(currentToken.getType()!=TokenType.RPAREN)
									args=parseArgumentList();
		
								accept(TokenType.RPAREN);
								accept(TokenType.SEMICOLON);
								
								statement=new CallStmt(new QualifiedRef(false, identifierList, identifierList.get(0).posn), args, identifierList.get(0).posn);
								break;
							}
		
							case LBRACKET:
							{
								acceptIt();
								Expression indexExpression=parseExpression();
								
								accept(TokenType.RBRACKET);
								accept(TokenType.EQUALS);
								
								Expression rightSideExpression=parseExpression();
								accept(TokenType.SEMICOLON);
								
								Reference r=new IndexedRef(new QualifiedRef(false, identifierList, identifierList.get(0).posn), indexExpression, identifierList.get(0).posn);
								statement=new AssignStmt(r, rightSideExpression, r.posn);
								
								break;
							}
	
							case EQUALS:
							{
								acceptIt();
								Expression rightSideExpression=parseExpression();
								
								accept(TokenType.SEMICOLON);
								statement=new AssignStmt(new QualifiedRef(false, identifierList, identifierList.get(0).posn), rightSideExpression, identifierList.get(0).posn);
								
								break;
							}
	
							default:
								parseError("Error parsing reference statement!");
						}
						
						break;
					}

					case EQUALS:
					{
						acceptIt();
						Expression rightSideExpression=parseExpression();
						
						accept(TokenType.SEMICOLON);
						statement=new AssignStmt(new QualifiedRef(identifier), rightSideExpression, identifier.posn);
						
						break;
					}

					default:
						parseError("Error parsing single statement!");
				}

				break;
			}

			case THIS:
			{
				SourcePosition thisSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				acceptIt();
				IdentifierList identifierList=new IdentifierList();
				
				while(currentToken.getType()==TokenType.PERIOD)
				{
					acceptIt();
					identifierList.add(parseIdentifier());
				}

				switch(currentToken.getType())
				{
					case LPAREN:
					{
						acceptIt();
						ExprList args=new ExprList();
						
						if(currentToken.getType()!=TokenType.RPAREN)
							args=parseArgumentList();
	
						accept(TokenType.RPAREN);
						accept(TokenType.SEMICOLON);
	
						statement=new CallStmt(new QualifiedRef(true, identifierList, thisSourcePosition), args, thisSourcePosition);
						break;
					}
	
					case LBRACKET:
					{
						acceptIt();
						Expression indexExpression=parseExpression();
						
						accept(TokenType.RBRACKET);
						accept(TokenType.EQUALS);
						
						Expression rightSideExpression=parseExpression();
						accept(TokenType.SEMICOLON);
	
						Reference r=new IndexedRef(new QualifiedRef(true, identifierList, thisSourcePosition), indexExpression, thisSourcePosition);
						statement=new AssignStmt(r, rightSideExpression, r.posn);
						
						break;
					}
	
					case EQUALS:
					{
						accept(TokenType.EQUALS);
						Expression rightSideExpression=parseExpression();
						accept(TokenType.SEMICOLON);
	
						statement=new AssignStmt(new QualifiedRef(true, identifierList, thisSourcePosition), rightSideExpression, thisSourcePosition);
						break;
					}
	
					default:
						parseError("Error parsing \"this\" reference!");
				}

				break;
			}
				
			default:
				parseError("Error parsing statement!");
		}
		
		return statement;
	}
	
	private Expression parseExpression()
	{
		Expression expression=parseOrTerm();
		
		while(currentToken.getSpelling().equals("||"))
		{
			Token operator=currentToken;
			SourcePosition operatorSourcePosition=new SourcePosition(operator.getSourcePosition().getLineNumber(), operator.getSourcePosition().getColumnNumber());
			
			acceptIt();
			
			Expression expression2=parseOrTerm();
			expression=new BinaryExpr(new Operator(operator.getSpelling(), operatorSourcePosition), expression, expression2, expression.posn);
		}
		
		return expression;
	}
	
	private Expression parseOrTerm()
	{
		Expression expression=parseAndTerm();
		
		while(currentToken.getSpelling().equals("&&"))
		{
			Token operator=currentToken;
			SourcePosition operatorSourcePosition=new SourcePosition(operator.getSourcePosition().getLineNumber(), operator.getSourcePosition().getColumnNumber());
			
			acceptIt();
			
			Expression expression2=parseAndTerm();
			expression=new BinaryExpr(new Operator(operator.getSpelling(), operatorSourcePosition), expression, expression2, expression.posn);
		}
		
		return expression;
	}
	
	private Expression parseAndTerm()
	{
		Expression expression=parseEqualityTerm();
		
		while(currentToken.getSpelling().equals("==") || currentToken.getSpelling().equals("!="))
		{
			Token operator=currentToken;
			SourcePosition operatorSourcePosition=new SourcePosition(operator.getSourcePosition().getLineNumber(), operator.getSourcePosition().getColumnNumber());
			
			acceptIt();
			
			Expression expression2=parseEqualityTerm();
			expression=new BinaryExpr(new Operator(operator.getSpelling(), operatorSourcePosition), expression, expression2, expression.posn);
		}
		
		return expression;
	}
	
	private Expression parseEqualityTerm()
	{
		Expression expression=parseComparisonTerm();
		
		while(currentToken.getSpelling().equals("<=") || currentToken.getSpelling().equals("<") || currentToken.getSpelling().equals(">=") || currentToken.getSpelling().equals(">"))
		{
			Token operator=currentToken;
			SourcePosition operatorSourcePosition=new SourcePosition(operator.getSourcePosition().getLineNumber(), operator.getSourcePosition().getColumnNumber());
			
			acceptIt();
			
			Expression expression2=parseComparisonTerm();
			expression=new BinaryExpr(new Operator(operator.getSpelling(), operatorSourcePosition), expression, expression2, expression.posn);
		}
		
		return expression;
	}
	
	private Expression parseComparisonTerm()
	{
		Expression expression=parseAdditiveTerm();
		
		while(currentToken.getSpelling().equals("+") || (currentToken.getSpelling().equals("-") && currentToken.getType()!=TokenType.ERROR))
		{
			Token operator=currentToken;
			SourcePosition operatorSourcePosition=new SourcePosition(operator.getSourcePosition().getLineNumber(), operator.getSourcePosition().getColumnNumber());
			
			acceptIt();
			
			Expression expression2=parseAdditiveTerm();
			expression=new BinaryExpr(new Operator(operator.getSpelling(), operatorSourcePosition), expression, expression2, expression.posn);
		}
		
		return expression;
	}
	
	private Expression parseAdditiveTerm()
	{
		Expression expression=parseMultiplicativeTerm();
		
		while(currentToken.getSpelling().equals("*") || currentToken.getSpelling().equals("/"))
		{
			Token operator=currentToken;
			SourcePosition operatorSourcePosition=new SourcePosition(operator.getSourcePosition().getLineNumber(), operator.getSourcePosition().getColumnNumber());
			
			acceptIt();
			
			Expression expression2=parseMultiplicativeTerm();
			expression=new BinaryExpr(new Operator(operator.getSpelling(), operatorSourcePosition), expression, expression2, expression.posn);
		}
		
		return expression;
	}
	
	private Expression parseMultiplicativeTerm()
	{
		Expression expression=null;
		
		if(currentToken.getSpelling().equals("!") || (currentToken.getSpelling().equals("-") && currentToken.getType()!=TokenType.ERROR))
		{
			Token unaryOperator=currentToken;
			SourcePosition unaryOperatorPosition=new SourcePosition(unaryOperator.getSourcePosition().getLineNumber(), unaryOperator.getSourcePosition().getColumnNumber());
			
			acceptIt();
			
			Expression unaryExpression=parseUnaryTerm();
			expression=new UnaryExpr(new Operator(unaryOperator.getSpelling(), unaryOperatorPosition), unaryExpression, unaryOperatorPosition);
		}
		
		else
			expression=parseBaseTerm();
		
		return expression;
	}
	
	private Expression parseUnaryTerm()
	{
		Expression expression=null;
		
		if(currentToken.getType()==TokenType.UNOP || currentToken.getType()==TokenType.MINUS)
		{
			String operatorSpelling=currentToken.getSpelling();
			SourcePosition unaryOperatorPosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
			
			acceptIt();
			Expression unaryExpression=parseUnaryTerm();
		
			expression=new UnaryExpr(new Operator(operatorSpelling, unaryOperatorPosition), unaryExpression, unaryOperatorPosition);
		}
		
		else
			expression=parseBaseTerm();
		
		return expression;
	}
	
	private Expression parseBaseTerm()
	{
		Expression expression=null;
		
		switch(currentToken.getType())
		{
			case THIS: case IDENTIFIER:
			{
				SourcePosition thisIdentifierPosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				boolean thisRelative=(currentToken.getType()==TokenType.THIS);
			
				IdentifierList identifierList=new IdentifierList();
				Identifier firstIdentifier=null;
			
				if(currentToken.getType()==TokenType.IDENTIFIER)
				{
					firstIdentifier=parseIdentifier();
					identifierList.add(firstIdentifier); //Include first identifier in list
				}
			
				else
					acceptIt(); //Accept "this" keyword if not dealing with identifier
			
				while(currentToken.getType()==TokenType.PERIOD)
				{
					acceptIt();
					identifierList.add(parseIdentifier()); //Gets and accepts the identifier
				}
			
				switch(currentToken.getType())
				{
					case LPAREN:
					{
						acceptIt();
						ExprList args=new ExprList();
					
						if(currentToken.getType()!=TokenType.RPAREN)
							args=parseArgumentList();
					
						accept(TokenType.RPAREN);
						expression=new CallExpr(new QualifiedRef(thisRelative, identifierList, (thisRelative ? thisIdentifierPosition : identifierList.get(0).posn)), args, (thisRelative ? thisIdentifierPosition : identifierList.get(0).posn));
					
						break;
					}
				
					case LBRACKET:
					{
						acceptIt();
						Expression indexExpression=parseExpression();
						accept(TokenType.RBRACKET);
					
						Reference qr=new QualifiedRef(thisRelative, identifierList, (thisRelative ? thisIdentifierPosition : identifierList.get(0).posn));
						expression=new RefExpr(new IndexedRef(qr, indexExpression, qr.posn), qr.posn);
					
						break;
					}
				
					default:
					{
						expression=new RefExpr(new QualifiedRef(thisRelative, identifierList, (thisRelative ? thisIdentifierPosition : identifierList.get(0).posn)), (thisRelative ? thisIdentifierPosition : identifierList.get(0).posn));
						break;
					}		
				}
			
				break;
			}
			
			case LPAREN:
			{
				acceptIt();
				expression=parseExpression();
				accept(TokenType.RPAREN);

				break;
			}

			case NEW:
			{
				SourcePosition newSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				acceptIt();

				switch(currentToken.getType())
				{
					case INT:
					{
						SourcePosition intSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
						
						acceptIt();
						accept(TokenType.LBRACKET);

						Expression indexExpression=parseExpression();
						accept(TokenType.RBRACKET);

						expression=new NewArrayExpr(new BaseType(TypeKind.INT, intSourcePosition), indexExpression, newSourcePosition);
						break;
					}

					case IDENTIFIER:
					{
						Identifier className=parseIdentifier();

						switch(currentToken.getType())
						{
							case LPAREN:
							{
								acceptIt();
								accept(TokenType.RPAREN);

								expression=new NewObjectExpr(new ClassType(className.spelling, className.posn), newSourcePosition);
								break;
							}

							case LBRACKET:
							{
								acceptIt();
								Expression indexExpression=parseExpression();

								accept(TokenType.RBRACKET);

								expression=new NewArrayExpr(new ClassType(className.spelling, className.posn), indexExpression, newSourcePosition);
								break;
							}

							default:
								parseError("Error in \"id\" construct");
						}

						break;
					}

					default:
						parseError("Incorrect type for \"new()\" construct!");
				}
				
				break;
			}

			case NUM: case TRUE: case FALSE:
			{
				SourcePosition literalSourcePosition=new SourcePosition(currentSourcePosition.getLineNumber(), currentSourcePosition.getColumnNumber());
				
				expression=new LiteralExpr(currentToken.getType()==TokenType.NUM ? new IntLiteral(currentToken.getSpelling(), literalSourcePosition) : new BooleanLiteral(currentToken.getSpelling(), literalSourcePosition), literalSourcePosition);
				acceptIt();

				break;
			}

			default:
				parseError("Invalid Expression!");
		}

		return expression;
	}
	
	private void acceptIt()
	{
		accept(currentToken.getType());
	}
	
	private void accept(TokenType tokenExpected)
	{
		if(this.currentToken.getType()==tokenExpected)
		{
			System.out.println("Accepting '"+currentToken+"'");
			
			do
			{
				currentToken=this.sc.scan();
				currentSourcePosition=currentToken.getSourcePosition();
			}
			while(currentToken.getType()!=TokenType.EOT && currentToken.getType()==TokenType.COMMENT);
		}
		
		else
			parseError("Expected '"+tokenExpected+"' but got '"+currentToken+"'");
	}
	
	private void parseError(String errorText)
	{
		System.out.println(errorText);
		System.exit(4);
	}
}
