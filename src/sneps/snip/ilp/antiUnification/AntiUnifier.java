package sneps.snip.ilp.antiUnification;

import java.util.HashSet;
import java.util.Hashtable;

import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.VariableNode;
import sneps.snip.ilp.Pair;

public class AntiUnifier {
	
	Hashtable<VariableNode, NodeSet> instances;
	
	public AntiUnifier(Hashtable<VariableNode, NodeSet> instances){
		this.instances = instances;
	}
	
	public String toString(){
		return "< " + this.instances + " >";
	}
}
