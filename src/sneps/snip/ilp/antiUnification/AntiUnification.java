package sneps.snip.ilp.antiUnification;

import java.util.*;

import nu.xom.Nodes;
import sneps.network.*;
import sneps.network.cables.DownCable;
import sneps.network.cables.DownCableSet;
import sneps.network.nodes.ClosedNode;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.PatternNode;
import sneps.network.nodes.VariableNode;
import sneps.network.classes.semantic.*;

public class AntiUnification {
	
//	private GeneralizationTree gTree;
	private GeneralizationMap gMap;
	
	public AntiUnification(){
//		this.gTree = new GeneralizationTree();
		this.gMap = new GeneralizationMap();
	}
	
//  Compares The case frame of two nodes
	public boolean antiUnifiable(MolecularNode m, MolecularNode n){
		CaseFrame cfM = m.getDownCableSet().getCaseFrame();
		CaseFrame cfN = n.getDownCableSet().getCaseFrame();
		return (cfM.equals(cfN));
	}
	
//  Produces a nodeSet with all the antiUnifing results between two nodeSets
	public SetAntiUnifier produceNodeSetCombinations(NodeSet a, NodeSet b, String relationName) throws CustomException{
		NodeSet resultSet = new NodeSet();
		SetAntiUnifier result = new SetAntiUnifier(relationName);
		NodeSet cloneA = new NodeSet();
		NodeSet cloneB = new NodeSet();
		cloneA.addAll(a);
		cloneB.addAll(b);
		for (int i = 0; i < cloneA.size(); i++){
			Node na = cloneA.getNode(i);
			for (int j = 0; j < cloneB.size(); j++){
				Node nb = cloneB.getNode(j);
				if(na.getIdentifier().equals(nb.getIdentifier())){
					result.addConstant(na);
					resultSet.addNode(na);
				}
				else {
					if ((na instanceof MolecularNode) && (nb instanceof MolecularNode) && 
							(antiUnifiable((MolecularNode) na, (MolecularNode) nb))){
						try {
							SingularAntiUnifier ant = antiUnify((MolecularNode) na, (MolecularNode) nb);
							result.addMolecular(ant);
							resultSet.addNode(ant.antiUnifierNode);
						}
						catch (Exception e){
							System.out.println(na + " <-> " + nb + " threw exception: ");
							System.out.println(e.getMessage());
							continue;
						}
					}
					else {
						VariableNode variable = this.gMap.getGeneralization(na, nb);
						resultSet.addNode(this.gMap.getGeneralization(na, nb));
						result.addVariable(variable, na, nb);
					}
				}
			}
		}
		System.out.println(result);
		return result;
	}
	
	public SingularAntiUnifier antiUnify(MolecularNode m, MolecularNode n) throws Exception{
		if(!this.antiUnifiable(m, n)){
			throw new CustomException("Can not anti-unify these two incompatible nodes");
		}
		ArrayList<SetAntiUnifier> setAntiUnifierList = new ArrayList<>();
		MolecularNode res = null;
		CaseFrame cf = m.getDownCableSet().getCaseFrame();
		Hashtable<Relation, NodeSet> relNodesTable = new Hashtable<>();
		int numberOfNodes = 0;
		for (RCFP rcfp : cf.getRelations().values()){
			Relation r = rcfp.getRelation();
			NodeSet setm = m.getDownCableSet().getDownCable(r.getName()).getNodeSet();
			NodeSet setn = n.getDownCableSet().getDownCable(r.getName()).getNodeSet();
			SetAntiUnifier setAntiUnifier = produceNodeSetCombinations(setm, setn, r.toString());
			setAntiUnifierList.add(setAntiUnifier);
			NodeSet resultSet = setAntiUnifier.getNodes();
			relNodesTable.put(r, resultSet);
			numberOfNodes += resultSet.size();
		}
//		System.out.println(relNodesTable);
		Object[][] relNodes = new Object[numberOfNodes][2];
		int j = 0;
		for (Relation r : relNodesTable.keySet()){
			NodeSet currentSet = relNodesTable.get(r);
//			System.out.println(currentSet);
			for (int k = 0; k < currentSet.size(); k++){
				relNodes[j][0] = r;
				relNodes[j][1] = currentSet.getNode(k);
				j++;
			}
		}
		if (Network.getMolecularNode(relNodes) != null){
			res = Network.getMolecularNode(relNodes);
		}
		else {
			res = Network.buildMolecularNode(relNodes, cf);
		}
		System.out.println("Anti-unifying: " + m + " <---> " + n + "  To: " + res);
		SingularAntiUnifier antiUnifier = new SingularAntiUnifier(res, setAntiUnifierList);
//		System.out.println(antiUnifier);
		return antiUnifier;
	}
	
	public SingularAntiUnifier antiUnify(NodeSet set) throws Exception{
		MolecularNode m = (MolecularNode) set.getNode(0);
		SingularAntiUnifier res = new SingularAntiUnifier(null, new ArrayList<>());
		for (String r : m.getDownCableSet().getCaseFrame().getRelations().keySet()){
			res.setAntiUnifiers.add(new SetAntiUnifier(r));
		}
		for (int i = 0; i < set.size() - 1; i++){
			MolecularNode n = (MolecularNode) set.getNode(i+1);
			if (this.antiUnifiable(m, n)){
				System.out.println(m + " <---> " + n);
				SingularAntiUnifier temp = this.antiUnify(m, n);
				m = temp.antiUnifierNode;
				res.combine(temp);
//				m = this.antiUnify(m, n).node;
			}
		}
		return res;
	}
	
	public void displayGeneralizationMap(){
		this.gMap.displayMap();
	}
		
//	=========== MAIN METHOD FOR TESTING ============
	
	public static void main(String[] args) throws Exception{
		
		 Relation member = new Relation("member", "Entity", "reduce", 1);
		 Relation cl = new Relation("class", "Entity", "none", 1);
		 Relation brother = new Relation("brothers", "Entity", "none", 2);

		 Entity e = new Entity();
		 Node mohamed = Network.buildBaseNode("Mohamed", e);
		 Node ahmed = Network.buildBaseNode("Ahmed", e);
		 Node abdeltawab = Network.buildBaseNode("Abdeltawab", e);
		 Node omar = Network.buildBaseNode("omar", e);
		 
		 Node student  = Network.buildBaseNode("Student",e);
		 
		 Node v1 = Network.buildVariableNode();
		 Node v2 = Network.buildVariableNode();
		 Node v3 = Network.buildVariableNode();
		 Node v4 = Network.buildVariableNode();

		 RCFP prop = Network.defineRelationPropertiesForCF(member, "none", 1);
		 RCFP prop1 = Network.defineRelationPropertiesForCF(cl, "none", 1);
		 LinkedList<RCFP> propList = new LinkedList<>();
		 propList.add(prop);
		 propList.add(prop1);
		 
		 CaseFrame cf = Network.defineCaseFrame("Individual", propList);
		 
		 RCFP prop2 = Network.defineRelationPropertiesForCF(brother, "none", 1);
		 LinkedList<RCFP> propList1 = new LinkedList<>();
		 propList1.add(prop2);
		 
		 CaseFrame cf1 = Network.defineCaseFrame("Individual", propList1);
		 
		 Object [][] relNodes = new Object[3][2];
		 relNodes [0][0] = member;
		 relNodes [0][1] = mohamed;
		 relNodes [1][0] = member;
		 relNodes [1][1] = ahmed;
		 relNodes [2][0] = cl;
		 relNodes [2][1] = student;
		 MolecularNode node = Network.buildMolecularNode(relNodes, cf);
		 
		 Object [][] relNodes1 = new Object[2][2];
		 relNodes1 [0][0] = member;
		 relNodes1 [0][1] = ahmed;
		 relNodes1 [1][0] = cl;
		 relNodes1 [1][1] = student;
		 MolecularNode node1 = Network.buildMolecularNode(relNodes1, cf);

		 Object relNodes2 [][] = new Object[4][2];
		 relNodes2[0][0] = member;
		 relNodes2[0][1] = mohamed;
		 relNodes2[1][0] = member;
		 relNodes2[1][1] = ahmed;
		 relNodes2[2][0] = member;
		 relNodes2[2][1] = abdeltawab;
		 relNodes2[3][0] = cl;
		 relNodes2[3][1] = student;
		 MolecularNode node2 = Network.buildMolecularNode(relNodes2, cf);
		 
		 Object relNodes3 [][] = new Object[3][2];
		 relNodes3 [0][0] = member;
		 relNodes3 [0][1] = v1;
		 relNodes3 [1][0] = member;
		 relNodes3 [1][1] = omar;
		 relNodes3 [2][0] = cl;
		 relNodes3 [2][1] = student;
		 MolecularNode node3 = Network.buildMolecularNode(relNodes3, cf);
		 
		 Object[][] relNodes4 = new Object[2][2];
		 relNodes4[0][0] = brother;
		 relNodes4[0][1] = mohamed;
		 relNodes4[1][0] = brother;
		 relNodes4[1][1] = ahmed;
		 
		 MolecularNode node4 = Network.buildMolecularNode(relNodes4, cf1);
		 

		 Object[][] relNodes5 = new Object[2][2];
		 relNodes5[0][0] = brother;
		 relNodes5[0][1] = omar;
		 relNodes5[1][0] = brother;
		 relNodes5[1][1] = abdeltawab;
		 
		 MolecularNode node5 = Network.buildMolecularNode(relNodes5, cf1);
		 
		 NodeSet set = new NodeSet();
		 set.addNode(mohamed);
		 set.addNode(ahmed);
		 
		 NodeSet set1 = new NodeSet();
		 set1.addNode(abdeltawab);
		 set1.addNode(mohamed);
		 set1.addNode(v1);
		 		 
		 AntiUnification a1 = new AntiUnification();
		 
		 System.out.println("===================");
		 		
//		 System.out.println(node4);
//		 System.out.println(node5);
//		 System.out.println(a1.antiUnify(node4, node5));
		 
//		 a1.antiUnify(node, node2);
		 NodeSet set2 = new NodeSet();
		 set2.addNode(node);
		 set2.addNode(node1);
		 set2.addNode(node2);
		 System.out.println(a1.antiUnify(set2));
		 System.out.println("===================");
		 System.out.println("Generalization Map: ");
		 System.out.println(a1.gMap);

	}
}
