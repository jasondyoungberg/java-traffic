import java.util.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class TrafficAnimation extends JPanel {
	private final int DELAY = 1000/60; //60 fps

	private int t = 0;
	private ArrayList<Car> cars = new ArrayList<Car>();
	private ArrayList<Person> people = new ArrayList<Person>();

	public static int width = 800;
	public static int height = 600;

	private int stoplight = 0;
	/*	  |people|  car |
		-2|green |  red |
		-1|yellow|  red |
		 0|  red |  red |
		 1|  red |yellow|
		 2|  red | green|*/
	// -2,-1,0,2,1,0
 
	// Spawning variables
		private final int SPAWN_RATE = 30;
		private final int SPAWN_CAR = 2;
		private final int SPAWN_PERSON = 1;
		private final int SPAWN_CHANCES = 5;

	// Possible Paths
		private Cord CAR_PATHS[][] = {
			{
				new Cord(-500,53),
				new Cord(-100,53),
				new Cord(-50,53),
				new Cord(500,53)
			},
			{
				new Cord(500,-53),
				new Cord(100,-53),
				new Cord(50,-53),
				new Cord(-500,-53)
			}
		};
		private Cord PERSON_PATHS[][] = {
			{
				new Cord(-15,-400),
				new Cord(-15,-120),
				new Cord(-15,-115),
				new Cord(-15,400)
			},
			{
				new Cord(15,400),
				new Cord(15,120),
				new Cord(15,115),
				new Cord(15,-400)
			}
		};

	// Color setup
		private final Color BACKGROUND_COLOR = new Color(0,255,0);
		private final Color ROAD_COLOR = new Color(51,51,51);
		private final Color CURB_COLOR = new Color(102,102,102);
		private final Color LINE_COLOR = new Color(255,255,0);
		private final Color PATH_COLOR = new Color(153,153,153);
		private final Color CROSSWALK_COLOR = new Color(255,255,255);
		private final Color TEXT_COLOR = new Color(0,0,0);
		private final Color PERSON_COLORS[] = {
			new Color(204,102,102),
			new Color(204,204,102),
			new Color(102,204,102),
			new Color(102,204,204),
			new Color(102,102,204),
			new Color(204,102,204)
		};
		private final Color CAR_COLORS[] = {
			new Color(204,102,102),
			new Color(204,204,102),
			new Color(102,204,102),
			new Color(102,204,204),
			new Color(102,102,204),
			new Color(204,102,204)
		};
		private final Color STOPLIGHT_BODY_COLOR = new Color(102,102,102);
		private final Color STOPLIGHT_RED_COLOR = new Color(204,0,0);
		private final Color STOPLIGHT_YELLOW_COLOR = new Color(204,204,0);
		private final Color STOPLIGHT_GREEN_COLOR = new Color(0,204,0);

	public void setupPaths(int w,int h){
		CAR_PATHS[0][0] = new Cord(-(w/2) - 100,53);
		CAR_PATHS[0][3] = new Cord((w/2) + 100,53);
		CAR_PATHS[1][0] = new Cord((w/2) + 100,-53);
		CAR_PATHS[1][3] = new Cord(-(w/2) - 100,-53);

		PERSON_PATHS[0][0] = new Cord(-15,-(h/2) - 100);
		PERSON_PATHS[0][3] = new Cord(-15,(h/2) + 100);
		PERSON_PATHS[1][0] = new Cord(15,(h/2) + 100);
		PERSON_PATHS[1][3] = new Cord(15,-(h/2) - 100);
	}

	public void paintComponent(Graphics g){
		t++;
		int w = getWidth();
		int h = getHeight();

		if(w != width || h != height){
			// if use has resized window fix up paths
			setupPaths(w,h);
			width = w;
			height = h;
		}

		{// Handle Stoplight
			if(t%1000 == 0)stoplight = 0;
			if(t%1000 == 50)stoplight = 2;
			if(t%1000 == 400)stoplight = 1;
			if(t%1000 == 500)stoplight = 0;
			if(t%1000 == 550)stoplight = -2;
			if(t%1000 == 900)stoplight = -1;
		}
		{// Draw Area
			// Draw Background
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0,0,w,h);

			// Draw Curb
			g.setColor(CURB_COLOR);
			g.fillRect(0,(h/2) - 110,w,220);
			
			// Draw Path
			g.setColor(PATH_COLOR);
			g.fillRect((w/2) - 30,0,60,h);
			
			// Draw Road
			g.setColor(ROAD_COLOR);
			g.fillRect(0,(h/2) - 100,w,200);
			
			// Draw Lines
			g.setColor(LINE_COLOR);
			g.fillRect(0,(h/2) - 9,w,6);
			g.fillRect(0,(h/2) + 3,w,6);
			
			// Draw Crosswalk
			g.setColor(CROSSWALK_COLOR);
			for(int i = (h/2) - 89;i < (h/2) + 100;i += 27){
				g.fillRect((w/2) - 30,i,60,16);
			}
			
		}
		{// Spawning
			if(t%SPAWN_RATE == 0){
				double rand = Math.random() * SPAWN_CHANCES;
				if(rand < SPAWN_CAR){
					// pick random path and color
					Cord randPath[] = CAR_PATHS[(int)(Math.random() * CAR_PATHS.length)];
					Color randColor = CAR_COLORS[(int)(Math.random() * CAR_COLORS.length)];

					// find closest car to spawn location
					double minDist = Double.MAX_VALUE;
					for(Car car:cars){
						double dist = car.getPos().dist(randPath[0]);
						if(dist<minDist)minDist = dist;
					}

					// only spawn if car is far enough away
					if(minDist>100)cars.add(new Car(randPath,randColor));
				}else if(rand < SPAWN_CAR + SPAWN_PERSON){
					// pick random path and color
					Cord randPath[] = PERSON_PATHS[(int)(Math.random() * PERSON_PATHS.length)];
					Color randColor = PERSON_COLORS[(int)(Math.random() * PERSON_COLORS.length)];

					// find closest person to spawn location
					double minDist = Double.MAX_VALUE;
					for(Person person:people){
						double dist = person.getPos().dist(randPath[0]);
						if(dist<minDist)minDist = dist;
					}

					// only spawn if person is far enough away
					if(minDist>50)people.add(new Person(randPath,randColor));
				}
			}
		}
		{// Calculate Objects
			int i = 0;
			while(i < cars.size()){
				if(cars.get(i).tick(cars,stoplight)){
					cars.remove(i);
				}else{i++;}
			}

			i = 0;
			while(i < people.size()){
				if(people.get(i).tick(people,stoplight)){
					people.remove(i);
				}else{i++;}
			}
		}
		{// Draw Objects
			for(Car car:cars){car.draw(g);}
			for(Person person:people){person.draw(g);}
		}
		{// Draw Traffic Light
			// Light for cars
			if(stoplight<=0){
				g.setColor(STOPLIGHT_RED_COLOR);
			}else if(stoplight == 1){
				g.setColor(STOPLIGHT_YELLOW_COLOR);
			}else{
				g.setColor(STOPLIGHT_GREEN_COLOR);
			}
			g.fillRect((w/2) - 25,(h/2) - 15,50,30);

			// Light for people
			if(stoplight>=0){
				g.setColor(STOPLIGHT_RED_COLOR);
			}else if(stoplight == -1){
				g.setColor(STOPLIGHT_YELLOW_COLOR);
			}else{
				g.setColor(STOPLIGHT_GREEN_COLOR);
			}
			g.fillRect((w/2) - 15,(h/2) - 25,30,50);

			// Main body
			g.setColor(STOPLIGHT_BODY_COLOR);
			g.fillRect((w/2) - 20,(h/2) - 20,40,40);
		}
		{// Draw Text
			g.setColor(TEXT_COLOR);
			g.setFont(new Font("Roboto",Font.BOLD,24));
			g.drawString("Can I go?",12,12);
			g.drawString("Vehicles: " + (stoplight == 2 ? "yes" : stoplight == 1 ? "maybe" : "no"),12,36);
			g.drawString("Pedestrians: " + (stoplight == -2 ? "yes" : stoplight == -1 ? "maybe" : "no"),12,60);
		}
		Toolkit.getDefaultToolkit().sync();
	}
	public static void main(String[] args){
		JFrame frame = new JFrame ("Traffic Animation");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new TrafficAnimation());
		frame.pack();
		frame.setVisible(true);
	}
	public TrafficAnimation(){
		setPreferredSize(new Dimension(800,600));
		this.setDoubleBuffered(true);
		startAnimation();
	}
	private void startAnimation(){
		ActionListener timerListener = new TimerListener();
		Timer timer = new Timer(DELAY,timerListener);
		timer.start();
	}
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			repaint();
		}
	}
}
