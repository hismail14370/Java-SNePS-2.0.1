package sneps.snip.ilp.rules;

import java.util.HashSet;

import sneps.snip.ilp.antiUnification.SingularAntiUnifier;

public class AndEntailment {
	
	public HashSet<SingularAntiUnifier> antecedents;
	public HashSet<SingularAntiUnifier> consequents;
	
	public AndEntailment(HashSet<SingularAntiUnifier> antecedents, HashSet<SingularAntiUnifier> consequents){
		this.antecedents = antecedents;
		this.consequents = consequents;
	}
	
	public AndEntailment(){
		this.antecedents = new HashSet<>();
		this.consequents = new HashSet<>();
	}
	
	public String toString(){
		String s = "{";
		for (SingularAntiUnifier sin : this.antecedents){
			s += sin.antiUnifierNode + " ";
		}
		s+="} &=> {";
		for (SingularAntiUnifier sin : this.consequents){
			s += sin.antiUnifierNode + " ";
		}
		s+= "}";
		return s;
	}

}
