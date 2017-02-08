package sneps.actuators;

import java.util.Random;

import sneps.network.nodes.ActNode;
import sneps.network.nodes.NodeSet;
import sneps.snip.Runner;

public class DoAllActuator extends ControlActuator {
	
	private static DoAllActuator actuator;
	
	private DoAllActuator() {
		
	}

	@Override
	public void actOnNode(ActNode n) {
		Random rand = new Random();
		NodeSet acts = n.getDownCableSet().getDownCable("obj").getNodeSet();
		NodeSet actsCopy = new NodeSet();
		actsCopy.addAll(acts);
		while(!actsCopy.isEmpty()) {
			int nextActIndex = rand.nextInt(actsCopy.size());
			ActNode nextAct = (ActNode) actsCopy.getNode(nextActIndex);
			nextAct.restartAgenda();
			Runner.addToActStack(nextAct);
			actsCopy.removeNode(nextAct);
		}
	}
	
	public static DoAllActuator init() {
		if(actuator == null) {
			actuator = new DoAllActuator();
		}
		return actuator;
	}

}
