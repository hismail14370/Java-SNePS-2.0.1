package sneps.snip.ilp;

import java.util.*;

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
	
	public static MolecularNode antiUnify(MolecularNode m, MolecularNode n) throws CustomException{
		MolecularNode res = null;
		if(!antiUnifiable(m, n)){
			throw new CustomException("Can not anti-unify these two incompatible nodes");
		}
		Set<String> setM = m.getDownCableSet().getDownCables().keySet();
		Set<String> setN = n.getDownCableSet().getDownCables().keySet();
		for(String key : setM){
			
		}
		return res;
	}
	
	// Produces a nodeSet with all the antiUnifing results between two nodeSets
	public static NodeSet produceNodeSetCombinations(NodeSet a, NodeSet b) throws CustomException{
		NodeSet set = new NodeSet();
		for (int i = 0; i < a.size(); i++){
			Node na = a.getNode(i);
			for (int j = 0; j < b.size(); j++){
				Node nb = b.getNode(i);
				if(na.getSyntacticType().equals("Base") && nb.getSyntacticType().equals("Base") && na.getIdentifier().equals(nb.getIdentifier())){
					set.addNode(na);
				}
				else {
					set.addNode(InductiveEngine.gMap.recordSubstitution(na, nb));
				}
			}
		}
		return set;
	}
	
	// Compares The case frame of two nodes
	public static boolean antiUnifiable(MolecularNode m, MolecularNode n){
		CaseFrame cfM = m.getDownCableSet().getCaseFrame();
		CaseFrame cfN = n.getDownCableSet().getCaseFrame();
		return (cfM.equals(cfN));
	}
	
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
		 set1.addNode(abdeltawab);
		 set1.addNode(v1);
		 
		 System.out.println(produceNodeSetCombinations(set, set1));
		 System.out.println(gMap.toString());
		 
	}
}
