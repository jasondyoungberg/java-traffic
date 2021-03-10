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

		// random value from -SPEED_VARIATION to SPEED_VARIATION
		int rand = (int)Math.floor((2*SPEED_VARIATION+1)*Math.random()-SPEED_VARIATION);
		this.speed = rand + BASE_SPEED;
	}

	public Cord getPos(){
		return pos;
	}

	// draw car
	public void draw(Graphics g){
		g.setColor(color);
		if(dir == 1){
			g.fillRect(pos.screenX()-30,pos.screenY()-25,45,50);
			g.fillOval(pos.screenX()-10,pos.screenY()-25,50,50);
			g.fillOval(pos.screenX()-40,pos.screenY()-25,20,50);
		}else{
			g.fillRect(pos.screenX()-15,pos.screenY()-25,45,50);
			g.fillOval(pos.screenX()-40,pos.screenY()-25,50,50);
			g.fillOval(pos.screenX()+20,pos.screenY()-25,20,50);
		}
		g.setColor(new Color(0,0,0));
		if(dir == 1){
			g.fillRect(pos.screenX()-30,pos.screenY()-20,40,40);
			g.fillOval(pos.screenX()-5,pos.screenY()-20,40,40);
			g.fillOval(pos.screenX()-35,pos.screenY()-20,10,40);
		}else{
			g.fillRect(pos.screenX()-15,pos.screenY()-20,45,40);
			g.fillOval(pos.screenX()-35,pos.screenY()-20,40,40);
			g.fillOval(pos.screenX()+25,pos.screenY()-20,10,40);
		}
	}

	// calculate movement each frame
	// delete car if returns true
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
		return "Car " + pos;
	}
}
