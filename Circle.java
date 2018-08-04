import java.awt.Graphics;


public class Circle extends GameObject {

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)position.x - (int)radius , (int)position.y - (int)radius, (int)radius*2, (int)radius*2);
		
	}
}
