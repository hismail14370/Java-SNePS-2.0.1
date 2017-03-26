package sneps.nlp;

import edu.stanford.nlp.ling.IndexedWord;

public class Quantifiers {

	String antecedent;
	String antecode = "X";
	Event consequent;

	static int counter = 0;

	public Quantifiers(IndexedWord ante, Event event) {
		this.antecode += + ++counter;
		this.antecedent = ante.lemma() + "(" + antecode + ")";
		this.consequent = event;
	}

}
