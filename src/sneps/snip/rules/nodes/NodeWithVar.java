package sneps.snip.rules.nodes;

import java.util.LinkedList;

import sneps.network.nodes.VariableNode;
import sneps.network.classes.semantic.Entity;
import sneps.network.classes.syntactic.Term;

public interface NodeWithVar {
	public LinkedList<VariableNode> getFreeVariables();
	public int getId();
	public boolean hasSameFreeVariablesAs(NodeWithVar node);
	public Term getSyntactic();
	public Entity getSemantic();
}
