package cn.noy.kaboom.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum TileType {
    VOID,
    AIR(TileFlag.PASSABLE),
    FLOOR(TileFlag.PASSABLE),
    ROADBLOCK(TileFlag.BREAKABLE),
    WALL,
    BOUNDARY,
    BOMB(TileFlag.BREAKABLE),
    ;
    private final Set<TileFlag> flags;

    TileType(TileFlag... flags) {
        this.flags = new HashSet<>(List.of(flags));
    }

    public boolean hasFlag(TileFlag flag){
        return flags.contains(flag);
    }
}
