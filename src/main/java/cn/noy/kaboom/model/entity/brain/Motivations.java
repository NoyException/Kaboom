package cn.noy.kaboom.model.entity.brain;

import cn.noy.kaboom.common.Direction;
import cn.noy.kaboom.common.Pair;
import cn.noy.kaboom.common.TileFlag;
import cn.noy.kaboom.model.entity.Entity;
import cn.noy.kaboom.model.entity.LivingEntity;
import cn.noy.kaboom.model.entity.Mob;
import cn.noy.kaboom.model.entity.TestBomb;
import cn.noy.kaboom.model.util.Location;

import java.util.List;

public class Motivations {
    public static Motivation FIND_TARGET = mob -> {
        Location location = mob.getLocation();
        LivingEntity target = mob.getTarget();
        double dist = target == null ? Double.POSITIVE_INFINITY : location.manhattanDistance(target.getLocation())-4;

        for (Entity entity : mob.getWorld().getEntities()) {
            if (entity == mob) continue;
            if (entity instanceof LivingEntity livingEntity) {
                double d = location.manhattanDistance(entity.getLocation());
                if (d < dist) {
                    dist = d;
                    target = livingEntity;
                }
            }
        }
        if (target != null) {
            LivingEntity finalTarget = target;
            return () -> mob.setTarget(finalTarget);
        }
        return null;
    };

    public static Motivation MOVE_TO_TARGET = mob -> {
        if (!mob.hasTarget()) return null;
        if(mob.getTargetLocation()!=null) return null;
        Location location = mob.getLocation();
        Direction direction = location.directionToTarget(mob.getTarget().getLocation(), tileType -> tileType.hasFlag(TileFlag.BREAKABLE) ? 3 : 32767);
        if (direction != null) {
            Location moved = location.toBlockLocation().add(0.5, 0.5).move(direction, 1);
            if (!moved.isDangerous())
                return () -> mob.setTargetLocation(moved);
        }
        return null;
    };

    private record Node(Location location, Direction direction, Node parent) {
    }

    public static Motivation MOVE_TO_SAFE_PLACE = mob -> {
        Location location = mob.getLocation().toBlockLocation().add(0.5, 0.5);
        if (!location.isDangerous()) return null;

        Pair<Direction, Integer> pair = location.bfsToSafePlace(mob.getSpeed());
        if (pair != null && pair.getFirst() != null)
            return () -> mob.setTargetLocation(location.move(pair.getFirst(), 1));
        return null;
    };

    public static Motivation MOVE = mob -> {
        Location targetLocation = mob.getTargetLocation();
        if (targetLocation == null) return null;

        Location location = mob.getLocation();
        double speed = mob.getSpeed();

        if (location.distanceSquared(targetLocation) > speed * speed) {
            Direction direction = getDirection(mob, location, targetLocation);
            if (mob.canMove(direction)) {
                return () -> mob.move(direction);
            }
        } else mob.setTargetLocation(null);
        return null;
    };

    private static Direction getDirection(LivingEntity livingEntity, Location location, Location targetLocation) {
        Direction direction = null;
        if (Math.abs(location.getX() - targetLocation.getX()) > Math.abs(location.getY() - targetLocation.getY())) {
            if (location.getX() > targetLocation.getX()) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
        }
        if (direction == null || (targetLocation.isPassable() && !livingEntity.canMove(direction))){
            if (location.getY() > targetLocation.getY()) {
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        }
        return direction;
    }

    public static Motivation PLACE_BOMB_TO_CLEAR_ROAD = mob -> {
        Location targetLocation = mob.getTargetLocation();
        if (targetLocation == null) return null;

        if (mob.getAvailableBombs() <= 0) return null;

        Location location = mob.getLocation().toBlockLocation().add(0.5, 0.5);
        double speed = mob.getSpeed();

        if (location.distanceSquared(targetLocation) >= speed * speed) {
            Direction direction = getDirection(mob, location, targetLocation);
            if (!mob.canMove(direction)) {

                TestBomb testBomb = new TestBomb(50, mob.getAttack());
                testBomb.setLocation(location);
                List<Location> locations = testBomb.getExplosionLocations();

                Pair<Direction, Integer> pair = location.bfsToSafePlace(loc -> {
                    if(locations.stream().anyMatch(l -> loc.getBlockX() == l.getBlockX() && loc.getBlockY() == l.getBlockY()))
                        return Math.min(loc.getTicksBeforeExplosion(), testBomb.getTicksLeft());
                    return loc.getTicksBeforeExplosion();
                }, mob.getSpeed());

                testBomb.explode();
                if (pair == null || pair.getSecond() > mob.getSpeed() * 49)
                    return null;

                return () -> {
                    mob.placeBomb();
                    mob.setTargetLocation(null);
                };
            }
        }
        return null;
    };

    public static Motivation PLACE_BOMB_TO_ATTACK_TARGET = mob -> {
        if(!mob.hasTarget()) return null;
        LivingEntity target = mob.getTarget();
        if(target.getLocation().toBlockLocation().manhattanDistance(mob.getLocation().toBlockLocation())<=1.1){
            return mob::placeBomb;
        }
        return null;
    };
}
