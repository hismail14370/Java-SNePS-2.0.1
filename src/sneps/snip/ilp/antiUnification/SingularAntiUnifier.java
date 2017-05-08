package sneps.snip.ilp.antiUnification;

import java.util.Hashtable;

import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.VariableNode;

public class SingularAntiUnifier extends AntiUnifier{
	
	MolecularNode node;
	
	public SingularAntiUnifier(MolecularNode node, Hashtable<VariableNode, NodeSet> instances){
		super(instances);
		this.node = node;
	}
	
	public String toString(){
		return "< " + this.node + ", " + this.instances + " >";
	}
}
