package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.Game;

public abstract class Entity {
	
	protected double x, y, dx, dy;
	protected Game game;
	protected Rectangle collisionRectangle = new Rectangle();
	protected int collisionWidth, collisionHeight;
	
	public Entity(final int x, final int y){
		this.x = x;
		this.y = y;
	}
	
	public Entity(Game game,final int x, final int y){
		this.game = game;
		this.x = x;
		this.y = y;
	}
	
	public void move(final long delta){
		x += (delta * dx)/1000;
		y += (delta * dy)/1000;
	}
	
	public boolean checkCollisionWith(Entity other){
		collisionRectangle.setBounds((int) this.x, (int) this.y, this.collisionWidth, this.collisionHeight );
		return collisionRectangle.intersects( other.getCollisionRectangle() );
	}
	
	public Rectangle getCollisionRectangle(){
		collisionRectangle.setBounds((int) x, (int)y, collisionWidth, collisionHeight );
		return collisionRectangle;
	}
	
	public abstract void draw(Graphics2D g);
	
	public abstract void collidedWith(Entity other);
	
	public void doLogic(){ // implemented in subclasses
	}
	
	public void setHorizontalMovement(double dx){
		this.dx = dx;
	}
	public void setVerticalMovement(double dy){
		this.dy = dy;
	}
	
	public int getX(){
		return (int)x;
	}
	
	public int getY(){
		return (int)y;
	}
	
	public double getHorizontalMovement(){
		return dx;
	}
	
	public double getVerticalMovement(){
		return dy;
	}
	
}
