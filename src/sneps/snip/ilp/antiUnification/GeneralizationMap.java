package sneps.snip.ilp.antiUnification;

import java.util.*;

import sneps.network.Network;
import sneps.network.classes.semantic.Entity;
import sneps.network.nodes.Node;
import sneps.network.nodes.VariableNode;

public class GeneralizationMap {
	
	private HashMap<String, VariableNode> map;

	public GeneralizationMap(){
		this.map = new HashMap<>();
	}
	
	private boolean existsInMap(Node n){
		return this.map.containsKey(n.getIdentifier());
	}
	
	private boolean variableExistsInMap(VariableNode v){
		return (this.map.containsKey(v.getIdentifier()) || this.map.containsValue(v));
	}
	
	private boolean thetaSubsumes(Node n1, Node n2){
		while(n2 != null){
			if(n1.getIdentifier().equals(n2.getIdentifier())){
				return true;
			}
			n2 = getParentNode(n2);
		}
		return false;
	}
	
	private VariableNode getParentNode(Node n){
		return this.map.get(n.getIdentifier());
	}
		
	public VariableNode getGeneralization(Node n1, Node n2){
		if (existsInMap(n1) && existsInMap(n2)){
			VariableNode v1 = getParentNode(n1);
			VariableNode v2 = getParentNode(n2);
			if (v1.getIdentifier().equals(v2.getIdentifier())){
				return v1;
			}
			else {
				return getGeneralization(v1, v2);
			}
		}
		else {
			if ((existsInMap(n1) && !existsInMap(n2)) || (existsInMap(n2) && !existsInMap(n1))){
				Node absentNode = existsInMap(n1)? n2 : n1;
				String absentKey = absentNode.getIdentifier();
				VariableNode presentNode = existsInMap(n1)? getParentNode(n1) : getParentNode(n2);
				if (!this.thetaSubsumes(absentNode, presentNode)){
					this.map.put(absentKey, presentNode);
				}
				return presentNode;
			}
			else {
				VariableNode newVariable = Network.buildVariableNode();
				this.map.put(n1.getIdentifier(), newVariable);
				this.map.put(n2.getIdentifier(), newVariable);
				return newVariable;
			}
		}
	}
	
	public VariableNode baseVariableGeneralization(Node base, VariableNode variable){
		if (existsInMap(base) && existsInMap(variable)){
			return variable;
		}
		else {
			if (existsInMap(base)){
				VariableNode v = getParentNode(base);
				while (getParentNode(v) != null){
					v = getParentNode(v);
				}
				this.map.put(v.getIdentifier(), variable);
				return variable;
			}
			else {
				if (existsInMap(variable)){
					this.map.put(base.getIdentifier(), variable);
					return variable;
				}
				else {
					this.map.put(base.getIdentifier(), variable);
					return variable;
				}
			}
		}
	}
	
	public VariableNode variableVariableGeneralization(VariableNode v1, VariableNode v2){
		if(variableExistsInMap(v1) && variableExistsInMap(v2)){
			return v1;
		}
		else {
			if (variableExistsInMap(v1)){
				this.map.put(v2.getIdentifier(), v1);
				return v1;
			}
			else {
				this.map.put(v1.getIdentifier(), v2);
				return v2;
			}
		}
	}
		
	public void displayMap(){
		System.out.println(this.map);
	}
	
	public String toString(){
		return this.map.toString();
	}
	
//	=========== MAIN METHOD FOR TESTING ============
	
	public static void main(String[] args){
		GeneralizationMap m = new GeneralizationMap();
		Entity e = new Entity();
		Node n1 = Network.buildBaseNode("a", e);
		Node n2 = Network.buildBaseNode("b", e);
		Node n3 = Network.buildBaseNode("c", e);
		Node n4 = Network.buildBaseNode("d", e);
		Node n5 = Network.buildBaseNode("e", e);
		System.out.println(m.getGeneralization(n1, n2));
		System.out.println(m.getGeneralization(n3, n4));
		System.out.println(m.getGeneralization(n1, n3));
		System.out.println("____");
		VariableNode v = m.getGeneralization(n1, n5);
		System.out.println(v);
		System.out.println("____");
		System.out.println(m.getParentNode(v));
		System.out.println("____Testing base to variable_____");
		VariableNode v1 = Network.buildVariableNode();
		System.out.println("VARIABLE: "+v1.getIdentifier());
		System.out.println("GENERALIZED TO: "+m.baseVariableGeneralization(n1, v1));
		System.out.println(m.baseVariableGeneralization(Network.buildBaseNode("f", e), v));
		System.out.println(m.baseVariableGeneralization(Network.buildBaseNode("g", e), Network.buildVariableNode()));
//		System.out.println("__ Test theta subsumption __");
//		VariableNode v1 = m.getGeneralization(n1, n2);
//		VariableNode v2 = m.getGeneralization(n3, n4);
//		VariableNode v3 = m.getGeneralization(n1, n3);
//		System.out.println(v1);
//		System.out.println(v2);
//		System.out.println(v3);
//		System.out.println(m.thetaSubsumes(v1, v2));
//		System.out.println(m.thetaSubsumes(v3, v3));
//		System.out.println(m.thetaSubsumes(v3, v2));
//		System.out.println(m.thetaSubsumes(v3, v1));
//		System.out.println(m.thetaSubsumes(v1, v3));
//		System.out.println(m.thetaSubsumes(v2, v3));
		m.displayMap();
	}
}
