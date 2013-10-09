package processing;

/**
 * Used to create list processor objects that scan through sentences and classify
 * each of their words
 * @author Robert Dallara
 *
 */
public interface ListProcessor {

	String analyzeSentenceParts(String sentence);
}
