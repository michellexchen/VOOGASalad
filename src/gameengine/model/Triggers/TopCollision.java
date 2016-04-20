package gameengine.model.Triggers;

import gameengine.model.Actor;
import gameengine.model.IActor;
import gameengine.model.ITrigger;

public class TopCollision extends CollisionTrigger implements ITrigger {

    private static final String COLLISION_TYPE = "TopTrigger";

    public TopCollision(Actor mainActor, Actor collisionActor) {
        super(mainActor, collisionActor);
    }

    @Override
    public boolean evaluate(ITrigger otherTrigger) {
        TopCollision otherCollision = (TopCollision) otherTrigger;
        return this.equals(otherCollision);
    }

    @Override
    public String getMyKey() {
        return COLLISION_TYPE;
    }

}