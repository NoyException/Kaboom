package cn.noy.kaboom.model.event;

import cn.noy.kaboom.model.entity.Bomb;

public abstract class BombEvent extends EntityEvent{
    public BombEvent(Bomb bomb) {
        super(bomb);
    }

    @Override
    public Bomb getEntity() {
        return (Bomb) super.getEntity();
    }
}
