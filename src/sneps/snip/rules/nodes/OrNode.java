package sneps.snip.rules.nodes;

import java.util.Set;

import sneps.snebr.Support;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.classes.semantic.Proposition;
import sneps.network.classes.syntactic.Molecular;
import sneps.snip.channels.Channel;
import sneps.snip.Report;
import sneps.snip.rules.utils.RuleUseInfo;

public class OrNode extends RuleNode {

	private int ant, cq;

	public OrNode(Molecular syn, Proposition sym) {
		super(syn, sym);
		ant = getDownNodeSet("ant").size();
		cq = getDownNodeSet("cq").size();
	}

	@Override
	public void applyRuleHandler(Report report, Node signature) {
		if (report.isNegative())
			return;

		Set<Support> originSupports = ((Proposition) this.getSemantic())
				.getOriginSupport();
		Report reply = new Report(report.getSubstitutions(), Support.combine(
				originSupports, report.getSupports()), true,
				report.getContextID());
		for (Channel outChannel : outgoingChannels)
			outChannel.addReport(reply);
	}

	@Override
	protected void sendRui(RuleUseInfo tRui, int context) {
	}

	public int getAnt() {
		return ant;
	}

	public int getCq() {
		return cq;
	}

	@Override
	public NodeSet getDownAntNodeSet() {
		return this.getDownNodeSet("ant");
	}
}