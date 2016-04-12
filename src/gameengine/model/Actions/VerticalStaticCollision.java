package gameengine.model.Actions;

import gameengine.model.Actor;
import gameengine.model.Actions.Action;

/**
 * This Action represents an static vertical collision between Actors
 * Actor will not bounce off, merely lose its velocity
 * 
 * @author justinbergkamp
 *
 */
public class VerticalStaticCollision extends Action{

	public VerticalStaticCollision(Actor assignedActor) {
		super(assignedActor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void perform() {
		//myPhysicsEngine.vertStaticCollision(getActor());
		getActor().setVeloY(0);
	}


}