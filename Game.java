
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;


public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1200, HEIGHT = WIDTH * 3/4;  //for window size
	private static final int width = WIDTH, height = HEIGHT - 25 ; //the -25 is for the bar at the top of the window
	private final int ticksPerSecond = 60;
	private Thread thread;
	private boolean running = false;
	private long currentTick = 0;
	Graphics g;

	ArrayList<GameObject> objects = new ArrayList<GameObject>();

	public Game() {
		new Window(WIDTH, HEIGHT, "Meteor Defense", this);
	}

	public void print(String text) {
		System.out.println(text);
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void run()
	{
		float msPerTick = 1000/ticksPerSecond;
		long startTime = System.currentTimeMillis();
		render();
		setUp();
		render();

		while(running)
		{
			if(System.currentTimeMillis() - startTime > currentTick * (long)msPerTick) {
				currentTick++;
				bodyPhysics();
				tick();
				//print(""+objects.size());
			}
			render();
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		for(int i = 0; i < objects.size(); i++) 
			objects.get(i).draw(g);

		g.dispose();
		bs.show();
	}

	public void setUp() { 

		Circle newCircle = new Circle();
		Circle newCircle1 = new Circle();
		
		newCircle.position = new Vector2(width-60,height-60);
		newCircle.radius = 60;
		//newCircle.velocity = new Vector2((float)Math.random() * 20 - 10f, (float)Math.random() * 20 - 10f);
		newCircle.color = Color.WHITE;
		newCircle.wallBounce = true;
		newCircle.canBeHit = true;

		objects.add(newCircle);

		newCircle1.position = new Vector2(700,700);
		newCircle1.radius = 60;
		newCircle1.velocity = new Vector2((float)Math.random() * 20 - 10f, (float)Math.random() * 20 - 10f);
		newCircle1.color = Color.WHITE;
		newCircle1.wallBounce = true;
		newCircle1.canBeHit = true;

		objects.add(newCircle1);

	}

	private void tick() {
		detectOffScreenObjects();
		deleteObjects();
		detectCollision();
		//spawnParticles();

	}

	private void bodyPhysics() {
		for(int i = 0; i < objects.size(); i++) {
			objects.get(i).position.x += objects.get(i).velocity.x;
			objects.get(i).position.y -= objects.get(i).velocity.y;
		}
	}

	private void detectOffScreenObjects() {
		float distanceFromCenter = 2000;
		for(int i = 0; i < objects.size(); i++) {
			double objectDistance1 = Math.pow((double)((objects.get(i).position.x) - (width/2)) , 2) + Math.pow((double)((objects.get(i).position.y) - (height/2)) , 2);
			double objectDistance2 = Math.sqrt(objectDistance1);
			if(objectDistance2 > distanceFromCenter) {
				objects.get(i).deleteAfterTick = true;
			}
		}
	}

	private void detectCollision() {
		//print(""+objects.size());
		for(int i = 0; i < objects.size(); i++){
			for(int j = i + 1; j < objects.size(); j++) {
				if(objects.get(i).canBeHit && objects.get(j).canBeHit) {
					
					double distanceFromEachOther1 = Math.pow((double)((objects.get(i).position.x) - objects.get(j).position.x) , 2) + Math.pow((double)((objects.get(i).position.y) - objects.get(j).position.y) , 2);
					double distanceFromEachOther2 = Math.sqrt(distanceFromEachOther1);

					if(objects.get(i).radius + objects.get(j).radius >= distanceFromEachOther2) {
						
						
						print("Collision Detected!");
						objects.get(i).deleteAfterTick = true;
						objects.get(j).deleteAfterTick = true;
					}
				}
			}

			if(objects.get(i).wallBounce) {
				double objectPosX = objects.get(i).position.x;
				double objectPosY = objects.get(i).position.y;
				if(objectPosX - objects.get(i).radius <= 0 || objectPosX + objects.get(i).radius >= width) {
					objects.get(i).velocity.x = -1 * objects.get(i).velocity.x;
					//print("X Reflect");
				}
				if(objectPosY - objects.get(i).radius <= 0 || objectPosY + objects.get(i).radius >= height) {
					objects.get(i).velocity.y = -1 * objects.get(i).velocity.y;
					//print("Y Reflect");
				}
			}
		}
	}

	public void spawnParticles() {
		for(int i = 0; i < 5; i++) {
			Circle newCircle = new Circle();
			newCircle.position = new Vector2(width/2, height/2);
			newCircle.radius = (int)(Math.random()*2)+1;
			newCircle.velocity = new Vector2((float)Math.random() * 20 - 10f, (float)Math.random() * 20 - 10f);
			newCircle.color = Color.WHITE;
			newCircle.wallBounce = true;

			objects.add(newCircle);
			//print(""+objects.size());
		}
	}

	public void deleteObjects() {
		for(int i = objects.size() - 1; i >= 0 ; i--) { //runs threw the gameObject array backwards
			if(objects.get(i).deleteAfterTick) {
				objects.remove(i);
				print("Object Deleted");
			}
		}
	}

	public void testCollision() {
		int numberOfTests = 8;
		int counter = 0;
		for(int i = 0; i < objects.size(); i++) {
			if(objects.get(i).canBeHit) {
				counter++;
			}
		}
		print(""+(numberOfTests - counter));
		if(counter != numberOfTests) {
			for(int j = 0; j < (numberOfTests - counter); j++) {
				Circle newCircle = new Circle();
				newCircle.position = new Vector2(500, 500);   //(float)Math.random() * width, (float)Math.random() * height);
				newCircle.radius = 30; //(int)(Math.random()*30) + 10;
				newCircle.velocity = new Vector2((float)Math.random() * 20 - 10f, (float)Math.random() * 20 - 10f);
				newCircle.color = Color.WHITE;
				newCircle.canBeHit = true;
				newCircle.wallBounce = true;
			}
		}
	}
}