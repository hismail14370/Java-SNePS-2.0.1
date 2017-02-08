/**
 * @className PreconditionAct.java
 * 
 * @ClassDescription This is the class that represents the
 * Precondition-Act rule (or transformer).
 * 
 * @author Ahmed Osama Abd El-Meguid Ibrahim
 * @version 2.00 20th of March, 2016
 */

package sneps.snip.rules.nodes;

import sneps.network.nodes.NodeSet;
import sneps.network.classes.semantic.Proposition;
import sneps.network.classes.syntactic.Molecular;
import sneps.snip.rules.utils.RuleUseInfo;

public class PreconditionAct extends RuleNode {
	
	int precondition, act;
	
	public PreconditionAct(Molecular syn, Proposition sym) {
		super(syn, sym);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, int contextID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("precondition");
	}

}
