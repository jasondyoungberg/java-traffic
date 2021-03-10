public class Cord {
	private int x,y;

	public Cord(int x,int y){
		this.x = x;
		this.y = y;
	}

	public Cord(Cord other){
		this.x = other.x;
		this.y = other.y;
	}

	public void translate(Cord offset){
		this.x += offset.x;
		this.y += offset.y;
	}

	public double dist(Cord other){
		int xDiff = Math.abs(this.x-other.x);
		int yDiff = Math.abs(this.y-other.y);
		return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
	}

	public boolean between(Cord a,Cord b){
		boolean withinX = (a.x <= x && x <= b.x) || (a.x >= x && x >= b.x);
		boolean withinY = (a.y <= y && y <= b.y) || (a.y >= y && y >= b.y);
		return withinX && withinY;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public String toString(){
		return "("+x+","+y+")";
	}
}