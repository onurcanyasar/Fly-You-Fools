import java.awt.*;
import java.util.ArrayList;

public class Player extends Sprite implements Creature{

  private boolean goLeft;
  private boolean goRight;
  protected int projectileSpeed;
  protected ArrayList<Projectile> projectiles;
  protected int health;
  protected TimeHandler timer;
  protected int currentLook = 0;
  protected String imagePath;
  protected String projectilePath;


  public Player(String imagePath, String projectilePath, int projectileSpeed) {
    this.imagePath = imagePath;
    this.projectilePath = projectilePath;

    setImage(imagePath + "1.png");

    timer = new TimeHandler();
    width = image.getWidth();
    height = image.getHeight();


    x = (GameData.BOARD_WIDTH / 2) - (width / 2);
    y = (GameData.BOARD_HEIGHT - height - 50);


    hitbox = new Rectangle(x, y, width, height);

    projectiles = new ArrayList<>();

    dead = false;

    this.projectileSpeed = projectileSpeed;

    health = 10;


  }


  @Override
  public void move() {
    animate();
    if (health < 1) {
      dead = true;
    }

    if (x <= 0 && isGoLeft() || x + width + 15 >= GameData.BOARD_WIDTH && isGoRight()) {
      stop();
    } else if (isGoLeft() && !isGoRight()) {
      moveLeft();
    } else if (isGoRight() && !isGoLeft()) {
      moveRight();
    } else if (isGoRight() == isGoLeft()) {
      stop();
    }

    x += speed_x;

  }

  @Override
  public void attack() {
    projectiles.add(new Projectile(x + (width / 2), y, projectileSpeed, projectilePath));

  }

  @Override
  public void animate() {
    if (currentLook >= 64) {
      currentLook = 0;
    }

    ArrayList<String> paths = new ArrayList<>();

    for (int i = 1; i < 32; i += 2) {

      String temp_path = imagePath;
      temp_path += Integer.toString(i);
      temp_path += ".png";
      paths.add(temp_path);

    }
    if (currentLook > 15 && currentLook < 32) {
      setImage(paths.get(currentLook % 16));
    } else if (currentLook >= 32) {
      setImage(paths.get(15));
    } else {
      setImage(paths.get(currentLook));
    }


    if (timer.timeBomb(0.019)) {
      currentLook++;

    }


  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public boolean isGoLeft() {
    return goLeft;
  }

  public void setGoLeft(boolean goLeft) {
    this.goLeft = goLeft;
  }

  public boolean isGoRight() {
    return goRight;
  }


  public void setGoRight(boolean goRight) {
    this.goRight = goRight;
  }

  public ArrayList<Projectile> getProjectiles() {
    return projectiles;
  }

  public void setProjectiles(ArrayList<Projectile> projectiles) {
    this.projectiles = projectiles;
  }

  private void stop() {
    setSpeed_x(0);
  }

  private void moveRight() {
    setSpeed_x(7);
  }

  private void moveLeft() {
    setSpeed_x(-7);
  }

  public int getProjectileSpeed() {
    return projectileSpeed;
  }

  public void setProjectileSpeed(int projectileSpeed) {
    this.projectileSpeed = projectileSpeed;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

}
