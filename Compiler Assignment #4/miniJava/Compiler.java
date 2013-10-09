package miniJava;

import java.io.*;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.ContextualAnalyzer.*;
import miniJava.CodeGenerator.*;
import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.Scanner;

public class Compiler {

	/**
	 * Tries to compile a java file into miniJava
	 * @param args the file to try and compile
	 */
	public static void main(String[] args)
	{
		try
		{
			InputStream inputStream=new FileInputStream(args[0]);
			
			if(!args[0].endsWith(".java") && !args[0].endsWith(".mjava"))
			{
				System.out.println("Incorrect file extension!");
				System.exit(4);
			}

			Scanner scanner=new Scanner(inputStream);
			Parser parser=new Parser(scanner);
			
			AST ast=parser.parse();
			System.out.println("Successful program parse!\n\n");
			
	        ASTDisplay display = new ASTDisplay();
	        display.showTree(ast);
	        
	        TypeChecker checker=new TypeChecker();
	        checker.checkTree(ast);

	        Encoder encoder=new Encoder();
	        encoder.compileAST(ast, args[0].substring(0, args[0].lastIndexOf('.'))+".mJAM");
	        
			System.exit(0);
		}
		
		catch(FileNotFoundException e)
		{
			System.out.println("File not found!");
		}
		
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Missing file name parameter!");
		}
	}
}
