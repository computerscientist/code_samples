package miniJava.CodeGenerator;

public class UnknownValue extends RuntimeEntity {

	public int index;
	
	public UnknownValue(int size, int index)
	{
		super(size);
		this.index=index;
	}
	
	public String toString()
	{
		return "UnknownValue[size="+this.size+", index="+this.index+"]";
	}
}
