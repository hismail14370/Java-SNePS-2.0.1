package sneps.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import java.util.ArrayList;

public class Verb {

	String name;
	String predicate;
	String id = "E";
	boolean cop;
	int scope = 0;
	String scopeid = "";
	ArrayList<Noun> agents = new ArrayList<Noun>();
	ArrayList<Noun> patients = new ArrayList<Noun>();
	ArrayList<Noun> prepostions = new ArrayList<Noun>();
	ArrayList<Noun> owners = new ArrayList<Noun>();
	ArrayList<String> ccomp = new ArrayList<String>();
	static int counter = 0;

	public Verb(IndexedWord word) {
		this.name = word.lemma();
		this.id = this.id + "" + ++counter;
		// this.predicate=name+"("+id+")";
	}

	public String build(ArrayList<Noun> ns) {
		String res = "";
		for (int i = 0; i < ns.size(); i++) {
			if (ns.get(i).det.equals("the") || ns.get(i).det.equals("a")
					&& !ns.get(i).ner) {
				if (this.scope > 0) {
					res += ns.get(i).name + "(" + ns.get(i).id + "("
							+ this.scopeid + ")" + ")" + " and ";
					ns.get(i).id = ns.get(i).id + "(" + this.scopeid + ")";
				} else
					res += ns.get(i).term + " and ";
			}
			if (ns.get(i).det.equals("all") || ns.get(i).det.equals("every")
					|| (ns.get(i).det.equals("") && ns.get(i).antecednt)) {
				res += "all(" + ns.get(i).id + ")" + "(" + ns.get(i).term
						+ "=>";
				this.scope++;
				if (this.scopeid.length() == 0)
					this.scopeid += ns.get(i).id;
				else
					this.scopeid += "," + ns.get(i).id;
			}
			if (ns.get(i).ner && ns.get(i).amods.size() > 0) {
				res += ns.get(i).term + " and ";
			}

		}
		return res;
	}

	public String getAgents() {
		String res = "";
		for (int i = 0; i < agents.size(); i++) {
			if (agents.get(i).ner)
				res += " and agent(" + this.id + "," + agents.get(i).name + ")";

			if (!agents.get(i).ner && !agents.get(i).collection)
				res += " and agent(" + this.id + "," + agents.get(i).id + ")";

			if (agents.get(i).collection)
				res += " and agent(" + this.id + "," + agents.get(i).collId
						+ ")";
		}
		return res;
	}

	public String getPatients() {
		String res = "";
		for (int i = 0; i < patients.size(); i++) {
			if (patients.get(i).ner)
				res += " and patient(" + this.id + "," + patients.get(i).name
						+ ")";

			if (!patients.get(i).ner && !patients.get(i).collection)
				res += " and patient(" + this.id + "," + patients.get(i).id
						+ ")";

			if (patients.get(i).collection)
				res += " and Patient(" + this.id + "," + patients.get(i).collId
						+ ")";

		}
		return res;
	}

	public String getOwners() {
		String res = "";
		for (int i = 0; i < owners.size(); i++) {
			if (!owners.get(i).ner && !owners.get(i).collection) {
				res += "(" + owners.get(i).id + ")";
			}

			if (!owners.get(i).ner && owners.get(i).collection)
				res += "(" + owners.get(i).collId + ")";

			if (owners.get(i).ner) {
				res += "(" + owners.get(i).name + ")";
			}
		}
		return res;
	}

	public String getLocs() {
		String res = "";
		for (int i = 0; i < prepostions.size(); i++) {
			res += " and " + prepostions.get(i).prepostion + "(" + this.id
					+ "," + prepostions.get(i).id + ")";
		}
		return res;
	}

	public String getCcomps() {
		String res = "";
		for (int i = 0; i < ccomp.size(); i++) {
			res += " and patient(" + this.id + "," + ccomp.get(i) + ")";
		}
		return res;
	}

	public String toString(int c) {
		if (c == 2) {
			String patient = build(patients);
			String agent = build(agents);
			String prepostion = build(prepostions);
			String owner = build(owners);

			String closescope = "";
			for (int i = 0; i < scope; i++) {
				closescope += ")";
			}
			if (scope > 0)
				this.id += "(" + this.scopeid + ")";

			if (this.cop)
				return owner + this.predicate + getOwners() + closescope;
			else
				return patient + agent + prepostion + this.name + "(" + this.id
						+ ")" + getAgents() + getPatients() + getCcomps()
						+ getLocs() + closescope;
		} else {
			if (c == 3) {

				String prepostion = build(prepostions);
				String agent = build(agents);
				String patient = build(patients);
				String owner = build(owners);

				String closescope = "";
				for (int i = 0; i < scope; i++) {
					closescope += ")";
				}
				if (scope > 0)
					this.id += "(" + this.scopeid + ")";

				if (this.cop)
					return owner + this.predicate + getOwners() + closescope;
				else
					return prepostion + agent + patient + this.name + "("
							+ this.id + ")" + getAgents() + getPatients()
							+ getCcomps() + getLocs() + closescope;
			} else {

				String agent = build(agents);
				String patient = build(patients);
				String prepostion = build(prepostions);
				String owner = build(owners);

				String closescope = "";
				for (int i = 0; i < scope; i++) {
					closescope += ")";
				}
				if (scope > 0)
					this.id += "(" + this.scopeid + ")";

				if (this.cop) {
					return owner + this.predicate + getOwners() + closescope;
				} else {
					return agent + patient + prepostion + this.name + "("
							+ this.id + ")" + getAgents() + getPatients()
							+ getCcomps() + getLocs() + closescope;
				}
			}
		}

	}



}
