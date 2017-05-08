package sneps.snip.ilp;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;

import sneps.network.CaseFrame;
import sneps.network.CustomException;
import sneps.network.Network;
import sneps.network.RCFP;
import sneps.network.Relation;
import sneps.network.cables.DownCable;
import sneps.network.classes.semantic.Entity;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.PatternNode;
import sneps.snip.ilp.antiUnification.AntiUnification;

public class InductionEngine {
	
	NodeSet positiveEvidence;
	NodeSet negativeEvidence;
	NodeSet backgroundKnowledge;
	Hashtable<String, NodeSet> positiveEvidenceTable;
	Hashtable<String, NodeSet> negativeEvidenceTable;
	Hashtable<String, NodeSet> backgroundKnowledgeTable;
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
	
	public InductionEngine(NodeSet positiveEvidence, NodeSet negativeEvidence, NodeSet backgroundKnowledge){
		this.positiveEvidenceTable = new Hashtable<>();
		this.negativeEvidenceTable = new Hashtable<>();
		this.backgroundKnowledgeTable = new Hashtable<>();
		this.initializePositiveEvidence(positiveEvidence);
		this.initializeNegativeEvidence(negativeEvidence);
		this.initializeBackgroundKnowledge(backgroundKnowledge);
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
				if(!this.positiveEvidenceTable.containsKey(cf.toString())){
					this.positiveEvidenceTable.put(cf.toString(), new NodeSet());
				}
				this.positiveEvidenceTable.get(cf.toString()).addNode(n);
//				Hashtable<String, NodeSet> table = getAllSubInstances(n);
//				for (String s1 : table.keySet()){
//					if (this.positiveEvidenceTable.get(s1) == null){
//						this.positiveEvidenceTable.put(s1, new NodeSet());
//					}
//					this.positiveEvidenceTable.get(s1).addAll(table.get(s1));
//				}
			}
		}
	}
	
	private void clusterNegativeEvidence(){
		for (int i = 0; i < this.negativeEvidence.size(); i++){
			if (this.negativeEvidence.getNode(i) instanceof MolecularNode){
				MolecularNode n = (MolecularNode) this.negativeEvidence.getNode(i);
				CaseFrame cf = n.getDownCableSet().getCaseFrame();
				if(!this.negativeEvidenceTable.containsKey(cf.toString())){
					this.negativeEvidenceTable.put(cf.toString(), new NodeSet());
				}
				this.negativeEvidenceTable.get(cf.toString()).addNode(n);
//				Hashtable<String, NodeSet> table = getAllSubInstances(n);
//				for (String s1 : table.keySet()){
//					if (this.negativeEvidenceTable.get(s1) == null){
//						this.negativeEvidenceTable.put(s1, new NodeSet());
//					}
//					this.negativeEvidenceTable.get(s1).addAll(table.get(s1));
//				}
			}
		}
	}
	
	private void clusterBackgroundKnowledge(){
		for (int i = 0; i < this.backgroundKnowledge.size(); i++){
			if (this.backgroundKnowledge.getNode(i) instanceof MolecularNode){
				MolecularNode n = (MolecularNode) this.backgroundKnowledge.getNode(i);
				CaseFrame cf = n.getDownCableSet().getCaseFrame();
				if(!this.backgroundKnowledgeTable.containsKey(cf.toString())){
					this.backgroundKnowledgeTable.put(cf.toString(), new NodeSet());
				}
				this.backgroundKnowledgeTable.get(cf.toString()).addNode(n);
//				Hashtable<String, NodeSet> table = getAllSubInstances(n);
//				for (String s1 : table.keySet()){
//					if (this.backgroundKnowledgeTable.get(s1) == null){
//						this.backgroundKnowledgeTable.put(s1, new NodeSet());
//					}
//					this.backgroundKnowledgeTable.get(s1).addAll(table.get(s1));
//				}
			}
		}
	}
	
	public HashSet<CaseFrame> getCommonCaseFrames(MolecularNode m, MolecularNode n){
		HashSet<CaseFrame> result = new HashSet<>();
		if (m.getDownCableSet().getCaseFrame().equals(n.getDownCableSet().getCaseFrame())){
			result.add(m.getDownCableSet().getCaseFrame());
		}
		for (String r : n.getDownCableSet().getDownCables().keySet()){
			NodeSet set = n.getDownCableSet().getDownCable(r).getNodeSet();
			for (int i = 0; i < set.size(); i++){
				if (set.getNode(i) instanceof MolecularNode){
					result.addAll(getCommonCaseFrames(m, (MolecularNode)set.getNode(i)));
				}
			}
		}
		for (String r : m.getDownCableSet().getDownCables().keySet()){
			NodeSet set = m.getDownCableSet().getDownCable(r).getNodeSet();
			for (int i = 0; i < set.size(); i++){
				if (set.getNode(i) instanceof MolecularNode){
					result.addAll(getCommonCaseFrames(n, (MolecularNode)set.getNode(i)));
				}
			}
		}
		return result;
	}
	
	public void addToPositiveEvidence(MolecularNode n){
		this.positiveEvidence.addNode(n);
		this.positiveEvidenceTable.get(n.getDownCableSet().getCaseFrame().toString()).addNode(n);
	}
	
	public void addToNegativeEvidence(MolecularNode n){
		this.negativeEvidence.addNode(n);
		this.negativeEvidenceTable.get(n.getDownCableSet().getCaseFrame().toString()).addNode(n);
	}
	
	public void addToBackgroundKnowledge(MolecularNode n){
		this.backgroundKnowledge.addNode(n);
		this.backgroundKnowledgeTable.get(n.getDownCableSet().getCaseFrame().toString()).addNode(n);
	}
	
	public static void displayNodeSet(NodeSet set){
		for (int i = 0; i < set.size(); i++){
			System.out.println(set.getNode(i));
		}
	}
	
	public static void displayNodeSetTable(Hashtable<String, NodeSet> table){
		for (String s : table.keySet()){
			System.out.println(s + " = " + table.get(s));
		}
	}
	
	public Hashtable<String, NodeSet> getAllSubInstances(MolecularNode n){
		Hashtable<String, NodeSet> result = new Hashtable<>();
		for (String s : n.getDownCableSet().getDownCables().keySet()){
			DownCable dc = n.getDownCableSet().getDownCable(s);
			NodeSet set = dc.getNodeSet();
			for (int i = 0; i < set.size(); i++){
				Node node = set.getNode(i);
				if(set.getNode(i) instanceof MolecularNode){
					MolecularNode m = (MolecularNode) node;
					if (result.get(m.getDownCableSet().getCaseFrame().toString()) == null){
						result.put(m.getDownCableSet().getCaseFrame().toString(), new NodeSet());
					}
					NodeSet innerSet = result.get(m.getDownCableSet().getCaseFrame().toString());
					innerSet.addNode(m);
					Hashtable<String, NodeSet> innerTable = getAllSubInstances(m);
					for (String s1 : innerTable.keySet()){
						if (result.get(s1) == null){
							result.put(s1, new NodeSet());
						}
						result.get(s1).addAll(innerTable.get(s1));
					}
				}
				if(set.getNode(i).getSyntacticType().equals("Base")){
					if (result.get("BaseNode") == null){
						result.put("BaseNode", new NodeSet());
					}
					result.get("BaseNode").addNode(node);
				}
				if(set.getNode(i).getSyntacticType().equals("Variable")){
					if (result.get("VariableNode") == null){
						result.put("VariableNode", new NodeSet());
					}
					result.get("VariableNode").addNode(node);
				}
			}
		}
//		System.out.println(result);
		return result;
	}
	
	public void induceEntailment(CaseFrame bgCaseFrame, CaseFrame posCaseFrame) throws Exception{
		
		NodeSet bgNodeSet = this.backgroundKnowledgeTable.get(bgCaseFrame.toString());
		NodeSet posNodeSet = this.positiveEvidenceTable.get(posCaseFrame.toString());
		MolecularNode antiUnifiedBg = this.antiUnification.antiUnify(bgNodeSet);
		System.out.println(antiUnifiedBg);
		MolecularNode antiUnifiedPosEv = this.antiUnification.antiUnify(posNodeSet);
		System.out.println(antiUnifiedPosEv);
		
		// test for common variables.. anti-unifier data structure.. in progress
		// Save in candidate hypothesis.. will we?
		// test regarding negative evidence
		// if test with negative evidence fails, specialize.. cluster by a common relation or conjuctions
		// eventually, save to candidate hypotheses
		
	}
	
//	=========== MAIN METHOD FOR TESTING ============
	
	public static void main(String[] args) throws Exception {
		
		Relation member = Network.defineRelation("member", "Entity", "none", 1);
		Relation cl = Network.defineRelation("class", "Entity", "none", 1);
		Relation object = Network.defineRelation("object", "Entity", "none", 1);
		Relation property = Network.defineRelation("property", "Entity", "none", 1);
		Relation love = Network.defineRelation("love", "Entity", "none", 1);
		Relation agent = Network.defineRelation("agent", "Entity", "none", 1);
		
		RCFP prop = Network.defineRelationPropertiesForCF(member, "none", 1);
		RCFP prop1 = Network.defineRelationPropertiesForCF(cl, "none", 1);
		LinkedList<RCFP> propList = new LinkedList<>();
		propList.add(prop);
		propList.add(prop1);
		CaseFrame memberClassCaseFrame = Network.defineCaseFrame("Individual", propList);
		
		RCFP prop2 = Network.defineRelationPropertiesForCF(object, "none", 1);
		RCFP prop3 = Network.defineRelationPropertiesForCF(property, "none", 1);
		LinkedList<RCFP> propList1 = new LinkedList<>();
		propList1.add(prop2);
		propList1.add(prop3);
		CaseFrame objectPropertyCaseFrame = Network.defineCaseFrame("Individual", propList1);
		
		RCFP prop4 = Network.defineRelationPropertiesForCF(agent, "none", 1);
		RCFP prop5 = Network.defineRelationPropertiesForCF(love, "none", 1);
		LinkedList<RCFP> propList2 = new LinkedList<>();
		propList2.add(prop4);
		propList2.add(prop5);
		CaseFrame lovesCaseFrame = Network.defineCaseFrame("Proposition", propList2);
		
		Entity ent = new Entity();
		Node a = Network.buildBaseNode("a", ent);
		Node b = Network.buildBaseNode("b", ent);
		Node c = Network.buildBaseNode("c", ent);
		Node d = Network.buildBaseNode("d", ent);
		Node e = Network.buildBaseNode("e", ent);
		Node person = Network.buildBaseNode("person", ent);
		Node robot = Network.buildBaseNode("robot", ent);
		Node mortal = Network.buildBaseNode("mortal", ent);
		
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
		
		Object[][] relNodes8 = new Object[2][2];
		relNodes8[0][0] = member;
		relNodes8[0][1] = e;
		relNodes8[1][0] = cl;
		relNodes8[1][1] = robot;
		MolecularNode node8 = Network.buildMolecularNode(relNodes8, memberClassCaseFrame);
		
		Object[][] relNodes2 = new Object[2][2];
		relNodes2[0][0] = object;
		relNodes2[0][1] = a;
		relNodes2[1][0] = property;
		relNodes2[1][1] = mortal;
		MolecularNode node2 = Network.buildMolecularNode(relNodes2, objectPropertyCaseFrame);
		
		Object[][] relNodes3 = new Object[2][2];
		relNodes3[0][0] = object;
		relNodes3[0][1] = b;
		relNodes3[1][0] = property;
		relNodes3[1][1] = mortal;
		MolecularNode node3 = Network.buildMolecularNode(relNodes3, objectPropertyCaseFrame);
	
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
		
		Object[][] relNodes7 = new Object[2][2];
		relNodes7[0][0] = agent;
		relNodes7[0][1] = node3;
		relNodes7[1][0] = love;
		relNodes7[1][1] = node2;
		MolecularNode node7 = Network.buildMolecularNode(relNodes7, lovesCaseFrame);
		
		InductionEngine in = new InductionEngine();
		NodeSet posEv = new NodeSet();
		posEv.addNode(node2);
		posEv.addNode(node3);
		in.initializePositiveEvidence(posEv);
		
		NodeSet bgKnow = new NodeSet();
		bgKnow.addNode(node);
		bgKnow.addNode(node1);
		in.initializeBackgroundKnowledge(bgKnow);
		
		in.induceEntailment(memberClassCaseFrame, objectPropertyCaseFrame);
		
		System.out.println("\n\n====================================");
		System.out.println("Positive Evidence:");
		displayNodeSetTable(in.positiveEvidenceTable);
		System.out.println();
		System.out.println("Background Knowledge:");
		displayNodeSetTable(in.backgroundKnowledgeTable);
		System.out.println("==");
		System.out.println();
		
	}
	
}
