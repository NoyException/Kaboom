package cn.noy.kaboom.view;

import cn.noy.kaboom.common.Action;
import cn.noy.kaboom.common.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

public class Sprite {
    private Image[] images;
    private Map<Action, Pair<Integer, Integer>> actionIndices;

    public Sprite(Image[] images, Map<Action, Pair<Integer, Integer>> actionIndices){
        this.images = images;
        this.actionIndices = actionIndices;
    }

    public static Sprite cut(BufferedImage image, int width, int height, Map<Action, Pair<Integer, Integer>> actionIndices){
        int rows = image.getHeight(null) / height;
        int cols = image.getWidth(null) / width;
        Image[] images = new Image[rows * cols];
        for (int i = 0; i < rows; i++) {
            int y = i * height;
            for (int j = 0; j < cols; j++) {
                int x = j * width;
                images[i * cols + j] = image.getSubimage(x, y, width, height);
            }
        }
        return new Sprite(images, actionIndices);
    }

    public Image[] getImage(Action action){
        Pair<Integer, Integer> pair = actionIndices.get(action);
        return Arrays.copyOfRange(images, pair.getFirst(), pair.getSecond());
    }

    public Image getImage(Action action, long frame){
        Image[] image = getImage(action);
        return image[(int) (frame%image.length)];
    }

    public void resize(int width, int height){
        for (int i = 0; i < images.length; i++) {
            images[i] = images[i].getScaledInstance(width, height, Image.SCALE_DEFAULT);
        }
    }

}
