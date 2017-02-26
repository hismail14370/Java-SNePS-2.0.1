import edu.stanford.nlp.ling.IndexedWord;
import java.util.ArrayList;

public class Patient {
	String rname;
	String name;
	String patient;
	String term;
	String code = "P";
	boolean ner = false;
	boolean relcl = false;
	Event e;
	String collection;
	String collCode = "Y";
	String scopeSymbol;
	String adj = "";
	ArrayList<String> adjs = new ArrayList<String>();
	static int collcounter = 0;
	static int counter = 0;

	public Patient() {

	}

	public Patient(IndexedWord name, Event e) {
		this.rname = name.lemma() + "";
		this.code = code + "" + ++counter;
		if (name.ner().equals("O")) {
			this.e = e;
			if (this.e.cop) {
				this.term = name.lemma() + "(" + this.code + ")";
			} else {
				this.name = name.lemma() + "(" + this.code + ")";
				this.patient = "^patient(" + this.e.code + "," + this.code
						+ ")";
				this.term = this.name + this.patient;
			}
		} else {
			this.ner = true;
			this.e = e;
			if (this.e.cop) {
				this.term = "" + name.lemma();
				this.code = "";
			} else {
				this.term = "Patient(" + this.e.code + "," + name.lemma() + ")";
				// this.code = ""+name.lemma();
			}
		}
		if (name.tag().equals("NNS")) {
			this.e.patientCollection = true;
			this.collCode += "" + ++collcounter;
			if (this.e.cop) {
				this.term = "All(" + this.code + ")(Member(" + this.code + ","
						+ this.collCode + "))=>" + this.term;
				this.code = this.collCode;
			} else {
				this.term = "All(" + this.code + ")(Member(" + this.code + ","
						+ this.collCode + "))=>";
				this.name = name.lemma() + "(" + this.code + ")";
				this.patient = "^patient(" + this.e.code + "," + this.collCode
						+ ")";
				this.term += this.name + this.patient;
			}
		}
	}

	public void setCode(String code) {
		this.code = code;
		this.name = this.rname + "(" + this.code + ")";
		this.patient = "^patient(" + this.e.code + "," + this.code + ")";
		this.adj = "";
		for (int i = 0; i < adjs.size(); i++) {
			this.adj += adjs.get(i) + "(" + this.code + ")";
			if (i + 1 < adjs.size()) {
				adj += "^";
			}
		}
		this.term = this.name + this.patient + this.adj;
	}

	public void setEventCode(String code) {
		if (this.ner) {
			this.term = "Patient(" + code + "," + this.rname + ")" + this.adj;
		} else {
			if (this.e.cop) {
				this.term = this.rname + this.adj;
			} else {
				if (this.e.patientCollection && !this.e.scopePatient) {
					this.patient = "^patient(" + code + "," + this.collCode
							+ ")";
					this.term = "All(" + this.code + ")(Member(" + this.code
							+ "," + this.collCode + "))=>";
					this.term += this.name + this.patient;
				} else {
					this.patient = "^patient(" + code + "," + this.code + ")";
					this.term = this.name + this.patient + this.adj;
				}
			}
		}
	}

	public void skolem(String code) {
		this.code += code;
		this.name = this.rname + "(" + this.code + ")";
		this.patient = "^patient(" + this.e.code + "," + this.code + ")";
		this.adj = "";
		for (int i = 0; i < adjs.size(); i++) {
			this.adj += adjs.get(i) + "(" + this.code + ")";
			if (i + 1 < adjs.size()) {
				adj += "^";
			}
		}
		// if(this.e.patientCollection){
		// if(this.e.cop){
		// this.term =
		// "All("+this.code+")(Member("+this.code+","+this.collCode+"))=>"+this.term;
		// }else{
		// this.term =
		// "All("+this.code+")(Member("+this.code+","+this.collCode+"))=>"+this.name+this.patient;
		// }
		// }else{
		// if(this.ner){
		// this.term = "Patient("+this.e.code+","+this.rname+")";
		// }else{
		// this.term = this.name+this.patient+this.adj;
		// }
		// }
	}

}
