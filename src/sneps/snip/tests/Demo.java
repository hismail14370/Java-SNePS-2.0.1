package sneps.snip.tests;

import java.util.Enumeration;

import sneps.network.CaseFrame;
import sneps.network.Network;
import sneps.network.Relation;
import sneps.network.nodes.ActNode;
import sneps.network.nodes.ActionNode;
import sneps.network.nodes.ControlActionNode;
import sneps.network.nodes.Node;
import sneps.network.classes.semantic.Action;
import sneps.network.classes.semantic.Individual;
import sneps.actuators.Adder;
import sneps.actuators.Printer;

public class Demo {
	
	private static void printNetwork() {
		System.out.println("-------------------------------------Printing the network------------------------------------------");
		
		Enumeration<Node> nodes = Network.getNodes().elements();
		while(nodes.hasMoreElements()) {
			System.out.println(nodes.nextElement());
		}
		
		System.out.println("---------------------------------------------------------------------------------------------------");
	}
	
	public static void main(String[]args) {
		
		try {
			
			Network.defineDefaults();
			
			System.out.println("Example 1: Printing action");
			
			ActionNode print = (ActionNode) Network.buildBaseNode("PRINT", new Action());
			print.setActuator(new Printer());
			
			Node n1 = Network.buildBaseNode("A very important String", new Individual());
			
			Object[][] array = new Object[2][2];
			array[0][0] = Relation.obj;
			array[0][1] = n1;
			array[1][0] = Relation.action;
			array[1][1] = print;
			
			ActNode printAct = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
			
			printAct.perform();
			
			printNetwork();
			
			System.out.println("Example 2: Adding action");
			
			ActionNode add = (ActionNode) Network.buildBaseNode("ADD", new Action());
			add.setActuator(new Adder());
			
			Node n2 = Network.buildBaseNode("34", new Individual());
			Node n3 = Network.buildBaseNode("55", new Individual());
			
			array = new Object[3][2];
			array[0][0] = Relation.obj;
			array[0][1] = n2;
			array[1][0] = Relation.obj;
			array[1][1] = n3;
			array[2][0] = Relation.action;
			array[2][1] = add;
			
			ActNode addAct = (ActNode) Network.buildMolecularNode(array, CaseFrame.act);
			
			addAct.perform();
			
			printNetwork();
			
			array = new Object[3][2];
			array[0][0] = Relation.action;
			array[0][1] = ControlActionNode.SNSEQUENCE;
			array[1][0] = Relation.obj1;
			array[1][1] = addAct;
			array[2][0] = Relation.obj2;
			array[2][1] = printAct;
			
			ActNode ap = (ActNode) Network.buildMolecularNode(array, CaseFrame.act1);
			
			ap.perform();
			
			printNetwork();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
