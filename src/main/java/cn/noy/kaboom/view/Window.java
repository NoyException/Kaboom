package cn.noy.kaboom.view;

import cn.noy.kaboom.common.Direction;
import cn.noy.kaboom.common.EntityType;
import cn.noy.kaboom.model.Game;
import cn.noy.kaboom.model.entity.Player;
import cn.noy.kaboom.view.animation.Animation;
import com.google.common.collect.TreeMultimap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class Window {
    private final TreeMultimap<Integer, View> views = TreeMultimap.create(Comparator.comparingInt((Integer i) -> i), Comparator.comparingInt(Object::hashCode));
    private final JFrame frame;
    private final JPanel panel;
    private boolean isRunning = false;
    private final LinkedList<Integer> pressedKeys = new LinkedList<>();
    private final ConcurrentLinkedQueue<View> toAdd = new ConcurrentLinkedQueue<>();

    public Window() {
        this.frame = new JFrame();
        frame.setTitle("Kaboom!");
        frame.setSize(16*32+15, 16*32+36);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == ' '){
                    Game.getInstance().getPlayer().placeBomb();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.remove((Integer) e.getKeyCode());
                pressedKeys.addFirst(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove((Integer) e.getKeyCode());
            }
        });

        panel = new JPanel();
        panel.setDoubleBuffered(true);
        panel.setSize(16*32, 16*32);
        frame.add(panel);

        Game.getInstance().getScheduler().schedule(task -> {
            LinkedList<Direction> directions = new LinkedList<>();
            for (Integer key : pressedKeys) {
                switch (key){
                    case KeyEvent.VK_W -> directions.addLast(Direction.UP);
                    case KeyEvent.VK_S -> directions.addLast(Direction.DOWN);
                    case KeyEvent.VK_A -> directions.addLast(Direction.LEFT);
                    case KeyEvent.VK_D -> directions.addLast(Direction.RIGHT);
                }
            }
            Game.getInstance().getPlayer().setControlDirections(directions);
        },1,1);
    }

    public void display(){
        if(isRunning){
            return;
        }
        isRunning = true;
        frame.setVisible(true);
        new Thread(() -> {
            while (isRunning) {
                render();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close(){
        isRunning = false;
        frame.dispose();
    }

    public void addView(View view){
        toAdd.add(view);
    }

    public void playAnimation(Animation animation){
        addView(animation);
        animation.play();
    }

    public void render(){
        for (View view : toAdd) {
            views.put(view.getLayer(), view);
        }
        toAdd.clear();

        BufferedImage image = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        for(var it = views.values().iterator(); it.hasNext();){
            View view = it.next();
            if(view.isRemoved()){
                it.remove();
                continue;
            }
            view.render(graphics);
        }

        panel.getGraphics().drawImage(image, 0, 0, null);
    }

}
