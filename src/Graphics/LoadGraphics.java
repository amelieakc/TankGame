
package Graphics;
//author: codenmore
import java.awt.image.BufferedImage;
import Graphics.Sprite;
import static Graphics.LoadImage.*;

public class LoadGraphics {
  
    static Sprite sheet = new Sprite(loadImage("/Sprite.png"));
    public static BufferedImage tank1 = sheet.cut(384, 64, 64, 64);
    public static BufferedImage tank2 = sheet.cut(384, 64, 64, 64);
    public static BufferedImage wall1 = sheet.cut(384, 128, 32, 32);
    public static BufferedImage wall2 = sheet.cut(416, 128, 32, 32);
    public static BufferedImage rocket = sheet.cut(416,190, 25, 17);
    public static BufferedImage bullet = sheet.cut(384, 192, 32, 20);
    public static BufferedImage explosion = sheet.cut(320, 192, 30, 30);
    public static BufferedImage explosion2 = sheet.cut(320, 0, 64, 64);
    public static BufferedImage background = sheet.cut(0, 0, 320, 240);
}
