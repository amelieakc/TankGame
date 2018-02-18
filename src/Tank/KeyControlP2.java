
package Tank;
/*source code from plane game*/
import Graphics.LoadGraphics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import Tank.Orientation.Direction;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy; 
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class KeyControlP2 extends JPanel implements KeyListener {;
    int x = 576, y = 208;
    int w = 64, h = 64; 
    private BufferStrategy b; 
    private Display display;
    public Direction direction;
    public ArrayList<Bullet> projectiles = new ArrayList<Bullet>();
    
    public KeyControlP2() {
        x = 576;
        y = 208;
        direction = Direction.Left;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    public int getX() {
        return x;
    }
    
    @Override 
    public int getY() {
        return y;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = computeTransform();
        g2.drawImage(LoadGraphics.tank1, at, null);
        evictBullets();
    }

    public AffineTransform computeTransform() {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        
        int angle = 0;
        
        if (null != direction) switch (direction) {
            case Left:
                angle = 0;
                break;
            case Down:
                angle = -90;
                break;
            case Right:
                angle = 180;
                break;
            case Up:
                angle = -270;
                break;
            default:
                break;
        }
        at.rotate(Math.toRadians(angle));
        at.translate(-32, -32);
        return at;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Key Pressed");
        int code = e.getKeyCode();

        switch(code){
        case KeyEvent.VK_DOWN:
            if(y < 420){
            y+=10;
            direction = Direction.Down;
            break;
            }
        case  KeyEvent.VK_UP:
            if(y > 60){
            y -= 10;
            direction = Direction.Up;
            break;
            }
        case  KeyEvent.VK_LEFT:
            if(x > 60){
            x -= 10;
            direction = Direction.Left;
            break;
            }
        case KeyEvent.VK_RIGHT:
            if(x < 570){
            x += 10;
            direction = Direction.Right;
            break;
            }
        case KeyEvent.VK_ENTER:
            addProjectile(x, y, direction);
            break;
        case KeyEvent.VK_DELETE:
            JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
            System.exit(ABORT);
            break;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
      @Override
    public void keyReleased(KeyEvent e) {
        
    }
        
    public void updateProjectiles() {
        for (Bullet bullet : projectiles) {
            bullet.update();
        }
    }
        
    public void paintProjectiles(Graphics g) {
        for (Bullet bullet : projectiles) {
            bullet.paint(g);
        }
    }
    
    private boolean outOfBounds(Rectangle r) {
        if (r.getX() > 640 || r.getX() < 0 || r.getY() < 0 || r.getY() > 480) {
            return true;
        } else {
            return false;
        }
    }
    
    /*
      Remove the bullets that have gone off screen
    */
    private void evictBullets() {
        ArrayList<Bullet> toRemove = new ArrayList<Bullet>();
        for (Bullet bullet: projectiles) {
            
            if (outOfBounds(bullet.getBounds2D())) {
                toRemove.add(bullet);
            }
        }
        for (Bullet bullet : toRemove) {
            removeProjectile(bullet);
        }
    }
    
    public void addProjectile(int x, int y, Direction ori){
        projectiles.add(new Bullet(x, y, ori));
    }
       
    public void removeProjectile(Bullet b){
       projectiles.remove(b);
    }
    
    public Rectangle getBounds2D() {
		return new Rectangle(x-w, y-h, w, h);
    }

}