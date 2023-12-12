package cn.noy.kaboom.common;

public record ExplosionArea(
        Position center,
        Position upEdge,
        Position downEdge,
        Position leftEdge,
        Position rightEdge
) {
}
