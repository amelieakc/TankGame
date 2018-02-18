package Tank;
 
import Graphics.LoadGraphics;
import com.sun.javafx.scene.traversal.Direction;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import static java.awt.image.ImageObserver.ABORT;
import java.io.File;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel implements Runnable{
    private Thread thread;
    private Display display;
    Graphics2D g2;
    private BufferStrategy b;
    private Graphics g;
    private KeyControlP1 keyControlP1;
    private KeyControlP2 keyControlP2;
    int score1 = 0, score2 = 0;
    int speed = 1, move = 0;
    int w = 640, h = 480;
    String t;
    private GameBoard gameBoard;
    boolean isDead = false;
    private boolean running = false;
    JLabel printScore1, printScore2;
    
public Game(String t, int w, int h){
    this.w = w;
    this.h = h;
    this.t = t;
    keyControlP1 = new KeyControlP1();
    keyControlP2 = new KeyControlP2();
    gameBoard = new GameBoard();
    printScore1 = new JLabel("Player 1 Score: 0");
    printScore2 = new JLabel("Player 2 Score: 0");
    printScore1.setVisible(true);
    printScore2.setVisible(true);
}

public synchronized void start(){
    if(running)
        return;
    running = true;
    thread = new Thread(this);
    thread.start();
}

public synchronized void stop(){
if(!running)
    return;
running = false;
try{
     thread.join();
 }   catch(InterruptedException e){
 }
}

public void init(){
    display = new Display(t, w, h);
    display.getFrame().addKeyListener(keyControlP1);
    display.getFrame().addKeyListener(keyControlP2);
    try {
        initSound();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
}
int x = 0;

private void update(){
    x += 1;
    keyControlP1.updateProjectiles();
    keyControlP2.updateProjectiles();

    if (score1 > 9) {
        JOptionPane.showMessageDialog(this, "Player 1 Wins!", "Player 1 Wins!", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    } 
    
    if (score2 > 9) {
        JOptionPane.showMessageDialog(this, "Player 2 Wins!", "Player 2 Wins!", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }
}

private void render(){
    b = display.getCanvas().getBufferStrategy();
    if(b == null){
        display.getCanvas().createBufferStrategy(3);
        return;
    }
    g = b.getDrawGraphics();
    //clear
    g.clearRect(0, 0, w, h);
    //draw
    for(int i = 0; i < 480; i+=320){
        for(int j = 0; j < 640; j+=240){
             g.drawImage(LoadGraphics.background, i, j, null);
        }
    }
    
    keyControlP1.paintComponent(g);
    keyControlP2.paintComponent(g);
    keyControlP1.paintProjectiles(g);
    keyControlP2.paintProjectiles(g);
    
    gameBoard.paintWalls(g);
    
    //border walls
    for(int i = 0; i < 640; i+=32){
        g.drawImage(LoadGraphics.wall2, 0, i, null);
        g.drawImage(LoadGraphics.wall2, 608, i, null);
    }
    for(int i = 0; i < 640; i+=32){
        g.drawImage(LoadGraphics.wall2, i, 0, null);
        g.drawImage(LoadGraphics.wall2, i, 448, null);
    }
    
    // 1. check whether a bullet has hit something
    // 2. if it has then draw explo. 
    // 3. increment score of other tank that wasn't hit was bullet
    // 4. once the bullet has hit something, delete that bullet from list
    checkCollision(keyControlP1, keyControlP2, g);
    checkWallCollision(keyControlP1, keyControlP2, gameBoard);
    checkTankWallCollision(keyControlP1, keyControlP2, gameBoard);
    checkTankCollision(keyControlP1, keyControlP2);
    b.show();
    g.dispose();
}

    @Override
    public void run(){
        init();
    //author codenmore
    //https://github.com/CodeNMore/New-Beginner-Java-Game-Programming-Src
    int fps = 60;
    double timePerTick = 1000000000 / fps;
    double delta = 0;
    long now;
    long lastTime = System.nanoTime();
    long timer = 0;
    int ticks = 0;
    
    while(running){
        now = System.nanoTime();
        delta += (now - lastTime) / timePerTick;
        timer += now - lastTime;
        lastTime = now;
        
        if(delta >= 1){
            update();
            render();
            ticks++;
            delta--;
        }   
        if(timer >= 1000000000){
            ticks = 0;
            timer = 0;
        }
    }
    stop();
    }

    public void initSound(){
        /* 
        Source of audio input stream taken from:
        https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
        */
        //stackoverflow
        AudioInputStream stream;
        AudioFormat format;
        DataLine.Info info;
        Clip clip;
        
        try {
            System.out.println(System.getProperty("user.dir"));
            stream = AudioSystem.getAudioInputStream(new File("Resources/TankMusic2.wav"));
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            clip.loop(100);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public KeyControlP1 getKeyControlP1(){
        return keyControlP1;
    }
    
     public KeyControlP2 getKeyControlP2(){
        return keyControlP2;
    }
        
     
    void checkCollision(KeyControlP1 keyControl1, KeyControlP2 keyControl2, Graphics g){
        this.display.getJFrame().getContentPane().add(printScore1, BorderLayout.WEST);
        this.display.getJFrame().getContentPane().add(printScore2, BorderLayout.EAST);
        this.display.getJFrame().pack();
        this.display.getJFrame().setVisible(true);
        
        Bullet bulletToRemove = null;
        for(int i = 0; i < keyControl1.projectiles.size(); i++){
            if(keyControl1.projectiles.get(i).collides(keyControl2.getBounds2D())){
                g.drawImage(LoadGraphics.explosion, keyControl2.getX(), keyControl2.getY(), null);
                score1++;
                printScore1.setText("Player 1 Score: " +score1);
                bulletToRemove = keyControl1.projectiles.get(i);
            }   
        }
        if (bulletToRemove != null) {
            keyControl1.removeProjectile(bulletToRemove);
            bulletToRemove = null;
        }
        for(int i = 0; i < keyControl2.projectiles.size(); i++){
            if(keyControl2.projectiles.get(i).collides(keyControl1.getBounds2D())){
                g.drawImage(LoadGraphics.explosion, keyControl1.getX(), keyControl1.getY(), null);
                score2++;
                printScore2.setText("Player 2 Score: " +score2);
                bulletToRemove = keyControl2.projectiles.get(i);
            }        
        }  
        if (bulletToRemove != null) {
            keyControl2.removeProjectile(bulletToRemove);
        }
    }
    
    void checkWallCollision(KeyControlP1 keyControl1, KeyControlP2 keyControl2, GameBoard gameBoard){
        Wall wallToRemove = null;
        Bullet bulletToRemove = null;
        for(int i = 0; i < keyControl1.projectiles.size(); i++){
            for(int j = 0; j < gameBoard.gameWalls.size(); j++){
                if(keyControl1.projectiles.get(i).collides(gameBoard.gameWalls.get(j).getBounds2D())){
                    g.drawImage(LoadGraphics.explosion, gameBoard.gameWalls.get(j).getX(), gameBoard.gameWalls.get(j).getY(), null);
                    wallToRemove = gameBoard.gameWalls.get(j);
                    bulletToRemove = keyControl1.projectiles.get(i);
                }
            }
        }
        if (wallToRemove != null) {
            gameBoard.removeWall(wallToRemove);
            keyControl1.removeProjectile(bulletToRemove);
        }
        for(int i = 0; i < keyControl2.projectiles.size(); i++){
            for(int j = 0; j < gameBoard.gameWalls.size(); j++){
                if(keyControl2.projectiles.get(i).collides(gameBoard.gameWalls.get(j).getBounds2D())){
                    g.drawImage(LoadGraphics.explosion, gameBoard.gameWalls.get(j).getX(), gameBoard.gameWalls.get(j).getY(), null);
                    wallToRemove = gameBoard.gameWalls.get(j);
                    bulletToRemove = keyControl2.projectiles.get(i);
                }
            }
        }
        if (wallToRemove != null) {
            gameBoard.removeWall(wallToRemove);
            keyControl2.removeProjectile(bulletToRemove);
        }  
    }
    
    void checkTankWallCollision(KeyControlP1 keyControlP1, KeyControlP2 keyControlP2, GameBoard gameboard){
        Wall wallToRemove = null;
        for(int i = 0; i < gameBoard.gameWalls.size(); i++){
            if((keyControlP1.getBounds2D().intersects(gameBoard.gameWalls.get(i).getBounds2D()) || gameBoard.gameWalls.get(i).getBounds2D().intersects(keyControlP1.getBounds2D()))){
                g.drawImage(LoadGraphics.explosion, gameBoard.gameWalls.get(i).getX(), gameBoard.gameWalls.get(i).getY(), null);
                wallToRemove = gameBoard.gameWalls.get(i); 
        }
        if(wallToRemove != null){
            gameBoard.removeWall(wallToRemove);
        }
        }
        
        for(int x = 0; x < gameBoard.gameWalls.size(); x++){
            if((keyControlP2.getBounds2D().intersects(gameBoard.gameWalls.get(x).getBounds2D()) || gameBoard.gameWalls.get(x).getBounds2D().intersects(keyControlP2.getBounds2D()))){
                g.drawImage(LoadGraphics.explosion, gameBoard.gameWalls.get(x).getX(), gameBoard.gameWalls.get(x).getY(), null);
                wallToRemove = gameBoard.gameWalls.get(x); 
        }
        if(wallToRemove != null){
            gameBoard.removeWall(wallToRemove);
            }    
        }   
    }

    
    void checkTankCollision(KeyControlP1 keyControlP1, KeyControlP2 keyControlP2){
        if(keyControlP1.getBounds2D().intersects(keyControlP2.getBounds2D())){
            g.drawImage(LoadGraphics.explosion, keyControlP1.getX(), keyControlP1.getY(), null);
        }
        if(keyControlP2.getBounds2D().intersects(keyControlP1.getBounds2D())){
            g.drawImage(LoadGraphics.explosion, keyControlP2.getX(), keyControlP2.getY(), null);
        }
    }
    
    public static void main(String[] args) {
        Game myGame = new Game("Amelie's Tank Game", 640, 480);
        JOptionPane.showMessageDialog(null, "Welcome to Amelie's Tank Game!", "Welcome!", JOptionPane.PLAIN_MESSAGE);
        myGame.start();
       
    }
}

  