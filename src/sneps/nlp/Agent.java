package sneps.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import java.util.ArrayList;

public class Agent {
	String rname;
	String name;
	String agent;
	String term;
	String code;
	String adj = "";
	String collection;
	String collCode = "C";
	String collHead;
	String scopeSymbol;
	boolean ner = false;
	boolean relcl = false;
	ArrayList<String> adjs = new ArrayList<String>();
	Event e;
	static int collcounter = 0;
	static int counter = 0;

	public Agent(IndexedWord name, Event e) {
		this.rname = "" + name.lemma();
		if (name.ner().equals("O")) {
			this.e = e;
			this.code = "A" + "" + ++counter;
			this.name = name.lemma() + "(" + this.code + ")";
			this.agent = "^Agent(" + this.e.code + "," + this.code + ")";
			this.term = this.name + this.agent;
		} else {
			this.ner = true;
			this.name = "" + name.lemma();
			this.e = e;
			this.code = "" + name.lemma();
			this.term = "Agent(" + this.e.code + "," + name.lemma() + ")";

		}
		if (name.tag().equals("NNS")) {
			this.e.agentCollection = true;
			this.collCode += "" + ++collcounter;
			this.collHead = "All(" + this.code + ")^(Member(" + this.code + ","
					+ this.collCode + "))=>";
			this.name = name.lemma() + "(" + this.code + ")";
			this.agent = "^Agent(" + this.e.code + "," + this.collCode + ")";
			this.term = this.collHead + this.name + this.agent;
		}
	}

	public void setCode(String code) {
		this.code = code;
		this.name = this.rname + "(" + this.code + ")";
		this.agent = "^Agent(" + this.e.code + "," + this.code + ")";
		this.adj = "";
		for (int i = 0; i < adjs.size(); i++) {
			this.adj += adjs.get(i) + "(" + this.code + ")";
			if (i + 1 < adjs.size()) {
				adj += "^";
			}
		}
		this.term = this.name + this.agent + this.adj;

	}

	public void setEventCode(String code) {
		if (this.ner) {
			this.term = "Agent(" + code + "," + this.rname + ")" + this.adj;
		} else {
			if (this.e.agentCollection && !this.e.scopeAgent) {
				this.agent = "Agent(" + code + "," + this.collCode + ")";
				this.term = this.collHead + this.name + this.agent;
			} else {
				this.agent = "Agent(" + code + "," + this.code + ")";
				this.term = this.name + this.agent + this.adj;
			}
		}
	}

	public void skolem(String code) {
		this.code += code;
		this.name = this.rname + "(" + this.code + ")";
		this.agent = "Agent(" + this.e.code + "," + this.code + ")";
		this.collHead = "All(" + this.code + ")^(Member(" + this.code + ","
				+ this.collCode + "))=>";
		this.adj = "";
		for (int i = 0; i < adjs.size(); i++) {
			this.adj += adjs.get(i) + "(" + this.code + ")";
			if (i + 1 < adjs.size()) {
				adj += "^";
			}
		}
	}

}
