package cn.noy.kaboom.model.event;

public interface Cancelable {
    boolean isCanceled();
    void setCanceled(boolean canceled);
}
