package sneps.network; 
import java.util.Hashtable;

import sneps.network.classes.semantic.Act;
import sneps.network.classes.semantic.Action;
import sneps.network.classes.semantic.ControlAction;
import sneps.network.classes.semantic.Entity;
import sneps.network.classes.semantic.Infimum; 
import sneps.network.classes.semantic.Individual;
import sneps.network.classes.semantic.Proposition; 
import sneps.network.classes.syntactic.Term;
import sneps.network.classes.syntactic.Base;
import sneps.network.classes.syntactic.Closed;
import sneps.network.classes.syntactic.Pattern;
import sneps.network.classes.syntactic.Variable;
import sneps.network.nodes.ActNode;
import sneps.network.nodes.ActionNode;
import sneps.network.nodes.ClosedNode;
import sneps.network.nodes.ControlActionNode;
import sneps.network.nodes.MolecularNode;
import sneps.network.nodes.Node;
import sneps.network.nodes.NodeSet;
import sneps.network.nodes.PatternNode;
import sneps.network.nodes.PropositionNode;
import sneps.network.nodes.VariableNode;

public class Wire {
  
	private Relation wireRelation;  
    private Node wireNode;
    
    
    /**
     * first constructor that assigns the wire node and builds a node instance according to the passed parameters
     * 
     * @param wireRelation
     * @param nodeId
     * @param syntacticType
     * @param semanticType 
     * 
     * @throws CustomException
     */
	
    public Wire(Relation wireRelation, String nodeId, String syntacticType, String semanticType) throws CustomException{

		this.wireRelation = wireRelation;  
	   
			if(Network.getNodes().containsKey(nodeId)) 
				throw new CustomException("This node's identifier already exists"); 
		
			else {
				
			  if(syntacticType == "Base")
			  {   
				  if(semanticType=="Individual") {
				     Individual e = new Individual();
				     this.wireNode = Network.buildBaseNode(nodeId, e);  
				  } 
				  
				  if(semanticType=="Entity") {
					  Entity e = new Entity();
					  this.wireNode = Network.buildBaseNode(nodeId, e);  
				  } 
				  
				  if(semanticType=="Act") {
					  Act e = new Act();
					  this.wireNode = Network.buildBaseNode(nodeId, e);  
				  } 
				  
				  if(semanticType=="Proposition") {
					  Proposition e = new Proposition();
					  this.wireNode = Network.buildBaseNode(nodeId, e);  
				  }
			  } 
			  
	
			    }
		} 
	
    /**
     * a second constructor used to build a wire with a relation pointing to variable node 
     * 
     * @param wireRelation 
     * 
     * @throws CustomException
    */
    
	public Wire(Relation wireRelation) throws CustomException{ 
		
		this.wireRelation = wireRelation;
		this.wireNode = Network.buildVariableNode(); 
		
	}
	
	
	/**
     * a second constructor used to build a wire with a relation pointing to variable node with a given semantic type
     * 
     * @param wireRelation 
     * 
     * @throws CustomException
    */
    
	public Wire(Relation wireRelation, Entity semantic) throws CustomException{ 
		
		this.wireRelation = wireRelation;
		this.wireNode = Network.buildVariableNode(semantic); 
		
	}
	
	/**
	 * a third constructor that accepts a node object as a parameter in case of creating two wires with different relations
	 * and one common node 
	 * 
	 * @param wireRelation 
	 * 
	 * @param node
	 */
	
	public Wire(Relation wireRelation, Node node){ 
		
		 this.wireRelation = wireRelation; 
		 this.wireNode = node;
	}




	public Node getWireNode() {
		return wireNode;
	}



	public Relation getWireRelation() {
		return wireRelation;
	}
  
	
	

	
   
	
	
}
