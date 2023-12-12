package cn.noy.kaboom.model.entity;

import cn.noy.kaboom.common.BoundingBox;
import cn.noy.kaboom.common.EntityType;
import cn.noy.kaboom.model.entity.brain.Brain;
import cn.noy.kaboom.model.entity.brain.Motivations;
import cn.noy.kaboom.model.util.Location;

public class Mob extends LivingEntity{
    private LivingEntity target;
    private Location targetLocation;
    private final Brain brain;

    public Mob(){
        brain = new Brain(this);
        brain.addMotivation(0, Motivations.MOVE);
        brain.addMotivation(1, Motivations.MOVE_TO_SAFE_PLACE);
        brain.addMotivation(2, Motivations.PLACE_BOMB_TO_ATTACK_TARGET);
        brain.addMotivation(2.5, Motivations.PLACE_BOMB_TO_CLEAR_ROAD);
        brain.addMotivation(3, Motivations.MOVE_TO_TARGET);
        brain.addMotivation(4, Motivations.FIND_TARGET);
    }

    public boolean hasTarget(){
        return target != null;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    @Override
    public void onTick() {
        if(target!=null && target.isRemoved())
            target = null;
        brain.getAction().run();
    }

    @Override
    public EntityType getType() {
        return EntityType.MOB;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(getLocation().getPosition().add(-0.4,-0.4), 0.8, 0.8);
    }
}
