package cn.noy.kaboom.model.entity;

import cn.noy.kaboom.common.*;
import cn.noy.kaboom.model.Game;
import cn.noy.kaboom.model.event.EntityPropertyChangedEvent;
import cn.noy.kaboom.model.event.EntityRemoveEvent;
import cn.noy.kaboom.model.util.Location;
import cn.noy.kaboom.model.world.World;

import java.util.LinkedList;
import java.util.List;

public abstract class Entity {
    private Location location;
    private int attack = 2;
    private double speed = 0.1;
    private final List<Bomb> bombs = new LinkedList<>();
    private int maxBombs = 1;
    private Action action = Action.IDLE;
    private boolean removed = false;
    private long frozenTicks = 0;
    private boolean invisible = false;
    private boolean resetToIdle;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public Action getAction() {
        return action;
    }

    protected void setAction(Action action) {
        this.action = action;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getMaxBombs() {
        return maxBombs;
    }

    public void setMaxBombs(int maxBombs) {
        this.maxBombs = maxBombs;
    }

    public int getAvailableBombs() {
        bombs.removeIf(Bomb::isExploded);
        return maxBombs - bombs.size();
    }

    public void placeBomb() {
        bombs.removeIf(Bomb::isExploded);
        if (bombs.size() >= maxBombs) return;
        Bomb bomb = new Bomb(50, attack);
        boolean b = getWorld().spawnEntity(bomb, getLocation().getBlockX() + 0.5, getLocation().getBlockY() + 0.5);
        if (b) bombs.add(bomb);
    }

    public double canMoveDistance(Direction direction) {
        BoundingBox aabb = getBoundingBox();
        Location loc = getLocation();
        double dist = 2;
        List<BoundingBox> boxes = new LinkedList<>();
        for (int i = loc.getBlockX() - 1; i <= loc.getBlockX() + 1; i++) {
            for (int j = loc.getBlockY() - 1; j <= loc.getBlockY() + 1; j++) {
                if (getWorld().getTile(i, j, false).hasFlag(TileFlag.PASSABLE))
                    continue;
                boxes.add(new BoundingBox(i, j, 1, 1));
            }
        }

        for (Bomb bomb : getWorld().getBombs()) {
            boxes.add(bomb.getBoundingBox());
        }

        for (BoundingBox box : boxes) {
            BoundingBox.BoundingBoxRayTraceResult result =
                    box.rayTrace(loc.getPosition(), direction.toVec2d(), dist, aabb.getWidth(), aabb.getHeight());
            if (result != null) {
                double d = result.hitPoint().distance(loc.getPosition());
                dist = Math.min(dist, d);
            }
        }
        return dist;
    }

    public boolean canMove(Direction direction) {
        return canMoveDistance(direction) > 0;
    }

    public boolean canMove(Direction direction, double distance) {
        return canMoveDistance(direction) >= distance;
    }

    public double move(Direction direction) {
        resetToIdle = false;
        action = switch (direction){
            case UP -> Action.WALK_UP;
            case DOWN -> Action.WALK_DOWN;
            case LEFT -> Action.WALK_LEFT;
            case RIGHT -> Action.WALK_RIGHT;
        };
        double dist = Math.min(canMoveDistance(direction), getSpeed());
        this.location = getLocation().move(direction, dist);
        return dist;
    }

    public long getFrozenTicks() {
        return frozenTicks;
    }

    public void setFrozenTicks(long frozenTicks) {
        this.frozenTicks = frozenTicks;
    }

    public void setInvisible(boolean invisible) {
        EntityPropertyChangedEvent event = new EntityPropertyChangedEvent(this, "invisible", this.invisible, invisible);
        event.call();
        if(!event.isCanceled())
            this.invisible = (boolean) event.getNewValue();
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void preventResetToIdle(){
        resetToIdle = false;
    }

    public final void tick() {
        if(resetToIdle) action = Action.IDLE;
        resetToIdle = true;

        if (frozenTicks > 0) {
            frozenTicks--;
            return;
        }
        onTick();
    }

    public void onTick() {
    }

    public abstract EntityType getType();

    public abstract BoundingBox getBoundingBox();

    public boolean isRemoved() {
        return removed;
    }

    public void remove() {
        if (removed) return;
        EntityRemoveEvent event = new EntityRemoveEvent(this);
        event.call();

        removed = true;
        getWorld().remove(this);
    }

}
