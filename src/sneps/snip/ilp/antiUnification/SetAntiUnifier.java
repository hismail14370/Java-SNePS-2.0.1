package sneps.snip.ilp.antiUnification;

import java.util.HashSet;
import java.util.Hashtable;

import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.VariableNode;

public class SetAntiUnifier{
	
	NodeSet constants;
	HashSet<SingularAntiUnifier> moleculars;
	Hashtable<VariableNode, NodeSet> instances;
	public String relationName;
	
	public SetAntiUnifier(String relationName){
		this.relationName = relationName;
		this.constants = new NodeSet();
		this.instances = new Hashtable<>();
		this.moleculars = new HashSet<>();
	}
	
	public NodeSet getNodes(){
		NodeSet result = new NodeSet();
		for (VariableNode v : this.instances.keySet()){
			result.addNode(v);
		}
		result.addAll(this.constants);
		for (SingularAntiUnifier s : this.moleculars){
			result.addNode(s.antiUnifierNode);
		}
		return result;
	}
	
	public NodeSet getGroundInstances(){
		NodeSet result = new NodeSet();
		for (VariableNode v : this.instances.keySet()){
			NodeSet set = this.instances.get(v);
			for (int i = 0; i < set.size(); i++){
				if (!(set.getNode(i) instanceof VariableNode)){
					result.addNode(set.getNode(i));
				}
			}
		}
		result.addAll(this.constants);
		for (SingularAntiUnifier s : this.moleculars){
			for (int i = 0; i < s.setAntiUnifiers.size(); i++){
				SetAntiUnifier set = s.setAntiUnifiers.get(i);
				result.addAll(set.getGroundInstances());
			}
		}
//		System.out.println("Ground instances of "+this.relationName + ": " + result);
		return result;
	}
	
	public void addConstant(Node n){
		this.constants.addNode(n);
	}
	
	public void addVariable(VariableNode v, Node a, Node b){
		if (this.instances.get(v) == null){
			this.instances.put(v, new NodeSet());
		}
		this.instances.get(v).addNode(a);
		this.instances.get(v).addNode(b);
	}
	
	public void addMolecular(SingularAntiUnifier ant){
		this.moleculars.add(ant);
	}
	
	public String toString(){
		return "Set Anti-Unifier: " + this.relationName +  ": < " + this.constants + ", " + this.instances + ", " + this.moleculars + " >";
	}
}
