package cn.noy.kaboom.model.event;

import cn.noy.kaboom.model.entity.Entity;
import cn.noy.kaboom.model.util.Location;

public class EntitySpawnEvent extends EntityEvent implements Cancelable{
    private boolean canceled;
    private Location spawnLocation;
    public EntitySpawnEvent(Entity entity, Location spawnLocation) {
        super(entity);
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation(){
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation){
        this.spawnLocation = spawnLocation;
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
