package sneps.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import java.util.ArrayList;

public class Event {

	String rname;
	String name;
	String code = "E";
	String time;
	boolean scopeAgent = false;
	boolean scopePatient = false;
	boolean cop = false;
	boolean agentCollection = false;
	boolean patientCollection = false;
	ArrayList<Agent> agent = new ArrayList<Agent>();
	ArrayList<Patient> patient = new ArrayList<Patient>();
	ArrayList<Prepostion> prep = new ArrayList<Prepostion>();
	ArrayList<Quantifiers> quant = new ArrayList<Quantifiers>();
	ArrayList<String> quantSymboles = new ArrayList<String>();
	static int counter = 0;

	public Event(IndexedWord name) {
		this.code = code + "" + ++counter;
		if (name.tag().equals("VBD") || name.tag().equals("VBN")) {
			this.time = "time(" + this.code + ",t1)^" + "after(t0,t1)";

		} else {
			if (name.tag().equals("VBP") || name.tag().equals("VBZ")
					|| name.tag().equals("VBG")) {
				this.time = "now(t0)";
			}
		}
		// if(name.tag().equals("JJ") || name.tag().equals("NN")){
		// this.code = code+""+ ++counter;
		// this.name=name.lemma();
		// this.rname=""+name.lemma();
		// this.cop=true;
		// }else{
		this.name = name.lemma() + "(" + this.code + ")^" + this.time;
		this.rname = "" + name.lemma();

		// }
	}

	public void setCode(String code) {
		this.code = code;
		this.name = rname + "(" + this.code + ")";
		this.adjust();

	}

	public void skolem(String code) {
		this.code += code;
		this.name = rname + "(" + this.code + ")";
		this.adjust();
	}

	public void adjust() {
		for (int i = 0; i < agent.size(); i++) {
			agent.get(i).setEventCode(this.code);
		}

		for (int i = 0; i < patient.size(); i++) {
			patient.get(i).setEventCode(this.code);
		}

		for (int i = 0; i < prep.size(); i++) {
			prep.get(i).e.code = this.code;
		}
	}

	public String getPrep() {
		String p = "";
		for (int i = 0; i < prep.size(); i++) {
			p += prep.get(i).toString();
			if (i + 1 < prep.size()) {
				p += "^";
			}
		}
		// System.out.println(p);
		return p;
	}

	public String getPatient() {
		String p = "";
		for (int i = 0; i < patient.size(); i++) {
			p += patient.get(i).patient + patient.get(i).adj;
			if (i + 1 < patient.size()) {
				p += "^";
			}
		}
		// System.out.println(p);
		return p;
	}

	public String getPatientTerm() {
		String p = "";
		for (int i = 0; i < patient.size(); i++) {
			p += patient.get(i).term;
			if (i + 1 < patient.size()) {
				p += "^";
			}
		}
		// System.out.println(p);
		return p;
	}

	public String getAgent() {
		String p = "";
		for (int i = 0; i < agent.size(); i++) {
			p += agent.get(i).agent + agent.get(i).adj;
			if (i + 1 < agent.size()) {
				p += "^";
			}
		}

		// System.out.println(p);
		return p;
	}

	public String getAgentTerm() {
		String p = "";
		for (int i = 0; i < agent.size(); i++) {
			p += agent.get(i).term;
			if (i + 1 < agent.size()) {
				p += "^";
			}
			if (agent.get(i).collection != null) {
				p += "^" + agent.get(i).collection;
			}
		}
		// System.out.println(p);
		return p;
	}

	public String getQuant() {
		String p = "";
		for (int i = 0; i < quant.size(); i++) {
			p += "All(" + quant.get(i).antecode + ")^"
					+ quant.get(i).antecedent;
			if (i + 1 <= quant.size()) {
				p += "=>";
			}
		}
		// System.out.println(p);
		return p;
	}

	public void skolemization() {
		String res = "(";
		for (int i = 0; i < quantSymboles.size(); i++) {
			res += quantSymboles.get(i);
			if (i + 1 < quantSymboles.size()) {
				res += ",";
			} else {
				res += ")";
			}
		}
		if (!this.scopeAgent) {
			for (int i = 0; i < agent.size(); i++) {
				agent.get(i).skolem(res);
			}
		}// else{
			// for(int i =0; i<agent.size(); i++){
		// agent.get(i).setCode(res);
		// }
		// }
		if (!this.scopePatient) {
			for (int i = 0; i < patient.size(); i++) {
				patient.get(i).skolem(res);
			}
		}// else{
			// for(int i =0; i<patient.size(); i++){
		// patient.get(i).setCode(res);
		//
		// }
		// }
		this.skolem(res);

	}

	public String toString() {
		if (quant.size() > 0 && this.scopeAgent && this.scopePatient) {
			return getQuant() + this.name + "^" + getAgent() + getPatient();
		} else {
			if (quant.size() > 0 && this.scopeAgent && !this.scopePatient) {
				return getQuant() + this.name + "^" + getAgent() + "^"
						+ getPatientTerm();
			} else {
				if (quant.size() > 0 && !this.scopeAgent && this.scopePatient) {
					return getQuant() + this.name + "^" + getAgentTerm()
							+ getPatient();
				} else {
					if (cop) {
						return this.name + "(" + getPatientTerm() + ")";
					} else {
						if (this.patient.size() == 0 && this.prep.size() == 0) {
							return this.name + "^" + getAgentTerm();
						} else {
							if (this.prep.size() > 0
									&& this.patient.size() == 0) {

								return this.name + "^" + getAgentTerm()
										+ getPrep();
							} else {
								if (this.prep.size() == 0
										&& this.patient.size() > 0) {

									return this.name + "^" + getAgentTerm()
											+ "^" + getPatientTerm();
								} else {
									return this.name + "^" + getAgentTerm()
											+ "^" + getPatientTerm() + "^"
											+ getPrep();
								}
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
