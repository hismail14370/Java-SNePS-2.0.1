package sneps.actuators;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import sneps.snebr.SNeBR;
import sneps.snebr.Support;
import sneps.network.CaseFrame;
import sneps.network.CustomException;
import sneps.network.Network;
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
			Relation rel1 = Network.defineRelation("result", "Individual", "none", 1);
			RCFP r1 = Network.defineRelationPropertiesForCF(rel1, "none", 1);
			Relation rel2 = Network.defineRelation("operation", "Individual", "none", 1);
			RCFP r2 = Network.defineRelationPropertiesForCF(rel2, "none", 1);
			NodeSet nodes = n.getDownCableSet().getDownCable("obj").getNodeSet();
			int result = 0;
			Object[][] array = new Object[nodes.size() + 2][2];
			for(int i = 0; i < nodes.size(); i++) {
				try {
					result += Integer.parseInt(nodes.getNode(i).getIdentifier());
					array[i + 2][0] = Relation.obj;
					array[i + 2][1] = nodes.getNode(i);
				} catch(NumberFormatException e) {
					System.out.println("Cannot add node: " + nodes.getNode(i).toString());
				}
			}
			Node resultNode = Network.buildBaseNode("" + result, new Individual());
			Node operation = Network.buildBaseNode("Addition", new Individual());
			array[0][0] = rel1;
			array[0][1] = resultNode;
			array[1][0] = rel2;
			array[1][1] = operation;
			LinkedList<RCFP> relationSet = new LinkedList<>();
			relationSet.add(r1);
			relationSet.add(r2);
			relationSet.add(RCFP.obj);
			CaseFrame caseFrame;
			try {
				caseFrame = Network.defineCaseFrame("Proposition", relationSet);
			} catch(CustomException e) {
				caseFrame = Network.getCaseFrame("obj,operation,result");
			}
			
			PropositionNode p = (PropositionNode) Network.getMolecularNode(array);
			if(p == null) {
				p = (PropositionNode) Network.buildMolecularNode(array, caseFrame);
			}
			SNeBR.assertProposition(p);
			
			Support s = new Support();
			s.addToOriginSet(p);
			Set<Support> ss = new HashSet<>();
			ss.add(s);
			Report report = new Report(new LinearSubstitutions(), ss, true, SNeBR.getCurrentContext().getId());
			p.broadcastReport(report);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
