package cn.noy.kaboom.model.entity;

public class TestBomb extends AbstractBomb{
    public TestBomb(int timeLeft, int power) {
        super(timeLeft, power);
    }

    @Override
    public void explode() {
        remove();
    }

}
