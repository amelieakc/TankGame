
package Tank;

import Graphics.LoadGraphics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import Tank.KeyControlP1;
import Tank.KeyControlP2;

public class GameBoard {
    public int x, y, w = 640, h = 480;
    
    public ArrayList<Wall> gameWalls = new ArrayList<Wall>();
        
    BufferedImage bi[] = {LoadGraphics.wall1, LoadGraphics.wall2};    

    private void createGameBoard() {
        // block configuration
        for(int i = 32; i < 443; i+=32){
            addWall(bi[1], 160, i);
            addWall(bi[0], 288, i);
            addWall(bi[0], 320, i);
            addWall(bi[1], 448, i);
        }
    
        for(int i = 192; i < 288; i+=32){
            addWall(bi[1], i, 96);
            addWall(bi[1], i, 128);
            addWall(bi[1], i, 224);
            addWall(bi[1], i, 320);
            addWall(bi[1], i, 352);
        }
        for(int i = 352; i < 448; i+=32){
            addWall(bi[1], i, 96);
            addWall(bi[1], i, 128);
            addWall(bi[1], i, 224);
            addWall(bi[1], i, 320);
            addWall(bi[1], i, 352);
        }
    }
    
    public GameBoard(){

        createGameBoard();
    }

    private void paintWall(Graphics g, Wall b) {
        g.drawImage(b.i, b.getX(), b.getY(), null);
    }
    
    public void paintWalls(Graphics g) {
        for (Wall b : gameWalls) {
            paintWall(g, b);
        }
    }

    public void addWall(BufferedImage i, int x, int y){
        gameWalls.add(new Wall(i, x, y));
    }

    public void removeWall(Wall b){
        gameWalls.remove(b);
    }
}
  
