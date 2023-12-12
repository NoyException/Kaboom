package cn.noy.kaboom.model.entity.brain;

import cn.noy.kaboom.model.entity.Mob;

import java.util.function.Consumer;

public interface Motivation {
    Runnable getAction(Mob mob);
}
