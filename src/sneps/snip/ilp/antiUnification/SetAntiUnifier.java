package sneps.snip.ilp.antiUnification;

import java.util.Hashtable;

import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.VariableNode;

public class SetAntiUnifier extends AntiUnifier{
	
	NodeSet nodes;
	
	public SetAntiUnifier(NodeSet nodes, Hashtable<VariableNode, NodeSet> instances){
		super(instances);
		this.nodes = nodes;
	}
	
	public String toString(){
		return "< " + this.nodes + ", " + this.instances + " >";
	}
}
