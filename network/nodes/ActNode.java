package sneps.network.nodes;

import java.util.ArrayList;

import sneps.network.RelationsRestrictedCaseFrame;
import sneps.network.Network; 
import sneps.network.Wire;
import sneps.network.Relation;
import sneps.network.classes.semantic.Act;
import sneps.network.classes.semantic.Proposition;
import sneps.network.classes.syntactic.Molecular;
import sneps.snip.matching.LinearSubstitutions;
import sneps.snebr.SNeBR;
import sneps.snip.channels.Channel;
import sneps.snip.channels.MatchChannel;
import sneps.snip.Runner;

public class ActNode extends MolecularNode {

	private Agenda agenda;

	public ActNode(Molecular syntactic, Act semantic) {
		super(syntactic, semantic);
		agenda = Agenda.DONE;
	}

	public void processIntends() {
		ActionNode action = (ActionNode) getDownCableSet().getDownCable("action").getNodeSet().getNode(0);
		if(action instanceof ControlActionNode) {
			processControlIntend((ControlActionNode) action);
			return;
		}
		switch(agenda) {
		case START:
			try {
				agenda = Agenda.FIND_PRECONDITIONS;
				Runner.addToActStack(this);
			

				ArrayList<Wire> wires = new ArrayList();
				Wire w1 = new Wire(Relation.precondition,new Proposition());
				Wire w2 = new Wire(Relation.act,this); 

				wires.add(w1);
				wires.add(w2);
				MolecularNode preconditionAct = Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.preconditionAct);
				preconditionAct.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
						SNeBR.getCurrentContext().getId(), this, preconditionAct, true));
				System.out.println("TRYING TO FIND PRECONDITIONS OF " + this);
			} catch(Exception e) {
				System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 1");
				e.printStackTrace();
			}
			break;
		case FIND_PRECONDITIONS:
			ArrayList<PropositionNode> preconditions = new ArrayList();
			//TODO process each report from the report buffer into the NodeSet
			if(!preconditions.isEmpty()) {
				agenda = Agenda.TEST;
				Runner.addToActStack(this);
				for(PropositionNode precondition: preconditions) {
					precondition.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
							SNeBR.getCurrentContext().getId(), this, precondition, true));
				}
				System.out.println("TRYING TO CHECK IF THE PRECONDITIONS OF " + this + " HOLD");
			} else {
				System.out.println("NO PRECONDITIONS FOUND FOR " + this);
				try {
					agenda = Agenda.FIND_EFFECTS;
					Runner.addToActStack(this);

					
					ArrayList<Wire> wires = new ArrayList();
					Wire w1 = new Wire(Relation.act,this);
					Wire w2 = new Wire(Relation.effect,new Proposition()); 

					wires.add(w1);
					wires.add(w2);
					MolecularNode actEffect = Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.actEffect);
					actEffect.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
							SNeBR.getCurrentContext().getId(), this, actEffect, true));
					System.out.println("TRYING TO FIND EFFECTS OF " + this);
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 2");					
				}
			}
			break;
		case TEST:
			//TODO Check if the reports assert all the preconditions
			boolean preconditionsAsserted = true;
			preconditions = new ArrayList();
			if(preconditionsAsserted) {
				try {
					System.out.println("PRECONDITIONS OF " + this + " ARE SATISFIED!!");
					agenda = Agenda.FIND_EFFECTS;
					Runner.addToActStack(this);
					VariableNode y = Network.buildVariableNode(new Proposition());
			
					ArrayList<Wire> wires = new ArrayList();
					Wire w1 = new Wire(Relation.act,this);
					Wire w2 = new Wire(Relation.effect,new Proposition()); 

					wires.add(w1);
					wires.add(w2);
					MolecularNode actEffect = Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.actEffect);
					actEffect.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
							SNeBR.getCurrentContext().getId(), this, actEffect, true));
					System.out.println("TRYING TO FIND EFFECTS OF " + this);
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 3");					
				}
			} else {
				try{
					agenda = Agenda.START;
					Runner.addToActStack(this);
					ArrayList<Wire> wires2 = new ArrayList();
					Relation r1 = Relation.obj;
					Relation r2 = Relation.action;
					for(int i = 0; i < preconditions.size(); i++) {
						ArrayList<Wire> wires = new ArrayList();
						Wire w1 = new Wire(r1,preconditions.get(i));
						Wire w2 = new Wire(r2,ControlActionNode.ACHIEVE); 

						wires.add(w1);
						wires.add(w2);;
						MolecularNode achieve = Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.act);
					 
						Wire w3 = new Wire(r1,achieve);
						wires2.add(i, w3);
					}
				
					Wire w4 = new Wire(r2,ControlActionNode.DO_ALL);
					wires2.add(preconditions.size(), w4);
					ActNode doAll = (ActNode) Network.buildMolecularNode(wires2, RelationsRestrictedCaseFrame.act);
					doAll.restartAgenda();
					Runner.addToActStack(doAll);
					System.out.println("PRECONDITIONS OF " + this + " ARE NOT SATISFIED!! ATTEMPTING TO ACHIEVE THEM.");
					System.out.println("RESTARTING THE ACT " + this);
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 4");
				}
			}
			break;
		case FIND_EFFECTS:
			ArrayList<PropositionNode> effects = new ArrayList<PropositionNode>();
			//TODO process the reports into the effects array
			System.out.println("NO EFFECTS FOUND FOR " + this);
			if(!effects.isEmpty()) {
				System.out.println("EFFECTS FOUND FOR " + this + " SCHEDULING TO BELIEVE THEM ON THE ACT QUEUE!!");
				try {
					for(PropositionNode effect: effects) {
					
						
						ArrayList<Wire> wires = new ArrayList();
						Wire w1 = new Wire(Relation.action,ControlActionNode.BELIEVE);
						Wire w2 = new Wire(Relation.obj,effect); 

						wires.add(w1);
						wires.add(w2);
						ActNode believe = (ActNode) Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.act);
						believe.restartAgenda();
						Runner.addToActStack(believe);
					}
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 5");
				}
			}
			agenda = Agenda.EXECUTE;
			Runner.addToActStack(this);
			break;
		case EXECUTE:
			if(action.isPremitive()) {
				System.out.println(this + " IS PRIMITIVE, EXECUTING!!");
				agenda = Agenda.DONE;
				//TODO Initiate forward inference to trigger
				action.runActuator(this);
			} else {
				System.out.println(this + " IS NOT PRIMITIVE, TRYING TO FIND PLANS!!");
				try {
					agenda = Agenda.FIND_PLANS;
					Runner.addToActStack(this);
					
					ArrayList<Wire> wires = new ArrayList();
					Wire w1 = new Wire(Relation.plan,new Proposition());
					Wire w2 = new Wire(Relation.act,this); 

					wires.add(w1);
					wires.add(w2);
					
					MolecularNode planAct = Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.planAct);
					planAct.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
							SNeBR.getCurrentContext().getId(), this, planAct, true));
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 6");
				}
			}
			break;
		case FIND_PLANS:
			ArrayList<ActNode> plans = new ArrayList(); 
			ArrayList<Wire> wires = new ArrayList();
			//TODO Process the reports into the plans array
			if(!plans.isEmpty()) {
				System.out.println("PLANS WERE FOUND FOR " + this + " SCHEDULING TO EXECUTE A DO-ONE FOR THE PLANS!!");
				try {
					agenda = Agenda.DONE;
					Relation r1 = Relation.obj;
					Relation r2 = Relation.action;
					for(int i = 0; i < plans.size(); i++) {
						Wire w1 = new Wire(r1,plans.get(i));
						wires.add(i, w1);
					}
					Wire w2 = new Wire(r2,ControlActionNode.DO_ONE);
					wires.add(plans.size(), w2);
					ActNode doOne = (ActNode) Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.act);
					doOne.restartAgenda();
					Runner.addToActStack(doOne);
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 7");
				}
			} else {
				System.out.println("NO PLANS WHERE FOUND FOR " + this + " !!");
			}
			break;
		case DONE:
			break;
		}
	}

	public void processControlIntend(ControlActionNode action) {
		if(!action.needsDeliberation()) {
			action.runActuator(this);
			return;
		}
		if(action == ControlActionNode.ACHIEVE) {
			switch(agenda) {
			case START:
				PropositionNode p = (PropositionNode) getDownCableSet().getDownCable("obj").getNodeSet().getNode(0);
				//TODO Check if p is asserted
				boolean asserted = true;
				if(asserted) {
					return;
				} else {
					try {
						agenda = Agenda.FIND_PLANS;
						Runner.addToActStack(this);
						VariableNode x = Network.buildVariableNode(new Proposition());
					
						ArrayList<Wire> wires = new ArrayList();
						Wire w1 = new Wire(Relation.plan,new Proposition());
						Wire w2 = new Wire(Relation.goal,p); 

						wires.add(w1);
						wires.add(w2);
						
						MolecularNode planGoal = Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.planGoal);
						planGoal.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
								SNeBR.getCurrentContext().getId(), this, planGoal, true));
					} catch(Exception e) {
						System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 8");						
					}
				}
				break;
			case FIND_PLANS:
				ArrayList<ActNode> plans = new ArrayList();
				ArrayList<Wire> wires = new ArrayList();
				//TODO Process the reports into the plans array
				if(!plans.isEmpty()) {
					try {
						agenda = Agenda.DONE;
						Relation r1 = Relation.obj;
						Relation r2 = Relation.action;
						for(int i = 0; i < plans.size(); i++) {
							Wire w1 = new Wire(r1,plans.get(i));
							wires.add(i, w1);
						}
					
						Wire w2 = new Wire(r2,ControlActionNode.DO_ONE);
						wires.add(plans.size(), w2);
						ActNode doOne = (ActNode) Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.act);
						doOne.restartAgenda();
						Runner.addToActStack(doOne);
					} catch(Exception e) {
						System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 9");
					}
				} else {
					//TODO A planning algorithm should be used here
				}
				break;
			default:
				System.out.print("UNIDENTIFIED AGENDA FOR ACHIEVE!!");
				return;
			}
		} else if(action == ControlActionNode.SNIF) {
			switch(agenda) {
			case START:
				agenda = Agenda.TEST;
				Runner.addToActStack(this);
				NodeSet guards = new NodeSet();
				for(Node n: getDownCableSet().getDownCable("obj").getNodeSet()) {
					guards.addAll(((MolecularNode) n).getDownCableSet().getDownCable("guard").getNodeSet());
				}
				for(Node guard: guards) {
					guard.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
							SNeBR.getCurrentContext().getId(), this, guard, true));
				}
				break;
			case TEST:
				try{
					agenda = Agenda.DONE;
					NodeSet allActs = getDownCableSet().getDownCable("obj").getNodeSet();
					ArrayList<ActNode> possibleActs = new ArrayList(); 
					ArrayList<Wire> wires = new ArrayList();
//				Process the report buffer into the guards array
					ArrayList<PropositionNode> satisfiedGaurds = new ArrayList();
					for(Node act: allActs) {
						boolean containsAll = true;
						for(Node n: ((MolecularNode) act).getDownCableSet().getDownCable("guard").getNodeSet()) {
							if(!satisfiedGaurds.contains(n)) {
								containsAll = false;
								break;
							}
						}
						if(containsAll) {
							possibleActs.add((ActNode) act);
						}
					}
					Object[][] array = new Object[possibleActs.size() + 1][2];
					Relation r1 = Relation.obj;
					Relation r2 = Relation.action;
					for(int i = 0; i < possibleActs.size(); i++) {
			
						Wire w1 = new Wire(r1,possibleActs.get(i));
						wires.add(i, w1);
					}
		 
					Wire w2 = new Wire(r2,ControlActionNode.DO_ONE); 
					wires.add(possibleActs.size(), w2);
					ActNode doOne = (ActNode) Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.act);
					doOne.restartAgenda();
					Runner.addToActStack(doOne);
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 10");
				}
				break;
			default:
				System.out.print("UNIDENTIFIED AGENDA FOR ACHIEVE!!");
				return;
			}
		} else if(action == ControlActionNode.SNITERATE) {
			switch(agenda) {
			case START:
				agenda = Agenda.TEST;
				Runner.addToActStack(this);
				NodeSet guards = new NodeSet();
				for(Node n: getDownCableSet().getDownCable("obj").getNodeSet()) {
					guards.addAll(((MolecularNode) n).getDownCableSet().getDownCable("guard").getNodeSet());
				}
				for(Node guard: guards) {
					guard.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
							SNeBR.getCurrentContext().getId(), this, guard, true));
				}
				break;
			case TEST:
				try{
					agenda = Agenda.DONE;
					NodeSet allActs = getDownCableSet().getDownCable("obj").getNodeSet();
					ArrayList<ActNode> possibleActs = new ArrayList();
					ArrayList<Wire> wires = new ArrayList();
//				Process the report buffer into the guards array
					ArrayList<PropositionNode> satisfiedGaurds = new ArrayList();
					for(Node act: allActs) {
						boolean containsAll = true;
						for(Node n: ((MolecularNode) act).getDownCableSet().getDownCable("guard").getNodeSet()) {
							if(!satisfiedGaurds.contains(n)) {
								containsAll = false;
								break;
							}
						}
						if(containsAll) {
							possibleActs.add((ActNode) act);
						}
					}
					Relation r1 = Relation.obj;
					Relation r2 = Relation.action;
					for(int i = 0; i < possibleActs.size(); i++) {
						Wire w1 = new Wire(r1,possibleActs.get(i));
						wires.add(i, w1);
					}
	 
					Wire w2 = new Wire(r2,ControlActionNode.DO_ONE);
					wires.add(w2);
					ActNode doOne = (ActNode) Network.buildMolecularNode(wires, RelationsRestrictedCaseFrame.act);
					restartAgenda();
					doOne.restartAgenda();
					Runner.addToActStack(this);
					Runner.addToActStack(doOne);
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 11");
				}
				break;
			default:
				System.out.print("UNIDENTIFIED AGENDA FOR ACHIEVE!!");
				return;
			}
		} else {
			System.out.println("UNIDENTIFIED CONTROL ACT; IDENTIFIER: " + action.getIdentifier());
		}
	}
	
	public void receiveRequest(Channel channel) {
		receiveIntend(channel);
	}

	public void receiveIntend(Channel channel) {
		incomingChannels.addChannel(channel);
		Runner.addToActStack(this);
	}

	public void perform() {
		Runner.initiate();
		restartAgenda();
//		Should send an intend message instead of just scheduling the act
		Runner.addToActStack(this);
		System.out.println("Final Runner sequence: " + Runner.run());
	}
	
	public Agenda getAgenda() {
		return agenda;
	}
	
	public void restartAgenda() {
		agenda = Agenda.START;
	}

}
