package sneps.snip.ilp.antiUnification;

import java.util.HashMap;

import sneps.network.Network;
import sneps.network.classes.semantic.Entity;
import sneps.network.nodes.Node;
import sneps.network.nodes.VariableNode;

public class GeneralizationMap {
	
	private HashMap<String, VariableNode> map;
	
	public GeneralizationMap(){
		this.map = new HashMap<>();
	}
	
	private boolean existsInMap(Node m, Node n){
		String x = m.getIdentifier() + " - " + n.getIdentifier();
//		String y = n.getIdentifier() + " - " + m.getIdentifier();
//		return (this.map.containsKey(x) || this.map.containsKey(y));
		return this.map.containsKey(x);
	}
	
	private VariableNode getVariableFromMap(Node n1, Node n2){
//		if (this.map.containsKey(n1.getIdentifier() + " - " + n2.getIdentifier())){
//			return this.map.get(n1.getIdentifier() + " - " + n2.getIdentifier());
//		}
//		else {
//			return this.map.get(n2.getIdentifier() + " - " + n1.getIdentifier());
//		}
		return this.map.get(n1.getIdentifier() + " - " + n2.getIdentifier());
	}
	
	public VariableNode getGeneralization(Node n1, Node n2){
		if(n1.getSyntacticType().equals("Variable")){
			if(n2.getSyntacticType().equals("Variable")){
				return variableVariableGeneralization((VariableNode) n1, (VariableNode) n2);
			}
			else {
				return baseVariableGeneralization(n2, (VariableNode) n1);
			}
		}
		else {
			if(n2.getSyntacticType().equals("Variable")){
				return baseVariableGeneralization(n1, (VariableNode) n2);
			}
			else {
				return baseGeneralization(n1, n2);
			}
		}
	}
	
	private VariableNode baseGeneralization(Node n1, Node n2){
		if (existsInMap(n1, n2)){
			return getVariableFromMap(n1, n2);
		}
		else {
			VariableNode variable = Network.buildVariableNode();
			this.map.put(n1.getIdentifier() + " - " + n2.getIdentifier(), variable);
			return variable;
		}
	}
	
	private VariableNode baseVariableGeneralization(Node base, VariableNode variable){
		if (existsInMap(base, variable)){
			return getVariableFromMap(base, variable);
		}
		else {
			this.map.put(base.getIdentifier() + " - " + variable.getIdentifier(), variable);
			return variable;
		}
	}
	
	private VariableNode variableVariableGeneralization(VariableNode v1, VariableNode v2){
		if (this.map.containsValue(v1)){
			return v1;
		}
		if (this.map.containsValue(v2)){
			return v2;
		}
		this.map.put(v1.getIdentifier() + " - " + v2.getIdentifier(), v1);
		return v1;
	}
	
	public VariableNode renameVariables(VariableNode v1, VariableNode v2){
		if (existsInMap(v1, v2)){
			return getVariableFromMap(v1, v2);
		}
		if (existsInMap(v2, v1)){
			return getVariableFromMap(v2, v1);
		}
		VariableNode res = Network.buildVariableNode();
		this.map.put(v1.getIdentifier() + " - " + v2.getIdentifier(), res);
		return res;
	}
	
	public void displayMap(){
		System.out.println("Generalization Map: " + this.map);
	}
	
	public String toString(){
		return this.map.toString();
	}
	
	public static void main(String[] args) {
		GeneralizationMap m = new GeneralizationMap();
		Entity ent = new Entity();
		Node a = Network.buildBaseNode("a", ent);
		Node b = Network.buildBaseNode("b", ent);
		Node c = Network.buildBaseNode("c", ent);
		Node d = Network.buildBaseNode("d", ent);
		Node e = Network.buildBaseNode("e", ent);
		VariableNode v1 = Network.buildVariableNode();
		VariableNode v2 = Network.buildVariableNode();
//		System.out.println(m.getGeneralization(a, b));
//		System.out.println(m.getGeneralization(b, c));
//		System.out.println(m.getGeneralization(c, d));
//		System.out.println(m.getGeneralization(c, b));
//		System.out.println(m.getGeneralization(c, a));
//		System.out.println(m.getGeneralization(a, v1));
//		System.out.println(m.getGeneralization(v1, a));
//		System.out.println(m.getGeneralization(b, v2));
//		System.out.println(m.getGeneralization(v1, v2));
		System.out.println(m.getGeneralization(a, b));
		System.out.println(m.getGeneralization(b, a));

		System.out.println(m);
	}
}
