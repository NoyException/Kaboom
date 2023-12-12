package cn.noy.kaboom.view.animation;

import cn.noy.kaboom.view.View;

import java.awt.*;

public abstract class Animation extends View {
    private boolean playing = false;
    private final long duration;
    private long time;
    protected Animation(int layer, long duration) {
        super(layer);
        this.duration = duration;
    }

    public void play(){
        playing = true;
        time = 0;
    }

    public abstract void render(Graphics graphics, long time);

    @Override
    public void render(Graphics graphics) {
        if(playing){
            if(time>=duration){
                remove();
                return;
            }
            render(graphics, time);
            time++;
        }
    }
}
