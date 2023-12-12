package cn.noy.kaboom.model;

import cn.noy.kaboom.model.core.Scheduler;
import cn.noy.kaboom.model.entity.Player;
import cn.noy.kaboom.model.event.EventManager;
import cn.noy.kaboom.model.world.World;

public class Game {
    private static final Game instance = new Game();
    private final EventManager eventManager = new EventManager();
    private final Scheduler scheduler = new Scheduler();
    private World world;
    private boolean running = false;

    public static Game getInstance() {
        return instance;
    }

    public void start(){
        if(running) return;
        running = true;
        world = new World();
        world.init();
        scheduler.start();
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return world.getPlayer();
    }
}
