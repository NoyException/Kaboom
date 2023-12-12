package cn.noy.kaboom.app;

import cn.noy.kaboom.common.PlayerController;
import cn.noy.kaboom.model.Game;
import cn.noy.kaboom.model.event.BombExplodeEvent;
import cn.noy.kaboom.model.event.EntityRemoveEvent;
import cn.noy.kaboom.model.event.EntitySpawnEvent;
import cn.noy.kaboom.model.event.EventHandler;
import cn.noy.kaboom.view.EntityView;
import cn.noy.kaboom.view.Window;
import cn.noy.kaboom.view.WorldView;
import cn.noy.kaboom.view.animation.ExplosionAnimation;
import cn.noy.kaboom.viewmodel.EntityViewModel;
import cn.noy.kaboom.viewmodel.WorldViewModel;

public class KaboomLauncher {
    private Window window;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        KaboomLauncher launcher = new KaboomLauncher();
        launcher.launch();
    }

    public void launch(){
        window = new Window();
        Game.getInstance().getEventManager().registerListeners(this);
        Game.getInstance().start();

        WorldViewModel worldViewModel = new WorldViewModel(Game.getInstance().getWorld());
        WorldView worldView = new WorldView();
        Binder.bind(worldViewModel, worldView);

        window.addView(worldView);
        window.display();

        PlayerController controller = new PlayerController();
        window.setPlayerController(controller);
        Game.getInstance().getPlayer().setController(controller);
    }

    @EventHandler(priority = EventHandler.Priority.HIGHEST, ignoreCanceled = true)
    public void onEntitySpawn(EntitySpawnEvent event){
        EntityViewModel entityViewModel = new EntityViewModel(event.getEntity());
        EntityView entityView = new EntityView();
        Binder.bind(entityViewModel, entityView);
        window.addView(entityView);
    }

    @EventHandler(priority = EventHandler.Priority.HIGHEST, ignoreCanceled = true)
    public void onExplode(BombExplodeEvent event){
        window.playAnimation(new ExplosionAnimation(event.getEntity().getExplosionArea()));
    }
}