package cn.noy.kaboom.model.event;

import cn.noy.kaboom.model.entity.Entity;

public abstract class EntityEvent extends Event{
    private Entity entity;
    public EntityEvent(Entity entity){
        this.entity = entity;
    }
    public Entity getEntity(){
        return entity;
    }
}
