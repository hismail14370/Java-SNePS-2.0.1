package sneps.snip.ilp.antiUnification;

import java.util.*;

import sneps.network.CustomException;
import sneps.network.Network;
import sneps.network.classes.semantic.Entity;
import sneps.network.nodes.Node;
import sneps.network.nodes.VariableNode;

public class GeneralizationMap {
	
	private HashMap<String, VariableNode> map;
	
	public GeneralizationMap(){
		this.map = new HashMap<>();
	}
	
	public boolean clearForRecord(Node n1, Node n2){
		String x = n1.toString() + " - " + n2.toString();
		String y = n2.toString() + " - " + n1.toString();
		return !(this.map.containsKey(x) || this.map.containsKey(y));
	}
	
	public boolean clearForRecord(String a, String b){
		String x = a + " - " + b;
		String y = b + " - " + a;
		return !(this.map.containsKey(x) || this.map.containsKey(y));
	}
	
	public boolean clearForRecord(String s){
		return !(this.map.containsKey(s));
	}
	
	public VariableNode getGeneralization(Node n1, Node n2) throws CustomException{
		if (this.map.containsKey(n1.toString() + " - " + n2.toString())){
			return this.map.get(n1.toString() + " - " + n2.toString());
		}
		else {
			if (this.map.containsKey(n2.toString() + " - " + n1.toString())){
				return this.map.get(n2.toString() + " - " + n1.toString());
			}
			else {
				throw new CustomException("No record found for this substitution");
			}
		}
	}
	
	public Node recordSubstitution(Node n1, Node n2) throws CustomException{
		if (this.clearForRecord(n1.toString(), n2.toString())){
			if (n1.getSyntacticType().equals("Variable")){
				this.map.put(n1.toString() + " - " + n2.toString(), (VariableNode) n1);
				return (VariableNode) n1;
			}
			else {
				if (n2.getSyntacticType().equals("Variable")){
					this.map.put(n2.toString() + " - " + n1.toString(), (VariableNode) n2);
					return (VariableNode) n2;
				} 
				else {
					VariableNode v = Network.buildVariableNode();
					this.map.put(n1.toString() + " - " + n2.toString(), v);
					return v;
				}
			}
		}
		else {
			return getGeneralization(n1, n2);
		}
	}
	
	public void displayMap(){
		System.out.println(this.map);
	}
	
	public String toString(){
		return this.map.toString();
	}
	
//////////////////////////////////////
//  Main method for testing //////////
//////////////////////////////////////
	
	public static void main(String[] args) throws CustomException {
		GeneralizationMap m = new GeneralizationMap();
		Entity e = new Entity();
		Node n1 = Network.buildBaseNode("a", e);
		Node n2 = Network.buildBaseNode("b", e);
		Node n3 = Network.buildBaseNode("c", e);
		VariableNode v1 = Network.buildVariableNode();
		VariableNode v2 = Network.buildVariableNode();
		VariableNode v3 = Network.buildVariableNode();
		
		m.recordSubstitution(n1, n2);
		m.recordSubstitution(n2, n3);
		m.recordSubstitution(n1, n2);
		m.recordSubstitution(v1, n1);
		m.recordSubstitution(v1, v2);
		
		m.displayMap();
	}
}
