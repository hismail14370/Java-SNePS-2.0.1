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

public class Test {

	public static void main(String[] args) throws Exception {

		Relation member = Network.defineRelation("member", "Entity", "none", 1);
		Relation cl = Network.defineRelation("class", "Entity", "none", 1);
		Relation object = Network.defineRelation("object", "Entity", "none", 1);
		Relation property = Network.defineRelation("property", "Entity", "none", 1);

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

		Entity ent = new Entity();
		Node a = Network.buildBaseNode("a", ent);
		Node b = Network.buildBaseNode("b", ent);
		Node c = Network.buildBaseNode("c", ent);
		Node d = Network.buildBaseNode("d", ent);
		Node e = Network.buildBaseNode("e", ent);
		Node r = Network.buildBaseNode("r", ent);
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

		Object[][] relNodes4 = new Object[2][2];
		relNodes4[0][0] = member;
		relNodes4[0][1] = Network.buildVariableNode();
		relNodes4[1][0] = cl;
		relNodes4[1][1] = robot;
		MolecularNode node4 = Network.buildMolecularNode(relNodes4, memberClassCaseFrame);


	}
}
