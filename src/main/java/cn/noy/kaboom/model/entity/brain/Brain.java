package cn.noy.kaboom.model.entity.brain;

import cn.noy.kaboom.model.entity.Mob;

import java.util.PriorityQueue;
import java.util.function.Consumer;

public class Brain {
    private final Mob owner;
    private final PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

    public Brain(Mob owner) {
        this.owner = owner;
    }

    public void addMotivation(double priority, Motivation motivation){
        priorityQueue.add(new Node(priority, motivation));
    }

    public Runnable getAction(){
        for (Node node : priorityQueue) {
            Runnable action = node.motivation.getAction(owner);
            if(action != null) return action;
        }
        return () -> {};
    }

    private record Node(double priority, Motivation motivation) implements Comparable<Node> {
        @Override
        public int compareTo(Node o) {
            return Double.compare(priority, o.priority);
        }
    }
}
