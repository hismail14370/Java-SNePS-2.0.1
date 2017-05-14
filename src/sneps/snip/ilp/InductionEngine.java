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
import sneps.snip.ilp.antiUnification.SetAntiUnifier;
import sneps.snip.ilp.antiUnification.SingularAntiUnifier;
import sneps.snip.ilp.rules.AndEntailment;
import sneps.snip.ilp.rules.OrEntailment;

public class InductionEngine {
	
	NodeSet positiveEvidence;
	NodeSet negativeEvidence;
	NodeSet backgroundKnowledge;
	Hashtable<String, NodeSet> positiveEvidenceTable;
	Hashtable<String, NodeSet> negativeEvidenceTable;
	Hashtable<String, NodeSet> backgroundKnowledgeTable;
	AntiUnification antiUnification;
	HashSet<AndEntailment> andEntailments;
	HashSet<OrEntailment> orEntailments;
	
	public InductionEngine(){
		this.positiveEvidence = new NodeSet();
		this.negativeEvidence = new NodeSet();
		this.backgroundKnowledge = new NodeSet();
		this.positiveEvidenceTable = new Hashtable<>();
		this.negativeEvidenceTable = new Hashtable<>();
		this.backgroundKnowledgeTable = new Hashtable<>();
		this.antiUnification = new AntiUnification();
		this.andEntailments = new HashSet<>();
		this.orEntailments = new HashSet<>();
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
			}
		}
	}
	
	public void addToPositiveEvidence(MolecularNode n){
		this.positiveEvidence.addNode(n);
		if (this.positiveEvidenceTable.get(n.getDownCableSet().getCaseFrame().toString()) == null){
			this.positiveEvidenceTable.put(n.getDownCableSet().getCaseFrame().toString(), new NodeSet());
		}
		this.positiveEvidenceTable.get(n.getDownCableSet().getCaseFrame().toString()).addNode(n);
	}
	
	public void addToNegativeEvidence(MolecularNode n){
		this.negativeEvidence.addNode(n);
		if (this.negativeEvidenceTable.get(n.getDownCableSet().getCaseFrame().toString()) == null){
			this.negativeEvidenceTable.put(n.getDownCableSet().getCaseFrame().toString(), new NodeSet());
		}
		this.negativeEvidenceTable.get(n.getDownCableSet().getCaseFrame().toString()).addNode(n);
	}
	
	public void addToBackgroundKnowledge(MolecularNode n){
		this.backgroundKnowledge.addNode(n);
		if (this.backgroundKnowledgeTable.get(n.getDownCableSet().getCaseFrame().toString()) == null){
			this.backgroundKnowledgeTable.put(n.getDownCableSet().getCaseFrame().toString(), new NodeSet());
		}
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
		return result;
	}
	
	public void induceAndEntailments() throws Exception{
		for (String bgcf : this.backgroundKnowledgeTable.keySet()){
			for (String poscf : this.positiveEvidenceTable.keySet()){
				induceAndEntailment(bgcf, poscf);
				System.out.println("AndEntailments: " + this.andEntailments);
			}
		}
	}
	
	public void specializeByConjuctions(){
		for (AndEntailment a : this.andEntailments){
			for (AndEntailment b : this.andEntailments){
				if (a.equals(b)) continue;
				if (a.combinableAntecedents(b)){
					AndEntailment res = new AndEntailment();
					res.antecedents.addAll(a.antecedents);
					res.antecedents.addAll(b.antecedents);
					res.consequents.addAll(a.consequents);
					this.andEntailments.remove(a);
					this.andEntailments.remove(b);
					this.andEntailments.add(res);
				}
			}
		}
	}
	
	public NodeSet specializeByCommonRelation(NodeSet set, String relationName, Node value){
		NodeSet resultSet = new NodeSet();
		for (int i = 0; i < set.size(); i++){
			MolecularNode m = (MolecularNode) set.getNode(i);
			NodeSet innerSet = m.getDownCableSet().getDownCable(relationName).getNodeSet();
			if (innerSet.contains(value)){
				resultSet.addNode(m);
			}
		}
		return resultSet;
	}
	
	public Hashtable<Pair<String, Node>, NodeSet> getSpecialSets(NodeSet set) throws Exception{
		Hashtable<Pair<String, Node>, NodeSet> res = new Hashtable<>();
		SingularAntiUnifier antiUnifier = this.antiUnification.antiUnify(set);
		for (int i = 0; i < antiUnifier.setAntiUnifiers.size(); i++){
			SetAntiUnifier setAntiUnifier = antiUnifier.setAntiUnifiers.get(i);
			for (int j = 0; j < setAntiUnifier.getGroundInstances().size(); j++){
				Node value = setAntiUnifier.getGroundInstances().getNode(j);
				NodeSet specialSet = this.specializeByCommonRelation(set, setAntiUnifier.relationName, value);
				if (specialSet.size() > 1){
					res.put(new Pair<String, Node>(setAntiUnifier.relationName, value), specialSet);
				}
			}
		}
		return res;
	}
	
	public void induceOrEntailments(){
		for (AndEntailment a : this.andEntailments){
			for (AndEntailment b : this.andEntailments){
				if (a.equals(b)) continue;
				if (a.combinableAntecedents(b)){
					OrEntailment o = new OrEntailment();
					o.antecedents.add(a);
					o.antecedents.add(b);
					o.consequents.addAll(a.consequents);
					this.orEntailments.add(o);
				}
			}
		}
		System.out.println("Or Entailments: " + this.orEntailments);
	}
	
	public void induceAndEntailment(String bgCaseFrame, String posCaseFrame) throws Exception{
		NodeSet bgNodeSet = this.backgroundKnowledgeTable.get(bgCaseFrame);
		NodeSet posNodeSet = this.positiveEvidenceTable.get(posCaseFrame);
		SingularAntiUnifier antiUnifiedBg = this.antiUnification.antiUnify(bgNodeSet);
		System.out.println(antiUnifiedBg);
		SingularAntiUnifier antiUnifiedPosEv = this.antiUnification.antiUnify(posNodeSet);
		System.out.println(antiUnifiedPosEv);
		
		HashSet<Pair<SetAntiUnifier, SetAntiUnifier>> consistencies = antiUnifiedPosEv.getConsistentPairs(antiUnifiedBg);
		System.out.println("Consistent? " + (consistencies.size() != 0));
		if(consistencies.size() != 0){
			System.out.println("On: " + consistencies);
			AndEntailment andEnt = new AndEntailment();
			andEnt.antecedents.add(antiUnifiedBg);
			andEnt.consequents.add(antiUnifiedPosEv);
			this.andEntailments.add(andEnt);
		}
				
		// test regarding negative evidence
		
	}
	
	public void induceAndEntailment(NodeSet bg, NodeSet posEv) throws Exception{
		SingularAntiUnifier antiUnifiedBg = this.antiUnification.antiUnify(bg);
		System.out.println(antiUnifiedBg);
		SingularAntiUnifier antiUnifiedPosEv = this.antiUnification.antiUnify(posEv);
		System.out.println(antiUnifiedPosEv);
		
		HashSet<Pair<SetAntiUnifier, SetAntiUnifier>> consistencies = antiUnifiedPosEv.getConsistentPairs(antiUnifiedBg);
		System.out.println("Consistent? " + (consistencies.size() != 0));
		if(consistencies.size() != 0){
			System.out.println("On: " + consistencies);
			AndEntailment andEnt = new AndEntailment();
			andEnt.antecedents.add(antiUnifiedBg);
			andEnt.consequents.add(antiUnifiedPosEv);
			this.andEntailments.add(andEnt);
		}
		// test regarding negative evidence
	}
	
//	=========== MAIN METHOD FOR TESTING ============
	
	public static void main(String[] args) throws Exception {
		
		Network.defineDefaults();
		
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
		CaseFrame memberClassCaseFrame = Network.defineCaseFrame("Proposition", propList);
		
		RCFP prop2 = Network.defineRelationPropertiesForCF(object, "none", 1);
		RCFP prop3 = Network.defineRelationPropertiesForCF(property, "none", 1);
		LinkedList<RCFP> propList1 = new LinkedList<>();
		propList1.add(prop2);
		propList1.add(prop3);
		CaseFrame objectPropertyCaseFrame = Network.defineCaseFrame("Proposition", propList1);
		
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
		Node animal = Network.buildBaseNode("animal", ent);
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
		
		Object[][] relNodes9 = new Object[2][2];
		relNodes9[0][0] = object;
		relNodes9[0][1] = e;
		relNodes9[1][0] = property;
		relNodes9[1][1] = mortal;
		MolecularNode node9 = Network.buildMolecularNode(relNodes9, objectPropertyCaseFrame);
	
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
		
		Object[][] relNodes10 = new Object[2][2];
		relNodes10[0][0] = member;
		relNodes10[0][1] = c;
		relNodes10[1][0] = cl;
		relNodes10[1][1] = animal;
		MolecularNode node10 = Network.buildMolecularNode(relNodes10, memberClassCaseFrame);
		
		Object[][] relNodes11 = new Object[2][2];
		relNodes11[0][0] = member;
		relNodes11[0][1] = d;
		relNodes11[1][0] = cl;
		relNodes11[1][1] = animal;
		MolecularNode node11 = Network.buildMolecularNode(relNodes11, memberClassCaseFrame);
		
		Object[][] relNodes12 = new Object[2][2];
		relNodes12[0][0] = object;
		relNodes12[0][1] = c;
		relNodes12[1][0] = property;
		relNodes12[1][1] = mortal;
		MolecularNode node12 = Network.buildMolecularNode(relNodes12, objectPropertyCaseFrame);
		
		Object[][] relNodes13 = new Object[2][2];
		relNodes13[0][0] = object;
		relNodes13[0][1] = d;
		relNodes13[1][0] = property;
		relNodes13[1][1] = mortal;
		MolecularNode node13 = Network.buildMolecularNode(relNodes13, objectPropertyCaseFrame);
		
		InductionEngine in = new InductionEngine();
		NodeSet posEv = new NodeSet();
		posEv.addNode(node3);
		posEv.addNode(node2);
		posEv.addNode(node12);
		posEv.addNode(node13);
		in.initializePositiveEvidence(posEv);
		
		NodeSet bgKnow = new NodeSet();
		bgKnow.addNode(node);
		bgKnow.addNode(node1);
		bgKnow.addNode(node10);
		bgKnow.addNode(node11);
		System.out.println(in.getSpecialSets(bgKnow));
		System.out.println("++");
		in.initializeBackgroundKnowledge(bgKnow);
				
		in.induceAndEntailments();
		in.induceOrEntailments();
		
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
