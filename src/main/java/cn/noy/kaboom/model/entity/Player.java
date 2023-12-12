package cn.noy.kaboom.model.entity;

import cn.noy.kaboom.common.BoundingBox;
import cn.noy.kaboom.common.Direction;
import cn.noy.kaboom.common.EntityType;

import java.util.Collection;
import java.util.Set;

public class Player extends LivingEntity{
    private Collection<Direction> controlDirections = Set.of();
    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    public void setControlDirections(Collection<Direction> controlDirections) {
        this.controlDirections = controlDirections;
    }

    @Override
    public void onTick() {
        if(!controlDirections.isEmpty()){
            for (Direction direction : controlDirections) {
                if(move(direction)>0) break;
            }
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(getLocation().getPosition().add(-0.4,-0.4), 0.8, 0.8);
    }
}
