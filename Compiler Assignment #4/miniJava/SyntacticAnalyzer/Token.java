package miniJava.SyntacticAnalyzer;

public class Token {

	private TokenType type;
	private String spelling;
	private SourcePosition posn;
	
	public Token(TokenType type, String spelling, SourcePosition posn)
	{
		this.type=type;
		this.spelling=spelling;
		this.posn=posn;
	}
	
	public String toString()
	{
		return this.type.toString()+((this.spelling.length()==0) ? "" : "["+this.spelling+"]");
	}
	
	public TokenType getType()
	{
		return this.type;
	}
	
	public String getSpelling()
	{
		return this.spelling;
	}
	
	public SourcePosition getSourcePosition()
	{
		return this.posn;
	}
}
