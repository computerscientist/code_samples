package tutorial;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.*;
import java.util.*;

public class PhraseLabeler {

	public static void main(String[] args)
	{
		Scanner scanner=null;
		
		try
		{
			FileReader reader=new FileReader("short_grammatical_phrases_to_look_for.txt");
			MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
			scanner=new Scanner(reader);
			
			while(scanner.hasNextLine())
			{
				String nextPhrase=scanner.nextLine();
				System.out.println(tagger.tagString(nextPhrase));
			}
		}
		
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			if(scanner!=null)
				scanner.close();
		}
	}
}
