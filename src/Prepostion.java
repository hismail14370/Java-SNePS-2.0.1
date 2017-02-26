import edu.stanford.nlp.ling.IndexedWord;

public class Prepostion {

	String rname;
	String name;
	String loc;
	String locCode = "L";
	Event e;
	static int counter = 0;

	public Prepostion(String name, IndexedWord loc, Event e) {

		if (loc.ner().equals("O")) {
			this.rname = "" + loc;
			this.locCode = locCode + "" + ++counter;
			this.e = e;
			this.loc = loc + "(" + this.locCode + ")^" + name + "("
					+ this.e.code + "," + this.locCode + ")";
			this.name = this.loc;
		} else {
			this.rname = "" + loc;
			this.loc = "" + loc;
			this.locCode = "" + loc;
			this.e = e;
			this.name = name + "(" + this.e.code + "," + this.loc + ")";

		}
	}

	// public void setEventCode(String code){
	// this.patient ="^patient("+code+","+ this.loc+")";
	// }

	public String toString() {
		return this.name;
	}



}
