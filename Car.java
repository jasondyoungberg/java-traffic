import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

public class Car {
	private static final int BASE_SPEED = 10;
	private static final int SPEED_VARIATION = 3;

	private Cord path[];
	private Cord pos;
	private Color color;
	private int speed;

	// 1:right, -1:left
	private int dir;

	public Car(Cord[] path,Color color){
		this.path = path;
		this.pos = new Cord(path[0]);
		this.color = color;

		this.dir = (path[0].getX() < path[3].getX()) ? 1 : -1;

		int rand = (int)Math.floor((2*SPEED_VARIATION+1)*Math.random()-SPEED_VARIATION);
		this.speed = rand + BASE_SPEED;
	}

	public void draw(Graphics g){
		g.setColor(color);
		if(dir == 1){
			g.fillRect(pos.getX()-30,pos.getY()-25,45,50);
			g.fillOval(pos.getX()-10,pos.getY()-25,50,50);
			g.fillOval(pos.getX()-40,pos.getY()-25,20,50);
		}else{
			g.fillRect(pos.getX()-15,pos.getY()-25,45,50);
			g.fillOval(pos.getX()-40,pos.getY()-25,50,50);
			g.fillOval(pos.getX()+20,pos.getY()-25,20,50);
		}
	}

	public boolean tick(ArrayList<Car> others,int stoplight){
		boolean canMove = true;

		if(pos.between(path[1],path[2]) && stoplight < 2)canMove = false;
		for(Car other:others){
			if(this != other && dir == other.dir){
				if(dir>0){
					if(other.pos.getX() - 100 < pos.getX() &&
					other.pos.getX() > pos.getX())canMove = false;
				}else{
					if(other.pos.getX() + 100 > pos.getX() &&
					other.pos.getX() < pos.getX())canMove = false;
				}
			}
		}

		if(canMove)pos.translate(new Cord(speed * dir,0));
		return !pos.between(path[0],path[3]);
	}

	public String toString(){
		return "Car @" + pos;
	}
}
