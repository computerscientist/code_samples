package miniJava.ContextualAnalyzer;

import miniJava.AbstractSyntaxTrees.*;

public class IdentificationTable {

	public int lastScopeLevelRetrieved;
	private int currentLevel;
	
	boolean userSystemClassAdded;
	TableEntry lastEntry;
	
	public IdentificationTable()
	{
		this.currentLevel=0;
		this.lastScopeLevelRetrieved=0;
		
		this.userSystemClassAdded=false; //Whether or not the user has also declared a class with the name "System"
		this.lastEntry=null;
	}
	
	public void openScope()
	{
		this.currentLevel++;
	}
	
	public void closeScope()
	{
		while(lastEntry!=null && lastEntry.level==this.currentLevel)
			lastEntry=lastEntry.previousEntry;
		
		this.currentLevel--;
	}
	
	public void enter(String id, Declaration declaration, boolean isInitiallyNull)
	{
		boolean alreadyThere=false;
		TableEntry currentEntry=lastEntry;
		
		//Make sure entry isn't at wrong scope level
		if(currentLevel>=2)
		{
			while(currentEntry!=null)
			{
				//Don't worry about similar id's at a lower scope level
				if(currentEntry.level==1)
					break;
				
				else if(currentEntry.id.equals(id))
				{
					alreadyThere=true;
					break;
				}
				
				else
					currentEntry=currentEntry.previousEntry;
			}
		}
		
		//Case for checking field declarations (whose names can be same as class names)
		else if(currentLevel==1)
		{
			while(currentEntry!=null)
			{
				//Don't worry about similar id's at a lower scope level
				if(currentEntry.level==0)
					break;
				
				else if(currentEntry.id.equals(id))
				{
					alreadyThere=true;
					break;
				}
				
				else
					currentEntry=currentEntry.previousEntry;
			}
		}
		
		//Case for checking for identical class names
		else
		{
			while(currentEntry!=null)
			{
				if(currentEntry.id.equals(id))
				{
					if(currentEntry.id.equals("System") && !userSystemClassAdded)
					{
						userSystemClassAdded=true;
						
						TableEntry newEntry=new TableEntry(id, declaration, currentLevel, isInitiallyNull, this.lastEntry);
						this.lastEntry=newEntry;
						
						return;
					}
					
					else
					{
						alreadyThere=true;
						break;
					}
				}
				
				else
					currentEntry=currentEntry.previousEntry;
			}
		}
		
		if(!alreadyThere)
		{
			TableEntry newEntry=new TableEntry(id, declaration, currentLevel, isInitiallyNull, this.lastEntry);
			this.lastEntry=newEntry;
		}
		
		else
		{
			TypeChecker.duplicateFound=true;
			TypeChecker.errorFound=true;
			
			System.out.println("***Error: "+id+" attribute already exists in its current scope");
		}
	}
	
	public Declaration retrieve(String id)
	{
		TableEntry currentEntry=this.lastEntry;
		Declaration declaration=null;
		
		while(currentEntry!=null)
		{
			if(currentEntry.isNull && currentEntry.id.equals(id))
			{
				TypeChecker.errorFound=true;
				System.out.println("***Error: identifier "+id+" being retrieved is unusable");
				
				break;
			}
			
			else if(currentEntry.id.equals(id))
			{
				declaration=currentEntry.declaration;
				lastScopeLevelRetrieved=currentEntry.level;
				
				break;
			}
			
			else
				currentEntry=currentEntry.previousEntry;
		}
		
		return declaration;
	}
	
	//Designed to retrieve variables from table (and nothing else)
	public Declaration retrieveVariable(String id)
	{
		TableEntry currentEntry=this.lastEntry;
		Declaration declaration=null;
		
		while(currentEntry!=null)
		{
			if(currentEntry.level==0)
				break;
			
			else if(currentEntry.isNull && currentEntry.id.equals(id))
			{
				TypeChecker.errorFound=true;
				System.out.println("***Error: variable "+id+" being retrieved is unusable");
				
				break;
			}
			
			else if(currentEntry.id.equals(id))
			{
				declaration=currentEntry.declaration;
				
				if(declaration instanceof MethodDecl)
					declaration=null;
				
				else
					lastScopeLevelRetrieved=currentEntry.level;
				
				break;
			}
			
			else
				currentEntry=currentEntry.previousEntry;
		}
		
		return declaration;
	}
	
	//Specifically designed to retrieve class declaration (which is at scope level 0)
	public Declaration retrieveClass(String cn)
	{
		TableEntry currentEntry=this.lastEntry;
		Declaration declaration=null;
		
		while(currentEntry!=null)
		{
			if(currentEntry.level==0 && currentEntry.isNull && currentEntry.id.equals(cn))
			{
				TypeChecker.errorFound=true;
				System.out.println("***Error: class "+cn+" being retrieved is unusable");
				
				break;
			}
			
			else if(currentEntry.level==0 && currentEntry.id.equals(cn))
			{
				declaration=currentEntry.declaration;
				lastScopeLevelRetrieved=currentEntry.level;
				
				break;
			}
			
			else
				currentEntry=currentEntry.previousEntry;
		}
		
		return declaration;
	}
	
	//Specifically designed to retrieve method declaration (which is at scope level 1)
	public Declaration retrieveMethod(String mn)
	{
		TableEntry currentEntry=this.lastEntry;
		Declaration declaration=null;

		while(currentEntry!=null)
		{
			if(currentEntry.level==0)
				break;
			
			else if(currentEntry.level==1 && currentEntry.isNull && currentEntry.id.equals(mn))
			{
				TypeChecker.errorFound=true;
				System.out.println("***Error: method "+mn+" being retrieved is unusable");
				
				break;
			}
			
			else if(currentEntry.level==1 && currentEntry.id.equals(mn))
			{
				declaration=currentEntry.declaration;
				
				if(!(declaration instanceof MethodDecl))
					declaration=null;
				
				else
					lastScopeLevelRetrieved=currentEntry.level;

				break;
			}

			else
				currentEntry=currentEntry.previousEntry;
		}

		return declaration;
	}
}
