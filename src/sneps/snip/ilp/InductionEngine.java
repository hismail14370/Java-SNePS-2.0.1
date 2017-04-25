package sneps.snip.ilp;

import java.util.Hashtable;
import java.util.LinkedList;

import sneps.network.CaseFrame;
import sneps.network.CustomException;
import sneps.network.Network;
import sneps.network.RCFP;
import sneps.network.Relation;
import sneps.network.classes.semantic.Entity;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.snip.ilp.antiUnification.AntiUnification;

public class InductionEngine {
	
	NodeSet positiveEvidence;
	NodeSet negativeEvidence;
	NodeSet backgroundKnowledge;
	Hashtable<CaseFrame, NodeSet> positiveEvidenceTable;
	Hashtable<CaseFrame, NodeSet> negativeEvidenceTable;
	Hashtable<CaseFrame, NodeSet> backgroundKnowledgeTable;
	AntiUnification antiUnification;
	
	public InductionEngine(){
		this.positiveEvidence = new NodeSet();
		this.negativeEvidence = new NodeSet();
		this.backgroundKnowledge = new NodeSet();
		this.positiveEvidenceTable = new Hashtable<>();
		this.negativeEvidenceTable = new Hashtable<>();
		this.backgroundKnowledgeTable = new Hashtable<>();
		this.antiUnification = new AntiUnification();
	}
	
	public void initializePositiveEvidence(NodeSet set){
		this.positiveEvidence = set;
		this.clusterPositiveEvidence();
	}
	
	public void initializeNegativeEvidence(NodeSet set){
		this.negativeEvidence = set;
		this.clusterNegativeEvidence();
	}
	
	public void initializeBackgroundKnowledge(NodeSet set){
		this.backgroundKnowledge = set;
		this.clusterBackgroundKnowledge();
	}
	
	private void clusterPositiveEvidence(){
		for (int i = 0; i < this.positiveEvidence.size(); i++){
			if (this.positiveEvidence.getNode(i) instanceof MolecularNode){
				MolecularNode n = (MolecularNode) this.positiveEvidence.getNode(i);
				CaseFrame cf = n.getDownCableSet().getCaseFrame();
				if(!this.positiveEvidenceTable.containsKey(cf)){
					this.positiveEvidenceTable.put(cf, new NodeSet());
				}
				this.positiveEvidenceTable.get(cf).addNode(n);
			}
		}
	}
	
	private void clusterNegativeEvidence(){
		for (int i = 0; i < this.positiveEvidence.size(); i++){
			if (this.positiveEvidence.getNode(i) instanceof MolecularNode){
				MolecularNode n = (MolecularNode) this.positiveEvidence.getNode(i);
				CaseFrame cf = n.getDownCableSet().getCaseFrame();
				if(!this.positiveEvidenceTable.containsKey(cf)){
					this.positiveEvidenceTable.put(cf, new NodeSet());
				}
				this.positiveEvidenceTable.get(cf).addNode(n);
			}
		}
	}
	
	private void clusterBackgroundKnowledge(){
		for (int i = 0; i < this.positiveEvidence.size(); i++){
			if (this.positiveEvidence.getNode(i) instanceof MolecularNode){
				MolecularNode n = (MolecularNode) this.positiveEvidence.getNode(i);
				CaseFrame cf = n.getDownCableSet().getCaseFrame();
				if(!this.positiveEvidenceTable.containsKey(cf)){
					this.positiveEvidenceTable.put(cf, new NodeSet());
				}
				this.positiveEvidenceTable.get(cf).addNode(n);
			}
		}
	}
	
//	=========== MAIN METHOD FOR TESTING ============
	
	public static void main(String[] args) throws Exception {
		
		Relation member = Network.defineRelation("member", "Entity", "none", 1);
		Relation cl = Network.defineRelation("class", "Entity", "none", 1);
		Relation love = Network.defineRelation("love", "Entity", "none", 1);
		Relation agent = Network.defineRelation("agent", "Entity", "none", 1);
		
		RCFP prop = Network.defineRelationPropertiesForCF(member, "none", 1);
		RCFP prop1 = Network.defineRelationPropertiesForCF(cl, "none", 1);
		LinkedList<RCFP> propList = new LinkedList<>();
		propList.add(prop);
		propList.add(prop1);
		CaseFrame memberClassCaseFrame = Network.defineCaseFrame("Individual", propList);
		
		RCFP prop2 = Network.defineRelationPropertiesForCF(agent, "none", 1);
		RCFP prop3 = Network.defineRelationPropertiesForCF(love, "none", 1);
		LinkedList<RCFP> propList1 = new LinkedList<>();
		propList1.add(prop2);
		propList1.add(prop3);
		CaseFrame lovesCaseFrame = Network.defineCaseFrame("Proposition", propList1);
		
		Entity ent = new Entity();
		Node a = Network.buildBaseNode("a", ent);
		Node b = Network.buildBaseNode("b", ent);
		Node c = Network.buildBaseNode("c", ent);
		Node d = Network.buildBaseNode("d", ent);
		Node e = Network.buildBaseNode("e", ent);
		Node person = Network.buildBaseNode("person", ent);
		
		Object[][] relNodes = new Object[2][2];
		relNodes[0][0] = member;
		relNodes[0][1] = a;
		relNodes[1][0] = cl;
		relNodes[1][1] = person;
		MolecularNode node = Network.buildMolecularNode(relNodes, memberClassCaseFrame);
		
		Object[][] relNodes1 = new Object[2][2];
		relNodes1[0][0] = member;
		relNodes1[0][1] = b;
		relNodes1[1][0] = cl;
		relNodes1[1][1] = person;
		MolecularNode node1 = Network.buildMolecularNode(relNodes1, memberClassCaseFrame);
		
		Object[][] relNodes2 = new Object[2][2];
		relNodes2[0][0] = member;
		relNodes2[0][1] = c;
		relNodes2[1][0] = cl;
		relNodes2[1][1] = person;
		MolecularNode node2 = Network.buildMolecularNode(relNodes2, memberClassCaseFrame);
		
		Object[][] relNodes3 = new Object[2][2];
		relNodes3[0][0] = member;
		relNodes3[0][1] = d;
		relNodes3[1][0] = cl;
		relNodes3[1][1] = person;
		MolecularNode node3 = Network.buildMolecularNode(relNodes3, memberClassCaseFrame);
	
		Object[][] relNodes5 = new Object[2][2];
		relNodes5[0][0] = agent;
		relNodes5[0][1] = node;
		relNodes5[1][0] = love;
		relNodes5[1][1] = node1;
		MolecularNode node5 = Network.buildMolecularNode(relNodes5, lovesCaseFrame);
		
		Object[][] relNodes6 = new Object[2][2];
		relNodes6[0][0] = agent;
		relNodes6[0][1] = node2;
		relNodes6[1][0] = love;
		relNodes6[1][1] = node3;
		MolecularNode node6 = Network.buildMolecularNode(relNodes6, lovesCaseFrame);
		
		InductionEngine in = new InductionEngine();
		NodeSet set = new NodeSet();
		set.addNode(node);
		set.addNode(node1);
		set.addNode(node2);
		set.addNode(node3);
		set.addNode(node5);
		set.addNode(node6);
		
		in.initializePositiveEvidence(set);
		System.out.println("===");
		System.out.println(in.positiveEvidence);
		System.out.println(in.positiveEvidenceTable);
		in.antiUnification.displayGeneralizationMap();
	}
	
}
