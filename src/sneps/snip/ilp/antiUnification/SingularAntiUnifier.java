package sneps.snip.ilp.antiUnification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import nu.xom.Nodes;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.VariableNode;
import sneps.snip.ilp.Pair;

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
	
	public boolean combinable(SingularAntiUnifier ant){
		return this.antiUnifierNode.getDownCableSet().getCaseFrame()
				.equals(ant.antiUnifierNode.getDownCableSet().getCaseFrame());
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
	
	public boolean equivalent(SingularAntiUnifier ant){
		for (int i = 0; i < this.setAntiUnifiers.size(); i++){
			SetAntiUnifier set = this.setAntiUnifiers.get(i);
			SetAntiUnifier set1 = ant.getSetAntiUnifierByRelation(set.relationName);
			if (set1 == null) return false;
			for (int j = 0; j < set.constants.size(); j++){
				if (!set1.constants.contains(set.constants.getNode(j))) return false;
			}
		}
		return true;
	}
	
	public HashSet<Pair<SetAntiUnifier, SetAntiUnifier>> getConsistentPairs(SingularAntiUnifier ant){
		HashSet<Pair<SetAntiUnifier, SetAntiUnifier>> consistencies = new HashSet<>();;
		for (int i = 0; i < this.setAntiUnifiers.size(); i++){
			outer: for (int j = 0; j < ant.setAntiUnifiers.size(); j++){
				NodeSet bgInstances = ant.setAntiUnifiers.get(j).getGroundInstances();
				NodeSet posEvInstances = this.setAntiUnifiers.get(i).getGroundInstances();
				System.out.println("bg instances : " + bgInstances);
				System.out.println("posEv instances : " + posEvInstances);
				for (int k = 0; k < bgInstances.size(); k++){
					if (!posEvInstances.contains(bgInstances.getNode(k))){
						continue outer;
					}
				}
//				System.out.println("CONSISTENCY ON: ");
//				System.out.println(this.setAntiUnifiers.get(i));
//				System.out.println(ant.setAntiUnifiers.get(j));
				Pair<SetAntiUnifier, SetAntiUnifier> con = new Pair<>(ant.setAntiUnifiers.get(j), this.setAntiUnifiers.get(i));
				consistencies.add(con);
//				return true;
			}
		}
		for (Pair<SetAntiUnifier, SetAntiUnifier> p : consistencies){
			SetAntiUnifier bgSetAntiUnifier = p.getLeft();
			SetAntiUnifier posSetAntiUnifier = p.getRight();
			MolecularNode posNode = this.antiUnifierNode;
			for (VariableNode v : bgSetAntiUnifier.instances.keySet()){
				posNode.getDownCableSet().getDownCable(posSetAntiUnifier.relationName).getNodeSet().addNode(v);
			}
		}
		return consistencies;
//		return false;
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
