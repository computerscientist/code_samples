package miniJava.SyntacticAnalyzer;

import java.io.*;

public class Scanner {

	boolean endOfText=false;
	boolean newLineJustAppeared=false;
	boolean returnJustAppeared=false;
	
	char currentChar;
	InputStream input;
	
	int currentLine;
	int currentColumn;
	
	public Scanner(InputStream input)
	{
		this.currentLine=1;
		this.currentColumn=0;
		
		this.input=input;
		takeIt(); //Get first character
	}
	
	public Token scan()
	{
		while(!endOfText && isWhitespace(currentChar))
			takeIt();
		
		SourcePosition posn=new SourcePosition(currentLine, currentColumn);
		
		TokenType type=TokenType.EOT;
		String spelling="";
		
		if(!endOfText)
		{
			switch(currentChar)
			{
				case 'a':  case 'b':  case 'c':  case 'd':  case 'e':
			    case 'f':  case 'g':  case 'h':  case 'i':  case 'j':
			    case 'k':  case 'l':  case 'm':  case 'n':  case 'o':
			    case 'p':  case 'q':  case 'r':  case 's':  case 't':
			    case 'u':  case 'v':  case 'w':  case 'x':  case 'y':
			    case 'z':
			    case 'A':  case 'B':  case 'C':  case 'D':  case 'E':
			    case 'F':  case 'G':  case 'H':  case 'I':  case 'J':
			    case 'K':  case 'L':  case 'M':  case 'N':  case 'O':
			    case 'P':  case 'Q':  case 'R':  case 'S':  case 'T':
			    case 'U':  case 'V':  case 'W':  case 'X':  case 'Y':
			    case 'Z':
			    {
			    	do
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    	}
			    	while(!endOfText && (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar=='_'));
			    	
			    	if(spelling.equals("class"))
			    		type=TokenType.CLASS;
			    	
			    	else if(spelling.equals("return"))
			    		type=TokenType.RETURN;
			    	
			    	else if(spelling.equals("public"))
			    		type=TokenType.PUBLIC;
			    	
			    	else if(spelling.equals("private"))
			    		type=TokenType.PRIVATE;
			    	
			    	else if(spelling.equals("static"))
			    		type=TokenType.STATIC;
			    	
			    	else if(spelling.equals("int"))
			    		type=TokenType.INT;
			    	
			    	else if(spelling.equals("boolean"))
			    		type=TokenType.BOOLEAN;
			    	
			    	else if(spelling.equals("void"))
			    		type=TokenType.VOID;
			    	
			    	else if(spelling.equals("if"))
			    		type=TokenType.IF;
			    	
			    	else if(spelling.equals("else"))
			    		type=TokenType.ELSE;
			    	
			    	else if(spelling.equals("while"))
			    		type=TokenType.WHILE;
			    	
			    	else if(spelling.equals("new"))
			    		type=TokenType.NEW;
			    	
			    	else if(spelling.equals("this"))
			    		type=TokenType.THIS;
			    	
			    	else if(spelling.equals("true"))
			    		type=TokenType.TRUE;
			    	
			    	else if(spelling.equals("false"))
			    		type=TokenType.FALSE;
			    	
			    	else
			    		type=TokenType.IDENTIFIER;
			    	
			    	break;
			    }
			    
			    case '0':  case '1':  case '2':  case '3':  case '4':
			    case '5':  case '6':  case '7':  case '8':  case '9':
			    {
			    	do
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    	}
			    	while(!endOfText && Character.isDigit(currentChar));
			    	
			    	type=TokenType.NUM;
			    	break;
			    }
			    
			    case '!':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	if(currentChar=='=')
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    		
			    		type=TokenType.BINOP;
			    	}
			    	
			    	else
			    		type=TokenType.UNOP;
			    	
			    	break;
			    }
			    
			    case '-':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	if(currentChar=='-' && !endOfText)
			    		type=TokenType.ERROR; //"--" expressions not allowed
			    	
			    	else
			    		type=TokenType.MINUS;
			    	
			    	break;
			    }
			    
			    case '<': case '>':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	if(currentChar=='=')
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    	}
			    	
			    	type=TokenType.BINOP;
			    	break;
			    }
			    
			    case '=':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	if(currentChar=='=' && !endOfText)
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    		
			    		type=TokenType.BINOP;
			    	}
			    	
			    	else
			    		type=TokenType.EQUALS;
			    	
			    	break;
			    }
			    
			    case '&':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	if(currentChar!='&' || endOfText)
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    		
			    		type=TokenType.ERROR;
			    	}
			    	
			    	else
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    		
			    		type=TokenType.BINOP;
			    	}
			    	
			    	break;
			    }
			    
			    case '|':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	if(currentChar!='|' || endOfText)
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    		
			    		type=TokenType.ERROR;
			    	}
			    	
			    	else
			    	{
			    		spelling+=currentChar;
			    		takeIt();
			    		
			    		type=TokenType.BINOP;
			    	}
			    	
			    	break;
			    }
			    
			    case '/':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	if(currentChar=='/' && !endOfText)
			    	{
			    		do
			    		{
							takeIt();
			    		}
			    		while(!endOfText && currentChar!='\n' && currentChar!='\r');
			    		
						type=TokenType.COMMENT;
			    	}
			    	
			    	else if(currentChar=='*')
			    	{
			    		takeIt();
			    		
			    		while(!endOfText)
						{
							while(!endOfText && currentChar!='*')
								takeIt();
							
							takeIt();
							
							if(currentChar=='/')
							{
								takeIt();
								break;
							}
						}
			    		
			    		if(endOfText)
			    			type=TokenType.ERROR; //Multi-line comments must eventually end
			    		
			    		else
			    			type=TokenType.COMMENT;
			    	}
			    	
			    	else
			    		type=TokenType.BINOP;
			    	
			    	break;
			    }
			    
			    case '+': case '*':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.BINOP;
			    	break;
			    }
			    
			    case '[':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.LBRACKET;
			    	break;
			    }
			    
			    case ']':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.RBRACKET;
			    	break;
			    }
			    
			    case '{':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.LBRACE;
			    	break;
			    }
			    
			    case '}':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.RBRACE;
			    	break;
			    }
			    
			    case '(':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.LPAREN;
			    	break;
			    }
			    
			    case ')':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.RPAREN;
			    	break;
			    }
			    
			    case ';':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.SEMICOLON;
			    	break;
			    }
			    
			    case '.':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.PERIOD;
			    	break;
			    }
			    
			    case ',':
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.COMMA;
			    	break;
			    }
			    
			    default:
			    {
			    	spelling+=currentChar;
			    	takeIt();
			    	
			    	type=TokenType.ERROR;
			    	break;
			    }
			}
		}
		
		Token t=new Token(type, spelling, posn);
		return t;
	}
	
	private void takeIt()
	{
		try
		{
			int readValue=this.input.read();
			
			if(readValue<0)
				endOfText=true;
			
			else
			{
				currentChar=(char) readValue;
				
				if((currentChar=='\n' || currentChar=='\r'))
				{
					if((currentChar=='\n' && returnJustAppeared) || (currentChar=='\r' && newLineJustAppeared))
						currentLine++;
					
					currentColumn=0;
					
					if(currentChar=='\n')
					{
						newLineJustAppeared=true;
						returnJustAppeared=false;
					}
					
					else if(currentChar=='\r')
					{
						returnJustAppeared=true;
						newLineJustAppeared=false;
					}
				}
				
				else
				{
					returnJustAppeared=false;
					newLineJustAppeared=false;
					
					if(currentChar=='\t')
						currentColumn+=5;
				
					else
						currentColumn++;
				}
			}
		}
		
		catch(IOException e)
		{
			System.out.println("Error reading from scanner input!");
			System.exit(4);
		}
	}
	
	private boolean isWhitespace(char c)
	{
		return c==' ' || c=='\t' || c=='\n' || c=='\r';
	}
}
