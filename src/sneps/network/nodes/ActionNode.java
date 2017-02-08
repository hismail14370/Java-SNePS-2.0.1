package sneps.network.nodes;

import sneps.network.classes.semantic.Action;
import sneps.network.classes.syntactic.Base;
import sneps.actuators.Actuator;

public class ActionNode extends Node {
	
	private Actuator actuator;
	
	public ActionNode(Base syntactic, Action semantic) {
		super(syntactic, semantic);
	}
	
	public ActionNode(String syn, String sem, String name) throws Exception {
		super(syn, sem, name);
	}
	
	public boolean isPremitive() {
		return actuator != null;
	}
	
	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}
	
	public void runActuator(ActNode node) {
		actuator.actOnNode(node);
	}

}
