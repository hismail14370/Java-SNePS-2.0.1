package sneps.snip.ilp.rules;

import java.util.HashSet;

import sneps.network.CaseFrame;
import sneps.network.CustomException;
import sneps.network.Network;
import sneps.network.Relation;
import sneps.network.classes.semantic.Entity;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.snip.ilp.antiUnification.SingularAntiUnifier;

public class OrEntailment {
	
	public HashSet<AndEntailment> antecedents;
	public HashSet<SingularAntiUnifier> consequents;
	
	public OrEntailment(){
		this.antecedents = new HashSet<>();
		this.consequents = new HashSet<>();
	}
		
	public MolecularNode build() throws Exception{
		int antCount = this.antecedents.size();
		int conCount = this.consequents.size();
		Object[][] relNodes = new Object[antCount + conCount][2];
		int i = 0;
		for (AndEntailment a : this.antecedents){
			relNodes[i][0] = Relation.ant;
			relNodes[i][1] = a.buildAntecedentForOr();
			i++;
		}
		for (SingularAntiUnifier s : this.consequents){
			relNodes[i][0] = Relation.cq;
			relNodes[i][1] = s.antiUnifierNode;
			i++;
		}
		return Network.buildMolecularNode(relNodes, CaseFrame.orRule);
	}
	
	public String toString(){
		String s = "RULE: {";
		for (AndEntailment andE : this.antecedents){
			s += andE.antecedents + " ";
		}
		s+="} v=> {";
		for (SingularAntiUnifier sin : this.consequents){
			s += sin.antiUnifierNode + " ";
		}
		s+= "}";
		return s;	
	}
	
}
