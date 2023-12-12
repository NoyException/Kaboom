package cn.noy.kaboom.view.animation;

import cn.noy.kaboom.common.Action;
import cn.noy.kaboom.common.ExplosionArea;
import cn.noy.kaboom.common.Pair;
import cn.noy.kaboom.common.Position;
import cn.noy.kaboom.view.Sprite;
import cn.noy.kaboom.view.Sprites;
import cn.noy.kaboom.view.util.ImageUtil;

import java.awt.*;

public class ExplosionAnimation extends Animation{
    private final ExplosionArea explosionArea;
    private final Sprite sprite = Sprites.Explosion;

    public ExplosionAnimation(ExplosionArea explosionArea) {
        super(3, 28);
        this.explosionArea = explosionArea;
    }

    @Override
    public void render(Graphics graphics, long time) {
        if (explosionArea.center()==null) return;

        Pair<Integer, Integer> position = explosionArea.center().add(-0.5,-0.5).toViewPosition();
        graphics.drawImage(sprite.getImage(Action.IDLE, (int)(time/4)), position.getKey(), position.getValue(), null);

        if(explosionArea.upEdge()!=null && explosionArea.upEdge().y()<explosionArea.center().y()){
            Pair<Integer, Integer> viewPosition = explosionArea.upEdge().add(-0.5,-0.5).toViewPosition();
            graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.DEAD, (int)(time/4)), 270), viewPosition.getKey(), viewPosition.getValue(), null);

            for(Position pos = explosionArea.center().add(0, -1); pos.y()-0.1>explosionArea.upEdge().y(); pos = pos.add(0, -1)){
                viewPosition = pos.add(-0.5,-0.5).toViewPosition();
                graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.ATTACK, (int)(time/4)),270), viewPosition.getKey(), viewPosition.getValue(), null);
            }
        }
        if(explosionArea.downEdge()!=null && explosionArea.downEdge().y()>explosionArea.center().y()){
            Pair<Integer, Integer> viewPosition = explosionArea.downEdge().add(-0.5,-0.5).toViewPosition();
            graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.DEAD, (int)(time/4)), 90), viewPosition.getKey(), viewPosition.getValue(), null);

            for(Position pos = explosionArea.center().add(0, 1); pos.y()+0.1<explosionArea.downEdge().y(); pos = pos.add(0, 1)){
                viewPosition = pos.add(-0.5,-0.5).toViewPosition();
                graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.ATTACK, (int)(time/4)), 90), viewPosition.getKey(), viewPosition.getValue(), null);
            }
        }
        if(explosionArea.leftEdge()!=null && explosionArea.leftEdge().x()<explosionArea.center().x()){
            Pair<Integer, Integer> viewPosition = explosionArea.leftEdge().add(-0.5,-0.5).toViewPosition();
            graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.DEAD, (int)(time/4)), 180), viewPosition.getKey(), viewPosition.getValue(), null);

            for(Position pos = explosionArea.center().add(-1, 0); pos.x()-0.1>explosionArea.leftEdge().x(); pos = pos.add(-1, 0)){
                viewPosition = pos.add(-0.5,-0.5).toViewPosition();
                graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.ATTACK, (int)(time/4)), 180), viewPosition.getKey(), viewPosition.getValue(), null);
            }
        }
        if(explosionArea.rightEdge()!=null && explosionArea.rightEdge().x()>explosionArea.center().x()){
            Pair<Integer, Integer> viewPosition = explosionArea.rightEdge().add(-0.5,-0.5).toViewPosition();
            graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.DEAD, (int)(time/4)), 0), viewPosition.getKey(), viewPosition.getValue(), null);

            for(Position pos = explosionArea.center().add(1, 0); pos.x()+0.1<explosionArea.rightEdge().x(); pos = pos.add(1, 0)){
                viewPosition = pos.add(-0.5,-0.5).toViewPosition();
                graphics.drawImage(ImageUtil.rotate(sprite.getImage(Action.ATTACK, (int)(time/4)), 0), viewPosition.getKey(), viewPosition.getValue(), null);
            }
        }
    }
}
