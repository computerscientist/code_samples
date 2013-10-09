package processing;

/**
 * An interface used as a type for all command parser objects
 * @author Robert Dallara
 *
 */
public interface Parser {

	String getSentence();
	void setSentence(String sentence);
}
