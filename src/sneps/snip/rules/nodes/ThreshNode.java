package sneps.snip.rules.nodes;

import java.util.HashSet;
import java.util.Set;

import sneps.network.nodes.NodeSet;
import sneps.network.classes.semantic.Proposition;
import sneps.network.classes.syntactic.Molecular;
import sneps.snip.channels.Channel;
import sneps.snip.Report;
import sneps.snip.rules.utils.FlagNode;
import sneps.snip.rules.utils.RuleUseInfo;
import sneps.snebr.Support;

public class ThreshNode extends RuleNode {

	private int min, max, arg;

	public ThreshNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		NodeSet minNode = this.getDownNodeSet("thresh");
		min = Integer.parseInt(minNode.getNode(0).getIdentifier());
		NodeSet maxNode = this.getDownNodeSet("threshmax");
		max = Integer.parseInt(maxNode.getNode(0).getIdentifier());
		NodeSet antNodes = this.getDownNodeSet("arg");
		arg = antNodes.size();
		this.processNodes(antNodes);
	}

	protected void sendRui(RuleUseInfo ruiRes, int context) {
		// TODO Mussab Compute Support
		boolean sign = false;
		if (ruiRes.getPosCount() == min
				&& ruiRes.getNegCount() == arg - max - 1)
			sign = true;
		else if (ruiRes.getPosCount() != min - 1
				|| ruiRes.getNegCount() != arg - max)
			return;

		Set<Integer> consequents = new HashSet<Integer>();
		for (FlagNode fn : ruiRes.getFlagNodeSet()) {
			if (antNodesWithVarsIDs.contains(fn.getNode().getId()))
				continue;
			if (antNodesWithoutVarsIDs.contains(fn.getNode().getId()))
				continue;
			consequents.add(fn.getNode().getId());
		}
		Set<Support> originSupports = ((Proposition) this.getSemantic())
				.getOriginSupport();
		Report reply = new Report(ruiRes.getSub(),
				ruiRes.getSupport(originSupports), sign, context);
		for (Channel outChannel : outgoingChannels) {
			if (!consequents.contains(outChannel.getRequester().getId()))
				continue;
			outChannel.addReport(reply);
		}

	}

	public int getThresh() {
		return min;
	}

	public int getThreshMax() {
		return max;
	}

	public int getArg() {
		return arg;
	}

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("arg");
	}

}
