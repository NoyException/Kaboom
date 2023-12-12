package cn.noy.kaboom.model.entity;

import cn.noy.kaboom.common.BoundingBox;
import cn.noy.kaboom.common.Direction;
import cn.noy.kaboom.common.EntityType;
import cn.noy.kaboom.common.PlayerController;

import java.util.Collection;
import java.util.Set;

public class Player extends LivingEntity{
    private PlayerController controller;
    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void onTick() {
        if(controller!=null){
            if(controller.isToPlaceBomb()){
                placeBomb();
                controller.setToPlaceBomb(false);
            }
            for (Direction direction : controller.getDirections()) {
                if(move(direction)>0) break;
            }
        }
    }

    public void setController(PlayerController controller) {
        this.controller = controller;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(getLocation().getPosition().add(-0.4,-0.4), 0.8, 0.8);
    }
}
