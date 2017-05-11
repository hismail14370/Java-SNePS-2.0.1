package sneps.snip.ilp.antiUnification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import nu.xom.Nodes;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.VariableNode;

public class SingularAntiUnifier{
	
	public MolecularNode antiUnifierNode;
	public ArrayList<SetAntiUnifier> setAntiUnifiers;
	
	public SingularAntiUnifier(MolecularNode node, ArrayList<SetAntiUnifier> setAntiUnifiers){
		this.antiUnifierNode = node;
		this.setAntiUnifiers = setAntiUnifiers;
	}
	
	public SetAntiUnifier getSetAntiUnifierByRelation(String relationName){
		SetAntiUnifier result = null;
		for (int i = 0; i < this.setAntiUnifiers.size(); i++){
			if (this.setAntiUnifiers.get(i).relationName.equals(relationName)){
				result = this.setAntiUnifiers.get(i);
			}
		}
		return result;
	}
	
	public void combine(SingularAntiUnifier ant){
		for (int i = 0; i < this.setAntiUnifiers.size(); i++){
			SetAntiUnifier setAnt = ant.getSetAntiUnifierByRelation(this.setAntiUnifiers.get(i).relationName);
			this.setAntiUnifiers.get(i).constants.addAll(setAnt.constants);
			for (VariableNode v : setAnt.instances.keySet()){
				if (this.setAntiUnifiers.get(i).instances.get(v) == null){
					this.setAntiUnifiers.get(i).instances.put(v, new NodeSet());
				}
				this.setAntiUnifiers.get(i).instances.get(v).addAll(setAnt.instances.get(v));
			}
			this.setAntiUnifiers.get(i).moleculars.addAll(setAnt.moleculars);
		}
		this.antiUnifierNode = ant.antiUnifierNode;
	}
	
	public boolean consistent(SingularAntiUnifier ant){
		for (int i = 0; i < this.setAntiUnifiers.size(); i++){
			outer: for (int j = 0; j < ant.setAntiUnifiers.size(); j++){
				NodeSet bgInstances = ant.setAntiUnifiers.get(j).getGroundInstances();
				NodeSet posEvInstances = this.setAntiUnifiers.get(i).getGroundInstances();
				for (int k = 0; k < bgInstances.size(); k++){
					if (!posEvInstances.contains(bgInstances.getNode(k))){
						continue outer;
					}
				}
				System.out.println("CONSISTENCY ON: ");
				System.out.println(this.setAntiUnifiers.get(i));
				System.out.println(ant.setAntiUnifiers.get(j));
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		String s = "Singular Anti-Unifier: < " + this.antiUnifierNode + " >\n";
		for (int i = 0; i < this.setAntiUnifiers.size(); i++){
			s += "\t\t" + setAntiUnifiers.get(i) + "\n";
		}
		System.out.println(s);
		return s;	
	}
}
