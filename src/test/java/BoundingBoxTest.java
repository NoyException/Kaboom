import cn.noy.kaboom.common.BoundingBox;
import cn.noy.kaboom.common.Position;
import cn.noy.kaboom.common.Vec2d;
import org.junit.jupiter.api.Test;

public class BoundingBoxTest {
    @Test
    public void testRayTrace(){
        BoundingBox aabb = new BoundingBox(1, 1, 1, 1);
        BoundingBox.BoundingBoxRayTraceResult result = aabb.rayTrace(new Position(0, 0), new Vec2d(1, 0), 10, 2, 4);
        System.out.println(result);
    }
}
