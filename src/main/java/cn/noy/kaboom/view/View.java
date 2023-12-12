package cn.noy.kaboom.view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class View implements PropertyChangeListener {
    private final int layer;
    private boolean removed = false;

    protected View(int layer) {
        this.layer = layer;
    }

    public abstract void render(Graphics graphics);
    public void onAttached(){}
    public void onRemoved(){}

    public int getLayer() {
        return layer;
    }

    public final void remove(){
        removed = true;
        onRemoved();
    }

    public boolean isRemoved() {
        return removed;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("remove"))
            remove();
    }
}
