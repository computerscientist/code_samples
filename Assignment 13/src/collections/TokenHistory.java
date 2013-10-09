package collections;

import tokens.Token;

/**
 * An interface used as a type for all token collection objects that are histories
 * @author Robert Dallara
 *
 */
public interface TokenHistory {

	Token elementAt(int index);
	void addElement(Token token);
	
	int size();
	int maxSize();
}
