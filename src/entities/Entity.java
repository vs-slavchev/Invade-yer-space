package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.Game;

public abstract class Entity {
	
	protected double x, y, dx, dy;
	protected Game game;
	protected Rectangle collisionRectangle = new Rectangle();
	protected int collisionWidth, collisionHeight;
	
	public Entity( int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Entity(Game game,int x, int y){
		this.game = game;
		this.x = x;
		this.y = y;
	}
	
	public void move(long delta){
		this.x += (delta * this.dx)/1000;
		this.y += (delta * this.dy)/1000;
	}
	
	public void setHorizontalMovement(double dx){
		this.dx = dx;
	}
	public void setVerticalMovement(double dy){
		this.dy = dy;
	}
	
	public int getX(){
		return (int) this.x;
	}
	
	public int getY(){
		return (int) this.y;
	}
	
	public double getHorizontalMovement(){
		return this.dx;
	}
	
	public double getVerticalMovement(){
		return this.dy;
	}
	
	public abstract void draw(Graphics2D g);
	
	//collision detection
	public boolean checkCollisionWith(Entity other){
		this.collisionRectangle.setBounds((int) this.x, (int) this.y, this.collisionWidth, this.collisionHeight );
		return this.collisionRectangle.intersects( other.getCollisionRectangle() );
	}
	
	public Rectangle getCollisionRectangle(){
		this.collisionRectangle.setBounds((int) this.x, (int) this.y, this.collisionWidth, this.collisionHeight );
		return this.collisionRectangle;
	}
	
	public abstract void collidedWith(Entity other);
	
	public void doLogic(){ // implemented in subclasses
	}
	
	
	
}
