import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Sprite {

  protected BufferedImage image;
  protected boolean dead;

  protected int x;
  protected int y;

  protected int speed_x;
  protected int speed_y;

  protected int height;
  protected int width;

  protected Rectangle hitbox;

  public abstract void move();

  public void setImage(String path) {

    try {
      this.image = ImageIO.read(new File(path));

    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  public BufferedImage getImage() {

    return image;
  }

  public void setX(int x) {

    this.x = x;
  }

  public void setY(int y) {

    this.y = y;
  }

  public int getY() {

    return y;
  }

  public int getX() {

    return x;
  }

  public int getSpeed_x() {
    return speed_x;
  }

  public void setSpeed_x(int speed_x) {
    this.speed_x = speed_x;
  }

  public int getSpeed_y() {
    return speed_y;
  }

  public void setSpeed_y(int speed_y) {
    this.speed_y = speed_y;
  }

  public void setDead(boolean dying) {

    this.dead = dying;
  }

  public boolean isDead() {
    return dead;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public Rectangle getHitbox() {
    return new Rectangle(x, y, width, height);
  }

  public void setHitBox(Rectangle hitbox) {
    this.hitbox = hitbox;
  }


  public void drawImage(Graphics2D g) {
    g.drawImage(image, x, y, null);
  }


}
