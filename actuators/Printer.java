package sneps.actuators;

import sneps.actuators.Actuator;
import sneps.network.nodes.ActNode;
import sneps.network.nodes.Node;

public class Printer implements Actuator {

	@Override
	public void actOnNode(ActNode n) {
		Node toPrint = n.getDownCableSet().getDownCable("obj").getNodeSet().getNode(0);
		System.out.println(toPrint);
	}

}
