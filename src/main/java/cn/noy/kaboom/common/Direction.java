package cn.noy.kaboom.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position move(Position position, double distance){
        return new Position(position.x() + x*distance, position.y() + y*distance);
    }

    public static Collection<Direction> disorderedValues(){
        ArrayList<Direction> directions = new ArrayList<>(Arrays.asList(values()));
        Collections.shuffle(directions);
        return directions;
    }

    public Vec2d toVec2d(){
        return new Vec2d(x, y);
    }

    public Direction opposite() {
        return switch (this){
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
