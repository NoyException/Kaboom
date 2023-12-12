package cn.noy.kaboom.common;

public record Position(double x, double y) {

    public Position add(double x, double y){
        return new Position(this.x+x, this.y+y);
    }

    public Position subtract(double x, double y) {
        return new Position(this.x-x, this.y-y);
    }

    public double distanceSquared(Position position){
        return (x-position.x)*(x-position.x) + (y-position.y)*(y-position.y);
    }

    public double distance(Position position){
        return Math.sqrt(distanceSquared(position));
    }

    public Pair<Integer, Integer> toViewPosition(){
        return new Pair<>((int)Math.floor(x*32), (int)Math.floor(y*32));
    }

    public int getBlockX(){
        return (int) Math.floor(x);
    }

    public int getBlockY(){
        return (int) Math.floor(y);
    }

    public Position toBlockPosition(){
        return new Position(getBlockX(), getBlockY());
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
