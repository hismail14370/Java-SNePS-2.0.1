package sneps.snip.rules.nodes;

import java.util.Set;

import sneps.snebr.Support;
import sneps.network.nodes.NodeSet;
import sneps.network.classes.semantic.Proposition;
import sneps.network.classes.syntactic.Molecular;
import sneps.snip.channels.Channel;
import sneps.snip.Report;
import sneps.snip.rules.utils.RuleUseInfo;

public class NumericalNode extends RuleNode {

	private int i, ant, cq;;

	public NumericalNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet maxNode = this.getDownNodeSet("i");
		i = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("&ant");
		ant = antNodes.size();
		cq = this.getDownNodeSet("cq").size();
		this.processNodes(antNodes);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, int context) {
		if (tRui.getPosCount() < i)
			return;
		Set<Support> originSupports = ((Proposition) this.getSemantic())
				.getOriginSupport();
		Report reply = new Report(tRui.getSub(),
				tRui.getSupport(originSupports), true, context);
		// This part should be broadcast(reply) instead of a loop.
		for (Channel outChannel : outgoingChannels)
			outChannel.addReport(reply);
	}

	public int getI() {
		return i;
	}

	public int getAnt() {
		return ant;
	}

	public int getCq() {
		return cq;
	}

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("&ant");
	}

}
