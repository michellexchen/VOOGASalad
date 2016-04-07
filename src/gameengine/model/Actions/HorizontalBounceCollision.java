package gameengine.model.Actions;

import gameengine.model.Actor;
import gameengine.model.PhysicsEngine;
import gameengine.model.Actions.Action;

/**
 * This Action represents an elastic sideways collision between Actors
 * 
 * @author justinbergkamp
 *
 */

public class HorizontalBounceCollision extends Action{

	public HorizontalBounceCollision(Actor assignedActor) {
		super(assignedActor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void perform(PhysicsEngine myPhysicsEngine) {
		//myPhysicsEngine.horzBounceCollision(getActor());
		getActor().setXVelo(getActor().getXVelo()*-1);
	}

}