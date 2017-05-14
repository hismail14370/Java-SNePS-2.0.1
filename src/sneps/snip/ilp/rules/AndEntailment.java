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
	
	public boolean combinableAntecedents(AndEntailment a){
		outer : for (SingularAntiUnifier s : this.consequents){
			for (SingularAntiUnifier t : a.consequents){
				if (s.equivalent(t)){
					continue outer;
				}
			}
			return false;
		}
		return true;
	}
	
	public MolecularNode buildAntecedentForOr() throws Exception{
		if (this.antecedents.size() > 1){
			int n = this.antecedents.size();
			Entity e = new Entity();
			Node minMax = Network.buildBaseNode(""+n, e);
			Object[][] relNodes = new Object[n+2][2];
			relNodes[0][0] = Relation.min;
			relNodes[0][1] = minMax;
			relNodes[1][0] = Relation.max;
			relNodes[1][1] = minMax;
			int i = 0;
			for (SingularAntiUnifier s : this.antecedents){
				relNodes[i+2][0] = Relation.arg;
				relNodes[i+2][1] = s.antiUnifierNode;
			}
			return Network.buildMolecularNode(relNodes, CaseFrame.andOrRule);
		}
		else {
			for (SingularAntiUnifier s : this.antecedents){
				return s.antiUnifierNode;
			}
			return null;
		}
	}
	
	public MolecularNode build() throws Exception{
		int antCount = this.antecedents.size();
		int conCount = this.consequents.size();
		Object[][] relNodes = new Object[antCount + conCount][2];
		int i = 0;
		for (SingularAntiUnifier s : this.antecedents){
			relNodes[i][0] = Relation.andAnt;
			relNodes[i][1] = s.antiUnifierNode;
			i++;
		}
		for (SingularAntiUnifier s : this.consequents){
			relNodes[i][0] = Relation.cq;
			relNodes[i][1] = s.antiUnifierNode;
			i++;
		}
		return Network.buildMolecularNode(relNodes, CaseFrame.andRule);
	}
	
	public String toString(){
		String s = "RULE: {";
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
