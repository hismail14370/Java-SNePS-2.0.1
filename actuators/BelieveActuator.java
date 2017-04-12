package sneps.actuators;

import java.util.HashSet;
import java.util.Set;

import sneps.snip.matching.LinearSubstitutions;
import sneps.network.nodes.ActNode;
import sneps.network.nodes.PropositionNode;
import sneps.snebr.SNeBR;
import sneps.snebr.Support;
import sneps.snip.Report;

public class BelieveActuator extends ControlActuator {
	
	private static BelieveActuator actuator;
	
	private BelieveActuator() {
		
	}

	@Override
	public void actOnNode(ActNode n) {
		PropositionNode p = (PropositionNode) n.getDownCableSet().getDownCable("obj").getNodeSet().getNode(0);
		SNeBR.assertProposition(p);
		
		Support s1 = new Support();
		s1.addToOriginSet(p);
		Set<Support> ss = new HashSet();
		ss.add(s1);
		Report r = new Report(new LinearSubstitutions(), ss, true, SNeBR.getCurrentContext().getId());
		p.broadcastReport(r);
		
	}
	
	public static BelieveActuator init() {
		if(actuator == null) {
			actuator = new BelieveActuator();
		}
		return actuator;
	}
	
}
