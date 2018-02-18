
package Tank;

import Graphics.*;
import Tank.Orientation.Direction;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
    private int x; 
    private int y;
    static final int w = 15, h = 15;
    BufferedImage rocket;
    BufferedImage bullet;
    private Direction direction;
    private static int speed = 15;
    
    
    public Bullet(int x, int y, Direction direction){
        this.y = y;
        this.x = x;
        this.direction = direction;
    }
    
    public void paint(Graphics g){
        g.drawImage(LoadGraphics.bullet, (int) x, (int) y, null);
    }
    
    public void update(){
        if(direction == Direction.Up){
            y -= speed;
        } else if(direction == Direction.Down){
            y += speed;
        } else if(direction == Direction.Left){
            x -= speed;
        } else if(direction == Direction.Right)
            x += speed;
        }
    
    public Rectangle getBounds2D() {
		return new Rectangle(x-w, y-h, w, h);
	}
    
    public boolean collides(Rectangle rec){
        return getBounds2D().intersects(rec);
    }    
}