package cn.noy.kaboom.view;

import cn.noy.kaboom.common.Action;
import cn.noy.kaboom.common.Pair;
import cn.noy.kaboom.view.util.ImageUtil;

import java.util.Map;

public class Sprites {
    public static final Sprite BLACK_ANGEL = Sprite.cut(ImageUtil.getImageFromResource("chr/BlackAngel.png"), 32, 32, Map.of(
            Action.IDLE, new Pair<>(0, 1),
            Action.WALK_DOWN, new Pair<>(0, 4),
            Action.WALK_RIGHT, new Pair<>(4, 8),
            Action.WALK_UP, new Pair<>(8, 12),
            Action.WALK_LEFT, new Pair<>(12, 16)
    ));

    public static final Sprite RUSSELL = Sprite.cut(ImageUtil.getImageFromResource("chr/Russell.png"), 32, 32, Map.of(
            Action.IDLE, new Pair<>(0, 1),
            Action.WALK_DOWN, new Pair<>(0, 4),
            Action.WALK_RIGHT, new Pair<>(4, 8),
            Action.WALK_UP, new Pair<>(8, 12),
            Action.WALK_LEFT, new Pair<>(12, 16)
    ));

    public static final Sprite BOMB = Sprite.cut(ImageUtil.getImageFromResource("bomb/bomb.png"), 16, 16, Map.of(
            Action.IDLE, new Pair<>(0, 3)
    ));

    public static final Sprite Explosion = Sprite.cut(ImageUtil.getImageFromResource("bomb/explosion.png"), 48, 48, Map.of(
            Action.IDLE, new Pair<>(0, 7),
            Action.ATTACK, new Pair<>(7, 14),
            Action.DEAD, new Pair<>(14, 21)
    ));

    public static final Sprite PROPS = Sprite.cut(ImageUtil.getImageFromResource("prop/props.png"), 32, 32, Map.of(
            Action.IDLE, new Pair<>(0, 1),
            Action.WALK_UP, new Pair<>(1, 2),
            Action.WALK_RIGHT, new Pair<>(2, 3)
    ));

    public static final Sprite WALL = Sprite.cut(ImageUtil.getImageFromResource("tile/wall.png"), 32, 32, Map.of(
            Action.IDLE, new Pair<>(0, 4)
    ));

    public static final Sprite GROUND = Sprite.cut(ImageUtil.getImageFromResource("tile/ground.png"), 32, 32, Map.of(
            Action.IDLE, new Pair<>(0, 4)
    ));

    static {
        BOMB.resize(32, 32);
        Explosion.resize(32, 32);
        PROPS.resize(16,16);
    }

}
