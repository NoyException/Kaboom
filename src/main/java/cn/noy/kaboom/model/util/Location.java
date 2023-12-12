package cn.noy.kaboom.model.util;

import cn.noy.kaboom.common.*;
import cn.noy.kaboom.model.entity.Entity;
import cn.noy.kaboom.model.world.World;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Function;

public class Location {
    private final WeakReference<World> world;
    private final Position position;

    public Location(WeakReference<World> world, Position position) {
        this.world = world;
        this.position = position;
    }

    public Location(WeakReference<World> world, double x, double y) {
        this.world = world;
        this.position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }

    public double getX() {
        return position.x();
    }

    public double getY() {
        return position.y();
    }

    public static Location of(World world, double x, double y) {
        return new Location(new WeakReference<>(world), x, y);
    }

    public static Location of(World world, Position position) {
        return new Location(new WeakReference<>(world), position);
    }

    public int getBlockX() {
        return (int) Math.floor(getX());
    }

    public int getBlockY() {
        return (int) Math.floor(getY());
    }

    public Location toBlockLocation() {
        return new Location(world, getBlockX(), getBlockY());
    }

    public Location add(double x, double y) {
        return new Location(this.world, this.getX() + x, this.getY() + y);
    }

    public Location add(Location location) {
        return add(location.getX(), location.getY());
    }

    public Location subtract(double x, double y) {
        return add(-x, -y);
    }

    public Location subtract(Location location) {
        return subtract(location.getX(), location.getY());
    }

    public Location move(Direction direction, double distance) {
        return new Location(world, direction.move(position, distance));
    }

    public World getWorld() {
        return world.get();
    }

    public TileType getTile(boolean isUnderground) {
        return getWorld().getTile(getBlockX(), getBlockY(), isUnderground);
    }

    public void setTile(boolean isUnderground, TileType tile) {
        getWorld().setTile(getBlockX(), getBlockY(), isUnderground, tile);
    }

    public void destroy(){
        getWorld().destroyTile(getBlockX(), getBlockY());
    }

    public boolean isPassable() {
        return getTile(false).hasFlag(TileFlag.PASSABLE) && getTile(true).hasFlag(TileFlag.PASSABLE);
    }

    public boolean isDangerous() {
        return getWorld().isDangerous(getBlockX(), getBlockY());
    }

    public long getTicksBeforeExplosion(){
        return getWorld().getTicksBeforeExplosion(getBlockX(), getBlockY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return getX() == location.getX() && getY() == location.getY() && Objects.equals(world.get(), location.world.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(world.get(), getX(), getY());
    }

    public Entity rayTrace(Direction direction) {
        Entity result = null;
        double dist = Double.POSITIVE_INFINITY;
        for (Entity entity : getWorld().getEntities()) {
            double d = switch (direction) {
                case UP ->
                        entity.getLocation().getBlockX() == getBlockX() ? getY() - entity.getLocation().getY() : Double.POSITIVE_INFINITY;
                case DOWN ->
                        entity.getLocation().getBlockX() == getBlockX() ? entity.getLocation().getY() - getY() : Double.POSITIVE_INFINITY;
                case LEFT ->
                        entity.getLocation().getBlockY() == getBlockY() ? getX() - entity.getLocation().getX() : Double.POSITIVE_INFINITY;
                case RIGHT ->
                        entity.getLocation().getBlockY() == getBlockY() ? entity.getLocation().getX() - getX() : Double.POSITIVE_INFINITY;
            };
            if (d > 0 && d < dist) {
                dist = d;
                result = entity;
            }
        }
        return result;
    }

    public double distance(Location location) {
        return Math.sqrt(distanceSquared(location));
    }

    public double distanceSquared(Location location) {
        return Math.pow(location.getX() - getX(), 2) + Math.pow(location.getY() - getY(), 2);
    }

    public double manhattanDistance(Location location) {
        return Math.abs(location.getX() - getX()) + Math.abs(location.getY() - getY());
    }

    public int manhattanBlockDistance(Location location) {
        return Math.abs(location.getBlockX() - getBlockX()) + Math.abs(location.getBlockY() - getBlockY());
    }

    public int steps(Location location, Function<TileType, Integer> breakTileCost) {
        return bStar(location, breakTileCost).getValue();
    }

    public Direction directionToTarget(Location location, Function<TileType, Integer> breakTileCost) {
        return bStar(location, breakTileCost).getKey();
    }

    public Pair<Direction, Integer> bStar(Location target, Function<TileType, Integer> breakTileCost) {
        target = target.toBlockLocation();

        // 如果本坐标和目标坐标相同，那么不需要移动，返回null和0
        if (this.toBlockLocation().equals(target)) {
            return new Pair<>(null, 0);
        }

        // 定义一个优先队列，用于存储待扩展的节点，按照f值从小到大排序
        PriorityQueue<bStarNode> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.f));

        // 定义一个哈希集合，用于存储已经访问过的节点，避免重复搜索
        HashSet<Location> visited = new HashSet<>();

        // 将本坐标作为初始节点加入队列和集合
        queue.offer(new bStarNode(this, null, 0, this.manhattanBlockDistance(target), null));
        visited.add(this);

        // 当队列不为空时，循环执行以下操作
        while (!queue.isEmpty()) {
            // 弹出队首节点，即f值最小的节点
            bStarNode current = queue.poll();

            // 如果当前节点的位置和目标位置相同，说明找到了最短路径
            if (current.loc.toBlockLocation().equals(target)) {
                // 回溯找到第一步的方向和总步数
                Direction firstDir = null;
                int steps = 0;
                while (current.parent != null) {
                    firstDir = current.dir;
                    steps++;
                    current = current.parent;
                }
                // 返回结果，注意要减去初始节点的步数
                return new Pair<>(firstDir, steps - 1);
            }

            // 否则，尝试四个方向进行扩展
            for (Direction dir : Direction.disorderedValues()) {
                // 计算沿着该方向移动一步后的新位置
                Location nextLoc = current.loc.move(dir, 1);

                if (!nextLoc.getTile(true).hasFlag(TileFlag.PASSABLE) || nextLoc.isDangerous())
                    continue;

                int cost = (nextLoc.isPassable() || nextLoc.equals(target)) ? 1 : breakTileCost.apply(nextLoc.getTile(false));
                // 如果新位置没有越界，并且能够经过，并且没有被访问过，那么将其作为新节点加入队列和集合
                if (cost >= 0 && !visited.contains(nextLoc)) {
                    queue.offer(new bStarNode(nextLoc, dir, current.g + cost, nextLoc.manhattanBlockDistance(target), current));
                    visited.add(nextLoc);
                }
            }
        }

        // 如果队列为空，说明没有找到可行的路径，返回null和-1
        return new Pair<>(null, -1);
    }

    // 定义一个Node类，表示搜索过程中的节点
    private static class bStarNode {
        // 节点的位置
        Location loc;
        // 节点的方向，即从父节点到该节点的移动方向
        Direction dir;
        // 节点的g值，即从初始节点到该节点的实际步数
        int g;
        // 节点的h值，即从该节点到目标节点的估计步数（使用曼哈顿距离）
        int h;
        // 节点的f值，即g值和h值之和
        int f;
        // 节点的父节点，用于回溯路径
        bStarNode parent;

        // 构造方法，根据给定的参数创建一个Node对象
        public bStarNode(Location loc, Direction dir, int g, int h, bStarNode parent) {
            this.loc = loc;
            this.dir = dir;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }
    }


    private record BfsNode(Location location, Direction direction, BfsNode parent) {
    }

    public Pair<Direction, Integer> bfsToSafePlace(double speed){
        return bfsToSafePlace(Location::getTicksBeforeExplosion, speed);
    }

    public Pair<Direction, Integer> bfsToSafePlace(Function<Location, Long> dangerDetector, double speed) {
        Location location = toBlockLocation().add(0.5, 0.5);
        if (dangerDetector.apply(location)==Long.MAX_VALUE) return new Pair<>(null, 0);

        LinkedList<BfsNode> nodes = new LinkedList<>();
        nodes.addFirst(new BfsNode(location, null, null));

        HashSet<Location> visited = new HashSet<>();

        while (!nodes.isEmpty()) {
            BfsNode node = nodes.removeFirst();
            if (visited.contains(node.location))
                continue;
            long ticksLeft = dangerDetector.apply(node.location);
            if (ticksLeft==Long.MAX_VALUE) {
                int steps = 0;
                Direction direction = node.direction;
                while (node.parent != null) {
                    steps++;
                    direction = node.direction;
                    node = node.parent;
                }
                return new Pair<>(direction, steps);
            }
            visited.add(node.location);
            if (node.location.manhattanDistance(location)+0.1 >= speed*ticksLeft)
                continue;
            for (Direction direction : Direction.values()) {
                Location nextLocation = node.location.move(direction, 1);
                if (!nextLocation.isPassable()) continue;
                if (visited.contains(nextLocation)) continue;
                nodes.addLast(new BfsNode(nextLocation, direction, node));
            }
        }

        return null;
    }

}
