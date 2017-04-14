package sneps.snip.ilp.antiUnification;

import java.util.*;

import nu.xom.Nodes;
import sneps.network.*;
import sneps.network.cables.DownCable;
import sneps.network.cables.DownCableSet;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.VariableNode;
import sneps.network.classes.semantic.*;

public class InductiveEngine {
	
	private static GeneralizationMap gMap = new GeneralizationMap();
	
//  Compares The case frame of two nodes
	public static boolean antiUnifiable(MolecularNode m, MolecularNode n){
		CaseFrame cfM = m.getDownCableSet().getCaseFrame();
		CaseFrame cfN = n.getDownCableSet().getCaseFrame();
		return (cfM.equals(cfN));
	}
	
//  Produces a nodeSet with all the antiUnifing results between two nodeSets
	public static NodeSet produceNodeSetCombinations(NodeSet a, NodeSet b) throws CustomException{
		NodeSet resultSet = new NodeSet();
		NodeSet cloneA = new NodeSet();
		NodeSet cloneB = new NodeSet();
		cloneA.addAll(a);
		cloneB.addAll(b);
		for (int i = 0; i < cloneA.size(); i++){
			Node na = cloneA.getNode(i);
			if (na.getSyntacticType().equals("Base")){
				for (int j = 0; j < cloneB.size(); j++){
					Node nb = cloneB.getNode(j);
					if (nb.getSyntacticType().equals("Base")){
						if (nb.getIdentifier().equals(na.getIdentifier())){
							resultSet.addNode(na);
							cloneA.removeNode(na);
							cloneB.removeNode(nb);
							System.out.println("MATCHED: "+ na.getIdentifier()+" - "+nb.getIdentifier());
						}
						else {
							Node variable = Network.buildVariableNode();
							resultSet.addNode(variable);
							System.out.println("MAPPED: "+na.getIdentifier() + " - "+nb.getIdentifier() + " to: "+ variable.getIdentifier());
						}
					}
					if (nb.getSyntacticType().equals("Variable")){
						resultSet.addNode(nb);
						System.out.println("PASSED: "+na.getIdentifier() + " - "+nb.getIdentifier() + " to: "+ nb.getIdentifier());
					}
					else {
						resultSet.addNode(Network.buildVariableNode());
						System.out.println("PASSED: "+na.getIdentifier() + " - "+nb.getIdentifier() + " to: "+ nb.getIdentifier());
					}
				}
			}
			if(na.getSyntacticType().equals("Variable")){
				resultSet.addNode(na);
				System.out.println("DIRECT PASS: "+na.getIdentifier());
			}
			else {
				
				// Recursive call on antiUnify to perform it on two molecular nodes
	
			}	
		}
		return resultSet;
	}
	
	public static MolecularNode antiUnify(MolecularNode m, MolecularNode n) throws CustomException{
		if(!antiUnifiable(m, n)){
			throw new CustomException("Can not anti-unify these two incompatible nodes");
		}
		MolecularNode res = null;
		CaseFrame cf = m.getDownCableSet().getCaseFrame();
		for(String key : m.getDownCableSet().getDownCables().keySet()){
			NodeSet nodeSetM = m.getDownCableSet().getDownCable(key).getNodeSet();
			NodeSet nodeSetN = n.getDownCableSet().getDownCable(key).getNodeSet();
			NodeSet combinationalSet = produceNodeSetCombinations(nodeSetM, nodeSetN);
		}
		return res;
	}
		
//	Main method for testing 
	public static void main(String[] args) throws Exception{
		
		 Relation member = new Relation("member", "Entity", "reduce", 1);
		 Relation cl = new Relation("class", "Entity", "none", 1);
		 		 
		 Entity e = new Entity();
		 Node mohamed = Network.buildBaseNode("Mohamed", e);
		 Node ahmed = Network.buildBaseNode("Ahmed", e);
		 Node student  = Network.buildBaseNode("Student",e);
		 Node abdeltawab = Network.buildBaseNode("Abdeltawab", e);
		 
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
//		 DownCableSet dcSet = new DownCableSet(dcList, cf);
		 
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
		 
		 NodeSet set = new NodeSet();
		 set.addNode(mohamed);
		 set.addNode(ahmed);
		 
		 NodeSet set1 = new NodeSet();
//		 set1.addNode(abdeltawab);
		 set1.addNode(mohamed);
		 set1.addNode(v1);
		 		 
//		 System.out.println(produceNodeSetCombinations(set, set1));
//		 System.out.println(gMap.toString());
//		 antiUnify(node, node1);
	}
}
