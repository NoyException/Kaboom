package cn.noy.kaboom.model.event;

import cn.noy.kaboom.model.entity.Entity;

public class EntityPropertyChangedEvent extends EntityEvent implements Cancelable{
    private boolean canceled;
    private final String propertyName;
    private final Object oldValue;
    private Object newValue;

    public EntityPropertyChangedEvent(Entity entity, String propertyName, Object oldValue, Object newValue) {
        super(entity);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
