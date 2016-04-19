package gameengine.model.Actions;

import gameengine.model.Actor;
import gameengine.model.PhysicsEngine;


import java.util.List;

/**
 * @author blakekaplan
 */
public class MoveUp extends MovingAction {

    private static final double UP_ANGLE = 90.0;

    public MoveUp(Actor assignedActor) {
        super(assignedActor);
    }

    @Override
    public void perform() {
    	getMyActor().getPhysicsEngine().jump(getMyActor());
    	getMyActor().setHeading(90);
    }

    
}
