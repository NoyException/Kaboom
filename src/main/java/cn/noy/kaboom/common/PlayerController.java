package cn.noy.kaboom.common;

import java.util.LinkedList;

public class PlayerController {
    private LinkedList<Direction> directions = new LinkedList<>();
    private boolean toPlaceBomb;

    public LinkedList<Direction> getDirections() {
        return directions;
    }

    public void setDirections(LinkedList<Direction> directions) {
        this.directions = directions;
    }

    public void setToPlaceBomb(boolean toPlaceBomb) {
        this.toPlaceBomb = toPlaceBomb;
    }

    public boolean isToPlaceBomb() {
        return toPlaceBomb;
    }
}
