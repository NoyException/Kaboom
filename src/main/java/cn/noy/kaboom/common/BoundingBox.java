package cn.noy.kaboom.common;

public class BoundingBox {
    private static final double epsilon = 0.000001;
    private final Position position;
    private final double width;
    private final double height;

    public BoundingBox(double x, double y, double width, double height){
        this.position = new Position(x, y);
        this.width = width;
        this.height = height;
    }

    public BoundingBox(Position position, double width, double height){
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(BoundingBox boundingBox){
        return intersects(boundingBox.getX(), boundingBox.getY(), boundingBox.width, boundingBox.height);
    }

    public boolean intersects(double x, double y, double width, double height){
        return this.getX() < x + width && this.getX() + this.width > x && this.getY() < y + height && this.getY() + this.height > y;
    }

    public boolean contains(double x, double y){
        return x >= this.getX() && x <= this.getX() + this.width
                && y >= this.getY() && y <= this.getY() + this.height;
    }

    public boolean contains(Position position){
        return contains(position.x(), position.y());
    }

    public double getX() {
        return position.x();
    }

    public double getY() {
        return position.y();
    }

    public double getCenterX(){
        return getX() + width / 2;
    }

    public double getCenterY(){
        return getY() + height / 2;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public BoundingBox move(Direction direction, double distance) {
        return new BoundingBox(direction.move(position, distance), width, height);
    }

    public Position getCenter(){
        return new Position(getCenterX(), getCenterY());
    }

    public Position getTopLeft(){
        return position;
    }

    public Position getTopRight(){
        return position.add(width, 0);
    }

    public Position getBottomLeft(){
        return position.add(0, height);
    }

    public Position getBottomRight(){
        return position.add(width, height);
    }

    public BoundingBox expand(double x, double y){
        return new BoundingBox(position.subtract(x, y), width + x * 2, height + y * 2);
    }

    public BoundingBox expand(double left, double top, double right, double bottom){
        return new BoundingBox(position.subtract(left, top), width + left + right, height + top + bottom);
    }

    private boolean doubleEqual(double a, double b){
        return a-b<epsilon && b-a<epsilon;
    }

    public enum Face{
        LEFT,RIGHT,TOP,BOTTOM;
    }

    public record BoundingBoxRayTraceResult(Position hitPoint, Face face){
        @Override
        public String toString() {
            return "BoundingBoxRayTraceResult{" +
                    "hitPoint=" + hitPoint +
                    ", face=" + face +
                    '}';
        }
    }

    public BoundingBoxRayTraceResult rayTrace(Position startPoint, Vec2d direction, double maxDistance){
        BoundingBox tmp = this.expand(-epsilon,-epsilon,-epsilon,-epsilon);
        //在碰撞箱内，不检测
        if(tmp.contains(startPoint))
            return null;
        //在碰撞箱表面或者外面，如果不朝着里面走就不碰撞
        if((doubleEqual(startPoint.x(),getX()) && direction.x()<=0)
                || (doubleEqual(startPoint.x(),getX()+getWidth()) && direction.x()>=0)
                || (doubleEqual(startPoint.y(),getY()) && direction.y()<=0)
                || (doubleEqual(startPoint.y(),getY()+getHeight()) && direction.y()>=0))
            return null;

        Vec2d dir = direction;
        dir = dir.normalize();
        // 计算射线和边界框的四个交点
        Position leftIntersect = null, rightIntersect = null, topIntersect = null, bottomIntersect = null;
        // 如果射线不垂直于x轴
        if (Math.abs(dir.x()) > epsilon) {
            // 计算与左边界的交点
            double t1 = (getX() - startPoint.x()) / dir.x();
            if (t1 >= -epsilon && t1 <= maxDistance+epsilon) {
                double y1 = startPoint.y() + t1 * dir.y();
                if (y1 >= getY() && y1 <= getY()+getHeight()) {
                    leftIntersect = new Position(getX(), y1);
                }
            }
            // 计算与右边界的交点
            double t2 = (getX()+getWidth() - startPoint.x()) / dir.x();
            if (t2 >= -epsilon && t2 <= maxDistance+epsilon) {
                double y2 = startPoint.y() + t2 * dir.y();
                if (y2 >= getY() && y2 <= getY()+getHeight()) {
                    rightIntersect = new Position(getX()+getWidth(), y2);
                }
            }
        }
        // 如果射线不垂直于y轴
        if (Math.abs(dir.y()) > epsilon) {
            // 计算与上边界的交点
            double t3 = (getY()+getHeight() - startPoint.y()) / dir.y();
            if (t3 >= -epsilon && t3 <= maxDistance+epsilon) {
                double x3 = startPoint.x() + t3 * dir.x();
                if (x3 >= getX() && x3 <= getX()+getWidth()) {
                    topIntersect = new Position(x3, getY()+getHeight());
                }
            }
            // 计算与下边界的交点
            double t4 = (getY() - startPoint.y()) / dir.y();
            if (t4 >= -epsilon && t4 <= maxDistance+epsilon) {
                double x4 = startPoint.x() + t4 * dir.x();
                if (x4 >= getX() && x4 <= getX()+getWidth()) {
                    bottomIntersect = new Position(x4, getY());
                }
            }
        }
        // 筛选有效的交点
        BoundingBoxRayTraceResult[] validIntersects = new BoundingBoxRayTraceResult[4];
        int validIntersectCount = 0;
        if (leftIntersect != null) validIntersects[validIntersectCount++] = new BoundingBoxRayTraceResult(leftIntersect,Face.LEFT);
        if (rightIntersect != null) validIntersects[validIntersectCount++] = new BoundingBoxRayTraceResult(rightIntersect,Face.RIGHT);
        if (topIntersect != null) validIntersects[validIntersectCount++] = new BoundingBoxRayTraceResult(topIntersect,Face.TOP);
        if (bottomIntersect != null) validIntersects[validIntersectCount++] = new BoundingBoxRayTraceResult(bottomIntersect,Face.BOTTOM);
        // 如果没有有效的交点，返回空值
        if (validIntersectCount == 0) return null;
        // 找出距离起点最近的交点
        BoundingBoxRayTraceResult closestIntersect = validIntersects[0];
        double minDistance = startPoint.distanceSquared(closestIntersect.hitPoint);
        for (int i = 1; i < validIntersectCount; i++) {
            double distance = startPoint.distanceSquared(validIntersects[i].hitPoint);
            if (distance < minDistance) {
                closestIntersect = validIntersects[i];
                minDistance = distance;
            }
        }
        // 返回最终的相交点
        return closestIntersect;
    }

    public BoundingBoxRayTraceResult rayTrace(Position startPoint, Vec2d direction, double maxDistance, double xSize, double ySize){
        return expand(xSize/2,ySize/2,xSize/2,ySize/2).rayTrace(startPoint, direction, maxDistance);
    }

}
