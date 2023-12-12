package cn.noy.kaboom.view;

import cn.noy.kaboom.common.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;

public class EntityView extends View{
    private Sprite sprite;
    private Action action;
    private Position position;
    private EntityType entityType;
    private boolean invisible;

    public EntityView() {
        super(1);
    }

    @Override
    public void render(Graphics graphics) {
        if(invisible || sprite == null || position == null || action == null) return;

        Pair<Integer, Integer> viewPosition = position.toViewPosition();
        Image image = sprite.getImage(action, System.currentTimeMillis() / 250);
        graphics.drawImage(image,
                viewPosition.getFirst()-image.getWidth(null)/2,
                viewPosition.getSecond()-image.getHeight(null)/2,
                null);
    }

    @Override
    public void onAttached() {
        super.onAttached();
        sprite = switch (entityType){
            case MOB -> Sprites.BLACK_ANGEL;
            case PLAYER -> Sprites.RUSSELL;
            case BOMB -> Sprites.BOMB;
            case PROP -> Sprites.PROPS;
        };
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        switch (evt.getPropertyName()){
            case "entityType" -> entityType = (EntityType) evt.getNewValue();
            case "position" -> position = (Position) evt.getNewValue();
            case "action" -> action = (Action) evt.getNewValue();
            case "invisible" -> invisible = (boolean) evt.getNewValue();
        }
    }

}
