package sneps.network.nodes;

import java.util.ArrayList;

import sneps.network.CaseFrame;
import sneps.network.Network;
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
				VariableNode x = Network.buildVariableNode(new Proposition());
				Relation r1 = Relation.precondition;
				Relation r2 = Relation.act;
				Object[][] array = new Object[2][2];
				array[0][0] = r1;
				array[0][1] = x;
				array[1][0] = r2;
				array[1][1] = this;
				MolecularNode preconditionAct = Network.buildMolecularNode(array, CaseFrame.preconditionAct);
				preconditionAct.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
						SNeBR.getCurrentContext().getId(), this, preconditionAct, true));
				System.out.println("TRYING TO FIND PRECONDITIONS OF " + this);
			} catch(Exception e) {
				System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 1");
				e.printStackTrace();
			}
			break;
		case FIND_PRECONDITIONS:
			ArrayList<PropositionNode> preconditions = new ArrayList<>();
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
					VariableNode y = Network.buildVariableNode(new Proposition());
					Relation r1 = Relation.act;
					Relation r2 = Relation.effect;
					Object[][] array = new Object[2][2];
					array[0][0] = r1;
					array[0][1] = this;
					array[1][0] = r2;
					array[1][1] = y;
					MolecularNode actEffect = Network.buildMolecularNode(array, CaseFrame.actEffect);
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
			preconditions = new ArrayList<>();
			if(preconditionsAsserted) {
				try {
					System.out.println("PRECONDITIONS OF " + this + " ARE SATISFIED!!");
					agenda = Agenda.FIND_EFFECTS;
					Runner.addToActStack(this);
					VariableNode y = Network.buildVariableNode(new Proposition());
					Relation r1 = Relation.act;
					Relation r2 = Relation.effect;
					Object[][] array = new Object[2][2];
					array[0][0] = r1;
					array[0][1] = this;
					array[1][0] = r2;
					array[1][1] = y;
					MolecularNode actEffect = Network.buildMolecularNode(array, CaseFrame.actEffect);
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
					Object[][] array = new Object[preconditions.size() + 1][2];
					Relation r1 = Relation.obj;
					Relation r2 = Relation.action;
					for(int i = 0; i < preconditions.size(); i++) {
						Object[][] array2 = new Object[2][2];
						array2[0][0] = r1;
						array2[0][1] = preconditions.get(i);
						array2[1][0] = r2;
						array2[1][1] = ControlActionNode.ACHIEVE;
						MolecularNode achieve = Network.buildMolecularNode(array2, CaseFrame.act);
						array[i][0] = r1;
						array[i][1] = achieve;
					}
					array[preconditions.size()][0] = r2;
					array[preconditions.size()][1] = ControlActionNode.DO_ALL;
					ActNode doAll = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
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
			ArrayList<PropositionNode> effects = new ArrayList<>();
			//TODO process the reports into the effects array
			System.out.println("NO EFFECTS FOUND FOR " + this);
			if(!effects.isEmpty()) {
				System.out.println("EFFECTS FOUND FOR " + this + " SCHEDULING TO BELIEVE THEM ON THE ACT QUEUE!!");
				try {
					for(PropositionNode effect: effects) {
						Object[][] array = new Object[2][2];
						array[0][0] = Relation.action;
						array[0][1] = ControlActionNode.BELIEVE;
						array[1][0] = Relation.obj;
						array[1][1] = effect;
						ActNode believe = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
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
					VariableNode x = Network.buildVariableNode(new Proposition());
					Relation r1 = Relation.plan;
					Relation r2 = Relation.act;
					Object[][] array = new Object[2][2];
					array[0][0] = r1;
					array[0][1] = x;
					array[1][0] = r2;
					array[1][1] = this;
					MolecularNode planAct = Network.buildMolecularNode(array, CaseFrame.planAct);
					planAct.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
							SNeBR.getCurrentContext().getId(), this, planAct, true));
				} catch(Exception e) {
					System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 6");
				}
			}
			break;
		case FIND_PLANS:
			ArrayList<ActNode> plans = new ArrayList<>();
			//TODO Process the reports into the plans array
			if(!plans.isEmpty()) {
				System.out.println("PLANS WERE FOUND FOR " + this + " SCHEDULING TO EXECUTE A DO-ONE FOR THE PLANS!!");
				try {
					agenda = Agenda.DONE;
					Object[][] array = new Object[plans.size() + 1][2];
					Relation r1 = Relation.obj;
					Relation r2 = Relation.action;
					for(int i = 0; i < plans.size(); i++) {
						array[i][0] = r1;
						array[i][1] = plans.get(i);
					}
					array[plans.size()][0] = r2;
					array[plans.size()][1] = ControlActionNode.DO_ONE;
					ActNode doOne = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
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
						Relation r1 = Relation.plan;
						Relation r2 = Relation.goal;
						Object[][] array = new Object[2][2];
						array[0][0] = r1;
						array[0][1] = x;
						array[1][0] = r2;
						array[1][1] = p;
						MolecularNode planGoal = Network.buildMolecularNode(array, CaseFrame.planGoal);
						planGoal.receiveRequest(new MatchChannel(new LinearSubstitutions(), new LinearSubstitutions(),
								SNeBR.getCurrentContext().getId(), this, planGoal, true));
					} catch(Exception e) {
						System.out.println("SOMETHING WENT WRONG!! EXCEPTION THROWN FROM ACTNODE.JAVA 8");						
					}
				}
				break;
			case FIND_PLANS:
				ArrayList<ActNode> plans = new ArrayList<>();
				//TODO Process the reports into the plans array
				if(!plans.isEmpty()) {
					try {
						agenda = Agenda.DONE;
						Object[][] array = new Object[plans.size() + 1][2];
						Relation r1 = Relation.obj;
						Relation r2 = Relation.action;
						for(int i = 0; i < plans.size(); i++) {
							array[i][0] = r1;
							array[i][1] = plans.get(i);
						}
						array[plans.size()][0] = r2;
						array[plans.size()][1] = ControlActionNode.DO_ONE;
						ActNode doOne = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
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
					ArrayList<ActNode> possibleActs = new ArrayList<>();
//				Process the report buffer into the guards array
					ArrayList<PropositionNode> satisfiedGaurds = new ArrayList<>();
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
						array[i][0] = r1;
						array[i][1] = possibleActs.get(i);
					}
					array[possibleActs.size()][0] = r2;
					array[possibleActs.size()][1] = ControlActionNode.DO_ONE;
					ActNode doOne = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
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
					ArrayList<ActNode> possibleActs = new ArrayList<>();
//				Process the report buffer into the guards array
					ArrayList<PropositionNode> satisfiedGaurds = new ArrayList<>();
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
					Object[][] array = new Object[possibleActs.size() + 2][2];
					Relation r1 = Relation.obj;
					Relation r2 = Relation.action;
					for(int i = 0; i < possibleActs.size(); i++) {
						array[i][0] = r1;
						array[i][1] = possibleActs.get(i);
					}
					array[possibleActs.size() + 1][0] = r2;
					array[possibleActs.size() + 1][1] = ControlActionNode.DO_ONE;
					ActNode doOne = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
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
