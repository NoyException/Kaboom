package cn.noy.kaboom.model.entity;

public abstract class LivingEntity extends Entity{
    private double health;
    private double maxHealth;

    public void damage(double damage){
        damage = Math.max(damage, 0);
        health = Math.max(health - damage, 0);
        if(health == 0) setDead();
        onDamaged(damage);
    }

    public void heal(double heal){
        health = Math.min(health + heal, maxHealth);
    }

    public void setHealth(double health) {
        this.health = Math.min(Math.max(health, 0), maxHealth);
    }

    public double getHealth() {
        return health;
    }

    public void setMaxHealth(double maxHealth) {
        if(maxHealth<=0)
            throw new IllegalArgumentException("Max health must be positive");
        this.maxHealth = maxHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setDead(){
        onDeath();
        remove();
    }

    public void onDeath(){}

    public void onDamaged(double damage){}
}
