package sneps.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import java.util.ArrayList;

public class Noun {

	String name;
	String id;
	String det = "";
	String prepostion;
	String term = "";
	// int index;
	boolean collection;
	boolean antecednt;
	boolean property;
	boolean ner = true;
	// boolean relcl;
	// boolean amod;
	String collId = "C";
	ArrayList<Noun> amods = new ArrayList<Noun>();
	static int collCounter = 0;

	public Noun(IndexedWord word, String det) {
		this.name = word.lemma();
		// this.term = this.name;
		this.id = name.substring(0, 1);
		if (word.ner().equals("O")) {
			ner = false;
			this.det = det;
			this.term = this.name + "(" + this.id + ")";
		}
		if (word.tag().equals("NNS") && det.equals("the")) {
			collection = true;
			this.collId += "" + ++collCounter;
			this.term = "all(" + this.id + ")(Member(" + this.id + ","
					+ this.collId + ")=>" + this.term + ")";
		}
		if (word.tag().equals("NNS") && det.equals("") || det.equals("all")
				|| det.equals("every")) {
			antecednt = true;
		}

	}

	public void setAdjectives() {
		for (int i = 0; i < amods.size(); i++) {
			if (ner)
				this.term += amods.get(i).name + "(" + this.name + ")";

			else if (collection)
				this.term += " and " + amods.get(i).name + "(" + this.collId
						+ ")";
			else
				this.term += " and " + amods.get(i).name + "(" + this.id + ")";
		}

	}


}
