package miniJava.SyntacticAnalyzer;

public class SourcePosition {

	private int lineNumber;
	private int columnNumber;
	
	public SourcePosition(int lineNumber, int columnNumber)
	{
		this.lineNumber=lineNumber;
		this.columnNumber=columnNumber;
	}
	
	public int getLineNumber()
	{
		return this.lineNumber;
	}
	
	public int getColumnNumber()
	{
		return this.columnNumber;
	}
	
	public String toString()
	{
		return this.lineNumber+":"+this.columnNumber;
	}
}
