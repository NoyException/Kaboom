package cn.noy.kaboom.model.entity;

import cn.noy.kaboom.common.*;
import cn.noy.kaboom.model.Game;
import cn.noy.kaboom.model.event.BombExplodeEvent;
import cn.noy.kaboom.model.util.Location;

import java.util.List;

public class Bomb extends AbstractBomb{
    private List<Location> explosionLocations;

    public Bomb(int timeLeft, int power) {
        super(timeLeft, power);
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        location.setTile(false, TileType.BOMB);
    }

    @Override
    public void explode(){
        BombExplodeEvent event = new BombExplodeEvent(this);
        event.call();
        if(event.isCanceled()) return;

        power = event.getPower();
        ticksLeft = -1;

        setInvisible(true);
    }

    @Override
    public List<Location> getExplosionLocations(){
        if(explosionLocations!=null) return explosionLocations;
        return super.getExplosionLocations();
    }

    private void causeDamage(){
        if(explosionLocations == null) explosionLocations = super.getExplosionLocations();

        for (Location location : explosionLocations) {
            getWorld().getEntities(location.getBlockX(), location.getBlockY()).forEach(entity -> {
                if(entity instanceof LivingEntity){
                    ((LivingEntity) entity).damage(1);
                }
                if(entity instanceof Bomb bomb && bomb!=this && bomb.ticksLeft >1){
                    bomb.ticksLeft = 1;
                }
            });
            TileType tile = location.getTile(false);
            if(tile.hasFlag(TileFlag.BREAKABLE))
                location.destroy();
        }
    }

    @Override
    public void onTick() {
        super.onTick();
        if(ticksLeft <-14){
            remove();
            return;
        }
        if(ticksLeft <0){
            causeDamage();
            ticksLeft--;
        }
    }

}
