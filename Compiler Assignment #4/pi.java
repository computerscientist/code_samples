class Pi
{
	public static void main(String[] args)
	{
		PiCalc piCalculator=new PiCalc();
		piCalculator.calculateDigits();
	}
}

class PiCalc
{
	int x;
	
	int digits;
	int quotient;
	
	int size;
	int[] numbers;
	
	int[] digitsOfPi;
	
	public void calculateDigits()
	{
		digits=221;
		size=(digits*10)/3;
		
		numbers=new int[size];
		digitsOfPi=new int[digits];
		
		int i=0;
		
		while(i<numbers.length)
		{
			numbers[i]=2;
			i=i+1;
		}
		
		int j=0;
		
		while(j<digits)
		{
			quotient=0;
			i=size-1;
			
			while(i>=0)
			{
				x=10*numbers[i]+quotient*(i+1);
				numbers[i]=mod(x, (2*(i+1)-1));
				quotient=(x/(2*(i+1)-1));
				
				i=i-1;
			}
			
			numbers[0]=mod(quotient, 10);
			quotient=(quotient/10);
			
			digitsOfPi[j]=quotient;
			j=j+1;
		}
		
		if(digits>1)
		{
			int a=1;
			
			while(a<digitsOfPi.length)
			{
				int k=a;
				
				while(digitsOfPi[k]>=10)
				{
					digitsOfPi[k]=digitsOfPi[k]-10;
					digitsOfPi[k-1]=digitsOfPi[k-1]+1;
					
					k=k-1;
				}
				
				a=a+1;
			}
		}
		
		i=0;
		
		while(i<digitsOfPi.length)
		{
			System.out.println(digitsOfPi[i]);
			i=i+1;
		}
	}

	
    	int mod(int a, int b)
	{
        	return a - (a/b)*b;
    	}
}