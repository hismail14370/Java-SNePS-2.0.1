package sneps.actuators;

import java.util.Stack;

import sneps.network.cables.DownCable;
import sneps.network.nodes.ActNode;
import sneps.snip.Runner;

public class SNSequenceActuator extends ControlActuator {
	
	private static SNSequenceActuator actuator;
	
	private SNSequenceActuator() {
		
	}

	@Override
	public void actOnNode(ActNode n) {
		Stack<ActNode> acts = new Stack();
		int i = 1;
		DownCable next = n.getDownCableSet().getDownCable("obj" + i);
		ActNode act;
		while(next != null) {
			act = (ActNode) next.getNodeSet().getNode(0);
			act.restartAgenda();
			acts.push(act);
			next = n.getDownCableSet().getDownCable("obj" + ++i);
		}
		while(!acts.isEmpty()) {
			Runner.addToActStack(acts.pop());
		}
	}
	
	public static SNSequenceActuator init() {
		if(actuator == null) {
			actuator = new SNSequenceActuator();
		}
		return actuator;
	}

}
