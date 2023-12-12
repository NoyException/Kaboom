package cn.noy.kaboom.model.entity;

import cn.noy.kaboom.common.Action;
import cn.noy.kaboom.common.BoundingBox;
import cn.noy.kaboom.common.EntityType;
import cn.noy.kaboom.model.util.Location;

public class Prop extends Entity{
    private final Effect effect;

    public Prop(Effect effect) {
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

    @Override
    public void onTick() {
        super.onTick();
        preventResetToIdle();
        setAction(effect.getMappedAction());

        Location location = getLocation();
        for (Entity entity : getWorld().getEntities()) {
            if(entity instanceof LivingEntity livingEntity){
                if(entity.getLocation().distanceSquared(location)<0.25){
                    pickBy(livingEntity);
                    return;
                }
            }
        }
    }

    public void pickBy(LivingEntity livingEntity){
        switch (effect){
            case MORE_BOMB -> livingEntity.setMaxBombs(livingEntity.getMaxBombs()+1);
            case INCREASE_ATTACK -> livingEntity.setAttack(livingEntity.getAttack()+1);
            case INCREASE_SPEED -> livingEntity.setSpeed(livingEntity.getSpeed()+0.01);
        }
        remove();
    }

    @Override
    public EntityType getType() {
        return EntityType.PROP;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(getLocation().getPosition().add(-0.4,-0.4), 0.8, 0.8);
    }

    public enum Effect{
        MORE_BOMB(Action.IDLE),
        INCREASE_ATTACK(Action.WALK_UP),
        INCREASE_SPEED(Action.WALK_RIGHT),
        ;
        private final Action mappedAction;

        Effect(Action mappedAction) {
            this.mappedAction = mappedAction;
        }

        public Action getMappedAction() {
            return mappedAction;
        }
    }
}
