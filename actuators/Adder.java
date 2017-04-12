package sneps.actuators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import sneps.snebr.SNeBR;
import sneps.snebr.Support;
import sneps.network.CaseFrame;
import sneps.network.CustomException;
import sneps.network.Network;
import sneps.network.Wire;
import sneps.network.RCFP;
import sneps.network.Relation;
import sneps.network.classes.semantic.Individual;
import sneps.snip.matching.LinearSubstitutions;
import sneps.network.nodes.ActNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.PropositionNode;
import sneps.snip.Report;

public class Adder implements Actuator {

	@Override
	public void actOnNode(ActNode n) {
		try {
			Relation rel1 = Network.defineRelation("result", "Individual");
			Relation rel2 = Network.defineRelation("operation", "Individual");
			NodeSet nodes = n.getDownCableSet().getDownCable("obj").getNodeSet();
			int result = 0; 
			ArrayList<Wire> m1Wires = new ArrayList<Wire>(); 
			for(int i = 0; i < nodes.size(); i++) {
				try {
					result += Integer.parseInt(nodes.getNode(i).getIdentifier());
					Wire w = new Wire(Relation.obj,nodes.getNode(i));
					m1Wires.add(i+2, w);
				} catch(NumberFormatException e) {
					System.out.println("Cannot add node: " + nodes.getNode(i).toString());
				}
			}
			
			
			
			Wire w1 = new Wire(rel1,""+ result, "Base", "Individual");
			Wire w2 = new Wire(rel2, "Addition", "Base", "Individual"); 
			
			m1Wires.add(w1);
			m1Wires.add(w2); 
			
			LinkedList<Relation> relationSet = new LinkedList<Relation>();
			relationSet.add(rel1);
			relationSet.add(rel2);
			CaseFrame caseFrame;
			try {
				caseFrame = Network.defineCaseFrame("Proposition", relationSet);
			} catch(CustomException e) {
				caseFrame = Network.getCaseFrame("obj,operation,result");
			}
			
			PropositionNode p = (PropositionNode) Network.getMolecularNode(m1Wires);
			if(p == null) {
				p = (PropositionNode) Network.buildMolecularNode(m1Wires, caseFrame);
			}
			SNeBR.assertProposition(p);
			
			Support s = new Support();
			s.addToOriginSet(p);
			Set<Support> ss = new HashSet<Support>();
			ss.add(s);
			Report report = new Report(new LinearSubstitutions(), ss, true, SNeBR.getCurrentContext().getId());
			p.broadcastReport(report);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
