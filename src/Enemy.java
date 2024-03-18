import java.awt.*;
import java.util.ArrayList;
import java.security.SecureRandom;

public class Enemy extends Player implements Creature {
  private TimeHandler tim;
  private TimeHandler tim2;
  private double attackSpeed;

  public Enemy(int x, int y, String imagePath, String projectilePath, int projectileSpeed, double attackSpeed, int health) {
    super(imagePath, projectilePath, projectileSpeed);

    this.x = x;
    this.y = y;

    this.attackSpeed = attackSpeed;

    this.health = health;

    hitbox = new Rectangle(x, y, width, height);

    width = image.getWidth();
    height = image.getHeight();

    speed_x = 1;
    speed_y = 1;

    projectiles = new ArrayList<>();


    timer = new TimeHandler();

    tim = new TimeHandler();

    tim2 = new TimeHandler();

  }


  @Override
  public void move() {
    if(health < 1 ){
      setDead(true);
    }
    attack();
    animate();

      SecureRandom random = new SecureRandom();
      int direction = random.nextInt(4);
      int r_speed = 1 + (random.nextInt(9));
//    int direction = (int) Math.round(Math.random() * 3);
//    int r_speed = (int) Math.round(1 + (Math.random() * 8));

    if (tim2.timeBomb(0.7)) {

      switch (direction) {

        case 0:
          speed_y = r_speed;
          break;
        case 1:
          speed_y = -(r_speed);
          break;
        case 2:
          speed_x = r_speed;
          break;
        case 3:
          speed_x = -(r_speed);
          break;
      }
    }

    if (y < 0 && speed_y < 0) {
      speed_y = r_speed;
    }

    if (y > GameData.BOARD_HEIGHT / 3 && speed_y > 0) {
      speed_y = -(r_speed);
    }

    if (x < 0 && speed_x < 0) {
      speed_x = r_speed;
    }
    if (x > GameData.BOARD_WIDTH - width - 15 && speed_x > 0) {
      speed_x = -(r_speed);
    }
    x += speed_x;
    y += speed_y;

  }


  @Override
  public void attack() {
    SecureRandom random = new SecureRandom();

    if (tim.timeBomb((double) 1 + random.nextDouble() / attackSpeed)) {
      projectiles.add(new Projectile(x + (width / 2), y + height, projectileSpeed, projectilePath));

    }
  }
  @Override
  public void animate() {

    ArrayList<String> paths = new ArrayList<>();

    for (int i = 1; i < 12; i += 1) {

      String temp_path = imagePath;
      temp_path += Integer.toString(i);
      temp_path += ".png";
      paths.add(temp_path);
    }

    if (currentLook > 10) {
      currentLook = 0;
    }
    setImage(paths.get(currentLook));

    //double rand = 0.02 + Math.random() * 0.06;

    SecureRandom rand = new SecureRandom();

    double r = 0.01 + rand.nextDouble() * 0.05;

    if (timer.timeBomb(r)) {
      currentLook++;

    }


  }


}
