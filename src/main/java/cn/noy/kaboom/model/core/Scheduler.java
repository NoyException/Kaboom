package cn.noy.kaboom.model.core;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Scheduler {
    private final Timer timer;
    private long ticks;
    private final LinkedList<Task> tasks = new LinkedList<>();
    private final LinkedList<Task> toAdd = new LinkedList<>();

    public Scheduler() {
        timer = new Timer();
    }

    public void start(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ticks++;
                tasks.forEach(Task::tick);
                tasks.removeIf(Task::isCancelled);
                tasks.addAll(toAdd);
                toAdd.clear();
            }
        }, 0, 20);
    }

    public void stop(){
        timer.cancel();
    }

    public long getTicks() {
        return ticks;
    }

    public Task schedule(Consumer<Task> task, long delay, long period, long times){
        Task task1 = new Task(task, delay, period, times);
        toAdd.addLast(task1);
        return task1;
    }

    public Task schedule(Consumer<Task> task, long delay, long period){
        return schedule(task, delay, period, -1);
    }

    public Task schedule(Consumer<Task> task, long delay){
        return schedule(task, delay, 1, 1);
    }

    public Task schedule(Consumer<Task> task){
        return schedule(task, 0, 1, 1);
    }

    public static class Task{
        private final Consumer<Task> consumer;
        private final long delay;
        private final long period;
        private final long times;
        private long ticks;
        private boolean cancelled;

        public Task(Consumer<Task> consumer, long delay, long period, long times) {
            this.consumer = consumer;
            this.delay = delay;
            this.period = period;
            this.times = times;
            this.ticks = 0;
            this.cancelled = false;
        }

        public void tick() {
            if(cancelled) return;
            long dt = ticks - delay;
            if(dt >= 0 && dt % period == 0){
                if(times > 0 && dt / period >= times){
                    cancel();
                    return;
                }
                consumer.accept(this);
            }
            ticks++;
        }

        public void cancel() {
            this.cancelled = true;
        }

        public boolean isCancelled() {
            return cancelled;
        }
    }
}
