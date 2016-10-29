package entities;

import main.Game;

import java.awt.*;

public abstract class Entity {

    protected double x, y, dx, dy;
    protected Game game;
    protected int collisionWidth, collisionHeight;

    public Entity(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Entity(Game game, final int x, final int y) {
        this.game = game;
        this.x = x;
        this.y = y;
    }

    public void move(final long delta) {
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    }

    public boolean checkCollisionWith(Entity other) {
        return getCollisionRectangle().intersects(other.getCollisionRectangle());
    }

    public Rectangle getCollisionRectangle() {
        return new Rectangle((int) this.x, (int) this.y,
                this.collisionWidth, this.collisionHeight);
    }

    public abstract void draw(Graphics2D g);

    public abstract void collidedWith(Entity other);

    public abstract void doLogic();

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public double getDeltaX() {
        return dx;
    }

    public void setDeltaX(double dx) {
        this.dx = dx;
    }
}
