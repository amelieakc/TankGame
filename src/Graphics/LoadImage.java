package Graphics;
//author: codenmore
//https://github.com/CodeNMore/New-Beginner-Java-Game-Programming-Src
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoadImage {
    public static BufferedImage loadImage(String path){
        try{
            return ImageIO.read(LoadImage.class.getResource(path));
        } catch(IOException e){
            System.exit(1);
        }
        return null;
    }
}
