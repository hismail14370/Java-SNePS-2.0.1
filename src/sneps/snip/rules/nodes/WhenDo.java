package sneps.snip.rules.nodes;

import sneps.network.nodes.NodeSet;
import sneps.network.classes.semantic.Proposition;
import sneps.network.classes.syntactic.Molecular;
import sneps.snip.rules.utils.RuleUseInfo;

public class WhenDo extends RuleNode {

	public WhenDo(Molecular syn, Proposition sym) {
		super(syn, sym);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, int contextID) {
		// TODO Auto-generated method stub

	}

	@Override
	public NodeSet getDownAntNodeSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
