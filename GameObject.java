import java.awt.Graphics;
import java.awt.Color;

public abstract class GameObject {
	public Vector2 position = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0);
	public Color color;
	public boolean canBeHit = false;
	public boolean wallBounce = false;
	public float radius = 0; //used exclusivy for circles and hit-able objects
	public boolean deleteAfterTick = false;
	
	public abstract void draw(Graphics g);

}


