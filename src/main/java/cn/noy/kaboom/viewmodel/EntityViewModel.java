package cn.noy.kaboom.viewmodel;

import cn.noy.kaboom.model.Game;
import cn.noy.kaboom.model.core.Scheduler;
import cn.noy.kaboom.model.entity.Entity;
import cn.noy.kaboom.model.event.EntityPropertyChangedEvent;
import cn.noy.kaboom.model.event.EntityRemoveEvent;
import cn.noy.kaboom.model.event.EventHandler;

public class EntityViewModel extends ViewModel{
    private final Entity entity;
    private Scheduler.Task task;

    public EntityViewModel(Entity entity){
        this.entity = entity;
    }

    @Override
    public void onAttached() {
        getPropertyChangeSupport().firePropertyChange("entityType", null, entity.getType());

        Game.getInstance().getEventManager().registerListeners(this);
        task = Game.getInstance().getScheduler().schedule(t -> {
            getPropertyChangeSupport().firePropertyChange("position", null, entity.getLocation().getPosition());
            getPropertyChangeSupport().firePropertyChange("action", null, entity.getAction());
        },1,1);
    }

    @EventHandler
    public void onRemove(EntityRemoveEvent event){
        if(event.getEntity() == entity){
            Game.getInstance().getEventManager().unregisterListeners(this);
            task.cancel();
            getPropertyChangeSupport().firePropertyChange("remove", null, true);
        }
    }

    @EventHandler(ignoreCanceled = true, priority = EventHandler.Priority.HIGHEST)
    public void onPropertyChanged(EntityPropertyChangedEvent event){
        if(event.getEntity() == entity){
            getPropertyChangeSupport().firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
        }
    }
}
