package cn.noy.kaboom.common;

public record Vec2d(double x, double y) {

    public Vec2d add(double x, double y){
        return new Vec2d(this.x+x, this.y+y);
    }

    public double length(){
        return Math.sqrt(x*x + y*y);
    }

    public Vec2d normalize() {
        double length = length();
        return new Vec2d(x / length, y / length);
    }
}
