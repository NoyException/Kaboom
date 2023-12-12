package cn.noy.kaboom.view.util;

import cn.noy.kaboom.view.Sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageUtil {
    public static BufferedImage getImageFromResource(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(Sprites.class.getClassLoader().getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage resize(Image image, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, width, height, null);
        return bufferedImage;
    }

    public static BufferedImage rotate(Image image, int degree) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.rotate(Math.toRadians(degree), (double) width / 2, (double)height / 2);
        graphics.drawImage(image, 0, 0, null);
        return bufferedImage;
    }
}
