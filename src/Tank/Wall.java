package Tank;
import java.awt.*;

import Graphics.LoadGraphics;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Tank.KeyControlP1;

public class Wall {
    static int w = 32, h = 32;
    protected int x, y;
    protected BufferedImage i;
    
public Wall(BufferedImage i, int x, int y){
    this.i = i; 
    this.x = x; 
    this.y = y;
}
    
public Rectangle getBounds2D() {
    return new Rectangle(x-w, y-h, w, h);
}

public boolean collides(Rectangle rec) {
    return getBounds2D().intersects(rec);
}    

public int getX(){
    return this.x;
}
    
public int getY(){ 
    return this.y;
}

public void setX(int newX){
    this.x = newX;
}

public void setY(int newY){
    this.y = newY;
}
}
