package cn.noy.kaboom.model.event;

import cn.noy.kaboom.model.entity.Entity;

public class EntityRemoveEvent extends EntityEvent{
    public EntityRemoveEvent(Entity entity) {
        super(entity);
    }
}
