package sneps.actuators;

import java.util.Random;

import sneps.network.nodes.ActNode;
import sneps.network.nodes.NodeSet;
import sneps.snip.Runner;

public class DoOneActuator extends ControlActuator {
	
	private static DoOneActuator actuator;
	
	private DoOneActuator() {
		
	}

	@Override
	public void actOnNode(ActNode n) {
		Random rand = new Random();
		NodeSet possibleActs = n.getDownCableSet().getDownCable("obj").getNodeSet();
		int actIndex = rand.nextInt(possibleActs.size());
		ActNode act = (ActNode) possibleActs.getNode(actIndex);
		act.restartAgenda();
		Runner.addToActStack(act);
	}
	
	public static DoOneActuator init() {
		if(actuator == null) {
			actuator = new DoOneActuator();
		}
		return actuator;
	}

}
