package cn.noy.kaboom.model.event;

import cn.noy.kaboom.model.entity.Bomb;

public class BombExplodeEvent extends BombEvent implements Cancelable{
    private int power;
    private boolean canceled = false;

    public BombExplodeEvent(Bomb bomb) {
        super(bomb);
        this.power = bomb.getPower();
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
