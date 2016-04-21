package gameengine.model.Triggers;

import gameengine.model.Actor;
import gameengine.model.IActor;
import gameengine.model.IPlayActor;
import gameengine.model.ITrigger;

public class SideCollision extends CollisionTrigger {

    private static final String COLLISION_NAME = "SideCollision";

    public SideCollision(IPlayActor actor1, IPlayActor actor2) {
        super(actor1, actor2);
    }

    @Override
    public boolean evaluate(ITrigger otherTrigger) {
        SideCollision otherCollision = (SideCollision) otherTrigger;
        return this.equals(otherCollision);

    }

    @Override
    public String getMyKey() {
        return COLLISION_NAME;
    }
}