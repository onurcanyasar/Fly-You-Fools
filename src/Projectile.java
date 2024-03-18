import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Projectile extends Sprite {
  private TimeHandler timeHandler;

  public Projectile(int x, int y, int speed, String path) {
    this.x = x;
    this.y = y;
    
    try {
      image = ImageIO.read(new File(path));
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    width = image.getWidth();
    height = image.getHeight();

    hitbox = new Rectangle(x, y, width, height);

    timeHandler = new TimeHandler();

    dead = false;


    speed_y = speed;

  }

  @Override
  public void move() {
    y -= speed_y;
    if (y < -30) {
      dead = true;
    }

  }

  public void move_e() {
    y += speed_y;
    if (y > GameData.BOARD_HEIGHT) {
      dead = true;
    }

  }

  @Override
  public void drawImage(Graphics2D g) {
    super.drawImage(g);

  }



  public TimeHandler getTimeHandler() {
    return timeHandler;
  }

}
