import java.util.*;

import java.awt.Color;
import java.awt.Dimension;
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
		private final Cord CAR_PATHS[][] = {
			{
				new Cord(-100,353),
				new Cord(300,353),
				new Cord(350,353),
				new Cord(900,353)
			},
			{
				new Cord(900,247),
				new Cord(500,247),
				new Cord(450,247),
				new Cord(-100,247)
			}
		};
		private final Cord PERSON_PATHS[][] = {
			{
				new Cord(385,-100),
				new Cord(385,180),
				new Cord(385,185),
				new Cord(385,700)
			},
			{
				new Cord(415,700),
				new Cord(415,420),
				new Cord(415,415),
				new Cord(415,-100)
			}
		};

	// Color setup
		private final Color BACKGROUND_COLOR = new Color(0,255,0);
		private final Color ROAD_COLOR = new Color(51,51,51);
		private final Color LINE_COLOR = new Color(255,255,0);
		private final Color PATH_COLOR = new Color(153,153,153);
		private final Color CROSSWALK_COLOR = new Color(255,255,255);
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

	public void paintComponent(Graphics g){
		t++;
		{// Handle Stoplight
			if(t%1000 == 0)stoplight = 0;
			if(t%1000 == 50)stoplight = 2;
			if(t%1000 == 400)stoplight = 1;
			if(t%1000 == 500)stoplight = 0;
			if(t%1000 == 550)stoplight = -2;
			if(t%1000 == 900)stoplight = -1;
		}
		{// Draw Area
			{// Draw Background
				g.setColor(BACKGROUND_COLOR);
				g.fillRect(0,0,800,600);
			}
			{// Draw Path
				g.setColor(PATH_COLOR);
				g.fillRect(370,0,60,600);
			}
			{// Draw Road
				g.setColor(ROAD_COLOR);
				g.fillRect(0,200,800,200);
			}
			{// Draw Lines
				g.setColor(LINE_COLOR);
				g.fillRect(0,291,800,6);
				g.fillRect(0,303,800,6);
			}
			{// Draw Crosswalk
				g.setColor(CROSSWALK_COLOR);
				for(int i=211;i<400;i+=27){
					g.fillRect(370,i,60,16);
				}
			}
		}
		{// Spawning
			if(t%SPAWN_RATE == 0){
				double rand = Math.random() * SPAWN_CHANCES;
				if(rand < SPAWN_CAR){
					Cord randPath[] = CAR_PATHS[(int)(Math.random() * CAR_PATHS.length)];
					Color randColor = CAR_COLORS[(int)(Math.random() * CAR_COLORS.length)];
					cars.add(new Car(randPath,randColor));
				}else if(rand < SPAWN_CAR + SPAWN_PERSON){
					Cord randPath[] = PERSON_PATHS[(int)(Math.random() * PERSON_PATHS.length)];
					Color randColor = PERSON_COLORS[(int)(Math.random() * PERSON_COLORS.length)];
					people.add(new Person(randPath,randColor));
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
			if(stoplight<=0){
				g.setColor(STOPLIGHT_RED_COLOR);
			}else if(stoplight == 1){
				g.setColor(STOPLIGHT_YELLOW_COLOR);
			}else{
				g.setColor(STOPLIGHT_GREEN_COLOR);
			}
			g.fillRect(375,285,50,30);

			if(stoplight>=0){
				g.setColor(STOPLIGHT_RED_COLOR);
			}else if(stoplight == -1){
				g.setColor(STOPLIGHT_YELLOW_COLOR);
			}else{
				g.setColor(STOPLIGHT_GREEN_COLOR);
			}
			g.fillRect(385,275,30,50);

			g.setColor(STOPLIGHT_BODY_COLOR);
			g.fillRect(380,280,40,40);
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
