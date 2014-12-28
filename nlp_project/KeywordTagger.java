package tutorial;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class KeywordTagger {

	public static void main(String[] args)
	{
		MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
		
		String sample = "right rights liberty liberties freedom freedoms choice choices worker workers worker's work working employee employees employee's woman woman's women women's female female's females females' girl girl's girls girls' environment forest forests resource resources habitat habitats land lands river rivers lake lakes regulate regulation regulates regulations rule rules control controls oversight oversee oversees monitor monitors business businesses company companies corporation corporations enterprise enterprises criminal criminal's criminals crime crimes accusedoffense offenses offender offender's offenders prayer prayers pray prays praying school schools segregate segregates segregation separate separates separation separations divide divides division divisions race races color colors creed creeds racial ethnic ethnicity ethnicities equal equality equivalent equivalence gay homosexual homosexuality national nationwide federal nation nation's interest interests desire desires goal goals matter matters concern concerns significant significance want wants security secure safety safe protect protects protection protections safeguard safeguards shield shields defend defends defense surveillance guard guarding guards vote votes voting suffrage poll polls ballot ballots privacy private confidential confidentiality search searches searching seize seizes seizing seizure right rights liberty liberties freedom freedoms choice choices speech language voice utterance vocalization pay pays paying compensation compensations income incomes wage wages";
		String tagged = tagger.tagString(sample);
		System.out.println(tagged);
	}
}