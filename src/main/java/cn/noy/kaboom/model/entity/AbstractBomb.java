package cn.noy.kaboom.model.entity;

import cn.noy.kaboom.common.*;
import cn.noy.kaboom.model.util.Location;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractBomb extends Entity{
    protected int ticksLeft;
    protected int power;

    public AbstractBomb(int ticksLeft, int power) {
        this.ticksLeft = ticksLeft;
        this.power = power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    private int getTileState(Location location){
        TileType tile = location.getTile(false);
        if(tile.hasFlag(TileFlag.PASSABLE) || tile==TileType.BOMB) return 0;
        if(tile.hasFlag(TileFlag.BREAKABLE)) return 1;
        return -1;
    }

    public ExplosionArea getExplosionArea(){
        Location location = getLocation();
        int centerState = getTileState(location);
        if(centerState<0)
            return new ExplosionArea(null, null, null, null, null);
        if(centerState>0)
            return new ExplosionArea(location.getPosition(), null, null, null, null);
        Location left = location, right = location, up = location, down = location;
        LOOP_LEFT: for (int i = 1; i < power; i++) {
            switch (getTileState(location.add(-i, 0))){
                case -1 -> {
                    left = location.add(-i+1, 0);
                    break LOOP_LEFT;
                }
                case 1 -> {
                    left = location.add(-i, 0);
                    break LOOP_LEFT;
                }
                case 0 -> left = location.add(-i, 0);
            }
        }
        LOOP_RIGHT: for (int i = 1; i < power; i++) {
            switch (getTileState(location.add(i, 0))){
                case -1 -> {
                    right = location.add(i-1, 0);
                    break LOOP_RIGHT;
                }
                case 1 -> {
                    right = location.add(i, 0);
                    break LOOP_RIGHT;
                }
                case 0 -> right = location.add(i, 0);
            }
        }
        LOOP_UP: for (int i = 1; i < power; i++) {
            switch (getTileState(location.add(0, -i))){
                case -1 -> {
                    up = location.add(0, -i+1);
                    break LOOP_UP;
                }
                case 1 -> {
                    up = location.add(0, -i);
                    break LOOP_UP;
                }
                case 0 -> up = location.add(0, -i);
            }
        }
        LOOP_DOWN: for (int i = 1; i < power; i++) {
            switch (getTileState(location.add(0, i))){
                case -1 -> {
                    down = location.add(0, i-1);
                    break LOOP_DOWN;
                }
                case 1 -> {
                    down = location.add(0, i);
                    break LOOP_DOWN;
                }
                case 0 -> down = location.add(0, i);
            }
        }
        return new ExplosionArea(location.getPosition(), up.getPosition(), down.getPosition(), left.getPosition(), right.getPosition());
    }

    public abstract void explode();

    public List<Location> getExplosionLocations() {
        ExplosionArea explosionArea = getExplosionArea();
        List<Location> locations = new LinkedList<>();
        if(explosionArea.center()==null) return locations;
        locations.add(Location.of(getWorld(), explosionArea.center()));
        if(explosionArea.upEdge()!=null && explosionArea.upEdge().y()<explosionArea.center().y()){
            for(Position pos = explosionArea.center().add(0, -1); pos.y()+0.1>=explosionArea.upEdge().y(); pos = pos.add(0, -1)){
                locations.add(Location.of(getWorld(), pos));
            }
        }
        if(explosionArea.downEdge()!=null && explosionArea.downEdge().y()>explosionArea.center().y()){
            for(Position pos = explosionArea.center().add(0, 1); pos.y()-0.1<=explosionArea.downEdge().y(); pos = pos.add(0, 1)){
                locations.add(Location.of(getWorld(), pos));
            }
        }
        if(explosionArea.leftEdge()!=null && explosionArea.leftEdge().x()<explosionArea.center().x()){
            for(Position pos = explosionArea.center().add(-1, 0); pos.x()+0.1>=explosionArea.leftEdge().x(); pos = pos.add(-1, 0)){
                locations.add(Location.of(getWorld(), pos));
            }
        }
        if(explosionArea.rightEdge()!=null && explosionArea.rightEdge().x()>explosionArea.center().x()){
            for(Position pos = explosionArea.center().add(1, 0); pos.x()-0.1<=explosionArea.rightEdge().x(); pos = pos.add(1, 0)){
                locations.add(Location.of(getWorld(), pos));
            }
        }
        return locations;
    }

    public boolean isExploded(){
        return ticksLeft <=0;
    }

    @Override
    public void onTick() {
        if(ticksLeft >=0) ticksLeft--;
        if(ticksLeft >0){
            setAction(Action.IDLE);
        }
        if(ticksLeft ==0){
            explode();
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.BOMB;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(getLocation().getPosition().add(-0.4,-0.4), 0.8, 0.8);
    }

    public int getTicksLeft() {
        return ticksLeft;
    }
}
