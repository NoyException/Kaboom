package cn.noy.kaboom.view;

import cn.noy.kaboom.common.Action;
import cn.noy.kaboom.common.TileType;
import cn.noy.kaboom.view.util.ImageUtil;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Random;

public class WorldView extends View {
    private static final Image ROADBLOCK = ImageUtil.resize(ImageUtil.getImageFromResource("tile/roadblock.png"),32,32);
    private TileType[][][] tiles;

    public WorldView() {
        super(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        switch (evt.getPropertyName()) {
            case "tiles" -> tiles = (TileType[][][]) evt.getNewValue();
        }
    }

    @Override
    public void render(Graphics graphics) {
        Random random = new Random(0);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                double rnd = random.nextDouble();
                Image img = switch (tiles[i][j][1]) {
                    case FLOOR -> Sprites.GROUND.getImage(Action.IDLE, (int)(rnd*4));
                    case ROADBLOCK -> ROADBLOCK;
                    case WALL,BOUNDARY -> Sprites.WALL.getImage(Action.IDLE, (int)(rnd*4));
                    default -> null;
                };
                if(img!=null) graphics.drawImage(img, i * 32, j * 32, null);

                rnd = random.nextDouble();
                img = switch (tiles[i][j][0]) {
                    case FLOOR -> Sprites.GROUND.getImage(Action.IDLE, (int)(rnd*4));
                    case ROADBLOCK -> ROADBLOCK;
                    case WALL,BOUNDARY -> Sprites.WALL.getImage(Action.IDLE, (int)(rnd*4));
                    default -> null;
                };
                if(img!=null) graphics.drawImage(img, i * 32, j * 32, null);
            }
        }
    }
}
