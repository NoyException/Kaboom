package cn.noy.kaboom.model.world;

import cn.noy.kaboom.common.Position;
import cn.noy.kaboom.common.TileType;
import cn.noy.kaboom.model.Game;
import cn.noy.kaboom.model.core.Scheduler;
import cn.noy.kaboom.model.entity.*;
import cn.noy.kaboom.model.event.EntitySpawnEvent;
import cn.noy.kaboom.model.event.EventManager;
import cn.noy.kaboom.model.util.Location;
import cn.noy.kaboom.view.Sprites;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class World {
    private TileType[][][] tiles;
    private final List<Entity> entities = new LinkedList<>();
    private final List<Bomb> bombs = new LinkedList<>();
    private Player player;

    public void init(){
        int width = 16;
        int height = 16;
        tiles = new TileType[width][height][2];

        InputStream stream = World.class.getClassLoader().getResourceAsStream("map/map0.txt");
        InputStreamReader reader = new InputStreamReader(stream);

        try {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    TileType type;
                    do{
                        type = switch ((char)reader.read()){
                            case ' ' -> TileType.AIR;
                            case '#' -> TileType.WALL;
                            case 'X' -> TileType.BOUNDARY;
                            case 'O' -> TileType.ROADBLOCK;
                            default -> null;
                        };
                    }while(type == null);
                    tiles[x][y][0] = type;
                    tiles[x][y][1] = TileType.FLOOR;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        tiles[1][1][0] = tiles[2][1][0] = tiles[1][2][0] = TileType.AIR;
//        tiles[width-2][height-2][0] = tiles[width-3][height-2][0] = tiles[width-2][height-3][0] = TileType.AIR;

        player = new Player();
        spawnEntity(player, 1.5, 1.5);

        Position[] positions = {
                new Position(width-2+0.5, height-2+0.5),
                new Position(1.5, height-2+0.5),
                new Position(width-2+0.5, 1.5),
        };

        for (Position position : positions) {
            Mob mob = new Mob();
            mob.setFrozenTicks(50);
            spawnEntity(mob, position);
        }

        Game.getInstance().getScheduler().schedule(task -> tick(), 0, 1);
    }

    public void tick() {
        new LinkedList<>(entities).forEach(Entity::tick);
    }

    public TileType getTile(int x, int y, boolean isUnderground) {
        if(x<0 || y<0 || x>=tiles.length || y>=tiles[0].length) return TileType.VOID;
        return tiles[x][y][isUnderground ? 1 : 0];
    }

    public void setTile(int x, int y, boolean isUnderground, TileType tile) {
        if(x<0 || y<0 || x>=tiles.length || y>=tiles[0].length) return;
        tiles[x][y][isUnderground ? 1 : 0] = tile;
    }

    public TileType[][][] getTiles() {
        return tiles;
    }

    public Collection<Entity> getEntities() {
        return entities;
    }

    public Collection<Entity> getEntities(int x, int y) {
        return entities.stream()
                .filter(entity -> entity.getLocation().getBlockX() == x && entity.getLocation().getBlockY() == y)
                .toList();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public boolean spawnEntity(Entity entity) {
        return spawnEntity(entity, 0, 0);
    }

    public boolean spawnEntity(Entity entity, Position position){
        return spawnEntity(entity, position.x(), position.y());
    }

    public boolean spawnEntity(Entity entity, double x, double y){
        Location location = Location.of(this, x, y);
        EntitySpawnEvent event = new EntitySpawnEvent(entity, location);
        event.call();
        if(event.isCanceled()) return false;

        entity.setLocation(event.getSpawnLocation());
        entities.add(entity);
        if (entity instanceof Bomb)
            bombs.add((Bomb) entity);
        return true;
    }

    public boolean isDangerous(int x, int y) {
        return new ArrayList<>(bombs).stream()
                .anyMatch(bomb -> bomb.getExplosionLocations().stream()
                        .anyMatch(location -> location.getBlockX() == x && location.getBlockY() == y));
    }

    public long getTicksBeforeExplosion(int x, int y){
        long ticks = Long.MAX_VALUE;
        for (Bomb bomb : new ArrayList<>(bombs)) {
            for (Location location : bomb.getExplosionLocations()) {
                if(location.getBlockX()!=x || location.getBlockY()!=y)
                    continue;
                ticks = Math.min(ticks, bomb.getTicksLeft());
            }
        }
        ticks = Math.max(ticks, 0);
        return ticks;
    }

    public void remove(Entity entity) {
        if(entity.isRemoved()){
            entities.remove(entity);
            if (entity instanceof Bomb)
                bombs.remove(entity);
        }
        else entity.remove();
    }

    public void dropProp(Position position, Prop.Effect effect){
        Prop prop = new Prop(effect);
        spawnEntity(prop, position.x(), position.y());
    }

    public void destroyTile(int blockX, int blockY) {
        TileType tile = getTile(blockX, blockY, false);
        switch (tile){
            case ROADBLOCK -> {
                double rand = Math.random();
                Position position = new Position(blockX+0.5, blockY+0.5);
                if(rand<0.08) dropProp(position, Prop.Effect.MORE_BOMB);
                else if(rand<0.12) dropProp(position, Prop.Effect.INCREASE_ATTACK);
                else if(rand<0.2) dropProp(position, Prop.Effect.INCREASE_SPEED);
            }
        }
        setTile(blockX, blockY, false, TileType.AIR);
    }
}
