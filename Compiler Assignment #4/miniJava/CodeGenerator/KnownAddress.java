package miniJava.CodeGenerator;

public class KnownAddress extends RuntimeEntity {

	public int address;
	
	public KnownAddress(int size, int address)
	{
		super(size);
		this.address=address;
	}
	
	public String toString()
	{
		return "KnownAddress[size="+this.size+", address="+this.address+"]";
	}
}
