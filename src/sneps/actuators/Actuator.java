/**
 * @className Actuator.java
 * 
 * @ClassDescription This interface is used to specify how an actuator
 * should interact with SNePS.
 * 
 * @author Ahmed Osama Abd El-Meguid Ibrahim
 * @version 2.00 30th of March, 2016
 */

package sneps.actuators;

import sneps.network.nodes.ActNode;

public interface Actuator {
	
	void actOnNode(ActNode n);

}
