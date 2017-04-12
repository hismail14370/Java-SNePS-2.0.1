package sneps.network.nodes;

import sneps.network.Network;
import sneps.network.classes.semantic.ControlAction;
import sneps.network.classes.syntactic.Base;
import sneps.actuators.Actuator;
import sneps.actuators.BelieveActuator;
import sneps.actuators.ControlActuator;
import sneps.actuators.DoAllActuator;
import sneps.actuators.DoOneActuator;
import sneps.actuators.SNSequenceActuator;

public class ControlActionNode extends ActionNode {
	
	public static ControlActionNode SNSEQUENCE, SNIF, SNITERATE, ACHIEVE, DO_ONE, DO_ALL, GUARDED_ACT, BELIEVE;

	public ControlActionNode(Base syntactic, ControlAction semantic) {
		super(syntactic, semantic);
	}
	
	public ControlActionNode(String syn, String sem, String name) throws Exception {
		super(syn, sem, name);
	}
	
	public boolean needsDeliberation() {
		switch(this.getIdentifier()) {
		case "SNIF":
		case "SNITERATE":
		case "ACHIEVE":
			return true;
			default: return false;
		}
	}
	
	public void setActuator(Actuator actuator) {
		if(actuator instanceof ControlActuator) {
			super.setActuator(actuator);
		} else {
			System.err.println("Trying to assign an invalid actuator to a control act!!\n"
					+ "Ignoring the actuator assignment...");
		}
	}
	
	public static void initControlActions() {
		//TODO
		try {
			SNSEQUENCE = (ControlActionNode) Network.buildBaseNode("SNSEQUECE", new ControlAction());
			SNSEQUENCE.setActuator(SNSequenceActuator.init());
						
			DO_ONE = (ControlActionNode) Network.buildBaseNode("DO_ONE", new ControlAction());
			DO_ONE.setActuator(DoOneActuator.init());
			
			DO_ALL = (ControlActionNode) Network.buildBaseNode("DO_ALL", new ControlAction());
			DO_ALL.setActuator(DoAllActuator.init());
						
			BELIEVE = (ControlActionNode) Network.buildBaseNode("BELIEVE", new ControlAction());
			BELIEVE.setActuator(BelieveActuator.init());

			SNIF = (ControlActionNode) Network.buildBaseNode("SNIF", new ControlAction());
			
			SNITERATE = (ControlActionNode) Network.buildBaseNode("SNITERATE", new ControlAction());
			
			ACHIEVE = (ControlActionNode) Network.buildBaseNode("ACHIEVE", new ControlAction());
			
			GUARDED_ACT = (ControlActionNode) Network.buildBaseNode("GUARDED_ACT", new ControlAction());

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
