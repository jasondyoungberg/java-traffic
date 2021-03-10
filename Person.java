import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

public class Person {
	private static final int BASE_SPEED = 3;
	private static final int SPEED_VARIATION = 1;

	private Cord path[];
	private Cord pos;
	private Color color;
	private int speed;

	// -1:up 1:down
	private int dir;

	public Person(Cord[] path,Color color){
		this.path = path;
		this.pos = new Cord(path[0]);
		this.color = color;

		this.dir = (path[0].getY() < path[3].getY()) ? 1 : -1;

		int rand = (int)Math.floor((2*SPEED_VARIATION+1)*Math.random()-SPEED_VARIATION);
		this.speed = rand + BASE_SPEED;
	}

	public void draw(Graphics g){
		g.setColor(color);
		g.fillOval(pos.getX()-10,pos.getY()-10,20,20);
	}

	public boolean tick(ArrayList<Person> others,int stoplight){
		boolean canMove = true;

		if(pos.between(path[1],path[2]) && stoplight > -2)canMove = false;
		for(Person other:others){
			if(this != other && dir == other.dir){
				if(dir>0){
					if(other.pos.getY() - 30 < pos.getY() &&
					other.pos.getY() > pos.getY())canMove = false;
				}else{
					if(other.pos.getY() + 30 > pos.getY() &&
					other.pos.getY() < pos.getY())canMove = false;
				}
			}
		}

		if(canMove)pos.translate(new Cord(0,dir * speed));
		return !pos.between(path[0],path[3]);
	}

	public String toString(){
		return "Person @" + pos;
	}
}
